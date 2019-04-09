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
package org.entando.entando.web.api.oauth2;

import com.agiletec.aps.system.exception.ApsSystemException;
import javax.validation.Valid;
import org.entando.entando.aps.system.services.oauth2.ApiConsumerService;
import org.entando.entando.aps.system.services.oauth2.model.ApiConsumer;
import org.entando.entando.web.api.oauth2.validator.ApiConsumerValidator;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiConsumerResourceController implements ApiConsumerResource {

    private final ApiConsumerValidator validator;
    private final ApiConsumerService service;

    @Autowired
    public ApiConsumerResourceController(ApiConsumerValidator validator, ApiConsumerService apiConsumerService) {
        this.validator = validator;
        this.service = apiConsumerService;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @Override
    public ResponseEntity<SimpleRestResponse<ApiConsumer>> get(@PathVariable("consumerKey") String consumerKey) throws ApsSystemException {
        return ResponseEntity.ok(new SimpleRestResponse<>(service.get(consumerKey)));
    }

    @Override
    public ResponseEntity<PagedRestResponse<ApiConsumer>> list(@Valid RestListRequest request) throws ApsSystemException {
        validator.validateRestListRequest(request, ApiConsumer.class);
        PagedMetadata<ApiConsumer> pagedMetadata = service.list(request);
        validator.validateRestListResult(request, pagedMetadata);
        return ResponseEntity.ok(new PagedRestResponse<>(pagedMetadata));
    }

    @Override
    public ResponseEntity<SimpleRestResponse<ApiConsumer>> create(@RequestBody @Valid ApiConsumer consumer) throws ApsSystemException {
        validator.validateForCreate(consumer);
        return new ResponseEntity<>(new SimpleRestResponse<>(service.create(consumer)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<SimpleRestResponse<ApiConsumer>> update(@PathVariable("consumerKey") String consumerKey,
            @RequestBody @Valid ApiConsumer consumer) throws ApsSystemException {
        validator.validateForUpdate(consumerKey, consumer);
        return ResponseEntity.ok(new SimpleRestResponse<>(service.update(consumer)));
    }

    @Override
    public ResponseEntity<SimpleRestResponse<String>> delete(@PathVariable("consumerKey") String consumerKey) throws ApsSystemException {
        service.delete(consumerKey);
        return ResponseEntity.ok(new SimpleRestResponse<>(consumerKey));
    }
}
