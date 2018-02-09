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

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.oauth2.model.ConsumerRecordVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class OAuthConsumerManager extends AbstractService implements IOAuthConsumerManager {

    private static final Logger _logger = LoggerFactory.getLogger(OAuthConsumerManager.class);

    public void init() throws Exception {
        _logger.debug("{} ready", this.getClass().getName());
    }

    protected void release() {
        super.release();
    }


    public ConsumerRecordVO getConsumerRecord(String consumerKey) throws ApsSystemException {
        ConsumerRecordVO consumer = null;
        try {
            consumer = this.getConsumerDAO().getConsumer(consumerKey);
        } catch (Throwable t) {
            _logger.error("Error extracting consumer record by key {}", consumerKey, t);
            //ApsSystemUtils.logThrowable(t, this, "getConsumerRecord", "Error extracting consumer record by key " + consumerKey);
            throw new ApsSystemException("Error extracting consumer record by key " + consumerKey, t);
        }
        return consumer;
    }

    public void addConsumer(ConsumerRecordVO consumer) throws ApsSystemException {
        try {
            this.getConsumerDAO().addConsumer(consumer);
        } catch (Throwable t) {
            _logger.error("Error adding consumer", t);
            //ApsSystemUtils.logThrowable(t, this, "addConsumer", "Error adding consumer");
            throw new ApsSystemException("Error adding consumer", t);
        }
    }

    public void updateConsumer(ConsumerRecordVO consumer) throws ApsSystemException {
        try {
            this.getConsumerDAO().updateConsumer(consumer);
        } catch (Throwable t) {
            _logger.error("Error updating consumer", t);
            //ApsSystemUtils.logThrowable(t, this, "updateConsumer", "Error updating consumer");
            throw new ApsSystemException("Error updating consumer", t);
        }
    }

    public void deleteConsumer(String clientId) throws ApsSystemException {
        try {
            this.getConsumerDAO().deleteConsumer(clientId);
        } catch (Throwable t) {
            _logger.error("Error deleting consumer record by key {}", clientId, t);
            //ApsSystemUtils.logThrowable(t, this, "getConsumerRecord", "Error deleting consumer record by key " + consumerKey);
            throw new ApsSystemException("Error deleting consumer record by key " + clientId, t);
        }
    }

    public List<String> getConsumerKeys(FieldSearchFilter[] filters) throws ApsSystemException {
        List<String> consumerKeys = null;
        try {
            consumerKeys = this.getConsumerDAO().getConsumerKeys(filters);
        } catch (Throwable t) {
            _logger.error("Error extracting consumer keys", t);
            //ApsSystemUtils.logThrowable(t, this, "getConsumerKeys", "Error extracting consumer keys");
            throw new ApsSystemException("Error extracting consumer keys", t);
        }
        return consumerKeys;
    }

    protected IOAuthConsumerDAO getConsumerDAO() {
        return _consumerDAO;
    }

    public void setConsumerDAO(IOAuthConsumerDAO consumerDAO) {
        this._consumerDAO = consumerDAO;
    }


    private IOAuthConsumerDAO _consumerDAO;


}
