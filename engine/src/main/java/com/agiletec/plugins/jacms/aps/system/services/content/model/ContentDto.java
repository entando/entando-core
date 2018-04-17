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
package com.agiletec.plugins.jacms.aps.system.services.content.model;

import java.io.Serializable;
import org.entando.entando.aps.system.services.entity.model.EntityDto;
import java.util.Date;

public class ContentDto extends EntityDto implements Serializable {

    private String status;
    private boolean onLine;
    private String viewPage;
    private String listModel;
    private String defaultModel;

    private Date created;
    private Date lastModified;

    private String version;
    private String firstEditor;
    private String lastEditor;

    public ContentDto() {
        super();
    }

    public ContentDto(Content src) {
        super(src);
        this.setStatus(src.getStatus());
        this.setOnLine(src.isOnLine());
        this.setViewPage(src.getViewPage());
        this.setListModel(src.getListModel());
        this.setDefaultModel(src.getDefaultModel());
        this.setCreated(src.getCreated());
        this.setLastModified(src.getLastModified());
        this.setVersion(src.getVersion());
        this.setFirstEditor(src.getFirstEditor());
        this.setLastEditor(src.getLastEditor());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isOnLine() {
        return onLine;
    }

    public void setOnLine(boolean onLine) {
        this.onLine = onLine;
    }

    public String getViewPage() {
        return viewPage;
    }

    public void setViewPage(String viewPage) {
        this.viewPage = viewPage;
    }

    public String getListModel() {
        return listModel;
    }

    public void setListModel(String listModel) {
        this.listModel = listModel;
    }

    public String getDefaultModel() {
        return defaultModel;
    }

    public void setDefaultModel(String defaultModel) {
        this.defaultModel = defaultModel;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFirstEditor() {
        return firstEditor;
    }

    public void setFirstEditor(String firstEditor) {
        this.firstEditor = firstEditor;
    }

    public String getLastEditor() {
        return lastEditor;
    }

    public void setLastEditor(String lastEditor) {
        this.lastEditor = lastEditor;
    }

}
