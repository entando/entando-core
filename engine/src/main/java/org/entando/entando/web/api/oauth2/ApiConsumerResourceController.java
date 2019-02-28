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
import com.agiletec.aps.system.services.role.Permission;
import javax.validation.Valid;
import org.entando.entando.aps.system.services.oauth2.ApiConsumerService;
import org.entando.entando.aps.system.services.oauth2.model.ApiConsumer;
import org.entando.entando.web.api.oauth2.validator.ApiConsumerValidator;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consumers")
public class ApiConsumerResourceController {

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

    @RestAccessControl(permission = Permission.SUPERUSER)
    @GetMapping(value = "/{consumerKey}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<ApiConsumer>> getConsumer(@PathVariable("consumerKey") String consumerKey) throws ApsSystemException {
        return ResponseEntity.ok(new SimpleRestResponse<>(service.get(consumerKey)));
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<ApiConsumer>> list(@Valid RestListRequest request) throws ApsSystemException {
        validator.validateRestListRequest(request, ApiConsumer.class);
        PagedMetadata<ApiConsumer> pagedMetadata = service.list(request);
        validator.validateRestListResult(request, pagedMetadata);
        return ResponseEntity.ok(new PagedRestResponse<>(pagedMetadata));
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<ApiConsumer>> create(@RequestBody @Valid ApiConsumer consumer) throws ApsSystemException {
        validator.validateForCreate(consumer);
        return new ResponseEntity<>(new SimpleRestResponse<>(service.create(consumer)), HttpStatus.CREATED);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @PutMapping(value = "/{consumerKey}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<ApiConsumer>> update(@PathVariable("consumerKey") String consumerKey,
            @RequestBody @Valid ApiConsumer consumer) throws ApsSystemException {
        validator.validateForUpdate(consumerKey, consumer);
        return ResponseEntity.ok(new SimpleRestResponse<>(service.update(consumer)));
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @DeleteMapping(value = "/{consumerKey}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<String>> delete(@PathVariable("consumerKey") String consumerKey) throws ApsSystemException {
        service.delete(consumerKey);
        return ResponseEntity.ok(new SimpleRestResponse<>(consumerKey));
    }
}
