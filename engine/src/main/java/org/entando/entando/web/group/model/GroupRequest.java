package org.entando.entando.web.group.model;

import javax.validation.constraints.NotNull;

/**
 * group add payload 
 *
 */
public class GroupRequest {

    @NotNull(message = "NotBlank.group.name")
    private String name;

    @NotNull(message = "NotBlank.group.descr")
    private String descr;


    public GroupRequest() {

    }

    public GroupRequest(String name, String descr) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
