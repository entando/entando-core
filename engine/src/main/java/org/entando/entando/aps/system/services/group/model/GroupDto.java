package org.entando.entando.aps.system.services.group.model;

import com.agiletec.aps.system.services.group.Group;

/**
 * Rappresentazione REST dell'oggetto GROUP
 *
 * 
 */
public class GroupDto {

    private String code;
    private String name;

    public GroupDto() {

    }

    public GroupDto(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public GroupDto(Group group) {
        this.setCode(group.getName());
        this.setName(group.getDescription());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getEntityFieldName(String dtoFieldName) {
        switch (dtoFieldName) {
            case "code":
                return "groupname";
            case "name":
                return "descr";
            default:
                return dtoFieldName;
        }
    }

}

