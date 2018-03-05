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
import java.util.TreeSet;

import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the representation of a portal page metadata
 * @author E.Mezzano, spuddu
 */
public class PageMetadata implements Cloneable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ApsProperties titles = new ApsProperties();

    private Set<String> extraGroups;

    /**
     * The page model associate to the current object
     */
    private PageModel model;

    /**
     * Toggle menu visibility on and off
     */
    private boolean showable = false;

    private boolean useExtraTitles = false;

    private String mimeType;

    private String charset;

    private Date updatedAt;

    public ApsProperties getTitles() {
        return titles;
    }

    /**
     * Set the titles of the node. 
     * @param titles A set of properties with the titles, 
     * where the keys are the codes of language.
     */
    public void setTitles(ApsProperties titles) {
        this.titles = titles;
    }

    public void setTitle(String langCode, String title) {
        this.getTitles().setProperty(langCode, title);
    }

    /**
     * Return the related model of page
     * @return the page model
     */
    public PageModel getModel() {
        return model;
    }

    /**
     * WARNING: This method is for the page manager service only exclusive use
     * Assign the given page model to the current object
     * @param pageModel the model of the page to assign
     */
    public void setModel(PageModel pageModel) {
        this.model = pageModel;
    }

    public void setExtraGroups(Set<String> extraGroups) {
        this.extraGroups = extraGroups;

    }

    public Set<String> getExtraGroups() {
        return extraGroups;
    }

    /**
     * WARING: this method is reserved to the page manager service only.
     * This returns a boolean values indicating whether the page is
     * displayed in the menus or similar.
     * @return true if the page must be shown in the menu, false otherwise. 
     */
    public boolean isShowable() {
        return showable;
    }

    /**
     * WARING: this method is reserved to the page manager service only.
     * Toggle the visibility of the current page in the menu or similar.
     * @param showable a boolean which toggles the visibility on when true, off otherwise.
     */
    public void setShowable(boolean showable) {
        this.showable = showable;
    }

    public boolean isUseExtraTitles() {
        return useExtraTitles;
    }

    public void setUseExtraTitles(boolean useExtraTitles) {
        this.useExtraTitles = useExtraTitles;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void addExtraGroup(String groupName) {
        if (null == this.getExtraGroups())
            this.setExtraGroups(new HashSet<String>());
        this.getExtraGroups().add(groupName);
    }

    public void removeExtraGroup(String groupName) {
        if (null == this.getExtraGroups())
            return;
        this.getExtraGroups().remove(groupName);
    }

    public String getTitle(String langCode) {
        return this.getTitles().getProperty(langCode);
    }

    @Override
    public PageMetadata clone() throws CloneNotSupportedException {
        PageMetadata copy = null;
        try {
            copy = this.getClass().newInstance();
            ApsProperties titles = new ApsProperties();
            titles.putAll(this.getTitles());
            copy.setTitles(titles);

            Set<String> extraGroups = this.getExtraGroups();
            if (extraGroups != null) {
                copy.setExtraGroups(new TreeSet<String>(extraGroups));
            }

            copy.setModel(this.getModel());
            copy.setShowable(this.isShowable());
            copy.setUseExtraTitles(this.isUseExtraTitles());
            copy.setMimeType(this.getMimeType());
            copy.setCharset(this.getCharset());
            copy.setUpdatedAt(this.getUpdatedAt());
        } catch (Throwable t) {
            logger.error("Error cloning {}" + this.getClass(), t);
            throw new RuntimeException("Error cloning " + this.getClass(), t);
        }
        return copy;
    }

    @Override
    public String toString() {
        return "PageMetadata";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((charset == null) ? 0 : charset.hashCode());
        result = prime * result + ((extraGroups == null) ? 0 : extraGroups.hashCode());
        result = prime * result + ((mimeType == null) ? 0 : mimeType.hashCode());
        result = prime * result + ((model == null) ? 0 : model.hashCode());
        result = prime * result + (showable ? 1231 : 1237);
        result = prime * result + ((titles == null) ? 0 : titles.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        result = prime * result + (useExtraTitles ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        boolean equalConf = this.hasEqualConfiguration(obj);
        if (equalConf) {
            PageMetadata other = (PageMetadata) obj;
            if (updatedAt == null) {
                if (other.updatedAt != null)
                    return false;
            } else if (!updatedAt.equals(other.updatedAt))
                return false;
        }
        return equalConf;
    }

    /**
     * all but lastUpdate
     * @param obj
     * @return
     */
    public boolean hasEqualConfiguration(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PageMetadata other = (PageMetadata) obj;
        if (charset == null) {
            if (other.charset != null)
                return false;
        } else if (!charset.equals(other.charset))
            return false;
        if (extraGroups == null) {
            if (other.extraGroups != null)
                return false;
        } else if (!extraGroups.equals(other.extraGroups))
            return false;
        if (mimeType == null) {
            if (other.mimeType != null)
                return false;
        } else if (!mimeType.equals(other.mimeType))
            return false;
        if (model == null) {
            if (other.model != null)
                return false;
        } else if (!model.equals(other.model))
            return false;
        if (showable != other.showable)
            return false;
        if (titles == null) {
            if (other.titles != null)
                return false;
        } else if (!titles.equals(other.titles))
            return false;
        if (useExtraTitles != other.useExtraTitles)
            return false;
        return true;
    }


}
