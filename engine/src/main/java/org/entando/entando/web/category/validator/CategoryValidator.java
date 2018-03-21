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
package org.entando.entando.web.category.validator;

import com.agiletec.aps.system.services.category.ICategoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author E.Santoboni
 */
@Component
public class CategoryValidator implements Validator {

    public static final String ERRCODE_PARENT_CATEGORY_NOT_FOUND = "1";
    public static final String ERRCODE_CATEGORY_NOT_FOUND = "1";

    @Autowired
    private ICategoryManager categoryManager;

    @Override
    public boolean supports(Class<?> paramClass) {
        return false;
        //return EntityTypeDtoRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //EntityTypeDtoRequest request = (EntityTypeDtoRequest) target;
        //this.validateBody(request, errors);
    }

    /*
    public int validateBodyName(String typeCode, EntityTypeDtoRequest request, Errors errors) {
        if (!StringUtils.equals(typeCode, request.getCode())) {
            errors.rejectValue("code", ERRCODE_URINAME_MISMATCH, new String[]{typeCode, request.getCode()}, "entityType.code.mismatch");
            return 400;
        }
        if (!this.existType(typeCode)) {
            errors.reject(ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST, new String[]{typeCode}, "entityType.notExists");
            return 404;
        }
        return this.validateBody(request, errors);
    }

    private int validateBody(EntityTypeDtoRequest request, Errors errors) {
        if (StringUtils.isBlank(request.getCode())) {
            errors.reject(ERRCODE_ENTITY_TYPE_CODE_REQUIRED, new String[]{}, "entityType.code.notBlank");
            return 400;
        }
        if (StringUtils.isBlank(request.getName())) {
            errors.reject(ERRCODE_ENTITY_TYPE_DESCR_REQUIRED, new String[]{}, "entityType.name.notBlank");
            return 400;
        }
        return 0;
    }

    public boolean existType(String typeCode) {
        return (null != this.getEntityManager().getEntityPrototype(typeCode));
    }

    protected abstract IEntityManager getEntityManager();
     */
    public ICategoryManager getCategoryManager() {
        return categoryManager;
    }

    public void setCategoryManager(ICategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

}
