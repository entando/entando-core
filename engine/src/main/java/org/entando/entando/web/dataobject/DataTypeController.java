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
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.validation.Valid;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.services.dataobject.IDataObjectService;
import org.entando.entando.aps.system.services.dataobject.model.DataTypeDto;
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
@RequestMapping(value = "/dataTypes")
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
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDataTypes(RestListRequest requestList) throws JsonProcessingException {
        PagedMetadata<EntityTypeShortDto> result = this.getDataObjectService().getShortDataTypes(requestList);
        logger.debug("Main Response -> " + new ObjectMapper().writeValueAsString(result));
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{dataTypeCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDataType(@PathVariable String dataTypeCode) throws JsonProcessingException {
        logger.debug("Requested data type -> " + dataTypeCode);
        if (!this.getDataTypeValidator().existType(dataTypeCode)) {
            throw new RestRourceNotFoundException(DataTypeValidator.ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST, "Data Type", dataTypeCode);
        }
        DataTypeDto dto = this.getDataObjectService().getDataType(dataTypeCode);
        logger.debug("Main Response -> " + new ObjectMapper().writeValueAsString(dto));
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addDataTypes(@Valid @RequestBody DataTypeDtoRequest bodyRequest,
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
        logger.debug("Main Response -> " + new ObjectMapper().writeValueAsString(result));
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{dataTypeCode}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateDataType(@PathVariable String dataTypeCode,
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
        logger.debug("Main Response -> " + new ObjectMapper().writeValueAsString(dto));
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{dataTypeCode}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteDataType(@PathVariable String dataTypeCode) throws ApsSystemException {
        logger.debug("Deleting data type -> " + dataTypeCode);
        this.getDataObjectService().deleteDataType(dataTypeCode);
        return new ResponseEntity<>(new RestResponse(dataTypeCode), HttpStatus.OK);
    }

}
