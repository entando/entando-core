package org.entando.entando.aps.system.services.pagemodel.model;

import com.agiletec.aps.util.ApsProperties;
import java.util.Properties;

public class DefaultWidgetDto {

    private String code;
    private Properties properties = new ApsProperties();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
