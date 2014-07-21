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
package org.hornetq.cli.commands;

import io.airlift.command.Command;
import org.hornetq.core.config.Configuration;
import org.hornetq.core.server.impl.HornetQServerImpl;
import org.hornetq.dto.BrokerDTO;
import org.hornetq.factory.CoreFactory;
import org.hornetq.factory.SecurityManagerFactory;
import org.hornetq.spi.core.security.HornetQSecurityManager;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.InputStream;

@Command(name = "run", description = "runs the broker instance")
public class Run implements Action
{

   @Override
   public Object execute(ActionContext context) throws Exception
   {
      JAXBContext jaxbContext = JAXBContext.newInstance(BrokerDTO.class);
      File file = new File("config/hornetq.xml");

      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      sf.setFeature("http://apache.org/xml/features/validation/schema-full-checking", false);
      InputStream xsdStream = Run.class.getClassLoader().getResourceAsStream("org/hornetq/dto/hornetq.xsd");
      StreamSource xsdSource = new StreamSource(xsdStream);
      Schema schema = sf.newSchema(xsdSource);
      unmarshaller.setSchema(schema);

      BrokerDTO broker = (BrokerDTO) unmarshaller.unmarshal(file);

      Configuration configuration = CoreFactory.create(broker.core);

      HornetQSecurityManager security = SecurityManagerFactory.create(broker.security);

      HornetQServerImpl server = new HornetQServerImpl(configuration, security);
      server.start();

      return null;
   }
}
