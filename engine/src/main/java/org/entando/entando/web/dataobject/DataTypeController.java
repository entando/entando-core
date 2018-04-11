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

import org.entando.entando.web.dataobject.validator.DataTypeValidator;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.Permission;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.services.dataobject.IDataObjectService;
import org.entando.entando.aps.system.services.dataobject.model.DataTypeDto;
import org.entando.entando.aps.system.services.entity.model.AttributeTypeDto;
import org.entando.entando.aps.system.services.entity.model.EntityAttributeFullDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.dataobject.model.DataTypeDtoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<RestResponse> getDataTypes(@Valid RestListRequest requestList) throws JsonProcessingException {
        this.getDataTypeValidator().validateRestListRequest(requestList, DataTypeDto.class);
        PagedMetadata<EntityTypeShortDto> result = this.getDataObjectService().getShortDataTypes(requestList);
        this.getDataTypeValidator().validateRestListResult(requestList, result);
        logger.debug("Main Response -> {}", result);
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypes/{dataTypeCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getDataType(@PathVariable String dataTypeCode) throws JsonProcessingException {
        logger.debug("Requested data type -> {}", dataTypeCode);
        if (!this.getDataTypeValidator().existType(dataTypeCode)) {
            throw new RestRourceNotFoundException(DataTypeValidator.ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST, "Data Type", dataTypeCode);
        }
        DataTypeDto dto = this.getDataObjectService().getDataType(dataTypeCode);
        logger.debug("Main Response -> {}", dto);
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> addDataTypes(@Valid @RequestBody DataTypeDtoRequest bodyRequest,
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
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypes/{dataTypeCode}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> updateDataType(@PathVariable String dataTypeCode,
            @Valid @RequestBody DataTypeDtoRequest request, BindingResult bindingResult) throws JsonProcessingException {
        int result = this.getDataTypeValidator().validateBodyName(dataTypeCode, request, bindingResult);
        if (bindingResult.hasErrors()) {
            if (result == 404) {
                throw new RestRourceNotFoundException(DataTypeValidator.ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST, "data type", dataTypeCode);
            } else {
                throw new ValidationGenericException(bindingResult);
            }
        }
        DataTypeDto dto = this.getDataObjectService().updateDataType(request, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        logger.debug("Main Response -> {}", dto);
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypes/{dataTypeCode}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> deleteDataType(@PathVariable String dataTypeCode) throws ApsSystemException {
        logger.debug("Deleting data type -> {}", dataTypeCode);
        this.getDataObjectService().deleteDataType(dataTypeCode);
        Map<String, String> payload = new HashMap<>();
        payload.put("code", dataTypeCode);
        return new ResponseEntity<>(new RestResponse(payload), HttpStatus.OK);
    }

    // ********************* ATTRIBUTE TYPES *********************
    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypeAttributes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getDataTypeAttributeTypes(RestListRequest requestList) throws JsonProcessingException {
        this.getDataTypeValidator().validateRestListRequest(requestList, AttributeTypeDto.class);
        PagedMetadata<String> result = this.getDataObjectService().getAttributeTypes(requestList);
        logger.debug("Main Response -> {}", result);
        this.getDataTypeValidator().validateRestListResult(requestList, result);
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypeAttributes/{attributeTypeCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getDataTypeAttributeType(@PathVariable String attributeTypeCode) throws JsonProcessingException {
        logger.debug("Extracting attribute type -> {}", attributeTypeCode);
        AttributeTypeDto attribute = this.getDataObjectService().getAttributeType(attributeTypeCode);
        logger.debug("Main Response -> {}", attribute);
        return new ResponseEntity<>(new RestResponse(attribute), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypes/{dataTypeCode}/attribute/{attributeCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getDataTypeAttribute(@PathVariable String dataTypeCode, @PathVariable String attributeCode) throws JsonProcessingException {
        logger.debug("Requested data type {} - attribute {}", dataTypeCode, attributeCode);
        EntityAttributeFullDto dto = this.getDataObjectService().getDataTypeAttribute(dataTypeCode, attributeCode);
        logger.debug("Main Response -> {}", dto);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("dataTypeCode", dataTypeCode);
        return new ResponseEntity<>(new RestResponse(dto, null, metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypes/{dataTypeCode}/attribute", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> addDataTypeAttribute(@PathVariable String dataTypeCode, @Valid @RequestBody EntityAttributeFullDto bodyRequest,
            BindingResult bindingResult) throws JsonProcessingException {
        logger.debug("Data type {} - Adding attribute {}", dataTypeCode, bodyRequest);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        EntityAttributeFullDto result = this.getDataObjectService().addDataTypeAttribute(dataTypeCode, bodyRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        logger.debug("Main Response -> {}", result);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("dataTypeCode", dataTypeCode);
        return new ResponseEntity<>(new RestResponse(result, null, metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypes/{dataTypeCode}/attribute/{attributeCode}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> updateDataTypeAttribute(@PathVariable String dataTypeCode,
            @PathVariable String attributeCode, @Valid @RequestBody EntityAttributeFullDto bodyRequest, BindingResult bindingResult) throws JsonProcessingException {
        logger.debug("Data type {} - Updating attribute {}", dataTypeCode, bodyRequest);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        } else if (!StringUtils.equals(attributeCode, bodyRequest.getCode())) {
            bindingResult.rejectValue("code", DataTypeValidator.ERRCODE_URINAME_MISMATCH, new String[]{attributeCode, bodyRequest.getCode()}, "entityType.attribute.code.mismatch");
            throw new ValidationConflictException(bindingResult);
        }
        EntityAttributeFullDto result = this.getDataObjectService().updateDataTypeAttribute(dataTypeCode, bodyRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        logger.debug("Main Response -> {}", result);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("dataTypeCode", dataTypeCode);
        return new ResponseEntity<>(new RestResponse(result, null, metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypes/{dataTypeCode}/attribute/{attributeCode}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> deleteDataTypeAttribute(@PathVariable String dataTypeCode, @PathVariable String attributeCode) throws ApsSystemException {
        logger.debug("Deleting attribute {} from profile type {}", attributeCode, dataTypeCode);
        this.getDataObjectService().deleteDataTypeAttribute(dataTypeCode, attributeCode);
        Map<String, String> result = new HashMap<>();
        result.put("dataTypeCode", dataTypeCode);
        result.put("attributeCode", attributeCode);
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dataTypes/refresh/{dataTypeCode}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> reloadReferences(@PathVariable String dataTypeCode) throws Throwable {
        logger.debug("reload references of data type {}", dataTypeCode);
        this.getDataObjectService().reloadDataTypeReferences(dataTypeCode);
        Map<String, String> result = new HashMap<>();
        result.put("status", "success");
        result.put("dataTypeCode", dataTypeCode);
        logger.debug("started reload references of profile type {}", dataTypeCode);
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

}
