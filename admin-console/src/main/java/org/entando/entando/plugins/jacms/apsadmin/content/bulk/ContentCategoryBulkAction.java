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
package org.entando.entando.plugins.jacms.apsadmin.content.bulk;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.entando.entando.aps.system.common.command.report.BulkCommandReport;
import org.entando.entando.aps.system.common.command.tracer.DefaultBulkCommandTracer;
import org.entando.entando.aps.system.services.command.IBulkCommandManager;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.category.JoinCategoryBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.category.RemoveCategoryBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.BaseContentPropertyBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.ContentPropertyBulkCommandContext;
import org.entando.entando.plugins.jacms.apsadmin.content.bulk.util.ContentBulkActionSummary;
import org.entando.entando.plugins.jacms.apsadmin.content.bulk.util.IContentBulkActionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.system.AbstractTreeAction;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.opensymphony.xwork2.Action;

public class ContentCategoryBulkAction extends AbstractTreeAction {

	private static final Logger _logger = LoggerFactory.getLogger(ContentCategoryBulkAction.class);

	public String entry() {
		return this.checkAllowedContents() ? this.buildTree() : "list";
	}

	public String join() {
		try {
			String categoryCode = this.getCategoryCode();
			Category category = this.getCategoryManager().getCategory(categoryCode);
			if (null != category && !category.isRoot()) {
				this.getCategoryCodes().add(categoryCode);
			}
		} catch (Throwable t) {
			_logger.error("Error joining category for bulk action", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String disjoin() {
		try {
			this.getCategoryCodes().remove(this.getCategoryCode());
		} catch (Throwable t) {
			_logger.error("Error removing category from bulk action", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String checkApply() {
		return this.checkCategories() ? SUCCESS : INPUT;
	}

	public String apply() {
		try {
			if (!this.checkAllowedContents()) {
				return "list";
			} else {
				List<Category> categories = this.getBulkActionHelper().getCategoriesToManage(this.getCategoryCodes(), this, this);
				if (categories == null) {
					return INPUT;
				} else {
					BaseContentPropertyBulkCommand<Category> command = this.initBulkCommand(categories);
					BulkCommandReport<String> report = this.getBulkCommandManager().addCommand(IContentBulkActionHelper.BULK_COMMAND_OWNER, command);
					this.setCommandId(report.getCommandId());
				}
			}
		} catch (Throwable t) {
			_logger.error("Error occurred applying categories (add/remove)", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	private BaseContentPropertyBulkCommand<Category> initBulkCommand(List<Category> categories) {
		String commandBeanName = ApsAdminSystemConstants.DELETE == this.getStrutsAction() ? 
				RemoveCategoryBulkCommand.BEAN_NAME : JoinCategoryBulkCommand.BEAN_NAME;
		WebApplicationContext applicationContext = ApsWebApplicationUtils.getWebApplicationContext(this.getRequest());
		BaseContentPropertyBulkCommand<Category> command = (BaseContentPropertyBulkCommand<Category>) applicationContext.getBean(commandBeanName);
		ContentPropertyBulkCommandContext<Category> context = new ContentPropertyBulkCommandContext<Category>(this.getSelectedIds(), 
				categories, this.getCurrentUser(), new DefaultBulkCommandTracer<String>());
		command.init(context);
		return command;
	}

	public ContentBulkActionSummary getSummary() {
		return this.getBulkActionHelper().getSummary(this.getSelectedIds());
	}

	protected boolean checkAllowedContents() {
		return this.getBulkActionHelper().checkAllowedContents(this.getSelectedIds(), this, this);
	}

	protected boolean checkCategories() {
		return this.getBulkActionHelper().checkCategories(this.getCategoryCodes(), this, this);
	}

	public Category getCategory(String categoryCode) {
		return this.getCategoryManager().getCategory(categoryCode);
	}

	public Category getCategoryRoot() {
		return (Category) this.getCategoryManager().getRoot();
	}

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

	public Set<String> getSelectedIds() {
		return _selectedIds;
	}
	public void setSelectedIds(Set<String> selectedIds) {
		this._selectedIds = selectedIds;
	}

	public int getStrutsAction() {
		return _strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}

	public String getCommandId() {
		return _commandId;
	}
	public void setCommandId(String commandId) {
		this._commandId = commandId;
	}

	public Set<String> getCategoryCodes() {
		return _categoryCodes;
	}
	public void setCategoryCodes(Set<String> categoryCodes) {
		this._categoryCodes = categoryCodes;
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

	protected IBulkCommandManager getBulkCommandManager() {
		return _bulkCommandManager;
	}
	public void setBulkCommandManager(IBulkCommandManager bulkCommandManager) {
		this._bulkCommandManager = bulkCommandManager;
	}

	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}

	protected IContentBulkActionHelper getBulkActionHelper() {
		return _bulkActionHelper;
	}
	public void setBulkActionHelper(IContentBulkActionHelper bulkActionHelper) {
		this._bulkActionHelper = bulkActionHelper;
	}

	private Set<String> _selectedIds;

	private int _strutsAction;
	private String _commandId;

	private Set<String> _categoryCodes = new TreeSet<String>();
	private String _categoryCode;
	private ICategoryManager _categoryManager;

	private IBulkCommandManager _bulkCommandManager;
	private IContentManager _contentManager;
	private IContentBulkActionHelper _bulkActionHelper;

}
