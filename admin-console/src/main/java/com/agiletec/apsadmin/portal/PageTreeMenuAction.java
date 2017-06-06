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
package com.agiletec.apsadmin.portal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.api.IApiCatalogManager;
import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.aps.system.services.page.IPageTokenManager;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.portal.helper.IPageActionHelper;

/**
 * Action per la gestione dell'albero delle pagine della sidebar a destra nella
 * pagina configuratore delle pagine.
 *
 * @author A.Pintus
 */
public class PageTreeMenuAction extends PageTreeAction {

	private static final Logger _logger = LoggerFactory.getLogger(PageTreeMenuAction.class);

	public String intro() {
		String pageCode = (this.getSelectedNode() != null ? this.getSelectedNode() : this.getPageCode());
		if (StringUtils.isBlank(pageCode)) {
			ITreeNode rootNode = this.getAllowedTreeRootNode();
			pageCode = rootNode.getCode();

			if (VIRTUAL_ROOT_CODE.equals(pageCode)) {
				if (null != rootNode.getChildren() && rootNode.getChildren().length > 0) {
					pageCode = rootNode.getChildren()[0].getCode();
				} else {
					this.addActionError(this.getText("error.page.virtualRootSelected"));
					return "noRoot";
				}
			}

		}
		this.setPageCode(pageCode);
		this.setSelectedNode(pageCode);
		String check = this.checkSelectedNode(pageCode);
		if (null != check) {
			return check;
		}
		return SUCCESS;
	}

	@Override
	public String buildTree() {
		this.intro();
		return super.buildTree();
	}

	public IPage getCurrentPage() {
		return this.getPage(this.getPageCode());
	}

	@Deprecated
	public List<List<SelectItem>> getShowletFlavours() {
		return this.getWidgetFlavours();
	}

	public List<List<SelectItem>> getWidgetFlavours() {
		List<String> pluginCodes = new ArrayList<String>();
		Map<String, List<SelectItem>> mapping = this.getWidgetFlavoursMapping(pluginCodes);
		List<List<SelectItem>> group = new ArrayList<List<SelectItem>>();
		try {
			this.addGroup(USER_WIDGETS_CODE, mapping, group);
			this.addGroup(CUSTOM_WIDGETS_CODE, mapping, group);
			for (int i = 0; i < pluginCodes.size(); i++) {
				String pluginCode = pluginCodes.get(i);
				this.addGroup(pluginCode, mapping, group);
			}
			this.addGroup(STOCK_WIDGETS_CODE, mapping, group);
		} catch (Throwable t) {
			_logger.error("error in getWidgetFlavours", t);
			throw new RuntimeException("Error extracting Widget flavours", t);
		}
		return group;
	}

	@Deprecated
	protected Map<String, List<SelectItem>> getShowletFlavoursMapping(List<String> pluginCodes) {
		return this.getWidgetFlavoursMapping(pluginCodes);
	}

	protected Map<String, List<SelectItem>> getWidgetFlavoursMapping(List<String> pluginCodes) {
		Map<String, List<SelectItem>> mapping = new HashMap<String, List<SelectItem>>();
		List<WidgetType> types = this.getWidgetTypeManager().getWidgetTypes();
		for (int i = 0; i < types.size(); i++) {
			WidgetType type = types.get(i);
			String pluginCode = type.getPluginCode();
			if (null != pluginCode && pluginCode.trim().length() > 0) {
				//is a plugin's widgets
				if (!pluginCodes.contains(pluginCode)) {
					pluginCodes.add(pluginCode);
				}
				this.addFlavourWidgetType(pluginCode, type, mapping);
			} else if (type.isUserType()) {
				//is a user widgets
				this.addFlavourWidgetType(USER_WIDGETS_CODE, type, mapping);
			} else if (this.getStockWidgetCodes().contains(type.getCode())) {
				this.addFlavourWidgetType(STOCK_WIDGETS_CODE, type, mapping);
			} else {
				this.addFlavourWidgetType(CUSTOM_WIDGETS_CODE, type, mapping);
			}
		}
		Collections.sort(pluginCodes);
		return mapping;
	}

	@Deprecated
	protected void addFlavourShowletType(String mapCode, WidgetType type, Map<String, List<SelectItem>> mapping) {
		this.addFlavourWidgetType(mapCode, type, mapping);
	}

	protected void addFlavourWidgetType(String mapCode, WidgetType type, Map<String, List<SelectItem>> mapping) {
		List<SelectItem> widgetTypes = mapping.get(mapCode);
		if (null == widgetTypes) {
			widgetTypes = new ArrayList<SelectItem>();
			mapping.put(mapCode, widgetTypes);
		}
		String title = super.getTitle(type.getCode(), type.getTitles());
		SelectItem item = new SelectItem(type.getCode(), title, mapCode);
		widgetTypes.add(item);
	}

	private void addGroup(String code, Map<String, List<SelectItem>> mapping, List<List<SelectItem>> group) {
		List<SelectItem> singleGroup = mapping.get(code);
		if (null != singleGroup) {
			BeanComparator comparator = new BeanComparator("value");
			Collections.sort(singleGroup, comparator);
			group.add(singleGroup);
		}
	}

	/**
	 * Returns the 'bread crumbs' targets.
	 *
	 * @param pageCode
	 * The code of the page being represented in the bread crumbs path.
	 * @return The bread crumbs targets requested.
	 */
	public List<IPage> getBreadCrumbsTargets(String pageCode) {
		IPage page = this.getPage(pageCode);
		if (null == page) {
			return null;
		}
		List<IPage> pages = new ArrayList<IPage>();
		this.getSubBreadCrumbsTargets(pages, page);
		return pages;
	}

	private void getSubBreadCrumbsTargets(List<IPage> pages, IPage current) {
		pages.add(0, current);
		IPage parent = current.getParent();
		if (parent != null && !parent.getCode().equals(current.getCode())) {
			this.getSubBreadCrumbsTargets(pages, parent);
		}
	}

	/**
	 * Check if the current user can access the specified page.
	 *
	 * @param page
	 * The page to check against the current user.
	 * @return True if the user has can access the given page, false otherwise.
	 */
	@Override
	public boolean isUserAllowed(IPage page) {
		if (page == null) {
			return false;
		}
		String pageGroup = page.getGroup();
		return this.isCurrentUserMemberOf(pageGroup);
	}

	@Override
	protected String checkSelectedNode(String selectedNode) {
		if (null == selectedNode || selectedNode.trim().length() == 0) {
			this.addActionError(this.getText("error.page.noSelection"));
			return "pageTree";
		}
		if (VIRTUAL_ROOT_CODE.equals(selectedNode)) {
			this.addActionError(this.getText("error.page.virtualRootSelected"));
			return "pageTree";
		}
		IPage selectedPage = this.getPage(selectedNode);
		if (null == selectedPage) {
			this.addActionError(this.getText("error.page.selectedPage.null"));
			return "pageTree";
		}
		if (!this.isUserAllowed(selectedPage)) {
			this.addActionError(this.getText("error.page.userNotAllowed"));
			return "pageTree";
		}
		return null;
	}

	@Override
	public IPage getPage(String pageCode) {
		return this.getPageManager().getDraftPage(pageCode);
	}

	/**
	 * Return the list of the system languages. The default language is placed
	 * first.
	 *
	 * @return The list of the system languages.
	 */
	public List<Lang> getLangs() {
		return this.getLangManager().getLangs();
	}

	/**
	 * Return the map of the system groups. The map is indexed by the group
	 * name.
	 *
	 * @return The map containing the system groups.
	 */
	public Map<String, Group> getSystemGroups() {
		return this.getGroupManager().getGroupsMap();
	}

	@Deprecated
	public Map<String, ApiMethod> getShowletTypeApiMappings() {
		return this.getWidgetTypeApiMappings();
	}

	public Map<String, ApiMethod> getWidgetTypeApiMappings() {
		Map<String, ApiMethod> mappings = null;
		try {
			mappings = this.getApiCatalogManager().getRelatedWidgetMethods();
		} catch (Throwable t) {
			_logger.error("error in getWidgetTypeApiMappings", t);
		}
		return mappings;
	}

	public boolean isInternalServletWidget(String widgetTypeCode) {
		return this.getInternalServletWidgetCode().equals(widgetTypeCode);
	}

	@Deprecated
	protected String getStockShowletCodes() {
		return this.getStockWidgetCodes();
	}

	@Deprecated
	public void setStockShowletCodes(String stockShowletCodes) {
		this.setStockWidgetCodes(stockShowletCodes);
	}

	protected String getStockWidgetCodes() {
		return _stockWidgetCodes;
	}

	public void setStockWidgetCodes(String stockWidgetCodes) {
		this._stockWidgetCodes = stockWidgetCodes;
	}

	protected String getInternalServletWidgetCode() {
		return _internalServletWidgetCode;
	}

	public void setInternalServletWidgetCode(String internalServletWidgetCode) {
		this._internalServletWidgetCode = internalServletWidgetCode;
	}

	protected IGroupManager getGroupManager() {
		return _groupManager;
	}

	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}

	protected IApiCatalogManager getApiCatalogManager() {
		return _apiCatalogManager;
	}

	public void setApiCatalogManager(IApiCatalogManager apiCatalogManager) {
		this._apiCatalogManager = apiCatalogManager;
	}

	protected IWidgetTypeManager getWidgetTypeManager() {
		return _widgetTypeManager;
	}

	public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
		this._widgetTypeManager = widgetTypeManager;
	}

	private String _stockWidgetCodes;
	private String _internalServletWidgetCode;

	private IGroupManager _groupManager;

	private IWidgetTypeManager _widgetTypeManager;
	private IApiCatalogManager _apiCatalogManager;

	/**
	 * This is the code of an abstract page which identifies a 'virtual'
	 * container of all the pages which can be viewed by the current page
	 * administrator.
	 */
	public static final String VIRTUAL_ROOT_CODE = "VIRTUAL_PAGE_ROOT";

	public static final String USER_WIDGETS_CODE = "userShowletCode";
	public static final String CUSTOM_WIDGETS_CODE = "customShowletCode";
	public static final String STOCK_WIDGETS_CODE = "stockShowletCode";

	/**
	 * @deprecated Use {@link #USER_WIDGETS_CODE} instead
	 */
	public static final String USER_SHOWLETS_CODE = USER_WIDGETS_CODE;

	/**
	 * @deprecated Use {@link #CUSTOM_WIDGETS_CODE} instead
	 */
	public static final String CUSTOM_SHOWLETS_CODE = CUSTOM_WIDGETS_CODE;

	/**
	 * @deprecated Use {@link #STOCK_WIDGETS_CODE} instead
	 */
	public static final String STOCK_SHOWLETS_CODE = STOCK_WIDGETS_CODE;

	@Deprecated
	public String viewShowlets() {
		return viewWidgets();
	}

	public String viewWidgets() {
		return SUCCESS;
	}

	@Deprecated
	public List<IPage> getShowletUtilizers(String widgetTypeCode) {
		return this.getWidgetUtilizers(widgetTypeCode);
	}

	public List<IPage> getWidgetUtilizers(String widgetTypeCode) {
		List<IPage> utilizers = null;
		try {
			utilizers = this.getPageManager().getOnlineWidgetUtilizers(widgetTypeCode);
		} catch (Throwable t) {
			_logger.error("Error on extracting widgetUtilizers : widget type code {}", t);
			throw new RuntimeException("Error on extracting widgetUtilizers : widget type code " + widgetTypeCode, t);
		}
		return utilizers;
	}

	public Group getGroup(String groupCode) {
		Group group = this.getGroupManager().getGroup(groupCode);
		if (null == group) {
			group = this.getGroupManager().getGroup(Group.FREE_GROUP_NAME);
		}
		return group;
	}

	public String getPreviewToken() {
		if (StringUtils.isNotBlank(this.getPageCode())) {
			return this.pageTokenMager.encrypt(this.getPageCode());
		}
		return null;
	}

	@Deprecated
	public String viewShowletUtilizers() {
		return viewWidgetUtilizers();
	}

	public String viewWidgetUtilizers() {
		return SUCCESS;
	}

	public List<IPage> getWidgetUtilizers() {
		return this.getWidgetUtilizers(this.getWidgetTypeCode());
	}

	@Deprecated
	public List<IPage> getShowletUtilizers() {
		return this.getWidgetUtilizers();
	}

	public WidgetType getWidgetType(String typeCode) {
		return this.getWidgetTypeManager().getWidgetType(typeCode);
	}

	@Deprecated
	public WidgetType getShowletType(String typeCode) {
		return this.getWidgetType(typeCode);
	}

	@Deprecated
	public String getShowletTypeCode() {
		return this.getWidgetTypeCode();
	}

	@Deprecated
	public void setShowletTypeCode(String widgetTypeCode) {
		this._widgetTypeCode = widgetTypeCode;
	}

	public String getWidgetTypeCode() {
		return _widgetTypeCode;
	}

	public void setWidgetTypeCode(String widgetTypeCode) {
		this._widgetTypeCode = widgetTypeCode;
	}

	protected IPageActionHelper getPageActionHelper() {
		return _pageActionHelper;
	}

	public void setPageActionHelper(IPageActionHelper pageActionHelper) {
		this._pageActionHelper = pageActionHelper;
	}

	public String getPageCode() {
		return pageCode;
	}

	public void setPageCode(String pageCode) {
		this.pageCode = pageCode;
	}

	public IPageTokenManager getPageTokenMager() {
		return pageTokenMager;
	}

	public void setPageTokenMager(IPageTokenManager pageTokenMager) {
		this.pageTokenMager = pageTokenMager;
	}

	private String _widgetTypeCode;
	private IPageActionHelper _pageActionHelper;
	private String pageCode;
	private IPageTokenManager pageTokenMager;

}
