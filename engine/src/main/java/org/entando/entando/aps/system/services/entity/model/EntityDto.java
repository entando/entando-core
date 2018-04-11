/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.entity.model;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import java.util.List;
import java.util.Set;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import java.io.Serializable;
import java.util.stream.Collectors;

public class EntityDto implements Serializable {

    private String id;
    private String typeCode;
    private String typeDescription;
    private String description;
    private String mainGroup;
    private Set<String> groups;
    private List<String> categories;
    private List<AttributeInterface> attributes;

    public EntityDto() {
    }

    public EntityDto(IApsEntity src) {
        this.setId(src.getId());
        this.setTypeCode(src.getTypeCode());
        this.setTypeDescription(src.getTypeDescription());
        this.setDescription(src.getDescription());
        this.setMainGroup(src.getMainGroup());
        this.setGroups(src.getGroups());
        this.setAttributes(src.getAttributeList());
        if (null != src.getCategories()) {
            this.setCategories(src.getCategories().stream().map(i -> i.getCode()).collect(Collectors.toList()));
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMainGroup() {
        return mainGroup;
    }

    public void setMainGroup(String mainGroup) {
        this.mainGroup = mainGroup;
    }

    public Set<String> getGroups() {
        return groups;
    }

    public void setGroups(Set<String> groups) {
        this.groups = groups;
    }

    public List<AttributeInterface> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeInterface> attributes) {
        this.attributes = attributes;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

}
