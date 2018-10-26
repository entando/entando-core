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

import com.agiletec.aps.system.services.role.Permission;
import org.entando.entando.aps.system.services.database.IDatabaseService;
import org.entando.entando.aps.system.services.database.model.ComponentDto;
import org.entando.entando.aps.system.services.database.model.DumpReportDto;
import org.entando.entando.aps.system.services.database.model.ShortDumpReportDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.database.validator.DatabaseValidator;
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

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author E.Santoboni
 */
@RestController
@RequestMapping(value = "/database")
public class DatabaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DatabaseValidator databaseValidator;
    @Autowired
    private IDatabaseService databaseService;

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<List<ShortDumpReportDto>>> getDumpReports(@Valid RestListRequest requestList) {
        this.getDatabaseValidator().validateRestListRequest(requestList, DumpReportDto.class);
        PagedMetadata<ShortDumpReportDto> result = this.getDatabaseService().getShortDumpReportDtos(requestList);
        this.getDatabaseValidator().validateRestListResult(requestList, result);
        return new ResponseEntity<>(new RestResponse(result.getBody(), new ArrayList<>(), result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getStatus() {
        Integer status = this.getDatabaseService().getStatus();
        Map<String, String> response = new HashMap<>();
        response.put("status", String.valueOf(status));
        logger.debug("Required database status -> {}", status);
        return new ResponseEntity<>(new RestResponse(response), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/initBackup", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<ComponentDto>> initBackup() {
        logger.debug("Required actual component configuration");
        List<ComponentDto> dtos = this.getDatabaseService().getCurrentComponents();
        logger.debug("Actual component configuration -> {}", dtos);
        return new ResponseEntity<>(new RestResponse(dtos), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/startBackup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> startBackup() throws Throwable {
        logger.debug("Starting database backup");
        this.getDatabaseService().startDatabaseBackup();
        Map<String, String> response = new HashMap<>();
        response.put("status", String.valueOf(this.getDatabaseService().getStatus()));
        logger.debug("Database backup started");
        return new ResponseEntity<>(new RestResponse(response), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/restoreBackup/{reportCode}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> restoreBackup(@PathVariable String reportCode) throws Throwable {
        logger.debug("Starting database restore -> code {}", reportCode);
        this.getDatabaseService().startDatabaseRestore(reportCode);
        Map<String, String> response = new HashMap<>();
        response.put("status", String.valueOf(this.getDatabaseService().getStatus()));
        logger.debug("Database restore started -> code {}", reportCode);
        return new ResponseEntity<>(new RestResponse(response), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/report/{reportCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<DumpReportDto>> getDumpReport(@PathVariable String reportCode) {
        logger.debug("Required dump report -> code {}", reportCode);
        DumpReportDto result = this.getDatabaseService().getDumpReportDto(reportCode);
        logger.debug("Extracted dump report -> {}", result);
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/report/{reportCode}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> deleteDumpReport(@PathVariable String reportCode) {
        logger.debug("Deleting dump report -> code {}", reportCode);
        this.getDatabaseService().deleteDumpReport(reportCode);
        logger.debug("Deleted dump report -> {}", reportCode);
        Map<String, String> response = new HashMap<>();
        response.put("code", reportCode);
        return new ResponseEntity<>(new RestResponse(response), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/report/{reportCode}/dump/{dataSource}/{tableName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getTableDump(@PathVariable String reportCode,
            @PathVariable String dataSource, @PathVariable String tableName) {
        logger.debug("Required dump report -> code {} - database {} - table {}", reportCode, dataSource, tableName);
        byte[] base64 = this.getDatabaseService().getTableDump(reportCode, dataSource, tableName);
        Map<String, Object> response = new HashMap<>();
        response.put("base64", base64);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("reportCode", reportCode);
        metadata.put("dataSource", dataSource);
        metadata.put("tableName", tableName);
        return new ResponseEntity<>(new RestResponse(response, new ArrayList<>(), metadata), HttpStatus.OK);
    }

    public DatabaseValidator getDatabaseValidator() {
        return databaseValidator;
    }

    public void setDatabaseValidator(DatabaseValidator databaseValidator) {
        this.databaseValidator = databaseValidator;
    }

    public IDatabaseService getDatabaseService() {
        return databaseService;
    }

    public void setDatabaseService(IDatabaseService databaseService) {
        this.databaseService = databaseService;
    }

}
