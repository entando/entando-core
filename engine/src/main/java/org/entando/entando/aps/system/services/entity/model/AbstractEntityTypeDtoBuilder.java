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
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import java.util.List;
import org.entando.entando.aps.system.services.DtoBuilder;

/**
 * @author E.Santoboni
 * @param <I>
 * @param <O>
 */
public abstract class AbstractEntityTypeDtoBuilder<I extends IApsEntity, O extends EntityTypeFullDto> extends DtoBuilder<I, O> {

    private List<AttributeRole> roles;

    public AbstractEntityTypeDtoBuilder(List<AttributeRole> roles) {
        this.setRoles(roles);
    }

    public List<AttributeRole> getRoles() {
        return roles;
    }

    public void setRoles(List<AttributeRole> roles) {
        this.roles = roles;
    }

}
