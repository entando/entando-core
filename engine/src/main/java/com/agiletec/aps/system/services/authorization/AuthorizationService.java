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
package com.agiletec.aps.system.services.authorization;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.group.GroupServiceUtilizer;
import org.entando.entando.aps.system.services.user.model.UserDto;
import org.entando.entando.aps.system.services.user.model.UserDtoBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorizationService implements IAuthorizationService, GroupServiceUtilizer<UserDto> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private IAuthorizationManager authorizationManager;
    private IUserManager userManager;
    private IDtoBuilder<UserDetails, UserDto> dtoBuilder;

    protected IAuthorizationManager getAuthorizationManager() {
        return authorizationManager;
    }

    public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    protected IDtoBuilder<UserDetails, UserDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<UserDetails, UserDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    protected IUserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    @PostConstruct
    public void setUp() {
        setDtoBuilder(new UserDtoBuilder());
    }

    @Override
    public String getManagerName() {
        return ((IManager) this.getAuthorizationManager()).getName();
    }

    @Override
    public List<UserDto> getGroupUtilizer(String groupCode) {
        try {

            List<String> usernames = ((GroupUtilizer<String>) this.getAuthorizationManager()).getGroupUtilizers(groupCode);
            List<UserDto> dtoList = new ArrayList<>();
            if (null != usernames) {
                usernames.stream().forEach(i -> {
                    try {
                        dtoList.add(this.getDtoBuilder().convert(this.getUserManager().getUser(i)));
                    } catch (ApsSystemException e) {
                        logger.error("error loading {}", i, e);

                    }
                });
            }
            return dtoList;
        } catch (ApsSystemException ex) {
            logger.error("Error loading user references for group {}", groupCode, ex);
            throw new RestServerError("Error loading user references by group", ex);
        }
    }

    @Override
    public List<UserDto> getRoleUtilizer(String roleCode) {
        try {
            List<String> usernames = this.getAuthorizationManager().getUsersByRole(roleCode, false);
            List<UserDto> dtoList = new ArrayList<>();
            if (null != usernames) {
                usernames.stream().forEach(i -> {
                    try {
                        dtoList.add(this.getDtoBuilder().convert(this.getUserManager().getUser(i)));
                    } catch (ApsSystemException e) {
                        logger.error("error loading {}", i, e);

                    }
                });
            }
            return dtoList;
        } catch (ApsSystemException ex) {
            logger.error("Error loading user references for role {}", roleCode, ex);
            throw new RestServerError("Error loading user references by role", ex);
        }
    }

}
