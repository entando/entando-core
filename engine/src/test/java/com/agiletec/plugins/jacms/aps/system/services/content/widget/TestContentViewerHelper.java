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
package com.agiletec.plugins.jacms.aps.system.services.content.widget;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;

/**
 * @author W.Ambu
 */
public class TestContentViewerHelper extends BaseTestCase {
	
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
    public void testGetRenderedContent() throws Throwable {
        try {
            String contentId = "ART1";
            String modelId = "3";
            String renderedContent = _helper.getRenderedContent(contentId, modelId, _requestContext);
            
            String expected = "------ RENDERING CONTENUTO: id = ART1; ---------\n" +
            		"ATTRIBUTI:\n" +
            		"  - AUTORI (Monolist-Monotext):\n" +
            		"         testo=Pippo;\n" +
            		"         testo=Paperino;\n" +
            		"         testo=Pluto;\n" +
            		"  - TITOLO (Text): testo=Il titolo;\n" +
            		"  - VEDI ANCHE (Link): testo=Spiderman, dest=http://www.spiderman.org;\n" +
            		"  - FOTO (Image): testo=Image description, src(1)=/Entando/resources/cms/images/lvback_d1.jpg;\n" +
            		"  - DATA (Date): data_media = 10-mar-2004;\n" +
            		"------ END ------";
            assertEquals(replaceNewLine(expected.trim()), replaceNewLine(renderedContent.trim()));
        } catch (Throwable t) {
            throw t;
        }
    }
    
    private String replaceNewLine(String input) {
    	input = input.replaceAll("\\n", "");
    	input = input.replaceAll("\\r", "");
        return input;
    }
    
    public void testGetRenderedContentNotApproved() throws Throwable {
        try {
            String contentId = "ART2";
            String modelId = "3";
            String renderedContent = _helper.getRenderedContent(
                    contentId, modelId, _requestContext);
            assertEquals("", renderedContent);
        } catch (Throwable t) {
            throw t;
        }
    }
    
    public void testGetRenderedContentNotPresent() throws Throwable {
        try {
            String contentId = "ART3";
            String modelId = "3";
            String renderedContent = _helper.getRenderedContent(
                    contentId, modelId, _requestContext);
            assertEquals("", renderedContent);
        } catch (Throwable t) {
            throw t;
        }
    }
    
    private void init() throws Exception {
        try {
            _requestContext = this.getRequestContext();
            Lang lang = new Lang();
            lang.setCode("it");
            lang.setDescr("italiano");
            _requestContext.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG, lang);
            
            Widget widget = new Widget();
            IWidgetTypeManager showletTypeMan = 
            	(IWidgetTypeManager) this.getService(SystemConstants.WIDGET_TYPE_MANAGER);
            WidgetType showletType = showletTypeMan.getWidgetType("content_viewer");
            widget.setType(showletType);
            widget.setConfig(new ApsProperties());
            _requestContext.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET, widget);
            
            this._helper = (IContentViewerHelper) this.getApplicationContext().getBean("jacmsContentViewerHelper");
        } catch (Throwable t) {
            throw new Exception(t);
        }
    }
    
    private RequestContext _requestContext;
    private IContentViewerHelper _helper;
    
}