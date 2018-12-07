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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2AccessTokenImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

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
        when(this.conn.prepareStatement(Mockito.startsWith("INSERT "))).thenReturn(stat);
        when(this.conn.prepareStatement(Mockito.startsWith("DELETE "))).thenReturn(stat);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 200);
        Mockito.when(res.getTimestamp("expiresin")).thenReturn(new Timestamp(calendar.getTime().getTime()));
    }

    @Test
    public void findTokensByClientIdAndUserName() throws Exception {
        this.executeFindTokens("client_id", "username");
    }

    @Test
    public void findTokensByClientId() throws Exception {
        this.executeFindTokens("client_id", null);
    }

    @Test
    public void findTokensByUserName() throws Exception {
        this.executeFindTokens(null, "username");
    }

    private void executeFindTokens(String clientId, String username) throws Exception {
        when(this.stat.executeQuery()).thenReturn(res);
        when(this.statForSearchId.executeQuery()).thenReturn(resForSearchId);
        Mockito.when(res.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resForSearchId.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resForSearchId.getString(Mockito.anyString())).thenReturn("token");
        List<OAuth2AccessToken> keys = null;
        if (StringUtils.isBlank(username)) {
            keys = this.tokenDAO.findTokensByClientId(clientId);
        } else if (StringUtils.isBlank(clientId)) {
            keys = this.tokenDAO.findTokensByUserName(username);
        } else {
            keys = this.tokenDAO.findTokensByClientIdAndUserName(clientId, username);
        }
        Assert.assertNotNull(keys);
        Assert.assertEquals(1, keys.size());
        if (!StringUtils.isBlank(clientId) && !StringUtils.isBlank(username)) {
            Mockito.verify(statForSearchId, Mockito.times(2)).setString(Mockito.anyInt(), Mockito.anyString());
        } else {
            Mockito.verify(statForSearchId, Mockito.times(1)).setString(Mockito.anyInt(), Mockito.anyString());
        }
        Mockito.verify(resForSearchId, Mockito.times(1)).getString(Mockito.anyString());
        Mockito.verify(stat, Mockito.times(1)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(res, Mockito.times(4)).getString(Mockito.anyString());
        Mockito.verify(res, Mockito.times(1)).getTimestamp(Mockito.anyString());
        this.executeFinalCheck(true);
        this.executeFinalCheckForSearchId(true);
        Mockito.verify(conn, Mockito.times(2)).close();
    }

    @Test(expected = RuntimeException.class)
    public void failFindTokensByClientIdAndUserName_1() throws Exception {
        this.failExecuteFindTokens_1("client_id", "username");
    }

    @Test(expected = RuntimeException.class)
    public void failFindTokensByClientId_1() throws Exception {
        this.failExecuteFindTokens_1("client_id", null);
    }

    @Test(expected = RuntimeException.class)
    public void failFindTokensByUserName_1() throws Exception {
        this.failExecuteFindTokens_1(null, "username");
    }

    private void failExecuteFindTokens_1(String clientId, String username) throws Exception {
        try {
            when(this.statForSearchId.executeQuery()).thenReturn(resForSearchId);
            Mockito.when(resForSearchId.next()).thenReturn(true).thenReturn(false);
            Mockito.when(resForSearchId.getString(Mockito.anyString())).thenReturn("token");
            when(this.stat.executeQuery()).thenReturn(res);
            Mockito.when(res.next()).thenReturn(true).thenReturn(false);
            when(res.getString(Mockito.anyString())).thenThrow(SQLException.class);
            List<OAuth2AccessToken> keys = null;
            if (StringUtils.isBlank(username)) {
                keys = this.tokenDAO.findTokensByClientId(clientId);
            } else if (StringUtils.isBlank(clientId)) {
                keys = this.tokenDAO.findTokensByUserName(username);
            } else {
                keys = this.tokenDAO.findTokensByClientIdAndUserName(clientId, username);
            }
            Assert.fail();
        } catch (RuntimeException e) {
            if (!StringUtils.isBlank(clientId) && !StringUtils.isBlank(username)) {
                Mockito.verify(statForSearchId, Mockito.times(2)).setString(Mockito.anyInt(), Mockito.anyString());
            } else {
                Mockito.verify(statForSearchId, Mockito.times(1)).setString(Mockito.anyInt(), Mockito.anyString());
            }
            Mockito.verify(resForSearchId, Mockito.times(1)).getString(Mockito.anyString());
            Mockito.verify(stat, Mockito.times(1)).setString(Mockito.anyInt(), Mockito.anyString());
            Mockito.verify(res, Mockito.times(1)).getString(Mockito.anyString());
            Mockito.verify(res, Mockito.times(0)).getTimestamp(Mockito.anyString());
            this.executeFinalCheck(true);
            this.executeFinalCheckForSearchId(true);
            Mockito.verify(conn, Mockito.times(2)).close();
            throw e;
        }
    }

    @Test(expected = RuntimeException.class)
    public void failFindTokensByClientIdAndUserName_2() throws Exception {
        this.failExecuteFindTokens_2("client_id", "username");
    }

    @Test(expected = RuntimeException.class)
    public void failFindTokensByClientId_2() throws Exception {
        this.failExecuteFindTokens_2("client_id", null);
    }

    @Test(expected = RuntimeException.class)
    public void failFindTokensByUserName_2() throws Exception {
        this.failExecuteFindTokens_2(null, "username");
    }

    private void failExecuteFindTokens_2(String clientId, String username) throws Exception {
        try {
            when(this.statForSearchId.executeQuery()).thenReturn(resForSearchId);
            when(resForSearchId.next()).thenReturn(true).thenReturn(false);
            when(resForSearchId.getString(Mockito.anyString())).thenThrow(SQLException.class);
            List<OAuth2AccessToken> keys = null;
            if (StringUtils.isBlank(username)) {
                keys = this.tokenDAO.findTokensByClientId(clientId);
            } else if (StringUtils.isBlank(clientId)) {
                keys = this.tokenDAO.findTokensByUserName(username);
            } else {
                keys = this.tokenDAO.findTokensByClientIdAndUserName(clientId, username);
            }
            Assert.fail();
        } catch (RuntimeException e) {
            if (!StringUtils.isBlank(clientId) && !StringUtils.isBlank(username)) {
                Mockito.verify(statForSearchId, Mockito.times(2)).setString(Mockito.anyInt(), Mockito.anyString());
            } else {
                Mockito.verify(statForSearchId, Mockito.times(1)).setString(Mockito.anyInt(), Mockito.anyString());
            }
            Mockito.verify(resForSearchId, Mockito.times(1)).getString(Mockito.anyString());
            Mockito.verify(stat, Mockito.times(0)).setString(Mockito.anyInt(), Mockito.anyString());
            Mockito.verify(res, Mockito.times(0)).getString(Mockito.anyString());
            Mockito.verify(res, Mockito.times(0)).getTimestamp(Mockito.anyString());
            this.executeFinalCheckForSearchId(true);
            Mockito.verify(conn, Mockito.times(1)).close();
            throw e;
        }
    }

    @Test
    public void storeAccessToken() throws Exception {
        when(this.stat.executeQuery()).thenReturn(res);
        Mockito.when(res.next()).thenReturn(false);
        this.tokenDAO.storeAccessToken(this.createMockAccessToken(), this.createMockAuthentication());
        Mockito.verify(stat, Mockito.times(6)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(stat, Mockito.times(1)).setTimestamp(Mockito.anyInt(), Mockito.any(Timestamp.class));
        Mockito.verify(res, Mockito.times(1)).close();
        Mockito.verify(stat, Mockito.times(2)).close();
        Mockito.verify(conn, Mockito.times(1)).close();
    }

    @Test(expected = RuntimeException.class)
    public void failStoreAccessToken() throws Exception {
        try {
            when(this.stat.executeQuery()).thenReturn(res);
            Mockito.when(res.next()).thenReturn(false);
            Mockito.doThrow(SQLException.class).when(stat).setTimestamp(Mockito.anyInt(), Mockito.any(Timestamp.class));
            this.tokenDAO.storeAccessToken(this.createMockAccessToken(), this.createMockAuthentication());
            Assert.fail();
        } catch (RuntimeException e) {
            Mockito.verify(stat, Mockito.times(3)).setString(Mockito.anyInt(), Mockito.anyString());
            Mockito.verify(stat, Mockito.times(1)).setTimestamp(Mockito.anyInt(), Mockito.any(Timestamp.class));
            Mockito.verify(res, Mockito.times(1)).close();
            Mockito.verify(stat, Mockito.times(2)).close();
            Mockito.verify(conn, Mockito.times(1)).close();
            throw e;
        }
    }

    @Test
    public void getAccessToken() throws Exception {
        when(this.stat.executeQuery()).thenReturn(res);
        Mockito.when(res.next()).thenReturn(true).thenReturn(false);
        OAuth2AccessToken token = this.tokenDAO.readAccessToken("token");
        Assert.assertNotNull(token);
        Mockito.verify(stat, Mockito.times(1)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(res, Mockito.times(4)).getString(Mockito.anyString());
        Mockito.verify(res, Mockito.times(1)).getTimestamp(Mockito.anyString());
        Mockito.verify(stat, Mockito.times(1)).close();
        Mockito.verify(res, Mockito.times(1)).close();
        Mockito.verify(conn, Mockito.times(1)).close();
    }

    @Test(expected = RuntimeException.class)
    public void failReadAccessToken() throws Exception {
        try {
            when(this.stat.executeQuery()).thenReturn(res);
            Mockito.when(res.next()).thenReturn(true).thenReturn(false);
            when(res.getTimestamp(Mockito.anyString())).thenThrow(SQLException.class);
            OAuth2AccessToken token = this.tokenDAO.readAccessToken("token");
            Assert.fail();
        } catch (RuntimeException e) {
            Mockito.verify(stat, Mockito.times(1)).setString(Mockito.anyInt(), Mockito.anyString());
            Mockito.verify(res, Mockito.times(4)).getString(Mockito.anyString());
            Mockito.verify(res, Mockito.times(1)).getTimestamp(Mockito.anyString());
            Mockito.verify(res, Mockito.times(1)).close();
            Mockito.verify(stat, Mockito.times(1)).close();
            Mockito.verify(conn, Mockito.times(1)).close();
            throw e;
        }
    }

    @Test
    public void removeAccessToken() throws Exception {
        this.tokenDAO.removeAccessToken("token");
        Mockito.verify(stat, Mockito.times(1)).setObject(Mockito.anyInt(), Mockito.any());
        Mockito.verify(stat, Mockito.times(1)).close();
        Mockito.verify(conn, Mockito.times(1)).close();
    }

    @Test(expected = RuntimeException.class)
    public void failRemoveAccessToken() throws Exception {
        try {
            Mockito.doThrow(SQLException.class).when(stat).setObject(Mockito.anyInt(), Mockito.any());
            this.tokenDAO.removeAccessToken("token");
            Assert.fail();
        } catch (RuntimeException e) {
            Mockito.verify(stat, Mockito.times(1)).close();
            Mockito.verify(conn, Mockito.times(1)).close();
            throw e;
        }
    }

    @Test
    public void deleteExpiredToken() throws Exception {
        this.tokenDAO.deleteExpiredToken(3600);
        Mockito.verify(stat, Mockito.times(1)).setTimestamp(Mockito.anyInt(), Mockito.any(Timestamp.class));
        Mockito.verify(stat, Mockito.times(1)).close();
        Mockito.verify(conn, Mockito.times(1)).close();
    }

    @Test(expected = RuntimeException.class)
    public void failDeleteExpiredToken() throws Exception {
        try {
            Mockito.doThrow(SQLException.class).when(stat).setTimestamp(Mockito.anyInt(), Mockito.any(Timestamp.class));
            this.tokenDAO.deleteExpiredToken(3600);
            Assert.fail();
        } catch (RuntimeException e) {
            Mockito.verify(stat, Mockito.times(1)).close();
            Mockito.verify(conn, Mockito.times(1)).close();
            throw e;
        }
    }

    @Test
    public void readRefreshToken() throws Exception {
        when(this.statForSearchId.executeQuery()).thenReturn(resForSearchId);
        Mockito.when(resForSearchId.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resForSearchId.getString(Mockito.anyString())).thenReturn("refresh_token");
        OAuth2RefreshToken refreshToken = this.tokenDAO.readRefreshToken("refresh_token");
        Assert.assertNotNull(refreshToken);
        Assert.assertEquals("refresh_token", refreshToken.getValue());
        Mockito.verify(statForSearchId, Mockito.times(1)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(resForSearchId, Mockito.times(1)).getString(Mockito.anyString());
        this.executeFinalCheckForSearchId(true);
        Mockito.verify(conn, Mockito.times(1)).close();
    }

    @Test
    public void readRefreshToken_noteExists() throws Exception {
        when(this.statForSearchId.executeQuery()).thenReturn(resForSearchId);
        Mockito.when(resForSearchId.next()).thenReturn(false);
        OAuth2RefreshToken refreshToken = this.tokenDAO.readRefreshToken("refresh_token");
        Assert.assertNull(refreshToken);
        Mockito.verify(statForSearchId, Mockito.times(1)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(resForSearchId, Mockito.times(0)).getString(Mockito.anyString());
        this.executeFinalCheckForSearchId(true);
        Mockito.verify(conn, Mockito.times(1)).close();
    }

    @Test(expected = RuntimeException.class)
    public void failReadRefreshToken() throws Exception {
        try {
            when(this.statForSearchId.executeQuery()).thenReturn(resForSearchId);
            when(resForSearchId.next()).thenReturn(true).thenReturn(false);
            when(resForSearchId.getString(Mockito.anyString())).thenThrow(SQLException.class);
            OAuth2RefreshToken refreshToken = this.tokenDAO.readRefreshToken("refresh");
            Assert.fail();
        } catch (RuntimeException e) {
            Mockito.verify(statForSearchId, Mockito.times(1)).setString(Mockito.anyInt(), Mockito.anyString());
            Mockito.verify(resForSearchId, Mockito.times(1)).getString(Mockito.anyString());
            this.executeFinalCheckForSearchId(true);
            Mockito.verify(conn, Mockito.times(1)).close();
            throw e;
        }
    }

    @Test
    public void readAuthenticationForRefreshToken() throws Exception {
        when(this.stat.executeQuery()).thenReturn(res);
        Mockito.when(res.next()).thenReturn(true).thenReturn(false);
        Mockito.when(res.getString("localuser")).thenReturn("username");
        Mockito.when(res.getString("clientid")).thenReturn("client_id");
        Mockito.when(res.getString("granttype")).thenReturn("password");
        OAuth2RefreshToken refreshToken = new DefaultOAuth2RefreshToken("value_X1");
        OAuth2Authentication auth = this.tokenDAO.readAuthenticationForRefreshToken(refreshToken);
        Assert.assertNotNull(auth);
        Assert.assertEquals("username", auth.getPrincipal());
        Assert.assertEquals("password", auth.getOAuth2Request().getGrantType());
        Mockito.verify(stat, Mockito.times(1)).setString(Mockito.anyInt(), Mockito.anyString());
        Mockito.verify(res, Mockito.times(3)).getString(Mockito.anyString());
        Mockito.verify(res, Mockito.times(0)).getTimestamp(Mockito.anyString());
        Mockito.verify(stat, Mockito.times(1)).close();
        Mockito.verify(res, Mockito.times(1)).close();
        Mockito.verify(conn, Mockito.times(1)).close();
    }

    @Test(expected = RuntimeException.class)
    public void failReadAuthenticationForRefreshToken() throws Exception {
        OAuth2RefreshToken refreshToken = new DefaultOAuth2RefreshToken("value_X2");
        try {
            when(this.stat.executeQuery()).thenReturn(res);
            Mockito.when(res.next()).thenReturn(true).thenReturn(false);
            Mockito.when(res.getString("localuser")).thenReturn("username");
            Mockito.when(res.getString("clientid")).thenThrow(SQLException.class);
            Mockito.when(res.getString("granttype")).thenReturn("password");
            OAuth2Authentication auth = this.tokenDAO.readAuthenticationForRefreshToken(refreshToken);
            Assert.fail();
        } catch (RuntimeException e) {
            Mockito.verify(stat, Mockito.times(1)).setString(Mockito.anyInt(), Mockito.anyString());
            Mockito.verify(res, Mockito.times(2)).getString(Mockito.anyString());
            Mockito.verify(res, Mockito.times(0)).getTimestamp(Mockito.anyString());
            Mockito.verify(stat, Mockito.times(1)).close();
            Mockito.verify(res, Mockito.times(1)).close();
            Mockito.verify(conn, Mockito.times(1)).close();
            throw e;
        }
    }

    private OAuth2AccessToken createMockAccessToken() {
        OAuth2AccessTokenImpl token = new OAuth2AccessTokenImpl("token");
        token.setValue("token");
        token.setClientId("client_id");
        token.setExpiration(new Date());
        token.setGrantType("password");
        token.setLocalUser("username");
        token.setRefreshToken(new DefaultOAuth2RefreshToken("refresh"));
        token.setTokenType("bearer");
        return token;
    }

    private OAuth2Authentication createMockAuthentication() {
        TestingAuthenticationToken mock = new TestingAuthenticationToken("username", "password");
        OAuth2Request oAuth2Request = new OAuth2Request(null, "clientId", null, true, null, null, null, null, null);
        OAuth2Authentication authentication = new OAuth2Authentication(oAuth2Request, mock);
        return authentication;
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
