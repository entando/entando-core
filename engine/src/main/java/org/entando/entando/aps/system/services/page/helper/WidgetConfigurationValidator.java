package org.entando.entando.aps.system.services.page.helper;

import com.agiletec.aps.system.services.page.IPage;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.springframework.validation.BeanPropertyBindingResult;

public interface WidgetConfigurationValidator {



    boolean supports(String widgetCode);

    /**
     * Validate the widgetConfiguration against the given page
     * @param widget
     * @param page
     * @return
     */
    public BeanPropertyBindingResult validate(WidgetConfigurationRequest widget, IPage page);
}
