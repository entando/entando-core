package org.entando.entando.aps.system.services.label.model;

import java.util.Map;

public class LabelDto {

    private String key;
    private Map<String, String> languages;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, String> getLanguages() {
        return languages;
    }

    public void setLanguages(Map<String, String> languages) {
        this.languages = languages;
    }

}
