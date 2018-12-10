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
package org.entando.entando.aps.system.services.digitalexchange.component;

import java.util.List;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClient;
import org.entando.entando.aps.system.services.digitalexchange.client.PagedDigitalExchangeCall;
import org.entando.entando.aps.system.services.digitalexchange.model.ResilientPagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.digitalexchange.component.DigitalExchangeComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.core.ParameterizedTypeReference;

@Service
public class DigitalExchangeComponentsServiceImpl implements DigitalExchangeComponentsService {

    private final DigitalExchangesClient client;

    @Autowired
    public DigitalExchangeComponentsServiceImpl(DigitalExchangesClient client) {
        this.client = client;
    }

    @Override
    public ResilientPagedMetadata<DigitalExchangeComponent> getComponents(RestListRequest requestList) {

        ParameterizedTypeReference<PagedRestResponse<DigitalExchangeComponent>> type
                = new ParameterizedTypeReference<PagedRestResponse<DigitalExchangeComponent>>() {
        };

        return client.getCombinedResult(new PagedDigitalExchangeCall<DigitalExchangeComponent>(requestList, type, "digitalExchange", "components") {

            @Override
            protected DigitalExchangeComponentListProcessor getRequestListProcessor(RestListRequest request, List<DigitalExchangeComponent> joinedList) {
                return new DigitalExchangeComponentListProcessor(request, joinedList);
            }
        });
    }
}
