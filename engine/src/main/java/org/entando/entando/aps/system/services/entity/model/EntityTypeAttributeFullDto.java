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
public class EntityTypeAttributeFullDto extends EntityTypeAttributeDto {

    private boolean indexable;
    private String enumeratorStaticItems;
    private String enumeratorStaticItemsSeparator;
    private String enumeratorExtractorBean;

    private EntityTypeAttributeValidationDto validationRules;
    private EntityTypeAttributeFullDto nestedAttribute;
    private List<EntityTypeAttributeFullDto> compositeAttributes;

    public EntityTypeAttributeFullDto() {
        super();
    }

    public EntityTypeAttributeFullDto(AttributeInterface attribute, List<AttributeRole> roles) {
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
        this.setValidationRules(new EntityTypeAttributeValidationDto(attribute));
        if (attribute instanceof AbstractListAttribute) {
            AttributeInterface nestedAttribute = ((AbstractListAttribute) attribute).getNestedAttributeType();
            this.setNestedAttribute(new EntityTypeAttributeFullDto(nestedAttribute, roles));
        } else if (attribute instanceof CompositeAttribute) {
            this.setCompositeAttributes(new ArrayList<>());
            List<AttributeInterface> attributes = ((CompositeAttribute) attribute).getAttributes();
            for (AttributeInterface compAttribute : attributes) {
                this.getCompositeAttributes().add(new EntityTypeAttributeFullDto(compAttribute, roles));
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

    public EntityTypeAttributeValidationDto getValidationRules() {
        return validationRules;
    }

    public void setValidationRules(EntityTypeAttributeValidationDto validationRules) {
        this.validationRules = validationRules;
    }

    public EntityTypeAttributeFullDto getNestedAttribute() {
        return nestedAttribute;
    }

    public void setNestedAttribute(EntityTypeAttributeFullDto nestedAttribute) {
        this.nestedAttribute = nestedAttribute;
    }

    public List<EntityTypeAttributeFullDto> getCompositeAttributes() {
        return compositeAttributes;
    }

    public void setCompositeAttributes(List<EntityTypeAttributeFullDto> compositeAttributes) {
        this.compositeAttributes = compositeAttributes;
    }

}
