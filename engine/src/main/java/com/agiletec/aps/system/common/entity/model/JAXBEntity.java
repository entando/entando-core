/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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
import com.agiletec.aps.system.common.entity.model.attribute.DefaultJAXBAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.JAXBHypertextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.JAXBListAttribute;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "entity")
@XmlType(propOrder = {"id", "descr", "typeCode", "typeDescr", "mainGroup", "categories", "groups", "attributes"})
@XmlSeeAlso({ArrayList.class, HashMap.class, JAXBHypertextAttribute.class, JAXBListAttribute.class})
public class JAXBEntity implements Serializable {

	private static final Logger _logger = LoggerFactory.getLogger(JAXBEntity.class);
	
    public JAXBEntity() {}
    
    public JAXBEntity(IApsEntity mainEntity, String langCode) {
        try {
            this.setDescr(mainEntity.getDescr());
            this.setId(mainEntity.getId());
            this.setMainGroup(mainEntity.getMainGroup());
            this.setTypeCode(mainEntity.getTypeCode());
            this.setTypeDescr(mainEntity.getTypeDescr());
            this.setGroups(mainEntity.getGroups());
            this.setCategories(mainEntity.getCategories());
            List<AttributeInterface> attributes = mainEntity.getAttributeList();
            if (null == attributes || attributes.size() == 0) {
                return;
            }
            for (int i = 0; i < attributes.size(); i++) {
                AttributeInterface attribute = attributes.get(i);
                DefaultJAXBAttribute jaxrAttribute = attribute.getJAXBAttribute(langCode);
                if (null != jaxrAttribute) {
                    this.getAttributes().add(jaxrAttribute);
                }
            }
        } catch (Throwable t) {
        	_logger.error("Error creating JAXBEntity", t);
            //ApsSystemUtils.logThrowable(t, this, "JAXBEntity");
            throw new RuntimeException("Error creating JAXBEntity", t);
        }
    }
    
    public IApsEntity buildEntity(IApsEntity prototype, ICategoryManager categoryManager) {
        try {
            prototype.setDescr(this.getDescr());
            prototype.setId(this.getId());
            prototype.setMainGroup(this.getMainGroup());
            prototype.setTypeCode(this.getTypeCode());
            prototype.setTypeDescr(this.getTypeDescr());
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
                DefaultJAXBAttribute jaxrAttribute = this.getAttributes().get(i);
                AttributeInterface attribute = (AttributeInterface) prototype.getAttribute(jaxrAttribute.getName());
                if (null != attribute && null != jaxrAttribute
                        && attribute.getType().equals(jaxrAttribute.getType())) {
                    attribute.valueFrom(jaxrAttribute);
                }
            }
        } catch (Throwable t) {
        	_logger.error("Error creating Entity", t);
            //ApsSystemUtils.logThrowable(t, this, "buildEntity");
            throw new RuntimeException("Error creating Entity", t);
        }
        return prototype;
    }
    
    /**
     * Return the ID of the Entity.
     * @return The identification string of the Entity.
     */
    @XmlElement(name = "id", required = true)
    public String getId() {
        return this._id;
    }
    
    /**
     * Associate the Entity to the given ID code.
     * @param id The identification string of the Entity.
     */
    protected void setId(String id) {
        this._id = id;
    }
    
    /**
     * Return the code of the Entity Type.
     * @return The code of the Entity Type.
     */
    @XmlElement(name = "typeCode", required = true)
    public String getTypeCode() {
        return this._typeCode;
    }

    /**
     * Set up the code of the Entity Type.
     * @param typeCode The Entity Type code.
     */
    protected void setTypeCode(String typeCode) {
        this._typeCode = typeCode;
    }

    /**
     * Return the description of the Content Type.
     * @return The description of the Content Type.
     */
    @XmlElement(name = "typeDescription", required = true)
    public String getTypeDescr() {
        return this._typeDescr;
    }

    /**
     * Set up the description of the Entity Type.
     * @param typeDescr The description of the Entity Type.
     */
    protected void setTypeDescr(String typeDescr) {
        this._typeDescr = typeDescr;
    }

    /**
     * Return the description of the Entity.
     * @return The Entity description.
     */
    @XmlElement(name = "description", required = true)
    public String getDescr() {
        return this._descr;
    }

    /**
     * Set up the description of the Entity.
     * @param descr The description to associate to the Entity.
     */
    protected void setDescr(String descr) {
        this._descr = descr;
    }

    /**
     * Return the string that identifies the main group this Entity belongs to.
     * @return The main group this Entity belongs to.
     */
    @XmlElement(name = "mainGroup", required = true)
    public String getMainGroup() {
        return _mainGroup;
    }

    /**
     * Set the string that identifies the main group this Entity belongs to.
     * @param mainGroup The main group this Entity belongs to.
     */
    protected void setMainGroup(String mainGroup) {
        this._mainGroup = mainGroup;
    }

    /**
     * Return the set of codes of the additional groups. 
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
    
    protected void setCategories(List<Category> categories) {
        if (null != categories && categories.size() > 0) {
            this.setCategories(new HashSet<String>());
            for (int i = 0; i < categories.size(); i++) {
                Category category = categories.get(i);
                if (null != category) {
                    this.getCategories().add(category.getCode());
                }
            }
        }
    }
    
    @XmlElement(name = "attribute", required = true)
    @XmlElementWrapper(name = "attributes")
    public List<DefaultJAXBAttribute> getAttributes() {
        return _attributes;
    }
    protected void setAttributes(List<DefaultJAXBAttribute> attributes) {
        this._attributes = attributes;
    }
    
    private String _id;
    private String _typeCode;
    private String _typeDescr;
    private String _descr;
    private String _mainGroup;
    private Set<String> _groups;
    private Set<String> _categories;
    private List<DefaultJAXBAttribute> _attributes = new ArrayList<DefaultJAXBAttribute>();
    
}
