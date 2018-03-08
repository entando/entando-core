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

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import java.util.ArrayList;
import java.util.List;

/**
 * @author E.Santoboni
 */
public class EntityManagerDto {

    private String code;
    private List<EntityTypeDto> entityTypes = new ArrayList<>();

    public EntityManagerDto() {
    }

    public EntityManagerDto(IEntityManager src) {
        this.setCode(((IManager) src).getName());
        List<AttributeRole> roles = src.getAttributeRoles();
        List<IApsEntity> entityTypes = new ArrayList<>(src.getEntityPrototypes().values());
        for (IApsEntity entityType : entityTypes) {
            EntityTypeDto dto = new EntityTypeDto(entityType, roles);
            this.getEntityTypes().add(dto);
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<EntityTypeDto> getEntityTypes() {
        return entityTypes;
    }

    public void setEntityTypes(List<EntityTypeDto> entityTypes) {
        this.entityTypes = entityTypes;
    }

}

/*
{
    "payload": [{
        "code": "userProfileManager",
        "entityTypes": [{
            "code": "PFL",
            "name": "Default user profile",
            "attributes": [{
                "code": "fullName",
                "type": "Monotext",
                "name": "Full Name",
                "roles": [{
                    "code": "userprofile:fullname",
                    "descr": "The attr containing the full name"
                }],
                "isMandatory": true,
                "canBeUsedAsFilterInList": true
            }]

        }]
    }],
    "errors": [],
    "metadata": {}
}
 */
