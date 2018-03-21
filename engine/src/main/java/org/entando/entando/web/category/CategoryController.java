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

import com.agiletec.aps.system.services.role.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.entando.entando.aps.system.services.category.ICategoryService;
import org.entando.entando.aps.system.services.category.model.CategoryDto;
import org.entando.entando.web.category.validator.CategoryValidator;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    /*
    public static final String ERRCODE_PAGE_ALREADY_EXISTS = "1";
    public static final String ERRCODE_URINAME_MISMATCH = "2";
    public static final String ERRCODE_ONLINE_PAGE = "1";
    public static final String ERRCODE_PAGE_HAS_CHILDREN = "2";
    public static final String ERRCODE_GROUP_MISMATCH = "2";
    public static final String ERRCODE_STATUS_PAGE_MISMATCH = "6";
    public static final String ERRCODE_CHANGE_POSITION_INVALID_REQUEST = "7";
    public static final String ERRCODE_REFERENCED_ONLINE_PAGE = "1";
    public static final String ERRCODE_REFERENCED_DRAFT_PAGE = "2";
     */
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
    /*
    @RestAccessControl(permission = Permission.MANAGE_PAGES)
    @RequestMapping(value = "/pages/{pageCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPage(@ModelAttribute("user") UserDetails user, @PathVariable String pageCode, @RequestParam(value = "status", required = false, defaultValue = IPageService.STATUS_DRAFT) String status) {
        logger.debug("getting page {}", pageCode);
        if (!this.getAuthorizationService().isAuth(user, pageCode)) {
            return new ResponseEntity<>(new RestResponse(new PageDto()), HttpStatus.UNAUTHORIZED);
        }
        PageDto page = this.getPageService().getPage(pageCode, status);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("status", status);
        return new ResponseEntity<>(new RestResponse(page, new ArrayList<>(), metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.MANAGE_PAGES)
    @RequestMapping(value = "/pages/{pageCode}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePage(@ModelAttribute("user") UserDetails user, @PathVariable String pageCode, @Valid @RequestBody PageRequest pageRequest, BindingResult bindingResult) {
        logger.debug("updating page {} with request {}", pageCode, pageRequest);

        if (!this.getAuthorizationService().isAuth(user, pageCode)) {
            return new ResponseEntity<>(new RestResponse(new PageDto()), HttpStatus.UNAUTHORIZED);
        }
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getPageValidator().validateBodyCode(pageCode, pageRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        PageDto page = this.getPageService().updatePage(pageCode, pageRequest);
        Map<String, String> metadata = new HashMap<>();
        return new ResponseEntity<>(new RestResponse(page, new ArrayList<>(), metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/pages/{pageCode}/status", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePageStatus(@ModelAttribute("user") UserDetails user, @PathVariable String pageCode, @Valid @RequestBody PageStatusRequest pageStatusRequest, BindingResult bindingResult) {
        logger.debug("changing status for page {} with request {}", pageCode, pageStatusRequest);
        if (!this.getAuthorizationService().isAuth(user, pageCode)) {
            return new ResponseEntity<>(new RestResponse(new PageDto()), HttpStatus.UNAUTHORIZED);
        }
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getPageValidator().validateReferences(pageCode, pageStatusRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        PageDto page = this.getPageService().updatePageStatus(pageCode, pageStatusRequest.getStatus());
        Map<String, String> metadata = new HashMap<>();
        metadata.put("status", pageStatusRequest.getStatus());
        return new ResponseEntity<>(new RestResponse(page, new ArrayList<>(), metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.MANAGE_PAGES)
    @RequestMapping(value = "/pages", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addPage(@ModelAttribute("user") UserDetails user, @Valid @RequestBody PageRequest pageRequest, BindingResult bindingResult) throws ApsSystemException {
        logger.debug("creating page with request {}", pageRequest);
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations
        getPageValidator().validate(pageRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationConflictException(bindingResult);
        }
        PageDto dto = this.getPageService().addPage(pageRequest);
        Map<String, String> metadata = new HashMap<>();
        return new ResponseEntity<>(new RestResponse(dto, new ArrayList<>(), metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.MANAGE_PAGES)
    @RequestMapping(value = "/pages/{pageCode}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePage(@ModelAttribute("user") UserDetails user, @PathVariable String pageCode) throws ApsSystemException {
        logger.info("deleting {}", pageCode);
        if (!this.getAuthorizationService().isAuth(user, pageCode)) {
            return new ResponseEntity<>(new RestResponse(new PageDto()), HttpStatus.UNAUTHORIZED);
        }
        DataBinder binder = new DataBinder(pageCode);
        BindingResult bindingResult = binder.getBindingResult();
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations
        getPageValidator().validateOnlinePage(pageCode, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations
        getPageValidator().validateChildren(pageCode, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getPageService().removePage(pageCode);
        Map<String, String> payload = new HashMap<>();
        payload.put("code", pageCode);
        Map<String, String> metadata = new HashMap<>();
        return new ResponseEntity<>(new RestResponse(payload, new ArrayList<>(), metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.MANAGE_PAGES)
    @RequestMapping(value = "/pages/{pageCode}/position", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> movePage(@ModelAttribute("user") UserDetails user, @PathVariable String pageCode, @Valid @RequestBody PageRequest pageRequest, BindingResult bindingResult) {
        logger.debug("changing position for page {} with request {}", pageCode, pageRequest);
        if (!this.getAuthorizationService().isAuth(user, pageCode)) {
            return new ResponseEntity<>(new RestResponse(new PageDto()), HttpStatus.UNAUTHORIZED);
        }
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getPageValidator().validateBodyCode(pageCode, pageRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getPageValidator().validateChangePositionRequest(pageCode, pageRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getPageValidator().validateGroups(pageCode, pageRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getPageValidator().validatePagesStatus(pageCode, pageRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        PageDto page = this.getPageService().movePage(pageCode, pageRequest);
        Map<String, String> metadata = new HashMap<>();
        return new ResponseEntity<>(new RestResponse(page, new ArrayList<>(), metadata), HttpStatus.OK);
    }
     */
}
