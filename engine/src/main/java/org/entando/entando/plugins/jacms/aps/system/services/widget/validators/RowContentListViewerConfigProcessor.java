package org.entando.entando.plugins.jacms.aps.system.services.widget.validators;

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
import org.entando.entando.aps.system.services.widget.validators.WidgetConfigurationProcessor;
import org.entando.entando.aps.system.services.widget.validators.WidgetConfigurationValidator;
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
