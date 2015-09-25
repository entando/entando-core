/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.plugins.jacms.aps.system.services.content.model;

import java.util.Date;

import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;

/**
 * @author E.Santoboni
 */
public class ContentRecordVO extends ApsEntityRecord {
	
	public String getDescription() {
		return _description;
	}
	@Deprecated
	public String getDescr() {
		return this.getDescription();
	}
	
	public void setDescription(String description) {
		this._description = description;
	}
	@Deprecated
	public void setDescr(String description) {
		this.setDescription(description);
	}
	
	public String getStatus() {
		return _status;
	}
	public void setStatus(String status) {
		this._status = status;
	}
	
	public Date getCreate() {
		return _create;
	}
	public void setCreate(Date create) {
		this._create = create;
	}
	
	public Date getModify() {
		return _modify;
	}
	public void setModify(Date modify) {
		this._modify = modify;
	}
	
	public String getXmlWork() {
		return super.getXml();
	}
	public void setXmlWork(String xmlWork) {
		super.setXml(xmlWork);
	}
	
	public boolean isOnLine() {
		return _onLine;
	}
	public void setOnLine(boolean onLine) {
		this._onLine = onLine;
	}
	
	public boolean isSync() {
		return _sync;
	}
	public void setSync(boolean sync) {
		this._sync = sync;
	}
	
	public String getXmlOnLine() {
		return _xmlOnLine;
	}
	public void setXmlOnLine(String xmlOnLine) {
		this._xmlOnLine = xmlOnLine;
	}
	
	public String getMainGroupCode() {
		return _mainGroupCode;
	}
	public void setMainGroupCode(String mainGroupCode) {
		this._mainGroupCode = mainGroupCode;
	}
	
	public String getVersion() {
		return _version;
	}
	public void setVersion(String version) {
		this._version = version;
	}
	
	public String getLastEditor() {
		return _lastEditor;
	}
	public void setLastEditor(String lastEditor) {
		this._lastEditor = lastEditor;
	}
	
	private String _description;
	private String _status;
	private Date _create;
	private Date _modify;
	private boolean _onLine;
	private boolean _sync;
	private String _xmlOnLine;
	
	private String _mainGroupCode;
	
	private String _version;
	private String _lastEditor;
	
}