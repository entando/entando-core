package org.entando.entando.aps.system.services.widgettype.validators;

import java.util.Map;

import com.agiletec.aps.util.ApsProperties;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;

public class NoOpWidgetConfigurationProcessor implements WidgetConfigurationProcessor {

    @Override
    public boolean supports(String widgetCode) {
        return false;
    }

    @Override
    public Object buildConfiguration(WidgetConfigurationRequest widget) {
        if (null == widget.getConfig()) {
            return null;
        }
        ApsProperties properties = new ApsProperties();
        properties.putAll((Map<? extends Object, ? extends Object>) widget.getConfig());
        return properties;
    }

    @Override
    public ApsProperties extractConfiguration(ApsProperties properties) {
        return properties;
    }

}
