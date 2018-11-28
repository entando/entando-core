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

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import org.entando.entando.aps.system.services.oauth2.model.ConsumerRecordVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

public class OAuthConsumerManager extends AbstractOAuthManager implements IOAuthConsumerManager {

    private static final Logger logger = LoggerFactory.getLogger(OAuthConsumerManager.class);

    private IOAuthConsumerDAO consumerDAO;

    @Override
    public void init() throws Exception {
        logger.debug("{} ready", this.getClass().getName());
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        BaseClientDetails details = new BaseClientDetails();
        try {
            ConsumerRecordVO consumer = this.getConsumerDAO().getConsumer(clientId);
            if (null == consumer) {
                throw new ClientRegistrationException("Client with id '" + clientId + "' does not exists");
            }
            if (null != consumer.getExpirationDate() && consumer.getExpirationDate().before(new Date())) {
                throw new ClientRegistrationException("Client '" + clientId + "' is expired");
            }
            details.setClientId(clientId);
            if (!StringUtils.isBlank(consumer.getAuthorizedGrantTypes())) {
                details.setAuthorizedGrantTypes(Arrays.asList(consumer.getAuthorizedGrantTypes().split(",")));
            }
            if (!StringUtils.isBlank(consumer.getScope())) {
                details.setScope(Arrays.asList(consumer.getScope().split(",")));
            }
            details.setClientSecret(consumer.getSecret());
            Set<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_CLIENT"));
            details.setAuthorities(authorities);
            if (null != consumer.getCallbackUrl()) {
                Set<String> uris = new HashSet<>();
                uris.add(consumer.getCallbackUrl());
                details.setRegisteredRedirectUri(uris);
            }
            details.setAccessTokenValiditySeconds(this.getAccessTokenValiditySeconds());
            details.setRefreshTokenValiditySeconds(this.getRefreshTokenValiditySeconds());
        } catch (ClientRegistrationException t) {
            throw t;
        } catch (Exception t) {
            logger.error("Error extracting consumer record by key {}", clientId, t);
            throw new ClientRegistrationException("Error extracting consumer record by key " + clientId, t);
        }
        return details;
    }

    @Override
    public ConsumerRecordVO getConsumerRecord(String consumerKey) throws ApsSystemException {
        ConsumerRecordVO consumer = null;
        try {
            consumer = this.getConsumerDAO().getConsumer(consumerKey);
        } catch (Exception t) {
            logger.error("Error extracting consumer record by key {}", consumerKey, t);
            throw new ApsSystemException("Error extracting consumer record by key " + consumerKey, t);
        }
        return consumer;
    }

    @Override
    public void addConsumer(ConsumerRecordVO consumer) throws ApsSystemException {
        try {
            this.getConsumerDAO().addConsumer(consumer);
        } catch (Throwable t) {
            logger.error("Error adding consumer", t);
            throw new ApsSystemException("Error adding consumer", t);
        }
    }

    @Override
    public void updateConsumer(ConsumerRecordVO consumer) throws ApsSystemException {
        try {
            this.getConsumerDAO().updateConsumer(consumer);
        } catch (Throwable t) {
            logger.error("Error updating consumer", t);
            throw new ApsSystemException("Error updating consumer", t);
        }
    }

    @Override
    public void deleteConsumer(String clientId) throws ApsSystemException {
        try {
            this.getConsumerDAO().deleteConsumer(clientId);
        } catch (Throwable t) {
            logger.error("Error deleting consumer record by key {}", clientId, t);
            throw new ApsSystemException("Error deleting consumer record by key " + clientId, t);
        }
    }

    @Override
    public List<String> getConsumerKeys(FieldSearchFilter[] filters) throws ApsSystemException {
        List<String> consumerKeys = null;
        try {
            consumerKeys = this.getConsumerDAO().getConsumerKeys(filters);
        } catch (Throwable t) {
            logger.error("Error extracting consumer keys", t);
            throw new ApsSystemException("Error extracting consumer keys", t);
        }
        return consumerKeys;
    }

    protected IOAuthConsumerDAO getConsumerDAO() {
        return consumerDAO;
    }

    public void setConsumerDAO(IOAuthConsumerDAO consumerDAO) {
        this.consumerDAO = consumerDAO;
    }

}
