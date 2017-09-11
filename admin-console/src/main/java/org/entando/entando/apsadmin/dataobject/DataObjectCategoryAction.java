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
package org.entando.entando.apsadmin.dataobject;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.apsadmin.system.AbstractTreeAction;
import com.opensymphony.xwork2.Action;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.apsadmin.dataobject.helper.IDataObjectActionHelper;

/**
 * Action class that manages the category tree operation on DataObject finding
 * GUI interface and the relationships between DataObject and categories.
 *
 * @author E.Santoboni
 */
public class DataObjectCategoryAction extends AbstractTreeAction {

	private static final Logger _logger = LoggerFactory.getLogger(DataObjectCategoryAction.class);

	@Override
	public String buildTree() {
		try {
			String result = super.buildTree();
			if (!result.equals(Action.SUCCESS)) {
				return result;
			}
			Set<String> targets = this.getTreeNodesToOpen();
			String marker = this.getTreeNodeActionMarkerCode();
			if (null == marker && null != this.getCategoryCode() && !targets.contains(this.getCategoryCode())) {
				targets.add(this.getCategoryCode());
			}
		} catch (Throwable t) {
			_logger.error("error in buildTree", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Performs the action of adding of a category to the DataObject.
	 *
	 * @return The result code.
	 */
	public String joinCategory() {
		this.updateDataObjectOnSession();
		try {
			String categoryCode = this.getCategoryCode();
			Category category = this.getCategoryManager().getCategory(categoryCode);
			if (null != category && !category.getCode().equals(category.getParentCode())
					&& !this.getDataObject().getCategories().contains(category)) {
				this.getDataObject().addCategory(category);
			}
		} catch (Throwable t) {
			_logger.error("error in joinCategory", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Performs the action of removing a category from the DataObject.
	 *
	 * @return The result code.
	 */
	public String removeCategory() {
		this.updateDataObjectOnSession();
		try {
			String categoryCode = this.getCategoryCode();
			Category category = this.getCategoryManager().getCategory(categoryCode);
			if (null != category) {
				this.getDataObject().removeCategory(category);
			}
		} catch (Throwable t) {
			_logger.error("error in removeCategory", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public Category getCategoryRoot() {
		return (Category) this.getCategoryManager().getRoot();
	}

	public DataObject getDataObject() {
		return (DataObject) this.getRequest().getSession()
				.getAttribute(DataObjectActionConstants.SESSION_PARAM_NAME_CURRENT_DATA_OBJECT_PREXIX + this.getContentOnSessionMarker());
	}

	protected DataObject updateDataObjectOnSession() {
		DataObject content = this.getDataObject();
		this.getDataObjectActionHelper().updateEntity(content, this.getRequest());
		return content;
	}

	public String getContentOnSessionMarker() {
		return _contentOnSessionMarker;
	}

	public void setContentOnSessionMarker(String contentOnSessionMarker) {
		this._contentOnSessionMarker = contentOnSessionMarker;
	}

	public String getCategoryCode() {
		return _categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this._categoryCode = categoryCode;
	}

	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}

	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}

	public IDataObjectActionHelper getDataObjectActionHelper() {
		return _dataObjectActionHelper;
	}

	public void setDataObjectActionHelper(IDataObjectActionHelper dataObjectActionHelper) {
		this._dataObjectActionHelper = dataObjectActionHelper;
	}

	private String _contentOnSessionMarker;

	private String _categoryCode;
	private ICategoryManager _categoryManager;

	private IDataObjectActionHelper _dataObjectActionHelper;

}
