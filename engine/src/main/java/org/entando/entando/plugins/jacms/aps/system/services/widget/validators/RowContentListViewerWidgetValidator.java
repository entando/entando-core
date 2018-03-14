package org.entando.entando.plugins.jacms.aps.system.services.widget.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.widget.validators.WidgetConfigurationValidator;
import org.entando.entando.plugins.jacms.aps.system.services.content.widget.RowContentListHelper;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;

public class RowContentListViewerWidgetValidator extends AbstractListViewerWidgetValidator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String WIDGET_CODE = "row_content_viewer_list";

    private static final String WIDGET_CONFIG_KEY_CONTENTS = "contents";


    private IContentManager contentManager;

    protected IContentManager getContentManager() {
        return contentManager;
    }

    public void setContentManager(IContentManager contentManager) {
        this.contentManager = contentManager;
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
            this.validateContents(widget, page, bindingResult);
        } catch (Throwable e) {
            logger.error("error in validate wiget {} in page {}", widget.getCode(), page.getCode());
            throw new RestServerError("error in widget config validation", e);
        }
        return bindingResult;
    }

    private void validateContents(WidgetConfigurationRequest widget, IPage page, BeanPropertyBindingResult errors) throws ApsSystemException {
        List<Object> contents = extractContentsConfiguration(widget);

        if (null == contents) {
            errors.reject(WidgetValidatorCmsHelper.ERRCODE_INVALID_CONFIGURATION, new String[]{}, widget.getCode() + ".contents.required");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        List<RowContentListConfigurationEntry> list = new ArrayList<>();
        for (Object map : contents) {
            RowContentListConfigurationEntry entry = mapper.convertValue(map, RowContentListConfigurationEntry.class);
            list.add(entry);
        }

        widget.getProcessInfo().put(WidgetConfigurationValidator.PROCESS_INFO_CONFIG, list);

        List<String> contentIdList = list
                                         .stream()
                                         .map(i -> i.getContentId()).collect(Collectors.toList());

        if (null == contentIdList || contentIdList.isEmpty()) {
            errors.reject(WidgetValidatorCmsHelper.ERRCODE_INVALID_CONFIGURATION, new String[]{}, widget.getCode() + ".contents.required");
            return;
        }

        //TODO validate MODEL if present
        for (String contentId : contentIdList) {
            WidgetValidatorCmsHelper.validateSingleContentOnPage(widget.getCode(), page, contentId, this.getContentManager(), errors);

        }
    }


    /**
     * try to build the configuration from a complex structure or from a string
     * @param widget
     * @return
     */
    protected List<Object> extractContentsConfiguration(WidgetConfigurationRequest widget) {
        List<Object> contents = WidgetValidatorCmsHelper.extractConfig(widget, WIDGET_CONFIG_KEY_CONTENTS);
        if (null == contents) {
            String configuration = WidgetValidatorCmsHelper.extractConfigParam(widget, WIDGET_CONFIG_KEY_CONTENTS);
            if (StringUtils.isNotBlank(configuration)) {
                List<Properties> props = RowContentListHelper.fromParameterToContents(configuration);
                if (null != props && !props.isEmpty()) {
                    final List<Object> validProps = new ArrayList<>(); //fix fromParameterToContents when also modelId is defined
                    props.stream().filter(i -> !i.isEmpty())
                         .forEach(i -> validProps.add(i));
                    contents = validProps;
                }

            }
        }
        return contents;
    }

}
