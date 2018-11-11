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
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public interface IOAuth2TokenDAO {

    public void storeAccessToken(OAuth2AccessToken accessToken, OAuth2Authentication authentication);

    public List<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String username);

    public List<OAuth2AccessToken> findTokensByClientId(String clientId);
    
    public List<OAuth2AccessToken> findTokensByUserName(String username);

    public OAuth2AccessToken getAccessToken(final String accessToken);

    public void deleteAccessToken(final String accessToken);

    public void deleteExpiredToken();

}
