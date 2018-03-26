package org.entando.entando.aps.system.services.usersettings;

import java.util.Map;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.baseconfig.SystemParamsUtils;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.usersettings.model.UserSettingsDto;
import org.entando.entando.aps.system.services.usersettings.model.UserSettingsDtoBuilder;
import org.entando.entando.web.usersettings.model.UserSettingsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UserSettingsService implements IUserSettingsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ConfigInterface configManager;

    private IDtoBuilder<Map<String, String>, UserSettingsDto> dtoBuilder = new UserSettingsDtoBuilder();

    protected ConfigInterface getConfigManager() {
        return configManager;
    }

    public void setConfigManager(ConfigInterface configManager) {
        this.configManager = configManager;
    }

    public IDtoBuilder<Map<String, String>, UserSettingsDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<Map<String, String>, UserSettingsDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }


    @Override
    public UserSettingsDto getUserSettings() {
        try {
            Map<String, String> systemParams = this.getSystemParams();
            return this.getDtoBuilder().convert(systemParams);
        } catch (Throwable e) {
            logger.error("Error getting user settings", e);
            throw new RestServerError("Error getting user settings", e);
        }
    }

    @Override
    public UserSettingsDto updateUserSettings(UserSettingsRequest request) {
        try {

            Map<String, String> params = request.toMap();
            Map<String, String> systemParams = this.getSystemParams();
            systemParams.putAll(params);
            String xmlParams = this.getConfigManager().getConfigItem(SystemConstants.CONFIG_ITEM_PARAMS);
            String newXmlParams = SystemParamsUtils.getNewXmlParams(xmlParams, systemParams);
            this.getConfigManager().updateConfigItem(SystemConstants.CONFIG_ITEM_PARAMS, newXmlParams);
            return this.getDtoBuilder().convert(systemParams);
        } catch (Throwable e) {
            logger.error("Error updating user settings", e);
            throw new RestServerError("Error updating user settings", e);
        }
    }

    private Map<String, String> getSystemParams() throws Throwable {
        String xmlParams = this.getConfigManager().getConfigItem(SystemConstants.CONFIG_ITEM_PARAMS);
        return SystemParamsUtils.getParams(xmlParams);
    }

}
