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
package org.entando.entando.apsadmin.tags;

import org.apache.struts2.components.Text;
import org.apache.struts2.views.jsp.TextTag;

/**
 * Render a title of the traced activity
 * @author E.Santoboni
 */
public class ActivityTitleTag extends TextTag {
	
	@Override
	protected void populateParams() {
        super.populateParams();
        Text text = (Text) super.component;
        text.setName(this.createLabelKey());
    }
	
	protected String createLabelKey() {
		StringBuilder builder = new StringBuilder("label.activity.");
		String evaluatedActionName = (String) super.findValue(this.getActionName());
		String evaluatedNamespace = (String) super.findValue(this.getNamespace());
		Integer evaluatedActionType = (null != this.getActionType()) ? (Integer) super.findValue(this.getActionType()) : null;
		String namespace = (evaluatedNamespace.startsWith("/")) ? evaluatedNamespace.substring(1) : evaluatedNamespace;
		builder.append(namespace).append("/").append(evaluatedActionName);
		if (null != evaluatedActionType) {
			builder.append(".").append(evaluatedActionType);
		}
		String labelKey = builder.toString();
		labelKey = labelKey.trim().replace(' ', '_');
		return labelKey.replaceAll("/", "_");
	}
	
	public String getActionName() {
		return _actionName;
	}
	public void setActionName(String actionName) {
		this._actionName = actionName;
	}
	
	public String getNamespace() {
		return _namespace;
	}
	public void setNamespace(String namespace) {
		this._namespace = namespace;
	}
	
	public String getActionType() {
		return _actionType;
	}
	public void setActionType(String actionType) {
		this._actionType = actionType;
	}
	
	private String _actionName;
	private String _namespace;
	private String _actionType;
	
}