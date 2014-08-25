/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jacms.aps.system.services.content.widget.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.helper.BaseFilterUtils;
import com.agiletec.aps.system.common.entity.helper.IEntityFilterBean;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.IContentListFilterBean;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.UserFilterOptionBean;

/**
 * Provides utility methods for content filters for showlet.
 * @author E.Santoboni
 */
public class FilterUtils extends BaseFilterUtils {

	private static final Logger _logger = LoggerFactory.getLogger(FilterUtils.class);
	
	/**
	 * Return the showlet parameters in the form of property list
	 * @param filtersShowletParam The string to convert into a property list
	 * @return The property list.
	 */
	public static List<Properties> getFiltersProperties(String filtersShowletParam) {
		if (null == filtersShowletParam || filtersShowletParam.trim().length() == 0) {
			return new ArrayList<Properties>();
		}
		String[] filterStrings = filtersShowletParam.split("\\+");
		List<Properties> properties = new ArrayList<Properties>(filterStrings.length);
		for (int i=0; i<filterStrings.length; i++) {
			String fullFilterString = filterStrings[i];
			String filterString = fullFilterString.substring(1, fullFilterString.length()-1);
			Properties props = getProperties(filterString, DEFAULT_FILTER_PARAM_SEPARATOR);
			properties.add(props);
		}
		return properties;
	}
	
	/**
	 * @deprecated From Entando 3.0 version 3.2.0. Use getUserFilters(String, Integer, Lang, IApsEntity, String, HttpServletRequest) method
	 */
	public static List<UserFilterOptionBean> getUserFilters(String userFiltersParam, 
			Integer currentFrame, Lang currentLang, IApsEntity prototype, HttpServletRequest request) {
		return getUserFilters(userFiltersParam, currentFrame, currentLang, prototype, "dd/MM/yyyy", request);
	}
	
	public static List<UserFilterOptionBean> getUserFilters(String userFiltersParam, 
			Integer currentFrame, Lang currentLang, IApsEntity prototype, String dateFormat, HttpServletRequest request) {
		if (null == userFiltersParam) {
			return new ArrayList<UserFilterOptionBean>();
		}
		List<UserFilterOptionBean> list = new ArrayList<UserFilterOptionBean>();
		String[] filterStrings = userFiltersParam.split("\\+");
		for (int i = 0; i < filterStrings.length; i++) {
			String fullFilterString = filterStrings[i];
			try {
				String toStringFilter = fullFilterString.substring(1, fullFilterString.length()-1);
				Properties properties = getProperties(toStringFilter, DEFAULT_FILTER_PARAM_SEPARATOR);
				UserFilterOptionBean filterBean = new UserFilterOptionBean(properties, prototype, currentFrame, currentLang, dateFormat, request);
				list.add(filterBean);
			} catch (Throwable t) {
				_logger.error("Error extracting user filter by string '{}' for type '{}'", fullFilterString, prototype.getTypeCode(), t);
				//ApsSystemUtils.logThrowable(t, FilterUtils.class, "getUserFilters", "Error extracting user filter by string '" + fullFilterString + "' for type '" + prototype.getTypeCode() + "'");
			}
		}
		return list;
	}
	
	/**
	 * @deprecated From Entando 3.0 version 3.0.1. Use getUserFilter(String, IEntityFilterBean, IContentManager, RequestContext) method
	 */
	public UserFilterOptionBean getUserFilter(String contentType, 
			IContentListFilterBean bean, IContentManager contentManager, RequestContext reqCtx) {
		return this.getUserFilter(contentType, (IEntityFilterBean) bean, contentManager, "dd/MM/yyyy", reqCtx);
	}
	
	/**
	 * @deprecated From Entando 3.0 version 3.2.0. Use getUserFilter(String contentType, IEntityFilterBean, IContentManager, String, RequestContext) method
	 */
	public UserFilterOptionBean getUserFilter(String contentType, 
			IEntityFilterBean bean, IContentManager contentManager, RequestContext reqCtx) {
		return getUserFilter(contentType, bean, contentManager, "dd/MM/yyyy", reqCtx);
	}
	
	public UserFilterOptionBean getUserFilter(String contentType, 
			IEntityFilterBean bean, IContentManager contentManager, String dateFormat, RequestContext reqCtx) {
		UserFilterOptionBean filter = null;
		try {
			IApsEntity prototype = contentManager.createContentType(contentType);
			Properties props = new Properties();
			props.setProperty(UserFilterOptionBean.PARAM_KEY, bean.getKey());
			props.setProperty(UserFilterOptionBean.PARAM_IS_ATTRIBUTE_FILTER, String.valueOf(bean.isAttributeFilter()));
			Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
			Integer currentFrame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
			filter = new UserFilterOptionBean(props, prototype, currentFrame, currentLang, dateFormat, reqCtx.getRequest());
		} catch (Throwable t) {
			_logger.error("Error creating user filter", t);
			//ApsSystemUtils.logThrowable(t, FilterUtils.class, "getUserFilter", "Error creating user filter");
		}
		return filter;
	}
	
	/**
	 * Crea il parametro di configurazione della showlet, caratteristico per la rappresentazione dei filtri.
	 * Il parametro viene ricavato in base alla lista di filtri specificati.
	 * @param filters I filtri applicati.
	 * @return Il parametro di configurazione della showlet.
	 * @deprecated use getFilterParam(EntitySearchFilter)
	 */
	public String getShowletParam(EntitySearchFilter[] filters) {
		return super.getFilterParam(filters);
	}
	
	/**
	 * Crea il parametro di configurazione della showlet, caratteristico per la rappresentazione dei filtri.
	 * Il parametro viene ricavato in base alla lista di properties specificata.
	 * @param properties Le properties rappresentanti ciascuna un filtro.
	 * @return Il parametro di configurazione della showlet.
	 */
	public static String getShowletParam(List<Properties> properties) {
		return getShowletParam(properties, DEFAULT_FILTER_PARAM_SEPARATOR);
	}
	
	public static String getShowletParam(List<Properties> properties, String separator) {
		return getToStringFilterParam(properties, separator);
	}
	
}