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
package org.entando.entando.web.userprofile.model;

import java.util.List;
import org.entando.entando.aps.system.services.userprofile.model.UserProfileTypeDto;

/**
 * @author E.Santoboni
 */
public class ProfileTypesBodyResponse {

    private List<UserProfileTypeDto> userProfileTypes;

    public ProfileTypesBodyResponse() {
    }

    public ProfileTypesBodyResponse(List<UserProfileTypeDto> userProfileTypes) {
        this.setUserProfileTypes(userProfileTypes);
    }

    public List<UserProfileTypeDto> getUserProfileTypes() {
        return userProfileTypes;
    }

    public void setUserProfileTypes(List<UserProfileTypeDto> userProfileTypes) {
        this.userProfileTypes = userProfileTypes;
    }

}
