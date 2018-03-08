package org.entando.entando.web.page.model;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class WidgetConfigurationRequest {

    @JsonIgnore
    private Map<String, Object> processInfo = new HashMap<>();

    @NotNull(message = "widgetConfigurationRequest.code.notBlank")
    private String code;

    private Map<String, Object> config;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    public Map<String, Object> getProcessInfo() {
        return processInfo;
    }

    public void setProcessInfo(Map<String, Object> processInfo) {
        this.processInfo = processInfo;
    }


}
