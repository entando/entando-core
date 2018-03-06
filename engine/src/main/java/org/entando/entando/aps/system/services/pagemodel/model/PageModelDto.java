package org.entando.entando.aps.system.services.pagemodel.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PageModelDto {

    public static final int NO_MAIN_FRAME = -1;

    private String code;
    private String description;

    @JsonProperty(value = "configuration")
    private PageModelConfigurationDto configuration = new PageModelConfigurationDto();
    private int mainFrame = NO_MAIN_FRAME;
    private String pluginCode;
    private String template;

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

}
