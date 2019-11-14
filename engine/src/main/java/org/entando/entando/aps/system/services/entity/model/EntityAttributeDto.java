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
import com.agiletec.aps.system.common.entity.model.attribute.BooleanAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.CompositeAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.NumberAttribute;
import com.agiletec.aps.util.DateConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.validation.BindingResult;

/**
 * @author E.Santoboni
 */
public class EntityAttributeDto {

    private String code;

    private Object value;

    private Map<String, Object> values = new HashMap<>();

    private List<EntityAttributeDto> elements = new ArrayList<>();

    @JsonProperty("compositeelements")
    private List<EntityAttributeDto> compositeElements = new ArrayList<>();

    @JsonProperty("listelements")
    private Map<String, List<EntityAttributeDto>> listElements = new HashMap<>();

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
            } else if (value instanceof Boolean) {
                this.setValue((Boolean) value);
            } else if (value instanceof Map) {
                ((Map) value).keySet().stream().forEach(key -> {
                    Object mapValue = ((Map) value).get(key);
                    if (mapValue instanceof AttributeInterface) {
                        this.getValues().put(key.toString(), new EntityAttributeDto((AttributeInterface) mapValue));
                    } else {
                        this.getValues().put(key.toString(), mapValue.toString());
                    }
                });
            }
        } else if (src instanceof MonoListAttribute) {
            List<AttributeInterface> list = ((MonoListAttribute) src).getAttributes();
            list.stream().forEach(element -> this.getElements().add(new EntityAttributeDto(element)));
        } else if (src instanceof CompositeAttribute) {
            Map<String, AttributeInterface> map = ((CompositeAttribute) src).getAttributeMap();
            map.keySet().stream().forEach(key -> this.getCompositeElements().add(new EntityAttributeDto(map.get(key))));
        } else if (src instanceof ListAttribute) {
            Map<String, List<AttributeInterface>> map = ((ListAttribute) src).getAttributeListMap();
            map.keySet().stream().forEach(key -> {
                List<EntityAttributeDto> dtos = new ArrayList<>();
                List<AttributeInterface> list = map.get(key);
                list.stream().forEach(element -> dtos.add(new EntityAttributeDto(element)));
                this.getListElements().put(key, dtos);
            });
        }
    }

    public void fillEntityAttribute(AttributeInterface attribute, BindingResult bindingResult) {
        if (attribute instanceof ITextAttribute) {
            ITextAttribute textAttribute = (ITextAttribute) attribute;
            if (attribute.isMultilingual() && !this.getValues().isEmpty()) {
                this.getValues().keySet().stream().forEach(langCode -> textAttribute.setText(this.getValues().get(langCode).toString(), langCode));
            } else if (null != this.getValue()) {
                textAttribute.setText(this.getValue().toString(), null);
            }
        }
        if (attribute instanceof NumberAttribute && (null != this.getValue())) {
            BigDecimal number = new BigDecimal(this.getValue().toString());
            ((NumberAttribute) attribute).setValue(number);
        }
        if (attribute instanceof BooleanAttribute) {
            ((BooleanAttribute) attribute).setBooleanValue((Boolean)this.getValue());
        }
        if (attribute instanceof DateAttribute && (null != this.getValue())) {
            Date date = null;
            String dateValue = null;
            try {
                date = DateConverter.parseDate(this.getValue().toString(), SystemConstants.API_DATE_FORMAT);
            } catch (Exception e) {
                dateValue = this.getValue().toString();
            }
            ((DateAttribute) attribute).setDate(date);
            ((DateAttribute) attribute).setFailedDateString(dateValue);
        }
        if (attribute instanceof CompositeAttribute && (null != this.getCompositeElements())) {
            this.getCompositeElements().stream().forEach(i -> {
                AttributeInterface compositeElement = ((CompositeAttribute) attribute).getAttribute(i.getCode());
                i.fillEntityAttribute(compositeElement, bindingResult);
            });
        } else if (attribute instanceof MonoListAttribute && (null != this.getElements())) {
            this.getElements().stream().forEach(i -> {
                AttributeInterface prototype = ((MonoListAttribute) attribute).addAttribute();
                prototype.setName(((MonoListAttribute) attribute).getName());
                i.fillEntityAttribute(prototype, bindingResult);
            });
        } else if (attribute instanceof ListAttribute && (null != this.getListElements())) {
            ((ListAttribute) attribute).getAttributeListMap().clear();
            this.getListElements().keySet().stream().forEach(langCode -> {
                List<EntityAttributeDto> list = this.getListElements().get(langCode);
                list.stream().forEach(i -> {
                    AttributeInterface prototype = ((ListAttribute) attribute).addAttribute(langCode);
                    prototype.setName(((ListAttribute) attribute).getName());
                    i.fillEntityAttribute(prototype, bindingResult);
                });
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

    public List<EntityAttributeDto> getCompositeElements() {
        return compositeElements;
    }

    public void setCompositeElements(List<EntityAttributeDto> compositeElements) {
        this.compositeElements = compositeElements;
    }

    public Map<String, List<EntityAttributeDto>> getListElements() {
        return listElements;
    }

    public void setListElements(Map<String, List<EntityAttributeDto>> listElements) {
        this.listElements = listElements;
    }
    
}
