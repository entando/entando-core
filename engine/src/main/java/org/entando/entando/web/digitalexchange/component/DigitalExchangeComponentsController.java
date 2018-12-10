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
package org.entando.entando.web.digitalexchange.component;

import com.agiletec.aps.system.services.role.Permission;
import org.entando.entando.aps.system.services.digitalexchange.component.DigitalExchangeComponentsService;
import org.entando.entando.aps.system.services.digitalexchange.model.ResilientPagedMetadata;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DigitalExchangeComponentsController implements DigitalExchangeComponentResource {

    private final DigitalExchangeComponentsService componentsService;

    @Autowired
    public DigitalExchangeComponentsController(DigitalExchangeComponentsService componentsService) {
        this.componentsService = componentsService;
    }

    @Override
    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<PagedRestResponse<DigitalExchangeComponent>> getComponents(RestListRequest requestList) {

        ResilientPagedMetadata<DigitalExchangeComponent> resilientPagedMetadata = componentsService.getComponents(requestList);

        PagedRestResponse<DigitalExchangeComponent> response = new PagedRestResponse<>(resilientPagedMetadata);
        response.setErrors(resilientPagedMetadata.getErrors());

        return ResponseEntity.ok(response);
    }
}
