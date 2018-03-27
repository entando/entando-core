package org.entando.entando.aps.system.services.widgettype.model;

import java.util.List;
import java.util.Properties;

import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;

public class WidgetTypeDto {

    private String code;
    private Properties titles;
    private List<WidgetTypeParameter> parameters;
    private String action;
    private String pluginCode;
    private String parentTypeCode;
    private Properties config;
    private boolean locked;
    private String mainGroup;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Properties getTitles() {
        return titles;
    }

    public void setTitles(Properties titles) {
        this.titles = titles;
    }

    public List<WidgetTypeParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<WidgetTypeParameter> parameters) {
        this.parameters = parameters;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPluginCode() {
        return pluginCode;
    }

    public void setPluginCode(String pluginCode) {
        this.pluginCode = pluginCode;
    }

    public String getParentTypeCode() {
        return parentTypeCode;
    }

    public void setParentTypeCode(String parentTypeCode) {
        this.parentTypeCode = parentTypeCode;
    }

    public Properties getConfig() {
        return config;
    }

    public void setConfig(Properties config) {
        this.config = config;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getMainGroup() {
        return mainGroup;
    }

    public void setMainGroup(String mainGroup) {
        this.mainGroup = mainGroup;
    }

}
