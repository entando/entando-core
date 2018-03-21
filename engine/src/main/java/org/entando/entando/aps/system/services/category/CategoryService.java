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
import com.agiletec.aps.system.services.category.ICategoryManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.category.model.CategoryDto;
import org.entando.entando.web.category.validator.CategoryValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class CategoryService implements ICategoryService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ICategoryManager categoryManager;

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

    public ICategoryManager getCategoryManager() {
        return categoryManager;
    }

    public void setCategoryManager(ICategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

}
