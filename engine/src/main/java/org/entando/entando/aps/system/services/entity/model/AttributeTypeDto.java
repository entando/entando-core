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
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author E.Santoboni
 */
public class AttributeTypeDto implements Serializable {

    private String code;
    private boolean multilingual;
    private boolean textAttribute;
    private boolean simple;
    private List<AttributePropertyDto> allowedRoles = new ArrayList<>();
    private List<AttributePropertyDto> allowedDisablingCodes = new ArrayList<>();

    public AttributeTypeDto() {
    }

    public AttributeTypeDto(AttributeInterface attribute, IEntityManager entityManager) {
        this.setCode(attribute.getType());
        this.setMultilingual(attribute.isMultilingual());
        this.setSimple(attribute.isSimple());
        this.setTextAttribute(attribute.isTextAttribute());
        Map<String, String> disablingCodes = entityManager.getAttributeDisablingCodes();
        if (null != disablingCodes) {
            disablingCodes.keySet().stream()
                    .forEach(i -> this.allowedDisablingCodes.add(new AttributePropertyDto(i, disablingCodes.get(i))));
        }
        List<AttributeRole> roles = entityManager.getAttributeRoles();
        if (null != roles) {
            roles.stream().filter(i -> i.getAllowedAttributeTypes().contains(attribute.getType()))
                    .forEach(i -> this.allowedRoles.add(new AttributePropertyDto(i.getName(), i.getDescription())));
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isMultilingual() {
        return multilingual;
    }

    public void setMultilingual(boolean multilingual) {
        this.multilingual = multilingual;
    }

    public boolean isTextAttribute() {
        return textAttribute;
    }

    public void setTextAttribute(boolean textAttribute) {
        this.textAttribute = textAttribute;
    }

    public boolean isSimple() {
        return simple;
    }

    public void setSimple(boolean simple) {
        this.simple = simple;
    }

    public List<AttributePropertyDto> getAllowedRoles() {
        return allowedRoles;
    }

    public void setAllowedRoles(List<AttributePropertyDto> allowedRoles) {
        this.allowedRoles = allowedRoles;
    }

    public List<AttributePropertyDto> getAllowedDisablingCodes() {
        return allowedDisablingCodes;
    }

    public void setAllowedDisablingCodes(List<AttributePropertyDto> allowedDisablingCodes) {
        this.allowedDisablingCodes = allowedDisablingCodes;
    }

}
