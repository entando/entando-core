/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.aps.system.services.oauth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import net.oauth.OAuthConsumer;

import org.entando.entando.aps.system.services.oauth.model.ConsumerRecordVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;

/**
 * @author E.Santoboni
 */
public class OAuthConsumerDAO extends AbstractSearcherDAO implements IOAuthConsumerDAO {
    
	private static final Logger _logger =  LoggerFactory.getLogger(OAuthConsumerDAO.class);
	
    public List<String> getConsumerKeys(FieldSearchFilter[] filters) {
        return super.searchId(filters);
    }
    
    public ConsumerRecordVO getConsumerRecord(String consumerKey) {
        return (ConsumerRecordVO) this.getConsumer(consumerKey, true);
    }
    
    public OAuthConsumer getConsumer(String consumerKey) {
        return (OAuthConsumer) this.getConsumer(consumerKey, false);
    }
    
    private Object getConsumer(String consumerKey, boolean needRecord) {
        Connection conn = null;
        Object consumer = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            conn = this.getConnection();
            String query = (!needRecord) ? SELECT_CONSUMER + SELECT_CONSUMER_EXPIRATION_DATE_FILTER : SELECT_CONSUMER;
            stat = conn.prepareStatement(query);
            stat.setString(1, consumerKey);
            if (!needRecord) {
                stat.setDate(2, new java.sql.Date(new Date().getTime()));
            }
            res = stat.executeQuery();
            if (res.next()) {
                //consumersecret, description, callbackurl, expirationdate
                String consumerSecret = res.getString(1);
                String description = res.getString(2);
                String callbackurl = res.getString(3);
                Date expirationdate = res.getDate(4);
                if (needRecord) {
                    ConsumerRecordVO consumerRecord = new ConsumerRecordVO();
                    consumerRecord.setCallbackUrl(callbackurl);
                    consumerRecord.setDescription(description);
                    consumerRecord.setExpirationDate(expirationdate);
                    consumerRecord.setKey(consumerKey);
                    consumerRecord.setSecret(consumerSecret);
                    consumer = consumerRecord;
                } else {
                    //if (null != expirationdate && new Date().after(expirationdate)) {
                        //trace exception
                    //}
                    OAuthConsumer oauthConsumer = new OAuthConsumer(callbackurl, consumerKey, consumerSecret, null);
                    oauthConsumer.setProperty("name", consumerKey);
                    oauthConsumer.setProperty("description", description);
                    consumer = oauthConsumer;
                }
            }
        } catch (Throwable t) {
           _logger.error("Error while loading consumer by key {}", consumerKey,  t);
			throw new RuntimeException("Error while loading consumer by key " + consumerKey, t);
			// processDaoException(t, "Error while loading consumer by key " + consumerKey, "getConsumer");
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
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error while adding a consumer",  t);
			throw new RuntimeException("Error while adding a consumer", t);
			//processDaoException(t, "Error while adding a consumer", "addConsumer");
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
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error while updating a consumer",  t);
			throw new RuntimeException("Error while updating a consumer", t);
			//processDaoException(t, "Error while updating a consumer", "updateConsumer");
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }
    
    private int fillStatement(ConsumerRecordVO consumer, int index, PreparedStatement stat) throws SQLException {
        stat.setString(index++, consumer.getSecret());
        stat.setString(index++, consumer.getDescription());
        stat.setString(index++, consumer.getCallbackUrl());
        if (null != consumer.getExpirationDate()) {
            stat.setDate(index++, new java.sql.Date(consumer.getExpirationDate().getTime()));
        } else {
            stat.setNull(index++, Types.DATE);
        }
        return index;
    }
    
    public void deleteConsumer(String consumerKey) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.delete(consumerKey, DELETE_CONSUMER_TOKENS, conn);
            this.delete(consumerKey, DELETE_CONSUMER, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error while deleting consumer '{}' and its tokens", consumerKey,  t);
			throw new RuntimeException("Error while deleting a consumer and its tokens", t);
			//processDaoException(t, "Error while deleting a consumer and its tokens", "deleteConsumer");
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }
    
    public void delete(String key, String query, Connection conn) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(query);
            stat.setString(1, key);
            stat.executeUpdate();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error while deleting records for {}", key,  t);
			throw new RuntimeException("Error while deleting records", t);
			//processDaoException(t, "Error while deleting records", "delete");
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
    
    protected boolean isForceCaseInsensitiveLikeSearch() {
        return true;
    }
    
    private String SELECT_CONSUMER =
            "SELECT consumersecret, description, callbackurl, expirationdate "
            + "FROM api_oauth_consumers WHERE consumerkey = ? ";
    
    private String ADD_CONSUMER = 
            "INSERT INTO api_oauth_consumers (consumerkey, "
            + "consumersecret, description, callbackurl, expirationdate) VALUES (?, ?, ?, ?, ?) ";
    
    private String UPDATE_CONSUMER = 
            "UPDATE api_oauth_consumers SET consumersecret = ? , "
            + "description = ? , callbackurl = ? , expirationdate = ? WHERE consumerkey = ? ";
    
    private String DELETE_CONSUMER = 
            "DELETE FROM api_oauth_consumers WHERE consumerkey = ? ";
    
    private String DELETE_CONSUMER_TOKENS = 
            "DELETE FROM api_oauth_tokens WHERE consumerkey = ? ";
    
    private String SELECT_CONSUMER_EXPIRATION_DATE_FILTER =
            " AND (expirationdate IS NULL OR expirationdate >= ? )";
    
}
