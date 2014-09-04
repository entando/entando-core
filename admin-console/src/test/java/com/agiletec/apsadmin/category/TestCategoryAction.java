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
package com.agiletec.apsadmin.category;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni - G.Cocco
 */
public class TestCategoryAction extends ApsAdminBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testNewCategory() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/Category", "new");
		String result = this.executeAction();
		assertEquals("categoryTree", result);
		CategoryAction action = (CategoryAction) this.getAction();
		assertEquals(1, action.getActionErrors().size());
		
		this.setUserOnSession("admin");
		this.initAction("/do/Category", "new");
		this.addParameter("selectedNode", this._categoryManager.getRoot().getCode());
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		action = (CategoryAction) this.getAction();
		assertEquals(this._categoryManager.getRoot().getCode(), action.getParentCategoryCode());
		assertEquals(0, action.getTitles().size());
		assertEquals(ApsAdminSystemConstants.ADD, action.getStrutsAction());
	}
	
	public void testEditCategory() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/Category", "edit");
		String result = this.executeAction();
		assertEquals("categoryTree", result);
		CategoryAction action = (CategoryAction) this.getAction();
		assertEquals(1, action.getActionErrors().size());
		
		this.setUserOnSession("admin");
		this.initAction("/do/Category", "edit");
		this.addParameter("selectedNode", "evento");
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		action = (CategoryAction) this.getAction();
		assertEquals(this._categoryManager.getRoot().getCode(), action.getParentCategoryCode());
		assertEquals(2, action.getTitles().size());
		assertEquals(ApsAdminSystemConstants.EDIT, action.getStrutsAction());
	}
	
	public void testViewTree() throws Throwable {
		this.setUserOnSession("pageManagerCoach");
		this.initAction("/do/Category", "viewTree");
		String result = this.executeAction();
		assertEquals("userNotAllowed", result);
		
		this.setUserOnSession("admin");
		this.initAction("/do/Category", "viewTree");
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		CategoryAction action = (CategoryAction) this.getAction();
		ITreeNode root = action.getTreeRootNode();
		assertEquals(this._categoryManager.getRoot().getCode(), root.getCode());
	}
	
	public void testValidateAddCategory_1() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("parentCategoryCode", this._categoryManager.getRoot().getCode());
		params.put("strutsAction", "1");
		params.put("categoryCode", "");
		params.put("langit", "");
		params.put("langen", "");
		String result = this.executeSaveCategory("admin", params);
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(2, fieldErrors.size());
		assertEquals(1, fieldErrors.get("langit").size());
		assertEquals(1, fieldErrors.get("langen").size());
	}
	
	public void testValidateAddCategory_2() throws Throwable {
		String categoryCode = "veryLongCategoryCode_veryLongCategoryCode";
		assertNull(this._categoryManager.getCategory(categoryCode));
		Map<String, String> params = new HashMap<String, String>();
		params.put("parentCategoryCode", this._categoryManager.getRoot().getCode());
		params.put("strutsAction", "1");
		params.put("categoryCode", categoryCode);//long category code
		params.put("langit", "Titolo in Italiano");
		params.put("langen", "English Title");
		String result = this.executeSaveCategory("admin", params);
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertEquals(1, fieldErrors.get("categoryCode").size());
	}
	
	public void testValidateAddCategory_3() throws Throwable {
		assertNotNull(this._categoryManager.getCategory("evento"));
		Map<String, String> params = new HashMap<String, String>();
		params.put("parentCategoryCode", this._categoryManager.getRoot().getCode());
		params.put("strutsAction", "1");
		params.put("categoryCode", "evento");//duplicate Code
		params.put("langit", "Titolo categoria");
		params.put("langen", "English Title");
		String result = this.executeSaveCategory("admin", params);
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertEquals(1, fieldErrors.get("categoryCode").size());
	}
	
	public void testValidateAddCategory_4() throws Throwable {
		String categoryCode = "cat_temp2";
		assertNull(this._categoryManager.getCategory(categoryCode));
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("parentCategoryCode", this._categoryManager.getRoot().getCode());
			params.put("strutsAction", "1");
			params.put("categoryCode", categoryCode);
			params.put("langit", "Titolo categoria seconda");
			params.put("langen", "");//empty English title field
			String result = this.executeSaveCategory("admin", params);
			assertEquals(Action.INPUT, result);
			Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
			assertEquals(1, fieldErrors.size());
			assertEquals(1, fieldErrors.get("langen").size());
		} catch (Throwable t) {
			this._categoryManager.deleteCategory(categoryCode);
			assertNotNull(this._categoryManager.getCategory(categoryCode));
			throw t;
		}
	}

	public void testAddCategory_1() throws Throwable {
		String categoryCode = "cat_temp";
		assertNull(this._categoryManager.getCategory(categoryCode));
		try {
			String result = this.saveNewCategory("admin", categoryCode);
			assertEquals(Action.SUCCESS, result);
			Category category = this._categoryManager.getCategory(categoryCode);
			assertNotNull(category);
			assertEquals("Titolo categoria In Italiano", category.getTitles().getProperty("it"));
			assertEquals(this._categoryManager.getRoot().getCode(), category.getParent().getCode());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._categoryManager.deleteCategory(categoryCode);
			assertNull(this._categoryManager.getCategory(categoryCode));
		}
	}

	public void testAddCategory_2() throws Throwable {
		String expectedCategoryCode = "titolo_italiano";
		assertNull(this._categoryManager.getCategory(expectedCategoryCode));
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("parentCategoryCode", this._categoryManager.getRoot().getCode());
			params.put("strutsAction", "1");
			params.put("langit", "Titolo Italiano");
			params.put("langen", "English Title");
			String result = this.executeSaveCategory("admin", params);
			assertEquals(Action.SUCCESS, result);
			Category category = this._categoryManager.getCategory(expectedCategoryCode);
			assertNotNull(category);
			assertEquals("Titolo Italiano", category.getTitles().getProperty("it"));
			assertEquals(this._categoryManager.getRoot().getCode(), category.getParent().getCode());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._categoryManager.deleteCategory(expectedCategoryCode);
			assertNull(this._categoryManager.getCategory(expectedCategoryCode));
		}
	}
	
	public void testTrashCategory() throws Throwable {
		String categoryCode = "cat_temp";
		assertNull(this._categoryManager.getCategory(categoryCode));
		try {
			String result = this.saveNewCategory("admin", categoryCode);
			assertEquals(Action.SUCCESS, result);
			Category category = this._categoryManager.getCategory(categoryCode);
			assertNotNull(category);
			
			this.initAction("/do/Category", "trash");
			this.addParameter("selectedNode", categoryCode);
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			category = this._categoryManager.getCategory(categoryCode);
			assertNotNull(category);
		} catch (Throwable t) {
			throw t;
		} finally {			
			this._categoryManager.deleteCategory(categoryCode);
			assertNull(this._categoryManager.getCategory(categoryCode));
		}
	}
	
	public void testDeleteCategory_1() throws Throwable {
		String categoryCode = "cat_temp";
		assertNull(this._categoryManager.getCategory(categoryCode));
		try {
			String result = this.saveNewCategory("admin", categoryCode);
			assertEquals(Action.SUCCESS, result);
			Category category = this._categoryManager.getCategory(categoryCode);
			assertNotNull(category);
			
			this.initAction("/do/Category", "trash");
			this.addParameter("selectedNode", categoryCode);
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			category = this._categoryManager.getCategory(categoryCode);
			assertNotNull(category);
			Map<String, Object> references = ((CategoryAction) this.getAction()).getReferences();
			assertNull(references);
			
			this.initAction("/do/Category", "delete");
			this.addParameter("selectedNode", categoryCode);
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			category = this._categoryManager.getCategory(categoryCode);
			assertNull(category);
			references = ((CategoryAction) this.getAction()).getReferences();
			assertNull(references);
		} catch (Throwable t) {
			this._categoryManager.deleteCategory(categoryCode);
			assertNull(this._categoryManager.getCategory(categoryCode));
			throw t;
		}
	}
	
	public void testDeleteCategory_2() throws Throwable {
		this.setUserOnSession("admin");
		String categoryCode = "general_cat1";
		assertNotNull(this._categoryManager.getCategory(categoryCode));
		try {
			this.initAction("/do/Category", "trash");
			this.addParameter("selectedNode", categoryCode);
			String result = this.executeAction();
			assertEquals("references", result);
			Category category = this._categoryManager.getCategory(categoryCode);
			assertNotNull(category);
			Map<String, Object> references = ((CategoryAction) this.getAction()).getReferences();
			assertNotNull(references);
			assertEquals(1, references.size());
			
			this.initAction("/do/Category", "delete");
			this.addParameter("selectedNode", categoryCode);
			result = this.executeAction();
			assertEquals("references", result);
			category = this._categoryManager.getCategory(categoryCode);
			assertNotNull(category);
			references = ((CategoryAction) this.getAction()).getReferences();
			assertNotNull(references);
			assertEquals(1, references.size());
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testCategoryDetails_1() throws Throwable {
		String categoryCode = "cat_temp";
		assertNull(this._categoryManager.getCategory(categoryCode));
		try {
			String result = this.executeCategoryDetail("admin", categoryCode);
			assertEquals("categoryTree", result);
			Collection<String> actionErrors = this.getAction().getActionErrors();
			assertEquals(1, actionErrors.size());
			
			result = this.saveNewCategory("admin", categoryCode);
			assertEquals(Action.SUCCESS, result);
			Category category = this._categoryManager.getCategory(categoryCode);
			assertNotNull(category);
			
			result = this.executeCategoryDetail("admin", categoryCode);
			assertEquals(Action.SUCCESS, result);
			CategoryAction action = (CategoryAction) this.getAction();
			assertEquals(categoryCode, action.getCategoryCode());
			assertEquals("Titolo categoria In Italiano", action.getTitles().get("it"));
			assertEquals("Titolo categoria In Inglese", action.getTitles().get("en"));
			assertNull(action.getReferences());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._categoryManager.deleteCategory(categoryCode);
			assertNull(this._categoryManager.getCategory(categoryCode));
		}
	}
	
	public void testCategoryDetails_2() throws Throwable {
		String categoryCode = "general_cat1";
		Category category = this._categoryManager.getCategory(categoryCode);
		assertNotNull(category);
		try {
			String result = this.executeCategoryDetail("admin", categoryCode);
			assertEquals(Action.SUCCESS, result);
			CategoryAction action = (CategoryAction) this.getAction();
			assertEquals(category.getCode(), action.getCategoryCode());
			assertEquals(category.getTitle("it"), action.getTitles().get("it"));
			assertEquals(category.getTitle("en"), action.getTitles().get("en"));
			assertNotNull(action.getReferences());
			assertEquals(1, action.getReferences().size());
		} catch (Throwable t) {
			throw t;
		}
	}
	
	private String saveNewCategory(String username, String categoryCode) throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("parentCategoryCode", this._categoryManager.getRoot().getCode());
		params.put("strutsAction", "1");
		params.put("categoryCode", categoryCode);
		params.put("langit", "Titolo categoria In Italiano");
		params.put("langen", "Titolo categoria In Inglese");
		String result = this.executeSaveCategory(username, params);
		return result;
	}
	
	private String executeSaveCategory(String username, Map<String, String> params) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/Category", "save");
		this.addParameters(params);
		return this.executeAction();
	}
	
	private String executeCategoryDetail(String username, String categoryCode) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/Category", "detail");
		this.addParameter("selectedNode", categoryCode);
		return this.executeAction();
	}
	
	private void init() throws Exception {
		this._categoryManager = (ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
	}
	
	private ICategoryManager _categoryManager;
	
}