/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.plugins.jacms.aps.system.services.widgettype.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.widgettype.validators.WidgetConfigurationValidator;
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
    private IContentModelManager contentModelManager;

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
            this.validateContents(widget, page, bindingResult);
        } catch (Throwable e) {
            logger.error("error in validate wiget {} in page {}", widget.getCode(), page.getCode());
            throw new RestServerError("error in widget config validation", e);
        }
        return bindingResult;
    }

    private void validateContents(WidgetConfigurationRequest widget, IPage page, BeanPropertyBindingResult errors) throws ApsSystemException {
        List<Object> contentsConfig = extractContentsConfiguration(widget);

        if (null == contentsConfig) {
            errors.reject(WidgetValidatorCmsHelper.ERRCODE_INVALID_CONFIGURATION, new String[]{}, widget.getCode() + ".contents.required");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        List<RowContentListConfigurationEntry> contentsConfigEntries = new ArrayList<>();
        for (Object contentConfEntry : contentsConfig) {
            RowContentListConfigurationEntry entry = mapper.convertValue(contentConfEntry, RowContentListConfigurationEntry.class);
            contentsConfigEntries.add(entry);
        }

        widget.getProcessInfo().put(WidgetConfigurationValidator.PROCESS_INFO_CONFIG, contentsConfigEntries);

        if (contentsConfigEntries.isEmpty()) {
            errors.reject(WidgetValidatorCmsHelper.ERRCODE_INVALID_CONFIGURATION, new String[]{}, widget.getCode() + ".contents.required");
            return;
        }

        for (RowContentListConfigurationEntry entry : contentsConfigEntries) {
            WidgetValidatorCmsHelper.validateSingleContentOnPage(widget.getCode(), page, entry.getContentId(), this.getContentManager(), errors);
            String typeCode = entry.getContentId().substring(0, 3);
            WidgetValidatorCmsHelper.validateContentModel(widget.getCode(), typeCode, entry.getModelId(), this.getContentModelManager(), errors);
        }
    }

    /**
     * try to build the configuration from a complex structure or from a string
     *
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
                    final List<Object> validProps = new ArrayList<>();
                    props.stream().filter(i -> !i.isEmpty())
                            .forEach(i -> validProps.add(i));
                    contents = validProps;
                }

            }
        }
        return contents;
    }

}
