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
package org.entando.entando.plugins.jacms.aps.system.services.widgettype.validators;

import org.apache.commons.lang3.StringUtils;

public class RowContentListConfigurationEntry {

    private String contentId;
    private String modelId;

    public RowContentListConfigurationEntry() {

    }

    public RowContentListConfigurationEntry(String contentId) {

        this.contentId = contentId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String toCfg() {
        String contentIdBlock = "contentId=" + this.getContentId();
        String modelIdBlock = StringUtils.isBlank(this.getModelId()) ? "" : ",modelId=" + this.getModelId();
        return "{" + contentIdBlock + modelIdBlock + "}";
    }

    @Override
    public String toString() {
        return "ContentContainer [contentId=" + contentId + ", modelId=" + modelId + "]";
    }

}
