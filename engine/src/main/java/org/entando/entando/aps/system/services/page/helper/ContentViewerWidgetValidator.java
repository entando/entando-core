package org.entando.entando.aps.system.services.page.helper;

import com.agiletec.aps.system.services.page.IPage;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;

public class ContentViewerWidgetValidator implements WidgetConfigurationValidator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String WIDGET_CODE = "content_viewer";

    @Override
    public boolean supports(String widgetCode) {
        return WIDGET_CODE.equals(widgetCode);
    }

    @Override
    public BeanPropertyBindingResult validate(WidgetConfigurationRequest widget, IPage page) {
        logger.debug("validate widget {} into page {}", widget.getCode(), page.getCode());
        //TODO 
        return null;
    }

}
