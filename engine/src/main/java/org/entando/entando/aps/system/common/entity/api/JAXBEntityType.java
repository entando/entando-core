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
package org.entando.entando.aps.system.common.entity.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiError;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.DefaultJAXBAttributeType;
import com.agiletec.aps.system.common.entity.model.attribute.JAXBCompositeAttributeType;
import com.agiletec.aps.system.common.entity.model.attribute.JAXBEnumeratorAttributeType;
import com.agiletec.aps.system.common.entity.model.attribute.JAXBListAttributeType;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "entityType")
@XmlType(propOrder = {"typeCode", "typeDescription", "entityManagerName", "attributes"})
@XmlSeeAlso({JAXBListAttributeType.class, JAXBCompositeAttributeType.class, JAXBEnumeratorAttributeType.class})
public class JAXBEntityType {

	private static final Logger _logger = LoggerFactory.getLogger(JAXBEntityType.class);
	
    public JAXBEntityType() {}
    
    public JAXBEntityType(IApsEntity entityType) {
        this.setTypeCode(entityType.getTypeCode());
        this.setTypeDescription(entityType.getTypeDescr());
        List<AttributeInterface> attributes = entityType.getAttributeList();
        if (null != attributes) {
            for (int i = 0; i < attributes.size(); i++) {
                AttributeInterface attribute = attributes.get(i);
                this.getAttributes().add(attribute.getJAXBAttributeType());
            }
        }
    }
    
    public IApsEntity buildEntityType(Class entityClass, Map<String, AttributeInterface> attributes) throws ApiException, Throwable {
        List<ApiError> errors = new ArrayList<ApiError>();
        IApsEntity entityType = null;
        try {
            entityType = (IApsEntity) entityClass.newInstance();
            entityType.setTypeCode(this.getTypeCode());
            entityType.setTypeDescr(this.getTypeDescription());
            List<DefaultJAXBAttributeType> jabxAttributes = this.getAttributes();
            for (int i = 0; i < jabxAttributes.size(); i++) {
                try {
                    DefaultJAXBAttributeType jaxbAttributeType = jabxAttributes.get(i);
                    AttributeInterface attribute = jaxbAttributeType.createAttribute(attributes);
                    if (null != entityType.getAttribute(attribute.getName())) {
                        throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, 
                                "Attribute '" + attribute.getName() + "' already defined");
                    }
                    entityType.addAttribute(attribute);
                } catch (ApiException e) {
                    errors.addAll(e.getErrors());
                }
            }
        } catch (Throwable t) {
        	_logger.error("error in buildEntityType", t);
            //ApsSystemUtils.logThrowable(t, this, "buildEntityType");
            throw t;
        }
        if (!errors.isEmpty()) throw new ApiException(errors);
        return entityType;
    }
    
    @XmlElement(name = "entityManagerName", required = true)
    public String getEntityManagerName() {
        return _entityManagerName;
    }
    public void setEntityManagerName(String entityManagerName) {
        this._entityManagerName = entityManagerName;
    }
    
    @XmlElement(name = "typeCode", required = true)
    public String getTypeCode() {
        return _typeCode;
    }
    public void setTypeCode(String typeCode) {
        this._typeCode = typeCode;
    }
    
    @XmlElement(name = "typeDescription", required = true)
    public String getTypeDescription() {
        return _typeDescription;
    }
    public void setTypeDescription(String typeDescription) {
        this._typeDescription = typeDescription;
    }
    
    @XmlElement(name = "attribute", required = false)
    @XmlElementWrapper(name = "attributes")
    public List<DefaultJAXBAttributeType> getAttributes() {
        return _attributes;
    }
    public void setAttributes(List<DefaultJAXBAttributeType> attributes) {
        this._attributes = attributes;
    }
    
    private String _entityManagerName;
    private String _typeCode;
    private String _typeDescription;
    
    private List<DefaultJAXBAttributeType> _attributes = new ArrayList<DefaultJAXBAttributeType>();
    
}