package com.agiletec.aps.system.services.group;


public class BaseGroupUtilizerEntry implements GroupUtilizerEntry {

    private String utilizerId;

    public BaseGroupUtilizerEntry() {}

    public BaseGroupUtilizerEntry(String id) {
        this.utilizerId = id;
    }

    public String getUtilizerId() {
        return utilizerId;
    }

    public void setUtilizerId(String utilizerId) {
        this.utilizerId = utilizerId;
    }

}

