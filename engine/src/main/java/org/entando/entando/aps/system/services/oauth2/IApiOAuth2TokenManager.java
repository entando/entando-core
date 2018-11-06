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
package org.entando.entando.aps.system.services.oauth2;

import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.Collection;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;

public interface IApiOAuth2TokenManager extends TokenStore {

    String BEAN_NAME = "OAuth2TokenManager";

    public Collection<OAuth2AccessToken> findTokensByUserName(String username);

    public OAuth2AccessToken createAccessTokenForLocalUser(String username);

    public OAuth2AccessToken getApiOAuth2Token(final String accessToken) throws ApsSystemException;

}
