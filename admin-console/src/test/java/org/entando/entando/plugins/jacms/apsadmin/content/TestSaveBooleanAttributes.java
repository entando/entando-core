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
package org.entando.entando.plugins.jacms.apsadmin.content;

import com.agiletec.aps.system.common.entity.model.attribute.BooleanAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.CheckBoxAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ThreeStateAttribute;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestSaveBooleanAttributes extends AbstractTestContentAttribute {
	
	public void testSaveBooleanAttribute() throws Throwable {
		try {
			String contentOnSessionMarker = this.executeCreateNewContent();
			Content content = this.getContentOnEdit(contentOnSessionMarker);
			BooleanAttribute attribute = (BooleanAttribute) content.getAttribute("Boolean");
			assertNull(attribute.getBooleanValue());
			assertFalse(attribute.getValue());
			
			this.initSaveContentAction(contentOnSessionMarker);
			contentOnSessionMarker = this.executeSaveAndReloadContent(contentOnSessionMarker);
			content = this.getContentOnEdit(contentOnSessionMarker);
			attribute = (BooleanAttribute) content.getAttribute("Boolean");
			assertNotNull(attribute.getBooleanValue());
			assertFalse(attribute.getValue());
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter("Boolean:Boolean", "false");
			contentOnSessionMarker = this.executeSaveAndReloadContent(contentOnSessionMarker);
			content = this.getContentOnEdit(contentOnSessionMarker);
			attribute = (BooleanAttribute) content.getAttribute("Boolean");
			assertNotNull(attribute.getBooleanValue());
			assertFalse(attribute.getValue());
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter("Boolean:Boolean", "true");
			contentOnSessionMarker = this.executeSaveAndReloadContent(contentOnSessionMarker);
			content = this.getContentOnEdit(contentOnSessionMarker);
			attribute = (BooleanAttribute) content.getAttribute("Boolean");
			assertNotNull(attribute.getBooleanValue());
			assertTrue(attribute.getValue());
			
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteTestContent();
		}
	}
	
	public void testSaveCheckBoxAttribute() throws Throwable {
		try {
			String contentOnSessionMarker = this.executeCreateNewContent();
			Content content = this.getContentOnEdit(contentOnSessionMarker);
			CheckBoxAttribute attribute = (CheckBoxAttribute) content.getAttribute("CheckBox");
			assertNull(attribute.getBooleanValue());
			assertFalse(attribute.getValue());
			
			this.initSaveContentAction(contentOnSessionMarker);
			contentOnSessionMarker = this.executeSaveAndReloadContent(contentOnSessionMarker);
			content = this.getContentOnEdit(contentOnSessionMarker);
			attribute = (CheckBoxAttribute) content.getAttribute("CheckBox");
			assertNull(attribute.getBooleanValue());
			assertFalse(attribute.getValue());
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter("CheckBox:CheckBox", "false");
			contentOnSessionMarker = this.executeSaveAndReloadContent(contentOnSessionMarker);
			content = this.getContentOnEdit(contentOnSessionMarker);
			attribute = (CheckBoxAttribute) content.getAttribute("CheckBox");
			assertNull(attribute.getBooleanValue());
			assertFalse(attribute.getValue());
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter("CheckBox:CheckBox", "true");
			contentOnSessionMarker = this.executeSaveAndReloadContent(contentOnSessionMarker);
			content = this.getContentOnEdit(contentOnSessionMarker);
			attribute = (CheckBoxAttribute) content.getAttribute("CheckBox");
			assertNotNull(attribute.getBooleanValue());
			assertTrue(attribute.getValue());
			
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteTestContent();
		}
	}
	
	public void testSaveThreeStateAttribute() throws Throwable {
		try {
			String contentOnSessionMarker = this.executeCreateNewContent();
			Content content = this.getContentOnEdit(contentOnSessionMarker);
			ThreeStateAttribute attribute = (ThreeStateAttribute) content.getAttribute("ThreeState");
			assertNull(attribute.getBooleanValue());
			assertFalse(attribute.getValue());
			
			this.initSaveContentAction(contentOnSessionMarker);
			contentOnSessionMarker = this.executeSaveAndReloadContent(contentOnSessionMarker);
			content = this.getContentOnEdit(contentOnSessionMarker);
			attribute = (ThreeStateAttribute) content.getAttribute("ThreeState");
			assertNull(attribute.getBooleanValue());
			assertFalse(attribute.getValue());
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter("ThreeState:ThreeState", "false");
			contentOnSessionMarker = this.executeSaveAndReloadContent(contentOnSessionMarker);
			content = this.getContentOnEdit(contentOnSessionMarker);
			attribute = (ThreeStateAttribute) content.getAttribute("ThreeState");
			assertNotNull(attribute.getBooleanValue());
			assertFalse(attribute.getValue());
			
			this.initSaveContentAction(contentOnSessionMarker);
			this.addParameter("ThreeState:ThreeState", "true");
			contentOnSessionMarker = this.executeSaveAndReloadContent(contentOnSessionMarker);
			content = this.getContentOnEdit(contentOnSessionMarker);
			attribute = (ThreeStateAttribute) content.getAttribute("ThreeState");
			assertNotNull(attribute.getBooleanValue());
			assertTrue(attribute.getValue());
			
		} catch (Throwable t) {
			throw t;
		} finally {
			this.deleteTestContent();
		}
	}
	
	private String executeSaveAndReloadContent(String contentOnSessionMarker) throws Throwable {
		Content contentOnSession = super.getContentOnEdit(contentOnSessionMarker);
		this.addParameter("Monotext:MARKER", "MARKER");
		this.executeAction(Action.SUCCESS);
		String id = contentOnSession.getId();
		String result = super.executeEdit(id, "admin");
		assertEquals(Action.SUCCESS, result);
		return this.extractSessionMarker(id, ApsAdminSystemConstants.EDIT);
	}
	
}