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
import com.agiletec.aps.system.common.entity.model.attribute.AbstractListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.EnumeratorAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.NumberAttribute;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import org.entando.entando.aps.system.common.entity.model.attribute.EnumeratorMapAttribute;

/**
 * @author E.Santoboni
 */
public class AttributeTypeDto implements Serializable {

    private String code;
    private boolean multilingual;
    private boolean textAttribute;
    private boolean simple;
    private boolean searchableOptionSupported;
    private boolean indexableOptionSupported;
    private boolean textFilterSupported;
    private boolean dateFilterSupported;
    private boolean numberFilterSupported;
    private boolean enumeratorOptionsSupported;
    private boolean enumeratorMapOptionsSupported;
    private boolean listAttribute;
    private List<String> enumeratorExtractorBeans = new ArrayList<>();
    private List<String> enumeratorMapExtractorBeans = new ArrayList<>();
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
        this.setSearchableOptionSupported(attribute.isSearchableOptionSupported());
        this.setTextFilterSupported(attribute.isTextAttribute());
        this.setDateFilterSupported(attribute instanceof DateAttribute);
        this.setNumberFilterSupported(attribute instanceof NumberAttribute);
        if (attribute instanceof EnumeratorMapAttribute) {
            this.setEnumeratorMapOptionsSupported(true);
        } else {
            this.setEnumeratorOptionsSupported(attribute instanceof EnumeratorAttribute);
        }
        this.setListAttribute(attribute instanceof AbstractListAttribute);
        this.setIndexableOptionSupported(attribute instanceof IndexableAttributeInterface);
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

    public boolean isSearchableOptionSupported() {
        return searchableOptionSupported;
    }

    public void setSearchableOptionSupported(boolean searchableOptionSupported) {
        this.searchableOptionSupported = searchableOptionSupported;
    }

    public boolean isIndexableOptionSupported() {
        return indexableOptionSupported;
    }

    public void setIndexableOptionSupported(boolean indexableOptionSupported) {
        this.indexableOptionSupported = indexableOptionSupported;
    }

    public boolean isTextFilterSupported() {
        return textFilterSupported;
    }

    public void setTextFilterSupported(boolean textFilterSupported) {
        this.textFilterSupported = textFilterSupported;
    }

    public boolean isDateFilterSupported() {
        return dateFilterSupported;
    }

    public void setDateFilterSupported(boolean dateFilterSupported) {
        this.dateFilterSupported = dateFilterSupported;
    }

    public boolean isNumberFilterSupported() {
        return numberFilterSupported;
    }

    public void setNumberFilterSupported(boolean numberFilterSupported) {
        this.numberFilterSupported = numberFilterSupported;
    }

    public boolean isEnumeratorOptionsSupported() {
        return enumeratorOptionsSupported;
    }

    public void setEnumeratorOptionsSupported(boolean enumeratorOptionsSupported) {
        this.enumeratorOptionsSupported = enumeratorOptionsSupported;
    }

    public boolean isEnumeratorMapOptionsSupported() {
        return enumeratorMapOptionsSupported;
    }

    public void setEnumeratorMapOptionsSupported(boolean enumeratorMapOptionsSupported) {
        this.enumeratorMapOptionsSupported = enumeratorMapOptionsSupported;
    }

    public List<String> getEnumeratorExtractorBeans() {
        return enumeratorExtractorBeans;
    }

    public void setEnumeratorExtractorBeans(List<String> enumeratorExtractorBeans) {
        this.enumeratorExtractorBeans = enumeratorExtractorBeans;
    }

    public List<String> getEnumeratorMapExtractorBeans() {
        return enumeratorMapExtractorBeans;
    }

    public void setEnumeratorMapExtractorBeans(List<String> enumeratorMapExtractorBeans) {
        this.enumeratorMapExtractorBeans = enumeratorMapExtractorBeans;
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

    public boolean isListAttribute() {
        return listAttribute;
    }

    public void setListAttribute(boolean listAttribute) {
        this.listAttribute = listAttribute;
    }

}
