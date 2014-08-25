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
package com.agiletec.aps.system.services.page.showlet;

import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.widget.INavigatorParser;
import com.agiletec.aps.system.services.page.widget.NavigatorTarget;

/**
 * @version 1.0
 * @author E.Santoboni
 */
public class TestNavigatorParser extends BaseTestCase {
	
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	public void testGetTargets_1() throws Throwable {
		RequestContext reqCtx = this.valueRequestContext("pagina_11", "admin");
		String spec = "current.path";
		List<NavigatorTarget> targets = this._navigatorParser.parseSpec(spec, reqCtx);
		assertEquals(3, targets.size());
		NavigatorTarget target0 = targets.get(0);
		assertEquals("homepage", target0.getPage().getCode());
		NavigatorTarget target1 = targets.get(1);
		assertEquals("pagina_1", target1.getPage().getCode());
		NavigatorTarget target2 = targets.get(2);
		assertEquals("pagina_11", target2.getPage().getCode());
	}
	
	public void testGetTargets_2() throws Throwable {
		RequestContext reqCtx = this.valueRequestContext("contentview", "admin");
		String spec = "current.path";
		List<NavigatorTarget> targets = this._navigatorParser.parseSpec(spec, reqCtx);
		assertEquals(2, targets.size());
		NavigatorTarget target0 = targets.get(0);
		assertEquals("homepage", target0.getPage().getCode());
		NavigatorTarget target1 = targets.get(1);
		assertEquals("contentview", target1.getPage().getCode());
	}
	
	public void testGetTargets_3() throws Throwable {
		RequestContext reqCtx = this.valueRequestContext("pagina_11", SystemConstants.GUEST_USER_NAME);
		String spec = "abs(0).subtree(2)";
		List<NavigatorTarget> targets = this._navigatorParser.parseSpec(spec, reqCtx);
		assertEquals(5, targets.size());
		NavigatorTarget target0 = targets.get(0);
		assertEquals("homepage", target0.getPage().getCode());
		assertEquals(0, target0.getLevel());
		
		NavigatorTarget target1 = targets.get(1);
		assertEquals("pagina_1", target1.getPage().getCode());
		assertEquals(1, target1.getLevel());
		
		NavigatorTarget target2 = targets.get(2);
		assertEquals("pagina_11", target2.getPage().getCode());
		assertEquals(2, target2.getLevel());
		
		NavigatorTarget target3 = targets.get(3);
		assertEquals("pagina_12", target3.getPage().getCode());
		assertEquals(2, target3.getLevel());
		
		NavigatorTarget target4 = targets.get(4);
		assertEquals("pagina_2", target4.getPage().getCode());
		assertEquals(1, target4.getLevel());
	}
	
	public void testGetTargets_4() throws Throwable {
		RequestContext reqCtx = this.valueRequestContext("pagina_11", "editorCustomers");
		String spec = "code(homepage).children";
		List<NavigatorTarget> targets = this._navigatorParser.parseSpec(spec, reqCtx);
		assertEquals(3, targets.size());
		
		NavigatorTarget target0 = targets.get(0);
		assertEquals("pagina_1", target0.getPage().getCode());
		assertEquals(0, target0.getLevel());
		
		NavigatorTarget target1 = targets.get(1);
		assertEquals("pagina_2", target1.getPage().getCode());
		assertEquals(0, target1.getLevel());
		
		NavigatorTarget target2 = targets.get(2);
		assertEquals("customers_page", target2.getPage().getCode());
		assertEquals(0, target2.getLevel());
	}
	
	public void testGetTargets_5() throws Throwable {
		RequestContext reqCtx = this.valueRequestContext("pagina_1", "editorCoach");
		String spec = "current.subtree(1)+parent.children";
		List<NavigatorTarget> targets = this._navigatorParser.parseSpec(spec, reqCtx);
		assertEquals(7, targets.size());
		
		NavigatorTarget target0 = targets.get(0);
		assertEquals("pagina_1", target0.getPage().getCode());
		assertEquals(0, target0.getLevel());
		
		NavigatorTarget target1 = targets.get(1);
		assertEquals("pagina_11", target1.getPage().getCode());
		assertEquals(1, target1.getLevel());
		
		NavigatorTarget target2 = targets.get(2);
		assertEquals("pagina_12", target2.getPage().getCode());
		assertEquals(1, target2.getLevel());
		
		NavigatorTarget target3 = targets.get(3);
		assertEquals("pagina_1", target3.getPage().getCode());
		assertEquals(0, target3.getLevel());
		
		NavigatorTarget target4 = targets.get(4);
		assertEquals("pagina_2", target4.getPage().getCode());
		assertEquals(0, target4.getLevel());
		
		NavigatorTarget target5 = targets.get(5);
		assertEquals("coach_page", target5.getPage().getCode());
		assertEquals(0, target5.getLevel());
		
		NavigatorTarget target6 = targets.get(6);
		assertEquals("customers_page", target6.getPage().getCode());
		assertEquals(0, target6.getLevel());
	}
	
	private RequestContext valueRequestContext(String currentPageCode, String currentUserName) throws Throwable {
		RequestContext reqCtx = this.getRequestContext();
		this.setUserOnSession(currentUserName);
		IPage currentPage = this._pageManager.getPage(currentPageCode);
		reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE, currentPage);
		return reqCtx;
	}
	
	private void init() throws Exception {
		this._pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
		this._navigatorParser = (INavigatorParser) this.getApplicationContext().getBean(SystemConstants.NAVIGATOR_PARSER);
    }
	
	private IPageManager _pageManager;
	private INavigatorParser _navigatorParser;
	
}