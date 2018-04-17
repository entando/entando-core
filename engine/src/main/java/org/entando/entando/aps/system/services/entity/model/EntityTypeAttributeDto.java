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

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author E.Santoboni
 */
public class EntityTypeAttributeDto {

    @NotEmpty(message = "entityType.attribute.code.notEmpty")
    private String code;

    @NotEmpty(message = "entityType.attribute.type.notEmpty")
    private String type;
    private String name;
    private List<AttributePropertyDto> roles = new ArrayList<>();
    private List<String> disablingCodes = new ArrayList<>();
    private boolean mandatory;
    private boolean listFilter;

    public EntityTypeAttributeDto() {
    }

    public EntityTypeAttributeDto(AttributeInterface attribute, List<AttributeRole> roles) {
        this.code = attribute.getName();
        this.type = attribute.getType();
        this.name = attribute.getDescription();
        String[] roleCodes = attribute.getRoles();
        if (null != roleCodes) {
            for (String roleCode : roleCodes) {
                for (AttributeRole role : roles) {
                    if (role.getName().equals(roleCode)) {
                        this.roles.add(new AttributePropertyDto(role));
                    }
                }
            }
        }
        String[] attributeDisablingCodes = attribute.getDisablingCodes();
        if (null != attributeDisablingCodes) {
            this.disablingCodes.addAll(Arrays.asList(attributeDisablingCodes));
        }
        this.mandatory = attribute.isRequired();
        this.listFilter = attribute.isSearchable();
        if (attribute.isSimple()) {
            return;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AttributePropertyDto> getRoles() {
        return roles;
    }

    public void setRoles(List<AttributePropertyDto> roles) {
        this.roles = roles;
    }

    public List<String> getDisablingCodes() {
        return disablingCodes;
    }

    public void setDisablingCodes(List<String> disablingCodes) {
        this.disablingCodes = disablingCodes;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isListFilter() {
        return listFilter;
    }

    public void setListFilter(boolean listFilter) {
        this.listFilter = listFilter;
    }

}
