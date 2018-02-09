/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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

/**
 * @author E.Santoboni
 */
public interface IOAuthConsumerManager {

    ConsumerRecordVO getConsumerRecord(String consumerKey) throws ApsSystemException;

    void addConsumer(ConsumerRecordVO consumer) throws ApsSystemException;

    void updateConsumer(ConsumerRecordVO consumer) throws ApsSystemException;

    void deleteConsumer(String consumerKey) throws ApsSystemException;

    List<String> getConsumerKeys(FieldSearchFilter[] filters) throws ApsSystemException;


    String CONSUMER_KEY_FILTER_KEY = "consumerkey";
    String CONSUMER_SECRET_FILTER_KEY = "consumersecret";
    String NAME_FILTER_NAME = "name";
    String CONSUMER_DESCRIPTION_FILTER_KEY = "description";
    String CONSUMER_CALLBACKURL_FILTER_KEY = "callbackurl";
    String CONSUMER_EXPIRATIONDATE_FILTER_KEY = "expirationdate";
    String CONSUMER_ISSUEDDATE_FILTER_KEY = "issueddate";
    String SCOPE_FILTER_KEY = "scope";
    String AUTHORIZED_GRANT_TYPE_FILTER_KEY = "authorizedgranttypes";

}
