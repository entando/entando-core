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
package org.entando.entando.aps.system.services.category;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.CategoryUtilizer;
import com.agiletec.aps.system.services.category.ICategoryManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.category.model.CategoryDto;
import org.entando.entando.web.category.validator.CategoryValidator;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

/**
 * @author E.Santoboni
 */
public class CategoryService implements ICategoryService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ICategoryManager categoryManager;
    @Autowired
    private List<CategoryUtilizer> categoryUtilizers;

    protected IDtoBuilder<Category, CategoryDto> getDtoBuilder() {
        return new DtoBuilder<Category, CategoryDto>() {
            @Override
            protected CategoryDto toDto(Category src) {
                return new CategoryDto(src);
            }
        };
    }

    @Override
    public List<CategoryDto> getTree(String parentCode) {
        List<CategoryDto> res = new ArrayList<>();
        Category parent = this.getCategoryManager().getCategory(parentCode);
        if (null == parent) {
            throw new RestRourceNotFoundException(CategoryValidator.ERRCODE_PARENT_CATEGORY_NOT_FOUND, "category", parentCode);
        }
        Optional.ofNullable(parent.getChildrenCodes()).ifPresent(children -> Arrays.asList(children).forEach(childCode -> {
            Category child = this.getCategoryManager().getCategory(childCode);
            CategoryDto childDto = this.getDtoBuilder().convert(child);
            childDto.setChildren(Arrays.asList(child.getChildrenCodes()));
            res.add(childDto);
        }));
        return res;
    }

    @Override
    public CategoryDto getCategory(String categoryCode) {
        Category category = this.getCategoryManager().getCategory(categoryCode);
        if (null == category) {
            throw new RestRourceNotFoundException(CategoryValidator.ERRCODE_CATEGORY_NOT_FOUND, "category", categoryCode);
        }
        CategoryDto dto = null;
        try {
            dto = this.getDtoBuilder().convert(category);
            for (CategoryUtilizer categoryUtilizer : this.getCategoryUtilizers()) {
                Map.Entry<String, List> entry = categoryUtilizer.getCategoryUtilizersForApi(categoryCode);
                dto.getReferences().put(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            logger.error("error extracting category " + categoryCode, e);
            throw new RestServerError("error extracting category " + categoryCode, e);
        }
        return dto;
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category parentCategory = this.getCategoryManager().getCategory(categoryDto.getParentCode());
        if (null == parentCategory) {
            throw new RestRourceNotFoundException(CategoryValidator.ERRCODE_PARENT_CATEGORY_NOT_FOUND, "category", categoryDto.getParentCode());
        }
        Category category = this.getCategoryManager().getCategory(categoryDto.getCode());
        if (null != category) {
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(category, "category");
            bindingResult.reject(CategoryValidator.ERRCODE_CATEGORY_ALREADY_EXISTS, new String[]{category.getCode()}, "category.exists");
            throw new ValidationGenericException(bindingResult);
        }
        CategoryDto dto = null;
        try {
            Category categoryToAdd = new Category();
            categoryToAdd.setCode(categoryDto.getCode());
            categoryToAdd.setParentCode(categoryDto.getParentCode());
            categoryToAdd.getTitles().putAll(categoryDto.getTitles());
            this.getCategoryManager().addCategory(categoryToAdd);
            dto = this.getDtoBuilder().convert(this.getCategoryManager().getCategory(categoryDto.getCode()));
        } catch (Exception e) {
            logger.error("error adding category " + categoryDto.getCode(), e);
            throw new RestServerError("error adding category " + categoryDto.getCode(), e);
        }
        return dto;
    }

    protected ICategoryManager getCategoryManager() {
        return categoryManager;
    }

    public void setCategoryManager(ICategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

    protected List<CategoryUtilizer> getCategoryUtilizers() {
        return categoryUtilizers;
    }

    public void setCategoryUtilizers(List<CategoryUtilizer> categoryUtilizers) {
        this.categoryUtilizers = categoryUtilizers;
    }

}
