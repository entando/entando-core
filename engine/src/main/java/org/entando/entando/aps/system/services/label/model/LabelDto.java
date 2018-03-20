package org.entando.entando.aps.system.services.label.model;

import java.util.Map;

public class LabelDto {

    private String key;
    private Map<String, String> titles;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, String> getTitles() {
        return titles;
    }

    public void setTitles(Map<String, String> titles) {
        this.titles = titles;
    }

}
