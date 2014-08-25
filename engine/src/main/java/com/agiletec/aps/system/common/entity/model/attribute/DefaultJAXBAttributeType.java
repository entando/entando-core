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
package com.agiletec.aps.system.common.entity.model.attribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.attribute.util.BaseAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.DateAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.IAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.NumberAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.OgnlValidationRule;
import com.agiletec.aps.system.common.entity.model.attribute.util.TextAttributeValidationRules;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.attribute.util.BaseAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.DateAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.IAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.NumberAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.OgnlValidationRule;
import com.agiletec.aps.system.common.entity.model.attribute.util.TextAttributeValidationRules;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "attributeType")
@XmlType(propOrder = {"name", "description", "type", "roles", "searchable", "indexable", "validationRules"})
@XmlSeeAlso({ArrayList.class, BaseAttributeValidationRules.class, DateAttributeValidationRules.class, 
    NumberAttributeValidationRules.class, TextAttributeValidationRules.class, OgnlValidationRule.class})
public class DefaultJAXBAttributeType {

	private static final Logger _logger =  LoggerFactory.getLogger(DefaultJAXBAttributeType.class);
	
	@XmlElement(name = "name", required = true)
    public String getName() {
        return _name;
    }
    public void setName(String name) {
        this._name = name;
    }
	
	@XmlElement(name = "description", required = false)
    public String getDescription() {
		return _description;
	}
	public void setDescription(String description) {
		this._description = description;
	}
    
    @XmlElement(name = "type", required = true)
    public String getType() {
        return _type;
    }
    public void setType(String type) {
        this._type = type;
    }
    
    @XmlElement(name = "role", required = false)
    @XmlElementWrapper(name = "roles")
    public List<String> getRoles() {
        return _roles;
    }
    public void setRoles(List<String> roles) {
        this._roles = roles;
    }
    
    public Boolean getIndexable() {
        return _indexable;
    }
    public void setIndexable(Boolean indexable) {
        this._indexable = indexable;
    }
    
    public Boolean getSearchable() {
        return _searchable;
    }
    public void setSearchable(Boolean searchable) {
        this._searchable = searchable;
    }
    
    public Object getValidationRules() {
        return _validationRules;
    }
    public void setValidationRules(Object validationRules) {
        this._validationRules = validationRules;
    }
    
    public AttributeInterface createAttribute(Map<String, AttributeInterface> attributes) throws ApiException {
        AttributeInterface attribute = null;
        try {
            AttributeInterface master = attributes.get(this.getType());
            if (null == master) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Attribute Type '" + this.getType() + "' does not exist");
            }
            attribute = (AttributeInterface) master.getAttributePrototype();
            Pattern pattern = Pattern.compile("\\w+");
            Matcher matcher = pattern.matcher(this.getName());
            if (null == this.getName() || !matcher.matches()) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, 
                        "Invalid name '" + this.getName() + "' of Attribute Type '" + this.getType() + "'");
            }
            attribute.setName(this.getName());
            attribute.setDescription(this.getDescription());
            attribute.setRoles(this.toArray(this.getRoles()));
            if (null != this.getSearchable()) attribute.setSearchable(this.getSearchable().booleanValue());
            if (null != this.getIndexable()) attribute.setIndexingType(IndexableAttributeInterface.INDEXING_TYPE_TEXT);
            attribute.setValidationRules((IAttributeValidationRules) this.getValidationRules());
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
        	_logger.error("Error creating attribute '{}'", this.getName(), t);
            //ApsSystemUtils.logThrowable(t, this, "createAttribute", "Error creating attribute '" + this.getName() + "'");
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Error creating attribute '" + this.getName() + "'");
        }
        return attribute;
    }
    
    private String[] toArray(List<String> strings) {
        if (null == strings || strings.isEmpty()) return null;
        String[] array = new String[strings.size()];
        for (int i = 0; i < strings.size(); i++) {
            array[i] = strings.get(i);
        }
        return array;
    }
    
    private String _name;
	private String _description;
    private String _type;
    private List<String> _roles;
    
    private Boolean _searchable;
    private Boolean _indexable;
    
    private Object _validationRules;
    
}