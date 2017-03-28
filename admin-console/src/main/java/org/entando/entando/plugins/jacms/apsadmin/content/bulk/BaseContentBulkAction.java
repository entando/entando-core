package org.entando.entando.plugins.jacms.apsadmin.content.bulk;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.common.command.BaseBulkCommand;
import org.entando.entando.aps.system.common.command.report.BulkCommandReport;
import org.entando.entando.aps.system.services.command.IBulkCommandManager;
import org.entando.entando.plugins.jacms.apsadmin.content.bulk.util.ContentBulkActionSummary;
import org.entando.entando.plugins.jacms.apsadmin.content.bulk.util.SmallBulkCommandReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;

public class BaseContentBulkAction extends BaseAction {

	private static final Logger _logger = LoggerFactory.getLogger(BaseContentBulkAction.class);

	public String entry() {
		if (this.checkAllowedContents(false)) {
			return SUCCESS;
		} else {
			return "list";
		}
	}

	public String viewResult() {
		BulkCommandReport<?> report = this.getReport();
		return report == null ? "expired" : SUCCESS;
	}

	/**
	 * Checks if the contents are allowed.<br/>
	 * The check can be done only on the number of elements, or can be more 
	 * @param fullCheck If true, checks also the existence and the user permissions on contents.
	 * @return True if the contents are allowed, false otherwise.
	 */
	protected boolean checkAllowedContents(boolean fullCheck) {
		boolean result = true;
		Collection<String> selectedContents = this.getContentIds();
		if (selectedContents == null || selectedContents.isEmpty()) {
			this.addActionError(this.getText("error.content.bulk.noItems"));
			result = false;
		}
		return result;
	}

	public ContentBulkActionSummary getSummary() {
		ContentBulkActionSummary summary = new ContentBulkActionSummary();
		try {
			IContentManager contentManager = this.getContentManager();
			for (String contentId : this.getContentIds()) {
				ContentRecordVO contentVO = contentManager.loadContentVO(contentId);
				if (contentVO.isOnLine()) {
					if (contentVO.isSync()) {
						summary.addAligned(contentId);
					} else {
						summary.addWorkAhead(contentId);
					}
				} else {
					summary.addNotOnline(contentId);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error checking content to generate summary for bulk action", t);
		}
		return summary;
	}

	public BaseBulkCommand<?, ?> getCommand() {
		BaseBulkCommand<?, ?> command = null;
		if (StringUtils.isNotEmpty(this.getCommandId())) {
			command = this.getBulkCommandManager().getCommand(this.getCommandOwner(), this.getCommandId());
		}
		return command;
	}

	public BulkCommandReport<?> getReport() {
		BulkCommandReport<?> report = null;
		if (StringUtils.isNotEmpty(this.getCommandId())) {
			report = this.getBulkCommandManager().getCommandReport(this.getCommandOwner(), this.getCommandId());
		}
		return report;
	}

	public SmallBulkCommandReport getSmallReport() {
		SmallBulkCommandReport smallReport = null;
		BulkCommandReport<?> report = this.getReport();
		if (report != null) {
			smallReport = new SmallBulkCommandReport();
			smallReport.setCommandId(report.getCommandId());
			smallReport.setStatus(report.getStatus());
			smallReport.setEndingTime(report.getEndingTime());
			smallReport.setTotal(report.getTotal());
			smallReport.setApplyTotal(report.getApplyTotal());
			smallReport.setApplySuccesses(report.getApplySuccesses());
			smallReport.setApplyErrors(report.getApplyErrors());
		}
		return smallReport;
	}

	public String getCommandOwner() {
		return BULK_COMMAND_OWNER;
	}

	public Set<String> getContentIds() {
		return _contentIds;
	}
	public void setContentIds(Set<String> contentIds) {
		this._contentIds = contentIds;
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

	private Set<String> _contentIds;

	private int _strutsAction;
	private String _commandId;

	private IBulkCommandManager _bulkCommandManager;
	private IContentManager _contentManager;
	public static final String BULK_COMMAND_OWNER = "jacms";

}
