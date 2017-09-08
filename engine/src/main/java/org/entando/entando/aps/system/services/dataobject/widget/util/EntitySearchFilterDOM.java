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

import java.util.List;
import java.util.Properties;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import org.entando.entando.aps.system.services.dataobject.helper.IDataTypeListFilterBean;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;

/**
 * Provides utility methods for dataObject filters.
 *
 * @author E.Santoboni
 * @deprecated From Entando 2.0 version 2.4.1. Use {@link FilterUtils} methods
 */
public class EntitySearchFilterDOM {

	/**
	 * Return the showlet parameters in the form of property list
	 *
	 * @param showletParam The string to convert into a property list
	 * @return The property list.
	 * @deprecated Use {@link FilterUtils}
	 */
	public static List<Properties> getPropertiesFilters(String showletParam) {
		return FilterUtils.getFiltersProperties(showletParam);
	}

	@Deprecated
	public EntitySearchFilter[] getFilters(String dataObjectType, String showletParam, IDataObjectManager dataObjectManager, String langCode) {
		FilterUtils filterUtils = new FilterUtils();
		return filterUtils.getFilters(dataObjectManager.getEntityPrototype(dataObjectType), showletParam, langCode);
	}

	@Deprecated
	public EntitySearchFilter getFilter(String dataObjectType, IDataTypeListFilterBean bean, IDataObjectManager contentManager, String langCode) {
		FilterUtils filterUtils = new FilterUtils();
		return filterUtils.getFilter(contentManager.getEntityPrototype(dataObjectType), bean, langCode);
	}

	@Deprecated
	public String getShowletParam(EntitySearchFilter[] filters) {
		FilterUtils filterUtils = new FilterUtils();
		return filterUtils.getFilterParam(filters);
	}

	@Deprecated
	public static String getShowletParam(List<Properties> properties) {
		return FilterUtils.getShowletParam(properties);
	}

}
