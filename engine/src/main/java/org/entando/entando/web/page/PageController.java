/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.web.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.databind.JsonNode;
import org.entando.entando.aps.system.services.page.IPageService;
import org.entando.entando.aps.system.services.page.PageAuthorizationService;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.web.common.annotation.ActivityStreamAuditable;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ResourcePermissionsException;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.entando.entando.web.component.ComponentUsage;
import org.entando.entando.web.component.ComponentUsageEntity;
import org.entando.entando.web.page.model.PagePositionRequest;
import org.entando.entando.web.page.model.PageRequest;
import org.entando.entando.web.page.model.PageSearchRequest;
import org.entando.entando.web.page.model.PageStatusRequest;
import org.entando.entando.web.page.validator.PageValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author paddeo
 */
@RestController
@SessionAttributes("user")
public class PageController {
    public static final String COMPONENT_ID = "page";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IPageService pageService;

    @Autowired
    private PageDtoToRequestConverter pageDtoToRequestConverter;

    @Autowired
    private PageValidator pageValidator;

    @Autowired
    private PageAuthorizationService authorizationService;

    public IPageService getPageService() {
        return pageService;
    }

    public void setPageService(IPageService pageService) {
        this.pageService = pageService;
    }

    public PageValidator getPageValidator() {
        return pageValidator;
    }

    public void setPageValidator(PageValidator pageValidator) {
        this.pageValidator = pageValidator;
    }

    public PageAuthorizationService getAuthorizationService() {
        return authorizationService;
    }

    public void setAuthorizationService(PageAuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @RestAccessControl(permission = Permission.MANAGE_PAGES)
    @RequestMapping(value = "/pages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<List<PageDto>, Map<String, String>>> getPages(@ModelAttribute("user") UserDetails user, @RequestParam(value = "parentCode", required = false, defaultValue = "homepage") String parentCode) {
        logger.debug("getting page tree for parent {}", parentCode);
        List<PageDto> result = this.getAuthorizationService().filterList(user, this.getPageService().getPages(parentCode));
        Map<String, String> metadata = new HashMap<>();
        metadata.put("parentCode", parentCode);
        return new ResponseEntity<>(new RestResponse<>(result, metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.MANAGE_PAGES)
    @RequestMapping(value = "/pages/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<PageDto>> getPages(@ModelAttribute("user") UserDetails user, PageSearchRequest searchRequest) {
        logger.debug("getting page list with request {}", searchRequest);
        this.getPageValidator().validateRestListRequest(searchRequest, PageDto.class);
        List<String> groups = this.getAuthorizationService().getAllowedGroupCodes(user);
        PagedMetadata<PageDto> result = this.getPageService().searchPages(searchRequest, groups);
        return new ResponseEntity<>(new PagedRestResponse<>(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.MANAGE_PAGES)
    @RequestMapping(value = "/pages/search/group/free", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<PageDto>> getFreeOnlinePages(@ModelAttribute("user") UserDetails user, RestListRequest restListRequest) {
        logger.debug("getting free pages list with request {}", restListRequest);
        this.getPageValidator().validateRestListRequest(restListRequest, PageDto.class);
        PagedMetadata<PageDto> result = this.getPageService().searchOnlineFreePages(restListRequest);
        return new ResponseEntity<>(new PagedRestResponse<>(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.MANAGE_PAGES)
    @RequestMapping(value = "/pages/viewpages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<List<PageDto>>> listViewPages() {
        logger.debug("REST request - content type list view pages");

        return ResponseEntity.ok(
                new SimpleRestResponse<>(pageService.listViewPages()));
    }

    @RestAccessControl(permission = Permission.MANAGE_PAGES)
    @RequestMapping(value = "/pages/{pageCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<PageDto, Map<String, String>>> getPage(@ModelAttribute("user") UserDetails user, @PathVariable String pageCode, @RequestParam(value = "status", required = false, defaultValue = IPageService.STATUS_DRAFT) String status) {
        logger.debug("getting page {}", pageCode);
        Map<String, String> metadata = new HashMap<>();
        if (!this.getAuthorizationService().isAuth(user, pageCode)) {
            return new ResponseEntity<>(new RestResponse<>(new PageDto(), metadata), HttpStatus.UNAUTHORIZED);
        }
        PageDto page = this.getPageService().getPage(pageCode, status);
        metadata.put("status", status);
        return new ResponseEntity<>(new RestResponse<>(page, metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/pages/{pageCode}/usage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<ComponentUsage>> getComponentUsage(@PathVariable String pageCode) {
        logger.trace("get {} usage by code {}", COMPONENT_ID, pageCode);

        ComponentUsage usage = ComponentUsage.builder()
                .type(COMPONENT_ID)
                .code(pageCode)
                .usage(getPageService().getPageUsage(pageCode))
                .build();

        return new ResponseEntity<>(new SimpleRestResponse<>(usage), HttpStatus.OK);
    }


    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/pages/{pageCode}/usage/details", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<ComponentUsageEntity>> getComponentUsageDetails(@PathVariable String pageCode, PageSearchRequest searchRequest) {

        logger.trace("get {} usage details by code {}", COMPONENT_ID, pageCode);

        PagedMetadata<ComponentUsageEntity> result = pageService.getPageUsageDetails(pageCode, searchRequest);

        return new ResponseEntity<>(new PagedRestResponse<>(result), HttpStatus.OK);
    }


    @ActivityStreamAuditable
    @RestAccessControl(permission = Permission.MANAGE_PAGES)
    @RequestMapping(value = "/pages/{pageCode}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<PageDto, Map<String, String>>> updatePage(@ModelAttribute("user") UserDetails user, @PathVariable String pageCode, @Valid @RequestBody PageRequest pageRequest, BindingResult bindingResult) {
        logger.debug("updating page {} with request {}", pageCode, pageRequest);

        if (!this.getAuthorizationService().isAuth(user, pageCode)) {
            throw new ResourcePermissionsException(bindingResult, user.getUsername(), pageCode);
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
        return new ResponseEntity<>(new RestResponse<>(page, metadata), HttpStatus.OK);
    }

    @ActivityStreamAuditable
    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/pages/{pageCode}/status", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<PageDto, Map<String, String>>> updatePageStatus(@ModelAttribute("user") UserDetails user, @PathVariable String pageCode, @Valid @RequestBody PageStatusRequest pageStatusRequest, BindingResult bindingResult) {
        logger.debug("changing status for page {} with request {}", pageCode, pageStatusRequest);
        Map<String, String> metadata = new HashMap<>();
        if (!this.getAuthorizationService().isAuth(user, pageCode)) {
            return new ResponseEntity<>(new RestResponse<>(new PageDto(), metadata), HttpStatus.UNAUTHORIZED);
        }
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        PageDto page = this.getPageService().updatePageStatus(pageCode, pageStatusRequest.getStatus());
        metadata.put("status", pageStatusRequest.getStatus());
        return new ResponseEntity<>(new RestResponse<>(page, metadata), HttpStatus.OK);
    }

    @ActivityStreamAuditable
    @RestAccessControl(permission = Permission.MANAGE_PAGES)
    @RequestMapping(value = "/pages", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<PageDto>> addPage(@ModelAttribute("user") UserDetails user, @Valid @RequestBody PageRequest pageRequest, BindingResult bindingResult) throws ApsSystemException {
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
        return new ResponseEntity<>(new SimpleRestResponse<>(dto), HttpStatus.OK);
    }

    @ActivityStreamAuditable
    @RestAccessControl(permission = Permission.MANAGE_PAGES)
    @RequestMapping(value = "/pages/{pageCode}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<?>> deletePage(@ModelAttribute("user") UserDetails user, @PathVariable String pageCode) throws ApsSystemException {
        logger.debug("deleting {}", pageCode);
        if (!this.getAuthorizationService().isAuth(user, pageCode)) {
            return new ResponseEntity<>(new SimpleRestResponse<>(new PageDto()), HttpStatus.UNAUTHORIZED);
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
        return new ResponseEntity<>(new SimpleRestResponse<>(payload), HttpStatus.OK);
    }

    @ActivityStreamAuditable
    @RestAccessControl(permission = Permission.MANAGE_PAGES)
    @RequestMapping(value = "/pages/{pageCode}/position", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<PageDto>> movePage(@ModelAttribute("user") UserDetails user, @PathVariable String pageCode, @Valid @RequestBody PagePositionRequest pageRequest, BindingResult bindingResult) {
        logger.debug("changing position for page {} with request {}", pageCode, pageRequest);
        if (!this.getAuthorizationService().isAuth(user, pageCode)) {
            return new ResponseEntity<>(new SimpleRestResponse<>(new PageDto()), HttpStatus.UNAUTHORIZED);
        }
        //field validations
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
        return new ResponseEntity<>(new SimpleRestResponse<>(page), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/pages/{pageCode}/references/{manager}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<?>> getPageReferences(@PathVariable String pageCode, @PathVariable String manager, RestListRequest requestList) {
        logger.debug("loading references for page {} and manager {}", pageCode, manager);
        PagedMetadata<?> result = this.getPageService().getPageReferences(pageCode, manager, requestList);
        return new ResponseEntity<>(new PagedRestResponse<>(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.MANAGE_PAGES)
    @RequestMapping(value = "/pages/{pageCode}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = "application/json-patch+json")
    public ResponseEntity<RestResponse<PageDto, Map<String, String>>> patchPage(@ModelAttribute("user") UserDetails user, @PathVariable String pageCode, @RequestBody JsonNode patchRequest, BindingResult bindingResult) {
        logger.debug("update page {} with jsonpatch-request {}", pageCode, patchRequest);

        this.getPageValidator().validateJsonPatchRequest(patchRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        PageDto updatedPageDto = this.getPageService().getPatchedPage(pageCode, patchRequest);
        PageRequest pageRequest = this.pageDtoToRequestConverter.convert(updatedPageDto);

        return this.updatePage(user, pageCode, pageRequest, bindingResult);
    }


}
