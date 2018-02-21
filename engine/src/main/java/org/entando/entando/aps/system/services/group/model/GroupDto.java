/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.aps.system.services.group.model;

import com.agiletec.aps.system.services.group.Group;


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

