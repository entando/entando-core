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
package com.agiletec.aps.system.common.entity.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractJAXBAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.JAXBBooleanAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.JAXBCompositeAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.JAXBDateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.JAXBHypertextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.JAXBListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.JAXBNumberAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.JAXBTextAttribute;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;

import org.entando.entando.aps.system.common.entity.model.attribute.JAXBEnumeratorMapAttribute;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "entity")
@XmlType(propOrder = {"id", "description", "typeCode", "typeDescription", "mainGroup", "categories", "groups", "attributes"})
@XmlSeeAlso({ArrayList.class, HashMap.class, JAXBBooleanAttribute.class, JAXBEnumeratorMapAttribute.class, JAXBCompositeAttribute.class, JAXBDateAttribute.class, JAXBHypertextAttribute.class, JAXBListAttribute.class, JAXBNumberAttribute.class, JAXBTextAttribute.class})
public class JAXBEntity implements Serializable {

    private static final Logger _logger = LoggerFactory.getLogger(JAXBEntity.class);

    public JAXBEntity() {
    }

    public JAXBEntity(IApsEntity mainEntity, String langCode) {
        try {
            this.setDescription(mainEntity.getDescription());
            this.setId(mainEntity.getId());
            this.setMainGroup(mainEntity.getMainGroup());
            this.setTypeCode(mainEntity.getTypeCode());
            this.setTypeDescription(mainEntity.getTypeDescription());
            this.setGroups(mainEntity.getGroups());
            List<Category> mainCategories = mainEntity.getCategories();
            if (null != mainCategories && mainCategories.size() > 0) {
                this.setCategories(new HashSet<>());
                for (int i = 0; i < mainCategories.size(); i++) {
                    Category category = mainCategories.get(i);
                    if (null != category) {
                        this.getCategories().add(category.getCode());
                    }
                }
            }
            List<AttributeInterface> attributes = mainEntity.getAttributeList();
            if (null == attributes || attributes.isEmpty()) {
                return;
            }
            for (int i = 0; i < attributes.size(); i++) {
                AttributeInterface attribute = attributes.get(i);
                AbstractJAXBAttribute jaxbAttribute = attribute.getJAXBAttribute(langCode);
                if (null != jaxbAttribute) {
                    this.getAttributes().add(jaxbAttribute);
                }
            }
        } catch (Throwable t) {
            _logger.error("Error creating JAXBEntity", t);
            throw new RuntimeException("Error creating JAXBEntity", t);
        }
    }

    public IApsEntity buildEntity(IApsEntity prototype, ICategoryManager categoryManager, String langCode) {
        try {
            prototype.setDescription(this.getDescription());
            prototype.setId(this.getId());
            prototype.setMainGroup(this.getMainGroup());
            prototype.setTypeCode(this.getTypeCode());
            prototype.setTypeDescription(this.getTypeDescription());
            if (null != this.getGroups() && !this.getGroups().isEmpty()) {
                Iterator<String> iter = this.getGroups().iterator();
                while (iter.hasNext()) {
                    prototype.addGroup(iter.next());
                }
            }
            if (null != this.getCategories() && !this.getCategories().isEmpty()) {
                Iterator<String> iter = this.getCategories().iterator();
                while (iter.hasNext()) {
                    String categoryCode = iter.next();
                    Category category = categoryManager.getCategory(categoryCode);
                    if (null != category) {
                        prototype.addCategory(category);
                    }
                }
            }
            if (null == this.getAttributes()) {
                return prototype;
            }
            for (int i = 0; i < this.getAttributes().size(); i++) {
                AbstractJAXBAttribute jaxrAttribute = this.getAttributes().get(i);
                AttributeInterface attribute = (AttributeInterface) prototype.getAttribute(jaxrAttribute.getName());
                if (null != attribute && attribute.getType().equals(jaxrAttribute.getType())) {
                    attribute.valueFrom(jaxrAttribute, langCode);
                }
            }
        } catch (Throwable t) {
            _logger.error("Error creating Entity", t);
            throw new RuntimeException("Error creating Entity", t);
        }
        return prototype;
    }

    /**
     * Return the ID of the Entity.
     *
     * @return The identification string of the Entity.
     */
    @XmlElement(name = "id", required = true)
    public String getId() {
        return this._id;
    }

    /**
     * Associate the Entity to the given ID code.
     *
     * @param id The identification string of the Entity.
     */
    public void setId(String id) {
        this._id = id;
    }

    /**
     * Return the code of the Entity Type.
     *
     * @return The code of the Entity Type.
     */
    @XmlElement(name = "typeCode", required = true)
    public String getTypeCode() {
        return this._typeCode;
    }

    /**
     * Set up the code of the Entity Type.
     *
     * @param typeCode The Entity Type code.
     */
    protected void setTypeCode(String typeCode) {
        this._typeCode = typeCode;
    }

    /**
     * Return the description of the Content Type.
     *
     * @return The description of the Content Type.
     */
    @XmlElement(name = "typeDescription", required = true)
    public String getTypeDescription() {
        return this._typeDescription;
    }

    /**
     * Set up the description of the Entity Type.
     *
     * @param typeDescription The description of the Entity Type.
     */
    protected void setTypeDescription(String typeDescription) {
        this._typeDescription = typeDescription;
    }

    /**
     * Return the description of the Entity.
     *
     * @return The Entity description.
     */
    @XmlElement(name = "description", required = true)
    public String getDescription() {
        return this._description;
    }

    /**
     * Set up the description of the Entity.
     *
     * @param description The description to associate to the Entity.
     */
    protected void setDescription(String description) {
        this._description = description;
    }

    /**
     * Return the string that identifies the main group this Entity belongs to.
     *
     * @return The main group this Entity belongs to.
     */
    @XmlElement(name = "mainGroup", required = true)
    public String getMainGroup() {
        return _mainGroup;
    }

    /**
     * Set the string that identifies the main group this Entity belongs to.
     *
     * @param mainGroup The main group this Entity belongs to.
     */
    protected void setMainGroup(String mainGroup) {
        this._mainGroup = mainGroup;
    }

    /**
     * Return the set of codes of the additional groups.
     *
     * @return The set of codes belonging to the additional groups.
     */
    @XmlElement(name = "group", required = true)
    @XmlElementWrapper(name = "groups")
    public Set<String> getGroups() {
        return _groups;
    }

    protected void setGroups(Set<String> groups) {
        this._groups = groups;
    }

    /**
     * Return the set of codes of the additional categories.
     *
     * @return The set of codes belonging to the additional categories.
     */
    @XmlElement(name = "category", required = true)
    @XmlElementWrapper(name = "categories")
    public Set<String> getCategories() {
        return _categories;
    }

    protected void setCategories(Set<String> categories) {
        this._categories = categories;
    }

    @XmlElement(name = "attribute", required = true)
    @XmlElementWrapper(name = "attributes")
    public List<AbstractJAXBAttribute> getAttributes() {
        return _attributes;
    }

    protected void setAttributes(List<AbstractJAXBAttribute> attributes) {
        this._attributes = attributes;
    }

    private String _id;
    private String _typeCode;
    private String _typeDescription;
    private String _description;
    private String _mainGroup;
    private Set<String> _groups;
    private Set<String> _categories;
    private List<AbstractJAXBAttribute> _attributes = new ArrayList<>();

}
