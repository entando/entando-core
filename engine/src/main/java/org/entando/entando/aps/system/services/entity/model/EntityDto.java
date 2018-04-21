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
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.BindingResult;

public class EntityDto implements Serializable {

    @NotBlank(message = "entity.id.notBlank")
    private String id;

    @Size(min = 3, max = 3, message = "string.size.invalid")
    @NotNull(message = "entity.typeCode.notBlank")
    private String typeCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String typeDescription;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String mainGroup;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<String> groups;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> categories;
    private List<EntityAttributeDto> attributes = new ArrayList<>();

    public EntityDto() {
    }

    public EntityDto(IApsEntity src) {
        this.setId(src.getId());
        this.setTypeCode(src.getTypeCode());
        this.setTypeDescription(src.getTypeDescription());
        this.setDescription(src.getDescription());
        this.setMainGroup(src.getMainGroup());
        this.setGroups(src.getGroups());
        if (null != src.getCategories()) {
            this.setCategories(src.getCategories().stream().map(i -> i.getCode()).collect(Collectors.toList()));
        }
        if (null != src.getAttributeList()) {
            src.getAttributeList().stream().forEach(j -> this.getAttributes().add(new EntityAttributeDto(j)));
        }
    }

    public void fillEntity(IApsEntity prototype, ICategoryManager categoryManager, BindingResult bindingResult) {
        prototype.setId(this.getId());
        prototype.setDescription(this.getDescription());
        prototype.setMainGroup(this.getMainGroup());
        if (null != this.getGroups()) {
            this.getGroups().stream().forEach(i -> prototype.addGroup(typeCode));
        }
        if (null != this.getCategories()) {
            this.getCategories().stream().forEach(i -> {
                Category category = categoryManager.getCategory(i);
                if (null != category) {
                    prototype.addCategory(category);
                }
            });
        }
        List<EntityAttributeDto> attributeDtos = this.getAttributes();
        if (null == attributeDtos) {
            return;
        }
        for (EntityAttributeDto attributeDto : attributeDtos) {
            String code = attributeDto.getCode();
            AttributeInterface attribute = prototype.getAttribute(code);
            if (null != attribute) {
                attributeDto.fillEntityAttribute(attribute, bindingResult);
            } else {
                //ADD LOG
            }
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

    public List<EntityAttributeDto> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<EntityAttributeDto> attributes) {
        this.attributes = attributes;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

}
