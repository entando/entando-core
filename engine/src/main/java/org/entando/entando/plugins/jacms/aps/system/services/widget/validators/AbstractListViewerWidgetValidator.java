package org.entando.entando.plugins.jacms.aps.system.services.widget.validators;

import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.widget.validators.WidgetConfigurationValidator;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;

/**
 * Abstract implementation for cms list-like widget validators 
 * 
 * @author spuddu
 *
 */
public abstract class AbstractListViewerWidgetValidator implements WidgetConfigurationValidator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ILangManager langManager;
    private IPageManager pageManager;

    protected ILangManager getLangManager() {
        return langManager;
    }

    public void setLangManager(ILangManager langManager) {
        this.langManager = langManager;
    }

    protected IPageManager getPageManager() {
        return pageManager;
    }

    public void setPageManager(IPageManager pageManager) {
        this.pageManager = pageManager;
    }

    @Override
    public boolean supports(String widgetCode) {
        return getWidgetCode().equals(widgetCode);
    }

    public abstract String getWidgetCode();

    @Override
    public BeanPropertyBindingResult validate(WidgetConfigurationRequest widget, IPage page) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(widget, widget.getClass().getSimpleName());
        try {
            logger.debug("validating widget {} for page {}", widget.getCode(), page.getCode());
            WidgetValidatorCmsHelper.validateTitle(widget, getLangManager(), bindingResult);
            WidgetValidatorCmsHelper.validateLink(widget, getLangManager(), getPageManager(), bindingResult);
        } catch (Throwable e) {
            logger.error("error in validate wiget {} in page {}", widget.getCode(), page.getCode());
            throw new RestServerError("error in widget config validation", e);
        }
        return bindingResult;
    }

}
