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
package com.agiletec.apsadmin.portal;

import java.util.Collection;


import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ITreeAction;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestPageTreeAction extends ApsAdminBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testViewTree_1() throws Throwable {
		this.initAction("/do/Page", "viewTree");
		this.setUserOnSession("admin");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		ITreeNode root = ((IPageTreeAction) this.getAction()).getAllowedTreeRootNode();
		assertNotNull(root);
		assertEquals("homepage", root.getCode());
		assertEquals(6, root.getChildren().length);
		ITreeNode showableRoot = ((IPageTreeAction) this.getAction()).getShowableTree();
		assertEquals("homepage", showableRoot.getCode());
		assertEquals(0, showableRoot.getChildren().length);
	}

	public void testViewTree_2() throws Throwable {
		this.initAction("/do/Page", "viewTree");
		this.setUserOnSession("pageManagerCustomers");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		ITreeNode root = ((IPageTreeAction) this.getAction()).getAllowedTreeRootNode();
		assertNotNull(root);
		assertEquals(AbstractPortalAction.VIRTUAL_ROOT_CODE, root.getCode());
		assertEquals(1, root.getChildren().length);
		assertEquals("customers_page", root.getChildren()[0].getCode());
		ITreeNode showableRoot = ((IPageTreeAction) this.getAction()).getShowableTree();
		assertEquals(AbstractPortalAction.VIRTUAL_ROOT_CODE, showableRoot.getCode());
		assertEquals(0, showableRoot.getChildren().length);
	}

	public void testViewTree_3() throws Throwable {
		this.initAction("/do/Page", "openCloseTreeNode");
		this.addParameter("targetNode", "pagina_12");
		this.addParameter("treeNodeActionMarkerCode", ITreeAction.ACTION_MARKER_OPEN);
		this.setUserOnSession("admin");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		this.checkTestViewTree_3_4();
	}

	public void testViewTree_4() throws Throwable {
		this.initAction("/do/Page", "viewTree");
		this.addParameter("selectedNode", "pagina_12");
		this.setUserOnSession("admin");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		this.checkTestViewTree_3_4();
	}

	private void checkTestViewTree_3_4() throws Throwable {
		ITreeNode showableRoot = ((IPageTreeAction) this.getAction()).getShowableTree();
		assertEquals("homepage", showableRoot.getCode());
		ITreeNode[] children = showableRoot.getChildren();
		assertEquals(6, children.length);
		boolean check = false;
		for (int i = 0; i < children.length; i++) {
			ITreeNode child = children[i];
			if (child.getCode().equals("pagina_1")) {
				assertEquals(2, child.getChildren().length);
				assertEquals("pagina_11", child.getChildren()[0].getCode());
				assertEquals("pagina_12", child.getChildren()[1].getCode());
				check = true;
			} else {
				assertEquals(0, child.getChildren().length);
			}
		}
		if (!check) fail();
	}

	public void testViewTree_5() throws Throwable {
		this.initAction("/do/Page", "openCloseTreeNode");
		this.addParameter("targetNode", "customers_page");
		this.addParameter("treeNodeActionMarkerCode", ITreeAction.ACTION_MARKER_OPEN);
		this.setUserOnSession("pageManagerCustomers");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		this.checkTestViewTree_5_6();
	}

	public void testViewTree_6() throws Throwable {
		this.initAction("/do/Page", "viewTree");
		this.addParameter("selectedNode", "customers_page");
		this.setUserOnSession("pageManagerCustomers");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		this.checkTestViewTree_5_6();
	}

	private void checkTestViewTree_5_6() throws Throwable {
		ITreeNode showableRoot = ((IPageTreeAction) this.getAction()).getShowableTree();
		assertEquals(AbstractPortalAction.VIRTUAL_ROOT_CODE, showableRoot.getCode());
		ITreeNode[] children = showableRoot.getChildren();
		assertEquals(1, children.length);
		boolean check = false;
		for (int i = 0; i < children.length; i++) {
			ITreeNode child = children[i];
			if (child.getCode().equals("customers_page")) {
				assertEquals(2, child.getChildren().length);
				assertEquals("customer_subpage_1", child.getChildren()[0].getCode());
				assertEquals("customer_subpage_2", child.getChildren()[1].getCode());
				check = true;
			} else {
				assertEquals(0, child.getChildren().length);
			}
		}
		if (!check) fail();
	}

	public void testMoveHome() throws Throwable {
		this.initAction("/do/Page", "moveUp");
		this.setUserOnSession("admin");
		this.addParameter("selectedNode", "homepage");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		Collection<String> errors = this.getAction().getActionErrors();
		assertEquals(1, errors.size());
	}

	public void testMoveForAdminUser() throws Throwable {
		String pageToMoveCode = "pagina_12";
		String sisterPageCode = "pagina_11";
		IPage pageToMove = _pageManager.getPage(pageToMoveCode);
		IPage sisterPage = _pageManager.getPage(sisterPageCode);
		assertNotNull(pageToMove);
		assertEquals(pageToMove.getPosition(), 2);
		assertNotNull(sisterPage);
		assertEquals(sisterPage.getPosition(), 1);

		this.initAction("/do/Page", "moveUp");
		this.setUserOnSession("admin");
		this.addParameter("selectedNode", pageToMoveCode);
		String result = this.executeAction();

		assertEquals(Action.SUCCESS, result);
		Collection<String> messages = this.getAction().getActionMessages();
		assertEquals(0, messages.size());

		pageToMove = this._pageManager.getPage(pageToMoveCode);
		assertEquals(pageToMove.getPosition(), 1);
		sisterPage = this._pageManager.getPage(sisterPageCode);
		assertEquals(sisterPage.getPosition(), 2);

		this.initAction("/do/Page", "moveDown");
		this.setUserOnSession("admin");
		this.addParameter("selectedNode", pageToMoveCode);
		result = this.executeAction();

		assertEquals(Action.SUCCESS, result);
		messages = this.getAction().getActionMessages();
		assertEquals(0, messages.size());

		pageToMove = _pageManager.getPage(pageToMoveCode);
		assertEquals(pageToMove.getPosition(), 2);
		sisterPage = _pageManager.getPage(sisterPageCode);
		assertEquals(sisterPage.getPosition(), 1);
	}

	public void testMoveForCoachUser() throws Throwable {
		String pageToMoveCode = "pagina_12";
		this.setUserOnSession("pageManagerCoach");
		this.initAction("/do/Page", "moveDown");
		this.addParameter("selectedNode", pageToMoveCode);
		String result = this.executeAction();
		assertEquals("pageTree", result);
		Collection<String> errors = this.getAction().getActionErrors();
		assertEquals(1, errors.size());
	}

	public void testMovementNotAllowed() throws Throwable {
		String pageToMoveCode = "primapagina";
		this.setUserOnSession("admin");
		this.initAction("/do/Page", "moveUp");
		this.addParameter("selectedNode", pageToMoveCode);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		Collection<String> errors = this.getAction().getActionErrors();
		assertEquals(1, errors.size());

		pageToMoveCode = "errorpage";
		this.initAction("/do/Page", "moveDown");
		this.addParameter("selectedNode", pageToMoveCode);
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		errors = this.getAction().getActionErrors();
		assertEquals(1, errors.size());
	}

	public void testCopyForAdminUser() throws Throwable {
		String pageToCopy = this._pageManager.getRoot().getCode();
		this.executeCopyPage(pageToCopy, "admin");

		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		String copyingPageCode = ((PageTreeAction)this.getAction()).getCopyingPageCode();
		assertEquals(pageToCopy, copyingPageCode);

		this.executeCopyPage("wrongPageCode", "admin");
		result = this.executeAction();
		assertEquals("pageTree", result);
		PageTreeAction action = (PageTreeAction)this.getAction();
		copyingPageCode = action.getCopyingPageCode();
		assertNull(copyingPageCode);
		assertEquals(1, action.getActionErrors().size());
	}

	public void testCopyForCoachUser() throws Throwable {
		String pageToCopy = this._pageManager.getRoot().getCode();
		this.executeCopyPage(pageToCopy, "pageManagerCoach");
		String result = this.executeAction();
		assertEquals("pageTree", result);
		assertEquals(1, this.getAction().getActionErrors().size());

		IPage customers_page = _pageManager.getPage("customers_page");
		this.executeCopyPage(customers_page.getCode(), "pageManagerCoach");
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		String copyingPageCode = ((PageTreeAction)this.getAction()).getCopyingPageCode();
		assertEquals(customers_page.getCode(), copyingPageCode);
	}

	private void executeCopyPage(String pageCodeToCopy, String userName) throws Throwable {
		this.setUserOnSession(userName);
		this.initAction("/do/Page", "copy");
		this.addParameter("selectedNode", pageCodeToCopy);
	}


	public void testMoveWidgetUp() throws Throwable {
		IPage page = this._pageManager.getRoot();
		String pageCode = page.getCode();
		try {
			Widget configWidget = page.getWidgets()[0];
			Widget nullWidget = page.getWidgets()[1];
			assertNotNull(configWidget);
			assertNull(nullWidget);
			
			String result = this.executeMoveDown(pageCode, 0, "admin");
			assertEquals(Action.SUCCESS, result);
			IPage updatedPage = this._pageManager.getRoot();
			Widget w00 = updatedPage.getWidgets()[0];
			Widget w11 = updatedPage.getWidgets()[1];
			assertNull(w00);
			assertEquals(w11.getType().getCode(), configWidget.getType().getCode());
			
			result = this.executeMoveUp(pageCode, 1, "admin");
			assertEquals(Action.SUCCESS, result);
			updatedPage = this._pageManager.getRoot();
			w00 = updatedPage.getWidgets()[0];
			w11 = updatedPage.getWidgets()[1];
			assertEquals(w00.getType().getCode(),  configWidget.getType().getCode());
			assertNull(w11);
			
		} finally {
			this._pageManager.updatePage(page);
		}
	}

	private String executeMoveUp(String selectedNode, int frame, String userName) throws Throwable {
		this.setUserOnSession(userName);
		this.initAction("/do/Page", "moveWidgetUp");
		this.addParameter("selectedNode", selectedNode);
		this.addParameter("frame", frame);
		return this.executeAction();
	}

	private String executeMoveDown(String selectedNode, int frame, String userName) throws Throwable {
		this.setUserOnSession(userName);
		this.initAction("/do/Page", "moveWidgetDown");
		this.addParameter("selectedNode", selectedNode);
		this.addParameter("frame", frame);
		return this.executeAction();
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
