/*
  * JBoss, Home of Professional Open Source
  * Copyright 2005, JBoss Inc., and individual contributors as indicated
  * by the @authors tag. See the copyright.txt in the distribution for a
  * full listing of individual contributors.
  *
  * This is free software; you can redistribute it and/or modify it
  * under the terms of the GNU Lesser General Public License as
  * published by the Free Software Foundation; either version 2.1 of
  * the License, or (at your option) any later version.
  *
  * This software is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  * Lesser General Public License for more details.
  *
  * You should have received a copy of the GNU Lesser General Public
  * License along with this software; if not, write to the Free
  * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
  * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
  */
package org.jboss.jms.server.endpoint;

import org.jboss.jms.delegate.SessionDelegate;
import org.jboss.jms.message.MessageProxy;

/**
 * Struct like class for holding information regarding a delivery 
 * on the client side - this is never passed to the server
 * 
 * @author <a href="mailto:tim.fox@jboss.com>Tim Fox </a>
 * @author <a href="mailto:ovidiu@jboss.com>Ovidiu Feodorov</a>
 *
 * $Id: AckInfo.java 1770 2006-12-12 10:49:42Z timfox $
 */
public class DeliveryInfo implements Ack
{
   // Constants -----------------------------------------------------
   
   // Attributes ----------------------------------------------------
   
   //This is needed on failover when recreating delivery list on the server session
   //we need to know the channel id so we can recreate the deliveries
   private long channelID;
   
   //This is needed when doing local redelivery of messages, since we need to know which
   //consumer gets the message
   private int consumerId;      

   private MessageProxy msg;
   
   //When using the evil abomination known as a ConnectionConsumer, the connection consumer
   //will get from a session that it created, then pass them onto sessions got from the pool
   //this means when the messages are acked/cancelled then this needs to be done against
   //the connection consumer's session not the session from the pool, since that session won't know
   //about the deliveries on the server side
   //Therefore if this delivery was done using a connection consumer then this attribute is set
   //to the connection consumer's session, otherwise it will be null
   private SessionDelegate connectionConsumerSession;
   
  // private boolean exceedDeliveryAttempts;
   
  // private boolean expired;
   
   // Static --------------------------------------------------------
   
   // Constructors --------------------------------------------------
   
   public DeliveryInfo(MessageProxy msg, int consumerId, long channelID,
                       SessionDelegate connectionConsumerSession)
   {      
      this.msg = msg;
      
      this.consumerId = consumerId;
      
      this.channelID = channelID;
      
      this.connectionConsumerSession = connectionConsumerSession;
   }

   // Public --------------------------------------------------------
   
   public long getChannelId()
   {
      return channelID;
   }
   
   public int getConsumerId()
   {
      return consumerId;
   }

   public MessageProxy getMessageProxy()
   {
      return msg;
   }
   
   public SessionDelegate getConnectionConsumerSession()
   {
      return connectionConsumerSession;
   }
   
//   public void setExceededDeliveryAttempts()
//   {
//      exceedDeliveryAttempts = true;
//   }
//   
//   public void setExpired()
//   {
//      expired = true;
//   }
//        
//   public boolean exceededDeliveryAttempts()
//   {
//      return exceedDeliveryAttempts;
//   }
//   
//   public boolean isExpired()
//   {
//      return expired;
//   }
//   
//   public boolean isToCancel()
//   {
//      return exceedDeliveryAttempts || expired;
//   }

   public String toString()
   {
      return "Delivery[" + getDeliveryID() + ", " + msg + "]";
   }

   // Ack Implementation  -------------------------------------------
   
   public long getDeliveryID()
   {
      return msg.getDeliveryId();
   }

   // Protected -----------------------------------------------------

   // Package Private -----------------------------------------------

   // Private -------------------------------------------------------
   
   // Inner Classes -------------------------------------------------
   
}
