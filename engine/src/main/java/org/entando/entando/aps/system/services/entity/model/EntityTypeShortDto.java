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

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Objects;

/**
 * @author E.Santoboni
 */
public class EntityTypeShortDto {
    
    @NotNull(message = "entityType.code.notBlank")
    @Size(min = 3, max = 3, message = "entityType.code.invalidCharacters")
    @Pattern(regexp = "^[A-Z0-9]*$", message = "entityType.code.invalidCharacters")
    private String code;

    @NotBlank(message = "entityType.name.notBlank")
    private String name;

    private String status = String.valueOf(IEntityManager.STATUS_READY);

    public EntityTypeShortDto() {
    }

    public EntityTypeShortDto(IApsEntity entityType) {
        this.setCode(entityType.getTypeCode());
        this.setName(entityType.getTypeDescription());
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityTypeShortDto that = (EntityTypeShortDto) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(name, that.name) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, status);
    }

    @Override
    public String toString() {
        return "EntityTypeShortDto{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
