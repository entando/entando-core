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

import java.util.Set;

import org.entando.entando.aps.system.common.command.BaseBulkCommand;
import org.entando.entando.aps.system.common.command.report.BulkCommandReport;
import org.entando.entando.aps.system.common.command.tracer.DefaultBulkCommandTracer;
import org.entando.entando.aps.system.services.command.IBulkCommandManager;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.DeleteContentBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.InsertOnlineContentBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.RemoveOnlineContentBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.BaseContentBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.ContentBulkCommandContext;
import org.entando.entando.plugins.jacms.apsadmin.content.bulk.util.ContentBulkActionSummary;
import org.entando.entando.plugins.jacms.apsadmin.content.bulk.util.IContentBulkActionHelper;
import org.entando.entando.plugins.jacms.apsadmin.content.bulk.util.SmallBulkCommandReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;

import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;

public class ContentBulkAction extends BaseAction {

	private static final Logger _logger = LoggerFactory.getLogger(ContentBulkAction.class);

	public String entry() {
		return this.checkAllowedContents() ? SUCCESS : "list";
	}

	public String applyOnline() {
		return this.apply(InsertOnlineContentBulkCommand.BEAN_NAME);
	}

	public String applyOffline() {
		return this.apply(RemoveOnlineContentBulkCommand.BEAN_NAME);
	}

	public String applyRemove() {
		return this.apply(DeleteContentBulkCommand.BEAN_NAME);
	}

	public String apply(String commandBeanName) {
		try {
			if (!this.checkAllowedContents()) {
				return "list";
			} else {
				BaseContentBulkCommand<ContentBulkCommandContext> command = this.initBulkCommand(commandBeanName);
				BulkCommandReport<String> report = this.getBulkCommandManager().addCommand(this.getCommandOwner(), command);
				this.setCommandId(report.getCommandId());
			}
		} catch (Throwable t) {
			_logger.error("Error occurred applying command {}", commandBeanName, t);
			return FAILURE;
		}
		return SUCCESS;
	}

	protected BaseContentBulkCommand<ContentBulkCommandContext> initBulkCommand(String commandBeanName) {
		WebApplicationContext applicationContext = ApsWebApplicationUtils.getWebApplicationContext(this.getRequest());
		BaseContentBulkCommand<ContentBulkCommandContext> command = (BaseContentBulkCommand<ContentBulkCommandContext>) applicationContext.getBean(commandBeanName);
		ContentBulkCommandContext context = new ContentBulkCommandContext(this.getSelectedIds(), this.getCurrentUser(), new DefaultBulkCommandTracer<String>());
		command.init(context);
		return command;
	}

	public String viewResult() {
		return this.getReport() == null ? "expired" : SUCCESS;
	}

	public ContentBulkActionSummary getSummary() {
		return this.getBulkActionHelper().getSummary(this.getSelectedIds());
	}

	public BaseBulkCommand<?, ?, ?> getCommand() {
		return this.getBulkCommandManager().getCommand(this.getCommandOwner(), this.getCommandId());
	}

	public BulkCommandReport<?> getReport() {
		return this.getBulkCommandManager().getCommandReport(this.getCommandOwner(), this.getCommandId());
	}

	public SmallBulkCommandReport getSmallReport() {
		return this.getBulkActionHelper().getSmallReport(this.getReport());
	}

	protected boolean checkAllowedContents() {
		return this.getBulkActionHelper().checkAllowedContents(this.getSelectedIds(), this, this);
	}

	protected String getCommandOwner() {
		return IContentBulkActionHelper.BULK_COMMAND_OWNER;
	}

	public Set<String> getSelectedIds() {
		return _selectedIds;
	}
	public void setSelectedIds(Set<String> selectedIds) {
		this._selectedIds = selectedIds;
	}

	public String getCommandId() {
		return _commandId;
	}
	public void setCommandId(String commandId) {
		this._commandId = commandId;
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

	private String _commandId;

	private IBulkCommandManager _bulkCommandManager;
	private IContentManager _contentManager;
	private IContentBulkActionHelper _bulkActionHelper;

}
