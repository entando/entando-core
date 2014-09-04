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
package org.entando.entando.aps.system.services.controller.executor;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;

import org.apache.commons.lang.StringUtils;

import org.entando.entando.aps.system.services.controller.AbstractTestExecutorService;
import org.entando.entando.aps.system.services.guifragment.GuiFragment;
import org.entando.entando.aps.system.services.guifragment.IGuiFragmentManager;

/**
 * @author E.Santoboni
 */
public class TestWidgetExecutorService extends AbstractTestExecutorService {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testExecutor() throws Exception {
		super.setUserOnSession("admin");
		IPageManager pageManager = (IPageManager) super.getApplicationContext().getBean(SystemConstants.PAGE_MANAGER);
		IPage currentPage = pageManager.getPage("homepage");
		super.getRequestContext().addExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE, currentPage);
		ExecutorServiceInterface wes = (ExecutorServiceInterface) super.getApplicationContext().getBean("WidgetExecutorService");
		wes.service(super.getRequestContext());
		String[] widgetOutput = (String[]) super.getRequestContext().getExtraParam("ShowletOutput");
		assertNotNull(widgetOutput);
		assertEquals(currentPage.getModel().getFrames().length, widgetOutput.length);
		for (int i = 0; i < widgetOutput.length; i++) {
			String output = widgetOutput[i];
			assertNotNull(output);
			Widget currentWidget = currentPage.getWidgets()[i];
			if (null == currentWidget) {
				assertTrue(StringUtils.isBlank(output));
			} else {
				GuiFragment fragment = this._guiFragmentManager.getUniqueGuiFragmentByWidgetType(currentWidget.getType().getCode());
				if (null == fragment) {
					assertTrue(StringUtils.isBlank(output));
				} else {
					assertTrue(StringUtils.isNotBlank(output));
				}
			}
		}
	}
	
	private void init() throws Exception {
		try {
			this._guiFragmentManager = (IGuiFragmentManager) this.getApplicationContext().getBean(SystemConstants.GUI_FRAGMENT_MANAGER);
		} catch (Throwable t) {
			throw new Exception(t);
		}
	}
	
	private IGuiFragmentManager _guiFragmentManager;
}
