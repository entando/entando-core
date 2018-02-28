package org.entando.entando.web.widget.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WidgetRequest {

    private String code;
    private String name;
    private Boolean used;
    private Map<String, String> titles;
    private List<GuiFragmentRef> guiFragments = new ArrayList<>();
    private String pluginCode;
    private String group;
    private String customUi;
    private String defaultUi;
    private Date createdAt;
    private Date updatedAt;

    public void addGuiFragmentRef(String code, String customUi, String defaultUi) {

        GuiFragmentRef ref = new GuiFragmentRef();
        ref.code = code;
        ref.customUi = customUi;
        ref.defaultUi = defaultUi;

        guiFragments.add(ref);
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public  Map<String, String>getTitles() {
        return titles;
    }

    public void setTitles( Map<String, String> titles) {
        this.titles = titles;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPluginCode() {
        return pluginCode;
    }

    public void setPluginCode(String pluginCode) {
        this.pluginCode = pluginCode;
    }

}
