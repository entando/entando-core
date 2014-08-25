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
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.parse.IApsEntityDOM;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.group.IGroupManager;

/**
 * This class represents an Entity.
 * The structure of the entity, defined during the configuration process, is built invoking the method
 * 'addAttribute', but this procedure is reserved for the Content Service which invokes this method only
 * during the system initialization.
 * The standard procedure to instantiate an entity, used during the normal execution of the system, 
 * is to make a request to the service: it will clone the prototype of the entity previously 
 * defined in the configuration.
 * @author E. Santoboni
 */
public interface IApsEntity extends Serializable{
    
    /**
     * Add an attribute to the list of the attributes of the entity.
     * @param attribute The attribute to add.
     */
    public void addAttribute(AttributeInterface attribute);
    
    /**
     * Return an Entity Attribute identified by the key.
     * @param key The name of the attribute,
     * @return The requested attribute.
     */
    public Object getAttribute(String key);
    
    /**
     * Return an Entity Attribute identified by the role.
     * @param roleName The role of the attribute,
     * @return The requested attribute.
     */
    public AttributeInterface getAttributeByRole(String roleName);
    
    /**
     * Add a category to the list of the entity categories.
     * @param category The category to add.
     */
    public void addCategory(Category category);
    
    /**
     * Return the list of categories associated to the entity.
     * @return A list of categories.
     */
    public List<Category> getCategories();
    
    /**
     * Remove a category from the list of the entity categories.
     * @param category The category to remove.
     */
    public void removeCategory(Category category);
    
    /**
     * Return a Map of the attributes defined in this entity.
     * @return A Map object containing all the attributes.
     */
    public Map<String, AttributeInterface> getAttributeMap();
    
    /**
     * Return the code of the Entity Type.
     * @return The code of the Entity Type.
     */
    public String getTypeCode();
    
    /**
     * Set up the code of the Entity Type.
     * @param typeCode The Entity Type code.
     */
    public void setTypeCode(String typeCode);
    
    /**
     * Return the description of the entity.
     * @return The description of entity.
     */
    public String getDescr();
    
    /**
     * Set up the description of the entity.
     * @param descr he description of entity.
     */
    public void setDescr(String descr);
    
    /**
     * Return the string that identifies the main group the entity belongs to.
     * @return The main group this entity belongs to.
     */
    public String getMainGroup();
    
    /**
     * Set up the main group the entity belongs to.
     * @param mainGroup The main group this entity belongs to.
     */
    public void setMainGroup(String mainGroup);
    
    /**
     * Return the set of codes belonging to the authorized groups.
     * @return The set of codes of the additional groups.
     */
    public Set<String> getGroups();
    
    /**
     * Add an additional group to those authorized.
     * @param groupName The code of the group to add.
     */
    public void addGroup(String groupName);
    
    /**
     * Return the ID of the entity.
     * @return The identification string of the entity.
     */
    public String getId();
    
    /**
     * Associate the entity to the given ID code.
     * @param id The identification string of the entity.
     */
    public void setId(String id);
    
    /**
     * Return a list of the Entity Attributes.
     * @return The list of attributes.
     */
    public List<AttributeInterface> getAttributeList();
    
    /**
     * Return the description of the Entity Type.
     * @return The description of the Entity Type.
     */
    public String getTypeDescr();
    
    /**
     * Set up the description of the Entity Type.
     * @param typeDescr The description of the Entity Type.
     */
    public void setTypeDescr(String typeDescr);
    
    /**
     * Set up the language to use in the rendering process of the entity and its attributes.
     * @param langCode The code of the language to use in the rendering process.
     */
    public void setRenderingLang(String langCode);
    
    /**
     * Set up the default language of the entity and its attributes.
     * @param langCode The code of the default language.
     */
    public void setDefaultLang(String langCode);
    
    /**
     * Create an object from the prototype.
     * @return The object created from the prototype.
     */
    public IApsEntity getEntityPrototype();
    
    /**
     * Set up the DOM class that generates the XML which defines the Entity.
     * @param entityDom The DOM class that generates the XML
     */
    public void setEntityDOM(IApsEntityDOM entityDom);
    
    /**
     * Return the XML string that describes the entity.
     * @return The XML string describing the entity.
     */
    public String getXML();
    
    /**
     * Disable those attributes whose deactivation code matches the given one.
     * @param disablingCode The deactivation code.
     */
    public void disableAttributes(String disablingCode);
    
    public void activateAttributes();
    
    public List<FieldError> validate(IGroupManager groupManager);
    
}
