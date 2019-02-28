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

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.List;
import java.util.stream.Collectors;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.services.oauth2.model.ApiConsumer;
import org.entando.entando.aps.system.services.oauth2.model.ConsumerRecordVO;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

public class ApiConsumerServiceImpl implements ApiConsumerService {

    public static final String ERRCODE_CONSUMER_ALREADY_EXISTS = "4";

    private final IOAuthConsumerManager consumerManager;

    public ApiConsumerServiceImpl(IOAuthConsumerManager consumerManager) {
        this.consumerManager = consumerManager;
    }

    @Override
    public ApiConsumer create(ApiConsumer consumer) throws ApsSystemException {
        validateForCreate(consumer);
        return new ApiConsumer(consumerManager.addConsumer(consumer.toConsumerRecordVO()));
    }

    @Override
    public ApiConsumer get(String consumerKey) throws ApsSystemException {
        ConsumerRecordVO record = consumerManager.getConsumerRecord(consumerKey);
        if (record == null) {
            throw new ResourceNotFoundException(ApiConsumer.class.getName(), consumerKey);
        }
        return new ApiConsumer(record);
    }

    @Override
    public ApiConsumer update(ApiConsumer consumer) throws ApsSystemException {
        if (consumerManager.getConsumerRecord(consumer.getKey()) == null) {
            throw new ResourceNotFoundException(ApiConsumer.class.getName(), consumer.getKey());
        }
        return new ApiConsumer(consumerManager.updateConsumer(consumer.toConsumerRecordVO()));
    }

    @Override
    public PagedMetadata<ApiConsumer> list(RestListRequest request) throws ApsSystemException {

        FieldSearchFilter<?>[] filters
                = request.buildFieldSearchFilters().stream()
                        .map(this::reMapFilterKeys)
                        .toArray(FieldSearchFilter[]::new);

        List<ConsumerRecordVO> allConsumers = consumerManager.getConsumers(filters);

        List<ApiConsumer> pagedList = request.getSublist(allConsumers)
                .stream().map(vo -> new ApiConsumer(vo))
                .collect(Collectors.toList());

        return new PagedMetadata<>(request, pagedList, allConsumers.size());
    }

    private FieldSearchFilter<?> reMapFilterKeys(FieldSearchFilter<?> filter) {
        if ("key".equals(filter.getKey())) {
            filter.setKey("consumerkey");
        }
        return filter;
    }

    @Override
    public void delete(String consumerKey) throws ApsSystemException {
        consumerManager.deleteConsumer(consumerKey);
    }

    private void validateForCreate(ApiConsumer consumer) throws ApsSystemException {

        if (consumerManager.getConsumerRecord(consumer.getKey()) != null) {
            DataBinder binder = new DataBinder(consumer);
            BindingResult bindingResult = binder.getBindingResult();
            bindingResult.reject(ERRCODE_CONSUMER_ALREADY_EXISTS, new String[]{consumer.getKey()}, "api.consumer.exists");
            throw new ValidationConflictException(bindingResult);
        }
    }
}
