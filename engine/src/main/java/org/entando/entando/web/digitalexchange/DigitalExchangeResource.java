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
package org.entando.entando.web.digitalexchange;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = {"digital-exchange"})
@RequestMapping(value = "/digitalExchange/exchanges")
public interface DigitalExchangeResource {

    @ApiOperation(value = "Create a new Digital Exchange configuration")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK")
    })
    ResponseEntity<SimpleRestResponse<DigitalExchange>> create(@Valid @RequestBody DigitalExchange digitalExchange, BindingResult bindingResult);

    @ApiOperation(value = "Returns a Digital Exchange configuration")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "Not Found")
    })
    ResponseEntity<SimpleRestResponse<DigitalExchange>> get(@PathVariable("name") String name);

    @ApiOperation(value = "Returns the list of all Digital Exchange configurations")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK")
    })
    ResponseEntity<SimpleRestResponse<List<String>>> list();

    @ApiOperation(value = "Update a Digital Exchange configuration")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "Not Found")
    })
    ResponseEntity<SimpleRestResponse<DigitalExchange>> update(@PathVariable("name") String name, @Valid @RequestBody DigitalExchange digitalExchange, BindingResult bindingResult);

    @ApiOperation(value = "Delete a Digital Exchange configuration")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "Not Found")
    })
    ResponseEntity<SimpleRestResponse<String>> delete(@PathVariable("name") String name);
}
