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
package org.entando.entando.web.category;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.entando.entando.aps.system.services.category.ICategoryService;
import org.entando.entando.aps.system.services.category.model.CategoryDto;
import org.entando.entando.web.category.validator.CategoryValidator;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author E.Santoboni
 */
@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private CategoryValidator categoryValidator;

    protected ICategoryService getCategoryService() {
        return categoryService;
    }

    public void setCategoryService(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public CategoryValidator getCategoryValidator() {
        return categoryValidator;
    }

    public void setCategoryValidator(CategoryValidator categoryValidator) {
        this.categoryValidator = categoryValidator;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getCategories(@RequestParam(value = "parentCode", required = false, defaultValue = "home") String parentCode) {
        logger.debug("getting category tree for parent {}", parentCode);
        List<CategoryDto> result = this.getCategoryService().getTree(parentCode);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("parentCode", parentCode);
        return new ResponseEntity<>(new RestResponse(result, new ArrayList<>(), metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.MANAGE_PAGES)
    @RequestMapping(value = "/{categoryCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getCategory(@PathVariable String categoryCode) {
        logger.debug("getting category {}", categoryCode);
        CategoryDto category = this.getCategoryService().getCategory(categoryCode);
        return new ResponseEntity<>(new RestResponse(category, new ArrayList<>(), new HashMap<>()), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{categoryCode}/references/{holder}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getCategoryReferences(@PathVariable String categoryCode, @PathVariable String holder, RestListRequest requestList) {
        logger.debug("getting category references - {}", categoryCode);
        PagedMetadata<?> result = this.getCategoryService().getCategoryReferences(categoryCode, holder, requestList);
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> addCategory(@Valid @RequestBody CategoryDto categoryRequest, BindingResult bindingResult) throws ApsSystemException {
        //field validations
        this.getCategoryValidator().validate(categoryRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations
        this.getCategoryValidator().validatePostReferences(categoryRequest, bindingResult);
        CategoryDto category = this.getCategoryService().addCategory(categoryRequest);
        return new ResponseEntity<>(new RestResponse(category, new ArrayList<>(), new HashMap<>()), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{categoryCode}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> updateCategory(@PathVariable String categoryCode, @Valid @RequestBody CategoryDto categoryRequest, BindingResult bindingResult) {
        logger.debug("updating category {} with request {}", categoryCode, categoryRequest);
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getCategoryValidator().validatePutReferences(categoryCode, categoryRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        CategoryDto category = this.getCategoryService().updateCategory(categoryRequest);
        Map<String, String> metadata = new HashMap<>();
        return new ResponseEntity<>(new RestResponse(category, new ArrayList<>(), metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{categoryCode}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> deleteCategory(@PathVariable String categoryCode) throws ApsSystemException {
        logger.debug("Deleting category -> " + categoryCode);
        this.getCategoryService().deleteCategory(categoryCode);
        Map<String, String> payload = new HashMap<>();
        payload.put("code", categoryCode);
        return new ResponseEntity<>(new RestResponse(payload), HttpStatus.OK);
    }

}
