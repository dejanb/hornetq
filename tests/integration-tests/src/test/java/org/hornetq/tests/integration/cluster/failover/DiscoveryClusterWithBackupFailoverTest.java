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
package org.hornetq.tests.integration.cluster.failover;


/**
 * A DiscoveryClusterWithBackupFailoverTest
 *
 * @author <a href="mailto:tim.fox@jboss.com">Tim Fox</a>
 *
 *
 */
public class DiscoveryClusterWithBackupFailoverTest extends ClusterWithBackupFailoverTestBase
{

   protected final String groupAddress = getUDPDiscoveryAddress();

   protected final int groupPort = getUDPDiscoveryPort();

   @Override
   protected void setupCluster(final boolean forwardWhenNoConsumers) throws Exception
   {
      // The lives

      setupDiscoveryClusterConnection("cluster0", 0, "dg1", "queues", forwardWhenNoConsumers, 1, isNetty());
      setupDiscoveryClusterConnection("cluster1", 1, "dg1", "queues", forwardWhenNoConsumers, 1, isNetty());
      setupDiscoveryClusterConnection("cluster2", 2, "dg1", "queues", forwardWhenNoConsumers, 1, isNetty());

      // The backups

      setupDiscoveryClusterConnection("cluster0", 3, "dg1", "queues", forwardWhenNoConsumers, 1, isNetty());
      setupDiscoveryClusterConnection("cluster1", 4, "dg1", "queues", forwardWhenNoConsumers, 1, isNetty());
      setupDiscoveryClusterConnection("cluster2", 5, "dg1", "queues", forwardWhenNoConsumers, 1, isNetty());
   }

   @Override
   protected void setupServers() throws Exception
   {
      // The lives
      setupLiveServerWithDiscovery(0, groupAddress, groupPort, isFileStorage(), isNetty(), true);
      setupLiveServerWithDiscovery(1, groupAddress, groupPort, isFileStorage(), isNetty(), true);
      setupLiveServerWithDiscovery(2, groupAddress, groupPort, isFileStorage(), isNetty(), true);

      // The backups
      setupBackupServerWithDiscovery(3, 0, groupAddress, groupPort, isFileStorage(), isNetty(), true);
      setupBackupServerWithDiscovery(4, 1, groupAddress, groupPort, isFileStorage(), isNetty(), true);
      setupBackupServerWithDiscovery(5, 2, groupAddress, groupPort, isFileStorage(), isNetty(), true);
   }

}
