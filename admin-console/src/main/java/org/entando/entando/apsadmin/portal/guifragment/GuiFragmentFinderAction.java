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
package org.entando.entando.apsadmin.portal.guifragment;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.portal.AbstractPortalAction;
import com.agiletec.apsadmin.portal.helper.IPageActionHelper;

import java.util.ArrayList;
import java.util.Collections;
import org.apache.commons.beanutils.BeanComparator;

import org.entando.entando.aps.system.services.guifragment.GuiFragment;
import org.entando.entando.aps.system.services.guifragment.IGuiFragmentManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class GuiFragmentFinderAction extends AbstractPortalAction {
	
	private static final Logger _logger =  LoggerFactory.getLogger(GuiFragmentFinderAction.class);
	
	public List<String> getGuiFragmentsCodes() {
		try {
			FieldSearchFilter[] filters = new FieldSearchFilter[0];
			if (StringUtils.isNotBlank(this.getCode())) {
				FieldSearchFilter filterToAdd = new FieldSearchFilter("code", this.getCode(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getWidgetTypeCode())) {
				FieldSearchFilter filterToAdd = new FieldSearchFilter("widgettypecode", this.getWidgetTypeCode(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getPluginCode())) {
				FieldSearchFilter filterToAdd = new FieldSearchFilter("plugincode", this.getPluginCode(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			List<String> guiFragments = this.getGuiFragmentManager().searchGuiFragments(filters);
			return guiFragments;
		} catch (Throwable t) {
			_logger.error("Error getting guiFragments list", t);
			throw new RuntimeException("Error getting guiFragments list", t);
		}
	}
	
	protected FieldSearchFilter[] addFilter(FieldSearchFilter[] filters, FieldSearchFilter filterToAdd) {
		int len = filters.length;
		FieldSearchFilter[] newFilters = new FieldSearchFilter[len + 1];
		for(int i=0; i < len; i++){
			newFilters[i] = filters[i];
		}
		newFilters[len] = filterToAdd;
		return newFilters;
	}
	
	public GuiFragment getGuiFragment(String code) {
		GuiFragment guiFragment = null;
		try {
			guiFragment = this.getGuiFragmentManager().getGuiFragment(code);
		} catch (Throwable t) {
			_logger.error("Error getting guiFragment with code {}", code, t);
			throw new RuntimeException("Error getting guiFragment with code " + code, t);
		}
		return guiFragment;
	}
	
	public WidgetType getWidgetType(String widgetTypeCode) {
		return this.getWidgetTypeManager().getWidgetType(widgetTypeCode);
	}
	
	public List<SelectItem> getGuiFragmentPlugins() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		try {
			List<String> codes = this.getGuiFragmentManager().loadGuiFragmentPluginCodes();
			if (null != codes) {
				for (int i = 0; i < codes.size(); i++) {
					String code = codes.get(i);
					items.add(new SelectItem(code, this.getText(code+".name")));
				}
				BeanComparator c = new BeanComparator("value");
				Collections.sort(items, c);
			}
		} catch (Throwable t) {
			_logger.error("Error loading guiFragment plugins", t);
			throw new RuntimeException("Error loading guiFragment plugins", t);
		}
		return items;
	}
	
	public String getCode() {
		return _code;
	}
	public void setCode(String code) {
		this._code = code;
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
	
	protected IGuiFragmentManager getGuiFragmentManager() {
		return _guiFragmentManager;
	}
	public void setGuiFragmentManager(IGuiFragmentManager guiFragmentManager) {
		this._guiFragmentManager = guiFragmentManager;
	}
	
	protected IPageActionHelper getPageActionHelper() {
		return _pageActionHelper;
	}
	public void setPageActionHelper(IPageActionHelper pageActionHelper) {
		this._pageActionHelper = pageActionHelper;
	}
	
	private String _code;
	private String _widgetTypeCode;
	private String _pluginCode;
	private String _gui;
	
	private IGuiFragmentManager _guiFragmentManager;
	private IPageActionHelper _pageActionHelper;
	
	
}