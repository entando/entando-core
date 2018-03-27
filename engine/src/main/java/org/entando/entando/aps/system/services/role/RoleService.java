/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.role;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationService;
import com.agiletec.aps.system.services.authorization.model.UserDto;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Role;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.role.model.RoleDto;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.role.model.RoleRequest;
import org.entando.entando.web.role.validator.RoleValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;

public class RoleService implements IRoleService {

    private static final Object KEY_FILTER_CODE = "code";
    private static final Object KEY_FILTER_DESCR = "name";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private IRoleManager roleManager;
    private RoleDdoBuilder dtoBuilder;
    private IAuthorizationService authorizationService;

    protected IRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(IRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    protected RoleDdoBuilder getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(RoleDdoBuilder dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    protected IAuthorizationService getAuthorizationService() {
        return authorizationService;
    }

    public void setAuthorizationService(IAuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public PagedMetadata<RoleDto> getRoles(RestListRequest restRequest) {
        List<Role> roles = this.getRoleManager().getRoles();
        roles = sortList(restRequest, roles);

        if (null != restRequest.getFilter()) {

            for (Filter f : restRequest.getFilter()) {
                if (f.getAttributeName().equals(KEY_FILTER_CODE)) {
                    roles = roles
                                 .stream()
                                 .filter(i -> i.getName().toLowerCase().contains(f.getValue()))
                                 .collect(Collectors.toList());
                }
                if (f.getAttributeName().equals(KEY_FILTER_DESCR)) {
                    roles = roles
                                 .stream()
                                 .filter(i -> i.getDescription().toLowerCase().contains(f.getValue()))
                                 .collect(Collectors.toList());
                }
            }
        }

        List<Role> subList = restRequest.getSublist(roles);
        List<RoleDto> dtoSlice = this.getDtoBuilder().convert(subList);
        SearcherDaoPaginatedResult<RoleDto> paginatedResult = new SearcherDaoPaginatedResult(roles.size(), dtoSlice);
        PagedMetadata<RoleDto> pagedMetadata = new PagedMetadata<>(restRequest, paginatedResult);
        pagedMetadata.setBody(dtoSlice);
        return pagedMetadata;
    }

    @Override
    public RoleDto updateRole(RoleRequest roleRequest) {
        try {
            Role role = this.getRoleManager().getRole(roleRequest.getCode());

            if (null == role) {
                logger.warn("no role found with code {}", roleRequest.getCode());
                throw new RestRourceNotFoundException(RoleValidator.ERRCODE_ROLE_NOT_FOUND, "role", roleRequest.getCode());
            }

            role.setDescription(roleRequest.getName());
            role.getPermissions().clear();
            if (null != roleRequest.getPermissions()) {
                roleRequest.getPermissions().forEach(i -> role.addPermission(i));
            }
            BeanPropertyBindingResult validationResult = this.validateRoleForUpdate(role);
            if (validationResult.hasErrors()) {
                throw new ValidationConflictException(validationResult);
            }

            this.getRoleManager().updateRole(role);
            RoleDto dto = this.getDtoBuilder().toDto(role, this.getRoleManager().getPermissionsCodes());
            return dto;
        } catch (ApsSystemException e) {
            logger.error("Error updating a role", e);
            throw new RestServerError("error in update role", e);
        }
    }

    @Override
    public RoleDto addRole(RoleRequest roleRequest) {
        try {
            Role role = this.createRole(roleRequest);
            BeanPropertyBindingResult validationResult = this.validateRoleForAdd(role);
            if (validationResult.hasErrors()) {
                throw new ValidationConflictException(validationResult);
            }
            this.getRoleManager().addRole(role);
            RoleDto dto = this.getDtoBuilder().toDto(role, this.getRoleManager().getPermissionsCodes());
            return dto;
        } catch (ApsSystemException e) {
            logger.error("Error adding a role", e);
            throw new RestServerError("error in add role", e);
        }
    }

    @Override
    public void removeRole(String roleCode) {
        try {
            Role role = this.getRoleManager().getRole(roleCode);
            if (null == role) {
                logger.info("role {} does not exists", roleCode);
                return;
            }
            BeanPropertyBindingResult validationResult = this.validateRoleForDelete(role);
            if (validationResult.hasErrors()) {
                throw new ValidationConflictException(validationResult);
            }
            this.getRoleManager().removeRole(role);
        } catch (ApsSystemException e) {
            logger.error("Error in delete role {}", roleCode, e);
            throw new RestServerError("error in delete role", e);
        }
    }

    @Override
    public RoleDto getRole(String roleCode) {
        Role role = this.getRoleManager().getRole(roleCode);
        if (null == role) {
            logger.warn("no role found with code {}", roleCode);
            throw new RestRourceNotFoundException(RoleValidator.ERRCODE_ROLE_NOT_FOUND, "role", roleCode);
        }
        RoleDto dto = this.getDtoBuilder().toDto(role, this.getRoleManager().getPermissionsCodes());
        return dto;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public PagedMetadata<UserDto> getRoleReferences(String roleCode, RestListRequest restRequest) {
        Role role = this.getRoleManager().getRole(roleCode);
        if (null == role) {
            logger.warn("no role found with code {}", roleCode);
            throw new RestRourceNotFoundException(RoleValidator.ERRCODE_ROLE_NOT_FOUND, "role", roleCode);
        }
        List<UserDto> dtoList = this.getAuthorizationService().getRoleUtilizer(roleCode);
        List<UserDto> subList = restRequest.getSublist(dtoList);
        SearcherDaoPaginatedResult<UserDto> pagedResult = new SearcherDaoPaginatedResult(dtoList.size(), subList);
        PagedMetadata<UserDto> pagedMetadata = new PagedMetadata<>(restRequest, pagedResult);
        pagedMetadata.setBody(subList);
        return pagedMetadata;
    }

    protected BeanPropertyBindingResult validateRoleForAdd(Role role) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(role, "role");
        validateCodeExists(role, errors);
        validatePermissions(role, errors);
        return errors;
    }

    protected BeanPropertyBindingResult validateRoleForDelete(Role role) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(role, "role");
        List<UserDto> users = this.getAuthorizationService().getRoleUtilizer(role.getName());
        if (!users.isEmpty()) {
            errors.reject(RoleValidator.ERRCODE_ROLE_REFERENCES, new String[]{role.getName()}, "role.references");
        }
        return errors;
    }

    protected BeanPropertyBindingResult validateRoleForUpdate(Role role) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(role, "role");
        validatePermissions(role, errors);
        return errors;
    }

    protected void validateCodeExists(Role role, BeanPropertyBindingResult errors) {
        if (null != roleManager.getRole(role.getName())) {
            errors.reject(RoleValidator.ERRCODE_ROLE_ALREADY_EXISTS, new String[]{role.getName()}, "role.exists");
        }
    }

    protected void validatePermissions(Role role, BeanPropertyBindingResult errors) {
        List<String> systemPermissions = this.getRoleManager().getPermissionsCodes();
        for (String permission : role.getPermissions()) {
            if (!systemPermissions.contains(permission)) {
                errors.reject(RoleValidator.ERRCODE_PERMISSON_NOT_FOUND, new String[]{permission}, "role.permission.notFound");
            }
        }
    }

    protected Role createRole(RoleRequest roleRequest) {
        Role role = new Role();
        role.setName(roleRequest.getCode());
        role.setDescription(roleRequest.getName());
        if (null != roleRequest.getPermissions()) {
            roleRequest.getPermissions().forEach(i -> role.addPermission(i));
        }
        return role;
    }

    protected List<Role> sortList(RestListRequest restRequest, List<Role> roles) {
        if (restRequest.getSort().equals(KEY_FILTER_DESCR)) {
            if (restRequest.getDirection().equals(FieldSearchFilter.DESC_ORDER)) {
                roles = roles.stream().sorted(Comparator.comparing(Role::getDescription).reversed()).collect(Collectors.toList());
            } else {
                roles = roles.stream().sorted(Comparator.comparing(Role::getDescription)).collect(Collectors.toList());
            }
        } else {
            if (restRequest.getDirection().equals(FieldSearchFilter.DESC_ORDER)) {
                roles = roles.stream().sorted(Comparator.comparing(Role::getName).reversed()).collect(Collectors.toList());
            } else {
                roles = roles.stream().sorted(Comparator.comparing(Role::getName)).collect(Collectors.toList());
            }
        }
        return roles;
    }

    protected List<UserDto> userReferences(String code) throws ApsSystemException {
        return this.getAuthorizationService().getRoleUtilizer(code);

    }

}
