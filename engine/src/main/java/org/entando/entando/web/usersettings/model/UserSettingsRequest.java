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
package org.entando.entando.web.usersettings.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.entando.entando.aps.system.services.usersettings.model.UserSettingsDto;

public class UserSettingsRequest {

    @JsonProperty("restrictionsActive")
    @NotNull(message = "userSettings.ExtendedPrivacyModuleEnabled.required")
    private Boolean extendedPrivacyModuleEnabled;

    @JsonProperty("enableGravatarIntegration")
    @NotNull(message = "userSettings.GravatarIntegrationEnabled.required")
    private Boolean gravatarIntegrationEnabled;

    @JsonProperty("lastAccessPasswordExpirationMonths")
    @NotNull(message = "userSettings.MaxMonthsSinceLastAccess.required")
    @Min(value = 0, message = "userSettings.MaxMonthsSinceLastAccess.invalid")
    private Integer maxMonthsSinceLastAccess;

    @JsonProperty("maxMonthsPasswordValid")
    @NotNull(message = "userSettings.maxMonthsSinceLastPasswordChange.required")
    @Min(value = 0, message = "userSettings.MaxMonthsSinceLastPasswordChange.invalid")
    private Integer maxMonthsSinceLastPasswordChange;

    private Boolean passwordAlwaysActive = false;

    public Boolean isExtendedPrivacyModuleEnabled() {
        return extendedPrivacyModuleEnabled;
    }

    public void setExtendedPrivacyModuleEnabled(Boolean extendedPrivacyModuleEnabled) {
        this.extendedPrivacyModuleEnabled = extendedPrivacyModuleEnabled;
    }

    public Boolean isGravatarIntegrationEnabled() {
        return gravatarIntegrationEnabled;
    }

    public void setGravatarIntegrationEnabled(Boolean gravatarIntegrationEnabled) {
        this.gravatarIntegrationEnabled = gravatarIntegrationEnabled;
    }

    public Integer getMaxMonthsSinceLastAccess() {
        return maxMonthsSinceLastAccess;
    }

    public void setMaxMonthsSinceLastAccess(Integer maxMonthsSinceLastAccess) {
        this.maxMonthsSinceLastAccess = maxMonthsSinceLastAccess;
    }

    public Integer getMaxMonthsSinceLastPasswordChange() {
        return maxMonthsSinceLastPasswordChange;
    }

    public void setMaxMonthsSinceLastPasswordChange(Integer maxMonthsSinceLastPasswordChange) {
        this.maxMonthsSinceLastPasswordChange = maxMonthsSinceLastPasswordChange;
    }

    public Boolean getPasswordAlwaysActive() {
        return passwordAlwaysActive;
    }

    public void setPasswordAlwaysActive(Boolean passwordAlwaysActive) {
        this.passwordAlwaysActive = passwordAlwaysActive;
    }

    public Map<String, String> toMap() {
        Map<String, String> settings = new HashMap<>();
        settings.put(UserSettingsDto.EXTENDED_PRIVACY_MODULE_ENABLED, String.valueOf(this.isExtendedPrivacyModuleEnabled()));
        settings.put(UserSettingsDto.GRAVATAR_INTEGRATION_ENABLED, String.valueOf(this.isGravatarIntegrationEnabled()));
        if (this.getPasswordAlwaysActive()) {
            settings.put(UserSettingsDto.MAX_MONTHS_SINCE_LASTACCESS, "0");
            settings.put(UserSettingsDto.MAX_MONTHS_SINCE_LASTPASSWORDCHANGE, "0");
        } else {
            settings.put(UserSettingsDto.MAX_MONTHS_SINCE_LASTACCESS, String.valueOf(this.getMaxMonthsSinceLastAccess()));
            settings.put(UserSettingsDto.MAX_MONTHS_SINCE_LASTPASSWORDCHANGE, String.valueOf(this.getMaxMonthsSinceLastPasswordChange()));
        }

        return settings;
    }
}
