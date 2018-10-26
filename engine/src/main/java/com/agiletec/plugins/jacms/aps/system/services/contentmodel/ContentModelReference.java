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
package com.agiletec.plugins.jacms.aps.system.services.contentmodel;

import java.io.Serializable;
import java.util.List;

/**
 * VO for representing a reference to a ContentModel inside a page widget.
 */
public class ContentModelReference implements Serializable {

    private String pageCode;
    private boolean online;
    private List<String> contentsId;
    private int widgetPosition;

    public String getPageCode() {
        return pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public List<String> getContentsId() {
        return contentsId;
    }

    public void setContentsId(List<String> contentsId) {
        this.contentsId = contentsId;
    }

    public int getWidgetPosition() {
        return widgetPosition;
    }

    public void setWidgetPosition(int widgetPosition) {
        this.widgetPosition = widgetPosition;
    }
}
