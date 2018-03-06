package org.entando.entando.aps.system.services.widget.validators;

import com.agiletec.aps.system.services.page.IPage;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;

public class NoOpWidgetConfigurationValidator implements WidgetConfigurationValidator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean supports(String widgetCode) {
        return false;
    }

    @Override
    public BeanPropertyBindingResult validate(WidgetConfigurationRequest widget, IPage page) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(widget, widget.getClass().getSimpleName());
        logger.warn("no WidgetConfigurationValidator implementation found for widget {} ", widget.getCode());
        return bindingResult;
    }

}
