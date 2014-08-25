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
package com.agiletec.aps.system.common.entity.model.attribute.util;

import java.io.Serializable;
import java.util.List;

import org.jdom.Element;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.services.lang.ILangManager;

/**
 * @author E.Santoboni
 */
public interface IAttributeValidationRules extends Serializable {
    
    public IAttributeValidationRules clone();
    
	public boolean isEmpty();
	
    public void setConfig(Element attributeElement);
    
    public Element getJDOMConfigElement();
    
    /**
     * Set up the required (mandatory) condition for the current attribute.
     * @param required True if the attribute is mandatory
     */
    public void setRequired(boolean required);
    
    /**
     * Test whether this attribute is declared mandatory or not.
     * @return True if the attribute is mandatory, false otherwise.
     */
    public boolean isRequired();
    
    public OgnlValidationRule getOgnlValidationRule();

    public void setOgnlValidationRule(OgnlValidationRule ognlValidationRule);
    
    public List<AttributeFieldError> validate(AttributeInterface attribute, AttributeTracer tracer, ILangManager langManager);
    
    public static final String VALIDATIONS_ELEMENT_NAME = "validations";
    
}