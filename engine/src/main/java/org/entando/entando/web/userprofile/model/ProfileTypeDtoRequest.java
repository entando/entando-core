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
package org.entando.entando.web.userprofile.model;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import java.util.List;
import org.entando.entando.aps.system.services.entity.model.EntityTypeAttributeFullDto;
import org.entando.entando.web.entity.model.EntityTypeDtoRequest;

/**
 * @author E.Santoboni
 */
public class ProfileTypeDtoRequest extends EntityTypeDtoRequest {

    public ProfileTypeDtoRequest() {
        super();
    }

    public ProfileTypeDtoRequest(IApsEntity entityType) {
        super(entityType);
    }

    public ProfileTypeDtoRequest(IApsEntity entityType, List<AttributeRole> roles) {
        this(entityType);
        List<AttributeInterface> entityAttributes = entityType.getAttributeList();
        for (AttributeInterface attribute : entityAttributes) {
            EntityTypeAttributeFullDto attributeDto = new EntityTypeAttributeFullDto(attribute, roles);
            this.getAttributes().add(attributeDto);
        }
    }

}
