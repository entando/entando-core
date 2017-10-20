/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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

import org.entando.entando.aps.system.services.oauth2.model.OAuth2Token;


public interface IOAuth2TokenDAO {

    void addAccessToken(final OAuth2Token accessor);

    OAuth2Token getAccessToken(final String accessToken);

    void deleteAccessToken(final String accessToken);

    void deleteExpiredToken();

}
