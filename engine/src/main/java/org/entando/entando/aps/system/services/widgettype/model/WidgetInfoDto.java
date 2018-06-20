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
package org.entando.entando.aps.system.services.widgettype.model;

import com.agiletec.aps.util.ApsProperties;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author paddeo
 */
public class WidgetInfoDto {

    private String code;
    private ApsProperties titles;
    private List<WidgetDetails> publishedUtilizers = new ArrayList<>();
    private List<WidgetDetails> draftUtilizers = new ArrayList<>();

    public WidgetInfoDto() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ApsProperties getTitles() {
        return titles;
    }

    public void setTitles(ApsProperties titles) {
        this.titles = titles;
    }

    public List<WidgetDetails> getPublishedUtilizers() {
        return publishedUtilizers;
    }

    public void setPublishedUtilizers(List<WidgetDetails> publishedUtilizers) {
        this.publishedUtilizers = publishedUtilizers;
    }

    public List<WidgetDetails> getDraftUtilizers() {
        return draftUtilizers;
    }

    public void setDraftUtilizers(List<WidgetDetails> draftUtilizers) {
        this.draftUtilizers = draftUtilizers;
    }

    public void addPublishedUtilizer(WidgetDetails utilizer) {
        this.publishedUtilizers.add(utilizer);
    }

    public void addDraftUtilizer(WidgetDetails utilizer) {
        this.draftUtilizers.add(utilizer);
    }
}
