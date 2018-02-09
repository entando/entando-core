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
import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.BaseContentPropertyBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.common.ContentPropertyBulkCommandContext;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.group.JoinGroupBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.group.RemoveGroupBulkCommand;
import org.entando.entando.plugins.jacms.apsadmin.content.bulk.util.ContentBulkActionSummary;
import org.entando.entando.plugins.jacms.apsadmin.content.bulk.util.IContentBulkActionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;

public class ContentGroupBulkAction extends BaseAction {

	private static final Logger _logger = LoggerFactory.getLogger(ContentGroupBulkAction.class);

	public String entry() {
		return this.checkAllowedContents() ? SUCCESS : "list";
	}

	public String join() {
		try {
			String groupName = this.getGroupName();
			Group group = this.getGroupManager().getGroup(groupName);
			if (null != group) {
				this.getExtraGroupNames().add(groupName);
			}
		} catch (Throwable t) {
			_logger.error("Error joining group for bulk action", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String disjoin() {
		try {
			this.getExtraGroupNames().remove(this.getGroupName());
		} catch (Throwable t) {
			_logger.error("Error removing group from bulk action", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String checkApply() {
		return this.checkGroups() ? SUCCESS : INPUT;
	}

	public String apply() {
		try {
			if (!this.checkAllowedContents()) {
				return "list";
			} else if (!this.checkGroups()) {
				return INPUT;
			} else {
				BaseContentPropertyBulkCommand<String> command = this.initBulkCommand();
				BulkCommandReport<String> report = this.getBulkCommandManager().addCommand(IContentBulkActionHelper.BULK_COMMAND_OWNER, command);
				this.setCommandId(report.getCommandId());
			}
		} catch (Throwable t) {
			_logger.error("Error occurred applying groups (add/remove)", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	private BaseContentPropertyBulkCommand<String> initBulkCommand() {
		String commandBeanName = ApsAdminSystemConstants.DELETE == this.getStrutsAction() ? 
				RemoveGroupBulkCommand.BEAN_NAME : JoinGroupBulkCommand.BEAN_NAME;
		WebApplicationContext applicationContext = ApsWebApplicationUtils.getWebApplicationContext(this.getRequest());
		BaseContentPropertyBulkCommand<String> command = (BaseContentPropertyBulkCommand<String>) applicationContext.getBean(commandBeanName);
		ContentPropertyBulkCommandContext<String> context = new ContentPropertyBulkCommandContext<String>(this.getSelectedIds(), 
				this.getExtraGroupNames(), this.getCurrentUser(), new DefaultBulkCommandTracer<String>());
		command.init(context);
		return command;
	}

	public ContentBulkActionSummary getSummary() {
		return this.getBulkActionHelper().getSummary(this.getSelectedIds());
	}

	protected boolean checkAllowedContents() {
		return this.getBulkActionHelper().checkAllowedContents(this.getSelectedIds(), this, this);
	}

	protected boolean checkGroups() {
		return this.getBulkActionHelper().checkGroups(this.getAllowedGroups(), this.getExtraGroupNames(), this, this);
	}

	public Group getGroup(String groupName) {
		return this.getGroupManager().getGroup(groupName);
	}

	public List<Group> getAllowedGroups() {
		return this.getActualAllowedGroups();
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

	public Set<String> getExtraGroupNames() {
		return _extraGroupNames;
	}
	public void setExtraGroupNames(Set<String> extraGroupNames) {
		this._extraGroupNames = extraGroupNames;
	}

	public String getGroupName() {
		return _groupName;
	}
	public void setGroupName(String groupName) {
		this._groupName = groupName;
	}

	protected IGroupManager getGroupManager() {
		return _groupManager;
	}
	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
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

	private Set<String> _extraGroupNames = new TreeSet<String>();
	private String _groupName;
	private IGroupManager _groupManager;

	private IBulkCommandManager _bulkCommandManager;
	private IContentManager _contentManager;
	private IContentBulkActionHelper _bulkActionHelper;

}
