package org.entando.entando.aps.system.services.widgettype.validators;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 
 * @author spuddu
 *
 */
@Component
public class WidgetProcessorFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

    }

    /**
     * Returns the proper processor for the given wigetCode
     * @param widgetCode
     * @return
     */
    public WidgetConfigurationProcessor get(String widgetCode) {
        Map<String, WidgetConfigurationProcessor> beans = applicationContext.getBeansOfType(WidgetConfigurationProcessor.class);
        WidgetConfigurationProcessor defName = beans.values().stream()
                                      .filter(service -> service.supports(widgetCode))
                                      .findFirst()
                                                    .orElseGet(NoOpWidgetConfigurationProcessor::new);
        return defName;
    }

}
