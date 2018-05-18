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
import java.util.Map;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.entity.AbstractEntityTypeService;
import org.entando.entando.aps.system.services.entity.model.AttributeTypeDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeAttributeFullDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypesStatusDto;
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
public class UserProfileTypeService extends AbstractEntityTypeService<IUserProfile, UserProfileTypeDto> implements IUserProfileTypeService {

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

    @Override
    public PagedMetadata<String> getAttributeTypes(RestListRequest requestList) {
        return super.getAttributeTypes(SystemConstants.USER_PROFILE_MANAGER, requestList);
    }

    @Override
    public AttributeTypeDto getAttributeType(String attributeCode) {
        return super.getAttributeType(SystemConstants.USER_PROFILE_MANAGER, attributeCode);
    }

    @Override
    public EntityTypeAttributeFullDto getUserProfileAttribute(String profileTypeCode, String attributeCode) {
        return super.getEntityAttribute(SystemConstants.USER_PROFILE_MANAGER, profileTypeCode, attributeCode);
    }

    @Override
    public EntityTypeAttributeFullDto addUserProfileAttribute(String profileTypeCode, EntityTypeAttributeFullDto bodyRequest, BindingResult bindingResult) {
        return super.addEntityAttribute(SystemConstants.USER_PROFILE_MANAGER, profileTypeCode, bodyRequest, bindingResult);
    }

    @Override
    public EntityTypeAttributeFullDto updateUserProfileAttribute(String profileTypeCode, EntityTypeAttributeFullDto bodyRequest, BindingResult bindingResult) {
        return super.updateEntityAttribute(SystemConstants.USER_PROFILE_MANAGER, profileTypeCode, bodyRequest, bindingResult);
    }

    @Override
    public void deleteUserProfileAttribute(String profileTypeCode, String attributeCode) {
        super.deleteEntityAttribute(SystemConstants.USER_PROFILE_MANAGER, profileTypeCode, attributeCode);
    }

    @Override
    public void moveUserProfileAttribute(String profileTypeCode, String attributeCode, boolean moveUp) {
        super.moveEntityAttribute(SystemConstants.USER_PROFILE_MANAGER, profileTypeCode, attributeCode, moveUp);
    }

    @Override
    public void reloadProfileTypeReferences(String profileTypeCode) {
        super.reloadEntityTypeReferences(SystemConstants.USER_PROFILE_MANAGER, profileTypeCode);
    }

    @Override
    public Map<String, Integer> reloadProfileTypesReferences(List<String> profileTypeCodes) {
        return super.reloadEntityTypesReferences(SystemConstants.USER_PROFILE_MANAGER, profileTypeCodes);
    }

    @Override
    public EntityTypesStatusDto getProfileTypesRefreshStatus() {
        return super.getEntityTypesRefreshStatus(SystemConstants.USER_PROFILE_MANAGER);
    }

}
