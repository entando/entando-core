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

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.DateConverter;
import java.util.ArrayList;
import java.util.List;
import org.entando.entando.aps.system.services.oauth2.model.ConsumerRecordVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

/**
 * @author E.Santoboni
 */
public class OAuthConsumerManagerTest {
    
    @Mock
    private IOAuthConsumerDAO consumerDAO;
    
    @InjectMocks
    private OAuthConsumerManager consumerManager;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void getConsumer_1() throws Exception {
        ConsumerRecordVO record = this.createMockConsumer("key_1", "secret", false);
        when(this.consumerDAO.getConsumer(Mockito.anyString())).thenReturn(record);
        ConsumerRecordVO extracted = this.consumerManager.getConsumerRecord("key");
        Assert.assertEquals(record, extracted);
        Mockito.verify(consumerDAO, Mockito.times(1)).getConsumer(Mockito.anyString());
    }
    
    @Test(expected = ApsSystemException.class)
    public void getConsumer_2() throws Exception {
        Mockito.doThrow(RuntimeException.class).when(this.consumerDAO).getConsumer(Mockito.anyString());
        try {
            ConsumerRecordVO extracted = this.consumerManager.getConsumerRecord("key");
        } catch (ApsSystemException e) {
            throw e;
        } finally {
            Mockito.verify(consumerDAO, Mockito.times(1)).getConsumer(Mockito.anyString());
        }
    }
    
    @Test
    public void addConsumer_1() throws Exception {
        ConsumerRecordVO record = this.createMockConsumer("key_1", "secret", false);
        this.consumerManager.addConsumer(record);
        Mockito.verify(consumerDAO, Mockito.times(1)).addConsumer(Mockito.any(ConsumerRecordVO.class));
    }
    
    @Test(expected = ApsSystemException.class)
    public void addConsumer_2() throws Exception {
        ConsumerRecordVO record = this.createMockConsumer("key_2", "secret", false);
        Mockito.doThrow(RuntimeException.class).when(this.consumerDAO).addConsumer(record);
        try {
            this.consumerManager.addConsumer(record);
        } catch (ApsSystemException e) {
            throw e;
        } finally {
            Mockito.verify(consumerDAO, Mockito.times(1)).addConsumer(Mockito.any(ConsumerRecordVO.class));
        }
    }

    @Test
    public void updateConsumer_1() throws Exception {
        ConsumerRecordVO record = this.createMockConsumer("key_1", "secret", false);
        this.consumerManager.updateConsumer(record);
        Mockito.verify(consumerDAO, Mockito.times(1)).updateConsumer(Mockito.any(ConsumerRecordVO.class));
    }
    
    @Test(expected = ApsSystemException.class)
    public void updateConsumer_2() throws Exception {
        ConsumerRecordVO record = this.createMockConsumer("key_2", "secret", false);
        Mockito.doThrow(RuntimeException.class).when(this.consumerDAO).updateConsumer(record);
        try {
            this.consumerManager.updateConsumer(record);
        } catch (ApsSystemException e) {
            throw e;
        } finally {
            Mockito.verify(consumerDAO, Mockito.times(1)).updateConsumer(Mockito.any(ConsumerRecordVO.class));
        }
    }

    @Test
    public void deleteConsumer_1() throws Exception {
        this.consumerManager.deleteConsumer("key_test_1");
        Mockito.verify(consumerDAO, Mockito.times(1)).deleteConsumer(Mockito.anyString());
    }
    
    @Test(expected = ApsSystemException.class)
    public void deleteConsumer_2() throws Exception {
        Mockito.doThrow(RuntimeException.class).when(this.consumerDAO).deleteConsumer(Mockito.anyString());
        try {
            this.consumerManager.deleteConsumer("key_test_2");
        } catch (ApsSystemException e) {
            throw e;
        } finally {
            Mockito.verify(consumerDAO, Mockito.times(1)).deleteConsumer(Mockito.anyString());
        }
    }
    
    @Test
    public void getConsumerKeys_1() throws Exception {
        List<String> mockKeys = new ArrayList<>();
        mockKeys.add("key_1");
        when(this.consumerDAO.getConsumerKeys(Mockito.any(FieldSearchFilter[].class))).thenReturn(mockKeys);
        List<String> keys = this.consumerManager.getConsumerKeys(new FieldSearchFilter[]{});
        Assert.assertNotNull(keys);
        Assert.assertEquals(1, keys.size());
        Mockito.verify(consumerDAO, Mockito.times(1)).getConsumerKeys(Mockito.any(FieldSearchFilter[].class));
    }
    
    @Test(expected = ApsSystemException.class)
    public void getConsumerKeys_2() throws Exception {
        Mockito.doThrow(RuntimeException.class).when(this.consumerDAO).getConsumerKeys(Mockito.any(FieldSearchFilter[].class));
        try {
            List<String> keys = this.consumerManager.getConsumerKeys(new FieldSearchFilter[]{});
        } catch (ApsSystemException e) {
            throw e;
        } finally {
            Mockito.verify(consumerDAO, Mockito.times(1)).getConsumerKeys(Mockito.any(FieldSearchFilter[].class));
        }
    }
    
    @Test
    public void loadClient_1() throws Exception {
        ConsumerRecordVO record = this.createMockConsumer("key_1", "secret", false);
        when(this.consumerDAO.getConsumer(Mockito.anyString())).thenReturn(record);
        ClientDetails extracted = this.consumerManager.loadClientByClientId("key_1");
        Assert.assertNotNull(extracted);
        Mockito.verify(consumerDAO, Mockito.times(1)).getConsumer(Mockito.anyString());
    }
    
    @Test(expected = ClientRegistrationException.class)
    public void loadClient_2() throws Exception {
        ConsumerRecordVO record = this.createMockConsumer("key_1", "secret", true);
        when(this.consumerDAO.getConsumer(Mockito.anyString())).thenReturn(record);
        try {
            ClientDetails extracted = this.consumerManager.loadClientByClientId("key_1");
        } catch (ClientRegistrationException e) {
            throw e;
        } finally {
            Mockito.verify(consumerDAO, Mockito.times(1)).getConsumer(Mockito.anyString());
        }
    }
    
    @Test(expected = ClientRegistrationException.class)
    public void loadClient_3() throws Exception {
        when(this.consumerDAO.getConsumer(Mockito.anyString())).thenReturn(null);
        try {
            ClientDetails extracted = this.consumerManager.loadClientByClientId("key_1");
        } catch (ClientRegistrationException e) {
            throw e;
        } finally {
            Mockito.verify(consumerDAO, Mockito.times(1)).getConsumer(Mockito.anyString());
        }
    }
    
    @Test(expected = ClientRegistrationException.class)
    public void loadClient_4() throws Exception {
        when(this.consumerDAO.getConsumer(Mockito.anyString())).thenThrow(RuntimeException.class);
        try {
            ClientDetails extracted = this.consumerManager.loadClientByClientId("key_1");
        } catch (ClientRegistrationException e) {
            throw e;
        } finally {
            Mockito.verify(consumerDAO, Mockito.times(1)).getConsumer(Mockito.anyString());
        }
    }

    private ConsumerRecordVO createMockConsumer(String key, String secret, boolean expired) {
        ConsumerRecordVO consumer = new ConsumerRecordVO();
        consumer.setAuthorizedGrantTypes("password");
        consumer.setCallbackUrl("http://test.test");
        consumer.setDescription("Test Description");
        if (expired) {
            consumer.setExpirationDate(DateConverter.parseDate("2000", "yyyy"));
        }
        consumer.setKey(key);
        consumer.setName("Test Name");
        consumer.setScope("trust");
        consumer.setSecret(secret);
        return consumer;
    }
    
}
