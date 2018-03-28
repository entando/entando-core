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

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.services.category.model.CategoryDto;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
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
    public static final String ERRCODE_PARENT_CATEGORY_CANNOT_BE_CHANGED = "3";
    public static final String ERRCODE_CATEGORY_REFERENCES = "1";
    public static final String ERRCODE_CATEGORY_NO_REFERENCES = "5";

    @Autowired
    private ICategoryManager categoryManager;

    @Override
    public boolean supports(Class<?> paramClass) {
        return CategoryDto.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
    }

    public void validatePutReferences(String categoryCode, CategoryDto request, BindingResult bindingResult) {
        if (!StringUtils.equals(categoryCode, request.getCode())) {
            bindingResult.rejectValue("code", ERRCODE_URINAME_MISMATCH, new String[]{categoryCode, request.getCode()}, "category.code.mismatch");
            throw new ValidationGenericException(bindingResult);
        }
        Category category = this.getCategoryManager().getCategory(request.getCode());
        if (null == category) {
            bindingResult.reject(ERRCODE_CATEGORY_NOT_FOUND, new String[]{request.getCode()}, "category.notexists");
            throw new RestRourceNotFoundException(bindingResult);
        }
        Category parent = this.getCategoryManager().getCategory(request.getParentCode());
        if (null == parent) {
            bindingResult.reject(ERRCODE_PARENT_CATEGORY_NOT_FOUND, new String[]{request.getCode()}, "category.parent.notexists");
            throw new RestRourceNotFoundException(bindingResult);
        } else if (!parent.getCode().equals(category.getParentCode())) {
            bindingResult.reject(ERRCODE_PARENT_CATEGORY_CANNOT_BE_CHANGED, new String[]{}, "category.parent.cannotBeChanged");
        }
    }

    public void validatePostReferences(CategoryDto request, BindingResult bindingResult) {
        if (null != this.getCategoryManager().getCategory(request.getCode())) {
            bindingResult.reject(ERRCODE_CATEGORY_ALREADY_EXISTS, new String[]{request.getCode()}, "category.exists");
            throw new ValidationGenericException(bindingResult);
        }
        if (null == this.getCategoryManager().getCategory(request.getParentCode())) {
            bindingResult.reject(ERRCODE_PARENT_CATEGORY_NOT_FOUND, new String[]{request.getCode()}, "category.parent.notexists");
            throw new RestRourceNotFoundException(bindingResult);
        }
    }

    public ICategoryManager getCategoryManager() {
        return categoryManager;
    }

    public void setCategoryManager(ICategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

}
