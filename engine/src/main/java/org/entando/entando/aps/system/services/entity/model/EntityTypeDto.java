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
package org.entando.entando.aps.system.services.entity.model;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import java.util.ArrayList;
import java.util.List;

/**
 * @author E.Santoboni
 */
public class EntityTypeDto {

    private String code;
    private String name;
    private List<EntityAttributeDto> attributes = new ArrayList<>();

    public EntityTypeDto() {
    }

    public EntityTypeDto(IApsEntity entityType, List<AttributeRole> roles) {
        this.setCode(entityType.getTypeCode());
        this.setName(entityType.getTypeDescription());
        List<AttributeInterface> entityAttributes = entityType.getAttributeList();
        for (AttributeInterface attribute : entityAttributes) {
            EntityAttributeDto attributeDto = new EntityAttributeDto(attribute, roles);
            this.getAttributes().add(attributeDto);
        }
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

    public List<EntityAttributeDto> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<EntityAttributeDto> attributes) {
        this.attributes = attributes;
    }

}
