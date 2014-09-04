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

import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.pagemodel.PageModelDOM;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import java.util.List;
import java.util.Map;

/**
 * @author E.Santoboni
 */
public class TestPageModelAction extends AbstractTestPageModelAction {
	
	public void testEditPageModels() throws Throwable {
		String testPageModelCode = "test_pagemodel";
		assertNull(this._pageModelManager.getPageModel(testPageModelCode));
		try {
			String result = this.executeAction("admin", "edit", testPageModelCode);
			assertEquals("pageModelList", result);
			PageModel mockModel = this.createMockPageModel(testPageModelCode);
			this._pageModelManager.addPageModel(mockModel);
			result = this.executeAction("admin", "edit", testPageModelCode);
			assertEquals(Action.SUCCESS, result);
		} catch (Exception e) {
			throw e;
		} finally {
			this._pageModelManager.deletePageModel(testPageModelCode);
			assertNull(this._pageModelManager.getPageModel(testPageModelCode));
		}
	}
	
	public void testValidate_1() throws Throwable {
		String testPageModelCode = "test_pagemodel";
		assertNull(this._pageModelManager.getPageModel(testPageModelCode));
		try {
			this.setUserOnSession("admin");
			this.initAction("/do/PageModel", "save");
			super.addParameter("code", testPageModelCode);
			super.addParameter("strutsAction", ApsAdminSystemConstants.ADD);
			String result = this.executeAction();
			ActionSupport action = super.getAction();
			assertEquals(Action.INPUT, result);
			assertEquals(3, action.getFieldErrors().size());
			assertNotNull(action.getFieldErrors().get("description"));
			assertNotNull(action.getFieldErrors().get("template"));
			assertNotNull(action.getFieldErrors().get("xmlConfiguration"));
		} catch (Exception e) {
			this._pageModelManager.deletePageModel(testPageModelCode);
			assertNull(this._pageModelManager.getPageModel(testPageModelCode));
			throw e;
		}
	}
	
	public void testValidate_2() throws Throwable {
		String testPageModelCode = "internal";
		PageModel model = this._pageModelManager.getPageModel(testPageModelCode);
		assertNotNull(model);
		try {
			this.setUserOnSession("admin");
			this.initAction("/do/PageModel", "save");
			super.addParameter("code", testPageModelCode);
			super.addParameter("description", "Description");
			super.addParameter("strutsAction", ApsAdminSystemConstants.ADD);
			String result = this.executeAction();
			ActionSupport action = super.getAction();
			assertEquals(Action.INPUT, result);
			assertEquals(3, action.getFieldErrors().size());
			assertNotNull(action.getFieldErrors().get("code"));
			assertNotNull(action.getFieldErrors().get("template"));
			assertNotNull(action.getFieldErrors().get("xmlConfiguration"));
		} catch (Exception e) {
			this._pageModelManager.updatePageModel(model);
			throw e;
		}
	}
	
	public void testSave() throws Throwable {
		String testPageModelCode = "test_pagemodel";
		assertNull(this._pageModelManager.getPageModel(testPageModelCode));
		try {
			PageModel mockModel = this.createMockPageModel(testPageModelCode);
			this.setUserOnSession("admin");
			this.initAction("/do/PageModel", "save");
			super.addParameter("code", mockModel.getCode());
			super.addParameter("description", mockModel.getDescription());
			super.addParameter("template", mockModel.getTemplate());
			PageModelDOM dom = new PageModelDOM(mockModel);
			super.addParameter("xmlConfiguration", dom.getXMLDocument());
			super.addParameter("strutsAction", ApsAdminSystemConstants.ADD);
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			assertNotNull(this._pageModelManager.getPageModel(testPageModelCode));
		} catch (Exception e) {
			throw e;
		} finally {
			this._pageModelManager.deletePageModel(testPageModelCode);
			assertNull(this._pageModelManager.getPageModel(testPageModelCode));
		}
	}
	
	public void testTrashPageModels_1() throws Throwable {
		String result = this.executeAction("admin", "trash", null);
		assertEquals("pageModelList", result);
		result = this.executeAction("admin", "trash", "invalidCode");
		assertEquals("pageModelList", result);
		result = this.executeAction("admin", "trash", "home");
		assertEquals("references", result);
		PageModelAction pageModelAction = (PageModelAction) this.getAction();
		Map<String, List<Object>> references = pageModelAction.getReferences();
		assertFalse(references.isEmpty());
		assertEquals(1, references.size());
		assertEquals(11, references.get("PageManagerUtilizers").size());
	}
	
	public void testTrashPageModels_2() throws Throwable {
		String testPageModelCode = "test_pagemodel";
		assertNull(this._pageModelManager.getPageModel(testPageModelCode));
		try {
			PageModel mockModel = this.createMockPageModel(testPageModelCode);
			this._pageModelManager.addPageModel(mockModel);
			String result = this.executeAction("admin", "trash", testPageModelCode);
			assertEquals(Action.SUCCESS, result);
			PageModelAction pageModelAction = (PageModelAction) this.getAction();
			Map<String, List<Object>> references = pageModelAction.getReferences();
			assertTrue(null == references || references.isEmpty());
		} catch (Exception e) {
			throw e;
		} finally {
			this._pageModelManager.deletePageModel(testPageModelCode);
			assertNull(this._pageModelManager.getPageModel(testPageModelCode));
		}
	}
	
	public void testDeletePageModels_1() throws Throwable {
		String result = this.executeAction("admin", "delete", null);
		assertEquals("pageModelList", result);
		result = this.executeAction("admin", "delete", "invalidCode");
		assertEquals("pageModelList", result);
		result = this.executeAction("admin", "delete", "home");
		assertEquals("references", result);
		PageModelAction pageModelAction = (PageModelAction) this.getAction();
		Map<String, List<Object>> references = pageModelAction.getReferences();
		assertFalse(references.isEmpty());
		assertEquals(1, references.size());
		assertEquals(11, references.get("PageManagerUtilizers").size());
	}
	
	public void testDeletePageModels_2() throws Throwable {
		String testPageModelCode = "test_pagemodel";
		assertNull(this._pageModelManager.getPageModel(testPageModelCode));
		try {
			PageModel mockModel = this.createMockPageModel(testPageModelCode);
			this._pageModelManager.addPageModel(mockModel);
			String result = this.executeAction("admin", "delete", testPageModelCode);
			assertEquals(Action.SUCCESS, result);
			PageModelAction pageModelAction = (PageModelAction) this.getAction();
			Map<String, List<Object>> references = pageModelAction.getReferences();
			assertTrue(null == references || references.isEmpty());
		} catch (Exception e) {
			this._pageModelManager.deletePageModel(testPageModelCode);
			throw e;
		} finally {
			assertNull(this._pageModelManager.getPageModel(testPageModelCode));
		}
	}
	
	private String executeAction(String currentUser, String actionName, String modelCode) throws Throwable {
		this.setUserOnSession(currentUser);
		this.initAction("/do/PageModel", actionName);
		super.addParameter("code", modelCode);
		return this.executeAction();
	}
	
}