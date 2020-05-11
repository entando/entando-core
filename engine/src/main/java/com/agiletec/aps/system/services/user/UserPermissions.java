package com.agiletec.aps.system.services.user;

import java.util.List;

public class UserPermissions {

    private String group;
    private List<String> permissions;

    public UserPermissions() {
    }

    public UserPermissions(String group, List<String> permissions) {
        this.group = group;
        this.permissions = permissions;
    }

    public String getGroup() {
        return group;
    }

    public UserPermissions setGroup(String group) {
        this.group = group;
        return this;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public UserPermissions setPermissions(List<String> permissions) {
        this.permissions = permissions;
        return this;
    }
}
