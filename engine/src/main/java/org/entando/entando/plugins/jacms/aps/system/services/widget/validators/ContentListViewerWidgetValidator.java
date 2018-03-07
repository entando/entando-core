package org.entando.entando.plugins.jacms.aps.system.services.widget.validators;

import java.util.List;

import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.IContentListWidgetHelper;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.widget.validators.WidgetConfigurationValidator;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;

public class ContentListViewerWidgetValidator implements WidgetConfigurationValidator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String WIDGET_CODE = "content_viewer_list";

    public static final String WIDGET_CONFIG_KEY_CATEGORIES = "categories";
    public static final String WIDGET_CONFIG_KEY_MAXELEMFORITEM = "maxElemForItem";
    public static final String WIDGET_CONFIG_KEY_MAXELEMENTS = "maxElements";
    public static final String WIDGET_CONFIG_KEY_CONTENTTYPE = "contentType";

    private static final String ERRCODE_INVALID_CONFIGURATION = "1";

    private ILangManager langManager;
    private IWidgetTypeManager widgetTypeManager;
    private IPageManager pageManager;

    @Override
    public boolean supports(String widgetCode) {
        return WIDGET_CODE.equals(widgetCode);
    }

    protected ILangManager getLangManager() {
        return langManager;
    }

    public void setLangManager(ILangManager langManager) {
        this.langManager = langManager;
    }

    protected IWidgetTypeManager getWidgetTypeManager() {
        return widgetTypeManager;
    }

    public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
        this.widgetTypeManager = widgetTypeManager;
    }

    protected IPageManager getPageManager() {
        return pageManager;
    }

    public void setPageManager(IPageManager pageManager) {
        this.pageManager = pageManager;
    }

    @Override
    public BeanPropertyBindingResult validate(WidgetConfigurationRequest widget, IPage page) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(widget, widget.getClass().getSimpleName());
        try {
            logger.debug("validating widget {} for page {}", widget.getCode(), page.getCode());
            this.validateTitle(widget, bindingResult);
            this.validateLink(widget, bindingResult);
            this.validateFilters(widget, bindingResult);
        } catch (Throwable e) {
            throw new RestServerError("error in widget config validation", e);
        }
        return bindingResult;
    }

    protected void validateTitle(WidgetConfigurationRequest widget, BeanPropertyBindingResult errors) {
        String titleParamPrefix = IContentListWidgetHelper.WIDGET_PARAM_TITLE + "_";
        if (this.isMultilanguageParamValued(widget, titleParamPrefix)) {
            Lang defaultLang = this.getLangManager().getDefaultLang();
            String defaultTitleParam = titleParamPrefix + defaultLang.getCode();
            String defaultTitle = this.extractConfigParam(widget, defaultTitleParam);
            if (StringUtils.isBlank(defaultTitle)) {
                errors.reject(ERRCODE_INVALID_CONFIGURATION, new String[]{defaultLang.getDescr()}, WIDGET_CODE + ".defaultLangTitle.required");
            }
        }
    }

    protected void validateLink(WidgetConfigurationRequest widget, BeanPropertyBindingResult errors) {
        String pageLink = this.extractConfigParam(widget, IContentListWidgetHelper.WIDGET_PARAM_PAGE_LINK);
        boolean existsPageLink = pageLink != null && this.getPage(pageLink) != null;
        String linkDescrParamPrefix = IContentListWidgetHelper.WIDGET_PARAM_PAGE_LINK_DESCR + "_";
        if (existsPageLink || this.isMultilanguageParamValued(widget, linkDescrParamPrefix)) {
            if (!existsPageLink) {
                errors.reject(ERRCODE_INVALID_CONFIGURATION, new String[]{pageLink}, WIDGET_CODE + ".pageLink.required");
            }
            Lang defaultLang = this.getLangManager().getDefaultLang();
            String defaultLinkDescrParam = linkDescrParamPrefix + defaultLang.getCode();
            String defaultLinkDescr = this.extractConfigParam(widget, defaultLinkDescrParam);
            if (defaultLinkDescr == null || defaultLinkDescr.length() == 0) {
                errors.reject(ERRCODE_INVALID_CONFIGURATION, new String[]{defaultLang.getDescr()}, WIDGET_CODE + ".defaultLangLink.required");
            }
        }
    }

    protected void validateFilters(WidgetConfigurationRequest widget, BeanPropertyBindingResult errors) {
        WidgetType type = this.getWidgetTypeManager().getWidgetType(widget.getCode());
        ApsProperties config = widget.getConfig();
        if (null != config &&
            null != type &&
            type.hasParameter(WIDGET_CONFIG_KEY_CATEGORIES) &&
            type.hasParameter(WIDGET_CONFIG_KEY_MAXELEMFORITEM) &&
            type.hasParameter(WIDGET_CONFIG_KEY_MAXELEMENTS) &&
            StringUtils.isNotEmpty(config.getProperty(WIDGET_CONFIG_KEY_CONTENTTYPE)) &&
            StringUtils.isEmpty(config.getProperty(WIDGET_CONFIG_KEY_CATEGORIES)) &&
            StringUtils.isEmpty(config.getProperty(WIDGET_CONFIG_KEY_MAXELEMFORITEM)) &&
            StringUtils.isEmpty(config.getProperty(WIDGET_CONFIG_KEY_MAXELEMENTS))) {
            errors.reject(ERRCODE_INVALID_CONFIGURATION, new String[]{}, WIDGET_CODE + ".parameters.invalid");
        }
    }

    protected boolean isMultilanguageParamValued(WidgetConfigurationRequest widget, String prefix) {
        ApsProperties config = widget.getConfig();
        if (null == config) {
            return false;
        }
        List<Lang> langs = this.getLangManager().getLangs();
        for (int i = 0; i < langs.size(); i++) {
            Lang lang = langs.get(i);
            String paramValue = config.getProperty(prefix + lang.getCode());
            if (null != paramValue && paramValue.trim().length() > 0) {
                return true;
            }
        }
        return false;
    }

    protected String extractConfigParam(WidgetConfigurationRequest widget, String key) {
        ApsProperties properties = widget.getConfig();
        if (null != properties) {
            return properties.getProperty(key);
        }
        return null;
    }

    protected IPage getPage(String pageCode) {
        return this.getPageManager().getDraftPage(pageCode);
    }

}
