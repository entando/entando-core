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
package org.entando.entando.plugins.jacms.aps.system.services.widgettype.validators;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import com.agiletec.aps.util.ApsProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.widgettype.validators.WidgetConfigurationProcessor;
import org.entando.entando.aps.system.services.widgettype.validators.WidgetConfigurationValidator;
import org.entando.entando.plugins.jacms.aps.system.services.content.widget.RowContentListHelper;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;

public class RowContentListViewerConfigProcessor implements WidgetConfigurationProcessor {

    public static final String WIDGET_CODE = "row_content_viewer_list";

    @Override
    public boolean supports(String widgetCode) {
        return WIDGET_CODE.equals(widgetCode);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object buildConfiguration(WidgetConfigurationRequest widget) {
        ApsProperties properties = new ApsProperties();
        List<RowContentListConfigurationEntry> entryList = (List<RowContentListConfigurationEntry>) widget.getProcessInfo().get(WidgetConfigurationValidator.PROCESS_INFO_CONFIG);
        if (null != entryList && !entryList.isEmpty()) {
            StringBuffer sbuffer = new StringBuffer("[");
            List<String> configTokens = entryList
                    .stream()
                    .map(i -> i.toCfg()).collect(Collectors.toList());
            sbuffer.append(StringUtils.join(configTokens, ","));
            sbuffer.append("]");
            properties.put("contents", sbuffer.toString());
        }
        return properties;
    }

    private static final String WIDGET_CONFIG_KEY_CONTENTS = "contents";

    /**
     * try to build the configuration from a complex structure or from a string
     *
     * @param widget
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    @Override
    public ApsProperties extractConfiguration(ApsProperties widgetProperties) {
        List<Properties> props = RowContentListHelper.fromParameterToContents(widgetProperties.getProperty(WIDGET_CONFIG_KEY_CONTENTS));
        Map<String, List<Properties>> map = new HashMap<>();
        map.put(WIDGET_CONFIG_KEY_CONTENTS, props);
        ApsProperties smartProperties = new ApsProperties();
        smartProperties.putAll(map);
        return smartProperties;
    }

}
