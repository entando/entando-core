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
import com.agiletec.aps.util.DateConverter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import javax.sql.DataSource;
import org.apache.derby.client.am.SqlException;
import org.entando.entando.aps.system.services.oauth2.model.ConsumerRecordVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 * @author E.Santoboni
 */
public class OAuthConsumerDAOTest {
    
    @Mock
    private DataSource dataSource;
    @Mock
    private Connection conn;
    @Mock
    private PreparedStatement stat;
    @Mock
    private ResultSet res;
    
    @InjectMocks
    private OAuthConsumerDAO consumerDAO;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(this.dataSource.getConnection()).thenReturn(conn);
        when(this.conn.prepareStatement(Mockito.anyString())).thenReturn(stat);
    }
    
    @Test
    public void getConsumerKeys_1() throws Exception {
        when(this.stat.executeQuery()).thenReturn(res);
        Mockito.when(res.next()).thenReturn(true).thenReturn(false);
        List<String> keys = this.consumerDAO.getConsumerKeys(new FieldSearchFilter[]{});
        Assert.assertNotNull(keys);
        Assert.assertEquals(1, keys.size());
        this.executeFinalCheck(true);
    }
    
    @Test(expected = RuntimeException.class)
    public void getConsumerKeys_2() throws Exception {
        when(this.stat.executeQuery()).thenThrow(SqlException.class);
        try {
            List<String> keys = this.consumerDAO.getConsumerKeys(new FieldSearchFilter[]{});
        } catch (RuntimeException e) {
            throw e;
        } finally {
            this.executeFinalCheck(true);
        }
    }
    
    @Test
    public void getConsumer_1() throws Exception {
        when(this.stat.executeQuery()).thenReturn(res);
        Mockito.when(res.next()).thenReturn(true).thenReturn(false);
        Mockito.when(res.getString("consumerkey")).thenReturn("client_id");
        Mockito.when(res.getString("consumersecret")).thenReturn("secret");
        ConsumerRecordVO consumer = this.consumerDAO.getConsumer("client_id");
        Assert.assertNotNull(consumer);
        Assert.assertEquals("client_id", consumer.getKey());
        Assert.assertEquals("secret", consumer.getSecret());
        this.executeFinalCheck(true);
    }
    
    @Test(expected = RuntimeException.class)
    public void getConsumer_2() throws Exception {
        when(this.stat.executeQuery()).thenReturn(res);
        Mockito.when(res.next()).thenReturn(true).thenReturn(false);
        Mockito.when(res.getString("consumerkey")).thenThrow(SqlException.class);
        try {
            List<String> keys = this.consumerDAO.getConsumerKeys(new FieldSearchFilter[]{});
        } catch (RuntimeException e) {
            throw e;
        } finally {
            Mockito.verify(stat, Mockito.times(1)).setString(Mockito.anyInt(), Mockito.anyString());
            Mockito.verify(res, Mockito.times(1)).next();
            this.executeFinalCheck(true);
        }
    }
    
    @Test
    public void addConsumer_1() throws Exception {
        ConsumerRecordVO record = this.createMockConsumer("key_1", "secret");
        this.consumerDAO.addConsumer(record);
        Mockito.verify(stat, Mockito.times(7)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(stat, Mockito.times(2)).setTimestamp(Mockito.anyInt(), Mockito.any(Timestamp.class));
        this.executeFinalCheck(false);
    }
    
    @Test(expected = RuntimeException.class)
    public void addConsumer_2() throws Exception {
        Mockito.doThrow(SqlException.class).when(stat).setTimestamp(Mockito.anyInt(), Mockito.any(Timestamp.class));
        ConsumerRecordVO record = this.createMockConsumer("key_2", "secret");
        try {
            this.consumerDAO.addConsumer(record);
        } catch (RuntimeException e) {
            throw e;
        } finally {
            Mockito.verify(stat, Mockito.times(7)).setString(Mockito.anyInt(), Mockito.anyString());
            Mockito.verify(stat, Mockito.times(1)).setTimestamp(Mockito.anyInt(), Mockito.any(Timestamp.class));
            this.executeFinalCheck(false);
        }
    }
    
    @Test
    public void updateConsumer_1() throws Exception {
        ConsumerRecordVO record = this.createMockConsumer("key_3", null);
        this.consumerDAO.updateConsumer(record);
        Mockito.verify(stat, Mockito.times(6)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(stat, Mockito.times(1)).setTimestamp(Mockito.anyInt(), Mockito.any(Timestamp.class));
        this.executeFinalCheck(false);
    }
    
    @Test(expected = RuntimeException.class)
    public void updateConsumer_2() throws Exception {
        Mockito.doThrow(SqlException.class).when(stat).setTimestamp(Mockito.anyInt(), Mockito.any(Timestamp.class));
        ConsumerRecordVO record = this.createMockConsumer("key_4", null);
        try {
            this.consumerDAO.updateConsumer(record);
        } catch (RuntimeException e) {
            throw e;
        } finally {
            Mockito.verify(stat, Mockito.times(6)).setString(Mockito.anyInt(), Mockito.anyString());
            Mockito.verify(stat, Mockito.times(1)).setTimestamp(Mockito.anyInt(), Mockito.any(Timestamp.class));
            this.executeFinalCheck(false);
        }
    }
    
    @Test
    public void deleteConsumer_1() throws Exception {
        this.consumerDAO.deleteConsumer("key_4");
        Mockito.verify(stat, Mockito.times(2)).setObject(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(conn, Mockito.times(2)).prepareStatement(Mockito.anyString());
        Mockito.verify(stat, Mockito.times(2)).executeUpdate();
        Mockito.verify(stat, Mockito.times(2)).close();
        Mockito.verify(conn, Mockito.times(1)).close();
    }
    
    private ConsumerRecordVO createMockConsumer(String key, String secret) {
        ConsumerRecordVO consumer = new ConsumerRecordVO();
        consumer.setAuthorizedGrantTypes("password");
        consumer.setCallbackUrl("http://test.test");
        consumer.setDescription("Test Description");
        consumer.setExpirationDate(DateConverter.parseDate("2050", "yyyy"));
        consumer.setKey(key);
        consumer.setName("Test Name");
        consumer.setScope("trust");
        consumer.setSecret(secret);
        return consumer;
    }
    
    private void executeFinalCheck(boolean resExist) throws Exception {
        if (resExist) {
            Mockito.verify(res, Mockito.times(1)).close();
        } else {
            Mockito.verify(res, Mockito.times(0)).close();
        }
        Mockito.verify(stat, Mockito.times(1)).close();
        Mockito.verify(conn, Mockito.times(1)).close();
    }
    
}
