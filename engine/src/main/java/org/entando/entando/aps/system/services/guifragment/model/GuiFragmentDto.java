/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.guifragment.model;

import com.agiletec.aps.system.services.pagemodel.PageModel;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.guifragment.GuiFragment;
import org.entando.entando.aps.system.services.widgettype.WidgetType;

/**
 * @author E.Santoboni
 */
public class GuiFragmentDto {

	private String code;
	private boolean locked;
	private WidgetTypeRef widgetType;
	private PluginRef plugin;
	private List<FragmentRef> fragments = new ArrayList<>();
	private List<PageModelRef> pageModels = new ArrayList<>();
	private String defaultGuiCode;
	private String guiCode;

	public GuiFragmentDto() {
	}

	public GuiFragmentDto(GuiFragment guiFragment, WidgetType type) {
		this.setCode(guiFragment.getCode());
		this.setLocked(guiFragment.isLocked());
		if (!StringUtils.isEmpty(guiFragment.getWidgetTypeCode())) {
			WidgetTypeRef widgetType = new WidgetTypeRef(guiFragment.getWidgetTypeCode(), null);
			this.setWidgetType(widgetType);
		}
		if (!StringUtils.isEmpty(guiFragment.getPluginCode())) {
			PluginRef plugin = new PluginRef(guiFragment.getPluginCode(), null);
			this.setPlugin(plugin);
		}
		this.setDefaultGuiCode(guiFragment.getDefaultGui());
		this.setGuiCode(guiFragment.getGui());
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public WidgetTypeRef getWidgetType() {
		return widgetType;
	}

	public void setWidgetType(WidgetTypeRef widgetType) {
		this.widgetType = widgetType;
	}

	public PluginRef getPlugin() {
		return plugin;
	}

	public void setPlugin(PluginRef plugin) {
		this.plugin = plugin;
	}

	public void addFragmentRef(GuiFragment fragment) {
		if (null == fragment) {
			return;
		}
		FragmentRef ref = new FragmentRef(fragment.getCode());
		this.getFragments().add(ref);
	}

	public List<FragmentRef> getFragments() {
		return fragments;
	}

	public void setFragments(List<FragmentRef> fragments) {
		this.fragments = fragments;
	}

	public void addPageModelRef(PageModel pageModel) {
		if (null == pageModel) {
			return;
		}
		PageModelRef ref = new PageModelRef(pageModel.getCode(), pageModel.getDescription());
		this.getPageModels().add(ref);
	}

	public List<PageModelRef> getPageModels() {
		return pageModels;
	}

	public void setPageModels(List<PageModelRef> pageModels) {
		this.pageModels = pageModels;
	}

	public String getDefaultGuiCode() {
		return defaultGuiCode;
	}

	public void setDefaultGuiCode(String defaultGuiCode) {
		this.defaultGuiCode = defaultGuiCode;
	}

	public String getGuiCode() {
		return guiCode;
	}

	public void setGuiCode(String guiCode) {
		this.guiCode = guiCode;
	}

	protected class WidgetTypeRef {

		private String code;
		private String title;

		public WidgetTypeRef(String code, String title) {
			this.setCode(code);
			this.setTitle(title);
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
	}

	protected class PluginRef {

		private String code;
		private String title;

		public PluginRef(String code, String title) {
			this.setCode(code);
			this.setTitle(title);
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
	}

	protected class FragmentRef {

		private String code;

		public FragmentRef(String code) {
			this.setCode(code);
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

	}

	protected class PageModelRef {

		private String code;
		private String name;

		public PageModelRef(String code, String name) {
			this.setCode(code);
			this.setName(name);
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

}
