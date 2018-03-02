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

import com.agiletec.aps.system.services.role.Permission;
import org.entando.entando.aps.system.services.dataobjectmodel.IDataObjectModelService;
import org.entando.entando.aps.system.services.dataobjectmodel.model.DataModelDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.dataobjectmodel.validator.DataObjectModelValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    /*
    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{groupCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getGroup(@PathVariable String groupCode) {
        GroupDto group = this.getGroupService().getGroup(groupCode);
        return new ResponseEntity<>(new RestResponse(group), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{groupCode}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateGroup(@PathVariable String groupCode, @Valid @RequestBody GroupRequest groupRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getGroupValidator().validateBodyName(groupCode, groupRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        GroupDto group = this.getGroupService().updateGroup(groupCode, groupRequest.getName());
        return new ResponseEntity<>(new RestResponse(group), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addGroup(@Valid @RequestBody GroupRequest groupRequest, BindingResult bindingResult) throws ApsSystemException {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations
        getGroupValidator().validate(groupRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationConflictException(bindingResult);
        }
        GroupDto dto = this.getGroupService().addGroup(groupRequest);
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = "group_delete")
    @RequestMapping(value = "/{groupName}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteGroup(@PathVariable String groupName) throws ApsSystemException {
        logger.info("deleting {}", groupName);
        this.getGroupService().removeGroup(groupName);
        return new ResponseEntity<>(new RestResponse(groupName), HttpStatus.OK);
    }
     */
}
