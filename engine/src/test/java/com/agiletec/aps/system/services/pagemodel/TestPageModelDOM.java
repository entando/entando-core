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
package com.agiletec.aps.system.services.pagemodel;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;

/**
 * @author M.Diana
 */
public class TestPageModelDOM extends BaseTestCase {
	
    public void testGetFrames() throws Throwable {
		IWidgetTypeManager widgetTypeManager = 
        	(IWidgetTypeManager) this.getService(SystemConstants.WIDGET_TYPE_MANAGER);
        PageModelDOM pageModelDOM = new PageModelDOM(FRAMES_XML, widgetTypeManager);
		Frame[] configuration = pageModelDOM.getConfiguration();
        assertTrue(configuration[0].getDescription().equals("Box sinistra alto"));
        assertEquals("content_viewer",configuration[1].getDefaultWidget().getType().getCode());
	} 
    
    public final static String FRAMES_XML = "<frames>" 
			+ "<frame pos=\"0\"><descr>Box sinistra alto</descr></frame>"
			+ "<frame pos=\"1\"><descr>Box sinistra basso</descr><defaultWidget code=\"content_viewer\" /></frame>"
			+ "<frame pos=\"2\" main=\"true\"><descr>Box centrale 1</descr></frame>"
			+ "<frame pos=\"3\"><descr>Box centrale 2</descr></frame>"
			+ "<frame pos=\"4\"><descr>Box destra alto</descr></frame>"
			+ "<frame pos=\"5\"><descr>Box destra basso</descr></frame>"
			+ "</frames>";
			
}
