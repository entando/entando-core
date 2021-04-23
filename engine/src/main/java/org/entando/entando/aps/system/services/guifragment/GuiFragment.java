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
package org.entando.entando.aps.system.services.guifragment;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

/**
 * @author E.Santoboni
 */
public class GuiFragment implements Serializable {
	
	@Override
	public GuiFragment clone() {
		GuiFragment clone = new GuiFragment();
		clone.setCode(this.getCode());
		clone.setDefaultGui(this.getDefaultGui());
		clone.setGui(this.getGui());
		clone.setLocked(this.isLocked());
		clone.setPluginCode(this.getPluginCode());
		clone.setWidgetTypeCode(this.getWidgetTypeCode());
		return clone;
	}
	
	public String getCode() {
		return _code;
	}
	public void setCode(String code) {
		this._code = code;
	}
	
	@Deprecated
	public String getWidgetCode() {
		return this.getWidgetTypeCode();
	}
	@Deprecated
	public void setWidgetCode(String widgetCode) {
		this.setWidgetTypeCode(widgetCode);
	}
	
	public String getWidgetTypeCode() {
		return _widgetTypeCode;
	}
	public void setWidgetTypeCode(String widgetTypeCode) {
		this._widgetTypeCode = widgetTypeCode;
	}
	
	public String getPluginCode() {
		return _pluginCode;
	}
	public void setPluginCode(String pluginCode) {
		this._pluginCode = pluginCode;
	}
	
	public String getGui() {
		return _gui;
	}
	public void setGui(String gui) {
		this._gui = gui;
	}
	
	public String getDefaultGui() {
		return _defaultGui;
	}
	public void setDefaultGui(String defaultGui) {
		this._defaultGui = defaultGui;
	}
	
	/**
	 * Return the current gui in use.
	 * Return the default gui if the custom gui ("gui" property) are not available, else the default gui.
	 * @return The current gui in use.
	 */
	public String getCurrentGui() {
		boolean hasCustomGui = StringUtils.isNotBlank(this.getGui());
		if (hasCustomGui) {
			return this.getGui();
		} else {
			return this.getDefaultGui();
		}
	}
	
	public boolean isLocked() {
		return _locked;
	}
	public void setLocked(boolean locked) {
		this._locked = locked;
	}
	
	private String _code;
	private String _widgetTypeCode;
	private String _pluginCode;
	private String _gui;
	private String _defaultGui;
	private boolean _locked;
	
}
