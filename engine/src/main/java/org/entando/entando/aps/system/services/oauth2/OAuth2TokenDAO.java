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

import com.agiletec.aps.system.common.AbstractDAO;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class OAuth2TokenDAO extends AbstractDAO implements IOAuth2TokenDAO {

    private static final Logger _logger = LoggerFactory.getLogger(OAuth2TokenDAO.class);

    public void addAccessToken(OAuth2Token accessToken) {

        //accesstoken, clientid, expiresin, refreshtoken, granttype

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
            stat.executeUpdate();
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error while adding an access token", t);
            throw new RuntimeException("Error while adding an access token", t);
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }

    @Override
    public OAuth2Token getAccessToken(final String accessToken) {
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
                token.setExpiresIn(res.getDate("expiresin"));

            }
        } catch (Throwable t) {
            _logger.error("Error while loading token {}", accessToken, t);
            throw new RuntimeException("Error while loading token " + accessToken, t);
        } finally {
            closeDaoResources(res, stat, conn);
        }
        return token;
    }

    /*
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


                 private String _accessToken;
        private String _clientId;
        private Date _expiresIn;
        private String _refreshToken;
        private String _grantType;


            */
    private String INSERT_TOKEN = "INSERT INTO api_oauth_tokens (accesstoken, clientid, expiresin, refreshtoken, granttype)  VALUES (? , ? , ? , ? , ?)";

    private String UPDATE_TOKEN = "UPDATE api_oauth_tokens SET expiresin = ? WHERE accesstoken = ?";

    private String DELETE_OLD_TOKENS = "DELETE FROM api_oauth_tokens WHERE expiresin < ?";

    private String SELECT_TOKEN = "SELECT * FROM api_oauth_tokens WHERE accesstoken = ? ";

    private String SELECT_OCCURRENCES =
            "SELECT clientid, count(clientid) FROM api_oauth_tokens GROUP BY clientid";

    private String DELETE_TOKEN = "DELETE FROM api_oauth_tokens WHERE accesstoken = ? ";


}
