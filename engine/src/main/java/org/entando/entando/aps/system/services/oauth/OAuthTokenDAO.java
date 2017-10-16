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
package org.entando.entando.aps.system.services.oauth;

import com.agiletec.aps.system.common.AbstractDAO;
import org.apache.oltu.oauth2.common.token.OAuthToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.Date;

/**
 * @author E.Santoboni
 */
public class OAuthTokenDAO extends AbstractDAO implements IOAuthTokenDAO {

    private static final Logger _logger = LoggerFactory.getLogger(OAuthTokenDAO.class);

    public void addAccessToken(String clientId, OAuthToken accessor) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            stat = conn.prepareStatement(INSERT_TOKEN);
            stat.setString(1, accessor.getAccessToken());
            stat.setString(2, clientId);
            stat.setString(3, accessor.getTokenType());
            stat.setLong(4, accessor.getExpiresIn());
            stat.setString(5, accessor.getRefreshToken());
            stat.setString(6, accessor.getScope());
            stat.executeUpdate();
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error while adding an access token", t);
            throw new RuntimeException("Error while adding an access token", t);
            //processDaoException(t, "Error while adding an access token", "addAccessToken");
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }


    public void refreshAccessTokens(String tokenToUpdate, int tokenTimeValidity) {
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.updateAccessTokens(tokenToUpdate, conn);
            this.deleteOldAccessTokens(tokenTimeValidity, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error refreshing access tokens", t);
            throw new RuntimeException("Error refreshing access tokens", t);
            //this.processDaoException(t, "Error refreshing access tokens", "refreshAccessTokens");
        } finally {
            this.closeConnection(conn);
        }
    }

    protected void updateAccessTokens(String tokenToUpdate, Connection conn) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(UPDATE_TOKEN);
            stat.setDate(1, new java.sql.Date(new Date().getTime()));
            stat.setString(2, tokenToUpdate);
            stat.executeUpdate();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error updating an access token", t);
            throw new RuntimeException("Error updating an access token", t);
            //this.processDaoException(t, "Error updating an access token", "updateAccessTokens");
        } finally {
            this.closeDaoResources(null, stat);
        }
    }

    protected void deleteOldAccessTokens(int tokenTimeValidity, Connection conn) {
        PreparedStatement stat = null;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -tokenTimeValidity);
            stat = conn.prepareStatement(DELETE_OLD_TOKENS);
            stat.setDate(1, new java.sql.Date(calendar.getTime().getTime()));
            stat.executeUpdate();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error deleting old access token", t);
            throw new RuntimeException("Error deleting old access token", t);
            //this.processDaoException(t, "Error deleting old access token", "deleteOldAccessTokens");
        } finally {
            this.closeDaoResources(null, stat);
        }
    }

    /*
            public EntandoOAuthAccessor getAccessor(String accessToken, OAuthConsumer consumer) {
                Connection conn = null;
                EntandoOAuthAccessor accessor = null;
                PreparedStatement stat = null;
                ResultSet res = null;
                try {
                    String consumer_key = (String) consumer.getProperty("name");
                    conn = this.getConnection();
                    stat = conn.prepareStatement(SELECT_TOKEN);
                    stat.setString(1, accessToken);
                    stat.setString(2, consumer_key);
                    res = stat.executeQuery();
                    if (res.next()) {
                        String tokensecret = res.getString(1);
                        String username = res.getString(2);
                        Date lastAccess = res.getDate(3);
                        accessor = new EntandoOAuthAccessor(consumer);
                        accessor.accessToken = accessToken;
                        accessor.tokenSecret = tokensecret;
                        accessor.setProperty("user", username);
                        accessor.setProperty("authorized", Boolean.TRUE);
                        accessor.setLastAccess(lastAccess);
                    }
                } catch (Throwable t) {
                    _logger.error("Error while loading accessor {}", accessToken,  t);
                    throw new RuntimeException("Error while loading accessor " + accessToken, t);
                    //processDaoException(t, "Error while loading accessor " + accessToken, "getAccessor");
                } finally {
                    closeDaoResources(res, stat, conn);
                }
                return accessor;
            }

            public void deleteAccessToken(String username, String accessToken, String consumerKey) {
                Connection conn = null;
                PreparedStatement stat = null;
                try {
                    conn = this.getConnection();
                    conn.setAutoCommit(false);
                    stat = conn.prepareStatement(DELETE_TOKEN);
                    stat.setString(1, username);
                    stat.setString(2, accessToken);
                    stat.setString(3, consumerKey);
                    stat.executeUpdate();
                    conn.commit();
                } catch (Throwable t) {
                    this.executeRollback(conn);
                    _logger.error("Error while deleting an access token",  t);
                    throw new RuntimeException("Error while deleting an access token", t);
                    //processDaoException(t, "Error while deleting an access token", "deleteAccessToken");
                } finally {
                    closeDaoResources(null, stat, conn);
                }
            }

            public Map<String, Integer> getOccurrencesByConsumer() {
                Connection conn = null;
                Map<String, Integer> occurrences = new HashMap<String, Integer>();
                PreparedStatement stat = null;
                ResultSet res = null;
                try {
                    conn = this.getConnection();
                    stat = conn.prepareStatement(SELECT_OCCURRENCES);
                    res = stat.executeQuery();
                    while (res.next()) {
                        String consumerkey = res.getString(1);
                        int count = res.getInt(2);
                        occurrences.put(consumerkey, count);
                    }
                } catch (Throwable t) {
                    _logger.error("Error while loading occurrences",  t);
                    throw new RuntimeException("Error while loading occurrences", t);
                    //processDaoException(t, "Error while loading occurrences", "getOccurrencesByConsumer");
                } finally {
                    closeDaoResources(res, stat, conn);
                }
                return occurrences;
            }


            protected String accessToken;
            protected String tokenType;
            protected Long expiresIn;
            protected String refreshToken;
            protected String scope;


        */
    private String INSERT_TOKEN =
            "INSERT INTO api_oauth_tokens (accesstoken, clientid, tokentype, expiresin, refreshtoken, scope) " +
                    "VALUES (? , ? , ? , ? , ? , ? )";

    private String UPDATE_TOKEN =
            "UPDATE api_oauth_tokens SET expiresin = ? WHERE accesstoken = ?";

    private String DELETE_OLD_TOKENS =
            "DELETE FROM api_oauth_tokens WHERE expiresin < ?";

    private String SELECT_TOKEN =
            "SELECT tokentype, expiresin, refreshtoken, scope " +
                    "FROM api_oauth_tokens WHERE accesstoken = ? AND clientid = ?";

    private String SELECT_OCCURRENCES =
            "SELECT clientid, count(clientid) FROM api_oauth_tokens GROUP BY consumerkey";

    private String DELETE_TOKEN = "DELETE FROM api_oauth_tokens WHERE accesstoken = ? AND clientid = ?";

}
