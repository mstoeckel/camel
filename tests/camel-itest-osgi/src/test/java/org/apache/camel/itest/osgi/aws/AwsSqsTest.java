/**
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
package org.apache.camel.itest.osgi.aws;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.component.aws.sqs.SqsConstants;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.springframework.osgi.context.support.OsgiBundleXmlApplicationContext;


@RunWith(JUnit4TestRunner.class)
public class AwsSqsTest extends AwsTestSupport {

    @EndpointInject(uri = "mock:result")
    private MockEndpoint result;
    
    @Override
    protected OsgiBundleXmlApplicationContext createApplicationContext() {
        return new OsgiBundleXmlApplicationContext(new String[]{"org/apache/camel/itest/osgi/aws/CamelContext.xml"});
    }
    
    @Test
    public void sendInOnly() throws Exception {
        result.expectedMessageCount(1);
        
        Exchange exchange = template.send("direct:start", ExchangePattern.InOnly, new Processor() {
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody("This is my message text.");
            }
        });
        
        assertMockEndpointsSatisfied();
        
        Exchange resultExchange = result.getExchanges().get(0);
        assertEquals("This is my message text.", resultExchange.getIn().getBody());
        assertNotNull(resultExchange.getIn().getHeader(SqsConstants.MESSAGE_ID));
        assertNotNull(resultExchange.getIn().getHeader(SqsConstants.RECEIPT_HANDLE));
        assertEquals("6a1559560f67c5e7a7d5d838bf0272ee", resultExchange.getIn().getHeader(SqsConstants.MD5_OF_BODY));
        assertNotNull(resultExchange.getIn().getHeader(SqsConstants.ATTRIBUTES));
        
        assertNotNull(exchange.getIn().getHeader(SqsConstants.MESSAGE_ID));
        assertEquals("6a1559560f67c5e7a7d5d838bf0272ee", resultExchange.getIn().getHeader(SqsConstants.MD5_OF_BODY));
    }
    
    @Test
    public void sendInOut() throws Exception {
        result.expectedMessageCount(1);
        
        Exchange exchange = template.send("direct:start", ExchangePattern.InOut, new Processor() {
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setBody("This is my message text.");
            }
        });
        
        assertMockEndpointsSatisfied();
        
        Exchange resultExchange = result.getExchanges().get(0);
        assertEquals("This is my message text.", resultExchange.getIn().getBody());
        assertNotNull(resultExchange.getIn().getHeader(SqsConstants.RECEIPT_HANDLE));
        assertNotNull(resultExchange.getIn().getHeader(SqsConstants.MESSAGE_ID));
        assertEquals("6a1559560f67c5e7a7d5d838bf0272ee", resultExchange.getIn().getHeader(SqsConstants.MD5_OF_BODY));
        assertNotNull(resultExchange.getIn().getHeader(SqsConstants.ATTRIBUTES));
        
        assertNotNull(exchange.getOut().getHeader(SqsConstants.MESSAGE_ID));
        assertEquals("6a1559560f67c5e7a7d5d838bf0272ee", exchange.getOut().getHeader(SqsConstants.MD5_OF_BODY));
    }

   
}