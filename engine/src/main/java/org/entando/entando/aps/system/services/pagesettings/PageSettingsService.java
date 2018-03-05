/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.aps.system.services.pagesettings;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.baseconfig.SystemParamsUtils;
import java.util.HashMap;
import java.util.Map;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.pagesettings.model.PageSettingsDto;
import org.entando.entando.web.pagesettings.model.PageSettingsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author paddeo
 */
public class PageSettingsService implements IPageSettingsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ConfigInterface configManager;

    @Autowired
    private IDtoBuilder<Map<String, String>, PageSettingsDto> dtoBuilder;

    public ConfigInterface getConfigManager() {
        return configManager;
    }

    public void setConfigManager(ConfigInterface configManager) {
        this.configManager = configManager;
    }

    public IDtoBuilder<Map<String, String>, PageSettingsDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<Map<String, String>, PageSettingsDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @Override
    public PageSettingsDto getPageSettings() {
        try {
            Map<String, String> systemParams = this.getSystemParams();
            return this.getDtoBuilder().convert(systemParams);
        } catch (Throwable e) {
            logger.error("Error getting page settings", e);
            throw new RestServerError("Error getting page settings", e);
        }
    }

    @Override
    public PageSettingsDto updatePageSettings(PageSettingsRequest request) {
        try {
            Map<String, String> params = this.createParamsMap(request);
            Map<String, String> systemParams = this.getSystemParams();
            params.keySet().forEach((param) -> {
                systemParams.put(param, params.get(param));
            });
            String xmlParams = this.getConfigManager().getConfigItem(SystemConstants.CONFIG_ITEM_PARAMS);
            String newXmlParams = SystemParamsUtils.getNewXmlParams(xmlParams, systemParams);
            this.getConfigManager().updateConfigItem(SystemConstants.CONFIG_ITEM_PARAMS, newXmlParams);
            return this.getDtoBuilder().convert(systemParams);
        } catch (Throwable e) {
            logger.error("Error updating page settings", e);
            throw new RestServerError("Error updating page settings", e);
        }

    }

    private Map<String, String> getSystemParams() throws Throwable {
        String xmlParams = this.getConfigManager().getConfigItem(SystemConstants.CONFIG_ITEM_PARAMS);
        return SystemParamsUtils.getParams(xmlParams);
    }

    private Map<String, String> createParamsMap(PageSettingsRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParams().forEach((param) -> {
            params.put(param.getName(), param.getValue());
        });
        return params;
    }

}
