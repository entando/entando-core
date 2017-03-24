package org.entando.entando.plugins.jacms.apsadmin.content.bulk;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.common.command.report.BulkCommandReport;
import org.entando.entando.aps.system.services.command.IBulkCommandManager;

import com.agiletec.plugins.jacms.apsadmin.content.ContentFinderAction;

public class BaseContentBulkAction extends ContentFinderAction {
	
	public String init() {
		// TODO any preliminar check
		return SUCCESS;
	}

	protected boolean checkSelectedContents() {
		boolean result = true;
		Collection<String> selectedContents = this.getContentIds();
		if (selectedContents == null || selectedContents.isEmpty()) {
			this.addActionError(this.getText("error.content.bulk.noItems"));
			result = false;
		}
		return result;
	}

	public BulkCommandReport<?> getReport() {
		BulkCommandReport<?> report = null;
		if (StringUtils.isNotEmpty(this.getThreadName())) {
			Object reportObj = this.getRequest().getSession().getAttribute(this.getThreadName());
			if (reportObj instanceof BulkCommandReport<?>) {
				report = (BulkCommandReport<?>) reportObj;
			}
		}
		return report;
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

	public String getThreadName() {
		return _threadName;
	}
	public void setThreadName(String threadName) {
		this._threadName = threadName;
	}

	protected IBulkCommandManager getBulkCommandManager() {
		return _bulkCommandManager;
	}
	public void setBulkCommandManager(IBulkCommandManager bulkCommandManager) {
		this._bulkCommandManager = bulkCommandManager;
	}

	private Set<String> _contentIds;

	private int _strutsAction;
	private String _threadName;
	private IBulkCommandManager _bulkCommandManager;

}
