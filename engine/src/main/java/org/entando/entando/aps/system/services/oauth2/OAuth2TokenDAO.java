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
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2AccessTokenImpl;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class OAuth2TokenDAO extends AbstractSearcherDAO implements IOAuth2TokenDAO {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2TokenDAO.class);

    private static final String ERROR_REMOVE_ACCESS_TOKEN = "Error while remove access token";

    private static final String INSERT_TOKEN = "INSERT INTO api_oauth_tokens (accesstoken, clientid, expiresin, refreshtoken, granttype, localuser)  VALUES (? , ? , ? , ? , ?, ?)";

    private static final String UPDATE_TOKEN = "UPDATE api_oauth_tokens SET expiresin = ? WHERE accesstoken = ?";

    private static final String DELETE_EXPIRED_TOKENS = "DELETE FROM api_oauth_tokens WHERE expiresin < ?";

    private static final String SELECT_TOKEN = "SELECT * FROM api_oauth_tokens WHERE accesstoken = ? ";

    private static final String DELETE_TOKEN = "DELETE FROM api_oauth_tokens WHERE accesstoken = ? ";

    @Override
    protected String getTableFieldName(String metadataFieldKey) {
        return metadataFieldKey;
    }

    @Override
    protected String getMasterTableName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getMasterTableIdFieldName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<OAuth2AccessToken> findTokensByClientId(String clientId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            stat = conn.prepareStatement(INSERT_TOKEN);
            stat.setString(1, accessToken.getValue());
            if (null != authentication.getOAuth2Request()) {
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
                stat.setNull(5, Types.VARCHAR);
                stat.setNull(6, Types.VARCHAR);
            }
            stat.executeUpdate();
            conn.commit();
        } catch (ApsSystemException | SQLException t) {
            this.executeRollback(conn);
            logger.error("Error while adding an access token", t);
            throw new RuntimeException("Error while adding an access token", t);
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }

    @Override
    @Deprecated
    public synchronized void addAccessToken(final OAuth2Token accessToken, boolean isLocalUser) throws ApsSystemException {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            stat = conn.prepareStatement(INSERT_TOKEN);
            stat.setString(1, accessToken.getAccessToken());
            stat.setString(2, accessToken.getClientId());
            stat.setTimestamp(3, new Timestamp(accessToken.getExpiresIn().getTime()));
            stat.setString(4, accessToken.getRefreshToken());
            stat.setString(5, accessToken.getGrantType());
            stat.setString(6, accessToken.getLocalUser());
            stat.executeUpdate();
            conn.commit();
        } catch (ApsSystemException | SQLException t) {
            this.executeRollback(conn);
            logger.error("Error while adding an access token", t);
            throw new ApsSystemException("Error while adding an access token", t);
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }

    @Override
    public synchronized void updateAccessToken(final String accessToken, long seconds) throws ApsSystemException {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            stat = conn.prepareStatement(UPDATE_TOKEN);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis() + seconds * 1000);
            stat.setTimestamp(1, timestamp);
            stat.setString(2, accessToken);
            stat.executeUpdate();
            conn.commit();
        } catch (ApsSystemException | SQLException t) {
            this.executeRollback(conn);
            logger.error("Error while update access token", t);
            throw new ApsSystemException("Error while update access token", t);
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }

    @Override
    public OAuth2Token getAccessToken(final String accessToken) throws ApsSystemException {
        Connection conn = null;
        OAuth2Token token = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            conn = this.getConnection();
            stat = conn.prepareStatement(SELECT_TOKEN);
            stat.setString(1, accessToken);
            res = stat.executeQuery();
            if (res.next()) {
                token = new OAuth2Token();
                token.setAccessToken(accessToken);
                token.setRefreshToken(res.getString("refreshtoken"));
                token.setClientId(res.getString("clientid"));
                token.setGrantType(res.getString("granttype"));
                token.setExpiresIn(res.getTimestamp("expiresin"));
            }
        } catch (ApsSystemException | SQLException t) {
            logger.error("Error while loading token {}", accessToken, t);
            throw new ApsSystemException("Error while loading token " + accessToken, t);
        } finally {
            closeDaoResources(res, stat, conn);
        }
        return token;
    }

    @Override
    public void deleteAccessToken(final String accessToken) throws ApsSystemException {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            stat = conn.prepareStatement(DELETE_TOKEN);
            stat.setString(1, accessToken);
            stat.executeUpdate();
            conn.commit();
        } catch (ApsSystemException | SQLException t) {
            this.executeRollback(conn);
            logger.error(ERROR_REMOVE_ACCESS_TOKEN, t);
            throw new ApsSystemException(ERROR_REMOVE_ACCESS_TOKEN, t);
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }

    @Override
    public void deleteExpiredToken() throws ApsSystemException {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            stat = conn.prepareStatement(DELETE_EXPIRED_TOKENS);
            stat.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            stat.executeUpdate();
            conn.commit();
        } catch (SQLException t) {
            this.executeRollback(conn);
            logger.error(ERROR_REMOVE_ACCESS_TOKEN, t);
            throw new ApsSystemException(ERROR_REMOVE_ACCESS_TOKEN, t);
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }

}
