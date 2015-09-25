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
package com.agiletec.plugins.jacms.aps.system.services.content.widget;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;

/**
 * @author E.Santoboni
 */
public class MockContentListTagBean implements IContentListTagBean {
	
	@Override
	public String getListName() {
		return _listName;
	}
	public void setListName(String listName) {
		this._listName = listName;
	}
	
	@Override
	public String getContentType() {
		return _contentType;
	}
	@Override
	public void setContentType(String contentType) {
		this._contentType = contentType;
	}
	
	@Override
	public String[] getCategories() {
		return _categories;
	}
	public void setCategories(String[] categories) {
		this._categories = categories;
	}
	
	@Override
	public EntitySearchFilter[] getFilters() {
		return _filters;
	}
	public void setFilters(EntitySearchFilter[] filters) {
		this._filters = filters;
	}
	
	@Override
	public String getCategory() {
		return _category;
	}
	@Override
	public void setCategory(String category) {
		this._category = category;
	}
	
	@Override
	public boolean isCacheable() {
		return true;
	}
	
	@Override
	public void addCategory(String category) {
		int len = this._categories.length;
		String[] newCategories = new String[len + 1];
		for(int i=0; i < len; i++){
			newCategories[i] = this._categories[i];
		}
		newCategories[len] = category;
		this._categories = newCategories;
	}
	
	@Override
	public void addFilter(EntitySearchFilter filter) {
		int len = this._filters.length;
		EntitySearchFilter[] newFilters = new EntitySearchFilter[len + 1];
		for(int i=0; i < len; i++){
			newFilters[i] = this._filters[i];
		}
		newFilters[len] = filter;
		this._filters = newFilters;
	}
	@Override
	public void addUserFilterOption(UserFilterOptionBean filter) {
		if (null == filter) return;
		if (null == this.getUserFilterOptions()) {
			this.setUserFilterOptions(new ArrayList<UserFilterOptionBean>());
		}
		this.getUserFilterOptions().add(filter);
	}
	
	@Override
	public List<UserFilterOptionBean> getUserFilterOptions() {
		return _userFilterOptions;
	}
	public void setUserFilterOptions(List<UserFilterOptionBean> userFilterOptions) {
		this._userFilterOptions = userFilterOptions;
	}
	
	private String _listName;
	private String _contentType;
	private String[] _categories = new String[0];
	private EntitySearchFilter[] _filters = new EntitySearchFilter[0];
	private String _category;
	private List<UserFilterOptionBean> _userFilterOptions = new ArrayList<UserFilterOptionBean>();
	
}