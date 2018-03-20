package org.entando.entando.aps.system.services.usersettings.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserSettingsDto {

    public static final String EXTENDED_PRIVACY_MODULE_ENABLED = "extendedPrivacyModuleEnabled";
    public static final String GRAVATAR_INTEGRATION_ENABLED = "gravatarIntegrationEnabled";
    public static final String MAX_MONTHS_SINCE_LASTACCESS = "maxMonthsSinceLastAccess";
    public static final String MAX_MONTHS_SINCE_LASTPASSWORDCHANGE = "maxMonthsSinceLastPasswordChange";

    @JsonProperty(value = "restrictionsActive")
    private boolean extendedPrivacyModuleEnabled;
    private boolean gravatarIntegrationEnabled; // extendedPrivacyModuleEnabled">false</Param>
    private int maxMonthsSinceLastAccess; // lastAccessPasswordExpirationMonths
    private int maxMonthsSinceLastPasswordChange; // maxMonthsSinceLastPasswordChange
    
    public boolean isExtendedPrivacyModuleEnabled() {
        return extendedPrivacyModuleEnabled;
    }

    public void setExtendedPrivacyModuleEnabled(boolean extendedPrivacyModuleEnabled) {
        this.extendedPrivacyModuleEnabled = extendedPrivacyModuleEnabled;
    }

    public boolean isGravatarIntegrationEnabled() {
        return gravatarIntegrationEnabled;
    }
    public void setGravatarIntegrationEnabled(boolean gravatarIntegrationEnabled) {
        this.gravatarIntegrationEnabled = gravatarIntegrationEnabled;
    }


    public int getMaxMonthsSinceLastAccess() {
        return maxMonthsSinceLastAccess;
    }
    public void setMaxMonthsSinceLastAccess(int maxMonthsSinceLastAccess) {
        this.maxMonthsSinceLastAccess = maxMonthsSinceLastAccess;
    }

    public int getMaxMonthsSinceLastPasswordChange() {
        return maxMonthsSinceLastPasswordChange;
    }

    public void setMaxMonthsSinceLastPasswordChange(int maxMonthsSinceLastPasswordChange) {
        this.maxMonthsSinceLastPasswordChange = maxMonthsSinceLastPasswordChange;
    }

    
    public Map<String, String> toMap() {
        Map<String, String> settings = new HashMap<>();
        settings.put(EXTENDED_PRIVACY_MODULE_ENABLED, String.valueOf(this.isExtendedPrivacyModuleEnabled()));
        settings.put(GRAVATAR_INTEGRATION_ENABLED, String.valueOf(this.isGravatarIntegrationEnabled()));
        settings.put(MAX_MONTHS_SINCE_LASTACCESS, String.valueOf(this.getMaxMonthsSinceLastPasswordChange()));
        settings.put(MAX_MONTHS_SINCE_LASTPASSWORDCHANGE, String.valueOf(this.getMaxMonthsSinceLastPasswordChange()));
        return settings;
    }


}
