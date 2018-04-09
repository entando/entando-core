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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.UserProfileTypeDto;
import org.entando.entando.web.entity.validator.EntityTypeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author E.Santoboni
 */
@Component
public class ProfileTypeValidator extends EntityTypeValidator {

    @Autowired
    private IUserProfileManager userProfileManager;

    @Override
    protected IEntityManager getEntityManager() {
        return this.userProfileManager;
    }

    @Override
    public boolean isValidField(String fieldName) {
        List<String> fields = new ArrayList<>();
        fields = getAllFields(fields, UserProfileTypeDto.class);
        return fields.contains(fieldName);
    }

    List<String> getAllFields(List<String> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()).stream()
                .map(field -> field.getName()).collect(Collectors.toList()));
        if (type.getSuperclass() != null) {
            return getAllFields(fields, type.getSuperclass());
        }
        return fields;
    }
}
