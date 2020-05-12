package com.agiletec.aps.system.services.user;

import java.util.Set;

public class UserPermissions {

    private String group;
    private Set<String> permissions;

    public UserPermissions(String group, Set<String> permissions) {
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

    public Set<String> getPermissions() {
        return permissions;
    }

    public UserPermissions setPermissions(Set<String> permissions) {
        this.permissions = permissions;
        return this;
    }
}
