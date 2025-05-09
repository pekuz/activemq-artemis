/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.activemq.artemis.tests.compatibility.base;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public abstract class VersionedBase extends ClasspathBase {

   protected final String server;
   protected final String sender;
   protected final String receiver;

   protected final ClassLoader serverClassloader;
   protected final ClassLoader senderClassloader;
   protected final ClassLoader receiverClassloader;

   public VersionedBase(String server, String sender, String receiver) throws Exception {
      if (server == null) {
         server = sender;
      }
      this.server = server;
      this.sender = sender;
      this.receiver = receiver;
      this.serverClassloader = getClasspath(server);
      this.senderClassloader = getClasspath(sender);
      this.receiverClassloader = getClasspath(receiver);
      clearGroovy(senderClassloader);
      clearGroovy(receiverClassloader);
      clearGroovy(serverClassloader);
   }

   protected static List<Object[]> combinatory(Object[] rootSide, Object[] sideLeft, Object[] sideRight) {
      return combinatory(null, rootSide, sideLeft, sideRight);
   }

   protected static List<Object[]> combinatory(Object required,
                                               Object[] rootSide,
                                               Object[] sideLeft,
                                               Object[] sideRight) {
      LinkedList<Object[]> combinations = new LinkedList<>();

      addCombinations(combinations, required, rootSide, sideLeft, sideRight);

      return combinations;
   }

   protected static void addCombinations(List<Object[]> combinations,
                                       Object required,
                                       Object[] rootSide,
                                       Object[] sideLeft,
                                       Object[] sideRight) {
      for (Object root : rootSide) {
         for (Object left : sideLeft) {
            for (Object right : sideRight) {
               if (required == null || root.equals(required) || left.equals(required) || right.equals(required)) {
                  combinations.add(new Object[]{root, left, right});
               }
            }
         }
      }
   }

   public void startServer(File folder, ClassLoader loader, String serverName) throws Throwable {
      startServer(folder, loader, serverName, null);
   }

   public void startServer(File folder, ClassLoader loader, String serverName, String globalMaxSize) throws Throwable {
      startServer(folder, loader, serverName, globalMaxSize, false);

   }

   public void startServer(File folder,
                           ClassLoader loader,
                           String serverName,
                           String globalMaxSize,
                           boolean setAddressSettings) throws Throwable {
      folder.mkdirs();

      String scriptToUse;
      if (getServerScriptToUse() != null && !getServerScriptToUse().isEmpty()) {
         scriptToUse = getServerScriptToUse();
      } else if (server.startsWith("ARTEMIS")) {
         scriptToUse = "servers/artemisServer.groovy";
      } else {
         scriptToUse = "servers/hornetqServer.groovy";
      }

      startServer(folder, loader, serverName, globalMaxSize, setAddressSettings, scriptToUse, server, sender, receiver);
   }

   public String getServerScriptToUse() {
      return null;
   }
}
