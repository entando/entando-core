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

    public void addAccessToken(final OAuth2Token accessToken) {

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

    @Override
    public void deleteAccessToken(final String accessToken) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = this.getConnection();
            stat = conn.prepareStatement(DELETE_TOKEN);
            stat.setString(1, accessToken);
            stat.executeUpdate();
            conn.commit();

        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error while remove access token", t);
            throw new RuntimeException("Error while remove access token", t);
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }

    private String INSERT_TOKEN = "INSERT INTO api_oauth_tokens (accesstoken, clientid, expiresin, refreshtoken, granttype)  VALUES (? , ? , ? , ? , ?)";

    private String UPDATE_TOKEN = "UPDATE api_oauth_tokens SET expiresin = ? WHERE accesstoken = ?";

    private String DELETE_OLD_TOKENS = "DELETE FROM api_oauth_tokens WHERE expiresin < ?";

    private String SELECT_TOKEN = "SELECT * FROM api_oauth_tokens WHERE accesstoken = ? ";

    private String SELECT_OCCURRENCES =
            "SELECT clientid, count(clientid) FROM api_oauth_tokens GROUP BY clientid";

    private String DELETE_TOKEN = "DELETE FROM api_oauth_tokens WHERE accesstoken = ? ";


}
