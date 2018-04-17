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
package org.entando.entando.web.userprofile.validator;

import com.agiletec.aps.system.common.entity.IEntityManager;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.web.entity.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author E.Santoboni
 */
@Component
public class ProfileValidator extends EntityValidator {

    @Autowired
    private IUserProfileManager userProfileManager;

    public boolean existProfile(String username) {
        return super.existEntity(username);
    }

    @Override
    protected IEntityManager getEntityManager() {
        return this.userProfileManager;
    }

}
