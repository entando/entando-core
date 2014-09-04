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
package com.agiletec.plugins.jacms.apsadmin.content.attribute;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.apsadmin.portal.IPageTreeAction;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.ITreeAction;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.LinkAttribute;
import com.agiletec.plugins.jacms.apsadmin.content.AbstractContentAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.action.link.helper.ILinkAttributeActionHelper;
import com.agiletec.plugins.jacms.apsadmin.content.util.AbstractBaseTestContentAction;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestPageLinkAction extends AbstractBaseTestContentAction {

	public void testConfigPageLink_1() throws Throwable {
		String contentOnSessionMarker = this.initJoinLinkTest("admin", "ART1", "VediAnche", "it");
		this.initContentAction("/do/jacms/Content/Link", "configPageLink", contentOnSessionMarker);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		ITreeNode root = ((IPageTreeAction) this.getAction()).getAllowedTreeRootNode();
		assertNotNull(root);
		assertEquals("homepage", root.getCode());
		assertEquals(3, root.getChildren().length);
		ITreeNode showableRoot = ((IPageTreeAction) this.getAction()).getShowableTree();
		assertEquals("homepage", showableRoot.getCode());
		assertEquals(0, showableRoot.getChildren().length);
	}

	public void testConfigPageLink_2() throws Throwable {
		String contentOnSessionMarker = this.initJoinLinkTest("admin", "ART102", "VediAnche", "it");
		this.initContentAction("/do/jacms/Content/Link", "configPageLink", contentOnSessionMarker);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		ITreeNode root = ((IPageTreeAction) this.getAction()).getAllowedTreeRootNode();
		assertNotNull(root);
		assertEquals("homepage", root.getCode());
		assertEquals(4, root.getChildren().length);
		ITreeNode showableRoot = ((IPageTreeAction) this.getAction()).getShowableTree();
		assertEquals("homepage", showableRoot.getCode());
		assertEquals(0, showableRoot.getChildren().length);
	}

	public void testOpenPageNode_1() throws Throwable {
		String contentOnSessionMarker = this.initJoinLinkTest("admin", "ART102", "VediAnche", "it");
		this.initContentAction("/do/jacms/Content/Link", "openCloseTreeOnConfigPageLink", contentOnSessionMarker);
		this.addParameter("treeNodeActionMarkerCode", ITreeAction.ACTION_MARKER_OPEN);
		this.addParameter("targetNode", "homepage");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		this.checkTestOpenPageTree_ART102();
	}

	public void testOpenPageNode_2() throws Throwable {
		String contentOnSessionMarker = this.initJoinLinkTest("editorCustomers", "ART102", "VediAnche", "it");
		this.initContentAction("/do/jacms/Content/Link", "openCloseTreeOnConfigPageLink", contentOnSessionMarker);
		this.addParameter("treeNodeActionMarkerCode", ITreeAction.ACTION_MARKER_OPEN);
		this.addParameter("targetNode", "homepage");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		this.checkTestOpenPageTree_ART102();
	}

	private void checkTestOpenPageTree_ART102() throws Throwable {
		ITreeNode root = ((IPageTreeAction) this.getAction()).getAllowedTreeRootNode();
		assertNotNull(root);
		assertEquals("homepage", root.getCode());
		assertEquals(4, root.getChildren().length);
		ITreeNode showableRoot = ((IPageTreeAction) this.getAction()).getShowableTree();
		assertEquals("homepage", showableRoot.getCode());
		assertEquals(4, showableRoot.getChildren().length);
	}

	public void testFailureJoinPageLink_1() throws Throwable {
		String contentOnSessionMarker = this.initJoinLinkTest("admin", "ART1", "VediAnche", "it");
		this.initContentAction("/do/jacms/Content/Link", "joinPageLink", contentOnSessionMarker);
		this.addParameter("linkType", String.valueOf(SymbolicLink.PAGE_TYPE));
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		List<String> typeFieldErrors = fieldErrors.get("selectedNode");
		assertEquals(1, typeFieldErrors.size());
	}

	public void testFailureJoinPageLink_2() throws Throwable {
		String contentOnSessionMarker = this.initJoinLinkTest("admin", "ART1", "VediAnche", "it");
		this.initContentAction("/do/jacms/Content/Link", "joinPageLink", contentOnSessionMarker);
		this.addParameter("linkType", String.valueOf(SymbolicLink.PAGE_TYPE));
		this.addParameter("selectedNode", "");
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		List<String> typeFieldErrors = fieldErrors.get("selectedNode");
		assertEquals(1, typeFieldErrors.size());
	}

	public void testFailureJoinPageLink_3() throws Throwable {
		String contentOnSessionMarker = this.initJoinLinkTest("admin", "ART1", "VediAnche", "it");
		this.initContentAction("/do/jacms/Content/Link", "joinPageLink", contentOnSessionMarker);
		this.addParameter("linkType", String.valueOf(SymbolicLink.PAGE_TYPE));
		this.addParameter("selectedNode", "wrongPageCode");
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		List<String> typeFieldErrors = fieldErrors.get("selectedNode");
		assertEquals(1, typeFieldErrors.size());
	}

	public void testJoinPageLink_1() throws Throwable {
		String contentOnSessionMarker = this.initJoinLinkTest("admin", "ART1", "VediAnche", "it");
		this.initContentAction("/do/jacms/Content/Link", "joinPageLink", contentOnSessionMarker);
		this.addParameter("linkType", String.valueOf(SymbolicLink.PAGE_TYPE));
		this.addParameter("selectedNode", "pagina_11");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		Content content = this.getContentOnEdit(contentOnSessionMarker);
		LinkAttribute attribute = (LinkAttribute) content.getAttribute("VediAnche");
		SymbolicLink symbolicLink = attribute.getSymbolicLink();
		assertNotNull(symbolicLink);
		assertEquals("pagina_11", symbolicLink.getPageDest());
	}

	private String initJoinLinkTest(String username, String contentId, String simpleLinkAttributeName, String langCode) throws Throwable {
		Content content = this.getContentManager().loadContent(contentId, false);
		this.executeEdit(contentId, username);
		String contentOnSessionMarker = AbstractContentAction.buildContentOnSessionMarker(content, ApsAdminSystemConstants.EDIT);
		//iniziazione parametri sessione
		HttpSession session = this.getRequest().getSession();
		session.setAttribute(ILinkAttributeActionHelper.ATTRIBUTE_NAME_SESSION_PARAM, simpleLinkAttributeName);
		session.setAttribute(ILinkAttributeActionHelper.LINK_LANG_CODE_SESSION_PARAM, langCode);
		return contentOnSessionMarker;
	}

}