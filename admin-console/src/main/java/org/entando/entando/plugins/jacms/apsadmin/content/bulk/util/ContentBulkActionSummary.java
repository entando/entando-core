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
