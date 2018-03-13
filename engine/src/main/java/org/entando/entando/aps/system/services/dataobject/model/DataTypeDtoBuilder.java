/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.aps.system.services.dataobject.model;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import java.util.List;
import org.entando.entando.aps.system.services.DtoBuilder;

/**
 * @author E.Santoboni
 */
public class DataTypeDtoBuilder extends DtoBuilder<IApsEntity, DataTypeDto> {

    private List<AttributeRole> roles;

    public DataTypeDtoBuilder(List<AttributeRole> roles) {
        this.setRoles(roles);
    }

    @Override
    protected DataTypeDto toDto(IApsEntity src) {
        return new DataTypeDto(src, this.getRoles());
    }

    public List<AttributeRole> getRoles() {
        return roles;
    }

    public void setRoles(List<AttributeRole> roles) {
        this.roles = roles;
    }

}
