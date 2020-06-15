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
package org.entando.entando.web.pagemodel.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.agiletec.aps.system.services.pagemodel.FrameSketch;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageModelFrameReq {

    @Min(value = 0)
    private int pos;

    @NotNull(message = "pageModelFrame.descr.notBlank")
    private String descr;

    private boolean mainFrame;
    private FrameSketch sketch;
    private DefaultWidgetReq defaultWidget = new DefaultWidgetReq();

    public PageModelFrameReq() {

    }

    public PageModelFrameReq(int pos, String descr) {
        this.pos = pos;
        this.descr = descr;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public boolean isMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(boolean mainFrame) {
        this.mainFrame = mainFrame;
    }

    public FrameSketch getSketch() {
        return sketch;
    }

    public void setSketch(FrameSketch sketch) {
        this.sketch = sketch;
    }

    public DefaultWidgetReq getDefaultWidget() {
        return defaultWidget;
    }

    public void setDefaultWidget(DefaultWidgetReq defaultWidget) {
        this.defaultWidget = defaultWidget;
    }

}
