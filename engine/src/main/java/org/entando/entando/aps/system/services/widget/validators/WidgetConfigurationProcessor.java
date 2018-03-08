package org.entando.entando.aps.system.services.widget.validators;

import org.entando.entando.web.page.model.WidgetConfigurationRequest;

/**
 * Interface to be implemented by components that performs widget configuration processing
 * 
 * 
 * @author spuddu
 *
 */
public interface WidgetConfigurationProcessor {

    /**
     * Returns true is the implementation of this processor fits the provided widget code
     * @param widgetCode
     * @return
     */
    boolean supports(String widgetCode);

    /**
     * Process the widgetConfiguration and returns the new one
     * @param widget
     * @return
     */
    public Object buildConfig(WidgetConfigurationRequest widget);
}
