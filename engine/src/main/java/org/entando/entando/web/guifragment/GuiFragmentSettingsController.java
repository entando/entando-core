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
package org.entando.entando.web.guifragment;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.role.Permission;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.guifragment.model.GuiFragmentSettingsBody;
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
@RequestMapping(value = "/fragmentsSettings")
public class GuiFragmentSettingsController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String RESULT_PARAM_NAME = "enableEditingWhenEmptyDefaultGui";

    @Autowired
    private ConfigInterface configManager;

    public ConfigInterface getConfigManager() {
        return configManager;
    }

    public void setConfigManager(ConfigInterface configManager) {
        this.configManager = configManager;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getGuiFragment(@PathVariable String fragmentCode) {
        String paramValue = this.getConfigManager().getParam(SystemConstants.CONFIG_PARAM_EDIT_EMPTY_FRAGMENT_ENABLED);
        Boolean value = null;
        try {
            value = Boolean.parseBoolean(paramValue);
        } catch (Exception e) {
            value = Boolean.FALSE;
        }
        Map<String, Boolean> result = new HashMap<>();
        result.put(RESULT_PARAM_NAME, value);
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> addGuiFragment(@Valid @RequestBody GuiFragmentSettingsBody bodyRequest, BindingResult bindingResult) throws ApsSystemException {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        Boolean value = bodyRequest.getEnableEditingWhenEmptyDefaultGui();
        this.getConfigManager().updateParam(SystemConstants.CONFIG_PARAM_EDIT_EMPTY_FRAGMENT_ENABLED, String.valueOf(value));
        Map<String, Boolean> result = new HashMap<>();
        result.put(RESULT_PARAM_NAME, value);
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

}
