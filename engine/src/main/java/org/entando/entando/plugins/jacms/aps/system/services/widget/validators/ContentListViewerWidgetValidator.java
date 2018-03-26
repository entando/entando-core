package org.entando.entando.plugins.jacms.aps.system.services.widget.validators;

import java.util.Map;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;

public class ContentListViewerWidgetValidator extends AbstractListViewerWidgetValidator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String WIDGET_CODE = "content_viewer_list";

    public static final String WIDGET_CONFIG_KEY_CATEGORIES = "categories";
    public static final String WIDGET_CONFIG_KEY_MAXELEMFORITEM = "maxElemForItem";
    public static final String WIDGET_CONFIG_KEY_MAXELEMENTS = "maxElements";
    public static final String WIDGET_CONFIG_KEY_CONTENTTYPE = "contentType";
    public static final String WIDGET_CONFIG_KEY_MODEL_ID = "modelId";


    private IWidgetTypeManager widgetTypeManager;
    private IContentManager contentManager;
    private IContentModelManager contentModelManager;

    protected IWidgetTypeManager getWidgetTypeManager() {
        return widgetTypeManager;
    }

    public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
        this.widgetTypeManager = widgetTypeManager;
    }

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
    public String getWidgetCode() {
        return WIDGET_CODE;
    }

    @Override
    public BeanPropertyBindingResult validate(WidgetConfigurationRequest widget, IPage page) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(widget, widget.getClass().getSimpleName());
        try {
            logger.debug("validating widget {} for page {}", widget.getCode(), page.getCode());
            WidgetValidatorCmsHelper.validateTitle(widget, getLangManager(), bindingResult);
            WidgetValidatorCmsHelper.validateLink(widget, getLangManager(), getPageManager(), bindingResult);
            this.validateContentType(widget, bindingResult);
            this.validateFilters(widget, bindingResult);
            this.validateContentModel(widget, bindingResult);

        } catch (Throwable e) {
            logger.error("error in validate wiget {} in page {}", widget.getCode(), page.getCode());
            throw new RestServerError("error in widget config validation", e);
        }
        return bindingResult;
    }

    private void validateContentModel(WidgetConfigurationRequest widget, BeanPropertyBindingResult errors) {
        String contentModel = WidgetValidatorCmsHelper.extractConfigParam(widget, WIDGET_CONFIG_KEY_MODEL_ID);
        String contentType = WidgetValidatorCmsHelper.extractConfigParam(widget, WIDGET_CONFIG_KEY_CONTENTTYPE);

        WidgetValidatorCmsHelper.validateContentModel(widget.getCode(), contentType, contentModel, this.getContentModelManager(), errors);

    }

    protected void validateContentType(WidgetConfigurationRequest widget, BeanPropertyBindingResult errors) {
        String contentType = WidgetValidatorCmsHelper.extractConfigParam(widget, WIDGET_CONFIG_KEY_CONTENTTYPE);
        if (StringUtils.isBlank(contentType)) {
            errors.reject(WidgetValidatorCmsHelper.ERRCODE_INVALID_CONFIGURATION, new String[]{}, WIDGET_CODE + ".contentType.required");
            return;
        }
        if (null == this.getContentManager().getSmallContentTypesMap().get(contentType)) {
            errors.reject(WidgetValidatorCmsHelper.ERRCODE_INVALID_CONFIGURATION, new String[]{contentType}, WIDGET_CODE + ".contentType.invalid");
            return;
        }
    }

    protected void validateFilters(WidgetConfigurationRequest widget, BeanPropertyBindingResult errors) {
        WidgetType type = this.getWidgetTypeManager().getWidgetType(widget.getCode());
        Map<String, Object> config = (Map<String, Object>) widget.getConfig();
        if (null != config &&
            null != type &&
            type.hasParameter(WIDGET_CONFIG_KEY_CATEGORIES) &&
            type.hasParameter(WIDGET_CONFIG_KEY_MAXELEMFORITEM) &&
            type.hasParameter(WIDGET_CONFIG_KEY_MAXELEMENTS) &&
            StringUtils.isNotEmpty((String) config.get(WIDGET_CONFIG_KEY_CONTENTTYPE)) &&
            StringUtils.isEmpty((String) config.get(WIDGET_CONFIG_KEY_CATEGORIES)) &&
            StringUtils.isEmpty((String) config.get(WIDGET_CONFIG_KEY_MAXELEMFORITEM)) &&
            StringUtils.isEmpty((String) config.get(WIDGET_CONFIG_KEY_MAXELEMENTS))) {
            errors.reject(WidgetValidatorCmsHelper.ERRCODE_INVALID_CONFIGURATION, new String[]{}, WIDGET_CODE + ".parameters.invalid");
        }
    }

}
