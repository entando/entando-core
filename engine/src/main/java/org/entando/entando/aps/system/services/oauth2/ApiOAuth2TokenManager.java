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

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.Calendar;
import java.util.Collection;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.commons.codec.digest.DigestUtils;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2AccessTokenImpl;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class ApiOAuth2TokenManager extends AbstractService implements IApiOAuth2TokenManager {

    private static final Logger logger = LoggerFactory.getLogger(ApiOAuth2TokenManager.class);
    private static final String ERROR_ADDING_TOKEN = "Error adding OAuth2Token";
    private transient final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private IOAuth2TokenDAO oAuth2TokenDAO;

    @Override
    public void init() throws Exception {
        logger.debug("{}  initialized ", this.getClass().getName());
        scheduler.scheduleAtFixedRate(new ScheduledDeleteExpiredTokenThread(oAuth2TokenDAO), 0, 1, TimeUnit.HOURS);
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        logger.warn("readAuthentication Not supported yet.");
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OAuth2Authentication readAuthentication(String tokenValue) {
        logger.warn("readAuthentication Not supported yet.");
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        this.getOAuth2TokenDAO().storeAccessToken(accessToken, authentication);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        logger.warn("readAccessToken Not supported yet.");
        throw new UnsupportedOperationException("readAccessToken Not supported yet.");
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        logger.warn("removeAccessToken Not supported yet.");
        throw new UnsupportedOperationException("removeAccessToken Not supported yet.");
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        logger.warn("storeRefreshToken Not supported yet.");
        throw new UnsupportedOperationException("storeRefreshToken Not supported yet.");
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        logger.warn("readRefreshToken Not supported yet.");
        throw new UnsupportedOperationException("readRefreshToken Not supported yet.");
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken refreshToken) {
        logger.warn("readAuthenticationForRefreshToken Not supported yet.");
        throw new UnsupportedOperationException("readAuthenticationForRefreshToken Not supported yet.");
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
        logger.warn("removeRefreshToken Not supported yet.");
        throw new UnsupportedOperationException("removeRefreshToken Not supported yet.");
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        logger.warn("removeAccessTokenUsingRefreshToken Not supported yet.");
        throw new UnsupportedOperationException("removeAccessTokenUsingRefreshToken Not supported yet.");
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        return this.createToken(authentication, "implicit");
    }

    private OAuth2AccessToken createToken(OAuth2Authentication authentication, String grantType) {
        String tokenPrefix = authentication.getPrincipal().toString() + System.nanoTime();
        final String accessToken = DigestUtils.md5Hex(tokenPrefix + "_accessToken");
        final String refreshToken = DigestUtils.md5Hex(tokenPrefix + "_refreshToken");
        final OAuth2AccessTokenImpl oAuth2Token = new OAuth2AccessTokenImpl(accessToken);
        oAuth2Token.setRefreshToken(new DefaultOAuth2RefreshToken(refreshToken));
        if (null != authentication.getOAuth2Request()) {
            oAuth2Token.setClientId(authentication.getOAuth2Request().getClientId());
        }
        oAuth2Token.setLocalUser(authentication.getPrincipal().toString());
        Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
        calendar.add(Calendar.SECOND, 3600);
        oAuth2Token.setExpiration(calendar.getTime());
        oAuth2Token.setGrantType(grantType);
        return oAuth2Token;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String username) {
        return this.getOAuth2TokenDAO().findTokensByClientIdAndUserName(clientId, username);
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        return this.getOAuth2TokenDAO().findTokensByClientId(clientId);
    }

    @Override
    @Deprecated
    public void addApiOAuth2Token(final OAuth2Token accessToken, final boolean isLocalUser) throws ApsSystemException {
        try {
            this.getOAuth2TokenDAO().addAccessToken(accessToken, isLocalUser);
        } catch (ApsSystemException t) {
            logger.error(ERROR_ADDING_TOKEN, t);
            throw new ApsSystemException(ERROR_ADDING_TOKEN, t);
        }
    }

    @Override
    public OAuth2AccessToken getApiOAuth2Token(final String accessToken) throws ApsSystemException {
        try {
            return this.getOAuth2TokenDAO().getAccessToken(accessToken);
        } catch (Exception t) {
            logger.error(ERROR_ADDING_TOKEN, t);
            throw new ApsSystemException(ERROR_ADDING_TOKEN, t);
        }
    }

    @Override
    public void updateToken(final String accessToken, final long seconds) throws ApsSystemException {
        try {
            this.getOAuth2TokenDAO().updateAccessToken(accessToken, seconds);
        } catch (ApsSystemException t) {
            logger.error(ERROR_ADDING_TOKEN, t);
            throw new ApsSystemException(ERROR_ADDING_TOKEN, t);
        }
    }

    protected IOAuth2TokenDAO getOAuth2TokenDAO() {
        return oAuth2TokenDAO;
    }

    public void setOAuth2TokenDAO(IOAuth2TokenDAO oAuth2TokenDAO) {
        this.oAuth2TokenDAO = oAuth2TokenDAO;
    }

}
