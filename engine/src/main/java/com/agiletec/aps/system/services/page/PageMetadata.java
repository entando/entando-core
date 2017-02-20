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
package com.agiletec.aps.system.services.page;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;

/**
 * This is the representation of a portal page metadata
 * @author E.Mezzano, S.Puddu
 */
public class PageMetadata {
	
	public String getCode() {
		return _code;
	}
	public void setCode(String code) {
		this._code = code;
	}
	
	public ApsProperties getTitles() {
		return _titles;
	}
	
	/**
	 * Set the titles of the node. 
	 * @param titles A set of properties with the titles, 
	 * where the keys are the codes of language.
	 */
	public void setTitles(ApsProperties titles) {
		this._titles = titles;
	}
	
	public void setTitle(String langCode, String title) {
		this.getTitles().setProperty(langCode, title);
	}
	
	public String getTitle(String langCode) {
		return this.getTitles().getProperty(langCode);
	}
	
	/**
	 * Return the related model of page
	 * @return the page model
	 */
	public PageModel getModel() {
		return _model;
	}
	
	/**
	 * WARNING: This method is for the page manager service only exclusive use
	 * Assign the given page model to the current object
	 * @param pageModel the model of the page to assign
	 */
	public void setModel(PageModel pageModel) {
		this._model = pageModel;
	}
	
	public void addExtraGroup(String groupName) {
		if (null == this.getExtraGroups()) this.setExtraGroups(new HashSet<String>());
		this.getExtraGroups().add(groupName);
	}
	
	public void removeExtraGroup(String groupName) {
		if (null == this.getExtraGroups()) return;
		this.getExtraGroups().remove(groupName);
	}
	
	public void setExtraGroups(Set<String> extraGroups) {
		this._extraGroups = extraGroups;
		
	}
	
	public Set<String> getExtraGroups() {
		return _extraGroups;
	}
	
	/**
	 * WARING: this method is reserved to the page manager service only.
	 * This returns a boolean values indicating whether the page is
	 * displayed in the menus or similar.
	 * @return true if the page must be shown in the menu, false otherwise. 
	 */
	public boolean isShowable() {
		return _showable;
	}
	
	/**
	 * WARING: this method is reserved to the page manager service only.
	 * Toggle the visibility of the current page in the menu or similar.
	 * @param showable a boolean which toggles the visibility on when true, off otherwise.
	 */
	public void setShowable(boolean showable) {
		this._showable = showable;
	}
	
	public boolean isUseExtraTitles() {
		return _useExtraTitles;
	}
	public void setUseExtraTitles(boolean useExtraTitles) {
		this._useExtraTitles = useExtraTitles;
	}
	
	public String getCharset() {
		return _charset;
	}
	public void setCharset(String charset) {
		this._charset = charset;
	}
	
	public String getMimeType() {
		return _mimeType;
	}
	public void setMimeType(String mimeType) {
		this._mimeType = mimeType;
	}
	
	public Date getUpdatedAt() {
		return _updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this._updatedAt = updatedAt;
	}
	
	@Override
	public String toString() {
		return "PageMetadata";
	}
	
	private String _code;
	
	private ApsProperties _titles = new ApsProperties();
	
	private Set<String> _extraGroups;
	
	/**
	 * The page model associate to the current object
	 */
	private PageModel _model;
	
	/**
	 * Toggle menu visibility on and off
	 */
	private boolean _showable = false;
	
	private boolean _useExtraTitles = false;
	
	private String _mimeType;
	
	private String _charset;
	
	private Date _updatedAt;
	
}