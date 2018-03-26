package org.entando.entando.plugins.jacms.aps.system.services.widget.validators;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.widget.validators.WidgetConfigurationValidator;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;

public class ContentViewerWidgetValidator implements WidgetConfigurationValidator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private IContentManager contentManager;
    private IContentModelManager contentModelManager;

    public static final String WIDGET_CODE = "content_viewer";

    public static final String WIDGET_CONFIG_KEY_CONTENT_ID = "contentId";
    public static final String WIDGET_CONFIG_KEY_MODEL_ID = "modelId";


    protected IContentManager getContentManager() {
        return contentManager;
    }

    public void setContentManager(IContentManager contentManager) {
        this.contentManager = contentManager;
    }

    protected IContentModelManager getContentModelManager() {
        return contentModelManager;
    }

    public void setContentModelManager(IContentModelManager contentModelManager) {
        this.contentModelManager = contentModelManager;
    }

    @Override
    public boolean supports(String widgetCode) {
        return WIDGET_CODE.equals(widgetCode);
    }

    @Override
    public BeanPropertyBindingResult validate(WidgetConfigurationRequest widget, IPage page) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(widget, widget.getClass().getSimpleName());
        try {
            logger.debug("validating widget {} for page {}", widget.getCode(), page.getCode());
            String contentId = WidgetValidatorCmsHelper.extractConfigParam(widget, WIDGET_CONFIG_KEY_CONTENT_ID);
            WidgetValidatorCmsHelper.validateSingleContentOnPage(widget.getCode(), page, contentId, this.getContentManager(), bindingResult);
            this.validateContentModel(widget, bindingResult);
        } catch (ApsSystemException e) {
            logger.error("error in validate wiget {} in page {}", widget.getCode(), page.getCode());
            throw new RestServerError("error in widget config validation", e);
        }
        return bindingResult;
    }

    private void validateContentModel(WidgetConfigurationRequest widget, BeanPropertyBindingResult errors) {
        String contentModel = WidgetValidatorCmsHelper.extractConfigParam(widget, WIDGET_CONFIG_KEY_MODEL_ID);
        String contentId = WidgetValidatorCmsHelper.extractConfigParam(widget, WIDGET_CONFIG_KEY_CONTENT_ID);
        String typeCode = contentId.substring(0, 3);

        WidgetValidatorCmsHelper.validateContentModel(widget.getCode(), typeCode, contentModel, this.getContentModelManager(), errors);

    }


}
