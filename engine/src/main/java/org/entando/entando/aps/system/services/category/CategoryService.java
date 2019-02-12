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

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.CategoryUtilizer;
import com.agiletec.aps.system.services.category.ICategoryManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.category.model.CategoryDto;
import org.entando.entando.web.category.validator.CategoryValidator;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
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
    @Autowired
    private List<CategoryServiceUtilizer> categoryServiceUtilizers;

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
            throw new ResourceNotFoundException(CategoryValidator.ERRCODE_PARENT_CATEGORY_NOT_FOUND, "category", parentCode);
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
            throw new ResourceNotFoundException(CategoryValidator.ERRCODE_CATEGORY_NOT_FOUND, "category", categoryCode);
        }
        CategoryDto dto = null;
        try {
            dto = this.getDtoBuilder().convert(category);
            for (CategoryUtilizer categoryUtilizer : this.getCategoryUtilizers()) {
                List references = categoryUtilizer.getCategoryUtilizers(categoryCode);
                dto.getReferences().put(((IManager) categoryUtilizer).getName(), (null != references && !references.isEmpty()));
            }
        } catch (Exception e) {
            logger.error("error extracting category " + categoryCode, e);
            throw new RestServerError("error extracting category " + categoryCode, e);
        }
        return dto;
    }

    @Override
    public PagedMetadata<?> getCategoryReferences(String categoryCode, String managerName, RestListRequest restListRequest) {
        Category group = this.getCategoryManager().getCategory(categoryCode);
        if (null == group) {
            logger.warn("no category found with code {}", categoryCode);
            throw new ResourceNotFoundException(CategoryValidator.ERRCODE_CATEGORY_NOT_FOUND, "category", categoryCode);
        }
        CategoryServiceUtilizer<?> utilizer = this.getCategoryServiceUtilizer(managerName);
        if (null == utilizer) {
            logger.warn("no references found for {}", managerName);
            throw new ResourceNotFoundException(CategoryValidator.ERRCODE_CATEGORY_NO_REFERENCES, "reference", managerName);
        }
        List<?> dtoList = utilizer.getCategoryUtilizer(categoryCode);
        List<?> subList = restListRequest.getSublist(dtoList);
        SearcherDaoPaginatedResult<?> pagedResult = new SearcherDaoPaginatedResult(dtoList.size(), subList);
        PagedMetadata<Object> pagedMetadata = new PagedMetadata<>(restListRequest, pagedResult);
        pagedMetadata.setBody((List<Object>) subList);
        return pagedMetadata;
    }

    private CategoryServiceUtilizer<?> getCategoryServiceUtilizer(String managerName) {
        List<CategoryServiceUtilizer> beans = this.getCategoryServiceUtilizers();
        Optional<CategoryServiceUtilizer> defName = beans.stream()
                .filter(service -> service.getManagerName().equals(managerName)).findFirst();
        if (defName.isPresent()) {
            return defName.get();
        }
        return null;
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category parentCategory = this.getCategoryManager().getCategory(categoryDto.getParentCode());
        if (null == parentCategory) {
            throw new ResourceNotFoundException(CategoryValidator.ERRCODE_PARENT_CATEGORY_NOT_FOUND, "parent category", categoryDto.getParentCode());
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

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category parentCategory = this.getCategoryManager().getCategory(categoryDto.getParentCode());
        if (null == parentCategory) {
            throw new ResourceNotFoundException(CategoryValidator.ERRCODE_PARENT_CATEGORY_NOT_FOUND, "parent category", categoryDto.getParentCode());
        }
        Category category = this.getCategoryManager().getCategory(categoryDto.getCode());
        if (null == category) {
            throw new ResourceNotFoundException(CategoryValidator.ERRCODE_CATEGORY_NOT_FOUND, "category", categoryDto.getCode());
        }
        CategoryDto dto = null;
        try {
            category.setParentCode(categoryDto.getParentCode());
            category.getTitles().clear();
            category.getTitles().putAll(categoryDto.getTitles());
            this.getCategoryManager().updateCategory(category);
            dto = this.getDtoBuilder().convert(this.getCategoryManager().getCategory(categoryDto.getCode()));
        } catch (Exception e) {
            logger.error("error updating category " + categoryDto.getCode(), e);
            throw new RestServerError("error updating category " + categoryDto.getCode(), e);
        }
        return dto;
    }

    @Override
    public void deleteCategory(String categoryCode) {
        Category category = this.getCategoryManager().getCategory(categoryCode);
        if (null == category) {
            return;
        }
        try {
            for (CategoryUtilizer categoryUtilizer : this.getCategoryUtilizers()) {
                List references = categoryUtilizer.getCategoryUtilizers(categoryCode);
                if (null != references && !references.isEmpty()) {
                    BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(category, "category");
                    bindingResult.reject(CategoryValidator.ERRCODE_CATEGORY_REFERENCES, new String[]{categoryCode}, "category.cannot.delete.references");
                    throw new ValidationGenericException(bindingResult);
                }
            }
            this.getCategoryManager().deleteCategory(categoryCode);
        } catch (ValidationGenericException e) {
            throw e;
        } catch (Exception e) {
            logger.error("error deleting category " + categoryCode, e);
            throw new RestServerError("error deleting category " + categoryCode, e);
        }
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

    public List<CategoryServiceUtilizer> getCategoryServiceUtilizers() {
        return categoryServiceUtilizers;
    }

    public void setCategoryServiceUtilizers(List<CategoryServiceUtilizer> categoryServiceUtilizers) {
        this.categoryServiceUtilizers = categoryServiceUtilizers;
    }

}
