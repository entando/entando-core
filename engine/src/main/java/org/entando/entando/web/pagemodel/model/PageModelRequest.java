package org.entando.entando.web.pagemodel.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class PageModelRequest {

    @NotNull(message = "pageModel.code.notBlank")
    private String code;

    @NotNull(message = "pageModel.descr.notBlank")
    private String description;

    @Valid
    @NotNull(message = "pageModel.configuration.notBlank")
    private PageModelConfigurationRequest configuration = new PageModelConfigurationRequest();

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

    public PageModelConfigurationRequest getConfiguration() {
        return configuration;
    }

    public void setConfiguration(PageModelConfigurationRequest configuration) {
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
