/*
 * Copyright 2005-2014 Red Hat, Inc.
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.hornetq.tests.integration.openwire.amq;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQDestination;
import org.junit.Before;
import org.junit.Test;

/**
 * adapted from: org.apache.activemq.JmsTopicWildcardSendReceiveTest
 *
 * @author <a href="mailto:hgao@redhat.com">Howard Gao</a>
 *
 */
public class JmsTopicWildcardSendReceiveTest extends JmsTopicSendReceiveTest
{

   private String destination1String = "TEST.ONE.ONE";
   private String destination2String = "TEST.ONE.ONE.ONE";
   private String destination3String = "TEST.ONE.TWO";
   private String destination4String = "TEST.TWO.ONE";

   @Override
   @Before
   public void setUp() throws Exception
   {
      topic = true;
      durable = false;
      deliveryMode = DeliveryMode.NON_PERSISTENT;
      super.setUp();
   }

   protected String getConsumerSubject()
   {
      return "FOO.>";
   }

   protected String getProducerSubject()
   {
      return "FOO.BAR.HUMBUG";
   }

   @Test
   public void testReceiveWildcardTopicEndAsterisk() throws Exception
   {
      connection.start();
      Session session = connection.createSession(false,
            Session.AUTO_ACKNOWLEDGE);

      ActiveMQDestination destination1 = (ActiveMQDestination) session
            .createTopic(destination1String);
      ActiveMQDestination destination3 = (ActiveMQDestination) session
            .createTopic(destination3String);

      Message m = null;
      MessageConsumer consumer = null;
      String text = null;

      ActiveMQDestination destination6 = (ActiveMQDestination) session
            .createTopic("TEST.ONE.*");
      consumer = session.createConsumer(destination6);
      sendMessage(session, destination1, destination1String);
      sendMessage(session, destination3, destination3String);
      m = consumer.receive(5000);
      assertNotNull(m);
      text = ((TextMessage) m).getText();
      if (!(text.equals(destination1String) || text.equals(destination3String)))
      {
         fail("unexpected message:" + text);
      }
      m = consumer.receive(5000);
      assertNotNull(m);
      text = ((TextMessage) m).getText();
      if (!(text.equals(destination1String) || text.equals(destination3String)))
      {
         fail("unexpected message:" + text);
      }
      assertNull(consumer.receiveNoWait());
   }

   @Test
   public void testReceiveWildcardTopicEndGreaterThan() throws Exception
   {
      connection.start();
      Session session = connection.createSession(false,
            Session.AUTO_ACKNOWLEDGE);

      ActiveMQDestination destination1 = (ActiveMQDestination) session
            .createTopic(destination1String);
      ActiveMQDestination destination2 = (ActiveMQDestination) session
            .createTopic(destination2String);
      ActiveMQDestination destination3 = (ActiveMQDestination) session
            .createTopic(destination3String);

      Message m = null;
      MessageConsumer consumer = null;
      String text = null;

      ActiveMQDestination destination7 = (ActiveMQDestination) session
            .createTopic("TEST.ONE.>");
      consumer = session.createConsumer(destination7);
      sendMessage(session, destination1, destination1String);
      sendMessage(session, destination2, destination2String);
      sendMessage(session, destination3, destination3String);
      m = consumer.receive(1000);
      assertNotNull(m);
      text = ((TextMessage) m).getText();
      if (!(text.equals(destination1String) || text.equals(destination2String) || text
            .equals(destination3String)))
      {
         fail("unexpected message:" + text);
      }
      m = consumer.receive(1000);
      assertNotNull(m);
      if (!(text.equals(destination1String) || text.equals(destination2String) || text
            .equals(destination3String)))
      {
         fail("unexpected message:" + text);
      }
      m = consumer.receive(1000);
      assertNotNull(m);
      if (!(text.equals(destination1String) || text.equals(destination2String) || text
            .equals(destination3String)))
      {
         fail("unexpected message:" + text);
      }
      assertNull(consumer.receiveNoWait());
   }

   @Test
   public void testReceiveWildcardTopicMidAsterisk() throws Exception
   {
      connection.start();
      Session session = connection.createSession(false,
            Session.AUTO_ACKNOWLEDGE);

      ActiveMQDestination destination1 = (ActiveMQDestination) session
            .createTopic(destination1String);
      ActiveMQDestination destination4 = (ActiveMQDestination) session
            .createTopic(destination4String);

      Message m = null;
      MessageConsumer consumer = null;
      String text = null;

      ActiveMQDestination destination8 = (ActiveMQDestination) session
            .createTopic("TEST.*.ONE");
      consumer = session.createConsumer(destination8);
      sendMessage(session, destination1, destination1String);
      sendMessage(session, destination4, destination4String);
      m = consumer.receive(1000);
      assertNotNull(m);
      text = ((TextMessage) m).getText();
      if (!(text.equals(destination1String) || text.equals(destination4String)))
      {
         fail("unexpected message:" + text);
      }
      m = consumer.receive(1000);
      assertNotNull(m);
      text = ((TextMessage) m).getText();
      if (!(text.equals(destination1String) || text.equals(destination4String)))
      {
         fail("unexpected message:" + text);
      }
      assertNull(consumer.receiveNoWait());

   }

   @Test
   public void testReceiveWildcardTopicMatchDoubleWildcard() throws Exception
   {
      connection.start();
      Session session = connection.createSession(false,
            Session.AUTO_ACKNOWLEDGE);

      ActiveMQDestination destination1 = (ActiveMQDestination) session
            .createTopic("a.*.>.>");
      ActiveMQDestination destination2 = (ActiveMQDestination) session
            .createTopic("a.b");

      Message m = null;
      MessageConsumer consumer = null;
      String text = null;

      consumer = session.createConsumer(destination1);
      sendMessage(session, destination2, destination3String);

      m = consumer.receive(1000);
      assertNotNull(m);
      text = ((TextMessage) m).getText();
      if (!(text.equals(destination1String) || text.equals(destination3String)))
      {
         fail("unexpected message:" + text);
      }

      assertNull(consumer.receiveNoWait());
   }

   @Test
   public void testReceiveWildcardTopicMatchSinglePastTheEndWildcard() throws Exception
   {
      connection.start();
      Session session = connection.createSession(false,
            Session.AUTO_ACKNOWLEDGE);

      ActiveMQDestination destination1 = (ActiveMQDestination) session
            .createTopic("a.>");
      ActiveMQDestination destination2 = (ActiveMQDestination) session
            .createTopic("a");

      Message m = null;
      MessageConsumer consumer = null;
      String text = null;

      consumer = session.createConsumer(destination1);
      sendMessage(session, destination2, destination3String);

      m = consumer.receive(1000);
      assertNotNull(m);
      text = ((TextMessage) m).getText();
      if (!(text.equals(destination1String) || text.equals(destination3String)))
      {
         fail("unexpected message:" + text);
      }

      assertNull(consumer.receiveNoWait());
   }

   private void sendMessage(Session session, Destination destination, String text) throws JMSException
   {
      MessageProducer producer = session.createProducer(destination);
      producer.send(session.createTextMessage(text));
      producer.close();
   }

}
