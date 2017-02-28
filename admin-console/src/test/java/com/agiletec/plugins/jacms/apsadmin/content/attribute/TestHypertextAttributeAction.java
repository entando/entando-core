/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.plugins.jacms.apsadmin.content.attribute;

import java.util.List;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.apsadmin.portal.PageTreeAction;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.ITreeAction;
import com.agiletec.plugins.jacms.apsadmin.content.attribute.action.hypertext.ContentLinkAttributeAction;
import com.agiletec.plugins.jacms.apsadmin.content.util.AbstractBaseTestContentAction;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestHypertextAttributeAction extends AbstractBaseTestContentAction {

	public void testFindContent_1() throws Throwable {
		this.initIntroContentLink("admin", "ART1");//Contenuto del gruppo Free
		ContentLinkAttributeAction action = (ContentLinkAttributeAction) this.getAction();
		List<String> contentIds = action.getContents();
		assertEquals(15, contentIds.size());//Contenuti pubblici liberi o non liberi con free gruppo extra
		assertTrue(contentIds.contains("EVN25"));//Contenuto coach abilitato al gruppo free
		assertTrue(contentIds.contains("ART121"));//Contenuto del gruppo "administrators" abilitato al gruppo free
	}

	public void testFindContent_2() throws Throwable {
		this.initIntroContentLink("admin", "ART120");//Contenuto del gruppo degli amministratori
		ContentLinkAttributeAction action = (ContentLinkAttributeAction) this.getAction();
		List<String> contentIds = action.getContents();
		assertEquals(24, contentIds.size());//Tutti i contenuti pubblici
	}

	public void testFindContent_3() throws Throwable {
		this.initIntroContentLink("editorCustomers", "ART102");//Contenuto del gruppo customers
		ContentLinkAttributeAction action = (ContentLinkAttributeAction) this.getAction();
		List<String> contentIds = action.getContents();
		assertEquals(20, contentIds.size());// Contenuti pubblici liberi, o del gruppo customers o altri con customers gruppo extra
		assertTrue(contentIds.contains("ART122"));//Contenuto del gruppo "administrators" abilitato al gruppo customers
		assertTrue(contentIds.contains("ART121"));//Contenuto del gruppo "administrators" abilitato al gruppo free
		assertTrue(contentIds.contains("EVN25"));//Contenuto del gruppo "coach" abilitato al gruppo free
		assertTrue(contentIds.contains("ART111"));//Contenuto del gruppo "coach" abilitato al gruppo customers
	}

	public void testFindContent_4() throws Throwable {
		this.initIntroContentLink("admin", "EVN25");//Contenuto del gruppo coach
		ContentLinkAttributeAction action = (ContentLinkAttributeAction) this.getAction();
		List<String> contentIds = action.getContents();
		assertEquals(20, contentIds.size());// Contenuti pubblici liberi, o del gruppo coach o altri con coach gruppo extra
		assertTrue(contentIds.contains("ART121"));//Contenuto del gruppo "administrators" abilitato al gruppo coach
		assertTrue(contentIds.contains("ART121"));//Contenuto del gruppo "administrators" abilitato al gruppo free
		assertTrue(contentIds.contains("EVN25"));//Contenuto del gruppo "coach" abilitato al gruppo free
		assertTrue(contentIds.contains("ART111"));//Contenuto del gruppo "coach" abilitato al gruppo customers
	}

	private void initIntroContentLink(String username, String contentId) throws Throwable {
		this.executeEdit(contentId, username);
		String contentOnSessionMarker = super.extractSessionMarker(contentId, ApsAdminSystemConstants.EDIT);
		this.initContentAction("/do/jacms/Content/Hypertext", "entandoIntroContentLink", contentOnSessionMarker);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}

	public void testConfigPageLink_1() throws Throwable {
		this.initIntroPageLink("admin", "ART1");
		ITreeNode root = ((PageTreeAction) this.getAction()).getAllowedTreeRootNode();
		assertNotNull(root);
		assertEquals("homepage", root.getCode());
		assertEquals(3, root.getChildren().length);
	}

	public void testConfigPageLink_2() throws Throwable {
		this.initIntroPageLink("admin", "ART102");
		ITreeNode root = ((PageTreeAction) this.getAction()).getAllowedTreeRootNode();
		assertNotNull(root);
		assertEquals("homepage", root.getCode());
		assertEquals(4, root.getChildren().length);
	}

	public void testOpenPageTree_1() throws Throwable {
		this.openTree("admin", "ART1", "homepage");
		ITreeNode root = ((PageTreeAction) this.getAction()).getAllowedTreeRootNode();
		assertNotNull(root);
		assertEquals("homepage", root.getCode());
		assertEquals(3, root.getChildren().length);
		ITreeNode showableRoot = ((PageTreeAction) this.getAction()).getShowableTree();
		assertEquals("homepage", showableRoot.getCode());
		assertEquals(3, showableRoot.getChildren().length);
	}

	public void testOpenPageTree_2() throws Throwable {
		this.openTree("admin", "ART102", "homepage");
		this.checkTestOpenPageTree_ART102();
	}

	public void testOpenPageTree_3() throws Throwable {
		this.openTree("editorCustomers", "ART102", "homepage");
		this.checkTestOpenPageTree_ART102();
	}

	public void testOpenPageTree_4() throws Throwable {
		this.openTreeOnDemand("editorCustomers", "ART102", "homepage");
		this.checkTestOpenPageTree_ART102();
	}
	
	private void checkTestOpenPageTree_ART102() throws Throwable {
		ITreeNode root = ((PageTreeAction) this.getAction()).getAllowedTreeRootNode();
		assertNotNull(root);
		assertEquals("homepage", root.getCode());
		assertEquals(4, root.getChildren().length);
		ITreeNode showableRoot = ((PageTreeAction) this.getAction()).getShowableTree();
		assertEquals("homepage", showableRoot.getCode());
		assertEquals(4, showableRoot.getChildren().length);
	}
	
	private void initIntroPageLink(String username, String contentId) throws Throwable {
		this.executeEdit(contentId, username);
		String contentOnSessionMarker = super.extractSessionMarker(contentId, ApsAdminSystemConstants.EDIT);
		this.initContentAction("/do/jacms/Content/Hypertext", "entandoPageLink", contentOnSessionMarker);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}

	private void openTree(String username, String contentId, String nodeToOpen) throws Throwable {
		this.executeEdit(contentId, username);
		String contentOnSessionMarker = super.extractSessionMarker(contentId, ApsAdminSystemConstants.EDIT);
		this.initContentAction("/do/jacms/Content/Hypertext", "entandoPageLink", contentOnSessionMarker);
		this.addParameter("treeNodeActionMarkerCode", ITreeAction.ACTION_MARKER_OPEN);
		this.addParameter("targetNode", nodeToOpen);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}

	private void openTreeOnDemand(String username, String contentId, String nodeToOpen) throws Throwable {
		this.executeEdit(contentId, username);
		String contentOnSessionMarker = super.extractSessionMarker(contentId, ApsAdminSystemConstants.EDIT);
		this.initContentAction("/do/jacms/Content/Hypertext", "openCloseTreeNode", contentOnSessionMarker);
		this.addParameter("treeNodeActionMarkerCode", ITreeAction.ACTION_MARKER_OPEN);
		this.addParameter("targetNode", nodeToOpen);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}
	
}