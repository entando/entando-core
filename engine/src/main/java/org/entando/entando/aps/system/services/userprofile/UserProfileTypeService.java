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
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.entity.AbstractEntityService;
import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.aps.system.services.userprofile.model.UserProfileTypeDto;
import org.entando.entando.aps.system.services.userprofile.model.UserProfileTypeDtoBuilder;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.entity.model.EntityTypeDtoRequest;
import org.entando.entando.web.userprofile.model.ProfileTypeDtoRequest;
import org.springframework.validation.BindingResult;

/**
 * @author E.Santoboni
 */
public class UserProfileTypeService extends AbstractEntityService<IUserProfile, UserProfileTypeDto> implements IUserProfileTypeService {

    @Override
    public PagedMetadata<EntityTypeShortDto> getShortUserProfileTypes(RestListRequest requestList) {
        return super.getShortEntityTypes(SystemConstants.USER_PROFILE_MANAGER, requestList);
    }

    @Override
    protected IDtoBuilder<IUserProfile, UserProfileTypeDto> getEntityTypeFullDtoBuilder(IEntityManager masterManager) {
        return new UserProfileTypeDtoBuilder(masterManager.getAttributeRoles());
    }

    @Override
    public UserProfileTypeDto getUserProfileType(String profileTypeCode) {
        return super.getFullEntityType(SystemConstants.USER_PROFILE_MANAGER, profileTypeCode);
    }

    @Override
    public UserProfileTypeDto addUserProfileType(ProfileTypeDtoRequest bodyRequest, BindingResult bindingResult) {
        return super.addEntityType(SystemConstants.USER_PROFILE_MANAGER, bodyRequest, bindingResult);
    }

    @Override
    public UserProfileTypeDto updateUserProfileType(ProfileTypeDtoRequest request, BindingResult bindingResult) {
        return super.updateEntityType(SystemConstants.USER_PROFILE_MANAGER, request, bindingResult);
    }

    @Override
    protected IUserProfile createEntityType(IEntityManager entityManager, EntityTypeDtoRequest dto, BindingResult bindingResult) throws Throwable {
        return super.createEntityType(entityManager, dto, bindingResult);
    }

    @Override
    public void deleteUserProfileType(String profileTypeCode) {
        super.deleteEntityType(SystemConstants.USER_PROFILE_MANAGER, profileTypeCode);
    }

}
