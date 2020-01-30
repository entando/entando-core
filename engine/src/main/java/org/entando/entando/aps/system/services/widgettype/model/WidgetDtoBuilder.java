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
package org.entando.entando.aps.system.services.widgettype.model;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.init.IComponentManager;
import org.entando.entando.aps.system.init.model.Component;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class WidgetDtoBuilder extends DtoBuilder<WidgetType, WidgetDto> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private IPageManager pageManager;

    private String stockWidgetCodes;

    private IComponentManager componentManager;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected WidgetDto toDto(WidgetType src) {
        WidgetDto dest = new WidgetDto();
        dest.setCode(src.getCode());
        dest.setGroup(src.getMainGroup());
        dest.setTitles((Hashtable) src.getTitles());
        Integer count = 0;
        try {
            List<IPage> onLinePages = this.getPageManager().getOnlineWidgetUtilizers(src.getCode());
            count += onLinePages.size();
            List<IPage> draftPages = this.getPageManager().getDraftWidgetUtilizers(src.getCode());
            count += draftPages.size();
        } catch (Exception e) {
            logger.error("Error extracting utilizers for widget {}", src.getCode());
            throw new RestServerError("Error extracting utilizers for widget " + src.getCode(), e);
        }
        dest.setUsed(count);
        String pluginCode = src.getPluginCode();
        Component plugin = this.getComponentManager().getInstalledComponent(pluginCode);
        dest.setPluginCode(pluginCode);
        dest.setPluginDesc(plugin != null ? plugin.getDescription() : null);
        List<WidgetTypeParameter> params = src.getTypeParameters();
        dest.setHasConfig(null != params && params.size() > 0);
        if (null != pluginCode && pluginCode.trim().length() > 0) {
            dest.setTypology(pluginCode);
        } else if (src.isUserType()) {
            dest.setTypology(WidgetDto.USER_TYPOLOGY_CODE);
        } else if (this.getStockWidgetCodes().contains(src.getCode())) {
            dest.setTypology(WidgetDto.STOCK_TYPOLOGY_CODE);
        } else {
            dest.setTypology(WidgetDto.CUSTOM_TYPOLOGY_CODE);
        }
        dest.setBundleId(src.getBundleId());
        try {
            if (StringUtils.isNotBlank(src.getConfigUi())) {
                dest.setConfigUi(objectMapper.readValue(src.getConfigUi(), new TypeReference<Map<String, Object>>() {
                }));
            }
        } catch (JsonProcessingException e) {
            logger.error("Error parsing configUi json to object for widget {}", src.getCode(), e);
        }
        return dest;
    }

    public IPageManager getPageManager() {
        return pageManager;
    }

    public void setPageManager(IPageManager pageManager) {
        this.pageManager = pageManager;
    }

    protected String getStockWidgetCodes() {
        return stockWidgetCodes;
    }

    public void setStockWidgetCodes(String stockWidgetCodes) {
        this.stockWidgetCodes = stockWidgetCodes;
    }

    public IComponentManager getComponentManager() {
        return componentManager;
    }

    public void setComponentManager(IComponentManager componentManager) {
        this.componentManager = componentManager;
    }

}
