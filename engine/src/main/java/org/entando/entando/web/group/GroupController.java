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
package org.entando.entando.web.group;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.Permission;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.entando.entando.aps.system.services.group.IGroupService;
import org.entando.entando.aps.system.services.group.model.GroupDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.group.model.GroupRequest;
import org.entando.entando.web.group.validator.GroupValidator;
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
import java.util.List;
import java.util.Map;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.SimpleRestResponse;

@RestController
@RequestMapping(value = "/groups")
public class GroupController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IGroupService groupService;

    @Autowired
    private GroupValidator groupValidator;

    public IGroupService getGroupService() {
        return groupService;
    }

    public void setGroupService(IGroupService groupService) {
        this.groupService = groupService;
    }

    public GroupValidator getGroupValidator() {
        return groupValidator;
    }

    public void setGroupValidator(GroupValidator groupValidator) {
        this.groupValidator = groupValidator;
    }

    @RestAccessControl(permission = Permission.ENTER_BACKEND)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<GroupDto>> getGroups(RestListRequest requestList) throws JsonProcessingException {
        this.getGroupValidator().validateRestListRequest(requestList, GroupDto.class);
        PagedMetadata<GroupDto> result = this.getGroupService().getGroups(requestList);
        this.getGroupValidator().validateRestListResult(requestList, result);
        logger.debug("Main Response -> {}", result);
        return new ResponseEntity<>(new PagedRestResponse<>(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{groupCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<GroupDto>> getGroup(@PathVariable String groupCode) {
        GroupDto group = this.getGroupService().getGroup(groupCode);
        return new ResponseEntity<>(new SimpleRestResponse<>(group), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{groupCode}/references/{holder}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<?>> getGroupReferences(@PathVariable String groupCode, @PathVariable String holder, RestListRequest requestList) {
        PagedMetadata<?> result = this.getGroupService().getGroupReferences(groupCode, holder, requestList);
        return new ResponseEntity<>(new PagedRestResponse<>(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{groupCode}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<GroupDto>> updateGroup(@PathVariable String groupCode, @Valid @RequestBody GroupRequest groupRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getGroupValidator().validateBodyName(groupCode, groupRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        GroupDto group = this.getGroupService().updateGroup(groupCode, groupRequest.getName());
        return new ResponseEntity<>(new SimpleRestResponse<>(group), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<GroupDto>> addGroup(@Valid @RequestBody GroupRequest groupRequest, BindingResult bindingResult) throws ApsSystemException {
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
        return new ResponseEntity<>(new SimpleRestResponse<>(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{groupName}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<Map>> deleteGroup(@PathVariable String groupName) throws ApsSystemException {
        logger.info("deleting {}", groupName);
        this.getGroupService().removeGroup(groupName);
        Map<String, String> result = new HashMap<>();
        result.put("code", groupName);
        return new ResponseEntity<>(new SimpleRestResponse<>(result), HttpStatus.OK);
    }

}
