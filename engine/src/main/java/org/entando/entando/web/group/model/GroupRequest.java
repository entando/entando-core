package org.entando.entando.web.group.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;


public class GroupRequest {

    @Size(max = 20, message = "string.size.invalid")
    @NotBlank(message = "group.code.notBlank")
    private String code;

    @Size(max = 50, message = "string.size.invalid")
    @NotBlank(message = "group.name.notBlank")
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
