/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.plugins.jacms.aps.system.services.content.util;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.common.util.EntityAttributeIterator;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * @version 1.0
 * @author M. Morini
 */
public class TestContentAttributeIterator extends BaseTestCase {
	
    public void testIterator() throws ApsSystemException {  
		Content content = new Content();
    	AttributeInterface attribute = new MonoTextAttribute();
    	attribute.setName("temp");
    	attribute.setDefaultLangCode("it");
    	attribute.setRenderingLang("it");
    	attribute.setSearchable(true);
    	attribute.setType("Monotext");        
        content.addAttribute(attribute);
        EntityAttributeIterator attributeIterator = new EntityAttributeIterator(content);
        boolean contains = false;
        while (attributeIterator.hasNext()) {
        	attribute = (AttributeInterface) attributeIterator.next();
			contains = attribute.getName().equals("temp");
		}
        assertTrue(contains);
    }        
}
