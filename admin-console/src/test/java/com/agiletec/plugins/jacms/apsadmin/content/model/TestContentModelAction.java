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
package com.agiletec.plugins.jacms.apsadmin.content.model;

import java.util.List;
import java.util.Map;

import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestContentModelAction  extends ApsAdminBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testNewModel() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jacms/ContentModel", "new");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}
	
	public void testEdit() throws Throwable {
		long modelId = 1;
		this.setUserOnSession("admin");
		this.initAction("/do/jacms/ContentModel", "edit");
		ContentModelAction action = (ContentModelAction)this.getAction();
		this.addParameter("modelId", Long.valueOf(modelId).toString());
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		assertEquals(ApsAdminSystemConstants.EDIT, action.getStrutsAction());
		ContentModel currentModel = _contentModelManager.getContentModel(modelId);
		assertEquals(currentModel.getId(), new Long(action.getModelId()).longValue());
		assertEquals(currentModel.getContentShape(), action.getContentShape());
		assertEquals(currentModel.getContentType(), action.getContentType());
		assertEquals(currentModel.getDescription(), action.getDescription());
		assertEquals(currentModel.getStylesheet(), action.getStylesheet());
	}
	
	public void testSaveWithErrors_1() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jacms/ContentModel", "save");
		addParameter("strutsAction", new Integer(ApsAdminSystemConstants.ADD).toString());
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertNotNull (fieldErrors);
		assertEquals(4, fieldErrors.size());
	}
	
	public void testSaveWithErrors_2() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jacms/ContentModel", "save");
		addParameter("contentType", "EVN");
		addParameter("strutsAction", new Integer(ApsAdminSystemConstants.ADD).toString());
		addParameter("description", "contentModel description");
		addParameter("contentShape", "contentShape field value");
		addParameter("modelId", "2");
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();		
		assertEquals(1, fieldErrors.size());
		assertEquals(1, fieldErrors.get("modelId").size());//duplicate modelId
		
		this.initAction("/do/jacms/ContentModel", "save");
		addParameter("contentType", "EVN");
		addParameter("strutsAction", new Integer(ApsAdminSystemConstants.ADD).toString());
		addParameter("description", "contentModel description");
		addParameter("contentShape", "contentShape field value");
		addParameter("modelId", "khtds");
		result = this.executeAction();
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors();		
		assertEquals(1, fieldErrors.size());
		assertEquals(2, fieldErrors.get("modelId").size());//wrong format
	}
	
	public void testSaveWithErrors_3() throws Throwable {
		String veryLongDescription = "Very but very very very long description (upper than 50 characters) for invoke description's length validation";
		int negativeModelId = 0;
		try {
			this.setUserOnSession("admin");
			this.initAction("/do/jacms/ContentModel", "save");
			addParameter("contentType", "EVN");
			addParameter("strutsAction", new Integer(ApsAdminSystemConstants.ADD).toString());
			addParameter("description", veryLongDescription);
			addParameter("contentShape", "contentShape field value");
			addParameter("modelId", String.valueOf(negativeModelId));
			String result = this.executeAction();
			assertEquals(Action.INPUT, result);
			Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
			assertEquals(2, fieldErrors.size());
			assertEquals(1, fieldErrors.get("modelId").size());
			assertEquals(1, fieldErrors.get("description").size());
		} catch (Throwable t) {
			throw t;
		} finally {
			ContentModel model = this._contentModelManager.getContentModel(negativeModelId);
			if (null != model) {
				this._contentModelManager.removeContentModel(model);
			}
		}
	}
	
	public void testAddNewModel() throws Throwable {
		List<ContentModel> eventModels = this._contentModelManager.getModelsForContentType("EVN");
		assertEquals(0, eventModels.size());
		long modelIdToAdd = 99;
		try {
			this.setUserOnSession("admin");
			this.initAction("/do/jacms/ContentModel", "save");
			addParameter("contentType", "EVN");
			addParameter("strutsAction", new Integer(ApsAdminSystemConstants.ADD).toString());
			addParameter("description", "contentModel description");
			addParameter("contentShape", "contentShape field value\r\n");
			addParameter("modelId", String.valueOf(modelIdToAdd));
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);

			eventModels = this._contentModelManager.getModelsForContentType("EVN");
			assertEquals(1, eventModels.size());
			
			ContentModel model = eventModels.get(0);
			assertEquals("contentShape field value\r\n", model.getContentShape());
		} catch (Throwable t) {
			throw t;
		} finally {
			ContentModel model = this._contentModelManager.getContentModel(modelIdToAdd);
			if (null != model) {
				this._contentModelManager.removeContentModel(model);
			}
			eventModels = this._contentModelManager.getModelsForContentType("EVN");
			assertEquals(0, eventModels.size());
		}
	}
	
	public void testUpdateModel() throws Throwable {
		List<ContentModel> eventModels = this._contentModelManager.getModelsForContentType("EVN");
		assertEquals(0, eventModels.size());
		long modelId = 99;
		this.addModelForTest(modelId, "EVN");
		eventModels = this._contentModelManager.getModelsForContentType("EVN");
		assertEquals(1, eventModels.size());
		ContentModel model = (ContentModel) eventModels.get(0);
		try {
			this.setUserOnSession("admin");

			this.initAction("/do/jacms/ContentModel", "save");
			this.addParameter("strutsAction", new Integer(ApsAdminSystemConstants.EDIT).toString());
			this.addParameter("modelId", new Long(modelId).toString());
			this.addParameter("description", "updated description");
			this.addParameter("contentType", model.getContentType());
			this.addParameter("contentShape", model.getContentShape());
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);

			eventModels = this._contentModelManager.getModelsForContentType("EVN");
			assertEquals(1, eventModels.size());

			model = this._contentModelManager.getContentModel(modelId);
			assertEquals("updated description", model.getDescription());
		} catch (Throwable t) {
			throw t;
		} finally {
			model = this._contentModelManager.getContentModel(modelId);
			if (null != model) {
				this._contentModelManager.removeContentModel(model);
			}
			eventModels = this._contentModelManager.getModelsForContentType("EVN");
			assertEquals(0, eventModels.size());
		}
	}
	
	public void testTrashModel() throws Throwable {
		long modelId = 1;
		this.setUserOnSession("admin");

		this.initAction("/do/jacms/ContentModel", "trash");
		this.addParameter("modelId", String.valueOf(modelId));
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}
	
	public void testTrashReferencedModel() throws Throwable {
		long modelId = 2;
		this.setUserOnSession("admin");
		this.initAction("/do/jacms/ContentModel", "trash");
		this.addParameter("modelId", String.valueOf(modelId));
		String result = this.executeAction();
		assertEquals("references", result);
	}
	
	public void testDeleteModel() throws Throwable {
		List<ContentModel> eventModels = this._contentModelManager.getModelsForContentType("EVN");
		assertEquals(0, eventModels.size());
		long modelId = 99;
		this.addModelForTest(modelId, "EVN");
		eventModels = this._contentModelManager.getModelsForContentType("EVN");
		assertEquals(1, eventModels.size());
		ContentModel model = (ContentModel) eventModels.get(0);
		try {
			this.setUserOnSession("admin");

			this.initAction("/do/jacms/ContentModel", "delete");
			this.addParameter("modelId", String.valueOf(modelId));
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			model = this._contentModelManager.getContentModel(modelId);
			assertNull(model);

			eventModels = this._contentModelManager.getModelsForContentType("EVN");
			assertEquals(0, eventModels.size());
		} catch (Throwable t) {
			model = this._contentModelManager.getContentModel(modelId);
			if (null != model) {
				this._contentModelManager.removeContentModel(model);
			}
			eventModels = this._contentModelManager.getModelsForContentType("EVN");
			assertEquals(0, eventModels.size());
			throw t;
		}
	}
	
	public void testDeleteReferencedModel() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jacms/ContentModel", "trash");
		this.addParameter("modelId", "2");
		String result = this.executeAction();
		assertEquals("references", result);
		ContentModelAction action = (ContentModelAction) this.getAction();
		assertEquals(1, action.getReferencingPages().size());
	}
	
	private void addModelForTest(long id, String contentType) throws Throwable {
		ContentModel model = new ContentModel();
		model.setId(id);
		model.setContentType(contentType);
		model.setDescription("contentModel description");
		model.setContentShape("contentShape field value");
		this._contentModelManager.addContentModel(model);
	}
	
	private void init() {
		this._contentModelManager = (IContentModelManager) this.getService(JacmsSystemConstants.CONTENT_MODEL_MANAGER);
	}

	private IContentModelManager _contentModelManager;

}
