/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.aps.system.services.guifragment.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.entando.entando.aps.system.services.api.model.CDataXmlTypeAdapter;
import org.entando.entando.aps.system.services.guifragment.GuiFragment;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "guiFragment")
@XmlType(propOrder = {"code", "widgetTypeCode", "pluginCode", "gui", "defaultGui", "locked"})
public class JAXBGuiFragment {
	
    public JAXBGuiFragment() {
        super();
    }
	
    public JAXBGuiFragment(GuiFragment guiFragment) {
		this.setCode(guiFragment.getCode());
		this.setWidgetTypeCode(guiFragment.getWidgetTypeCode());
		this.setPluginCode(guiFragment.getPluginCode());
		this.setGui(guiFragment.getGui());
		this.setDefaultGui(guiFragment.getDefaultGui());
		this.setLocked(guiFragment.isLocked());
    }
    
	@XmlTransient
    public GuiFragment getGuiFragment() {
    	GuiFragment guiFragment = new GuiFragment();
		guiFragment.setCode(this.getCode());
		guiFragment.setWidgetTypeCode(this.getWidgetTypeCode());
		guiFragment.setPluginCode(this.getPluginCode());
		guiFragment.setGui(this.getGui());
    	guiFragment.setDefaultGui(this.getDefaultGui());
    	guiFragment.setLocked(this.isLocked());
    	return guiFragment;
    }
	
	@XmlElement(name = "code", required = true)
	public String getCode() {
		return _code;
	}
	public void setCode(String code) {
		this._code = code;
	}
	
	@XmlElement(name = "widgetTypeCode", required = true)
	public String getWidgetTypeCode() {
		return _widgetTypeCode;
	}
	public void setWidgetTypeCode(String widgetTypeCode) {
		this._widgetTypeCode = widgetTypeCode;
	}
	
	@XmlElement(name = "pluginCode", required = true)
	public String getPluginCode() {
		return _pluginCode;
	}
	public void setPluginCode(String pluginCode) {
		this._pluginCode = pluginCode;
	}
	
	@XmlJavaTypeAdapter(CDataXmlTypeAdapter.class)
	@XmlElement(name = "gui", required = true)
	public String getGui() {
		return _gui;
	}
	public void setGui(String gui) {
		this._gui = gui;
	}
	
	@XmlJavaTypeAdapter(CDataXmlTypeAdapter.class)
	@XmlElement(name = "defaultGui", required = true)
	public String getDefaultGui() {
		return _defaultGui;
	}
	public void setDefaultGui(String defaultGui) {
		this._defaultGui = defaultGui;
	}
	
	@XmlElement(name = "locked", required = true)
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
