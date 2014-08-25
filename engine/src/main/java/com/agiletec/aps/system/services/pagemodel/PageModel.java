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
package com.agiletec.aps.system.services.pagemodel;

import com.agiletec.aps.system.services.page.Widget;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.entando.entando.aps.system.services.api.model.CDataXmlTypeAdapter;

/**
 * Representation of a page template. 
 * This object contains the description and the definition of "frames" available. 
 * The definition of the page model is in the form of jsp or freemarker template. 
 * In the case of representation on jsp, the file name is equals then the model code.
 * The "frames" are page sections that can contains a "widget".
 * @author M.Diana
 */
@XmlRootElement(name = "pageModel")
@XmlType(propOrder = {"code", "description", "pluginCode", "template", "configuration"})
public class PageModel implements Serializable {
	
	/**
	 * Return the code of page model.
	 * @return The code of page model.
	 */
	@XmlElement(name = "code", required = true)
	public String getCode() {
		return _code;
	}
	
	/**
	 * Set the code of page model.
	 * @param code The code to set
	 */
	public void setCode(String code) {
		this._code = code;
	}
	
	/**
	 * Return the description of page model.
	 * @return The description of page model.
	 */
	@XmlElement(name = "description", required = true)
	public String getDescription() {
		return _description;
	}
	
	/**
	 * Set the description of page model.
	 * @param description The description to set
	 */
	public void setDescription(String description) {
		this._description = description;
	}
	
	/**
	 * Return the description of page model.
	 * @return The description of page model.
	 * @deprecated use getDescription()
	 */
	@XmlTransient
	public String getDescr() {
		return this.getDescription();
	}
	
	/**
	 * Set the description of page model.
	 * @param descr The code to set
	 * @deprecated use setDescription(String)
	 */
	public void setDescr(String descr) {
		this.setDescription(descr);
	}
	
	/**
	 * Restituisce l'insieme ordinato delle descrizioni dei "frames" del modello.
	 * @return L'insieme delle descrizioni dei "frames"
	 */
	@XmlTransient
	public String[] getFrames() {
		Frame[] configuration = this.getConfiguration();
		if (null == configuration) {
			return new String[0];
		}
		String[] descriptions = new String[configuration.length];
		for (int i = 0; i < configuration.length; i++) {
			Frame frame = configuration[i];
			if (null != frame) {
				descriptions[i] = frame.getDescription();
			}
		}
		return descriptions;
	}
	
	/**
	 * Restituisce il numero relativo del mainFrame.
	 * @return Il numero relativo del mainFrame.
	 */
	@XmlTransient
	public int getMainFrame() {
		return _mainFrame;
	}

	/**
	 * Setta il numero relativo del mainFrame.
	 * @param mainFrame Il numero relativo del mainFrame.
	 */
	public void setMainFrame(int mainFrame) {
		this._mainFrame = mainFrame;
	}
	
	/**
	 * Restituisce la configurazione dei widget di default.
	 * @return I widget di default.
	 */
	@XmlTransient
	public Widget[] getDefaultWidget() {
		Frame[] configuration = this.getConfiguration();
		Widget[] defaultWidgets = new Widget[configuration.length];
		for (int i = 0; i < configuration.length; i++) {
			Frame frame = configuration[i];
			if (null != frame) {
				defaultWidgets[i] = frame.getDefaultWidget();
			}
		}
		return defaultWidgets;
	}
	
	@Override
	public PageModel clone() {
		PageModel clone = new PageModel();
		clone.setCode(this.getCode());
		clone.setDescription(this.getDescription());
		clone.setMainFrame(this.getMainFrame());
		clone.setPluginCode(this.getPluginCode());
		clone.setTemplate(this.getTemplate());
		Frame[] frames = this.getConfiguration();
		if (null != frames) {
			Frame[] framesClone = new Frame[frames.length];
			for (int i = 0; i < frames.length; i++) {
				Frame frame = frames[i];
				if (null != frame) {
					framesClone[i] = frame.clone();
				}
			}
			clone.setConfiguration(framesClone);
		}
		return clone;
	}
	
	@XmlTransient
	public String getPageModelJspPath() {
		return PageModel.getPageModelJspPath(this.getCode(), this.getPluginCode());
	}
	
	public static String getPageModelJspPath(String code, String pluginCode) {
		boolean isPluginPageModel = (null != pluginCode && pluginCode.trim().length()>0);
		StringBuilder jspPath = new StringBuilder("/WEB-INF/");
		if (isPluginPageModel) {
			jspPath.append("plugins/").append(pluginCode.trim()).append("/");
		}
		jspPath.append("aps/jsp/models/").append(code.trim()).append(".jsp");
		return jspPath.toString();
	}
	
	@XmlElement(name = "frame", required = false)
    @XmlElementWrapper(name = "configuration")
    public Frame[] getConfiguration() {
		return _configuration;
	}
	
	public void setConfiguration(Frame[] configuration) {
		this._configuration = configuration;
	}
	
	/**
	 * Return the code of the plugin owner of page model.
	 * The field is null if the page model belong to Entando Core.
	 * @return The plugin code.
	 */
	@XmlElement(name = "pluginCode", required = false)
	public String getPluginCode() {
		return _pluginCode;
	}
	
	/**
	 * Set the code of the plugin owner of page model.
	 * @param pluginCode The plugin code. 
	 */
	public void setPluginCode(String pluginCode) {
		this._pluginCode = pluginCode;
	}
	
	@XmlJavaTypeAdapter(CDataXmlTypeAdapter.class)
	@XmlElement(name = "template", required = false)
	public String getTemplate() {
		return _template;
	}
	public void setTemplate(String template) {
		this._template = template;
	}
	
	/**
	 * Il codice del modello di pagina
	 */
	private String _code;
	
	/**
	 * La descrizione del modello di pagina
	 */
	private String _description;
	
	private Frame[] _configuration = new Frame[0];
	
	/**
	 * La posizione del frame principale, se esiste;
	 * vale -1 se non esiste;
	 */
	private int _mainFrame = -1;
	
	/**
	 * The code of the plugin owner of page model.
	 */
	private String _pluginCode;
	
	private String _template;
	
}
