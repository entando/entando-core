/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General  License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General  License for more
 * details.
 */
package org.entando.entando.aps.system.services.dataobject.widget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.ListUtils;
import org.entando.entando.aps.system.services.searchengine.IEntitySearchEngineManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.helper.IEntityFilterBean;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import org.entando.entando.aps.system.services.dataobject.helper.BaseDataListHelper;
import org.entando.entando.aps.system.services.dataobject.widget.util.FilterUtils;
import org.entando.entando.aps.system.services.dataobject.helper.IDataTypeListFilterBean;

/**
 * Classe helper per la widget di erogazione contenuti in lista.
 *
 * @author E.Santoboni
 */
public class DataObjectListHelper extends BaseDataListHelper implements IDataObjectListWidgetHelper {

    private static final Logger _logger = LoggerFactory.getLogger(DataObjectListHelper.class);

    @Override
    public EntitySearchFilter[] getFilters(String dataObjectType, String filtersShowletParam, RequestContext reqCtx) {
        Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
        return super.getFilters(dataObjectType, filtersShowletParam, currentLang.getCode());
    }

    /**
     * @deprecated From Entando 3.0 version 3.0.1. Use getFilter(String,
     * IEntityFilterBean, RequestContext) method
     */
    @Override
    public EntitySearchFilter getFilter(String dataObjectType, IDataTypeListFilterBean bean, RequestContext reqCtx) {
        return this.getFilter(dataObjectType, (IEntityFilterBean) bean, reqCtx);
    }

    @Override
    public EntitySearchFilter getFilter(String dataObjectType, IEntityFilterBean bean, RequestContext reqCtx) {
        Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
        return super.getFilter(dataObjectType, bean, currentLang.getCode());
    }

    /**
     * @deprecated From Entando 3.0 version 3.0.1. Use
     * getUserFilterOption(String, IEntityFilterBean, RequestContext) method
     */
    @Override
    public UserFilterOptionBean getUserFilterOption(String dataObjectType, IDataTypeListFilterBean bean, RequestContext reqCtx) {
        return this.getUserFilterOption(dataObjectType, (IEntityFilterBean) bean, reqCtx);
    }

    @Override
    public UserFilterOptionBean getUserFilterOption(String dataObjectType, IEntityFilterBean bean, RequestContext reqCtx) {
        FilterUtils filterUtils = new FilterUtils();
        return filterUtils.getUserFilter(dataObjectType, bean, this.getDataObjectManager(), this.getUserFilterDateFormat(), reqCtx);
    }

    @Override
    @Deprecated
    public String getShowletParam(EntitySearchFilter[] filters) {
        return super.getFilterParam(filters);
    }

    @Override
    //@Cacheable(value = ICacheInfoManager.DEFAULT_CACHE_NAME,
    //        key = "T(com.agiletec.plugins.jacms.aps.system.services.content.widget.ContentListHelper).buildCacheKey(#bean, #reqCtx)",
    //        condition = "#bean.cacheable && !T(com.agiletec.plugins.jacms.aps.system.services.content.widget.ContentListHelper).isUserFilterExecuted(#bean)")
    //@CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME,
    //        key = "T(com.agiletec.plugins.jacms.aps.system.services.content.widget.ContentListHelper).buildCacheKey(#bean, #reqCtx)",
    //        beforeInvocation = true,
    //        condition = "T(org.entando.entando.aps.system.services.cache.CacheInfoManager).isExpired(T(com.agiletec.plugins.jacms.aps.system.services.content.widget.ContentListHelper).buildCacheKey(#bean, #reqCtx))")
    //@CacheableInfo(groups = "T(com.agiletec.plugins.jacms.aps.system.services.cache.CmsCacheWrapperManager).getContentListCacheGroupsCsv(#bean, #reqCtx)", expiresInMinute = 30)
    public List<String> getContentsId(IDataObjectListTagBean bean, RequestContext reqCtx) throws Throwable {
        List<String> dataObjectsId = null;
        try {
            dataObjectsId = this.extractContentsId(bean, reqCtx);
            dataObjectsId = this.executeFullTextSearch(bean, dataObjectsId, reqCtx);
        } catch (Throwable t) {
            _logger.error("Error extracting dataObjects id", t);
            throw new ApsSystemException("Error extracting dataObjects id", t);
        }
        return dataObjectsId;
    }

    public static boolean isUserFilterExecuted(IDataObjectListTagBean bean) {
        if (null == bean || null == bean.getUserFilterOptions() || bean.getUserFilterOptions().isEmpty()) {
            return false;
        }
        for (int i = 0; i < bean.getUserFilterOptions().size(); i++) {
            UserFilterOptionBean userFilter = bean.getUserFilterOptions().get(i);
            if (null != userFilter.getFormFieldValues() && userFilter.getFormFieldValues().size() > 0) {
                return true;
            }
        }
        return false;
    }

    protected List<String> extractContentsId(IDataObjectListTagBean bean, RequestContext reqCtx) throws ApsSystemException {
        List<String> contentsId = null;
        try {
            List<UserFilterOptionBean> userFilters = bean.getUserFilterOptions();
            Widget widget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
            ApsProperties config = (null != widget) ? widget.getConfig() : null;
            if (null == bean.getDataType() && null != config) {
                bean.setContentType(config.getProperty(WIDGET_PARAM_CONTENT_TYPE));
            }
            if (null == bean.getDataType()) {
                throw new ApsSystemException("Tipo contenuto non definito");
            }
            if (null == bean.getCategory() && null != config && null != config.getProperty(SHOWLET_PARAM_CATEGORY)) {
                bean.setCategory(config.getProperty(SHOWLET_PARAM_CATEGORY));
            }
            this.addWidgetFilters(bean, config, WIDGET_PARAM_FILTERS, reqCtx);
            if (null != userFilters && userFilters.size() > 0) {
                for (int i = 0; i < userFilters.size(); i++) {
                    UserFilterOptionBean userFilter = userFilters.get(i);
                    EntitySearchFilter filter = userFilter.getEntityFilter();
                    if (null != filter) {
                        bean.addFilter(filter);
                    }
                }
            }
            String[] categories = this.getCategories(bean.getCategories(), config, userFilters);
            Collection<String> userGroupCodes = this.getAllowedGroups(reqCtx);
            boolean orCategoryFilterClause = this.extractOrCategoryFilterClause(config);
            contentsId = this.getDataObjectManager().loadDataObjectsId(bean.getDataType(),
                    categories, orCategoryFilterClause, bean.getFilters(), userGroupCodes);
        } catch (Throwable t) {
            _logger.error("Error extracting contents id", t);
            throw new ApsSystemException("Error extracting contents id", t);
        }
        return contentsId;
    }

    protected boolean extractOrCategoryFilterClause(ApsProperties config) {
        if (null == config) {
            return false;
        }
        String param = config.getProperty(WIDGET_PARAM_OR_CLAUSE_CATEGORY_FILTER);
        if (null == param) {
            return false;
        }
        return Boolean.parseBoolean(param);
    }

    protected List<String> executeFullTextSearch(IDataObjectListTagBean bean,
            List<String> masterContentsId, RequestContext reqCtx) throws ApsSystemException {
        UserFilterOptionBean fullTextUserFilter = null;
        List<UserFilterOptionBean> userFilterOptions = bean.getUserFilterOptions();
        if (null != userFilterOptions) {
            for (int i = 0; i < userFilterOptions.size(); i++) {
                UserFilterOptionBean userFilter = userFilterOptions.get(i);
                if (null != userFilter.getFormFieldValues() && userFilter.getFormFieldValues().size() > 0) {
                    if (!userFilter.isAttributeFilter()
                            && userFilter.getKey().equals(UserFilterOptionBean.KEY_FULLTEXT)) {
                        fullTextUserFilter = userFilter;
                    }
                }
            }
        }
        if (fullTextUserFilter != null && null != fullTextUserFilter.getFormFieldValues()) {
            String word = fullTextUserFilter.getFormFieldValues().get(fullTextUserFilter.getFormFieldNames()[0]);
            /*
			String optionString = fullTextUserFilter.getFormFieldValues().get(fullTextUserFilter.getFormFieldNames()[1]);
			SearchEngineFilter.TextSearchOption option = SearchEngineFilter.TextSearchOption.AT_LEAST_ONE_WORD;
			if (null != optionString) {
				if (optionString.equals(UserFilterOptionBean.FULLTEXT_OPTION_ALL_WORDS)) {
					option = SearchEngineFilter.TextSearchOption.ALL_WORDS;
				} else if (optionString.equals(UserFilterOptionBean.FULLTEXT_OPTION_EXACT)) {
					option = SearchEngineFilter.TextSearchOption.EXACT;
				}
			}
             */
            Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
            /*
			SearchEngineFilter filter = new SearchEngineFilter(currentLang.getCode(), word, option);
			SearchEngineFilter[] filters = {filter};
			List<String> fullTextResult = this.getSearchEngineManager().searchEntityId(filters, null, this.getAllowedGroups(reqCtx));
             */
            List<String> fullTextResult = this.getSearchEngineManager().searchEntityId(currentLang.getCode(), word, this.getAllowedGroups(reqCtx));
            if (null != fullTextResult) {
                return ListUtils.intersection(fullTextResult, masterContentsId);
            } else {
                return new ArrayList<String>();
            }
        } else {
            return masterContentsId;
        }
    }

    protected String[] getCategories(String[] categories, ApsProperties config, List<UserFilterOptionBean> userFilters) {
        Set<String> codes = new HashSet<String>();
        if (null != categories) {
            for (int i = 0; i < categories.length; i++) {
                codes.add(categories[i]);
            }
        }
        String categoriesParam = (null != config) ? config.getProperty(WIDGET_PARAM_CATEGORIES) : null;
        if (null != categoriesParam && categoriesParam.trim().length() > 0) {
            List<String> categoryCodes = splitValues(categoriesParam, CATEGORIES_SEPARATOR);
            for (int j = 0; j < categoryCodes.size(); j++) {
                codes.add(categoryCodes.get(j));
            }
        }
        if (null != userFilters) {
            for (int i = 0; i < userFilters.size(); i++) {
                UserFilterOptionBean userFilterBean = userFilters.get(i);
                if (!userFilterBean.isAttributeFilter()
                        && userFilterBean.getKey().equals(UserFilterOptionBean.KEY_CATEGORY)
                        && null != userFilterBean.getFormFieldValues()) {
                    codes.add(userFilterBean.getFormFieldValues().get(userFilterBean.getFormFieldNames()[0]));
                }
            }
        }
        if (codes.isEmpty()) {
            return null;
        }
        String[] categoryCodes = new String[codes.size()];
        Iterator<String> iter = codes.iterator();
        int i = 0;
        while (iter.hasNext()) {
            categoryCodes[i++] = iter.next();
        }
        return categoryCodes;
    }

    @Deprecated
    protected void addShowletFilters(IDataObjectListTagBean bean, ApsProperties showletParams, String showletParamName, RequestContext reqCtx) {
        this.addWidgetFilters(bean, showletParams, showletParamName, reqCtx);
    }

    protected void addWidgetFilters(IDataObjectListTagBean bean, ApsProperties widgetParams, String widgetParamName, RequestContext reqCtx) {
        if (null == widgetParams) {
            return;
        }
        String widgetFilters = widgetParams.getProperty(widgetParamName);
        EntitySearchFilter[] filters = this.getFilters(bean.getDataType(), widgetFilters, reqCtx);
        if (null == filters) {
            return;
        }
        for (int i = 0; i < filters.length; i++) {
            bean.addFilter(filters[i]);
        }
    }

    @Deprecated
    protected List<String> getContentsId(IDataObjectListTagBean bean, String[] categories, RequestContext reqCtx) throws Throwable {
        return this.getContentsId(bean, reqCtx);
    }

    protected Collection<String> getAllowedGroups(RequestContext reqCtx) {
        UserDetails currentUser = (UserDetails) reqCtx.getRequest().getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
        return getAllowedGroupCodes(currentUser);
    }

    public static String buildCacheKey(IDataObjectListTagBean bean, RequestContext reqCtx) {
        UserDetails currentUser = (UserDetails) reqCtx.getRequest().getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
        Collection<String> userGroupCodes = getAllowedGroupCodes(currentUser);
        return buildCacheKey(bean.getListName(), userGroupCodes, reqCtx);
    }

    protected static String buildCacheKey(String listName, Collection<String> userGroupCodes, RequestContext reqCtx) {
        IPage page = (null != reqCtx) ? (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE) : null;
        StringBuilder cacheKey = (null != page) ? new StringBuilder(page.getCode()) : new StringBuilder("NOTFOUND");
        Widget currentWidget = (null != reqCtx) ? (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET) : null;
        if (null != currentWidget && null != currentWidget.getType()) {
            cacheKey.append("_").append(currentWidget.getType().getCode());
        }
        if (null != reqCtx) {
            Integer frame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
            if (null != frame) {
                cacheKey.append("_").append(frame.intValue());
            }
            Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
            if (null != currentLang) {
                cacheKey.append("_LANG").append(currentLang.getCode()).append("_");
            }
        }
        List<String> groupCodes = new ArrayList<String>(userGroupCodes);
        if (!groupCodes.contains(Group.FREE_GROUP_NAME)) {
            groupCodes.add(Group.FREE_GROUP_NAME);
        }
        Collections.sort(groupCodes);
        for (int i = 0; i < groupCodes.size(); i++) {
            String code = (String) groupCodes.get(i);
            cacheKey.append("_").append(code);
        }
        if (null != currentWidget && null != currentWidget.getConfig()) {
            List<String> paramKeys = new ArrayList(currentWidget.getConfig().keySet());
            Collections.sort(paramKeys);
            for (int i = 0; i < paramKeys.size(); i++) {
                if (i == 0) {
                    cacheKey.append("_WIDGETPARAM");
                } else {
                    cacheKey.append(",");
                }
                String paramkey = (String) paramKeys.get(i);
                cacheKey.append(paramkey).append("=").append(currentWidget.getConfig().getProperty(paramkey));
            }
        }
        if (null != listName) {
            cacheKey.append("_LISTNAME").append(listName);
        }
        return cacheKey.toString();
    }

    @Override
    public List<UserFilterOptionBean> getConfiguredUserFilters(IDataObjectListTagBean bean, RequestContext reqCtx) throws ApsSystemException {
        List<UserFilterOptionBean> userEntityFilters = null;
        try {
            Widget widget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
            ApsProperties config = (null != widget) ? widget.getConfig() : null;
            if (null == config || null == config.getProperty(WIDGET_PARAM_CONTENT_TYPE)) {
                return null;
            }
            String dataObjectTypeCode = config.getProperty(WIDGET_PARAM_CONTENT_TYPE);
            IApsEntity prototype = this.getDataObjectManager().getEntityPrototype(dataObjectTypeCode);
            if (null == prototype) {
                _logger.error("Null dataObject type by code '{}'", dataObjectTypeCode);
                return null;
            }
            Integer currentFrame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
            Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
            String userFilters = config.getProperty(WIDGET_PARAM_USER_FILTERS);
            if (null != userFilters && userFilters.length() > 0) {
                userEntityFilters = FilterUtils.getUserFilters(userFilters, currentFrame, currentLang, prototype, this.getUserFilterDateFormat(), reqCtx.getRequest());
            }
        } catch (Throwable t) {
            _logger.error("Error extracting user filters", t);
            throw new ApsSystemException("Error extracting user filters", t);
        }
        return userEntityFilters;
    }

    protected String getUserFilterDateFormat() {
        return _userFilterDateFormat;
    }

    public void setUserFilterDateFormat(String userFilterDateFormat) {
        this._userFilterDateFormat = userFilterDateFormat;
    }

    protected IEntitySearchEngineManager getSearchEngineManager() {
        return _searchEngineManager;
    }

    public void setSearchEngineManager(IEntitySearchEngineManager searchEngineManager) {
        this._searchEngineManager = searchEngineManager;
    }

    private String _userFilterDateFormat;

    private IEntitySearchEngineManager _searchEngineManager;

}
