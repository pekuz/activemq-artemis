/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Topic;

import org.apache.activemq.test.JmsTopicSendReceiveTest;

public class JmsQueueTopicCompositeSendReceiveTest extends JmsTopicSendReceiveTest {

   private static final org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(JmsQueueTopicCompositeSendReceiveTest.class);
   Destination consumerDestination2;
   MessageConsumer consumer2;

   /**
    * Sets a test to have a queue destination and non-persistent delivery mode.
    */
   @Override
   protected void setUp() throws Exception {
      deliveryMode = DeliveryMode.NON_PERSISTENT;
      topic = false;
      super.setUp();
      consumerDestination2 = consumeSession.createTopic("FOO.BAR.HUMBUG2");
      LOG.info("Created  consumer destination: " + consumerDestination2 + " of type: " + consumerDestination2.getClass());
      if (durable) {
         LOG.info("Creating durable consumer");
         consumer2 = consumeSession.createDurableSubscriber((Topic) consumerDestination2, getName());
      } else {
         consumer2 = consumeSession.createConsumer(consumerDestination2);
      }

   }

   @Override
   protected String getConsumerSubject() {
      return "FOO.BAR.HUMBUG";
   }

   @Override
   protected String getProducerSubject() {
      return "queue://FOO.BAR.HUMBUG,topic://FOO.BAR.HUMBUG2";
   }

   @Override
   public void testSendReceive() throws Exception {
      super.testSendReceive();
      messages.clear();
      consumer2.setMessageListener(this);
      assertMessagesAreReceived();
      LOG.info("" + data.length + " messages(s) received, closing down connections");
   }
}
