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

import java.util.List;
import java.util.Map;

import org.jdom.Element;

import com.agiletec.aps.system.common.entity.model.AttributeSearchInfo;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.Lang;

/**
 * Abstract class used for the implementation of "Complex Attributes". Complex attributes are those 
 * constituted by the aggregation, in different ways, of "Simple Attributes".
 * 
 * @author E.Santoboni
 */
public abstract class AbstractComplexAttribute extends AbstractAttribute {
	
	@Override
	public boolean isSimple() {
		return false;
	}
	
	/**
	 * Return the list of "Simple Attributes" constituting the Complex Attribute
	 * @return The list of simple Attributes.
	 */
	public abstract List<AttributeInterface> getAttributes();
	
	/**
	 * Return the structure of attributes suitable for the rendering process.
	 * @return The structure of attributes suitable for rendering.
	 */
	public abstract Object getRenderingAttributes();
	
	/**
	 * This method overrides the one of the abstract class so that it always returns false.
	 * This happens because Complex Attributes can never be "searchable" by design.
	 * @return Return always false.
	 * @see com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface#isSearchable()
	 */
	@Override
	public boolean isSearchable() {
		return false;
	}
	
	@Override
	public boolean isSearchableOptionSupported() {
		return false;
	}
	
	@Override
	public List<AttributeSearchInfo> getSearchInfos(List<Lang> systemLangs) {
		return null;
	}
	
	/**
	 * Set up the configuration of the Complex Attributes.
	 * @param attributeElement The JDOM element of the attribute. 
	 * @param attrTypes The list of the attributes available. 
	 * @throws ApsSystemException in case of error.
	 */
	public abstract void setComplexAttributeConfig(Element attributeElement, Map<String, AttributeInterface> attrTypes) throws ApsSystemException;
	
}
