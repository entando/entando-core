package org.entando.entando.plugins.jacms.aps.system.services.widget.validators;

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
        String modelIdBlock = StringUtils.isBlank(modelId) ? "" : ";modelId=" + this.getContentId();
        return "{" + contentIdBlock + modelIdBlock + "}";
    }

    @Override
    public String toString() {
        return "ContentContainer [contentId=" + contentId + ", modelId=" + modelId + "]";
    }
    
}