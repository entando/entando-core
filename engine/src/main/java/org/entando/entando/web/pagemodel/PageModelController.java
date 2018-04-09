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
package org.entando.entando.web.pagemodel;

import javax.validation.Valid;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.Permission;
import java.util.HashMap;
import java.util.Map;
import org.entando.entando.aps.system.services.pagemodel.IPageModelService;
import org.entando.entando.aps.system.services.pagemodel.model.PageModelDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.pagemodel.model.PageModelRequest;
import org.entando.entando.web.pagemodel.validator.PageModelValidator;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/pagemodels")
public class PageModelController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IPageModelService pageModelService;

    @Autowired
    private PageModelValidator pageModelValidator;

    protected IPageModelService getPageModelService() {
        return pageModelService;
    }

    public void setPageModelService(IPageModelService pageModelService) {
        this.pageModelService = pageModelService;
    }

    protected PageModelValidator getPagemModelValidator() {
        return pageModelValidator;
    }

    public void setPageModelValidator(PageModelValidator pageModelValidator) {
        this.pageModelValidator = pageModelValidator;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getPageModels(RestListRequest requestList) {
        logger.trace("loading page models");
        this.getPagemModelValidator().validateRestListRequest(requestList, PageModelDto.class);
        PagedMetadata<PageModelDto> result = this.getPageModelService().getPageModels(requestList);
        this.getPagemModelValidator().validateRestListResult(requestList, result);
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getPageModel(@PathVariable String code) {
        PageModelDto pageModelDto = this.getPageModelService().getPageModel(code);
        return new ResponseEntity<>(new RestResponse(pageModelDto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{code}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, name = "roleGroup")
    public ResponseEntity<RestResponse> updatePageModel(@PathVariable String code, @Valid @RequestBody PageModelRequest pageModelRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getPagemModelValidator().validateBodyName(code, pageModelRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        PageModelDto pageModel = this.getPageModelService().updatePageModel(pageModelRequest);
        return new ResponseEntity<>(new RestResponse(pageModel), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> addPageModel(@Valid @RequestBody PageModelRequest pagemodelRequest, BindingResult bindingResult) throws ApsSystemException {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getPagemModelValidator().validate(pagemodelRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        PageModelDto dto = this.getPageModelService().addPageModel(pagemodelRequest);
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{code}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> deletePageModel(@PathVariable String code) throws ApsSystemException {
        logger.debug("deleting {}", code);
        this.getPageModelService().removePageModel(code);
        Map<String, String> result = new HashMap<>();
        result.put("code", code);
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

}
