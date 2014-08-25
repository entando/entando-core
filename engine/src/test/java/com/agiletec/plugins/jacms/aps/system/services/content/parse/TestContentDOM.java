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
package com.agiletec.plugins.jacms.aps.system.services.content.parse;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * @version 1.0
 * @author M. Morini
 */
public class TestContentDOM extends BaseTestCase {
	
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void testGetXMLDocument() throws ApsSystemException {
    	Content contentTest = this._contentManager.createContentType("ART");
    	assertNotNull(contentTest);
    	contentTest.addGroup("tempGroupName");
    	String xml = contentTest.getXML();
		int index = xml.indexOf("tempGroupName");
		assertTrue((index != -1));
	}
    
    private void init() throws Exception {
		try {
			_contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
		} catch (Throwable t) {
			throw new Exception(t);
		}
	}

	private IContentManager _contentManager = null;
    
}
