package org.entando.entando.aps.system.services.language;

import java.util.List;

import com.agiletec.aps.system.services.lang.Lang;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LanguageDto {

    private String code;
    private String description;

    @JsonProperty("isActive")
    private boolean isActive;

    @JsonProperty("isDefault")
    private boolean defaultLang;

    public LanguageDto() {}

    public LanguageDto(Lang src) {
        this.setCode(src.getCode());
        this.setDescription(src.getDescr());
        this.setActive(true);
        this.setDefaultLang(src.isDefault());
    }

    public LanguageDto(Lang src, List<String> codes, String defaultCode) {
        this.setCode(src.getCode());
        this.setDescription(src.getDescr());
        this.setActive(codes.contains(src.getCode()));
        this.setDefaultLang(src.getCode().equals(defaultCode));
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

    @JsonProperty("isActive")
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
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
