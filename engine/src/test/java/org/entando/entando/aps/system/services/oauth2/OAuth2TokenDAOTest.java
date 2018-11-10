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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * @author E.Santoboni
 */
public class OAuth2TokenDAOTest {
    
    @Mock
    private DataSource dataSource;
    @Mock
    private Connection conn;
    @Mock
    private PreparedStatement stat;
    @Mock
    private PreparedStatement statForSearchId;
    @Mock
    private ResultSet res;
    @Mock
    private ResultSet resForSearchId;
    
    @InjectMocks
    private OAuth2TokenDAO tokenDAO;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(this.dataSource.getConnection()).thenReturn(conn);
        when(this.conn.prepareStatement(Mockito.startsWith("SELECT api_oauth_tokens.accesstoken"))).thenReturn(statForSearchId);
        when(this.conn.prepareStatement(Mockito.startsWith("SELECT * "))).thenReturn(stat);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 200);
        Mockito.when(res.getTimestamp("expiresin")).thenReturn(new Timestamp(calendar.getTime().getTime()));
    }
    
    @Test
    public void findTokensByClientIdAndUserName_1() throws Exception {
        System.out.println("org.entando.entando.aps.system.services.oauth2.OAuth2TokenDAOTest.findTokensByClientIdAndUserName_1()");
        when(this.stat.executeQuery()).thenReturn(res);
        when(this.statForSearchId.executeQuery()).thenReturn(resForSearchId);
        Mockito.when(res.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resForSearchId.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resForSearchId.getString(Mockito.anyString())).thenReturn("token");
        List<OAuth2AccessToken> keys = this.tokenDAO.findTokensByClientIdAndUserName("client_id", "username");
        Assert.assertNotNull(keys);
        Assert.assertEquals(1, keys.size());
        Mockito.verify(statForSearchId, Mockito.times(2)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(resForSearchId, Mockito.times(1)).getString(Mockito.anyString());
        Mockito.verify(stat, Mockito.times(1)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(res, Mockito.times(4)).getString(Mockito.anyString());
        Mockito.verify(res, Mockito.times(1)).getTimestamp(Mockito.anyString());
        this.executeFinalCheck(true);
        this.executeFinalCheckForSearchId(true);
        Mockito.verify(conn, Mockito.times(2)).close();
        System.out.println("org.entando.entando.aps.system.services.oauth2.OAuth2TokenDAOTest.findTokensByClientIdAndUserName_1()");
    }
    
    @Test(expected = RuntimeException.class)
    public void findTokensByClientIdAndUserName_2() throws Exception {
        try {
            when(this.statForSearchId.executeQuery()).thenReturn(resForSearchId);
            Mockito.when(resForSearchId.next()).thenReturn(true).thenReturn(false);
            Mockito.when(resForSearchId.getString(Mockito.anyString())).thenReturn("token");
            when(this.stat.executeQuery()).thenReturn(res);
            Mockito.when(res.next()).thenReturn(true).thenReturn(false);
            System.out.println("org.entando.entando.aps.system.services.oauth2.OAuth2TokenDAOTest.findTokensByClientIdAndUserName_2()");
            when(res.getString(Mockito.anyString())).thenThrow(SQLException.class);
            List<OAuth2AccessToken> keys = this.tokenDAO.findTokensByClientIdAndUserName("client_id", "username");
        } catch (RuntimeException e) {
            throw e;
        } finally {
            Mockito.verify(statForSearchId, Mockito.times(2)).setString(Mockito.anyInt(), Mockito.anyString());
            Mockito.verify(resForSearchId, Mockito.times(1)).getString(Mockito.anyString());
            Mockito.verify(stat, Mockito.times(1)).setString(Mockito.anyInt(), Mockito.anyString());
            Mockito.verify(res, Mockito.times(1)).getString(Mockito.anyString());
            Mockito.verify(res, Mockito.times(0)).getTimestamp(Mockito.anyString());
            this.executeFinalCheck(true);
            this.executeFinalCheckForSearchId(true);
            Mockito.verify(conn, Mockito.times(2)).close();
        }
    }
    
    @Test(expected = RuntimeException.class)
    public void findTokensByClientIdAndUserName_3() throws Exception {
        try {
            when(this.statForSearchId.executeQuery()).thenReturn(resForSearchId);
            when(resForSearchId.next()).thenReturn(true).thenReturn(false);
            when(resForSearchId.getString(Mockito.anyString())).thenThrow(SQLException.class);
            List<OAuth2AccessToken> keys = this.tokenDAO.findTokensByClientIdAndUserName("client_id", "username");
        } catch (RuntimeException e) {
            throw e;
        } finally {
            Mockito.verify(statForSearchId, Mockito.times(2)).setString(Mockito.anyInt(), Mockito.anyString());
            Mockito.verify(resForSearchId, Mockito.times(1)).getString(Mockito.anyString());
            Mockito.verify(stat, Mockito.times(0)).setString(Mockito.anyInt(), Mockito.anyString());
            Mockito.verify(res, Mockito.times(0)).getString(Mockito.anyString());
            Mockito.verify(res, Mockito.times(0)).getTimestamp(Mockito.anyString());
            this.executeFinalCheckForSearchId(true);
            Mockito.verify(conn, Mockito.times(1)).close();
        }
    }
    
    private void executeFinalCheck(boolean resExist) throws Exception {
        if (resExist) {
            Mockito.verify(res, Mockito.times(1)).close();
        } else {
            Mockito.verify(res, Mockito.times(0)).close();
        }
        Mockito.verify(stat, Mockito.times(1)).close();
    }
    
    private void executeFinalCheckForSearchId(boolean resExist) throws Exception {
        if (resExist) {
            Mockito.verify(resForSearchId, Mockito.times(1)).close();
        } else {
            Mockito.verify(resForSearchId, Mockito.times(0)).close();
        }
        Mockito.verify(statForSearchId, Mockito.times(1)).close();
    }
    
}
