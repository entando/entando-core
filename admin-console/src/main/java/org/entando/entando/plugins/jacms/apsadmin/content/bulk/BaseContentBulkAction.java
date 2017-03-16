package org.entando.entando.plugins.jacms.apsadmin.content.bulk;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.common.command.report.BulkCommandReport;

import com.agiletec.plugins.jacms.apsadmin.content.ContentFinderAction;

public class BaseContentBulkAction extends ContentFinderAction {
	
	public String init() {
		// TODO any preliminar check
		return SUCCESS;
	}

	protected boolean checkSelectedContents() {
		boolean result = true;
		Collection<String> selectedContents = this.getSelectedContents();
		if (selectedContents == null || selectedContents.isEmpty()) {
			this.addActionError(this.getText("error.content.bulk.noItems"));
			result = false;
		}
		return result;
	}

	protected Collection<String> getSelectedContents() {
		if (this._selectedContents == null) {
			this._selectedContents = this.getContentIds();
		}
		return this._selectedContents;
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

	public int getStrutsAction() {
		return _strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}

	public String getThreadName() {
		return threadName;
	}
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	private Collection<String> _selectedContents;
	
	private int _strutsAction;
	private String threadName;

}
