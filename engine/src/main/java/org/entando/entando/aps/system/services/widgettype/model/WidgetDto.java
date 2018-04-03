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
package org.entando.entando.aps.system.services.widgettype.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WidgetDto {

    public static final String USER_TYPOLOGY_CODE = "user";
    public static final String CUSTOM_TYPOLOGY_CODE = "custom";
    public static final String STOCK_TYPOLOGY_CODE = "stock";

    private String code;
    private Integer used = 0;
    private Map<String, String> titles = new HashMap<>();
    private String typology;
    private String group;
    private String pluginCode;
    private List<GuiFragmentRef> guiFragments = new ArrayList<>();
    private boolean hasConfig = false;

    protected class GuiFragmentRef {

        private String code;
        private String customUi;
        private String defaultUi;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCustomUi() {
            return customUi;
        }

        public void setCustomUi(String customUi) {
            this.customUi = customUi;
        }

        public String getDefaultUi() {
            return defaultUi;
        }

        public void setDefaultUi(String defaultUi) {
            this.defaultUi = defaultUi;
        }
    }

    public void addGuiFragmentRef(String code, String customUi, String defaultUi) {
        GuiFragmentRef ref = new GuiFragmentRef();
        ref.code = code;
        ref.customUi = customUi;
        ref.defaultUi = defaultUi;
        guiFragments.add(ref);
    }

    public static String getEntityFieldName(String dtoFieldName) {
        return dtoFieldName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }

    public Map<String, String> getTitles() {
        return titles;
    }

    public void setTitles(Map<String, String> titles) {
        this.titles = titles;
    }

    public String getTypology() {
        return typology;
    }

    public void setTypology(String typology) {
        this.typology = typology;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPluginCode() {
        return pluginCode;
    }

    public void setPluginCode(String pluginCode) {
        this.pluginCode = pluginCode;
    }

    public List<GuiFragmentRef> getGuiFragments() {
        return guiFragments;
    }

    public void setGuiFragments(List<GuiFragmentRef> guiFragments) {
        this.guiFragments = guiFragments;
    }

    public boolean isHasConfig() {
        return hasConfig;
    }

    public void setHasConfig(boolean hasConfig) {
        this.hasConfig = hasConfig;
    }

}
