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
package org.entando.entando.apsadmin.system.services.shortcut.model;

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