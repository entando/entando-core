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
package org.entando.entando.web.dataobject;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.Permission;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.services.dataobject.IDataObjectService;
import org.entando.entando.aps.system.services.dataobject.model.DataTypeDto;
import org.entando.entando.aps.system.services.entity.model.AttributeTypeDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeAttributeFullDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypesStatusDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.dataobject.model.DataTypeDtoRequest;
import org.entando.entando.web.dataobject.model.DataTypeRefreshRequest;
import org.entando.entando.web.dataobject.validator.DataTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.SimpleRestResponse;

/**
 * @author E.Santoboni
 */
@RestController
public class DataTypeController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IDataObjectService dataObjectService;

    @Autowired
    private DataTypeValidator dataTypeValidator;

    protected IDataObjectService getDataObjectService() {
        return dataObjectService;
    }

    public void setDataObjectService(IDataObjectService dataObjectService) {
        this.dataObjectService = dataObjectService;
    }

    public DataTypeValidator getDataTypeValidator() {
        return dataTypeValidator;
    }

    public void setDataTypeValidator(DataTypeValidator dataTypeValidator) {
        this.dataTypeValidator = dataTypeValidator;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<EntityTypeShortDto>> getDataTypes(@Valid RestListRequest requestList) throws JsonProcessingException {
        this.getDataTypeValidator().validateRestListRequest(requestList, DataTypeDto.class);
        PagedMetadata<EntityTypeShortDto> result = this.getDataObjectService().getShortDataTypes(requestList);
        this.getDataTypeValidator().validateRestListResult(requestList, result);
        logger.debug("Main Response -> {}", result);
        return new ResponseEntity<>(new PagedRestResponse<>(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypes/{dataTypeCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DataTypeDto>> getDataType(@PathVariable String dataTypeCode) throws JsonProcessingException {
        logger.debug("Requested data type -> {}", dataTypeCode);
        if (!this.getDataTypeValidator().existType(dataTypeCode)) {
            throw new ResourceNotFoundException(DataTypeValidator.ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST, "Data Type", dataTypeCode);
        }
        DataTypeDto dto = this.getDataObjectService().getDataType(dataTypeCode);
        logger.debug("Main Response -> {}", dto);
        return new ResponseEntity<>(new SimpleRestResponse<>(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DataTypeDto>> addDataTypes(@Valid @RequestBody DataTypeDtoRequest bodyRequest,
            BindingResult bindingResult) throws JsonProcessingException {
        //field validations
        this.getDataTypeValidator().validate(bodyRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations
        if (this.getDataTypeValidator().existType(bodyRequest.getCode())) {
            bindingResult.reject(DataTypeValidator.ERRCODE_ENTITY_TYPE_ALREADY_EXISTS, new String[]{bodyRequest.getCode()}, "entityType.exists");
        }
        if (bindingResult.hasErrors()) {
            throw new ValidationConflictException(bindingResult);
        }
        DataTypeDto result = this.getDataObjectService().addDataType(bodyRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        logger.debug("Main Response -> {}", result);
        return new ResponseEntity<>(new SimpleRestResponse<>(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypes/{dataTypeCode}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DataTypeDto>> updateDataType(@PathVariable String dataTypeCode,
            @Valid @RequestBody DataTypeDtoRequest request, BindingResult bindingResult) throws JsonProcessingException {
        int result = this.getDataTypeValidator().validateBodyName(dataTypeCode, request, bindingResult);
        if (bindingResult.hasErrors()) {
            if (result == 404) {
                throw new ResourceNotFoundException(DataTypeValidator.ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST, "data type", dataTypeCode);
            } else {
                throw new ValidationGenericException(bindingResult);
            }
        }
        DataTypeDto dto = this.getDataObjectService().updateDataType(request, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        logger.debug("Main Response -> {}", dto);
        return new ResponseEntity<>(new SimpleRestResponse<>(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypes/{dataTypeCode}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<Map>> deleteDataType(@PathVariable String dataTypeCode) throws ApsSystemException {
        logger.debug("Deleting data type -> {}", dataTypeCode);
        this.getDataObjectService().deleteDataType(dataTypeCode);
        Map<String, String> payload = new HashMap<>();
        payload.put("code", dataTypeCode);
        return new ResponseEntity<>(new SimpleRestResponse<>(payload), HttpStatus.OK);
    }

    // ********************* ATTRIBUTE TYPES *********************
    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypeAttributes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<String>> getDataTypeAttributeTypes(RestListRequest requestList) throws JsonProcessingException {
        this.getDataTypeValidator().validateRestListRequest(requestList, AttributeTypeDto.class);
        PagedMetadata<String> result = this.getDataObjectService().getAttributeTypes(requestList);
        logger.debug("Main Response -> {}", result);
        this.getDataTypeValidator().validateRestListResult(requestList, result);
        return new ResponseEntity<>(new PagedRestResponse<>(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypeAttributes/{attributeTypeCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<AttributeTypeDto>> getDataTypeAttributeType(@PathVariable String attributeTypeCode) throws JsonProcessingException {
        logger.debug("Extracting attribute type -> {}", attributeTypeCode);
        AttributeTypeDto attribute = this.getDataObjectService().getAttributeType(attributeTypeCode);
        logger.debug("Main Response -> {}", attribute);
        return new ResponseEntity<>(new SimpleRestResponse<>(attribute), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypes/{dataTypeCode}/attribute/{attributeCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<EntityTypeAttributeFullDto, Map<String, String>>> getDataTypeAttribute(@PathVariable String dataTypeCode, @PathVariable String attributeCode) throws JsonProcessingException {
        logger.debug("Requested data type {} - attribute {}", dataTypeCode, attributeCode);
        EntityTypeAttributeFullDto dto = this.getDataObjectService().getDataTypeAttribute(dataTypeCode, attributeCode);
        logger.debug("Main Response -> {}", dto);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("dataTypeCode", dataTypeCode);
        return new ResponseEntity<>(new RestResponse<>(dto, metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypes/{dataTypeCode}/attribute", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<EntityTypeAttributeFullDto, Map<String, String>>> addDataTypeAttribute(@PathVariable String dataTypeCode, @Valid @RequestBody EntityTypeAttributeFullDto bodyRequest,
            BindingResult bindingResult) throws JsonProcessingException {
        logger.debug("Data type {} - Adding attribute {}", dataTypeCode, bodyRequest);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        EntityTypeAttributeFullDto result = this.getDataObjectService().addDataTypeAttribute(dataTypeCode, bodyRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        logger.debug("Main Response -> {}", result);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("dataTypeCode", dataTypeCode);
        return new ResponseEntity<>(new RestResponse<>(result, metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypes/{dataTypeCode}/attribute/{attributeCode}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<EntityTypeAttributeFullDto, Map<String, String>>> updateDataTypeAttribute(@PathVariable String dataTypeCode,
            @PathVariable String attributeCode, @Valid @RequestBody EntityTypeAttributeFullDto bodyRequest, BindingResult bindingResult) throws JsonProcessingException {
        logger.debug("Data type {} - Updating attribute {}", dataTypeCode, bodyRequest);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        } else if (!StringUtils.equals(attributeCode, bodyRequest.getCode())) {
            bindingResult.rejectValue("code", DataTypeValidator.ERRCODE_URINAME_MISMATCH, new String[]{attributeCode, bodyRequest.getCode()}, "entityType.attribute.code.mismatch");
            throw new ValidationConflictException(bindingResult);
        }
        EntityTypeAttributeFullDto result = this.getDataObjectService().updateDataTypeAttribute(dataTypeCode, bodyRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        logger.debug("Main Response -> {}", result);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("dataTypeCode", dataTypeCode);
        return new ResponseEntity<>(new RestResponse<>(result, metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypes/{dataTypeCode}/attribute/{attributeCode}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<Map>> deleteDataTypeAttribute(@PathVariable String dataTypeCode, @PathVariable String attributeCode) throws ApsSystemException {
        logger.debug("Deleting attribute {} from data type {}", attributeCode, dataTypeCode);
        this.getDataObjectService().deleteDataTypeAttribute(dataTypeCode, attributeCode);
        Map<String, String> result = new HashMap<>();
        result.put("dataTypeCode", dataTypeCode);
        result.put("attributeCode", attributeCode);
        return new ResponseEntity<>(new SimpleRestResponse<>(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypes/refresh/{dataTypeCode}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<Map<String, String>>> reloadReferences(@PathVariable String dataTypeCode) throws Throwable {
        logger.debug("reload references of data type {}", dataTypeCode);
        this.getDataObjectService().reloadDataTypeReferences(dataTypeCode);
        Map<String, String> result = new HashMap<>();
        result.put("status", "success");
        result.put("dataTypeCode", dataTypeCode);
        logger.debug("started reload references of data type {}", dataTypeCode);
        return new ResponseEntity<>(new SimpleRestResponse<>(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypesStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<Map<String, Object>>> reloadReferences(@Valid @RequestBody DataTypeRefreshRequest bodyRequest,
            BindingResult bindingResult) throws Throwable {
        logger.debug("reload references of data types {}", bodyRequest.getDataTypeCodes());
        Map<String, Integer> status = this.getDataObjectService().reloadDataTypesReferences(bodyRequest.getDataTypeCodes());
        Map<String, Object> result = new HashMap<>();
        result.put("result", "success");
        result.put("dataTypeCodes", status);
        logger.debug("started reload references of data types {}", bodyRequest.getDataTypeCodes());
        return new ResponseEntity<>(new SimpleRestResponse<>(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypesStatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<EntityTypesStatusDto>> extractStatus() throws Throwable {
        logger.debug("Extract data types status");
        EntityTypesStatusDto status = this.getDataObjectService().getDataTypesRefreshStatus();
        logger.debug("Extracted data types status {}", status);
        return new ResponseEntity<>(new SimpleRestResponse<>(status), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypes/{dataTypeCode}/attribute/{attributeCode}/moveUp", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<Map<String, String>>> moveDataTypeAttributeUp(@PathVariable String dataTypeCode, @PathVariable String attributeCode) throws ApsSystemException {
        logger.debug("Move UP attribute {} from data type {}", attributeCode, dataTypeCode);
        return this.moveDataTypeAttribute(dataTypeCode, attributeCode, true);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypes/{dataTypeCode}/attribute/{attributeCode}/moveDown", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<Map<String, String>>> moveDataTypeAttributeDown(@PathVariable String dataTypeCode, @PathVariable String attributeCode) throws ApsSystemException {
        logger.debug("Move DOWN attribute {} from data type {}", attributeCode, dataTypeCode);
        return this.moveDataTypeAttribute(dataTypeCode, attributeCode, false);
    }

    private ResponseEntity<SimpleRestResponse<Map<String, String>>> moveDataTypeAttribute(String dataTypeCode, String attributeCode, boolean moveUp) throws ApsSystemException {
        this.getDataObjectService().moveDataTypeAttribute(dataTypeCode, attributeCode, moveUp);
        Map<String, String> result = new HashMap<>();
        result.put("dataTypeCode", dataTypeCode);
        result.put("attributeCode", attributeCode);
        String movement = (moveUp) ? "UP" : "DOWN";
        result.put("movement", movement);
        return new ResponseEntity<>(new SimpleRestResponse<>(result), HttpStatus.OK);
    }

}
