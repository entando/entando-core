/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General  License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General  License for more
 * details.
 */
package org.entando.entando.aps.system.services.oauth2;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.oauth2.model.ConsumerRecordVO;

import java.util.List;
import org.springframework.security.oauth2.provider.ClientDetailsService;

/**
 * @author E.Santoboni
 */
public interface IOAuthConsumerManager extends ClientDetailsService {
    
    public static final String[] GRANT_TYPES = { "authorization_code", 
        "client_credentials", "implicit", "password", "refresh_token"};

    public static final String CONSUMER_KEY_FILTER_KEY = "consumerkey";
    public static final String CONSUMER_SECRET_FILTER_KEY = "consumersecret";
    public static final String NAME_FILTER_NAME = "name";
    public static final String CONSUMER_DESCRIPTION_FILTER_KEY = "description";
    public static final String CONSUMER_CALLBACKURL_FILTER_KEY = "callbackurl";
    public static final String CONSUMER_EXPIRATIONDATE_FILTER_KEY = "expirationdate";
    public static final String CONSUMER_ISSUEDDATE_FILTER_KEY = "issueddate";
    public static final String SCOPE_FILTER_KEY = "scope";
    public static final String AUTHORIZED_GRANT_TYPE_FILTER_KEY = "authorizedgranttypes";

    public ConsumerRecordVO getConsumerRecord(String consumerKey) throws ApsSystemException;

    public void addConsumer(ConsumerRecordVO consumer) throws ApsSystemException;

    public void updateConsumer(ConsumerRecordVO consumer) throws ApsSystemException;

    public void deleteConsumer(String consumerKey) throws ApsSystemException;

    public List<String> getConsumerKeys(FieldSearchFilter[] filters) throws ApsSystemException;

}
