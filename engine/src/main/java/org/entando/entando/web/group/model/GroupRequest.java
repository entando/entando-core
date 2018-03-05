package org.entando.entando.web.group.model;

import javax.validation.constraints.NotNull;

/**
 * group add payload 
 *
 */
public class GroupRequest {

    @NotNull(message = "group.code.notBlank")
    private String code;

    @NotNull(message = "group.name.notBlank")
    private String name;


    public GroupRequest() {

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


}
