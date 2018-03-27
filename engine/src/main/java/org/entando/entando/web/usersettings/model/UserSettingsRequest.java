package org.entando.entando.web.usersettings.model;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.entando.entando.aps.system.services.usersettings.model.UserSettingsDto;

public class UserSettingsRequest {

    @NotNull(message = "userSettings.ExtendedPrivacyModuleEnabled.required")
    private Boolean extendedPrivacyModuleEnabled;

    @NotNull(message = "userSettings.GravatarIntegrationEnabled.required")
    private Boolean gravatarIntegrationEnabled;

    @NotNull(message = "userSettings.MaxMonthsSinceLastAccess.required")
    @Min(value = 0, message = "userSettings.MaxMonthsSinceLastAccess.invalid")
    private Integer maxMonthsSinceLastAccess;

    @NotNull(message = "userSettings.maxMonthsSinceLastPasswordChange.required")
    @Min(value = 0, message = "userSettings.MaxMonthsSinceLastPasswordChange.invalid")
    private Integer maxMonthsSinceLastPasswordChange;

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


    public Map<String, String> toMap() {
        Map<String, String> settings = new HashMap<>();
        settings.put(UserSettingsDto.EXTENDED_PRIVACY_MODULE_ENABLED, String.valueOf(this.isExtendedPrivacyModuleEnabled()));
        settings.put(UserSettingsDto.GRAVATAR_INTEGRATION_ENABLED, String.valueOf(this.isGravatarIntegrationEnabled()));
        settings.put(UserSettingsDto.MAX_MONTHS_SINCE_LASTACCESS, String.valueOf(this.getMaxMonthsSinceLastAccess()));
        settings.put(UserSettingsDto.MAX_MONTHS_SINCE_LASTPASSWORDCHANGE, String.valueOf(this.getMaxMonthsSinceLastPasswordChange()));
        return settings;
    }
}
