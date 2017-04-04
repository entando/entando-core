package org.entando.entando.plugins.jacms.apsadmin.content.bulk.util;

import java.util.ArrayList;
import java.util.Collection;

public class ContentBulkActionSummary {

	public Collection<String> getNotOnline() {
		return _notOnline;
	}
	public void addNotOnline(String toAdd) {
		this._notOnline.add(toAdd);
	}

	public Collection<String> getWorkAhead() {
		return _workAhead;
	}
	public void addWorkAhead(String toAdd) {
		this._workAhead.add(toAdd);
	}

	public Collection<String> getAligned() {
		return _aligned;
	}
	public void addAligned(String toAdd) {
		this._aligned.add(toAdd);
	}

	private Collection<String> _notOnline = new ArrayList<String>();
	private Collection<String> _workAhead = new ArrayList<String>();
	private Collection<String> _aligned = new ArrayList<String>();

}
