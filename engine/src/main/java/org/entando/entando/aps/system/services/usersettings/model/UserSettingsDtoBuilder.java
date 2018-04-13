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
package org.entando.entando.aps.system.services.usersettings.model;

import java.util.Map;

import com.agiletec.aps.system.SystemConstants;
import org.entando.entando.aps.system.services.DtoBuilder;

public class UserSettingsDtoBuilder extends DtoBuilder<Map<String, String>, UserSettingsDto> {
    
    @Override
    protected UserSettingsDto toDto(Map<String, String> sysParams) {
        UserSettingsDto dest = new UserSettingsDto();
        dest.setExtendedPrivacyModuleEnabled(this.extractBooleanValue(UserSettingsDto.EXTENDED_PRIVACY_MODULE_ENABLED, sysParams));
        dest.setGravatarIntegrationEnabled(this.extractBooleanValue(SystemConstants.CONFIG_PARAM_GRAVATAR_INTEGRATION_ENABLED, sysParams));
        
        dest.setMaxMonthsSinceLastAccess(this.extractIntValue(UserSettingsDto.MAX_MONTHS_SINCE_LASTACCESS, sysParams));
        dest.setMaxMonthsSinceLastPasswordChange(this.extractIntValue(UserSettingsDto.MAX_MONTHS_SINCE_LASTPASSWORDCHANGE, sysParams));
        if (dest.getMaxMonthsSinceLastAccess() == 0 && dest.getMaxMonthsSinceLastPasswordChange() == 0) {
            dest.setPasswordAlwaysActive(true);
        }
        return dest;
    }
    
    private boolean extractBooleanValue(String key, Map<String, String> sysParams) {
        String param = sysParams.getOrDefault(key, "false");
        return Boolean.parseBoolean(param);
    }
    
    private int extractIntValue(String key, Map<String, String> sysParams) {
        String param = sysParams.getOrDefault(key, "0");
        return new Integer(param);
    }
    
}
