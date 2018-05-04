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
import java.util.Map;
import java.util.stream.Collectors;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.IContentListWidgetHelper;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jacms.aps.util.CmsPageUtil;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;

public class WidgetValidatorCmsHelper {

    private static final Logger logger = LoggerFactory.getLogger(WidgetValidatorCmsHelper.class);

    public static final String ERRCODE_INVALID_CONFIGURATION = "1";
    public static final String ERRCODE_CONTENT_ID_NULL = "11";
    public static final String ERRCODE_CONTENT_INVALID = "12";

    public static void validateContentModel(String widgetCode, String typeCode, String modelId, IContentModelManager contentModelManager, BeanPropertyBindingResult errors) {
        if (null == modelId) {
            //the model id can be null, default model will be used
            return;
        }
        List<String> contentModels = contentModelManager.getModelsForContentType(typeCode).stream().map(i -> String.valueOf(i.getId())).collect(Collectors.toList());
        contentModels.add("list");
        contentModels.add("default");
        if (!contentModels.contains(modelId)) {
            errors.reject(WidgetValidatorCmsHelper.ERRCODE_INVALID_CONFIGURATION, new String[]{modelId, typeCode}, widgetCode + ".contentmodel.invalid");
        }
    }

    public static void validateSingleContentOnPage(String widgetCode, IPage page, String contentId, IContentManager contentManager, BeanPropertyBindingResult errors) throws ApsSystemException {
        if (StringUtils.isBlank(contentId)) {
            errors.reject(ERRCODE_CONTENT_ID_NULL, new String[]{}, widgetCode + ".contentId.required");
            return;
        }

        Content publishingContent = contentManager.loadContent(contentId, true);
        if (null == publishingContent) {
            errors.reject(ERRCODE_CONTENT_ID_NULL, new String[]{contentId}, widgetCode + ".contentId.not_found");
            return;
        }

        if (!CmsPageUtil.isContentPublishableOnPageDraft(publishingContent, page)) {
            PageMetadata metadata = page.getMetadata();
            List<String> pageGroups = new ArrayList<String>();
            pageGroups.add(page.getGroup());
            if (null != metadata.getExtraGroups()) {
                pageGroups.addAll(metadata.getExtraGroups());
            }
            List<String> contentGroups = CmsPageUtil.getContentGroups(publishingContent);
            errors.reject(ERRCODE_CONTENT_INVALID, new String[]{StringUtils.join(pageGroups, ", "), StringUtils.join(contentGroups, ", ")}, widgetCode + ".contentId.group.invalid");
            return;
        }
    }

    public static void validateTitle(WidgetConfigurationRequest widget, ILangManager langManager, BeanPropertyBindingResult errors) {
        String titleParamPrefix = IContentListWidgetHelper.WIDGET_PARAM_TITLE + "_";
        if (isMultilanguageParamValued(widget, titleParamPrefix, langManager)) {
            Lang defaultLang = langManager.getDefaultLang();
            String defaultTitleParam = titleParamPrefix + defaultLang.getCode();
            String defaultTitle = extractConfigParam(widget, defaultTitleParam);
            if (StringUtils.isBlank(defaultTitle)) {
                errors.reject(ERRCODE_INVALID_CONFIGURATION, new String[]{defaultLang.getDescr()}, widget.getCode() + ".defaultLangTitle.required");
            }
        }
    }

    public static void validateLink(WidgetConfigurationRequest widget, ILangManager langManager, IPageManager pageManager, BeanPropertyBindingResult errors) {
        String pageLink = extractConfigParam(widget, IContentListWidgetHelper.WIDGET_PARAM_PAGE_LINK);
        boolean existsPageLink = pageLink != null && pageManager.getDraftPage(pageLink) != null;
        String linkDescrParamPrefix = IContentListWidgetHelper.WIDGET_PARAM_PAGE_LINK_DESCR + "_";
        if (existsPageLink || isMultilanguageParamValued(widget, linkDescrParamPrefix, langManager)) {
            if (!existsPageLink) {
                errors.reject(ERRCODE_INVALID_CONFIGURATION, new String[]{pageLink}, widget.getCode() + ".pageLink.required");
            }
            Lang defaultLang = langManager.getDefaultLang();
            String defaultLinkDescrParam = linkDescrParamPrefix + defaultLang.getCode();
            String defaultLinkDescr = extractConfigParam(widget, defaultLinkDescrParam);
            if (defaultLinkDescr == null || defaultLinkDescr.length() == 0) {
                errors.reject(ERRCODE_INVALID_CONFIGURATION, new String[]{defaultLang.getDescr()}, widget.getCode() + ".defaultLangLink.required");
            }
        }
    }

    public static String extractConfigParam(WidgetConfigurationRequest widget, String key) {
        Map<String, Object> properties = (Map<String, Object>) widget.getConfig();
        if (null != properties) {
            return (String) properties.get(key);
        }
        return null;
    }

    public static List<Object> extractConfig(WidgetConfigurationRequest widget, String key) {
        Map<String, Object> config = (Map<String, Object>) widget.getConfig();
        if (null != config) {
            Object entry = config.get(key);
            if (null != entry && entry instanceof String) {
                logger.warn("the config entry {} is supposed to be complex, but is a String", entry.toString());
                return null;
            }
            return (List<Object>) entry;
        }
        return null;
    }

    protected static boolean isMultilanguageParamValued(WidgetConfigurationRequest widget, String prefix, ILangManager langManager) {
        Map<String, Object> config = widget.getConfig();
        if (null == config) {
            return false;
        }
        List<Lang> langs = langManager.getLangs();
        for (int i = 0; i < langs.size(); i++) {
            Lang lang = langs.get(i);
            String paramValue = extractConfigParam(widget, prefix + lang.getCode());// (String) config.get(prefix + lang.getCode());
            if (null != paramValue && paramValue.trim().length() > 0) {
                return true;
            }
        }
        return false;
    }

}
