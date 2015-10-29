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

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.apsadmin.system.AbstractTreeAction;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.helper.IContentActionHelper;
import com.opensymphony.xwork2.Action;

/**
 * Action class that manages the category tree operation on content finding GUI interface and the relationships between content and categories.
 * @author E.Santoboni
 */
public class ContentCategoryAction extends AbstractTreeAction {

	private static final Logger _logger = LoggerFactory.getLogger(ContentCategoryAction.class);
	
	@Override
	public String buildTree() {
		try {
			String result = super.buildTree();
			if (!result.equals(Action.SUCCESS)) return result;
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
	 * Performs the action of adding of a category to the content.
	 * @return The result code.
	 */
	public String joinCategory() {
		this.updateContentOnSession();
		try {
			String categoryCode = this.getCategoryCode();
			Category category = this.getCategoryManager().getCategory(categoryCode);
			if (null != category && !category.getCode().equals(category.getParentCode()) 
					&& !this.getContent().getCategories().contains(category)) { 
				this.getContent().addCategory(category);
			}
		} catch (Throwable t) {
			_logger.error("error in joinCategory", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Performs the action of removing a category from the content.
	 * @return The result code.
	 */
	public String removeCategory() {
		this.updateContentOnSession();
		try {
			String categoryCode = this.getCategoryCode();
			Category category = this.getCategoryManager().getCategory(categoryCode);
			if (null != category) {
				this.getContent().removeCategory(category);
			}
		} catch (Throwable t) {
			_logger.error("error in removeCategory", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public Content getContent() {
		return (Content) this.getRequest().getSession()
				.getAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + this.getContentOnSessionMarker());
	}
	
	protected Content updateContentOnSession() {
		Content content = this.getContent();
		this.getContentActionHelper().updateEntity(content, this.getRequest());
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
	
	protected IContentActionHelper getContentActionHelper() {
		return _contentActionHelper;
	}
	public void setContentActionHelper(IContentActionHelper contentActionHelper) {
		this._contentActionHelper = contentActionHelper;
	}
	
	private String _contentOnSessionMarker;
	
	private String _categoryCode;
	private ICategoryManager _categoryManager;
	
	private IContentActionHelper _contentActionHelper;
	
}