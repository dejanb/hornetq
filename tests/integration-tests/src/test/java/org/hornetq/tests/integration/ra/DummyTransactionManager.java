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
package org.hornetq.tests.integration.ra;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

public class DummyTransactionManager implements TransactionManager
{
   protected static DummyTransactionManager tm = new DummyTransactionManager();

   public Transaction tx;

   @Override
   public void begin() throws NotSupportedException, SystemException
   {
   }

   @Override
   public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException
   {
   }

   @Override
   public void rollback() throws IllegalStateException, SecurityException, SystemException
   {
   }

   @Override
   public void setRollbackOnly() throws IllegalStateException, SystemException
   {
   }

   @Override
   public int getStatus() throws SystemException
   {
      return 0;
   }

   @Override
   public Transaction getTransaction() throws SystemException
   {
      return tx;
   }

   @Override
   public void setTransactionTimeout(int i) throws SystemException
   {
   }

   @Override
   public Transaction suspend() throws SystemException
   {
      return null;
   }

   @Override
   public void resume(Transaction transaction) throws InvalidTransactionException, IllegalStateException, SystemException
   {
   }
}
