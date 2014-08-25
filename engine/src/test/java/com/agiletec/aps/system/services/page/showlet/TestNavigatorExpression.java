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

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.services.page.widget.NavigatorExpression;

/**
 * @version 1.0
 * @author E.Santoboni
 */
public class TestNavigatorExpression extends BaseTestCase {
	
	public void testSetExpression_1() throws Throwable {
		NavigatorExpression navExpr = new NavigatorExpression("super(3)");
		assertEquals(NavigatorExpression.SPEC_SUPER_ID, navExpr.getSpecId());
		assertEquals(3, navExpr.getSpecSuperLevel());
		assertTrue(navExpr.getOperatorId()<0);
	}
	
	public void testSetExpression_2() throws Throwable {
		NavigatorExpression navExpr = new NavigatorExpression("abs(1).subtree(2)");
		assertEquals(NavigatorExpression.SPEC_ABS_ID, navExpr.getSpecId());
		assertEquals(1, navExpr.getSpecAbsLevel());
		assertEquals(NavigatorExpression.OPERATOR_SUBTREE_ID, navExpr.getOperatorId());
		assertEquals(2, navExpr.getOperatorSubtreeLevel());
	}
	
	public void testSetExpression_3() throws Throwable {
		NavigatorExpression navExpr = new NavigatorExpression("current.path");
		assertEquals(NavigatorExpression.SPEC_CURRENT_PAGE_ID, navExpr.getSpecId());
		assertEquals(NavigatorExpression.OPERATOR_PATH_ID, navExpr.getOperatorId());
	}
	
	public void testSetWrongExpression_1() throws Throwable {
		NavigatorExpression navExpr = null;
		try {
			navExpr = new NavigatorExpression("wrongSpec.path");
		} catch (RuntimeException e) {
			assertTrue(e.getMessage().indexOf("wrongSpec") > 0);
		} catch (Throwable e) {
			fail();
		}
		assertNull(navExpr);
	}
	
	public void testSetWrongExpression_2() throws Throwable {
		NavigatorExpression navExpr = null;
		try {
			navExpr = new NavigatorExpression("current.wrongOperat");
		} catch (RuntimeException e) {
			assertTrue(e.getMessage().indexOf("wrongOperat") > 0);
		} catch (Throwable e) {
			fail();
		}
		assertNull(navExpr);
	}
	
	public void testGetExpression_1() throws Throwable {
		NavigatorExpression navExpr = new NavigatorExpression();
		navExpr.setSpecId(NavigatorExpression.SPEC_SUPER_ID);
		navExpr.setSpecSuperLevel(4);
		String expression = navExpr.toString();
		assertEquals("super(4)", expression);
	}
	
	public void testGetExpression_2() throws Throwable {
		NavigatorExpression navExpr = new NavigatorExpression();
		navExpr.setSpecId(NavigatorExpression.SPEC_CURRENT_PAGE_ID);
		navExpr.setOperatorId(NavigatorExpression.OPERATOR_CHILDREN_ID);
		String expression = navExpr.toString();
		assertEquals("current.children", expression);
	}
	
	public void testGetExpression_3() throws Throwable {
		NavigatorExpression navExpr = new NavigatorExpression();
		navExpr.setSpecId(NavigatorExpression.SPEC_SUPER_ID);
		navExpr.setSpecSuperLevel(2);
		navExpr.setOperatorId(NavigatorExpression.OPERATOR_SUBTREE_ID);
		navExpr.setOperatorSubtreeLevel(3);
		String expression = navExpr.toString();
		assertEquals("super(2).subtree(3)", expression);
	}
	
	public void testGetExpression_4() throws Throwable {
		NavigatorExpression navExpr = new NavigatorExpression();
		navExpr.setSpecId(NavigatorExpression.SPEC_PAGE_ID);
		navExpr.setSpecCode("pagina_11");
		navExpr.setOperatorId(NavigatorExpression.OPERATOR_PATH_ID);
		String expression = navExpr.toString();
		assertEquals("code(pagina_11).path", expression);
	}
	
	public void testGetWrongExpression_1() throws Throwable {
		NavigatorExpression navExpr = new NavigatorExpression();
		navExpr.setSpecId(7);
		navExpr.setOperatorId(NavigatorExpression.OPERATOR_PATH_ID);
		String expression = null;
		try {
			expression = navExpr.toString();
		} catch (RuntimeException e) {
			assertTrue(e.getMessage().indexOf("7") > 0);
		} catch (Throwable t) {
			fail();
		}
		assertNull(expression);
	}
	
	public void testGetWrongExpression_2() throws Throwable {
		NavigatorExpression navExpr = new NavigatorExpression();
		navExpr.setSpecId(NavigatorExpression.SPEC_PAGE_ID);
		navExpr.setSpecCode(null);
		navExpr.setOperatorId(NavigatorExpression.OPERATOR_PATH_ID);
		String expression = null;
		try {
			expression = navExpr.toString();
		} catch (RuntimeException e) {
			//Messaggio di Pagina nulla
		} catch (Throwable t) {
			fail();
		}
		assertNull(expression);
	}
	
}
