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
package org.entando.entando.aps.system.services.user;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.Authorization;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.user.*;

import java.util.*;
import java.util.stream.Collectors;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.role.IRoleService;
import org.entando.entando.aps.system.services.role.RoleService;
import org.entando.entando.aps.system.services.role.model.RoleDto;
import org.entando.entando.aps.system.services.user.model.UserAuthorityDto;
import org.entando.entando.aps.system.services.user.model.UserDto;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.user.model.UserAuthoritiesRequest;
import org.entando.entando.web.user.model.UserPasswordRequest;
import org.entando.entando.web.user.model.UserRequest;
import org.entando.entando.web.user.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

/**
 *
 * @author paddeo
 */
public class UserService implements IUserService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String ERRCODE_USER_NOT_FOUND = "1";

    @Autowired
    private IUserManager userManager;

    @Autowired
    private IUserProfileManager userProfileManager;

    @Autowired
    private IAuthorizationManager authorizationManager;

    @Autowired
    private IAuthenticationProviderManager authenticationProvider;

    @Autowired
    private IDtoBuilder<UserDetails, UserDto> dtoBuilder;

    @Autowired
    private IRoleService roleService;

    public IUserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    public IUserProfileManager getUserProfileManager() {
        return userProfileManager;
    }

    public void setUserProfileManager(IUserProfileManager userProfileManager) {
        this.userProfileManager = userProfileManager;
    }

    public IAuthorizationManager getAuthorizationManager() {
        return authorizationManager;
    }

    public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    public IAuthenticationProviderManager getAuthenticationProvider() {
        return authenticationProvider;
    }

    public void setAuthenticationProvider(IAuthenticationProviderManager authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    public IDtoBuilder<UserDetails, UserDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<UserDetails, UserDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @Override
    public List<UserAuthorityDto> getUserAuthorities(String username) {
        UserDetails user = this.loadUser(username);
        if (null == user) {
            return null;
        }
        List<UserAuthorityDto> dtos = new ArrayList<>();
        try {
            List<Authorization> auths = this.getAuthorizationManager().getUserAuthorizations(username);
            if (null != auths) {
                auths.forEach(auth -> dtos.add(new UserAuthorityDto(auth)));
            }
        } catch (ApsSystemException e) {
            logger.error("Error extracting auths for user {}", username, e);
            throw new RestServerError("Error extracting auths for user " + username, e);
        }
        return dtos;
    }

    @Override
    public List<UserAuthorityDto> addUserAuthorities(String username, UserAuthoritiesRequest request) {
        UserDetails user = this.loadUser(username);
        if (null == user) {
            return null;
        }
        List<UserAuthorityDto> authorizations = new ArrayList<>();
        request.forEach(authorization -> {
            try {
                if (!this.getAuthorizationManager().isAuthOnGroupAndRole(user, authorization.getGroup(), authorization.getRole(), true)) {
                    this.getAuthorizationManager().addUserAuthorization(username, authorization.getGroup(), authorization.getRole());
                }
            } catch (ApsSystemException ex) {
                logger.error("Error in add authorities for {}", username, ex);
                throw new RestServerError("Error in add authorities", ex);
            }
            authorizations.add(new UserAuthorityDto(authorization.getGroup(), authorization.getRole()));
        });
        return authorizations;
    }

    @Override
    public List<UserAuthorityDto> updateUserAuthorities(String username, UserAuthoritiesRequest request) {
        this.deleteUserAuthorities(username);
        return this.addUserAuthorities(username, request);
    }

    @Override
    public void deleteUserAuthorities(String username) {
        try {
            this.getAuthorizationManager().deleteUserAuthorizations(username);
        } catch (ApsSystemException e) {
            logger.error("Error in delete authorities for {}", username, e);
            throw new RestServerError("Error in delete authorities", e);
        }
    }

    @Override
    public PagedMetadata<UserDto> getUsers(RestListRequest requestList, String withProfile) {
        try {
            //transforms the filters by overriding the key specified in the request with the correct one known by the dto
            List<FieldSearchFilter> filters = new ArrayList<>(requestList.buildFieldSearchFilters());
            List<EntitySearchFilter> entityFilters = new ArrayList<>(requestList.buildEntitySearchFilters());
            filters.stream()
                    .filter(i -> ((i.getKey() != null) && (UserDto.getEntityFieldName(i.getKey()) != null)))
                    .forEach(i -> i.setKey(UserDto.getEntityFieldName(i.getKey())));
            List<String> userNames = null;
            List<UserDetails> users = new ArrayList<>(),
                    allUsers = new ArrayList<>();

            //username filter
            List<FieldSearchFilter> usernameFilter = filters.stream().filter(filter
                    -> filter.getValue() != null && filter.getKey().equals("username")).collect(Collectors.toList());
            if (usernameFilter.size() > 0) {
                String text = (String) filters.get(0).getValue();
                userNames = this.getUserManager().searchUsernames(text);
            } else {
                userNames = this.getUserManager().getUsernames();
            }
            userNames.forEach(username -> allUsers.add(this.loadUser(username)));

            // Profile and attributes filters
            users.addAll(allUsers.stream().filter(user
                    -> (withProfile == null || withProfile.equals("1"))
                    && checkAttributesFilter(filters, entityFilters, user)).collect(Collectors.toList()));

            List<UserDto> dtoList = dtoBuilder.convert(users);
            SearcherDaoPaginatedResult<UserDetails> result = new SearcherDaoPaginatedResult<>(users.size(), users);
            PagedMetadata<UserDto> pagedMetadata = new PagedMetadata<>(requestList, result);
            pagedMetadata.setBody(dtoList);
            pagedMetadata.imposeLimits();
            return pagedMetadata;
        } catch (Throwable t) {
            logger.error("error in search users", t);
            throw new RestServerError("error in search users", t);
        }
    }

    private boolean checkAttributesFilter(List<FieldSearchFilter> filters, List<EntitySearchFilter> entityFilters, UserDetails user) {
        if (!filters.stream().anyMatch(filter
                -> filter.getValue() != null && filter.getKey().equals("profileType"))) {
            return true;
        }
        if (user.getProfile() == null) {
            return false;
        }
        if (!filters.stream().anyMatch(filter
                -> filter.getValue() != null && (filter.getValue().equals(((IUserProfile) user.getProfile()).getTypeCode())
                || filter.getValue().equals("All")))) {
            return false;
        }
        if (entityFilters.size() > 0) {
            try {
                EntitySearchFilter[] attributes = new EntitySearchFilter[entityFilters.size()];
                List<String> userNames = this.getUserProfileManager().searchId(entityFilters.toArray(attributes));
                if (userNames.size() == 0 || !userNames.contains(user.getUsername())) {
                    return false;
                }
            } catch (ApsSystemException ex) {
                logger.error("error in filter users by profile attribute", ex);
                throw new RestServerError("error in filter users by profile attribute", ex);
            }
        }
        return true;
    }

    @Override
    public UserDto getUser(String username) {
        UserDetails user = this.loadUser(username);
        return dtoBuilder.convert(user);
    }

    @Override
    public UserDto updateUser(UserRequest userRequest) {
        UserDetails oldUser = this.loadUser(userRequest.getUsername());
        try {
            UserDetails newUser = this.updateUser(oldUser, userRequest);
            if (userRequest.isReset() && (oldUser instanceof User)) {
                ((User) newUser).setLastAccess(new Date());
                ((User) newUser).setLastPasswordChange(new Date());
            }
            this.getUserManager().updateUser(newUser);
            if (null != userRequest.getPassword()) {
                this.getUserManager().changePassword(userRequest.getUsername(), userRequest.getPassword());
            }
            UserDetails modifiedUser = this.getUserManager().getUser(userRequest.getUsername());
            return dtoBuilder.convert(modifiedUser);
        } catch (ApsSystemException e) {
            logger.error("Error in updating user {}", userRequest.getUsername(), e);
            throw new RestServerError("Error in updating user", e);
        }
    }

    @Override
    public UserDto addUser(UserRequest userRequest) {
        try {
            String username = userRequest.getUsername();
            if (null != this.getUserManager().getUser(username)) {
                BindingResult bindingResult = new BeanPropertyBindingResult(userRequest, "user");
                bindingResult.reject(UserValidator.ERRCODE_USER_ALREADY_EXISTS, new String[]{username}, "user.exists");
                throw new ValidationConflictException(bindingResult);
            }
            UserDetails newUser = this.createUser(userRequest);
            this.getUserManager().addUser(newUser);
            UserDetails addedUser = this.getUserManager().getUser(username);
            return dtoBuilder.convert(addedUser);
        } catch (ApsSystemException e) {
            logger.error("Error in adding user {}", userRequest.getUsername(), e);
            throw new RestServerError("Error in adding user", e);
        }
    }

    @Override
    public void removeUser(String username) {
        try {
            this.getUserManager().removeUser(username);
        } catch (ApsSystemException e) {
            logger.error("Error in deleting user {}", username, e);
            throw new RestServerError("Error in deleting user", e);
        }
    }

    @Override
    public UserDto updateUserPassword(UserPasswordRequest passwordRequest) {
        try {
            this.getUserManager().changePassword(passwordRequest.getUsername(), passwordRequest.getNewPassword());
            UserDetails user = this.loadUser(passwordRequest.getUsername());
            return dtoBuilder.convert(user);
        } catch (ApsSystemException e) {
            logger.error("Error in updating password for user {}", passwordRequest.getUsername(), e);
            throw new RestServerError("Error in updating password", e);
        }
    }


    @Override
    public List<UserPermissions> getCurrentUserPermissions(UserDetails user) {

        List<UserAuthorityDto> userAuthorities = this.getUserAuthorities(user.getUsername());

        return userAuthorities.stream()
                .map(userAuthorityDto -> {
                    RoleDto role = roleService.getRole(userAuthorityDto.getRole());
                    return new UserPermissions(userAuthorityDto.getGroup(), this.getPermissionList(role));
                })
                .collect(Collectors.toList());
    }


    private List<String> getPermissionList(RoleDto roleDto) {

        return roleDto.getPermissions().entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }



    private UserDetails loadUser(String username) {
        try {
            UserDetails user = this.getUserManager().getUser(username);
            if (user == null) {
                throw new ResourceNotFoundException(ERRCODE_USER_NOT_FOUND, "user", username);
            }
            return user;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (ApsSystemException e) {
            logger.error("Error in loading user {}", username, e);
            throw new RestServerError("Error in loading user", e);
        }
    }

    private UserDetails updateUser(UserDetails oldUser, UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword());
        user.setDisabled(userRequest.getStatus() != null && !userRequest.getStatus().equals(IUserService.STATUS_ACTIVE));
        if (oldUser instanceof User) {
            User userToClone = (User) oldUser;
            user.setLastAccess(userToClone.getLastAccess());
            user.setLastPasswordChange(userToClone.getLastPasswordChange());
        }
        return user;
    }

    private UserDetails createUser(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword());
        user.setDisabled(userRequest.getStatus() != null && !userRequest.getStatus().equals(IUserService.STATUS_ACTIVE));
        return user;
    }
}
