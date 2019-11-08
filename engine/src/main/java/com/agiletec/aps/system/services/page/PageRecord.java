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

public class PageRecord {

    public IPage createDraftPage() {
        Page page = this.createPage();
        page.setMetadata(this.getMetadataDraft());
        page.setWidgets(this.getWidgetsDraft());
        page.setOnlineInstance(false);
        return page;
    }

    public IPage createOnlinePage() {
        Page page = this.createPage();
        page.setMetadata(this.getMetadataOnline());
        page.setWidgets(this.getWidgetsOnline());
        page.setOnlineInstance(true);
        return page;
    }

    protected Page createPage() {
        Page page = new Page();
        page.setCode(this.getCode());
        page.setParentCode(this.getParentCode());
        page.setPosition(this.getPosition());
        page.setGroup(this.getGroup());
        page.setOnline(null != this.getMetadataOnline());
        page.setChanged(this.isChanged());
        return page;
    }

    protected boolean isChanged() {
        boolean changed = false;
        PageMetadata onlineMeta = this.getMetadataOnline();
        if (onlineMeta != null) {
            PageMetadata draftMeta = this.getMetadataDraft();
            if (draftMeta != null) {
                boolean widgetEquals = true;
                if (null != this.getWidgetsDraft() && null != this.getWidgetsOnline()
                        && this.getWidgetsDraft().length != this.getWidgetsOnline().length) {
                    return true;
                }
                for (int i = 0; i < this.getWidgetsDraft().length; i++) {
                    Widget widgetDraft = this.getWidgetsDraft()[i];
                    if (this.getWidgetsOnline().length < i) {
                        widgetEquals = false;
                        break;
                    }
                    Widget widgetOnline = this.getWidgetsOnline()[i];
                    if (null == widgetOnline && null == widgetDraft) {
                        continue;
                    }
                    if ((null != widgetOnline && null == widgetDraft) || (null == widgetOnline && null != widgetDraft)) {
                        widgetEquals = false;
                        break;
                    }
                    if (!widgetOnline.getType().getCode().equals(widgetDraft.getType().getCode())) {
                        widgetEquals = false;
                    }
                    if (null == widgetOnline.getConfig() && null == widgetDraft.getConfig()) {
                        continue;
                    }
                    if ((null != widgetOnline.getConfig() && null == widgetDraft.getConfig())
                            || (null == widgetOnline.getConfig() && null != widgetDraft.getConfig())) {
                        widgetEquals = false;
                        break;
                    }
                    if (!widgetOnline.getConfig().equals(widgetDraft.getConfig())) {
                        widgetEquals = false;
                        break;
                    }
                }
                boolean metaEquals = onlineMeta.hasEqualConfiguration(draftMeta);
                return !(widgetEquals && metaEquals);
            } else {
                changed = true;
            }
        }
        return changed;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Deprecated
    public String getGroup() {
        return group;
    }

    @Deprecated
    public void setGroup(String group) {
        this.group = group;
    }

    public PageMetadata getMetadataDraft() {
        return metadataDraft;
    }

    public void setMetadataDraft(PageMetadata metadataDraft) {
        this.metadataDraft = metadataDraft;
    }

    public PageMetadata getMetadataOnline() {
        return metadataOnline;
    }

    public void setMetadataOnline(PageMetadata metadataOnline) {
        this.metadataOnline = metadataOnline;
    }

    public Widget[] getWidgetsOnline() {
        return widgetsOnline;
    }

    public void setWidgetsOnline(Widget[] widgetsOnline) {
        this.widgetsOnline = widgetsOnline;
    }

    public Widget[] getWidgetsDraft() {
        return widgetsDraft;
    }

    public void setWidgetsDraft(Widget[] widgetsDraft) {
        this.widgetsDraft = widgetsDraft;
    }

    private String code;
    private String parentCode;
    private int position;
    @Deprecated
    private String group;

    private PageMetadata metadataDraft;
    private PageMetadata metadataOnline;

    private Widget[] widgetsOnline;
    private Widget[] widgetsDraft;
}
