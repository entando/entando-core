/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.apsadmin.portal.model;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.Frame;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;

/**
 * @author E.Santoboni
 */
public abstract class AbstractTestPageModelAction extends ApsAdminBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	protected PageModel createMockPageModel(String code) {
		PageModel model = new PageModel();
		model.setCode(code);
		model.setDescription("Description of model " + code);
		Frame frame0 = new Frame();
		frame0.setPos(0);
		frame0.setDescription("Freme 0");
		frame0.setMainFrame(true);
		Frame frame1 = new Frame();
		frame1.setPos(1);
		frame1.setDescription("Freme 1");
		Widget defWidg1 = new Widget();
		defWidg1.setType(this._widgetTypeManager.getWidgetType("content_viewer_list"));
		ApsProperties props1 = new ApsProperties();
		props1.setProperty("contentType", "ART");
		defWidg1.setConfig(props1);
		frame1.setDefaultWidget(defWidg1);
		Frame frame2 = new Frame();
		frame2.setPos(1);
		frame2.setDescription("Freme 2");
		Widget defWidg2 = new Widget();
		defWidg2.setType(this._widgetTypeManager.getWidgetType("login_form"));
		frame2.setDefaultWidget(defWidg2);
		Frame[] configuration = {frame0, frame1, frame2};
		model.setConfiguration(configuration);
		model.setTemplate("<strong>Freemarker template content</strong>");
		return model;
	}
	
	private void init() throws Exception {
    	try {
			this._widgetTypeManager = (IWidgetTypeManager) this.getService(SystemConstants.WIDGET_TYPE_MANAGER);
    		this._pageModelManager = (IPageModelManager) this.getService(SystemConstants.PAGE_MODEL_MANAGER);
    	} catch (Throwable t) {
    		throw new Exception(t);
        }
    }
    
	protected IWidgetTypeManager _widgetTypeManager;
    protected IPageModelManager _pageModelManager = null;
	
}