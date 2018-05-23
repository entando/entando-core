/*
 * Copyright 2018-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.web.usersettings;

import javax.validation.Valid;

import com.agiletec.aps.system.services.role.Permission;
import org.entando.entando.aps.system.services.usersettings.IUserSettingsService;
import org.entando.entando.aps.system.services.usersettings.model.UserSettingsDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.usersettings.model.UserSettingsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/userSettings")
public class UserSettingsController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IUserSettingsService userSettingsService;

    public IUserSettingsService getUserSettingsService() {
        return userSettingsService;
    }

    public void setUserSettingsService(IUserSettingsService userSettingsService) {
        this.userSettingsService = userSettingsService;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getUserSettings() {
        logger.debug("loading user settings");
        UserSettingsDto userSettings = this.getUserSettingsService().getUserSettings();
        return new ResponseEntity<>(new RestResponse(userSettings), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> updateUserSettings(@Valid @RequestBody UserSettingsRequest request, BindingResult bindingResult) {
        logger.debug("updatinug user settings");
        //params validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        UserSettingsDto settings = this.getUserSettingsService().updateUserSettings(request);
        return new ResponseEntity<>(new RestResponse(settings), HttpStatus.OK);
    }

}
