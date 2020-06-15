/*
 * Copyright 2020-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.pagemodel.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.*;

public class PageModelDto {

    public static final int NO_MAIN_FRAME = -1;

    private String code;
    private String descr;

    @JsonProperty(value = "configuration")
    private PageModelConfigurationDto configuration = new PageModelConfigurationDto();
    private int mainFrame = NO_MAIN_FRAME;
    private String pluginCode;
    private String template;
    
    /**
     * The references grouped by service name.
     * <p>
     * Lists all the managers that may contain references by indicating with <code>true</code> the presence of references
     */
    @JsonInclude(Include.NON_NULL)
    private Map<String, Boolean> references;

    public PageModelDto() {
    }
    
    public PageModelDto(PageModelDto other) {
        this.code = other.code;
        this.descr = other.descr;
        this.configuration = other.configuration;
        this.mainFrame = other.mainFrame;
        this.pluginCode = other.pluginCode;
        this.template = other.template;
        this.references = other.references;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }


    public int getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(int mainFrame) {
        this.mainFrame = mainFrame;
    }

    public String getPluginCode() {
        return pluginCode;
    }

    public void setPluginCode(String pluginCode) {
        this.pluginCode = pluginCode;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Map<String, Boolean> getReferences() {
        return references;
    }

    public void setReferences(Map<String, Boolean> references) {
        this.references = references;
    }

    public static String getEntityFieldName(String dtoFieldName) {
        switch (dtoFieldName) {
            case "description":
                return "descr";
            case "pluginCode":
                return "plugincode";
            case "template":
                return "templategui";
            default:
                return dtoFieldName;
        }
    }

    public PageModelConfigurationDto getConfiguration() {
        return configuration;
    }

    public void setConfiguration(PageModelConfigurationDto configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageModelDto that = (PageModelDto) o;
        return mainFrame == that.mainFrame &&
               Objects.equals(code, that.code) &&
               Objects.equals(descr, that.descr) &&
               Objects.equals(configuration, that.configuration) &&
               Objects.equals(pluginCode, that.pluginCode) &&
               Objects.equals(template, that.template) &&
               Objects.equals(references, that.references);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, descr, configuration, mainFrame, pluginCode, template, references);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("code", code)
                .append("descr", descr)
                .append("configuration", configuration)
                .append("mainFrame", mainFrame)
                .append("pluginCode", pluginCode)
                .append("template", template)
                .append("references", references)
                .toString();
    }
}
