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

import com.agiletec.aps.system.common.tree.TreeNode;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;

/**
 * This is the representation of a portal page
 * @author M.Diana - E.Santoboni
 */
public class Page extends TreeNode implements IPage {
	
	/**
	 * Set the position of the page with regard to its sisters
	 * @param position the position of the page.
	 */
	@Override
	protected void setPosition(int position) {
		super.setPosition(position);
	}
	
	@Override
	public IPage getParent() {
		return (IPage) super.getParent();
	}

	/**
	 * WARING: this method is reserved to the page manager service only.
	 * Return the code of the father of this page. This methods exists only to
	 * simplify the loading of the pages structure, it cannot be used in any other 
	 * circumstance.
	 * @return the code of the higher level page
	 */
	@Override
	public String getParentCode() {
		return _parentCode;
	}

	/**
	 * WARING: this method is reserved to the page manager service only.
	 * Set the code of the father of this page. This methods exists only to
	 * simplify the loading of the pages structure, it cannot be used in any other 
	 * circumstance.
	 * @param parentCode the code of the higher level page
	 */
	public void setParentCode(String parentCode) {
		this._parentCode = parentCode;
	}
	
	@Override
	public PageMetadata getOnlineMetadata() {
		return _onlineMetadata;
	}
	public void setOnlineMetadata(PageMetadata onlineMetadata) {
		this._onlineMetadata = onlineMetadata;
	}
	
	@Override
	public PageMetadata getDraftMetadata() {
		return _draftMetadata;
	}
	public void setDraftMetadata(PageMetadata draftMetadata) {
		this._draftMetadata = draftMetadata;
	}
	
	@Override
	public Widget[] getOnlineWidgets() {
		return _onlineWidgets;
	}
	public void setOnlineWidgets(Widget[] onlineWidgets) {
		this._onlineWidgets = onlineWidgets;
	}
	
	@Override
	public Widget[] getDraftWidgets() {
		return _draftWidgets;
	}
	public void setDraftWidgets(Widget[] draftWidgets) {
		this._draftWidgets = draftWidgets;
	}
	
	/**
	 * Return the related model of page
	 * @return the page model
	 */
	@Override
	public PageModel getModel() {// TODO Verify usage
		PageMetadata metadata = this.getOnlineMetadata();
		return metadata == null ? null : metadata.getModel();
	}

	/**
	 * WARNING: This method is for the page manager service only exclusive use
	 * Assign the given page model to the current object
	 * @param pageModel the model of the page to assign
	 * @deprecated Use getOnlineMetadata().setModel(pageModel)
	 */
	@Deprecated
	public void setModel(PageModel pageModel) {
		PageMetadata metadata = this.getOnlineMetadata();
		if (metadata != null) {
			metadata.setModel(pageModel);
		}
	}
	
	@Override
	@Deprecated
	public void addExtraGroup(String groupName) {
		PageMetadata metadata = this.getOnlineMetadata();
		if (metadata != null) {
			if (null == metadata.getExtraGroups()) metadata.setExtraGroups(new HashSet<String>());
			metadata.getExtraGroups().add(groupName);
		}
	}
	
	@Override
	@Deprecated
	public void removeExtraGroup(String groupName) {
		PageMetadata metadata = this.getOnlineMetadata();
		if (metadata != null) {
			if (null == metadata.getExtraGroups()) return;
			metadata.getExtraGroups().remove(groupName);
		}
	}
	
	@Deprecated
	public void setExtraGroups(Set<String> extraGroups) {
		PageMetadata metadata = this.getOnlineMetadata();
		if (metadata != null) {
			metadata.setExtraGroups(extraGroups);
		}
	}
	
	@Override
	public Set<String> getExtraGroups() {// TODO Verify usage
		PageMetadata metadata = this.getOnlineMetadata();
		return metadata == null ? null : metadata.getExtraGroups();
	}

	@Override
	public IPage[] getChildren() {// TODO Verify usage
		return this.getOnlineChildren();
	}
	
	@Override
	public IPage[] getOnlineChildren() {
		IPage[] children = new IPage[super.getChildren().length];
		for (int i=0; i<super.getChildren().length; i++) {
			children[i] = (IPage) super.getChildren()[i];
		}
		return children;
	}
	
	/**
	 * WARING: this method is reserved to the page manager service only.
	 * This returns a boolean values indicating whether the page is
	 * displayed in the menus or similar.
	 * @return true if the page must be shown in the menu, false otherwise. 
	 */
	@Override
	public boolean isShowable() {// TODO Verify usage
		PageMetadata metadata = this.getOnlineMetadata();
		return metadata != null && metadata.isShowable();
	}

	/**
	 * WARING: this method is reserved to the page manager service only.
	 * Toggle the visibility of the current page in the menu or similar.
	 * @param showable a boolean which toggles the visibility on when true, off otherwise.
	 */
	@Deprecated
	public void setShowable(boolean showable) {
		PageMetadata metadata = this.getOnlineMetadata();
		if (metadata != null) {
			metadata.setShowable(showable);
		}
	}
	
	@Override
	public ApsProperties getTitles() {// TODO Verify usage
		PageMetadata metadata = this.getOnlineMetadata();
		return metadata == null ? null : metadata.getTitles();
	}
	
	@Override
	@Deprecated
	public void setTitles(ApsProperties titles) {
		PageMetadata metadata = this.getOnlineMetadata();
		if (metadata != null) {
			metadata.setTitles(titles);
		}
	}

	/**
	 * Metodo riservato al servizio di gestione pagine.
	 * Imposta un titolo alla pagina
	 * @param lang La lingua del titolo
	 * @param title Il titolo da impostare
	 * @deprecated Use setTitle(String, String)
	 */
	public void setTitle(Lang lang, String title){
		this.setTitle(lang.getCode(), title);
	}

	/**
	 * Restituisce il titolo della pagina nella lingua specificata
	 * @param lang La lingua
	 * @return il titolo della pagina
	 * @deprecated Use getTitle(String)
	 */
	public String getTitle(Lang lang) {
		return this.getTitle(lang.getCode());
	}
	
	@Override
	@Deprecated
	public Widget[] getShowlets() {
		return this.getOnlineWidgets();
	}

	/**
	 * Return the widgets configured in this page.
	 * @return all the widgets of the current page
	 */
	@Override
	public Widget[] getWidgets() {// TODO Verify usage
		return this.getOnlineWidgets();
	}
	
	@Deprecated
	public void setShowlets(Widget[] widgets) {
		this.setOnlineWidgets(widgets);
	}

	/**
	 * Assign a set of widgets to the current page.
	 * @param widgets the widgets to assign.
	 */
	public void setWidgets(Widget[] widgets) {// TODO Verify usage
		this.setOnlineWidgets(widgets);
	}
	
	@Override
	public boolean isUseExtraTitles() {// TODO Verify usage
		PageMetadata metadata = this.getOnlineMetadata();
		return metadata != null && metadata.isUseExtraTitles();
	}
	@Deprecated
	public void setUseExtraTitles(boolean useExtraTitles) {
		PageMetadata metadata = this.getOnlineMetadata();
		if (metadata != null) {
			metadata.setUseExtraTitles(useExtraTitles);
		}
	}
	
	@Override
	public String getCharset() {// TODO Verify usage
		PageMetadata metadata = this.getOnlineMetadata();
		return metadata == null ? null : metadata.getCharset();
	}
	@Deprecated
	public void setCharset(String charset) {
		PageMetadata metadata = this.getOnlineMetadata();
		if (metadata != null) {
			metadata.setCharset(charset);
		}
	}
	
	@Override
	public String getMimeType() {// TODO Verify usage
		PageMetadata metadata = this.getOnlineMetadata();
		return metadata == null ? null : metadata.getMimeType();
	}
	@Deprecated
	public void setMimeType(String mimeType) {
		PageMetadata metadata = this.getOnlineMetadata();
		if (metadata != null) {
			metadata.setMimeType(mimeType);
		}
	}
	
	public boolean isVoid() {
		boolean isVoid = true;
		Widget[] widgets = this.getOnlineWidgets();
		if (null != widgets) {
			for (int i = 0; i < widgets.length; i++) {
				if (null != widgets[i]) {
					isVoid = false;
					break;
				}
			}
		}
		return isVoid;
	}
	
	@Override
	public boolean isOnline() {
		return this.getOnlineMetadata() != null;
	}
	
	@Override
	public boolean isChanged() {
		boolean changed = false;
		PageMetadata onlineMeta = this.getOnlineMetadata();
		if (onlineMeta != null) {
			PageMetadata draftMeta = this.getDraftMetadata();
			if (draftMeta != null) {
				Date onlineUpdate = onlineMeta.getUpdatedAt();
				Date draftUpdate = draftMeta.getUpdatedAt();
				changed = onlineUpdate == null || !onlineUpdate.equals(draftUpdate);
			} else {
				changed = true;
			}
		}
		return changed;
	}
	
	@Override
	public IPage[] getAllChildren() {
		return _allChildren;
	}
	public void setAllChildren(IPage[] children) {
		this._allChildren = children;
	}
	
	/**
	 * Adds a node to nodes in a lower level. 
	 * The new node is inserted in the final position.
	 * @param treeNode The node to add.
	 */
	public void addChild(IPage treeNode) {
		if (treeNode.isOnline()) {
			super.addChild(treeNode);
		}
		int len = this._allChildren.length;
		IPage[] newChildren = new IPage[len + 1];
		for (int i=0; i < len; i++) {
			newChildren[i] = this._allChildren[i];
		}
		newChildren[len] = treeNode;
		this._allChildren = newChildren;
	}
	
	@Override
	public String toString() {
		return "Page: " + this.getCode();
	}

	/**
	 * The code of the higher level page
	 */
	private String _parentCode;
	
	private PageMetadata _onlineMetadata;
	
	private PageMetadata _draftMetadata;
	
	private Widget[] _onlineWidgets;
	
	private Widget[] _draftWidgets;
	
	private IPage[] _allChildren = new IPage[0];
	
}