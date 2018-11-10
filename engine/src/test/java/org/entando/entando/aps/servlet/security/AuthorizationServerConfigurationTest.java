/*
 * Copyright 2018-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.servlet.security;

import com.jayway.jsonpath.JsonPath;
import java.util.Collection;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.oauth2.IApiOAuth2TokenManager;
import org.springframework.security.crypto.codec.Base64;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import org.junit.Before;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class AuthorizationServerConfigurationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IApiOAuth2TokenManager apiOAuth2TokenManager;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.removeTokens("admin", "mainEditor", "supervisorCustomers");
    }

    @Test
    public void obtainAccessToken() throws Exception {
        this.obtainAccessToken("admin", "admin");
        this.obtainAccessToken("mainEditor", "mainEditor");
        this.obtainAccessToken("supervisorCustomers", "supervisorCustomers");
    }

    private void obtainAccessToken(String username, String password) throws Exception {
        OAuth2AccessToken oauthToken = null;
        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "password");
            params.add("username", username);
            params.add("password", password);
            String hash = new String(Base64.encode("test1_consumer:secret".getBytes()));
            ResultActions result
                    = mockMvc.perform(post("/oauth/token")
                            .params(params)
                            .header("Authorization", "Basic " + hash)
                            .accept("application/json;charset=UTF-8"))
                            .andExpect(status().isOk())
                            .andExpect(content().contentType("application/json;charset=UTF-8"));
            String resultString = result.andReturn().getResponse().getContentAsString();
            Assert.assertTrue(StringUtils.isNotBlank(resultString));
            String token = JsonPath.parse(resultString).read("$.access_token");
            Assert.assertTrue(StringUtils.isNotBlank(token));
            Collection<OAuth2AccessToken> oauthTokens = apiOAuth2TokenManager.findTokensByUserName(username);
            Assert.assertEquals(1, oauthTokens.size());
            oauthToken = oauthTokens.stream().findFirst().get();
            Assert.assertEquals(token, oauthToken.getValue());
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != oauthToken) {
                this.apiOAuth2TokenManager.removeAccessToken(oauthToken);
            }
        }
    }

    @Test
    public void authenticationFailed() throws Exception {
        this.authenticationFailed("admin", "adminxx");
        this.authenticationFailed("admin", "");
        this.authenticationFailed("", "admin");
        this.authenticationFailed("mainEditor", "mainEditorxx");
        this.authenticationFailed("supervisorCustomers", "supervisorCustomersxx");
    }

    private void authenticationFailed(String username, String password) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", username);
        params.add("password", password);
        String hash = new String(Base64.encode("test1_consumer:secret".getBytes()));
        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                        .params(params)
                        .header("Authorization", "Basic " + hash)
                        .accept("application/json;charset=UTF-8"))
                        .andExpect(status().isUnauthorized())
                        .andExpect(content().contentType("application/json;charset=UTF-8"));
        String resultString = result.andReturn().getResponse().getContentAsString();
        Assert.assertTrue(StringUtils.isNotBlank(resultString));
        result.andExpect(jsonPath("$.error", is("unauthorized")));
        result.andExpect(jsonPath("$.error_description", anything()));
        if (!StringUtils.isEmpty(username)) {
            Collection<OAuth2AccessToken> oauthTokens = apiOAuth2TokenManager.findTokensByUserName(username);
            Assert.assertEquals(0, oauthTokens.size());
        }
    }

    @Test
    public void invalidClient() throws Exception {
        this.invalidClient("mainEditor", "mainEditor", "test2_consumer", "secret", "password");
    }

    private void invalidClient(String username, String password, String clientId, String secret, String grantType) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grantType);
        params.add("username", username);
        params.add("password", password);
        String hash = new String(Base64.encode((clientId + ":" + secret).getBytes()));
        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                        .params(params)
                        .header("Authorization", "Basic " + hash)
                        .accept("application/json;charset=UTF-8"))
                        .andExpect(status().isUnauthorized())
                        .andExpect(content().contentType("application/json;charset=UTF-8"));
        String resultString = result.andReturn().getResponse().getContentAsString();
        Assert.assertTrue(StringUtils.isNotBlank(resultString));
        result.andExpect(jsonPath("$.error", is("invalid_client")));
        String expectedMessage = "Unauthorized grant type: " + grantType;
        result.andExpect(jsonPath("$.error_description", is(expectedMessage)));
        Collection<OAuth2AccessToken> oauthTokens = apiOAuth2TokenManager.findTokensByUserName(username);
        Assert.assertEquals(0, oauthTokens.size());
    }

    @Test
    public void missingGrant() throws Exception {
        this.missingGrant("admin", "admin", "test1_consumer", "secret", "");
        this.missingGrant("admin", "adminxx", "test1_consumer", "secret", "");
        this.missingGrant("mainEditor", "mainEditor", "test1_consumer", "secret", "");
        this.missingGrant("mainEditor", "mainEditorxx", "test1_consumer", "secret", "");
        this.missingGrant("mainEditor", "mainEditor", "test1_consumer", "secret", null);
        this.missingGrant("mainEditor", "mainEditor", "test2_consumer", "secret", "");
    }

    private void missingGrant(String username, String password, String clientId, String secret, String grantType) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grantType);
        params.add("username", username);
        params.add("password", password);
        String hash = new String(Base64.encode((clientId + ":" + secret).getBytes()));
        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                        .params(params)
                        .header("Authorization", "Basic " + hash)
                        .accept("application/json;charset=UTF-8"))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType("application/json;charset=UTF-8"));
        String resultString = result.andReturn().getResponse().getContentAsString();
        Assert.assertTrue(StringUtils.isNotBlank(resultString));
        result.andExpect(jsonPath("$.error", is("invalid_request")));
        result.andExpect(jsonPath("$.error_description", is("Missing grant type")));
        Collection<OAuth2AccessToken> oauthTokens = apiOAuth2TokenManager.findTokensByUserName(username);
        Assert.assertEquals(0, oauthTokens.size());
    }

    @Test
    public void unauthorized() throws Exception {
        this.unauthorized("admin", "admin", "test1_consumer", "secretwrong");
        this.unauthorized("admin", "admin", "", "secret");
        this.unauthorized("admin", "admin", "test1_consumer", "");
        this.unauthorized("admin", "admin", null, "secret");
        this.unauthorized("admin", "admin", "test1_consumer", null);
    }

    private void unauthorized(String username, String password, String clientId, String secret) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", username);
        params.add("password", password);
        String hash = new String(Base64.encode((clientId + ":" + secret).getBytes()));
        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                        .params(params)
                        .header("Authorization", "Basic " + hash)
                        .accept("application/json;charset=UTF-8"))
                        .andExpect(status().isUnauthorized());
        String resultString = result.andReturn().getResponse().getContentAsString();
        Assert.assertTrue(StringUtils.isBlank(resultString));
        if (!StringUtils.isEmpty(username)) {
            Collection<OAuth2AccessToken> oauthTokens = apiOAuth2TokenManager.findTokensByUserName(username);
            Assert.assertEquals(0, oauthTokens.size());
        }
    }

    private void removeTokens(String... usernames) {
        for (String username : usernames) {
            Collection<OAuth2AccessToken> oauthTokens = apiOAuth2TokenManager.findTokensByUserName(username);
            oauthTokens.stream().forEach(oaat -> apiOAuth2TokenManager.removeAccessToken(oaat));
        }
    }

}
