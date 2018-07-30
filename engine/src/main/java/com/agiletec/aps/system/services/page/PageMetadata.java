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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;
import java.io.Serializable;

/**
 * This is the representation of a portal page metadata
 *
 * @author E.Mezzano, spuddu
 */
public class PageMetadata implements Cloneable, Serializable {

    private static final Logger _logger = LoggerFactory.getLogger(PageMetadata.class);

    @Override
    public PageMetadata clone() throws CloneNotSupportedException {
        PageMetadata copy = null;
        try {
            copy = this.getClass().newInstance();
            copy.setGroup(this.getGroup());
            ApsProperties titles = new ApsProperties();
            titles.putAll(this.getTitles());
            copy.setTitles(titles);
            Set<String> extraGroups = this.getExtraGroups();
            if (extraGroups != null) {
                copy.setExtraGroups(new TreeSet<>(extraGroups));
            }
            copy.setModel(this.getModel());
            copy.setShowable(this.isShowable());
            copy.setUseExtraTitles(this.isUseExtraTitles());
            copy.setMimeType(this.getMimeType());
            copy.setCharset(this.getCharset());
            copy.setUpdatedAt(this.getUpdatedAt());
        } catch (Throwable t) {
            _logger.error("Error cloning {}" + this.getClass(), t);
            throw new RuntimeException("Error cloning " + this.getClass(), t);
        }
        return copy;
    }

    public String getGroup() {
        return _group;
    }

    public void setGroup(String group) {
        this._group = group;
    }

    public ApsProperties getTitles() {
        return _titles;
    }

    /**
     * Set the titles of the node.
     *
     * @param titles A set of properties with the titles, where the keys are the
     * codes of language.
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
     *
     * @return the page model
     */
    public PageModel getModel() {
        return _model;
    }

    /**
     * WARNING: This method is for the page manager service only exclusive use
     * Assign the given page model to the current object
     *
     * @param pageModel the model of the page to assign
     */
    public void setModel(PageModel pageModel) {
        this._model = pageModel;
    }

    public void addExtraGroup(String groupName) {
        if (null == this.getExtraGroups()) {
            this.setExtraGroups(new HashSet<String>());
        }
        this.getExtraGroups().add(groupName);
    }

    public void removeExtraGroup(String groupName) {
        if (null == this.getExtraGroups()) {
            return;
        }
        this.getExtraGroups().remove(groupName);
    }

    public void setExtraGroups(Set<String> extraGroups) {
        this._extraGroups = extraGroups;

    }

    public Set<String> getExtraGroups() {
        return _extraGroups;
    }

    /**
     * WARING: this method is reserved to the page manager service only. This
     * returns a boolean values indicating whether the page is displayed in the
     * menus or similar.
     *
     * @return true if the page must be shown in the menu, false otherwise.
     */
    public boolean isShowable() {
        return _showable;
    }

    /**
     * WARING: this method is reserved to the page manager service only. Toggle
     * the visibility of the current page in the menu or similar.
     *
     * @param showable a boolean which toggles the visibility on when true, off
     * otherwise.
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_group == null) ? 0 : _group.hashCode());
        result = prime * result + ((_charset == null) ? 0 : _charset.hashCode());
        result = prime * result + ((_extraGroups == null) ? 0 : _extraGroups.hashCode());
        result = prime * result + ((_mimeType == null) ? 0 : _mimeType.hashCode());
        result = prime * result + ((_model == null) ? 0 : _model.hashCode());
        result = prime * result + (_showable ? 1231 : 1237);
        result = prime * result + ((_titles == null) ? 0 : _titles.hashCode());
        result = prime * result + ((_updatedAt == null) ? 0 : _updatedAt.hashCode());
        result = prime * result + (_useExtraTitles ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        boolean equalConf = this.hasEqualConfiguration(obj);
        if (equalConf) {
            PageMetadata other = (PageMetadata) obj;
            if (_updatedAt == null) {
                if (other._updatedAt != null) {
                    return false;
                }
            } else if (!_updatedAt.equals(other._updatedAt)) {
                return false;
            }
        }
        return equalConf;
    }

    /**
     * all but lastUpdate
     *
     * @param obj
     * @return
     */
    public boolean hasEqualConfiguration(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PageMetadata other = (PageMetadata) obj;
        if (_charset == null) {
            if (other._charset != null) {
                return false;
            }
        } else if (!_charset.equals(other._charset)) {
            return false;
        }
        if (_extraGroups == null) {
            if (other._extraGroups != null) {
                return false;
            }
        } else if (!_extraGroups.equals(other._extraGroups)) {
            return false;
        }
        if (_mimeType == null) {
            if (other._mimeType != null) {
                return false;
            }
        } else if (!_mimeType.equals(other._mimeType)) {
            return false;
        }
        if (_group == null) {
            if (other._group != null) {
                return false;
            }
        } else if (!_group.equals(other._group)) {
            return false;
        }
        if (_model == null) {
            if (other._model != null) {
                return false;
            }
        } else if (!_model.equals(other._model)) {
            return false;
        }
        if (_showable != other._showable) {
            return false;
        }
        if (_titles == null) {
            if (other._titles != null) {
                return false;
            }
        } else if (!_titles.equals(other._titles)) {
            return false;
        }
        if (_useExtraTitles != other._useExtraTitles) {
            return false;
        }
        return true;
    }

    private String _group;

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
