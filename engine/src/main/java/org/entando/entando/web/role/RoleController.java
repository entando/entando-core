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
package org.entando.entando.web.role;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.Permission;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.entando.entando.aps.system.services.role.IRoleService;
import org.entando.entando.aps.system.services.role.model.RoleDto;
import org.entando.entando.aps.system.services.user.model.UserDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.role.model.RoleRequest;
import org.entando.entando.web.role.validator.RoleValidator;
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

@RestController
@RequestMapping(value = "/roles")
public class RoleController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IRoleService roleService;

    private RoleValidator roleValidator = new RoleValidator();

    public IRoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(IRoleService roleService) {
        this.roleService = roleService;
    }

    public RoleValidator getRoleValidator() {
        return roleValidator;
    }

    public void setRoleValidator(RoleValidator roleValidator) {
        this.roleValidator = roleValidator;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<List<RoleDto>>> getRoles(RestListRequest requestList) throws JsonProcessingException {
        this.getRoleValidator().validateRestListRequest(requestList, RoleDto.class);
        PagedMetadata<RoleDto> result = this.getRoleService().getRoles(requestList);
        this.getRoleValidator().validateRestListResult(requestList, result);
        logger.debug("loading role list -> {}", result);
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{roleCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<RoleDto>> getRole(@PathVariable String roleCode) {
        logger.debug("loading role {}", roleCode);
        RoleDto role = this.getRoleService().getRole(roleCode);
        return new ResponseEntity<>(new RestResponse(role), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{roleCode}/userreferences", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<List<UserDto>>> getRoleReferences(@PathVariable String roleCode, RestListRequest requestList) {
        logger.debug("loading user references for role {}", roleCode);
        PagedMetadata<UserDto> result = this.getRoleService().getRoleReferences(roleCode, requestList);
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{roleCode}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<RoleDto>> updateRole(@PathVariable String roleCode, @Valid @RequestBody RoleRequest roleRequest, BindingResult bindingResult) {
        logger.debug("updating role {}", roleCode);
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getRoleValidator().validateBodyName(roleCode, roleRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        RoleDto role = this.getRoleService().updateRole(roleRequest);
        return new ResponseEntity<>(new RestResponse(role), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<RoleDto>> addRole(@Valid @RequestBody RoleRequest roleRequest, BindingResult bindingResult) throws ApsSystemException {
        logger.debug("adding role");
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        RoleDto dto = this.getRoleService().addRole(roleRequest);
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{roleCode}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> deleteRole(@PathVariable String roleCode) throws ApsSystemException {
        logger.info("deleting {}", roleCode);
        this.getRoleService().removeRole(roleCode);
        Map<String, String> result = new HashMap<>();
        result.put("code", roleCode);
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

}
