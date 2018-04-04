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
import java.util.Hashtable;
import java.util.List;
import org.entando.entando.aps.system.exception.RestServerError;

import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WidgetDtoBuilder extends DtoBuilder<WidgetType, WidgetDto> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private IPageManager pageManager;

    private String stockWidgetCodes;

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
        dest.setPluginCode(pluginCode);
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

}
