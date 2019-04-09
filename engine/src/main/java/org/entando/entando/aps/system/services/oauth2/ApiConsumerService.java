/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
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

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.oauth2.model.ApiConsumer;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;

public interface ApiConsumerService {

    ApiConsumer create(ApiConsumer consumer) throws ApsSystemException;

    ApiConsumer get(String consumerKey) throws ApsSystemException;

    PagedMetadata<ApiConsumer> list(RestListRequest request) throws ApsSystemException;

    ApiConsumer update(ApiConsumer consumer) throws ApsSystemException;

    void delete(String consumerKey) throws ApsSystemException;
}
