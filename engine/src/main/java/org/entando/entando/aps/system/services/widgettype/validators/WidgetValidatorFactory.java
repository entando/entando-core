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
public class WidgetValidatorFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

    }

    /**
     * Returns the proper validator for the given wigetCode
     * @param widgetCode
     * @return
     */
    public WidgetConfigurationValidator get(String widgetCode) {

        Map<String, WidgetConfigurationValidator> beans = applicationContext.getBeansOfType(WidgetConfigurationValidator.class);
        WidgetConfigurationValidator defName = beans.values().stream()
                                      .filter(service -> service.supports(widgetCode))
                                      .findFirst()
                                                    .orElseGet(NoOpWidgetConfigurationValidator::new);
        //TODO .. if defName is null, check the parent, the parent configuration...
        return defName;
    }

}
