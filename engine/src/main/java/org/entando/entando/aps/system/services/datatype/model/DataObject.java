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
package org.entando.entando.aps.system.services.datatype.model;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.agiletec.aps.system.common.entity.model.ApsEntity;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.parse.IApsEntityDOM;
import org.entando.entando.aps.system.services.datatype.parse.ContentDOM;

public class DataObject extends ApsEntity {

	public String getStatus() {
		return this._status;
	}

	public void setStatus(String status) {
		this._status = status;
	}

	public String getViewPage() {
		return this._viewPage;
	}

	public void setViewPage(String viewPage) {
		this._viewPage = viewPage;
	}

	public String getListModel() {
		return this._listModel;
	}

	public void setListModel(String listModel) {
		this._listModel = listModel;
	}

	public String getDefaultModel() {
		return this._defaultModel;
	}

	public void setDefaultModel(String defaultModel) {
		this._defaultModel = defaultModel;
	}

	@Override
	public IApsEntity getEntityPrototype() {
		DataObject content = (DataObject) super.getEntityPrototype();
		content.setStatus(STATUS_NEW);
		content.setVersion(INIT_VERSION);
		content.setViewPage(this.getViewPage());
		content.setListModel(this.getListModel());
		content.setDefaultModel(this.getDefaultModel());
		return content;
	}

	@Override
	protected IApsEntityDOM getBuildJDOM() {
		ContentDOM contentDOM = (ContentDOM) super.getBuildJDOM();
		contentDOM.setStatus(this.getStatus());
		contentDOM.setVersion(this.getVersion());
		contentDOM.setFirstEditor(this.getFirstEditor());
		contentDOM.setLastEditor(this.getLastEditor());
		contentDOM.setCreationDate(this.getCreated());
		contentDOM.setModifyDate(this.getLastModified());
		return contentDOM;
	}

	public boolean isOnLine() {
		return this._onLine;
	}

	public void setOnLine(boolean onLine) {
		this._onLine = onLine;
	}

	public Date getCreated() {
		return _created;
	}

	public void setCreated(Date created) {
		this._created = created;
	}

	public Date getLastModified() {
		return _lastModified;
	}

	public void setLastModified(Date lastModified) {
		this._lastModified = lastModified;
	}

	public String getVersion() {
		return _version;
	}

	public void setVersion(String version) {
		Pattern pattern = Pattern.compile("\\d+\\.\\d+");
		Matcher matcher = pattern.matcher(version);
		if (!matcher.matches()) {
			throw new RuntimeException("Invalid content version");
		}
		this._version = version;
	}

	public void incrementVersion(boolean approve) {
		if (approve) {
			this.updateVersionIdOnPublishing();
		} else {
			this.updateVersionId();
		}
	}

	protected void updateVersionId() {
		String prevVersionId = this.getVersion();
		if (null == prevVersionId) {
			prevVersionId = INIT_VERSION;
		}
		String[] item = this.getVersionItems(prevVersionId);
		int workVersion = Integer.parseInt(item[1]);
		int newWorkVersion = workVersion + 1;
		String newVersionId = item[0] + "." + newWorkVersion;
		this.setVersion(newVersionId);
	}

	protected void updateVersionIdOnPublishing() {
		String prevVersionId = this.getVersion();
		if (null == prevVersionId) {
			prevVersionId = INIT_VERSION;
		}
		String[] item = this.getVersionItems(prevVersionId);
		int onlineVersion = Integer.parseInt(item[0]);
		int newOnlineVersion = onlineVersion + 1;
		String newVersionId = newOnlineVersion + ".0";
		this.setVersion(newVersionId);
	}

	protected String[] getVersionItems(String versionId) {
		return versionId.split("\\.");
	}

	public String getFirstEditor() {
		return _firstEditor;
	}

	public void setFirstEditor(String firstEditor) {
		this._firstEditor = firstEditor;
	}

	public String getLastEditor() {
		return _lastEditor;
	}

	public void setLastEditor(String lastEditor) {
		this._lastEditor = lastEditor;
	}

	private String _status;
	private boolean _onLine;
	private String _viewPage;
	private String _listModel;
	private String _defaultModel;

	private Date _created;
	private Date _lastModified;

	private String _version;
	private String _firstEditor;
	private String _lastEditor;

	@Deprecated
	public static final String STATES_NEW = "Nuovo";

	public static final String STATUS_NEW = "NEW";

	@Deprecated
	public static final String STATES_DRAFT = "Bozza";

	public static final String STATUS_DRAFT = "DRAFT";

	@Deprecated
	public static final String STATES_READY = "Pronto";

	public static final String STATUS_READY = "READY";

	public static final String STATUS_PUBLIC = "PUBLIC";

	@Deprecated
	public static final String[] STATES = {STATES_DRAFT, STATES_READY};

	public static final String[] AVAILABLE_STATUS = {STATUS_DRAFT, STATUS_READY};

	public static final String INIT_VERSION = "0.0";

}
