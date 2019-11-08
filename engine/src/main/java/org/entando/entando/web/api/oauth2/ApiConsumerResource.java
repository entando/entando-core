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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import org.entando.entando.aps.system.services.oauth2.model.ApiConsumer;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = {"consumers"})
@RequestMapping("/consumers")
public interface ApiConsumerResource {

    @ApiOperation(value = "Retrieve a consumer by key")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Ok"),
        @ApiResponse(code = 404, message = "Not Found")
    })
    @RestAccessControl(permission = Permission.SUPERUSER)
    @GetMapping(value = "/{consumerKey}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SimpleRestResponse<ApiConsumer>> get(@PathVariable("consumerKey") String consumerKey) throws ApsSystemException;


    @ApiOperation(value = "List consumers")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Ok")
    })
    @RestAccessControl(permission = Permission.SUPERUSER)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PagedRestResponse<ApiConsumer>> list(@Valid RestListRequest request) throws ApsSystemException;


    @ApiOperation(value = "Create a consumer")
    @ApiResponses({
        @ApiResponse(code = 201, message = "Created")
    })
    @RestAccessControl(permission = Permission.SUPERUSER)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SimpleRestResponse<ApiConsumer>> create(@RequestBody @Valid ApiConsumer consumer) throws ApsSystemException;


    @ApiOperation(value = "Update a consumer")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Ok"),
        @ApiResponse(code = 404, message = "Not Found")
    })
    @RestAccessControl(permission = Permission.SUPERUSER)
    @PutMapping(value = "/{consumerKey}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SimpleRestResponse<ApiConsumer>> update(@PathVariable("consumerKey") String consumerKey,
            @RequestBody @Valid ApiConsumer consumer) throws ApsSystemException;


    @ApiOperation(value = "Delete a consumer")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Ok")
    })
    @RestAccessControl(permission = Permission.SUPERUSER)
    @DeleteMapping(value = "/{consumerKey}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SimpleRestResponse<String>> delete(@PathVariable("consumerKey") String consumerKey) throws ApsSystemException;
}
