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
package org.objectweb.jtests.jms.framework;

import java.util.Properties;

/**
 * Class used to provide configurable options in a convenient way
 */
public class TestConfig {

   // name of the configuration file
   private static final String PROP_FILE_NAME = "test.properties";

   // name of the timeout property
   private static final String PROP_NAME = "timeout";

   /**
    * timeout value used by {@code receive} method in the tests. the value is specified in the
    * {@code config/test.properties} file.
    */
   public static final long TIMEOUT;

   static {
      // load tests.properties
      long tempTimeOut = 0;
      try {
         Properties props = new Properties();
         props.load(ClassLoader.getSystemResourceAsStream(TestConfig.PROP_FILE_NAME));
         System.out.println("Found " + TestConfig.PROP_FILE_NAME);
         tempTimeOut = Long.parseLong(props.getProperty(TestConfig.PROP_NAME, "0"));
      } catch (Exception e) {
         e.printStackTrace();
         tempTimeOut = 30000;
      } finally {
         TIMEOUT = tempTimeOut;
      }
   }
}
