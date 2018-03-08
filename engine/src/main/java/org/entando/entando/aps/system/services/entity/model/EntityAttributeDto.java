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

import com.agiletec.aps.system.common.entity.model.attribute.AbstractListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import com.agiletec.aps.system.common.entity.model.attribute.CompositeAttribute;
import java.util.ArrayList;
import java.util.List;

/**
 * @author E.Santoboni
 */
public class EntityAttributeDto {

    private String code;
    private String type;
    private String name;
    private List<AttributeRoleDto> roles = new ArrayList<>();
    private boolean mandatory;
    private boolean canBeUsedAsFilterInList;
    private EntityAttributeDto nestedAttribute;
    private List<EntityAttributeDto> compositeAttributes;

    public EntityAttributeDto() {
    }

    public EntityAttributeDto(AttributeInterface attribute, List<AttributeRole> roles) {
        this.code = attribute.getName();
        this.type = attribute.getType();
        this.name = attribute.getDescription();
        String[] roleCodes = attribute.getRoles();
        if (null != roleCodes) {
            for (String roleCode : roleCodes) {
                for (AttributeRole role : roles) {
                    if (role.getName().equals(roleCode)) {
                        this.roles.add(new AttributeRoleDto(role));
                    }
                }
            }
        }
        this.mandatory = attribute.isRequired();
        this.canBeUsedAsFilterInList = attribute.isSearchable();
        if (attribute.isSimple()) {
            return;
        }
        if (attribute instanceof AbstractListAttribute) {
            AttributeInterface nestedAttribute = ((AbstractListAttribute) attribute).getNestedAttributeType();
            this.setNestedAttribute(new EntityAttributeDto(nestedAttribute, roles));
        } else if (attribute instanceof CompositeAttribute) {
            this.setCompositeAttributes(new ArrayList<>());
            List<AttributeInterface> attributes = ((CompositeAttribute) attribute).getAttributes();
            for (AttributeInterface compAttribute : attributes) {
                this.getCompositeAttributes().add(new EntityAttributeDto(compAttribute, roles));
            }
        }
    }

    /*
"attributes": [{
    "code": "fullName",
    "type": "Monotext",
    "name": "Full Name",
    "roles": [{
        "code": "userprofile:fullname",
        "descr": "The attr containing the full name"
    }],
    "mandatory": true,
    "canBeUsedAsFilterInList": true
}]
     */
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

    public List<AttributeRoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<AttributeRoleDto> roles) {
        this.roles = roles;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isCanBeUsedAsFilterInList() {
        return canBeUsedAsFilterInList;
    }

    public void setCanBeUsedAsFilterInList(boolean canBeUsedAsFilterInList) {
        this.canBeUsedAsFilterInList = canBeUsedAsFilterInList;
    }

    public EntityAttributeDto getNestedAttribute() {
        return nestedAttribute;
    }

    public void setNestedAttribute(EntityAttributeDto nestedAttribute) {
        this.nestedAttribute = nestedAttribute;
    }

    public List<EntityAttributeDto> getCompositeAttributes() {
        return compositeAttributes;
    }

    public void setCompositeAttributes(List<EntityAttributeDto> compositeAttributes) {
        this.compositeAttributes = compositeAttributes;
    }

}
