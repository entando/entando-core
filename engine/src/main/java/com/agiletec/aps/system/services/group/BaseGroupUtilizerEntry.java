package com.agiletec.aps.system.services.group;


public class BaseGroupUtilizerEntry implements GroupUtilizerEntry {

    private String id;

    public BaseGroupUtilizerEntry() {}

    public BaseGroupUtilizerEntry(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

