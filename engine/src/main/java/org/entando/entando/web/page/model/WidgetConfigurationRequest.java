package org.entando.entando.web.page.model;

import javax.validation.constraints.NotNull;

import com.agiletec.aps.util.ApsProperties;

public class WidgetConfigurationRequest {

    @NotNull(message = "widget.code.notBlank")
    private String code;

    private ApsProperties config;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ApsProperties getConfig() {
        return config;
    }

    public void setConfig(ApsProperties config) {
        this.config = config;
    }
}
