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

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.oauth2.model.ConsumerRecordVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * @author E.Santoboni
 */
public class OAuthConsumerDAO extends AbstractSearcherDAO implements IOAuthConsumerDAO {

    private static final Logger _logger = LoggerFactory.getLogger(OAuthConsumerDAO.class);

    public List<String> getConsumerKeys(FieldSearchFilter[] filters) {
        return super.searchId(filters);
    }

    public ConsumerRecordVO getConsumer(String clientId) {
        Connection conn = null;
        ConsumerRecordVO consumer = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            conn = this.getConnection();
            String query = SELECT_CONSUMER;
            stat = conn.prepareStatement(query);
            stat.setString(1, clientId);
            res = stat.executeQuery();
            if (res.next()) {
                consumer = new ConsumerRecordVO();
                consumer.setKey(res.getString("consumerkey"));
                consumer.setSecret(res.getString("consumersecret"));
                consumer.setCallbackUrl(res.getString("callbackurl"));
                consumer.setName(res.getString("name"));
                consumer.setDescription(res.getString("description"));
                consumer.setAuthorizedGrantTypes(res.getString("authorizedgranttypes"));
                consumer.setScope(res.getString("scope"));
                consumer.setExpirationDate(res.getDate("expirationdate"));
                consumer.setIssuedDate(res.getDate("issueddate"));

            }
        } catch (SQLException | ApsSystemException t) {
            _logger.error("Error while loading consumer by clientid {}", clientId, t);
            throw new RuntimeException("Error while loading consumer by key " + clientId, t);
        } finally {
            closeDaoResources(res, stat, conn);
        }
        return consumer;
    }


    public void addConsumer(ConsumerRecordVO consumer) {
        Connection conn = null;
        PreparedStatement stat = null;
        int index = 1;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            stat = conn.prepareStatement(ADD_CONSUMER);
            stat.setString(index++, consumer.getKey());
            index = this.fillStatement(consumer, index, stat);
            stat.executeUpdate();
            conn.commit();
        } catch (SQLException | ApsSystemException t) {
            this.executeRollback(conn);
            _logger.error("Error while adding a consumer", t);
            throw new RuntimeException("Error while adding a consumer", t);
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }

    public void updateConsumer(ConsumerRecordVO consumer) {
        Connection conn = null;
        PreparedStatement stat = null;
        int index = 1;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            stat = conn.prepareStatement(UPDATE_CONSUMER);
            index = this.fillStatement(consumer, index, stat);
            stat.setString(index++, consumer.getKey());
            stat.executeUpdate();
            conn.commit();
        } catch (SQLException | ApsSystemException t) {
            this.executeRollback(conn);
            _logger.error("Error while updating a consumer", t);
            throw new RuntimeException("Error while updating a consumer", t);
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }

    private int fillStatement(ConsumerRecordVO consumer, int index, PreparedStatement stat) throws SQLException {
        int idx = index;
        stat.setString(idx++, consumer.getSecret());
        stat.setString(idx++, consumer.getName());
        stat.setString(idx++, consumer.getDescription());
        stat.setString(idx++, consumer.getCallbackUrl());
        stat.setString(idx++, consumer.getScope());
        stat.setString(idx++, consumer.getAuthorizedGrantTypes());
        if (null != consumer.getExpirationDate()) {
            stat.setTimestamp(idx++, new Timestamp(consumer.getExpirationDate().getTime()));
        } else {
            // no operation
        }
        stat.setTimestamp(idx++, new Timestamp(System.currentTimeMillis()));
        return idx;
    }

    public void deleteConsumer(String clientId) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.delete(clientId, DELETE_CONSUMER_TOKENS, conn);
            this.delete(clientId, DELETE_CONSUMER, conn);
            conn.commit();
        } catch (SQLException | ApsSystemException t) {
            this.executeRollback(conn);
            _logger.error("Error while deleting consumer '{}' and its tokens", clientId, t);
            throw new RuntimeException("Error while deleting a consumer and its tokens", t);
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }

    public void delete(String clientId, String query, Connection conn) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(query);
            stat.setString(1, clientId);
            stat.executeUpdate();
        } catch (SQLException t) {
            this.executeRollback(conn);
            _logger.error("Error while deleting records for {}", clientId, t);
            throw new RuntimeException("Error while deleting records", t);
        } finally {
            closeDaoResources(null, stat);
        }
    }

    protected String getMasterTableIdFieldName() {
        return "consumerkey";
    }

    protected String getMasterTableName() {
        return "api_oauth_consumers";
    }

    protected String getTableFieldName(String metadataFieldKey) {
        return metadataFieldKey;
    }

    @Override
    protected boolean isForceCaseInsensitiveLikeSearch() {
        return true;
    }

    private String SELECT_CONSUMER
            = "SELECT consumerkey, consumersecret, name, description, callbackurl,scope, authorizedgranttypes, expirationdate, issueddate "
            + "FROM api_oauth_consumers WHERE consumerkey = ? ";

    private String ADD_CONSUMER
            = "INSERT INTO api_oauth_consumers (consumerkey, consumersecret,name, description, callbackurl,scope, authorizedgranttypes, expirationdate, issueddate) VALUES (?,?,?,?,?,?,?,?,?) ";

    private String UPDATE_CONSUMER
            = "UPDATE api_oauth_consumers SET consumersecret = ? , name=?, description = ? , callbackurl = ?, scope=?, authorizedgranttypes = ?, expirationdate = ? , issueddate = ? WHERE consumerkey = ? ";

    private String DELETE_CONSUMER
            = "DELETE FROM api_oauth_consumers WHERE consumerkey = ? ";

    private String DELETE_CONSUMER_TOKENS
            = "DELETE FROM api_oauth_tokens WHERE clientid = ? ";

}
