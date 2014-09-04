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
package com.agiletec.apsadmin.portal.specialwidget.navigator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.page.widget.NavigatorExpression;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author E.Santoboni
 */
public class TestNavigatorWidgetConfigAction extends ApsAdminBaseTestCase {

	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

	public void testInitConfigNavigator_1() throws Throwable {
		String result = this.executeConfigNavigator("admin", "homepage", "1", "leftmenu");
		assertEquals(Action.SUCCESS, result);
		INavigatorWidgetConfigAction action = (INavigatorWidgetConfigAction) this.getAction();
		Widget widget = action.getWidget();
		assertNotNull(widget);
		assertEquals(0, widget.getConfig().size());
	}

	public void testInitConfigNavigator_2() throws Throwable {
		String result = this.executeConfigNavigator("admin", "pagina_1", "2", null);
		assertEquals(Action.SUCCESS, result);
		INavigatorWidgetConfigAction action = (INavigatorWidgetConfigAction) this.getAction();
		Widget widget = action.getWidget();
		assertNotNull(widget);
		ApsProperties props = widget.getConfig();
		assertEquals(1, props.size());
		assertEquals("abs(1).subtree(2)", props.getProperty("navSpec"));
		List<NavigatorExpression> expressions = action.getExpressions();
		assertEquals(1, expressions.size());
		NavigatorExpression expression = expressions.get(0);
		assertEquals(NavigatorExpression.SPEC_ABS_ID, expression.getSpecId());
		assertEquals(1, expression.getSpecAbsLevel());
		assertEquals(NavigatorExpression.OPERATOR_SUBTREE_ID, expression.getOperatorId());
		assertEquals(2, expression.getOperatorSubtreeLevel());
	}

	public void testExecuteMoveExpression_1() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageCode", "pagina_2");
		params.put("frame", "0");
		params.put("widgetTypeCode", "leftmenu");
		params.put("navSpec", "parent.subtree(2)+abs(1).subtree(2)+current");
		params.put("movement", INavigatorWidgetConfigAction.MOVEMENT_DOWN_CODE);
		params.put("expressionIndex", "1");
		String result = this.executeMoveExpression("admin", params);
		assertEquals(Action.SUCCESS, result);
		INavigatorWidgetConfigAction action = (INavigatorWidgetConfigAction) this.getAction();
		Widget widget = action.getWidget();
		assertNotNull(widget);
		ApsProperties props = widget.getConfig();
		assertEquals(1, props.size());
		assertEquals("parent.subtree(2) + current + abs(1).subtree(2)", props.getProperty("navSpec"));
		List<NavigatorExpression> expressions = action.getExpressions();
		assertEquals(3, expressions.size());
		NavigatorExpression expression1 = expressions.get(1);
		assertEquals(NavigatorExpression.SPEC_CURRENT_PAGE_ID, expression1.getSpecId());
		assertTrue(expression1.getOperatorId()<0);
	}

	public void testExecuteMoveExpression_2() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageCode", "pagina_2");
		params.put("frame", "0");
		params.put("widgetTypeCode", "leftmenu");
		params.put("navSpec", "parent.subtree(2)+abs(1).subtree(2)+current");
		params.put("movement", INavigatorWidgetConfigAction.MOVEMENT_UP_CODE);
		params.put("expressionIndex", "2");
		String result = this.executeMoveExpression("admin", params);
		assertEquals(Action.SUCCESS, result);
		INavigatorWidgetConfigAction action = (INavigatorWidgetConfigAction) this.getAction();
		Widget widget = action.getWidget();
		assertNotNull(widget);
		ApsProperties props = widget.getConfig();
		assertEquals(1, props.size());
		assertEquals("parent.subtree(2) + current + abs(1).subtree(2)", props.getProperty("navSpec"));
		List<NavigatorExpression> expressions = action.getExpressions();
		assertEquals(3, expressions.size());
		NavigatorExpression expression1 = expressions.get(1);
		assertEquals(NavigatorExpression.SPEC_CURRENT_PAGE_ID, expression1.getSpecId());
		assertTrue(expression1.getOperatorId()<0);
	}

	public void testExecuteMoveExpression_3() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageCode", "pagina_2");
		params.put("frame", "0");
		params.put("widgetTypeCode", "leftmenu");
		params.put("navSpec", "parent.subtree(2)+abs(1).subtree(2)+current");
		params.put("movement", INavigatorWidgetConfigAction.MOVEMENT_UP_CODE);
		params.put("expressionIndex", "3");//INDICE SUPERIORE AL SIZE
		String result = this.executeMoveExpression("admin", params);
		assertEquals(Action.SUCCESS, result);
		INavigatorWidgetConfigAction action = (INavigatorWidgetConfigAction) this.getAction();
		Widget widget = action.getWidget();
		assertNotNull(widget);
		ApsProperties props = widget.getConfig();
		assertEquals(1, props.size());
		assertEquals("parent.subtree(2) + abs(1).subtree(2) + current", props.getProperty("navSpec"));
		List<NavigatorExpression> expressions = action.getExpressions();
		assertEquals(3, expressions.size());
		NavigatorExpression expression2 = expressions.get(2);
		assertEquals(NavigatorExpression.SPEC_CURRENT_PAGE_ID, expression2.getSpecId());
		assertTrue(expression2.getOperatorId()<0);
	}

	public void testExecuteRemoveExpression_1() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageCode", "pagina_2");
		params.put("frame", "0");
		params.put("widgetTypeCode", "leftmenu");
		params.put("navSpec", "parent.subtree(2)+abs(1).subtree(2)+current");
		params.put("expressionIndex", "1");
		String result = this.executeRemoveExpression("admin", params);
		assertEquals(Action.SUCCESS, result);
		INavigatorWidgetConfigAction action = (INavigatorWidgetConfigAction) this.getAction();
		Widget widget = action.getWidget();
		assertNotNull(widget);
		ApsProperties props = widget.getConfig();
		assertEquals(1, props.size());
		assertEquals("parent.subtree(2) + current", props.getProperty("navSpec"));
		List<NavigatorExpression> expressions = action.getExpressions();
		assertEquals(2, expressions.size());
		NavigatorExpression expression1 = expressions.get(1);
		assertEquals(NavigatorExpression.SPEC_CURRENT_PAGE_ID, expression1.getSpecId());
		assertTrue(expression1.getOperatorId()<0);
	}

	public void testExecuteRemoveExpression_2() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageCode", "pagina_2");
		params.put("frame", "0");
		params.put("widgetTypeCode", "leftmenu");
		params.put("navSpec", "parent.subtree(2)+abs(1).subtree(2)+current");
		params.put("expressionIndex", "3");//INDICE SUPERIORE AL SIZE
		String result = this.executeRemoveExpression("admin", params);
		assertEquals(Action.SUCCESS, result);
		INavigatorWidgetConfigAction action = (INavigatorWidgetConfigAction) this.getAction();
		Widget widget = action.getWidget();
		assertNotNull(widget);
		ApsProperties props = widget.getConfig();
		assertEquals(1, props.size());
		assertEquals("parent.subtree(2) + abs(1).subtree(2) + current", props.getProperty("navSpec"));
		List<NavigatorExpression> expressions = action.getExpressions();
		assertEquals(3, expressions.size());
		NavigatorExpression expression2 = expressions.get(2);
		assertEquals(NavigatorExpression.SPEC_CURRENT_PAGE_ID, expression2.getSpecId());
		assertTrue(expression2.getOperatorId()<0);
	}

	public void testFailureAddExpression_1() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageCode", "pagina_2");
		params.put("frame", "0");
		params.put("widgetTypeCode", "leftmenu");
		params.put("navSpec", "parent.subtree(2)+abs(1).subtree(2)+current");
		String result = this.executeAddExpression("admin", params);
		assertEquals(Action.INPUT, result);
		ActionSupport action = this.getAction();
		Map<String, List<String>> fieldErrors = action.getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertEquals(1, fieldErrors.get("specId").size());

		NavigatorWidgetConfigAction navAction = (NavigatorWidgetConfigAction) action;
		Widget widget = navAction.getWidget();
		assertNotNull(widget);
		ApsProperties props = widget.getConfig();
		assertEquals(0, props.size());
		assertEquals("parent.subtree(2)+abs(1).subtree(2)+current", navAction.getNavSpec());
		assertEquals(3, navAction.getExpressions().size());
	}

	public void testFailureAddExpression_2() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageCode", "pagina_2");
		params.put("frame", "0");
		params.put("widgetTypeCode", "leftmenu");
		params.put("navSpec", "parent.subtree(2)+abs(1).subtree(2)+current");
		params.put("specId", "3");
		params.put("specSuperLevel", "-2");
		String result = this.executeAddExpression("admin", params);
		assertEquals(Action.INPUT, result);
		ActionSupport action = this.getAction();
		assertEquals(1, action.getActionErrors().size());

		NavigatorWidgetConfigAction navAction = (NavigatorWidgetConfigAction) action;
		Widget widget = navAction.getWidget();
		assertNotNull(widget);
		ApsProperties props = widget.getConfig();
		assertEquals(0, props.size());
		assertEquals("parent.subtree(2)+abs(1).subtree(2)+current", navAction.getNavSpec());
		assertEquals(3, navAction.getExpressions().size());
	}

	public void testFailureAddExpression_3() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageCode", "pagina_2");
		params.put("frame", "0");
		params.put("widgetTypeCode", "leftmenu");
		params.put("navSpec", "parent.subtree(2)+current");
		params.put("specId", "4");
		params.put("specAbsLevel", "-1");
		String result = this.executeAddExpression("admin", params);
		assertEquals(Action.INPUT, result);
		ActionSupport action = this.getAction();
		assertEquals(1, action.getActionErrors().size());

		NavigatorWidgetConfigAction navAction = (NavigatorWidgetConfigAction) action;
		Widget widget = navAction.getWidget();
		assertNotNull(widget);
		ApsProperties props = widget.getConfig();
		assertEquals(0, props.size());
		assertEquals("parent.subtree(2)+current", navAction.getNavSpec());
		assertEquals(2, navAction.getExpressions().size());
	}

	public void testFailureAddExpression_4() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageCode", "pagina_2");
		params.put("frame", "0");
		params.put("widgetTypeCode", "leftmenu");
		params.put("navSpec", "");
		params.put("specId", "5");
		params.put("specCode", "  ");
		params.put("operatorId", "3");
		params.put("operatorSubtreeLevel", "-1");
		String result = this.executeAddExpression("admin", params);
		assertEquals(Action.INPUT, result);
		ActionSupport action = this.getAction();
		assertEquals(2, action.getActionErrors().size());

		NavigatorWidgetConfigAction navAction = (NavigatorWidgetConfigAction) action;
		Widget widget = navAction.getWidget();
		assertNotNull(widget);
		ApsProperties props = widget.getConfig();
		assertEquals(0, props.size());
		assertEquals("", navAction.getNavSpec());
		assertEquals(0, navAction.getExpressions().size());
	}

	public void testExecuteAddExpression_1() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageCode", "pagina_2");
		params.put("frame", "0");
		params.put("widgetTypeCode", "leftmenu");
		params.put("navSpec", "abs(1).subtree(2)");
		params.put("specId", String.valueOf(NavigatorExpression.SPEC_PARENT_PAGE_ID));
		params.put("operatorId", String.valueOf(NavigatorExpression.OPERATOR_CHILDREN_ID));
		String result = this.executeAddExpression("admin", params);
		assertEquals(Action.SUCCESS, result);

		NavigatorWidgetConfigAction action = (NavigatorWidgetConfigAction) this.getAction();
		assertEquals("abs(1).subtree(2) + parent.children", action.getNavSpec());
	}

	public void testExecuteAddExpression_2() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pageCode", "pagina_2");
		params.put("frame", "0");
		params.put("widgetTypeCode", "leftmenu");
		params.put("navSpec", "");
		params.put("specId", String.valueOf(NavigatorExpression.SPEC_ABS_ID));
		params.put("specAbsLevel", "1");
		params.put("operatorId", String.valueOf(NavigatorExpression.OPERATOR_SUBTREE_ID));
		params.put("operatorSubtreeLevel", "2");
		String result = this.executeAddExpression("admin", params);
		assertEquals(Action.SUCCESS, result);

		NavigatorWidgetConfigAction action = (NavigatorWidgetConfigAction) this.getAction();
		assertEquals("abs(1).subtree(2)", action.getNavSpec());
	}

	public void testSave() throws Throwable {
		String pageCode = "pagina_2";
		int frame = 0;
		IPage page = this._pageManager.getPage(pageCode);
		Widget widget = page.getWidgets()[frame];
		assertNull(widget);
		try {
			this.setUserOnSession("admin");
			this.initAction("/do/Page/SpecialWidget/Navigator", "saveNavigatorConfig");
			this.addParameter("pageCode", pageCode);
			this.addParameter("frame", String.valueOf(frame));
			this.addParameter("widgetTypeCode", "leftmenu");
			this.addParameter("navSpec", "parent.subtree(2)");
			String result = this.executeAction();
			assertEquals("configure", result);
			page = this._pageManager.getPage(pageCode);
			widget = page.getWidgets()[frame];
			assertNotNull(widget);
			assertEquals("leftmenu", widget.getType().getCode());
			assertEquals(1, widget.getConfig().size());
			assertEquals("parent.subtree(2)", widget.getConfig().getProperty("navSpec"));
		} catch (Throwable t) {
			throw t;
		} finally {
			page = this._pageManager.getPage(pageCode);
			page.getWidgets()[frame] = null;
			this._pageManager.updatePage(page);
		}
	}

	public void testFailureSaveEmptyExpression() throws Throwable {
		String pageCode = "pagina_2";
		int frame = 0;
		IPage page = this._pageManager.getPage(pageCode);
		Widget widget = page.getWidgets()[frame];
		assertNull(widget);
		try {
			this.setUserOnSession("admin");
			this.initAction("/do/Page/SpecialWidget/Navigator", "saveNavigatorConfig");
			this.addParameter("pageCode", pageCode);
			this.addParameter("frame", String.valueOf(frame));
			this.addParameter("widgetTypeCode", "leftmenu");
			this.addParameter("navSpec", "");
			String result = this.executeAction();
			assertEquals("input", result);
			NavigatorWidgetConfigAction action = (NavigatorWidgetConfigAction) this.getAction();
			assertEquals(1, action.getActionErrors().size());
		} catch (Throwable t) {
			throw t;
		} finally {
			page = this._pageManager.getPage(pageCode);
			page.getWidgets()[frame] = null;
			this._pageManager.updatePage(page);
		}
	}

	private String executeConfigNavigator(String userName,
			String pageCode, String frame, String showletTypeCode) throws Throwable {
		this.setUserOnSession(userName);
		this.initAction("/do/Page/SpecialWidget", "navigatorConfig");
		this.addParameter("pageCode", pageCode);
		this.addParameter("frame", frame);
		if (null != showletTypeCode && showletTypeCode.trim().length()>0) {
			this.addParameter("widgetTypeCode", showletTypeCode);
		}
		return this.executeAction();
	}

	private String executeMoveExpression(String userName, Map<String, String> params) throws Throwable {
		this.setUserOnSession(userName);
		this.initAction("/do/Page/SpecialWidget/Navigator", "moveExpression");
		this.addParameters(params);
		return this.executeAction();
	}

	private String executeRemoveExpression(String userName, Map<String, String> params) throws Throwable {
		this.setUserOnSession(userName);
		this.initAction("/do/Page/SpecialWidget/Navigator", "removeExpression");
		this.addParameters(params);
		return this.executeAction();
	}

	private String executeAddExpression(String userName, Map<String, String> params) throws Throwable {
		this.setUserOnSession(userName);
		this.initAction("/do/Page/SpecialWidget/Navigator", "addExpression");
		this.addParameters(params);
		String result = null;
		try {
			result = this.executeAction();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return result;
	}

	private void init() throws Exception {
    	try {
    		this._pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }

    private IPageManager _pageManager = null;

}
