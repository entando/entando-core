package org.entando.entando.aps.system.services.page.model;

import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;

public class WidgetConfigurationDto {

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

    public WidgetConfigurationDto() {

    }

    public WidgetConfigurationDto(Widget widget) {
        this.code = widget.getType().getCode();
        this.config = widget.getConfig();
    }

    public WidgetConfigurationDto(String code, ApsProperties config) {
        this.code = code;
        this.config = config;
    }

}