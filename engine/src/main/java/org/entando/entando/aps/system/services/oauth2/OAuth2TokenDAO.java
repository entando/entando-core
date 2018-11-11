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

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2AccessTokenImpl;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class OAuth2TokenDAO extends AbstractSearcherDAO implements IOAuth2TokenDAO {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2TokenDAO.class);

    private static final String ERROR_REMOVE_ACCESS_TOKEN = "Error while remove access token";

    private static final String INSERT_TOKEN = "INSERT INTO api_oauth_tokens (accesstoken, clientid, expiresin, refreshtoken, granttype, localuser)  VALUES (? , ? , ? , ? , ?, ?)";

    private static final String DELETE_EXPIRED_TOKENS = "DELETE FROM api_oauth_tokens WHERE expiresin < ?";

    private static final String SELECT_TOKEN = "SELECT * FROM api_oauth_tokens WHERE accesstoken = ? ";

    private static final String DELETE_TOKEN = "DELETE FROM api_oauth_tokens WHERE accesstoken = ? ";

    @Override
    protected String getTableFieldName(String metadataFieldKey) {
        return metadataFieldKey;
    }

    @Override
    protected String getMasterTableName() {
        return "api_oauth_tokens";
    }

    @Override
    protected String getMasterTableIdFieldName() {
        return "accesstoken";
    }

    @Override
    public List<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String username) {
        if (StringUtils.isBlank(clientId) && StringUtils.isBlank(username)) {
            throw new RuntimeException("clientId and username cannot both be null");
        }
        FieldSearchFilter expirationFilter = new FieldSearchFilter("expiresin");
        expirationFilter.setOrder(FieldSearchFilter.Order.ASC);
        FieldSearchFilter[] filters = {expirationFilter};
        if (!StringUtils.isBlank(clientId)) {
            FieldSearchFilter clientIdFilter = new FieldSearchFilter("clientid", clientId, true);
            filters = ArrayUtils.add(filters, clientIdFilter);
        }
        if (!StringUtils.isBlank(username)) {
            FieldSearchFilter usernameFilter = new FieldSearchFilter("localuser", username, true);
            filters = ArrayUtils.add(filters, usernameFilter);
        }
        List<OAuth2AccessToken> accessTokens = new ArrayList<>();
        List<String> tokens = super.searchId(filters);
        if (tokens.isEmpty()) {
            return accessTokens;
        }
        Connection conn = null;
        try {
            conn = this.getConnection();
            for (String token : tokens) {
                OAuth2AccessToken accessToken = this.getAccessToken(token, conn);
                if (!accessToken.isExpired()) {
                    accessTokens.add(accessToken);
                }
            }
        } catch (Exception t) {
            logger.error("Error while loading tokens", t);
            throw new RuntimeException("Error while loading tokens", t);
        } finally {
            this.closeConnection(conn);
        }
        return accessTokens;
    }

    protected OAuth2AccessToken getAccessToken(final String token, Connection conn) {
        OAuth2AccessTokenImpl accessToken = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            stat = conn.prepareStatement(SELECT_TOKEN);
            stat.setString(1, token);
            res = stat.executeQuery();
            if (res.next()) {
                accessToken = new OAuth2AccessTokenImpl(token);
                accessToken.setRefreshToken(new DefaultOAuth2RefreshToken(res.getString("refreshtoken")));
                accessToken.setClientId(res.getString("clientid"));
                accessToken.setGrantType(res.getString("granttype"));
                accessToken.setLocalUser(res.getString("localuser"));
                Timestamp timestamp = res.getTimestamp("expiresin");
                Date expiration = new Date(timestamp.getTime());
                accessToken.setExpiration(expiration);
            }
        } catch (Throwable t) {
            logger.error("Error loading token {}", token, t);
            throw new RuntimeException("Error loading token " + token, t);
        } finally {
            closeDaoResources(res, stat);
        }
        return accessToken;
    }

    @Override
    public List<OAuth2AccessToken> findTokensByUserName(String username) {
        return this.findTokensByClientIdAndUserName(null, username);
    }
    
    @Override
    public List<OAuth2AccessToken> findTokensByClientId(String clientId) {
        return this.findTokensByClientIdAndUserName(clientId, null);
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = this.getConnection();
            String tokenValue = accessToken.getValue();
            if (null != this.getAccessToken(tokenValue, conn)) {
                logger.debug("storeAccessToken: Stored Token already exists");
                return;
            }
            conn.setAutoCommit(false);
            stat = conn.prepareStatement(INSERT_TOKEN);
            stat.setString(1, accessToken.getValue());
            if (accessToken instanceof OAuth2AccessTokenImpl) {
                stat.setString(2, ((OAuth2AccessTokenImpl) accessToken).getClientId());
            } else if (null != authentication.getOAuth2Request()) {
                stat.setString(2, authentication.getOAuth2Request().getClientId());
            } else {
                stat.setNull(2, Types.VARCHAR);
            }
            stat.setTimestamp(3, new Timestamp(accessToken.getExpiration().getTime()));
            stat.setString(4, accessToken.getRefreshToken().getValue());
            if (accessToken instanceof OAuth2AccessTokenImpl) {
                stat.setString(5, ((OAuth2AccessTokenImpl) accessToken).getGrantType());
                stat.setString(6, ((OAuth2AccessTokenImpl) accessToken).getLocalUser());
            } else {
                if (null != authentication.getOAuth2Request()) {
                    stat.setString(5, authentication.getOAuth2Request().getGrantType());
                } else {
                    stat.setNull(5, Types.VARCHAR);
                }
                stat.setNull(6, Types.VARCHAR);
            }
            stat.executeUpdate();
            conn.commit();
        } catch (Exception t) {
            this.executeRollback(conn);
            logger.error("Error while adding an access token", t);
            throw new RuntimeException("Error while adding an access token", t);
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }

    @Override
    public OAuth2AccessToken getAccessToken(final String accessToken) {
        Connection conn = null;
        OAuth2AccessToken token = null;
        try {
            conn = this.getConnection();
            token = this.getAccessToken(accessToken, conn);
        } catch (Exception t) {
            logger.error("Error while loading token {}", accessToken, t);
            throw new RuntimeException("Error while loading token " + accessToken, t);
        } finally {
            this.closeConnection(conn);
        }
        return token;
    }

    @Override
    public void deleteAccessToken(final String accessToken) {
        super.executeQueryWithoutResultset(DELETE_TOKEN, accessToken);
    }

    @Override
    public void deleteExpiredToken() {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            stat = conn.prepareStatement(DELETE_EXPIRED_TOKENS);
            stat.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            stat.executeUpdate();
            conn.commit();
        } catch (Exception t) {
            this.executeRollback(conn);
            logger.error(ERROR_REMOVE_ACCESS_TOKEN, t);
            throw new RuntimeException(ERROR_REMOVE_ACCESS_TOKEN, t);
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }

}
