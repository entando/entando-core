package org.entando.entando.aps.system.services.pagemodel.model;

import java.util.ArrayList;
import java.util.List;

public class PageModelDto {

    public static final int NO_MAIN_FRAME = -1;

    private String code;
    private String description;
    private List<FrameDto> frames = new ArrayList<>();
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

    public List<FrameDto> getFrames() {
        return frames;
    }

    public void setFrames(List<FrameDto> frames) {
        this.frames = frames;
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

}
