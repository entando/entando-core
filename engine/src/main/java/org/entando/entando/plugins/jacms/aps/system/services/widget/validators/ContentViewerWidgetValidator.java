package org.entando.entando.plugins.jacms.aps.system.services.widget.validators;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.widget.validators.WidgetConfigurationValidator;
import org.entando.entando.plugins.jacms.aps.util.CmsPageUtil;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;

public class ContentViewerWidgetValidator implements WidgetConfigurationValidator {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private IContentManager contentManager;

    public static final String WIDGET_CODE = "content_viewer";

    public static final String WIDGET_CONFIG_KEY_CONTENT_ID = "contentId";

    private static final String ERRCODE_CONTENT_ID_NULL = "1";
    private static final String ERRCODE_CONTENT_INVALID = "2";

    public IContentManager getContentManager() {
        return contentManager;
    }

    public void setContentManager(IContentManager contentManager) {
        this.contentManager = contentManager;
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

            String contentId = this.extractContentId(widget);
            if (StringUtils.isBlank(contentId)) {
                bindingResult.reject(ERRCODE_CONTENT_ID_NULL, new String[]{}, WIDGET_CODE + ".contentId.required");
                return bindingResult;
            }

            Content publishingContent = this.getContentManager().loadContent(contentId, true);
            if (null == publishingContent) {
                bindingResult.reject(ERRCODE_CONTENT_ID_NULL, new String[]{contentId}, WIDGET_CODE + ".contentId.not_found");
                return bindingResult;
            }

            if (!CmsPageUtil.isContentPublishableOnPageDraft(publishingContent, page)) {
                PageMetadata metadata = page.getMetadata();
                List<String> pageGroups = new ArrayList<String>();
                pageGroups.add(page.getGroup());
                if (null != metadata.getExtraGroups()) {
                    pageGroups.addAll(metadata.getExtraGroups());
                }
                bindingResult.reject(ERRCODE_CONTENT_INVALID, new String[]{pageGroups.toString()}, WIDGET_CODE + ".contentId.invalid");
                return bindingResult;
            }

        } catch (ApsSystemException e) {
            throw new RestServerError("error in widget config validation", e);
        }
        return bindingResult;
    }

    protected String extractContentId(WidgetConfigurationRequest widget) {
        ApsProperties properties = widget.getConfig();
        if (null != properties) {
            return properties.getProperty(WIDGET_CONFIG_KEY_CONTENT_ID);
        }
        return null;
    }


}
