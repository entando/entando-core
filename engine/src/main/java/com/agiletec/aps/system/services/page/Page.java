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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.common.tree.TreeNode;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;

/**
 * This is the representation of a portal page
 *
 * @author M.Diana - E.Santoboni
 */
public class Page extends TreeNode implements IPage, Serializable {

    @Override
    public String getUtilizerId() {
        return this.getCode();
    }

    /**
     * Set the position of the page with regard to its sisters
     *
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
     * WARING: this method is reserved to the page manager service only. Return
     * the code of the father of this page. This methods exists only to simplify
     * the loading of the pages structure, it cannot be used in any other
     * circumstance.
     *
     * @return the code of the higher level page
     */
    @Override
    public String getParentCode() {
        return _parentCode;
    }

    /**
     * WARING: this method is reserved to the page manager service only. Set the
     * code of the father of this page. This methods exists only to simplify the
     * loading of the pages structure, it cannot be used in any other
     * circumstance.
     *
     * @param parentCode the code of the higher level page
     */
    public void setParentCode(String parentCode) {
        this._parentCode = parentCode;
    }

    /**
     * Return the related model of page
     *
     * @return the page model
     */
    @Override
    public PageModel getModel() {
        PageMetadata metadata = this.getMetadata();
        return metadata == null ? null : metadata.getModel();
    }

    /**
     * WARNING: This method is for the page manager service only exclusive use
     * Assign the given page model to the current object
     *
     * @param pageModel the model of the page to assign
     */
    public void setModel(PageModel pageModel) {
        PageMetadata metadata = this.getMetadata();
        metadata.setModel(pageModel);
    }

    @Override
    public void addExtraGroup(String groupName) {
        PageMetadata metadata = this.getMetadata();
        if (metadata.getExtraGroups() == null) {
            metadata.setExtraGroups(new HashSet<String>());
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

    protected void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public boolean isOnlineInstance() {
        return this.onlineInstance;
    }

    protected void setOnlineInstance(boolean onlineInstance) {
        this.onlineInstance = onlineInstance;
    }

    @Override
    public boolean isChanged() {
        return this.changed;
    }

    protected void setChanged(boolean changed) {
        this.changed = changed;
    }

    @Override
    protected String getFullTitle(String langCode, String separator, boolean shortTitle) {
        String title = this.getTitles().getProperty(langCode);
        if (null == title) {
            title = this.getCode();
        }
        if (this.isRoot()) {
            return title;
        }
        ITreeNode parent = this.getParent();
        while (parent != null && parent.getParent() != null) {
            String parentTitle = "..";
            if (!shortTitle) {
                parentTitle = parent.getTitles().getProperty(langCode);
                if (null == parentTitle) {
                    parentTitle = parent.getCode();
                }
            }
            title = parentTitle + separator + title;
            if (parent.isRoot()) {
                return title;
            }
            parent = parent.getParent();
        }
        return title;
    }

    @Override
    public String toString() {
        return "Page: " + this.getCode();
    }

    @Override
    public PageMetadata getMetadata() {
        return _metadata;
    }

    public void setMetadata(PageMetadata metadata) {
        this._metadata = metadata;
    }

    @Override
    public Widget[] getWidgets() {
        return widgets;
    }

    public void setWidgets(Widget[] widgets) {
        this.widgets = widgets;
    }

    /**
     * The code of the higher level page
     */
    private String _parentCode;
    private PageMetadata _metadata = new PageMetadata();
    private Widget[] widgets;
    private boolean online;
    private boolean onlineInstance;
    private boolean changed;


}
