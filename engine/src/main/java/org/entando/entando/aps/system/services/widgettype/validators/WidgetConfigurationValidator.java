package org.entando.entando.aps.system.services.widgettype.validators;

import com.agiletec.aps.system.services.page.IPage;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.springframework.validation.BeanPropertyBindingResult;

/**
 * Interface to be implemented by components that performs widget configuration validation
 * 
 * 
 * @author spuddu
 *
 */
public interface WidgetConfigurationValidator {

    public static final String PROCESS_INFO_CONFIG = "cfg";

    /**
     * Returns true is the implementation of this validator fits the provided widget code
     * @param widgetCode
     * @return
     */
    boolean supports(String widgetCode);

    /**
     * Validate the widgetConfiguration against the given page
     * @param widget
     * @param page
     * @return
     */
    public BeanPropertyBindingResult validate(WidgetConfigurationRequest widget, IPage page);
}
