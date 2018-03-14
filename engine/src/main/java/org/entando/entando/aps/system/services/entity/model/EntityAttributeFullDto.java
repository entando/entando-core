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
import com.agiletec.aps.system.common.entity.model.attribute.EnumeratorAttribute;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import java.util.ArrayList;
import java.util.List;

/**
 * @author E.Santoboni
 */
public class EntityAttributeFullDto extends EntityAttributeDto {

    private boolean indexable;
    private String enumeratorStaticItems;
    private String enumeratorStaticItemsSeparator;
    private String enumeratorExtractorBean;

    private EntityAttributeValidationDto validationRules;
    private EntityAttributeFullDto nestedAttribute;
    private List<EntityAttributeFullDto> compositeAttributes;

    public EntityAttributeFullDto() {
        super();
    }

    public EntityAttributeFullDto(AttributeInterface attribute, List<AttributeRole> roles) {
        super(attribute, roles);
        String indexingType = attribute.getIndexingType();
        if (null != indexingType) {
            this.setIndexable(indexingType.equalsIgnoreCase(IndexableAttributeInterface.INDEXING_TYPE_TEXT));
        }
        if (attribute.isTextAttribute()) {
            if (attribute instanceof EnumeratorAttribute) {
                EnumeratorAttribute enumeratorAttribute = (EnumeratorAttribute) attribute;
                this.setEnumeratorStaticItems(enumeratorAttribute.getStaticItems());
                this.setEnumeratorStaticItemsSeparator(enumeratorAttribute.getCustomSeparator());
                this.setEnumeratorExtractorBean(enumeratorAttribute.getExtractorBeanName());
            }
        }
        this.setValidationRules(new EntityAttributeValidationDto(attribute));
        if (attribute instanceof AbstractListAttribute) {
            AttributeInterface nestedAttribute = ((AbstractListAttribute) attribute).getNestedAttributeType();
            this.setNestedAttribute(new EntityAttributeFullDto(nestedAttribute, roles));
        } else if (attribute instanceof CompositeAttribute) {
            this.setCompositeAttributes(new ArrayList<>());
            List<AttributeInterface> attributes = ((CompositeAttribute) attribute).getAttributes();
            for (AttributeInterface compAttribute : attributes) {
                this.getCompositeAttributes().add(new EntityAttributeFullDto(compAttribute, roles));
            }
        }
    }

    public boolean isIndexable() {
        return indexable;
    }

    public void setIndexable(boolean indexable) {
        this.indexable = indexable;
    }

    public String getEnumeratorStaticItems() {
        return enumeratorStaticItems;
    }

    public void setEnumeratorStaticItems(String enumeratorStaticItems) {
        this.enumeratorStaticItems = enumeratorStaticItems;
    }

    public String getEnumeratorStaticItemsSeparator() {
        return enumeratorStaticItemsSeparator;
    }

    public void setEnumeratorStaticItemsSeparator(String enumeratorStaticItemsSeparator) {
        this.enumeratorStaticItemsSeparator = enumeratorStaticItemsSeparator;
    }

    public String getEnumeratorExtractorBean() {
        return enumeratorExtractorBean;
    }

    public void setEnumeratorExtractorBean(String enumeratorExtractorBean) {
        this.enumeratorExtractorBean = enumeratorExtractorBean;
    }

    public EntityAttributeValidationDto getValidationRules() {
        return validationRules;
    }

    public void setValidationRules(EntityAttributeValidationDto validationRules) {
        this.validationRules = validationRules;
    }

    public EntityAttributeFullDto getNestedAttribute() {
        return nestedAttribute;
    }

    public void setNestedAttribute(EntityAttributeFullDto nestedAttribute) {
        this.nestedAttribute = nestedAttribute;
    }

    public List<EntityAttributeFullDto> getCompositeAttributes() {
        return compositeAttributes;
    }

    public void setCompositeAttributes(List<EntityAttributeFullDto> compositeAttributes) {
        this.compositeAttributes = compositeAttributes;
    }

}
