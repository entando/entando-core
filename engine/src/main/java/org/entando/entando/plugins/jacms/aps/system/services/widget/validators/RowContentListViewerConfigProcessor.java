package org.entando.entando.plugins.jacms.aps.system.services.widget.validators;

import java.util.List;
import java.util.stream.Collectors;

import com.agiletec.aps.util.ApsProperties;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.widget.validators.WidgetConfigurationProcessor;
import org.entando.entando.aps.system.services.widget.validators.WidgetConfigurationValidator;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;

public class RowContentListViewerConfigProcessor implements WidgetConfigurationProcessor {

    public static final String WIDGET_CODE = "row_content_viewer_list";

    @Override
    public boolean supports(String widgetCode) {
        return WIDGET_CODE.equals(widgetCode);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object buildConfig(WidgetConfigurationRequest widget) {
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

}
