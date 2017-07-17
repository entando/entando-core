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
package org.entando.entando.apsadmin.portal.rs.model;

import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.system.services.page.Widget;

public class PageJO {

	public String getCode() {
		return _code;
	}

	public void setCode(String code) {
		this._code = code;
	}

	public Boolean getRoot() {
		return _root;
	}

	public void setRoot(Boolean root) {
		this._root = root;
	}

	public Boolean getOnline() {
		return _online;
	}

	public void setOnline(Boolean online) {
		this._online = online;
	}

	public Boolean getChanged() {
		return _changed;
	}

	public void setChanged(Boolean changed) {
		this._changed = changed;
	}

	public String getParentCode() {
		return _parentCode;
	}

	public void setParentCode(String parentCode) {
		this._parentCode = parentCode;
	}

	public String getGroup() {
		return _group;
	}

	public void setGroup(String group) {
		this._group = group;
	}

	public int getPosition() {
		return _position;
	}

	protected void setPosition(int position) {
		this._position = position;
	}

	public PageMetadata getOnlineMetadata() {
		return _onlineMetadata;
	}
	public void setOnlineMetadata(PageMetadata onlineMetadata) {
		this._onlineMetadata = onlineMetadata;
	}
	
	public PageMetadata getDraftMetadata() {
		return _draftMetadata;
	}
	public void setDraftMetadata(PageMetadata draftMetadata) {
		this._draftMetadata = draftMetadata;
	}
	
	public Widget[] getOnlineWidgets() {
		return _onlineWidgets;
	}
	public void setOnlineWidgets(Widget[] onlineWidgets) {
		this._onlineWidgets = onlineWidgets;
	}
	
	public Widget[] getDraftWidgets() {
		return _draftWidgets;
	}
	public void setDraftWidgets(Widget[] draftWidgets) {
		this._draftWidgets = draftWidgets;
	}

	private String _code;
	private Boolean _root;
	private Boolean _online;
	private Boolean _changed;
	private String _parentCode;
	private String _group;
	private int _position = -1;
	private PageMetadata _onlineMetadata;
	private PageMetadata _draftMetadata;
	private Widget[] _onlineWidgets;
	private Widget[] _draftWidgets;


}
