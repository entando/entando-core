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
package com.agiletec.aps.system.services.widgettype;

import java.util.ArrayList;
import java.util.List;

import org.entando.entando.aps.system.services.widgettype.WidgetTypeDOM;
import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author M.Diana
 */
public class TestWidgetTypeDOM extends BaseTestCase {
	
    public void testParseConfig() throws ApsSystemException {
		String framesXml = "<config>" +
							"<parameter name=\"contentType\">" +
							"Tipo di contenuto (obbligatorio)" +
							"</parameter>" +
							"<parameter name=\"modelId\">" +
							"Modello di contenuto (obbligatorio)" +
							"</parameter>" +
							"<parameter name=\"filters\" />" +
							"<action name=\"listViewerConfig\"/>" +
							"</config>";
		WidgetTypeDOM showletTypeDOM = new WidgetTypeDOM(framesXml);
        String action = showletTypeDOM.getAction();
        assertTrue(action.equals("listViewerConfig"));
        List<WidgetTypeParameter> params = showletTypeDOM.getParameters();
        assertEquals(3, params.size());
	}
    
    public void testCreateConfig() throws ApsSystemException {
    	WidgetTypeParameter params1 = new WidgetTypeParameter();
    	params1.setName("param1");
    	params1.setDescr("Param1 Descr");
    	WidgetTypeParameter params2 = new WidgetTypeParameter();
    	params2.setName("param2");
    	params2.setDescr("Param2 Descr");
    	List<WidgetTypeParameter> params = new ArrayList<WidgetTypeParameter>();
    	params.add(params1);
    	params.add(params2);
    	WidgetTypeDOM showletTypeDOM = new WidgetTypeDOM(params, "customActionName");
    	String xml = showletTypeDOM.getXMLDocument();
    	
    	WidgetTypeDOM showletTypeDOM2 = new WidgetTypeDOM(xml);
    	assertEquals("customActionName", showletTypeDOM2.getAction());
    	List<WidgetTypeParameter> extractedParams = showletTypeDOM2.getParameters();
    	assertEquals(2, extractedParams.size());
    	assertEquals("param1", extractedParams.get(0).getName());
    	assertEquals("Param2 Descr", extractedParams.get(1).getDescr());
    }
			
}
