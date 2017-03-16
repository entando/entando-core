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
package com.agiletec.plugins.jacms.apsadmin.content;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;

@SuppressWarnings("rawtypes")
public class ContentFinderSearchInfo {

	public static final String SESSION_NAME = "ContentFinderSearchInfo";
	public static final String ORDER_FILTER = "OrderFilter";
	public static final String ATTRIBUTE_FILTER = "attribute_filter_";


	private String pageName;
	private int pagePos;
	private Integer pageSize;
	private String categoryCode;

	private EntitySearchFilter[] attributeFilters;

	private Map<String, EntitySearchFilter> filters = new HashMap<String, EntitySearchFilter>();


	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public EntitySearchFilter[] getAttributeFilters() {
		return attributeFilters;
	}
	public void setAttributeFilters(EntitySearchFilter[] attributeFilters) {
		this.attributeFilters = attributeFilters;
	}

	public int getPagePos() {
		return pagePos;
	}
	public void setPagePos(int pagePos) {
		this.pagePos = pagePos;
	}

	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public Map<String, EntitySearchFilter> getFilters() {
		return filters;
	}
	public void setFilters(Map<String, EntitySearchFilter> filters) {
		this.filters = filters;
	}


	public void addFilter(String key, EntitySearchFilter filterToAdd) {
		this.filters.put(key, filterToAdd);
	}

	public EntitySearchFilter getFilter(String key) {
		return this.filters.get(key);
	}

	public void addAttributeFilters(EntitySearchFilter[] filtersToAdd) {
		if (null != filtersToAdd && filtersToAdd.length > 0) {
			for (int i = 0; i < filtersToAdd.length; i++) {
				EntitySearchFilter theFilter = filtersToAdd[i];
				this.addFilter(ATTRIBUTE_FILTER + theFilter.getKey(), theFilter);
			}
		}
	}

	public EntitySearchFilter[] getFiltersByKeyPrefix(String key) {
		EntitySearchFilter[] entitySearchFilters = null;
		List<EntitySearchFilter> list = new ArrayList<EntitySearchFilter>();
		for (String keyEntry : this.filters.keySet()) {
			if (keyEntry.startsWith(key)) {
				list.add (filters.get(keyEntry));
			}
		}

		if (!list.isEmpty()) {
			entitySearchFilters = list.toArray(new EntitySearchFilter[list.size()]);
		}
		return entitySearchFilters;
	}

	public void removeFilter(String contentStatusFilterKey) {
		this.filters.remove(contentStatusFilterKey);
	}

	public void removeFilterByPrefix(String key) {
		Map<String, EntitySearchFilter> newfilters = new HashMap<String, EntitySearchFilter>();
		for (String keyEntry : this.filters.keySet()) {
			if (!keyEntry.startsWith(key)) {
				newfilters.put(keyEntry, filters.get(keyEntry));
			}
		}
		this.filters = newfilters;
	}

	/**
	 * Try to set the page name and page position by choosing a parameter from the given parameters names enumeration
	 * @return true if a valid parameter is found and the values have been set
	 */
	public boolean setPagerFromParameters(Enumeration parameterNames) {
		boolean found = false;
		if (null == parameterNames) {
			return false;
		}
		String regexp = "(\\w.*)_(\\d.*)";
		Pattern pattern = Pattern.compile(regexp);
		while (parameterNames.hasMoreElements()) {
			String paramName = (String) parameterNames.nextElement();
			Matcher matcher = pattern.matcher(paramName);
			boolean matches = matcher.matches();
			if (matches) {
				found = true;
				this.setPageName(matcher.group(1));
				this.setPagePos(Integer.valueOf(matcher.group(2)));
			}
		}
		return found;
	}

}

