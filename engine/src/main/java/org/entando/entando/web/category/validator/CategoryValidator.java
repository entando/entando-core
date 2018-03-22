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
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.category.model.CategoryDto;
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
    public static final String ERRCODE_CATEGORY_VALIDATION = "2";
    public static final String ERRCODE_CATEGORY_ALREADY_EXISTS = "3";
    public static final String ERRCODE_URINAME_MISMATCH = "4";

    @Autowired
    private ICategoryManager categoryManager;

    @Override
    public boolean supports(Class<?> paramClass) {
        return CategoryDto.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CategoryDto request = (CategoryDto) target;
        this.validateBody(request, errors);
        /*
        if (errors.hasErrors()) {
            return;
        }
        if (null != this.getCategoryManager().getCategory(request.getCode())) {
            errors.reject(ERRCODE_CATEGORY_ALREADY_EXISTS, new String[]{request.getCode()}, "category.exists");
        }
        if (null == this.getCategoryManager().getCategory(request.getParentCode())) {
            errors.reject(ERRCODE_PARENT_CATEGORY_NOT_FOUND, new String[]{request.getCode()}, "category.parent.notexists");
        }
         */
    }

    public int validatePutReferences(String typeCode, CategoryDto request, Errors errors) {
        if (!StringUtils.equals(typeCode, request.getCode())) {
            errors.rejectValue("code", ERRCODE_URINAME_MISMATCH, new String[]{typeCode, request.getCode()}, "entityType.code.mismatch");
            return 400;
        }
        if (null == this.getCategoryManager().getCategory(request.getCode())) {
            errors.reject(ERRCODE_CATEGORY_NOT_FOUND, new String[]{request.getCode()}, "category.notexists");
            return 404;
        }
        if (null == this.getCategoryManager().getCategory(request.getParentCode())) {
            errors.reject(ERRCODE_PARENT_CATEGORY_NOT_FOUND, new String[]{request.getCode()}, "category.parent.notexists");
            return 404;
        }
        return 0;
    }

    public int validatePostReferences(CategoryDto request, Errors errors) {
        if (null != this.getCategoryManager().getCategory(request.getCode())) {
            errors.reject(ERRCODE_CATEGORY_ALREADY_EXISTS, new String[]{request.getCode()}, "category.exists");
            return 400;
        }
        if (null == this.getCategoryManager().getCategory(request.getParentCode())) {
            errors.reject(ERRCODE_PARENT_CATEGORY_NOT_FOUND, new String[]{request.getCode()}, "category.parent.notexists");
            return 404;
        }
        return 0;
    }

    public int validateBody(CategoryDto request, Errors errors) {
        if (StringUtils.isBlank(request.getCode())) {
            errors.reject(ERRCODE_CATEGORY_VALIDATION, new String[]{}, "category.code.notBlank");
        }
        if (StringUtils.isBlank(request.getParentCode())) {
            errors.reject(ERRCODE_CATEGORY_VALIDATION, new String[]{}, "category.description.notBlank");
        }
        if (null == request.getTitles() || request.getTitles().isEmpty()) {
            errors.reject(ERRCODE_CATEGORY_VALIDATION, new String[]{}, "category.titles.notBlank");
        }
        return (errors.hasErrors()) ? 400 : 0;
    }

    public ICategoryManager getCategoryManager() {
        return categoryManager;
    }

    public void setCategoryManager(ICategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

}
