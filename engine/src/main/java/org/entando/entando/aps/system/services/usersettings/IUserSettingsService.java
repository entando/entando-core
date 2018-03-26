package org.entando.entando.aps.system.services.usersettings;

import org.entando.entando.aps.system.services.usersettings.model.UserSettingsDto;
import org.entando.entando.web.usersettings.model.UserSettingsRequest;

public interface IUserSettingsService {

    UserSettingsDto getUserSettings();

    UserSettingsDto updateUserSettings(UserSettingsRequest request);

}
