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
package org.entando.entando.web.database;

import org.entando.entando.web.database.validator.DatabaseValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author E.Santoboni
 */
@RestController
@RequestMapping(value = "/database")
public class DatabaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private DatabaseValidator databaseValidator;

    /*
    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDataTypes(@Valid RestListRequest requestList) throws JsonProcessingException {
        this.getDatabaseValidator().validateRestListRequest(requestList);
        PagedMetadata<EntityTypeShortDto> result = this.getDataObjectService().getShortDataTypes(requestList);
        this.getDatabaseValidator().validateRestListResult(requestList, result);
        logger.debug("Main Response -> " + result);
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
    }
     */
    public DatabaseValidator getDatabaseValidator() {
        return databaseValidator;
    }

    public void setDatabaseValidator(DatabaseValidator databaseValidator) {
        this.databaseValidator = databaseValidator;
    }

}
