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
import java.util.HashMap;
import java.util.Map;

/**
 * Base resource data bean
 *
 * @author E.Santoboni
 */
public class BaseResourceDataBean implements ResourceDataBean {

    private String resourceId;
    private String resourceType;
    private String description;
    private String mainGroup;
    private File file;
    private List<Category> categories = new ArrayList<Category>();
    private String mimeType;
    private String fileName;
    private Map<String, String> metadata = new HashMap<String, String>();
    
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
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    @Override
    public String getDescr() {
        return description;
    }

    public void setDescr(String descr) {
        this.description = descr;
    }

    @Override
    public String getMainGroup() {
        return mainGroup;
    }

    public void setMainGroup(String mainGroup) {
        this.mainGroup = mainGroup;
    }

    @Override
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public int getFileSize() {
        if (null == file) {
            return 0;
        }
        return (int) this.getFile().length() / 1000;
    }

    @Override
    public String getFileName() {
        if (null != this.fileName) {
            return fileName;
        }
        if (null == file) {
            return null;
        }
        String fullName = this.getFile().getName();
        return fullName.substring(fullName.lastIndexOf('/') + 1).trim();
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public InputStream getInputStream() throws Throwable {
        if (null == file) {
            return null;
        }
        return new FileInputStream(this.file);
    }
    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

 
}
