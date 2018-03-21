package com.agiletec.aps.system.services.group;

import com.agiletec.aps.system.services.common.model.UtilizerEntry;

public class BaseGroupUtilizerEntry implements UtilizerEntry {

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

