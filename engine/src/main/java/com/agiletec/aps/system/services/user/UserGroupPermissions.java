package com.agiletec.aps.system.services.user;

import java.util.Set;

public class UserGroupPermissions {

    private String group;
    private Set<String> permissions;

    public UserGroupPermissions(String group, Set<String> permissions) {
        this.group = group;
        this.permissions = permissions;
    }

    public String getGroup() {
        return group;
    }

    public UserGroupPermissions setGroup(String group) {
        this.group = group;
        return this;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public UserGroupPermissions setPermissions(Set<String> permissions) {
        this.permissions = permissions;
        return this;
    }
}
