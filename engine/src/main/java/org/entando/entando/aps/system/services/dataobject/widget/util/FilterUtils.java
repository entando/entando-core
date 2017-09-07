/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.dataobject.widget.util;

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
import org.entando.entando.aps.system.services.dataobject.widget.UserFilterOptionBean;
import org.entando.entando.aps.system.services.dataobject.helper.IDataTypeListFilterBean;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;

/**
 * Provides utility methods for dataObject filters for widget.
 *
 * @author E.Santoboni
 */
public class FilterUtils extends BaseFilterUtils {

	private static final Logger _logger = LoggerFactory.getLogger(FilterUtils.class);

	/**
	 * Return the showlet parameters in the form of property list
	 *
	 * @param filtersShowletParam The string to convert into a property list
	 * @return The property list.
	 */
	public static List<Properties> getFiltersProperties(String filtersShowletParam) {
		if (null == filtersShowletParam || filtersShowletParam.trim().length() == 0) {
			return new ArrayList<Properties>();
		}
		String[] filterStrings = filtersShowletParam.split("\\+");
		List<Properties> properties = new ArrayList<Properties>(filterStrings.length);
		for (int i = 0; i < filterStrings.length; i++) {
			String fullFilterString = filterStrings[i];
			String filterString = fullFilterString.substring(1, fullFilterString.length() - 1);
			Properties props = getProperties(filterString, DEFAULT_FILTER_PARAM_SEPARATOR);
			properties.add(props);
		}
		return properties;
	}

	@Deprecated
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
				String toStringFilter = fullFilterString.substring(1, fullFilterString.length() - 1);
				Properties properties = getProperties(toStringFilter, DEFAULT_FILTER_PARAM_SEPARATOR);
				UserFilterOptionBean filterBean = new UserFilterOptionBean(properties, prototype, currentFrame, currentLang, dateFormat, request);
				list.add(filterBean);
			} catch (Throwable t) {
				_logger.error("Error extracting user filter by string '{}' for type '{}'", fullFilterString, prototype.getTypeCode(), t);
			}
		}
		return list;
	}

	/**
	 * @deprecated From Entando 3.0 version 3.0.1. Use getUserFilter(String,
	 * IEntityFilterBean, IContentManager, RequestContext) method
	 */
	public UserFilterOptionBean getUserFilter(String dataObjectType,
			IDataTypeListFilterBean bean, IDataObjectManager dataObjectManager, RequestContext reqCtx) {
		return this.getUserFilter(dataObjectType, (IEntityFilterBean) bean, dataObjectManager, "dd/MM/yyyy", reqCtx);
	}

	/**
	 * @deprecated From Entando 3.0 version 3.2.0. Use getUserFilter(String
	 * contentType, IEntityFilterBean, IContentManager, String, RequestContext)
	 * method
	 */
	public UserFilterOptionBean getUserFilter(String dataObjectType,
			IEntityFilterBean bean, IDataObjectManager dataObjectManager, RequestContext reqCtx) {
		return getUserFilter(dataObjectType, bean, dataObjectManager, "dd/MM/yyyy", reqCtx);
	}

	public UserFilterOptionBean getUserFilter(String dataObjectType,
			IEntityFilterBean bean, IDataObjectManager dataObjectManager, String dateFormat, RequestContext reqCtx) {
		UserFilterOptionBean filter = null;
		try {
			IApsEntity prototype = dataObjectManager.createDataObject(dataObjectType);
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
	 * Crea il parametro di configurazione della showlet, caratteristico per la
	 * rappresentazione dei filtri. Il parametro viene ricavato in base alla
	 * lista di filtri specificati.
	 *
	 * @param filters I filtri applicati.
	 * @return Il parametro di configurazione della showlet.
	 * @deprecated use getFilterParam(EntitySearchFilter)
	 */
	public String getShowletParam(EntitySearchFilter[] filters) {
		return super.getFilterParam(filters);
	}

	/**
	 * Crea il parametro di configurazione della showlet, caratteristico per la
	 * rappresentazione dei filtri. Il parametro viene ricavato in base alla
	 * lista di properties specificata.
	 *
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
