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
package org.entando.entando.aps.system.services.userprofile;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.IEntityManager;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.entity.AbstractEntityService;
import org.entando.entando.aps.system.services.entity.model.EntityDto;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.springframework.validation.BindingResult;

/**
 * @author E.Santoboni
 */
public class UserProfileService extends AbstractEntityService<IUserProfile, EntityDto> implements IUserProfileService {

    @Override
    public EntityDto getUserProfile(String username) {
        return super.getEntity(SystemConstants.USER_PROFILE_MANAGER, username);
    }

    @Override
    public EntityDto addUserProfile(EntityDto request, BindingResult bindingResult) {
        return super.addEntity(SystemConstants.USER_PROFILE_MANAGER, request, bindingResult);
    }

    @Override
    protected IUserProfile addEntity(IEntityManager entityManager, IUserProfile entityToAdd) {
        try {
            ((IUserProfileManager) entityManager).addProfile(entityToAdd.getUsername(), entityToAdd);
            return entityToAdd;
        } catch (Exception e) {
            logger.error("Error adding profile", e);
            throw new RestServerError("error adding profile", e);
        }
    }

    @Override
    public EntityDto updateUserProfile(EntityDto request, BindingResult bindingResult) {
        return super.updateEntity(SystemConstants.USER_PROFILE_MANAGER, request, bindingResult);
    }

    @Override
    protected IUserProfile updateEntity(IEntityManager entityManager, IUserProfile entityToUpdate) {
        try {
            ((IUserProfileManager) entityManager).updateProfile(entityToUpdate.getUsername(), entityToUpdate);
            return entityToUpdate;
        } catch (Exception e) {
            logger.error("Error updating profile", e);
            throw new RestServerError("error updating profile", e);
        }
    }

    @Override
    protected EntityDto buildEntityDto(IUserProfile entity) {
        return new EntityDto(entity);
    }

}
