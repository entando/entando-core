package org.entando.entando.aps.system.services.language;

import com.agiletec.aps.system.services.lang.Lang;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LanguageDto {

    private String code;
    private String description;

    @JsonProperty("isActive")
    private boolean active;

    @JsonProperty("isDefault")
    private boolean defaultLang;

    public LanguageDto() {}

    public LanguageDto(Lang src) {
        this.setCode(src.getCode());
        this.setDescription(src.getDescr());
        this.setActive(src.isDefault());
        this.setDefaultLang(src.isDefault());
    }

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDefaultLang() {
        return defaultLang;
    }

    public void setDefaultLang(boolean defaultLang) {
        this.defaultLang = defaultLang;
    }

    public static String getEntityFieldName(String dtoFieldName) {
        switch (dtoFieldName) {
            case "code":
                return "code";
            case "description":
                return "descr";
            default:
                return dtoFieldName;
        }
    }
}
