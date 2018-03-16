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

import java.util.List;
import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.aps.system.services.userprofile.model.UserProfileTypeDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.userprofile.model.ProfileTypeDtoRequest;
import org.entando.entando.web.userprofile.model.ProfileTypesBodyRequest;
import org.springframework.validation.BindingResult;

/**
 * @author E.Santoboni
 */
public interface IUserProfileTypeService {

    public PagedMetadata<EntityTypeShortDto> getShortDataTypes(RestListRequest requestList);

    public UserProfileTypeDto getDataType(String profileTypeCode);

    public List<UserProfileTypeDto> addDataTypes(ProfileTypesBodyRequest bodyRequest, BindingResult bindingResult);

    public UserProfileTypeDto updateDataType(ProfileTypeDtoRequest request, BindingResult bindingResult);

    public void deleteDataType(String profileTypeCode);

}
