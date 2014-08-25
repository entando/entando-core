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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.parse.IApsEntityDOM;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.group.IGroupManager;

/** 
 * This class represents an entity.
 * The structure of the entity, defined during the configuration process, is built invoking the method
 * 'addAttribute', but this procedure is reserved for the Entity Service which invokes this method only
 * during the system initialization.
 * The standard procedure to instantiate an entity, used during the normal execution of the system, 
 * is to make a request to the service: it will clone the prototype of the entity previously 
 * defined in the configuration.
 * @author E.Santoboni
 */
public class ApsEntity implements IApsEntity {

	private static final Logger _logger = LoggerFactory.getLogger(ApsEntity.class);
	
    /**
     * Initialization of the entity with its related elements.
     */
    public ApsEntity() {
        this._attributeList = new ArrayList<AttributeInterface>();
        this._attributeMap = new HashMap<String, AttributeInterface>();
        this._categories = new ArrayList<Category>();
        this._groups = new HashSet<String>();
    }

    /**
     * Return the ID of the entity.
     * @return The identification string of the entity.
     */
	@Override
    public String getId() {
        return this._id;
    }

    /**
     * Associate the entity to the given ID code.
     * @param id The identification string of the entity.
     */
	@Override
    public void setId(String id) {
        this._id = id;
    }

    /**
     * Return the code of the Entity Type.
     * @return The code of the Entity Type.
     */
	@Override
    public String getTypeCode() {
        return this._typeCode;
    }

    /**
     * Set up the code of the Entity Type.
     * @param typeCode The Entity Type code.
     */
	@Override
    public void setTypeCode(String typeCode) {
        this._typeCode = typeCode;
    }

    /**
     * Return the description of the entity.
     * @return The entity description.
     */
	@Override
    public String getDescr() {
        return this._descr;
    }

    /**
     * Set up the description of the entity.
     * @param descr The description to associate to the entity.
     */
	@Override
    public void setDescr(String descr) {
        this._descr = descr;
    }

    /**
     * Return the string that identifies the main group this entity belongs to.
     * @return The main group this entity belongs to.
     */
	@Override
    public String getMainGroup() {
        return _mainGroup;
    }

    /**
     * Set the string that identifies the main group this entity belongs to.
     * @param mainGroup The main group this entity belongs to.
     */
	@Override
    public void setMainGroup(String mainGroup) {
        this._mainGroup = mainGroup;
    }

    /**
     * Return the set of codes of the additional groups authorized to view the entity in the front-end. 
     * @return The set of codes belonging to the additional group authorized to access the entity, 
     */
	@Override
    public Set<String> getGroups() {
        return _groups;
    }

    /**
     * Add a group (code) authorized to view/access the entity in the Front-end.
     * @param groupName The group to add.
     */
	@Override
    public void addGroup(String groupName) {
        this.getGroups().add(groupName);
    }

    /**
     * Add an attribute to the list of the Entity Attributes.
     * @param attribute An attribute of the entity.
     */
	@Override
    public void addAttribute(AttributeInterface attribute) {
        this._attributeList.add(attribute);
        this._attributeMap.put(attribute.getName(), attribute);
    }

    /**
     * Return the Entity Attribute identified by the the attribute name.
     * @param key The name of the attribute
     * @return The requested attribute.
     */
	@Override
    public Object getAttribute(String key) {
        return this._attributeMap.get(key);
    }
    
	@Override
    public AttributeInterface getAttributeByRole(String roleName) {
        List<AttributeInterface> attributes = this.getAttributeList();
        for (int i = 0; i < attributes.size(); i++) {
            AttributeInterface attribute = attributes.get(i);
            if (null != attribute.getRoles()
                    && Arrays.asList(attribute.getRoles()).contains(roleName)) {
                return attribute;
            }
        }
        return null;
    }

    /**
     * Add a new category to the list of the entity categories.
     * @param category The category to add.
     */
	@Override
    public void addCategory(Category category) {
        this._categories.add(category);
    }

    /**
     * Return the list of the categories associated to the entity.
     * @return A list of categories.
     */
	@Override
    public List<Category> getCategories() {
        return this._categories;
    }

    /**
     * Remove a category from the list of the entity categories.
     * @param category The category to remove from the list.
     */
	@Override
    public void removeCategory(Category category) {
        this._categories.remove(category);
    }

    /**
     * Return the Map of the Entity Attributes.
     * @return A map containing the attributes.
     */
	@Override
    public Map<String, AttributeInterface> getAttributeMap() {
        return this._attributeMap;
    }

    /**
     * Return a list of the Entity Attributes.
     * @return The list of attributes.
     */
	@Override
    public List<AttributeInterface> getAttributeList() {
        return this._attributeList;
    }

    /**
     * Return the description of the Entity Type.
     * @return The description of the Entity Type.
     */
	@Override
    public String getTypeDescr() {
        return this._typeDescr;
    }

    /**
     * Set up the description of the Entity Type.
     * @param typeDescr The description of the Entity Type.
     */
	@Override
    public void setTypeDescr(String typeDescr) {
        this._typeDescr = typeDescr;
    }

    /**
     * Set up the language to use in the rendering process of the entity and its attributes.
     * @param langCode The code of the language to use in the rendering process.
     */
	@Override
    public void setRenderingLang(String langCode) {
        this._renderingLang = langCode;
        for (int i = 0; i < this._attributeList.size(); i++) {
            AttributeInterface attr = (AttributeInterface) this._attributeList.get(i);
            attr.setRenderingLang(this._renderingLang);
        }
    }

    /**
     * Set up the default language of the entity and its attributes.
     * @param langCode The code of the default language.
     */
	@Override
    public void setDefaultLang(String langCode) {
        this._defaultLang = langCode;
        for (int i = 0; i < this._attributeList.size(); i++) {
            AttributeInterface attr = (AttributeInterface) this._attributeList.get(i);
            attr.setDefaultLangCode(this._defaultLang);
        }
    }

    /**
     * Create an object from the prototype.
     * @return The object created from the prototype.
     */
	@Override
    public IApsEntity getEntityPrototype() {
        IApsEntity entity = null;
        try {
            Class entityClass = Class.forName(this.getClass().getName());
            entity = (IApsEntity) entityClass.newInstance();
            entity.setId(null);
            entity.setTypeCode(this.getTypeCode());
            entity.setTypeDescr(this.getTypeDescr());
            entity.setDescr(this.getDescr());
            AttributeInterface attr;
            for (int i = 0; i < this._attributeList.size(); i++) {
                attr = (AttributeInterface) this._attributeList.get((i));
                attr = (AttributeInterface) attr.getAttributePrototype();
                attr.setParentEntity(entity);
                entity.addAttribute(attr);
            }
            entity.setEntityDOM(this.getEntityDOM());
        } catch (Exception e) {
        	_logger.error("Error creating entity prototype", e);
            //ApsSystemUtils.logThrowable(e, this, "getEntityPrototype", "Error creating entity prototype");
            throw new RuntimeException("Error creating entity prototype", e);
        }
        return entity;
    }
    
	@Override
    public String getXML() {
        IApsEntityDOM entityDOM = this.getBuildJDOM();
        String xml = entityDOM.getXMLDocument();
        entityDOM.dispose();
        return xml;
    }

    /**
     * Return the DOM class that generates the XML corresponding to the entity
     * with its data.
     * This method must be extended to support customized XML structures;
     * this happen when, for example, a custom entity is based on an
     * object class which, in turn, extends ApsEntity.
     * @return The DOM class that generates the XML 
     */
    protected IApsEntityDOM getBuildJDOM() {
        IApsEntityDOM entityDom = this.getEntityDOM();
        entityDom.init();
        entityDom.setId(String.valueOf(this.getId()));
        entityDom.setTypeCode(this._typeCode);
        entityDom.setTypeDescr(this._typeDescr);
        entityDom.setDescr(this._descr);
        entityDom.setMainGroup(this._mainGroup);
        Iterator<String> iterGroups = this.getGroups().iterator();
        while (iterGroups.hasNext()) {
            String groupName = iterGroups.next();
            entityDom.addGroup(groupName);
        }
        Iterator<Category> iterCategory = this._categories.iterator();
        while (iterCategory.hasNext()) {
            Category category = iterCategory.next();
            entityDom.addCategory(category.getCode());
        }
        Iterator<AttributeInterface> iterAttribute = this._attributeList.iterator();
        while (iterAttribute.hasNext()) {
            AttributeInterface currentAttribute = iterAttribute.next();
            Element attributeElement = currentAttribute.getJDOMElement();
            if (attributeElement != null) {
                entityDom.addAttribute(currentAttribute.getJDOMElement());
            }
        }
        return entityDom;
    }

    /**
     * Disable those attributes whose deactivation code matches the given one.
     * @param disablingCode The deactivation code.
     */
	@Override
    public void disableAttributes(String disablingCode) {
        Iterator<AttributeInterface> iterAttribute = this.getAttributeList().iterator();
        while (iterAttribute.hasNext()) {
            AttributeInterface currentAttribute = iterAttribute.next();
            currentAttribute.disable(disablingCode);
        }
    }
	
	@Override
    public void activateAttributes() {
        Iterator<AttributeInterface> iterAttribute = this.getAttributeList().iterator();
        while (iterAttribute.hasNext()) {
            AttributeInterface currentAttribute = iterAttribute.next();
            currentAttribute.activate();
        }
    }
	
    /**
     * Return the DOM class that generates the XML associated to the entity.
     * @return The DOM class that generates the XML
     */
    protected IApsEntityDOM getEntityDOM() {
        return _entityDom;
    }
    
	@Override
    public void setEntityDOM(IApsEntityDOM entityDom) {
        this._entityDom = entityDom;
    }
    
	@Override
    public List<FieldError> validate(IGroupManager groupManager) {
        List<FieldError> errors = new ArrayList<FieldError>();
        try {
            if (null != this.getMainGroup() && null == groupManager.getGroup(this.getMainGroup())) {
                FieldError error = new FieldError("mainGroup", FieldError.INVALID);
                error.setMessage("Invalid main group - " + this.getMainGroup());
                errors.add(error);
            }
            if (null != this.getGroups()) {
                Iterator<String> groupsIter = this.getGroups().iterator();
                while (groupsIter.hasNext()) {
                    String groupName = groupsIter.next();
                    if (null == groupManager.getGroup(groupName)) {
                        FieldError error = new FieldError("extraGroup", FieldError.INVALID);
                        error.setMessage("Invalid extra group - " + groupName);
                        errors.add(error);
                    }
                }
            }
            if (null != this.getAttributeList()) {
                List<AttributeInterface> attributes = this.getAttributeList();
                for (int i = 0; i < attributes.size(); i++) {
                    AttributeInterface attribute = attributes.get(i);
                    AttributeTracer tracer = new AttributeTracer();
                    List<AttributeFieldError> attributeErrors = attribute.validate(tracer);
                    if (null != attributeErrors) {
                        errors.addAll(attributeErrors);
                    }
                }
            }
        } catch (Throwable t) {
        	_logger.error("Error validating entity", t);
            //ApsSystemUtils.logThrowable(t, this, "validate");
            throw new RuntimeException("Error validating entity");
        }
        return errors;
    }
    
    private String _id;
    private String _typeCode;
    private String _typeDescr;
    private String _descr;
    private String _mainGroup;
    private Set<String> _groups;
    private List<AttributeInterface> _attributeList;
    private Map<String, AttributeInterface> _attributeMap;
    private List<Category> _categories;
    private String _renderingLang;
    private String _defaultLang;
    private IApsEntityDOM _entityDom;
    
}