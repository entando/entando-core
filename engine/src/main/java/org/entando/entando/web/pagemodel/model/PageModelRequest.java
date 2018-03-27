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
