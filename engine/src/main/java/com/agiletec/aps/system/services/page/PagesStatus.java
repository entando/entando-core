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
package com.agiletec.aps.system.services.page;

import java.util.Date;

public class PagesStatus {

	private int online;
	private int onlineWithChanges;
	private int draft;
	private Date lastUpdate;
	
	public int getOnline() {
		return online;
	}
	public void setOnline(int online) {
		this.online = online;
	}
	
	public int getOnlineWithChanges() {
		return onlineWithChanges;
	}
	public void setOnlineWithChanges(int onlineWithChanges) {
		this.onlineWithChanges = onlineWithChanges;
	}
	
	public int getDraft() {
		return draft;
	}
	public void setDraft(int draft) {
		this.draft = draft;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public int getTotal() {
		return this.getOnline() + this.getOnlineWithChanges() + this.getDraft();
	}
	
	@Override
	public String toString() {
		return "PagesStatus [online=" + online + ", onlineWithChanges=" + onlineWithChanges + ", draft=" + draft + "]";
	}
	
	
}
