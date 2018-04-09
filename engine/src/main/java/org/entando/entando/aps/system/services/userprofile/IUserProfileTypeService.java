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

import org.entando.entando.aps.system.services.entity.model.AttributeTypeDto;
import org.entando.entando.aps.system.services.entity.model.EntityAttributeFullDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.aps.system.services.userprofile.model.UserProfileTypeDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.userprofile.model.ProfileTypeDtoRequest;
import org.springframework.validation.BindingResult;

/**
 * @author E.Santoboni
 */
public interface IUserProfileTypeService {

    public PagedMetadata<EntityTypeShortDto> getShortUserProfileTypes(RestListRequest requestList);

    public UserProfileTypeDto getUserProfileType(String profileTypeCode);

    public UserProfileTypeDto addUserProfileType(ProfileTypeDtoRequest bodyRequest, BindingResult bindingResult);

    public UserProfileTypeDto updateUserProfileType(ProfileTypeDtoRequest request, BindingResult bindingResult);

    public void deleteUserProfileType(String profileTypeCode);

    // ----------------------------------
    public PagedMetadata<String> getAttributeTypes(RestListRequest requestList);

    public AttributeTypeDto getAttributeType(String attributeCode);

    // ----------------------------------
    public EntityAttributeFullDto getUserProfileAttribute(String profileTypeCode, String attributeCode);

    public EntityAttributeFullDto addUserProfileAttribute(String profileTypeCode, EntityAttributeFullDto bodyRequest, BindingResult bindingResult);

    public EntityAttributeFullDto updateUserProfileAttribute(String profileTypeCode, EntityAttributeFullDto bodyRequest, BindingResult bindingResult);

    public void deleteUserProfileAttribute(String profileTypeCode, String attributeCode);

    public void reloadProfileTypeReferences(String profileTypeCode);

}
