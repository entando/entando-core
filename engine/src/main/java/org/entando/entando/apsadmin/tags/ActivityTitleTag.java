/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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