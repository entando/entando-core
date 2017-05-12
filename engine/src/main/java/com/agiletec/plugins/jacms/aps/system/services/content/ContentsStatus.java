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
package com.agiletec.plugins.jacms.aps.system.services.content;

import java.util.Date;

public class ContentsStatus {

	public int getTotal() {
		return this.getOnline() + this.getOnlineWithChanges() + this.getDraft();
	}
	
	@Override
	public String toString() {
		return "ContentsStatus [online=" + _online + ", onlineWithChanges=" + _onlineWithChanges + ", draft=" + _draft + "]";
	}
	
	public int getOnline() {
		return _online;
	}
	public void setOnline(int online) {
		this._online = online;
	}
	
	public int getOnlineWithChanges() {
		return _onlineWithChanges;
	}
	public void setOnlineWithChanges(int onlineWithChanges) {
		this._onlineWithChanges = onlineWithChanges;
	}
	
	public int getDraft() {
		return _draft;
	}
	public void setDraft(int draft) {
		this._draft = draft;
	}

	public Date getLastUpdate() {
		return _lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this._lastUpdate = lastUpdate;
	}
	
	private int _online;
	private int _onlineWithChanges;
	private int _draft;
	private Date _lastUpdate;
}
