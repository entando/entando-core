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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

/**
 * @author E.Santoboni
 */
public class ApiOAuth2TokenManagerTest {
    
    @Mock
    private OAuth2TokenDAO tokenDAO;
    
    @InjectMocks
    private ApiOAuth2TokenManager tokenManager;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void findTokensByUserName() {
        when(tokenDAO.findTokensByClientIdAndUserName(Mockito.anyString(), Mockito.anyString())).thenReturn(new ArrayList<>());
        Collection<OAuth2AccessToken> tokens = tokenManager.findTokensByUserName("username");
        Assert.assertNotNull(tokens);
    }

    @Test
    public void findTokensByClientIdAndUserName() {
        when(tokenDAO.findTokensByClientIdAndUserName(Mockito.anyString(), Mockito.anyString())).thenReturn(new ArrayList<>());
        Collection<OAuth2AccessToken> tokens = tokenManager.findTokensByClientIdAndUserName("clientId", "username");
        Assert.assertNotNull(tokens);
    }

    @Test
    public void findTokensByClientId() {
        when(tokenDAO.findTokensByClientIdAndUserName(Mockito.anyString(), Mockito.anyString())).thenReturn(new ArrayList<>());
        Collection<OAuth2AccessToken> tokens = tokenManager.findTokensByClientId("clientId");
        Assert.assertNotNull(tokens);
    }
    
    @Test
    public void createAccessTokenForLocalUser() {
        OAuth2AccessToken token = this.tokenManager.createAccessTokenForLocalUser("username");
        Assert.assertNotNull(token);
        Mockito.verify(tokenDAO, Mockito.times(1)).storeAccessToken(Mockito.any(OAuth2AccessToken.class), Mockito.eq(null));
        Assert.assertTrue(token instanceof OAuth2AccessTokenImpl);
        Assert.assertEquals("LOCAL_USER", ((OAuth2AccessTokenImpl)token).getClientId());
    }
    
    @Test
    public void getApiOAuth2Token() throws Exception {
        when(tokenDAO.getAccessToken(Mockito.anyString())).thenReturn(new OAuth2AccessTokenImpl("token"));
        OAuth2AccessToken token = tokenManager.getApiOAuth2Token("token");
        Assert.assertNotNull(token);
        Assert.assertTrue(token instanceof OAuth2AccessTokenImpl);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void readAuthentication_1() throws Exception {
        this.tokenManager.readAuthentication(this.createMockAccessToken());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void readAuthentication_2() throws Exception {
        this.tokenManager.readAuthentication("token");
    }
    
    @Test
    public void storeAccessToken() {
        this.tokenManager.storeAccessToken(this.createMockAccessToken(), this.createMockAuthentication());
        Mockito.verify(tokenDAO, Mockito.times(1)).storeAccessToken(Mockito.any(OAuth2AccessToken.class), Mockito.any(OAuth2Authentication.class));
    }
    
    @Test
    public void readAccessToken() throws Exception {
        when(tokenDAO.getAccessToken(Mockito.anyString())).thenReturn(new OAuth2AccessTokenImpl("token"));
        OAuth2AccessToken token = tokenManager.readAccessToken("token");
        Assert.assertNotNull(token);
        Assert.assertTrue(token instanceof OAuth2AccessTokenImpl);
        Assert.assertEquals("token", token.getValue());
    }
    
    @Test
    public void removeAccessToken() throws Exception {
        this.tokenManager.removeAccessToken(this.createMockAccessToken());
        Mockito.verify(tokenDAO, Mockito.times(1)).deleteAccessToken(Mockito.anyString());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void storeRefreshToken() throws Exception {
        this.tokenManager.storeRefreshToken(new DefaultOAuth2RefreshToken("value"), this.createMockAuthentication());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void readRefreshToken() throws Exception {
        this.tokenManager.readRefreshToken("refresh_token");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void readAuthenticationForRefreshToken() throws Exception {
        this.tokenManager.readAuthenticationForRefreshToken(new DefaultOAuth2RefreshToken("value"));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void removeRefreshToken() throws Exception {
        this.tokenManager.removeRefreshToken(new DefaultOAuth2RefreshToken("value"));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void removeAccessTokenUsingRefreshToken() throws Exception {
        this.tokenManager.removeAccessTokenUsingRefreshToken(new DefaultOAuth2RefreshToken("value"));
    }
    
    @Test
    public void getAccessToken() throws Exception {
        OAuth2AccessToken token = tokenManager.getAccessToken(this.createMockAuthentication());
        Assert.assertNotNull(token);
        Assert.assertTrue(token instanceof OAuth2AccessTokenImpl);
        Assert.assertEquals("clientId", ((OAuth2AccessTokenImpl) token).getClientId());
        Assert.assertEquals("username", ((OAuth2AccessTokenImpl) token).getLocalUser());
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
    
}
