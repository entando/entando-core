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
package org.entando.entando.aps.system.services.dataobject.api.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.agiletec.aps.system.common.entity.helper.BaseFilterUtils;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import org.entando.entando.aps.system.services.dataobject.helper.IContentListBean;

/**
 * @author E.Santoboni
 */
public class ApiDataObjectListBean implements IContentListBean {

	public ApiDataObjectListBean(String contentType, EntitySearchFilter[] filters, String[] categories) {
		this.setContentType(contentType);
		this.setCategories(categories);
		this.setFilters(filters);
	}

	public String getListName() {
		StringBuffer buffer = new StringBuffer("listName_api");
		buffer.append("-TYPE:" + this.getContentType());
		buffer.append("_FILTERS:");
		if (null != this.getFilters() && this.getFilters().length > 0) {
			BaseFilterUtils filterUtils = new BaseFilterUtils();
			buffer.append(filterUtils.getFilterParam(this.getFilters()));
		} else {
			buffer.append("NULL");
		}
		buffer.append("_CATEGORIES:");
		if (null != this.getCategories() && this.getCategories().length > 0) {
			List<String> categories = Arrays.asList(this.getCategories());
			Collections.sort(categories);
			for (int i = 0; i < categories.size(); i++) {
				if (i > 0) {
					buffer.append("+");
				}
				buffer.append(categories.get(i));
			}
		} else {
			buffer.append("NULL");
		}
		return buffer.toString();
	}

	public String getContentType() {
		return _contentType;
	}

	protected void setContentType(String contentType) {
		this._contentType = contentType;
	}

	public String[] getCategories() {
		return this._categories;
	}

	protected void setCategories(String[] categories) {
		this._categories = categories;
	}

	public EntitySearchFilter[] getFilters() {
		return this._filters;
	}

	protected void setFilters(EntitySearchFilter[] filters) {
		this._filters = filters;
	}

	public boolean isCacheable() {
		return true;
	}

	private String _contentType;
	private EntitySearchFilter[] _filters;
	private String[] _categories;

}
