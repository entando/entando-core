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
package org.entando.entando.web.pagesettings;

import javax.validation.Valid;

import com.agiletec.aps.system.services.role.Permission;
import org.entando.entando.aps.system.services.pagesettings.IPageSettingsService;
import org.entando.entando.aps.system.services.pagesettings.model.PageSettingsDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.pagesettings.model.PageSettingsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author paddeo
 */
@RestController
public class PageSettingsController {

    @Autowired
    private IPageSettingsService pageSettingsService;

    public IPageSettingsService getPageSettingsService() {
        return pageSettingsService;
    }

    public void setPageSettingsService(IPageSettingsService pageSettingsService) {
        this.pageSettingsService = pageSettingsService;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/pageSettings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getPageSettings() {
        PageSettingsDto pageSettings = this.getPageSettingsService().getPageSettings();
        return new ResponseEntity<>(new RestResponse(pageSettings), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/pageSettings", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> updatePageSettings(@RequestBody PageSettingsRequest request) {
        //params validations
        if (request == null || request.isEmpty()) {
            DataBinder binder = new DataBinder(request);
            BindingResult bindingResult = binder.getBindingResult();
            bindingResult.reject("NotEmpty.pagesettings.params");
            throw new ValidationGenericException(bindingResult);
        }
        PageSettingsDto pageSettings = this.getPageSettingsService().updatePageSettings(request);
        return new ResponseEntity<>(new RestResponse(pageSettings), HttpStatus.OK);
    }

}
