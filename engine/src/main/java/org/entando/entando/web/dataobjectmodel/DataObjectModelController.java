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
package org.entando.entando.web.dataobjectmodel;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.Permission;
import javax.validation.Valid;
import org.entando.entando.aps.system.services.dataobjectmodel.IDataObjectModelService;
import org.entando.entando.aps.system.services.dataobjectmodel.model.DataModelDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.dataobjectmodel.model.DataObjectModelRequest;
import org.entando.entando.web.dataobjectmodel.validator.DataObjectModelValidator;
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

@RestController
@RequestMapping(value = "/dataModels")
public class DataObjectModelController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IDataObjectModelService dataObjectModelService;

    @Autowired
    private DataObjectModelValidator dataObjectModelValidator;

    public IDataObjectModelService getDataObjectModelService() {
        return dataObjectModelService;
    }

    public void setDataObjectModelService(IDataObjectModelService dataObjectModelService) {
        this.dataObjectModelService = dataObjectModelService;
    }

    public DataObjectModelValidator getDataObjectModelValidator() {
        return dataObjectModelValidator;
    }

    public void setDataObjectModelValidator(DataObjectModelValidator dataObjectModelValidator) {
        this.dataObjectModelValidator = dataObjectModelValidator;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDataObjectModels(RestListRequest requestList) {
        PagedMetadata<DataModelDto> result = this.getDataObjectModelService().getDataObjectModels(requestList);
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{dataModelId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDataObjectModel(@Valid @PathVariable Long dataModelId) {
        DataModelDto dataModelDto = this.getDataObjectModelService().getDataObjectModel(dataModelId);
        return new ResponseEntity<>(new RestResponse(dataModelDto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addDataObjectModel(@Valid @RequestBody DataObjectModelRequest dataObjectModelRequest, BindingResult bindingResult) throws ApsSystemException {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations
        this.getDataObjectModelValidator().validate(dataObjectModelRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationConflictException(bindingResult);
        }
        DataModelDto dataModelDto = this.getDataObjectModelService().addDataObjectModel(dataObjectModelRequest);
        return new ResponseEntity<>(new RestResponse(dataModelDto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{dataModelId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateGroup(@Valid @PathVariable Long dataModelId, @Valid @RequestBody DataObjectModelRequest dataObjectModelRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getDataObjectModelValidator().validateBodyName(dataModelId, dataObjectModelRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        DataModelDto dataModelDto = this.getDataObjectModelService().updateDataObjectModel(dataObjectModelRequest);
        return new ResponseEntity<>(new RestResponse(dataModelDto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{dataModelId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteGroup(@Valid @PathVariable Long dataModelId) throws ApsSystemException {
        logger.info("deleting {}", dataModelId);
        this.getDataObjectModelService().removeDataObjectModel(dataModelId);
        return new ResponseEntity<>(new RestResponse(dataModelId), HttpStatus.OK);
    }

}
