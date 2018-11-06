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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.oauth2.IApiOAuth2TokenManager;
import org.springframework.security.crypto.codec.Base64;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.junit.Assert;

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
    
    @Test
    public void obtainAccessToken_1() throws Exception {
        this.obtainAccessToken_1("admin", "admin");
        this.obtainAccessToken_1("mainEditor", "mainEditor");
        this.obtainAccessToken_1("supervisorCustomers", "supervisorCustomers");
    }
    
    private void obtainAccessToken_1(String username, String password) throws Exception {
        OAuth2AccessToken oauthToken = null;
        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "password");
            params.add("username", username);
            params.add("password", password);
            ResultActions result
                    = mockMvc.perform(post("/oauth/token")
                            .params(params)
                            .header("Authorization", "Basic " + new String(Base64.encode("test_consumer:secret".getBytes())))
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
    
}
