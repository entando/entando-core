package org.entando.entando.plugins.jacms.apsadmin.content.bulk;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.entando.entando.aps.system.common.command.report.BulkCommandReport;
import org.entando.entando.aps.system.common.command.tracer.BulkCommandTracer;
import org.entando.entando.aps.system.common.command.tracer.DefaultBulkCommandTracer;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.group.BaseContentGroupBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.group.JoinGroupBulkCommand;
import org.entando.entando.plugins.jacms.aps.system.services.content.command.group.RemoveGroupBulkCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;

public class ContentGroupBulkAction extends BaseContentBulkAction {

	private static final Logger _logger = LoggerFactory.getLogger(ContentGroupBulkAction.class);

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
		if (!this.checkGroups()) {
			return INPUT;
		}
		return SUCCESS;
	}

	public String apply() {
		try {
			if (!this.checkAllowedContents(true)) {
				return "list";
			} else if (!this.checkGroups()) {
				return INPUT;
			} else {
				BulkCommandTracer<String> tracer = new DefaultBulkCommandTracer<String>();
				WebApplicationContext wax = ApsWebApplicationUtils.getWebApplicationContext(this.getRequest());
				BaseContentGroupBulkCommand command = null;
				if (ApsAdminSystemConstants.DELETE == this.getStrutsAction()) {
					command = new RemoveGroupBulkCommand(this.getContentIds(), this.getExtraGroupNames(), 
							this.getContentManager(), tracer, wax);
				} else {
					command = new JoinGroupBulkCommand(this.getContentIds(), this.getExtraGroupNames(), 
							this.getContentManager(), tracer, wax);
				}
				BulkCommandReport<String> report = this.getBulkCommandManager().addCommand(this.getCommandOwner(), command);
				this.setCommandId(report.getCommandId());
			}
		} catch (Throwable t) {
			_logger.error("Error occurred applying groups (add/remove)", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	protected boolean checkGroups() {
		boolean allowed = true;
		Set<String> extraGroupNames = this.getExtraGroupNames();
		if (extraGroupNames == null || extraGroupNames.isEmpty()) {
			this.addActionError(this.getText("error.bulk.groups.empty"));
			allowed = false;
		} else {
			List<String> groupNames = this.getAllowedGroupNames();
			for (String groupName : extraGroupNames) {
				if (!groupNames.contains(groupName)) {
					this.addActionError(this.getText("error.bulk.groups.notAllowed", groupName));
					allowed = false;
				}
			}
		}
		return allowed;
	}

	protected List<String> getAllowedGroupNames() {
		List<Group> groups = this.getAllowedGroups();
		List<String> groupNames = new ArrayList<String>(groups.size());
		for (Group group : groups) {
			groupNames.add(group.getAuthority());
		}
		return groupNames;
	}

	public List<Group> getAllowedGroups() {
		return super.getActualAllowedGroups();
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

	private Set<String> _extraGroupNames = new TreeSet<String>();
	private String _groupName;
	private IGroupManager _groupManager;

}
