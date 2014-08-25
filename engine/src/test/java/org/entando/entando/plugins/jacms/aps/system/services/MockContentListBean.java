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
package org.entando.entando.plugins.jacms.aps.system.services;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.IContentListBean;

/**
 * @author E.Santoboni
 */
public class MockContentListBean implements IContentListBean {
	
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
	public boolean isCacheable() {
		return true;
	}
	
	public void addCategory(String category) {
		int len = this._categories.length;
		String[] newCategories = new String[len + 1];
		for(int i=0; i < len; i++){
			newCategories[i] = this._categories[i];
		}
		newCategories[len] = category;
		this._categories = newCategories;
	}
	
	public void addFilter(EntitySearchFilter filter) {
		int len = this._filters.length;
		EntitySearchFilter[] newFilters = new EntitySearchFilter[len + 1];
		for(int i=0; i < len; i++){
			newFilters[i] = this._filters[i];
		}
		newFilters[len] = filter;
		this._filters = newFilters;
	}
	
	private String _listName;
	private String _contentType;
	private String[] _categories = new String[0];
	private EntitySearchFilter[] _filters = new EntitySearchFilter[0];
	
}