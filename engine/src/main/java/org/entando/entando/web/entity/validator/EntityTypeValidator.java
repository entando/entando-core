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
package org.entando.entando.web.entity.validator;

import com.agiletec.aps.system.common.entity.IEntityManager;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.entity.model.EntityTypeFullDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

//@Component
public abstract class EntityTypeValidator implements Validator {

    public static final String ERRCODE_ENTITY_TYPE_ALREADY_EXISTS = "1";
    public static final String ERRCODE_URINAME_MISMATCH = "2";
    public static final String ERRCODE_ENTITY_TYPE_REFERENCES = "4";

    @Override
    public boolean supports(Class<?> paramClass) {
        return EntityTypeFullDto.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EntityTypeFullDto request = (EntityTypeFullDto) target;
        String typeCode = request.getCode();
        if (null != this.getEntityManager().getEntityPrototype(typeCode)) {
            errors.reject(ERRCODE_ENTITY_TYPE_ALREADY_EXISTS, new String[]{typeCode}, "entityType.exists");
        }
    }

    public void validateBodyName(String typeCode, EntityTypeFullDto request, Errors errors) {
        if (!StringUtils.equals(typeCode, request.getCode())) {
            errors.rejectValue("code", ERRCODE_URINAME_MISMATCH, new String[]{typeCode, request.getCode()}, "entityType.code.mismatch");
        }
    }

    protected abstract IEntityManager getEntityManager();

}
