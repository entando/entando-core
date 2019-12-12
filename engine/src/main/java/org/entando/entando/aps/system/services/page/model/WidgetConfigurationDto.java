package org.entando.entando.aps.system.services.page.model;

import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.entando.entando.aps.system.services.page.serializer.WidgetConfigPropertiesSerializer;

public class WidgetConfigurationDto {

    private String code;

    /*
     * Related to EN6-183, Frontend needs all config objects to be completely valid JSON objects.
     * This Serializer converts known widget config formats, but may need to be improved case by case.
     * Also, possible conflicts may arise if different widgets use same property names and different value formats.
     * See also, WidgetConfigPropertiesDeserializer.java.
     */
    @JsonSerialize(using = WidgetConfigPropertiesSerializer.class)
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