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
package com.agiletec.plugins.jacms.aps.system.services.resource.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.services.category.Category;

/**
 * Base resource data bean
 *
 * @author E.Santoboni
 */
public class BaseResourceDataBean implements ResourceDataBean {

    public BaseResourceDataBean() {
    }

    public BaseResourceDataBean(File file) {
        if (null == file) {
            throw new RuntimeException("Null File");
        }
        this.setFile(file);
    }

    @Override
    public String getResourceId() {
        return _resourceId;
    }

    public void setResourceId(String resourceId) {
        this._resourceId = resourceId;
    }

    @Override
    public String getResourceType() {
        return _resourceType;
    }

    public void setResourceType(String resourceType) {
        this._resourceType = resourceType;
    }

    @Override
    public String getDescr() {
        return _description;
    }

    public void setDescr(String descr) {
        this._description = descr;
    }

    @Override
    public String getMainGroup() {
        return _mainGroup;
    }

    public void setMainGroup(String mainGroup) {
        this._mainGroup = mainGroup;
    }

    @Override
    public File getFile() {
        return _file;
    }

    public void setFile(File file) {
        this._file = file;
    }

    @Override
    public List<Category> getCategories() {
        return _categories;
    }

    public void setCategories(List<Category> categories) {
        this._categories = categories;
    }

    @Override
    public String getMimeType() {
        return _mimeType;
    }

    public void setMimeType(String mimeType) {
        this._mimeType = mimeType;
    }

    @Override
    public int getFileSize() {
        if (null == _file) {
            return 0;
        }
        return (int) this.getFile().length() / 1000;
    }

    @Override
    public String getFileName() {
        if (null != this._fileName) {
            return _fileName;
        }
        if (null == _file) {
            return null;
        }
        String fullName = this.getFile().getName();
        return fullName.substring(fullName.lastIndexOf('/') + 1).trim();
    }

    public void setFileName(String fileName) {
        this._fileName = fileName;
    }

    @Override
    public InputStream getInputStream() throws Throwable {
        if (null == _file) {
            return null;
        }
        return new FileInputStream(this._file);
    }

    private String _resourceId;
    private String _resourceType;
    private String _description;
    private String _mainGroup;
    private File _file;
    private List<Category> _categories = new ArrayList<Category>();
    private String _mimeType;
    private String _fileName;

}
