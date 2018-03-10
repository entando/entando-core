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
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.entity.model.EntityTypeDtoRequest;
import org.entando.entando.web.entity.model.IEntityTypesBodyRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

//@Component
public abstract class EntityTypeValidator implements Validator {

    public static final String ERRCODE_ENTITY_TYPE_ALREADY_EXISTS = "1";
    public static final String ERRCODE_ENTITY_TYPE_NOT_EXISTS = "2";
    public static final String ERRCODE_URINAME_MISMATCH = "3";
    public static final String ERRCODE_ENTITY_TYPE_REFERENCES = "4";

    public static final String ERRCODE_ENTITY_TYPES_EMPTY = "5";

    @Override
    public boolean supports(Class<?> paramClass) {
        return EntityTypeDtoRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof IEntityTypesBodyRequest) {
            IEntityTypesBodyRequest request = (IEntityTypesBodyRequest) target;
            List<EntityTypeDtoRequest> types = request.getEntityTypes();
            if (null == types || types.isEmpty()) {
                errors.reject(ERRCODE_ENTITY_TYPES_EMPTY, "entityTypes.list.notBlank");
                return;
            }
            for (EntityTypeDtoRequest type : types) {
                this.validateEntityType(type, errors);
            }
        } else {
            EntityTypeDtoRequest request = (EntityTypeDtoRequest) target;
            this.validateEntityType(request, errors);
        }
    }

    private void validateEntityType(EntityTypeDtoRequest request, Errors errors) {
        String typeCode = request.getCode();
        if (null != this.getEntityManager().getEntityPrototype(typeCode)) {
            errors.reject(ERRCODE_ENTITY_TYPE_ALREADY_EXISTS, new String[]{typeCode}, "entityType.exists");
        }
    }

    public void validateBodyName(String typeCode, EntityTypeDtoRequest request, Errors errors) {
        if (!StringUtils.equals(typeCode, request.getCode())) {
            errors.rejectValue("code", ERRCODE_URINAME_MISMATCH, new String[]{typeCode, request.getCode()}, "entityType.code.mismatch");
        }
        if (null != this.getEntityManager().getEntityPrototype(typeCode)) {
            errors.reject(ERRCODE_ENTITY_TYPE_NOT_EXISTS, new String[]{typeCode}, "entityType.notExists");
        }
    }

    protected abstract IEntityManager getEntityManager();

}
