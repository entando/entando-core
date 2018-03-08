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
package org.entando.entando.web.entity;

import com.agiletec.aps.system.services.role.Permission;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.aps.system.services.entity.IEntityManagerService;
import org.entando.entando.aps.system.services.entity.model.EntityManagerDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/entityManagers")
public class EntityManagerController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IEntityManagerService entityManagerService;

    protected IEntityManagerService getEntityManagerService() {
        return entityManagerService;
    }

    public void setEntityManagerService(IEntityManagerService entityManagerService) {
        this.entityManagerService = entityManagerService;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getEntityManagers(RestListRequest requestList) throws JsonProcessingException {
        PagedMetadata<String> result = this.getEntityManagerService().getEntityManagers(requestList);
        logger.debug("Main Response -> " + new ObjectMapper().writeValueAsString(result));
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{entityManagerCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getEntityManager(@PathVariable String entityManagerCode) throws JsonProcessingException {
        logger.debug("Requested manager -> " + entityManagerCode);
        EntityManagerDto dto = this.getEntityManagerService().getEntityManager(entityManagerCode);
        logger.debug("Main Response -> " + new ObjectMapper().writeValueAsString(dto));
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

}
