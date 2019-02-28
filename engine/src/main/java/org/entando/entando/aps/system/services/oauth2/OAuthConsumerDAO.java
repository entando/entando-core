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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.oauth2.model.ConsumerRecordVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author E.Santoboni
 */
public class OAuthConsumerDAO extends AbstractSearcherDAO implements IOAuthConsumerDAO {

    private static final Logger logger = LoggerFactory.getLogger(OAuthConsumerDAO.class);

    private static final String SELECT_CONSUMER
            = "SELECT consumerkey, consumersecret, name, description, callbackurl,scope, authorizedgranttypes, expirationdate, issueddate "
            + "FROM api_oauth_consumers WHERE consumerkey = ? ";

    private static final String ADD_CONSUMER
            = "INSERT INTO api_oauth_consumers (consumerkey, consumersecret,name, description, callbackurl,scope, authorizedgranttypes, expirationdate, issueddate) VALUES (?,?,?,?,?,?,?,?,?) ";

    private static final String UPDATE_CONSUMER_PREFIX = "UPDATE api_oauth_consumers SET ";

    private static final String UPDATE_CONSUMER_SECRET = "consumersecret = ? , ";

    private static final String UPDATE_CONSUMER_SUFFIX = "name = ? , description = ? , callbackurl = ?, scope=?, authorizedgranttypes = ?, expirationdate = ? WHERE consumerkey = ? ";

    private static final String UPDATE_CONSUMER = UPDATE_CONSUMER_PREFIX + UPDATE_CONSUMER_SUFFIX;

    private static final String UPDATE_CONSUMER_WITH_SECRET = UPDATE_CONSUMER_PREFIX + UPDATE_CONSUMER_SECRET + UPDATE_CONSUMER_SUFFIX;

    private static final String DELETE_CONSUMER
            = "DELETE FROM api_oauth_consumers WHERE consumerkey = ? ";

    private static final String DELETE_CONSUMER_TOKENS
            = "DELETE FROM api_oauth_tokens WHERE clientid = ? ";

    @Override
    public List<String> getConsumerKeys(FieldSearchFilter<?>[] filters) {
        return super.searchId(filters);
    }

    @Override
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
                consumer = consumerFromResultSet(res);
            }
        } catch (SQLException | ApsSystemException t) {
            logger.error("Error while loading consumer by clientid {}", clientId, t);
            throw new RuntimeException("Error while loading consumer by key " + clientId, t);
        } finally {
            closeDaoResources(res, stat, conn);
        }
        return consumer;
    }

    @Override
    public List<ConsumerRecordVO> getConsumers(FieldSearchFilter<?>[] filters) {
        List<ConsumerRecordVO> consumers = new ArrayList<>();

        List<String> keys = getConsumerKeys(filters);

        if (!keys.isEmpty()) {

            String query = SELECT_CONSUMER;
            if (keys.size() > 1) {
                for (int i = 1; i < keys.size(); i++) {
                    query += " OR consumerkey = ?";
                }
            }

            try (Connection conn = getConnection();
                    PreparedStatement stat = conn.prepareStatement(query)) {
                int i = 0;
                for (String key : keys) {
                    stat.setString(++i, key);
                }
                try (ResultSet rs = stat.executeQuery()) {
                    while (rs.next()) {
                        consumers.add(consumerFromResultSet(rs));
                    }
                }
            } catch (SQLException | ApsSystemException ex) {
                logger.error("Error while loading consumers", ex);
                throw new RuntimeException("Error while loading consumers", ex);
            }
        }

        // keeps the order of the keys collection
        return keys.stream()
                .map(key -> consumers.stream()
                .filter(c -> key.equals(c.getKey()))
                .findFirst().get())
                .collect(Collectors.toList());
    }

    private ConsumerRecordVO consumerFromResultSet(ResultSet res) throws SQLException {
        ConsumerRecordVO consumer = new ConsumerRecordVO();
        consumer.setKey(res.getString("consumerkey"));
        consumer.setSecret(res.getString("consumersecret"));
        consumer.setCallbackUrl(res.getString("callbackurl"));
        consumer.setName(res.getString("name"));
        consumer.setDescription(res.getString("description"));
        consumer.setAuthorizedGrantTypes(res.getString("authorizedgranttypes"));
        consumer.setScope(res.getString("scope"));
        consumer.setExpirationDate(res.getTimestamp("expirationdate"));
        consumer.setIssuedDate(res.getTimestamp("issueddate"));
        return consumer;
    }

    @Override
    public ConsumerRecordVO addConsumer(ConsumerRecordVO consumer) {
        Connection conn = null;
        PreparedStatement stat = null;
        int index = 1;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            stat = conn.prepareStatement(ADD_CONSUMER);
            stat.setString(index++, consumer.getKey());
            this.fillStatement(consumer, index, true, stat);
            stat.executeUpdate();
            conn.commit();
        } catch (SQLException | ApsSystemException t) {
            this.executeRollback(conn);
            logger.error("Error while adding a consumer", t);
            throw new RuntimeException("Error while adding a consumer", t);
        } finally {
            closeDaoResources(null, stat, conn);
        }
        return consumer;
    }

    @Override
    public ConsumerRecordVO updateConsumer(ConsumerRecordVO consumer) {
        Connection conn = null;
        PreparedStatement stat = null;
        int index = 1;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            String query = (!StringUtils.isBlank(consumer.getSecret())) ? UPDATE_CONSUMER_WITH_SECRET : UPDATE_CONSUMER;
            stat = conn.prepareStatement(query);
            index = this.fillStatement(consumer, index, false, stat);
            stat.setString(index++, consumer.getKey());
            stat.executeUpdate();
            conn.commit();
        } catch (SQLException | ApsSystemException t) {
            this.executeRollback(conn);
            logger.error("Error while updating a consumer", t);
            throw new RuntimeException("Error while updating a consumer", t);
        } finally {
            closeDaoResources(null, stat, conn);
        }
        return consumer;
    }

    private int fillStatement(ConsumerRecordVO consumer, int index, boolean add, PreparedStatement stat) throws SQLException {
        int idx = index;
        if (add || !StringUtils.isBlank(consumer.getSecret())) {
            String encoded = new BCryptPasswordEncoder().encode(consumer.getSecret());
            stat.setString(idx++, "{bcrypt}" + encoded);
        }
        stat.setString(idx++, consumer.getName());
        stat.setString(idx++, consumer.getDescription());
        stat.setString(idx++, consumer.getCallbackUrl());
        stat.setString(idx++, consumer.getScope());
        stat.setString(idx++, consumer.getAuthorizedGrantTypes());
        if (null != consumer.getExpirationDate()) {
            stat.setTimestamp(idx++, new Timestamp(consumer.getExpirationDate().getTime()));
        } else {
            stat.setNull(idx++, Types.TIMESTAMP);
        }
        if (add) {
            long currentTime = System.currentTimeMillis();
            consumer.setIssuedDate(new Date(currentTime));
            stat.setTimestamp(idx++, new Timestamp(currentTime));
        }
        return idx;
    }

    @Override
    public void deleteConsumer(String clientId) {
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            super.executeQueryWithoutResultset(conn, DELETE_CONSUMER_TOKENS, clientId);
            super.executeQueryWithoutResultset(conn, DELETE_CONSUMER, clientId);
            conn.commit();
        } catch (SQLException | ApsSystemException t) {
            this.executeRollback(conn);
            logger.error("Error while deleting consumer '{}' and its tokens", clientId, t);
            throw new RuntimeException("Error while deleting a consumer and its tokens", t);
        } finally {
            this.closeConnection(conn);
        }
    }

    @Override
    protected String getMasterTableIdFieldName() {
        return "consumerkey";
    }

    @Override
    protected String getMasterTableName() {
        return "api_oauth_consumers";
    }

    @Override
    protected String getTableFieldName(String metadataFieldKey) {
        return metadataFieldKey;
    }

}
