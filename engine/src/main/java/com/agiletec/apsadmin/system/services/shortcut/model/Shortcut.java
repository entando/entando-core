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
package com.agiletec.apsadmin.system.services.shortcut.model;

import java.util.HashMap;
import java.util.Map;

/**
 * A Short Cut Object
 * @author E.Santoboni
 */
public class Shortcut extends AbstractBaseBean {
	
	public Shortcut(String id) {
		super(id);
	}
	
	@Override
	public Shortcut clone() {
		Shortcut clone = new Shortcut(this.getId());
		clone.setDescription(this.getDescription());
		clone.setDescriptionKey(this.getDescriptionKey());
		clone.setLongDescription(this.getLongDescription());
		clone.setLongDescriptionKey(this.getLongDescriptionKey());
		clone.setRequiredPermission(this.getRequiredPermission());
		clone.setMenuSectionCode(this.getMenuSectionCode());
		if (null != this.getMenuSection()) {
			clone.setMenuSection(this.getMenuSection().clone());
		}
		clone.setSource(this.getSource());
		clone.setNamespace(this.getNamespace());
		clone.setActionName(this.getActionName());
		if (null != this.getParameters()) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(this.getParameters());
			clone.setParameters(params);
		}
		return clone;
	}
	
	public String getRequiredPermission() {
		return _requiredPermission;
	}
	public void setRequiredPermission(String requiredPermission) {
		this._requiredPermission = requiredPermission;
	}
	
	public String getMenuSectionCode() {
		return _menuSectionCode;
	}
	public void setMenuSectionCode(String menuSectionCode) {
		this._menuSectionCode = menuSectionCode;
	}
	
	public MenuSection getMenuSection() {
		return _menuSection;
	}
	public void setMenuSection(MenuSection menuSection) {
		if (null != menuSection) {
			this.setMenuSectionCode(menuSection.getId());
		} else {
			this.setMenuSectionCode(null);
		}
		this._menuSection = menuSection;
	}
	
	public String getSource() {
		return _source;
	}
	public void setSource(String source) {
		this._source = source;
	}
	
	public String getNamespace() {
		return _namespace;
	}
	public void setNamespace(String namespace) {
		this._namespace = namespace;
	}
	
	public String getActionName() {
		return _actionName;
	}
	public void setActionName(String actionName) {
		this._actionName = actionName;
	}
	
	public Map<String, Object> getParameters() {
		return _parameters;
	}
	public void setParameters(Map<String, Object> parameters) {
		this._parameters = parameters;
	}
	
	private String _requiredPermission;
	private String _menuSectionCode;
	private MenuSection _menuSection;
	private String _source;
	private String _namespace;
	private String _actionName;
	private Map<String, Object> _parameters;
	
}