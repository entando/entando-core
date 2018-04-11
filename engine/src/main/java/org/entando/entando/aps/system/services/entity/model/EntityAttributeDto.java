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

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.CompositeAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;
import com.agiletec.aps.util.DateConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author E.Santoboni
 */
public class EntityAttributeDto {

    private String code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object value;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> values = new HashMap<>();

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<EntityAttributeDto> elements = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, List<EntityAttributeDto>> listelements = new HashMap<>();

    public EntityAttributeDto() {
    }

    public EntityAttributeDto(AttributeInterface src) {
        this.setCode(src.getName());
        Object value = src.getValue();
        if (null == value) {
            return;
        }
        if (src.isSimple()) {
            if ((value instanceof String) || (value instanceof Number)) {
                this.setValue(value.toString());
            } else if (value instanceof Date) {
                String stringDate = DateConverter.getFormattedDate((Date) value, SystemConstants.API_DATE_FORMAT);
                this.setValue(stringDate);
            } else if (value instanceof Map) {
                this.setValues((Map) value);
            }
        } else if (src instanceof MonoListAttribute) {
            List<AttributeInterface> list = ((MonoListAttribute) src).getAttributes();
            list.stream().forEach(element -> this.getElements().add(new EntityAttributeDto(element)));
        } else if (src instanceof CompositeAttribute) {
            Map<String, AttributeInterface> map = ((CompositeAttribute) src).getAttributeMap();
            map.keySet().stream().forEach(key -> this.getElements().add(new EntityAttributeDto(map.get(key))));
        } else if (src instanceof ListAttribute) {
            Map<String, List<AttributeInterface>> map = ((ListAttribute) src).getAttributeListMap();
            map.keySet().stream().forEach(key -> {
                List<EntityAttributeDto> dtos = new ArrayList<>();
                List<AttributeInterface> list = map.get(key);
                list.stream().forEach(element -> dtos.add(new EntityAttributeDto(element)));
                this.getListelements().put(key, dtos);
            });
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    public List<EntityAttributeDto> getElements() {
        return elements;
    }

    public void setElements(List<EntityAttributeDto> elements) {
        this.elements = elements;
    }

    public Map<String, List<EntityAttributeDto>> getListelements() {
        return listelements;
    }

    public void setListelements(Map<String, List<EntityAttributeDto>> listelements) {
        this.listelements = listelements;
    }

}
