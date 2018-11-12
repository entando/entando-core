/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.aps.system.services.oauth2;

import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.util.DateConverter;
import java.util.Date;
import org.entando.entando.aps.system.services.oauth2.model.ConsumerRecordVO;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

/**
 * @author E.Santoboni
 */
public class OAuthConsumerManagerIntegrationTest extends BaseTestCase {

    private IOAuthConsumerManager oauthConsumerManager;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void testGetConsumer() throws Exception {
        ConsumerRecordVO consumer = oauthConsumerManager.getConsumerRecord("test1_consumer");
        assertNotNull(consumer);
        assertEquals(consumer.getName(), "Test 1 Consumer");
        assertEquals(consumer.getDescription(), "Test 1 Consumer Description");
        assertEquals(consumer.getScope(), "read,write,trust");
        assertEquals("2028-10-10", DateConverter.getFormattedDate(consumer.getExpirationDate(), "yyyy-MM-dd"));
        
        consumer = oauthConsumerManager.getConsumerRecord("test1_consumer_wrong");
        assertNull(consumer);
    }

    public void testAddConsumer() throws Exception {
        ConsumerRecordVO consumer = this.createConsumer("key", "secret", false);
        try {
            assertNull(this.oauthConsumerManager.getConsumerRecord(consumer.getKey()));
            oauthConsumerManager.addConsumer(consumer);
            ConsumerRecordVO extractedConsumer = oauthConsumerManager.getConsumerRecord(consumer.getKey());
            assertNotNull(extractedConsumer);
            assertEquals(consumer.getAuthorizedGrantTypes(), extractedConsumer.getAuthorizedGrantTypes());
            assertEquals(consumer.getCallbackUrl(), extractedConsumer.getCallbackUrl());
            assertEquals(consumer.getDescription(), extractedConsumer.getDescription());
            assertEquals(consumer.getExpirationDate(), extractedConsumer.getExpirationDate());
            assertEquals(DateConverter.getFormattedDate(new Date(), "dd-MM-yyyy"), 
                    DateConverter.getFormattedDate(extractedConsumer.getIssuedDate(), "dd-MM-yyyy"));
            assertEquals(consumer.getKey(), extractedConsumer.getKey());
            assertEquals(consumer.getName(), extractedConsumer.getName());
            assertEquals(consumer.getScope(), extractedConsumer.getScope());
            assertTrue(extractedConsumer.getSecret().startsWith("{bcrypt}"));
        } catch (Exception t) {
            throw t;
        } finally {
            oauthConsumerManager.deleteConsumer(consumer.getKey());
            assertNull(this.oauthConsumerManager.getConsumerRecord(consumer.getKey()));
        }
    }

    public void testUpdateRemoveCategory() throws Throwable {
        ConsumerRecordVO consumer = this.createConsumer("key_2", "secret_2", false);
        try {
            assertNull(this.oauthConsumerManager.getConsumerRecord(consumer.getKey()));
            oauthConsumerManager.addConsumer(consumer);
            ConsumerRecordVO extractedConsumer_1 = oauthConsumerManager.getConsumerRecord(consumer.getKey());
            assertNotNull(extractedConsumer_1);

            String newDescription = "New Test Description";
            extractedConsumer_1.setDescription(newDescription);
            oauthConsumerManager.updateConsumer(extractedConsumer_1);
            ConsumerRecordVO extractedConsumer_2 = oauthConsumerManager.getConsumerRecord(consumer.getKey());
            assertEquals(consumer.getAuthorizedGrantTypes(), extractedConsumer_1.getAuthorizedGrantTypes());
            assertEquals(consumer.getCallbackUrl(), extractedConsumer_2.getCallbackUrl());
            assertEquals(newDescription, extractedConsumer_2.getDescription());
            assertEquals(consumer.getExpirationDate(), extractedConsumer_1.getExpirationDate());
            assertEquals(extractedConsumer_1.getIssuedDate(), extractedConsumer_2.getIssuedDate());
            assertEquals(consumer.getKey(), extractedConsumer_2.getKey());
            assertEquals(consumer.getName(), extractedConsumer_2.getName());
            assertEquals(consumer.getScope(), extractedConsumer_2.getScope());
            assertTrue(extractedConsumer_2.getSecret().startsWith("{bcrypt}"));
        } catch (Throwable t) {
            throw t;
        } finally {
            oauthConsumerManager.deleteConsumer(consumer.getKey());
            assertNull(this.oauthConsumerManager.getConsumerRecord(consumer.getKey()));
        }
    }

    public void testGetConsumers() throws Exception {
        FieldSearchFilter filter = new FieldSearchFilter(IOAuthConsumerManager.CONSUMER_DESCRIPTION_FILTER_KEY, "1 Consumer", true);
        List<String> keys = this.oauthConsumerManager.getConsumerKeys(new FieldSearchFilter[]{filter});
        assertNotNull(keys);
        assertEquals(1, keys.size());
        assertEquals("test1_consumer", keys.get(0));
    }
    
    public void testLoadClientByClientId_1() {
        ClientDetails client = this.oauthConsumerManager.loadClientByClientId("test1_consumer");
        assertNotNull(client);
        assertEquals(3, client.getScope().size());
        assertEquals(4, client.getAuthorizedGrantTypes().size());
    }
    
    public void testLoadClientByClientId_2() throws Throwable {
        ConsumerRecordVO consumer = this.createConsumer("key_3", "secret_3", true);
        try {
            assertNull(this.oauthConsumerManager.getConsumerRecord(consumer.getKey()));
            oauthConsumerManager.addConsumer(consumer);
            ConsumerRecordVO extractedConsumer = oauthConsumerManager.getConsumerRecord(consumer.getKey());
            assertNotNull(extractedConsumer);
            this.oauthConsumerManager.loadClientByClientId("key_3");
            fail();
        } catch (ClientRegistrationException t) {
            assertEquals("Client 'key_3' is expired", t.getMessage());
        } catch (Throwable t) {
            throw t;
        } finally {
            oauthConsumerManager.deleteConsumer(consumer.getKey());
            assertNull(this.oauthConsumerManager.getConsumerRecord(consumer.getKey()));
        }
    }
    
    public void testLoadClientByClientId_3() {
        try {
            this.oauthConsumerManager.loadClientByClientId("invalid");
            fail();
        } catch (ClientRegistrationException t) {
            assertEquals("Client with id 'invalid' does not exists", t.getMessage());
        } catch (Throwable t) {
            throw t;
        }
    }

    private ConsumerRecordVO createConsumer(String key, String secret, boolean expired) {
        ConsumerRecordVO consumer = new ConsumerRecordVO();
        consumer.setAuthorizedGrantTypes("password");
        consumer.setCallbackUrl("http://test.test");
        consumer.setDescription("Test Description");
        if (expired) {
            consumer.setExpirationDate(DateConverter.parseDate("2000", "yyyy"));
        } else {
            consumer.setExpirationDate(DateConverter.parseDate("2099", "yyyy"));
        }
        consumer.setKey(key);
        consumer.setName("Test Name");
        consumer.setScope("trust");
        consumer.setSecret(secret);
        return consumer;
    }

    private void init() throws Exception {
        try {
            this.oauthConsumerManager = (IOAuthConsumerManager) this.getService(SystemConstants.OAUTH_CONSUMER_MANAGER);
        } catch (Throwable t) {
            throw new Exception(t);
        }
    }

}
