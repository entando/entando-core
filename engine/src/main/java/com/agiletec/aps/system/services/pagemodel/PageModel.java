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
package com.agiletec.aps.system.services.pagemodel;

import com.agiletec.aps.system.services.page.Widget;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.entando.entando.aps.system.services.api.model.CDataXmlTypeAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.*;

/**
 * Representation of a page template. 
 * This object contains the description and the definition of "frames" available. 
 * The definition of the page template is in the form of jsp or freemarker template. 
 * In the case of representation on jsp, the file name is equals then the template code.
 * The "frames" are page sections that can contains a "widget".
 */
@XmlRootElement(name = "pageModel")
@XmlType(propOrder = {"code", "description", "pluginCode", "template", "configuration"})
public class PageModel implements Serializable {

	private String code;
	private String description;
	private Frame[] configuration = new Frame[0];
	private int mainFrame = -1;
	private String pluginCode;
	private String template;

	/**
	 * Return the code of page template.
	 * @return The code of page template.
	 */
	@XmlElement(name = "code", required = true)
	public String getCode() {
		return code;
	}

	/**
	 * Set the code of page template.
	 * @param code The code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Return the description of page template.
	 * @return The description of page template.
	 */
	@XmlElement(name = "description", required = true)
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description of page template.
	 * @param description The description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Return the description of page template.
	 * @return The description of page template.
	 * @deprecated use getDescription()
	 */
	@XmlTransient
	public String getDescr() {
		return this.getDescription();
	}

	/**
	 * Set the description of page template.
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
	 * @return Position of the main frame, if it exists, -1 otherwise
	 */
	@XmlTransient
	public int getMainFrame() {
		return mainFrame;
	}

	/**
	 * Setta il numero relativo del mainFrame.
	 * @param mainFrame Il numero relativo del mainFrame.
	 */
	public void setMainFrame(int mainFrame) {
		this.mainFrame = mainFrame;
	}

	/**
	 * Returns the {@link Frame} object given a specific index
	 * @param index the index of the frame to return
	 * @return the {@link Frame} or null
	 */
	public Frame getFrameConfig(int index) {
		return isValidFrameIndex(index) ? configuration[index] : null;
	}

	private boolean isValidFrameIndex(int index) {
		return (configuration != null) &&
			   (index >= 0) &&
			   (index < configuration.length);
	}

	/**
	 * Returns the {@link Frame} array of the current {@link PageModel}
	 * @return the {@link Frame} array or null
	 */
	@XmlTransient
	public Frame[] getFramesConfig() {
		return configuration;
	}

	/**
	 * Restituisce la configurazione dei widget di default.
	 * @return I widget di default.
	 */
	@XmlTransient
	public Widget[] getDefaultWidget() {
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
		return PageModel.getPageModelJspPath(code, pluginCode);
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
		return configuration;
	}

	public void setConfiguration(Frame[] configuration) {
		this.configuration = configuration;
	}

	/**
	 * Return the code of the plugin owner of page template.
	 * The field is null if the page template belong to Entando Core.
	 * @return The plugin code.
	 */
	@XmlElement(name = "pluginCode", required = false)
	public String getPluginCode() {
		return pluginCode;
	}

	/**
	 * Set the code of the plugin owner of page template.
	 * @param pluginCode The plugin code. 
	 */
	public void setPluginCode(String pluginCode) {
		this.pluginCode = pluginCode;
	}

	@XmlJavaTypeAdapter(CDataXmlTypeAdapter.class)
	@XmlElement(name = "template", required = false)
	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("code", code)
				.append("description", description)
				.append("configuration", configuration)
				.append("mainFrame", mainFrame)
				.append("pluginCode", pluginCode)
				.append("template", template)
				.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PageModel pageModel = (PageModel) o;
		return mainFrame == pageModel.mainFrame &&
			   Objects.equals(code, pageModel.code) &&
			   Objects.equals(description, pageModel.description) &&
			   Arrays.equals(configuration, pageModel.configuration) &&
			   Objects.equals(pluginCode, pageModel.pluginCode) &&
			   Objects.equals(template, pageModel.template);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(code, description, mainFrame, pluginCode, template);
		result = 31 * result + Arrays.hashCode(configuration);
		return result;
	}
}
