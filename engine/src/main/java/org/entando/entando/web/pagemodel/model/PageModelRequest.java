package org.entando.entando.web.pagemodel.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

public class PageModelRequest {

    @NotNull(message = "pageModel.code.notBlank")
    private String code;

    @NotNull(message = "pageModel.descr.notBlank")
    private String description;

    private List<PageModelFrameReq> configuration = new ArrayList<>();

    private String template;
    private String pluginCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PageModelFrameReq> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(List<PageModelFrameReq> configuration) {
        this.configuration = configuration;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getPluginCode() {
        return pluginCode;
    }

    public void setPluginCode(String pluginCode) {
        this.pluginCode = pluginCode;
    }

}
