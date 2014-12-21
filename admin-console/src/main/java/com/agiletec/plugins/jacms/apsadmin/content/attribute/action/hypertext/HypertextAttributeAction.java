/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package com.agiletec.plugins.jacms.apsadmin.content.attribute.action.hypertext;

import com.agiletec.apsadmin.system.BaseAction;

/**
 * Classe action delegata alla gestione generale dei EntandoLink (link interni al testo degli attributi Hypertext).
 * @author E.Santoboni
 */
public class HypertextAttributeAction extends BaseAction {
	
	public String getInternalActionName() {
		return _internalActionName;
	}
	public void setInternalActionName(String internalActionName) {
		this._internalActionName = internalActionName;
	}
	
	public String getActiveTab() {
		return _activeTab;
	}
	public void setActiveTab(String activeTab) {
		this._activeTab = activeTab;
	}
	
	private String _internalActionName;
	private String _activeTab;
	
}