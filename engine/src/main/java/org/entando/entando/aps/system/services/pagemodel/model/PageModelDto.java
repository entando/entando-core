package org.entando.entando.aps.system.services.pagemodel.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

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



}
