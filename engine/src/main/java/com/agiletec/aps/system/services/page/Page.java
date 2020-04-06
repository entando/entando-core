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

import com.agiletec.aps.system.SystemConstants;
import java.util.HashSet;
import java.util.Set;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.common.tree.ITreeNodeManager;
import com.agiletec.aps.system.common.tree.TreeNode;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;
import java.io.Serializable;

/**
 * This is the representation of a portal page
 *
 * @author M.Diana - E.Santoboni
 */
public class Page extends TreeNode implements IPage, Serializable {

    /**
     * The code of the higher level page
     */
    private PageMetadata _metadata = new PageMetadata();
    private Widget[] widgets;
    private boolean online;
    private boolean onlineInstance;
    private boolean changed;
    
    @Override
    public IPage clone() {
        Page clone = (Page) super.clone();
        clone.setChanged(this.isChanged());
        clone.setOnline(this.isOnline());
        clone.setOnlineInstance(this.isOnlineInstance());
        if (null != this.getMetadata()) {
            clone.setMetadata(this.getMetadata().clone());
        }
        if (null != this.getWidgets()) {
            Widget[] clonedWidgets = new Widget[this.getWidgets().length];
            for (int i = 0; i < this.getWidgets().length; i++) {
                Widget widgetToClone = this.getWidgets()[i];
                if (null != widgetToClone) {
                    clonedWidgets[i] = widgetToClone.clone();
                }
            }
            clone.setWidgets(clonedWidgets);
        }
        return clone;
    }
    

    /**
     * Return the related model of page
     *
     * @return the page template
     */
    @Override
    public PageModel getModel() {
        PageMetadata metadata = this.getMetadata();
        return metadata == null ? null : metadata.getModel();
    }

    /**
     * WARNING: This method is for the page manager service only exclusive use
     * Assign the given page template to the current object
     *
     * @param pageModel the model of the page to assign
     */
    public void setModel(PageModel pageModel) {
        PageMetadata metadata = this.getMetadata();
        metadata.setModel(pageModel);
    }
    
    @Override
    public void setGroup(String group) {
        PageMetadata metadata = this.getMetadata();
        if (null != metadata) {
            metadata.setGroup(group);
        }
        super.setGroup(group);
    }

    @Override
    public String getGroup() {
        PageMetadata metadata = this.getMetadata();
        if (null != metadata) {
            return metadata.getGroup();
        }
        return super.getGroup();
    }

    @Override
    public void addExtraGroup(String groupName) {
        PageMetadata metadata = this.getMetadata();
        if (null == metadata.getExtraGroups()) {
            metadata.setExtraGroups(new HashSet<>());
        }
        metadata.getExtraGroups().add(groupName);
    }

    @Override
    public void removeExtraGroup(String groupName) {
        PageMetadata metadata = this.getMetadata();
        if (null == metadata.getExtraGroups()) {
            return;
        }
        metadata.getExtraGroups().remove(groupName);
    }

    public void setExtraGroups(Set<String> extraGroups) {
        PageMetadata metadata = this.getMetadata();
        metadata.setExtraGroups(extraGroups);
    }

    @Override
    public Set<String> getExtraGroups() {
        PageMetadata metadata = this.getMetadata();
        return metadata.getExtraGroups();
    }

    /**
     * WARING: this method is reserved to the page manager service only. This
     * returns a boolean values indicating whether the page is displayed in the
     * menus or similar.
     *
     * @return true if the page must be shown in the menu, false otherwise.
     */
    @Override
    public boolean isShowable() {
        PageMetadata metadata = this.getMetadata();
        return metadata != null && metadata.isShowable();
    }

    /**
     * WARING: this method is reserved to the page manager service only. Toggle
     * the visibility of the current page in the menu or similar.
     *
     * @param showable a boolean which toggles the visibility on when true, off
     * otherwise.
     */
    public void setShowable(boolean showable) {
        PageMetadata metadata = this.getMetadata();
        metadata.setShowable(showable);
    }

    @Override
    public ApsProperties getTitles() {
        PageMetadata metadata = this.getMetadata();
        return metadata.getTitles();
    }

    @Override
    public String getTitle(String langCode) {
        ApsProperties titles = this.getTitles();
        return titles != null ? titles.getProperty(langCode) : null;
    }

    /**
     * Metodo riservato al servizio di gestione pagine. Imposta un titolo alla
     * pagina
     *
     * @param lang La lingua del titolo
     * @param title Il titolo da impostare
     */
    public void setTitle(Lang lang, String title) {
        PageMetadata metadata = this.getMetadata();
        metadata.setTitle(lang.getCode(), title);
    }

    @Override
    public void setTitles(ApsProperties titles) {
        PageMetadata metadata = this.getMetadata();
        metadata.setTitles(titles);
    }

    /**
     * Restituisce il titolo della pagina nella lingua specificata
     *
     * @param lang La lingua
     * @return il titolo della pagina
     */
    public String getTitle(Lang lang) {
        return this.getTitle(lang.getCode());
    }

    @Override
    public void setTitle(String langCode, String title) {
        PageMetadata metadata = this.getMetadata();
        metadata.setTitle(langCode, title);
    }

    @Override
    public boolean isUseExtraTitles() {
        PageMetadata metadata = this.getMetadata();
        return metadata != null && metadata.isUseExtraTitles();
    }

    public void setUseExtraTitles(boolean useExtraTitles) {
        PageMetadata metadata = this.getMetadata();
        metadata.setUseExtraTitles(useExtraTitles);
    }

    @Override
    public String getCharset() {
        PageMetadata metadata = this.getMetadata();
        return metadata == null ? null : metadata.getCharset();
    }

    public void setCharset(String charset) {
        PageMetadata metadata = this.getMetadata();
        metadata.setCharset(charset);
    }

    @Override
    public String getMimeType() {
        PageMetadata metadata = this.getMetadata();
        return metadata == null ? null : metadata.getMimeType();
    }

    public void setMimeType(String mimeType) {
        PageMetadata metadata = this.getMetadata();
        metadata.setMimeType(mimeType);
    }

    public boolean isVoid() {
        boolean isVoid = true;
        Widget[] widgets = this.getWidgets();
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
        return this.online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public boolean isOnlineInstance() {
        return this.onlineInstance;
    }

    public void setOnlineInstance(boolean onlineInstance) {
        this.onlineInstance = onlineInstance;
    }

    @Override
    public boolean isChanged() {
        return this.changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
    
    @Override
    protected ITreeNode getParent(ITreeNode node, ITreeNodeManager treeNodeManager) {
        return (this.isOnlineInstance()) ? 
                ((IPageManager) treeNodeManager).getOnlinePage(node.getParentCode()) :
                ((IPageManager) treeNodeManager).getDraftPage(node.getParentCode());
    }

    @Override
    public String toString() {
        return "Page: " + this.getCode();
    }

    @Override
    public PageMetadata getMetadata() {
        return _metadata;
    }

    @Override
    public void setMetadata(PageMetadata metadata) {
        this._metadata = metadata;
    }

    @Override
    public Widget[] getWidgets() {
        return widgets;
    }

    @Override
    public void setWidgets(Widget[] widgets) {
        this.widgets = widgets;
    }

    @Override
    public String getManagerBeanCode() {
        return SystemConstants.PAGE_MANAGER;
    }

}
