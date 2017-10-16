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

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;
import org.apache.oltu.oauth2.common.domain.client.BasicClientInfo;
import org.apache.oltu.oauth2.common.domain.client.BasicClientInfoBuilder;
import org.apache.oltu.oauth2.common.domain.client.ClientInfo;
import org.entando.entando.aps.system.services.oauth.model.ConsumerRecordVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

/**
 * @author E.Santoboni
 */
public class OAuthConsumerDAO extends AbstractSearcherDAO implements IOAuthConsumerDAO {

    private static final Logger _logger = LoggerFactory.getLogger(OAuthConsumerDAO.class);

    public List<String> getConsumerKeys(FieldSearchFilter[] filters) {
        return super.searchId(filters);
    }

    public ConsumerRecordVO getConsumerRecord(String consumerKey) {
        return (ConsumerRecordVO) this.getConsumer(consumerKey, true);
    }


    public ClientInfo getConsumer(String clientId) {
        return (ClientInfo) this.getConsumer(clientId, false);
    }

    private Object getConsumer(String clientId, boolean needRecord) {
        Connection conn = null;
        Object consumer = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            conn = this.getConnection();
            String query = (!needRecord) ? SELECT_CONSUMER + SELECT_CONSUMER_EXPIRATION_DATE_FILTER : SELECT_CONSUMER;
            stat = conn.prepareStatement(query);
            stat.setString(1, clientId);
            if (!needRecord) {
                stat.setDate(2, new java.sql.Date(new Date().getTime()));
            }
            res = stat.executeQuery();
            if (res.next()) {
                String consumerKey = res.getString(1);
                String consumerSecret = res.getString(2);
                String description = res.getString(3);
                String callbackurl = res.getString(4);
                String authorizationCode = res.getString(5);
                Date expirationdate = res.getDate(6);
                Date issueddate = res.getDate(7);
                if (needRecord) {
                    ConsumerRecordVO consumerRecord = new ConsumerRecordVO();
                    consumerRecord.setClientId(clientId);
                    consumerRecord.setCallbackUrl(callbackurl);
                    consumerRecord.setAuthorizationCode(authorizationCode);
                    consumerRecord.setDescription(description);
                    consumerRecord.setExpirationDate(expirationdate);
                    consumerRecord.setKey(consumerKey);
                    consumerRecord.setSecret(consumerSecret);
                    consumerRecord.setIssuedDate(issueddate);
                    consumer = consumerRecord;
                } else {

                    ClientInfo clientInfo = BasicClientInfoBuilder
                            .clientInfo()
                            .setClientId(clientId)
                            .setClientSecret(consumerSecret)
                            .setClientUrl(callbackurl)
                            .setDescription(description)
                            .setExpiresIn(expirationdate.getTime())
                            .setIssuedAt(issueddate.getTime())
                            .build();

                    consumer = clientInfo;

                }
            }
        } catch (Throwable t) {
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
            stat.setString(index++, consumer.getClientId());
            index = this.fillStatement(consumer, index, stat);
            stat.executeUpdate();
            conn.commit();
        } catch (Throwable t) {
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
            stat.setString(index++, consumer.getClientId());
            stat.executeUpdate();
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error while updating a consumer", t);
            throw new RuntimeException("Error while updating a consumer", t);
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }

    private int fillStatement(ConsumerRecordVO consumer, int index, PreparedStatement stat) throws SQLException {
        stat.setString(index++, consumer.getKey());
        stat.setString(index++, consumer.getSecret());
        stat.setString(index++, consumer.getDescription());
        stat.setString(index++, consumer.getCallbackUrl());
        if (null != consumer.getExpirationDate()) {
            stat.setDate(index++, new java.sql.Date(consumer.getExpirationDate().getTime()));
        } else {
            stat.setNull(index++, Types.DATE);
        }
        if (null != consumer.getIssuedDate()) {
            stat.setDate(index++, new java.sql.Date(consumer.getIssuedDate().getTime()));
        } else {
            stat.setNull(index++, Types.DATE);
        }
        return index;
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
        } catch (Throwable t) {
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
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error while deleting records for {}", clientId, t);
            throw new RuntimeException("Error while deleting records", t);
        } finally {
            closeDaoResources(null, stat);
        }
    }

    protected String getMasterTableIdFieldName() {
        return "clientId";
    }

    protected String getMasterTableName() {
        return "api_oauth_consumers";
    }

    protected String getTableFieldName(String metadataFieldKey) {
        return metadataFieldKey;
    }

    protected boolean isForceCaseInsensitiveLikeSearch() {
        return true;
    }

    private String SELECT_CONSUMER
            = "SELECT consumerkey, consumersecret, description, callbackurl, authorizationcode, expirationdate, issueddate "
            + "FROM api_oauth_consumers WHERE clientid = ? ";

    private String ADD_CONSUMER
            = "INSERT INTO api_oauth_consumers (clientid, consumerkey, "
            + "consumersecret, description, callbackurl, authorizationcode, expirationdate, issueddate) VALUES (?,?,?,?,?,?,?,?) ";

    private String UPDATE_CONSUMER
            = "UPDATE api_oauth_consumers SET clientid = ?, consumersecret = ? , "
            + "description = ? , callbackurl = ? ,authorizationcode = ?, expirationdate = ?, issueddate = ? WHERE clientid = ? ";

    private String DELETE_CONSUMER
            = "DELETE FROM api_oauth_consumers WHERE clientid = ? ";

    private String DELETE_CONSUMER_TOKENS
            = "DELETE FROM api_oauth_tokens WHERE clientid = ? ";

    private String SELECT_CONSUMER_EXPIRATION_DATE_FILTER
            = " AND (expirationdate IS NULL OR expirationdate >= ? )";

}
