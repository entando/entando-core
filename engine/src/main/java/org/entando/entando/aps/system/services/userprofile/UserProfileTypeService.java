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
import java.util.List;
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
import org.entando.entando.web.userprofile.model.ProfileTypesBodyRequest;
import org.springframework.validation.BindingResult;

/**
 * @author E.Santoboni
 */
public class UserProfileTypeService extends AbstractEntityService<IUserProfile, UserProfileTypeDto> implements IUserProfileTypeService {

    @Override
    public PagedMetadata<EntityTypeShortDto> getShortDataTypes(RestListRequest requestList) {
        return super.getShortEntityTypes(SystemConstants.USER_PROFILE_MANAGER, requestList);
    }

    @Override
    protected IDtoBuilder<IUserProfile, UserProfileTypeDto> getEntityTypeFullDtoBuilder(IEntityManager masterManager) {
        return new UserProfileTypeDtoBuilder(masterManager.getAttributeRoles());
    }

    @Override
    public UserProfileTypeDto getDataType(String dataTypeCode) {
        return super.getFullEntityType(SystemConstants.USER_PROFILE_MANAGER, dataTypeCode);
    }

    @Override
    public List<UserProfileTypeDto> addDataTypes(ProfileTypesBodyRequest bodyRequest, BindingResult bindingResult) {
        return super.addEntityTypes(SystemConstants.USER_PROFILE_MANAGER, bodyRequest, bindingResult);
    }

    @Override
    public UserProfileTypeDto updateDataType(ProfileTypeDtoRequest request, BindingResult bindingResult) {
        return super.updateEntityType(SystemConstants.USER_PROFILE_MANAGER, request, bindingResult);
    }

    @Override
    protected IUserProfile createEntityType(IEntityManager entityManager, EntityTypeDtoRequest dto, BindingResult bindingResult) throws Throwable {
        IUserProfile userProfile = super.createEntityType(entityManager, dto, bindingResult);
        /*
        ProfileTypeDtoRequest dtr = (ProfileTypeDtoRequest) dto;
        userProfile.setDefaultModel(dtr.getDefaultModel());
        userProfile.setListModel(dtr.getListModel());
        userProfile.setViewPage(dtr.getViewPage());
         */
        return userProfile;
    }

    @Override
    public void deleteDataType(String entityTypeCode) {
        super.deleteEntityType(SystemConstants.USER_PROFILE_MANAGER, entityTypeCode);
    }

}
