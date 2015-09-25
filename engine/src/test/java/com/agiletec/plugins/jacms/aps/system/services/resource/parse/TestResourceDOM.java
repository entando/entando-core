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
package com.agiletec.plugins.jacms.aps.system.services.resource.parse;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @version 1.0
 * @author M. Morini
 */
public class TestResourceDOM extends BaseTestCase {
	
    public void testGetXMLDocument() throws ApsSystemException {  
		ResourceDOM resourceDom = new ResourceDOM();
        resourceDom.addCategory("tempcategory");
        int index = resourceDom.getXMLDocument().indexOf("tempcategory");
        assertTrue(index != -1);
    }
    
}
