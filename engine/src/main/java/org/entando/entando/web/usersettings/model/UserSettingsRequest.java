package org.entando.entando.web.usersettings.model;

import javax.validation.constraints.Min;

import org.entando.entando.aps.system.services.usersettings.model.UserSettingsDto;

public class UserSettingsRequest extends UserSettingsDto {

    @Override
    @Min(value = 0, message = "userSettings.MaxMonthsSinceLastAccess.invalid")
    public int getMaxMonthsSinceLastAccess() {
        return super.getMaxMonthsSinceLastAccess();
    }

    @Override
    @Min(value = 0, message = "userSettings.MaxMonthsSinceLastPasswordChange.invalid")
    public int getMaxMonthsSinceLastPasswordChange() {
        return super.getMaxMonthsSinceLastPasswordChange();
    }

    @Override

    public boolean isExtendedPrivacyModuleEnabled() {
        // TODO Auto-generated method stub
        return super.isExtendedPrivacyModuleEnabled();
    }

    @Override
    public boolean isGravatarIntegrationEnabled() {
        // TODO Auto-generated method stub
        return super.isGravatarIntegrationEnabled();
    }



}
