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
package org.entando.entando.apsadmin.dataobject.model;

import java.util.List;
import java.util.Map;

import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.opensymphony.xwork2.Action;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModel;
import org.entando.entando.aps.system.services.dataobjectmodel.IDataObjectModelManager;

/**
 * @author E.Santoboni
 */
public class TestDataObjectModelAction extends ApsAdminBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testNewModel() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/dataobject/model", "new");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}

	public void testEdit() throws Throwable {
		long modelId = 1;
		this.setUserOnSession("admin");
		this.initAction("/do/dataobject/model", "edit");
		DataObjectModelAction action = (DataObjectModelAction) this.getAction();
		this.addParameter("modelId", Long.valueOf(modelId).toString());
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		assertEquals(ApsAdminSystemConstants.EDIT, action.getStrutsAction());
		DataObjectModel currentModel = _dataObjectModelManager.getDataObjectModel(modelId);
		assertEquals(currentModel.getId(), new Long(action.getModelId()).longValue());
		assertEquals(currentModel.getShape(), action.getContentShape());
		assertEquals(currentModel.getDataType(), action.getContentType());
		assertEquals(currentModel.getDescription(), action.getDescription());
		assertEquals(currentModel.getStylesheet(), action.getStylesheet());
	}

	public void testSaveWithErrors_1() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/dataobject/model", "save");
		addParameter("strutsAction", new Integer(ApsAdminSystemConstants.ADD).toString());
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertNotNull(fieldErrors);
		assertEquals(4, fieldErrors.size());
	}

	public void testSaveWithErrors_2() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/dataobject/model", "save");
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

		this.initAction("/do/dataobject/model", "save");
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
			this.initAction("/do/dataobject/model", "save");
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
			DataObjectModel model = this._dataObjectModelManager.getDataObjectModel(negativeModelId);
			if (null != model) {
				this._dataObjectModelManager.removeDataObjectModel(model);
			}
		}
	}

	public void testAddNewModel() throws Throwable {
		List<DataObjectModel> eventModels = this._dataObjectModelManager.getModelsForDataObjectType("EVN");
		assertEquals(0, eventModels.size());
		long modelIdToAdd = 99;
		try {
			this.setUserOnSession("admin");
			this.initAction("/do/dataobject/model", "save");
			addParameter("contentType", "EVN");
			addParameter("strutsAction", new Integer(ApsAdminSystemConstants.ADD).toString());
			addParameter("description", "contentModel description");
			addParameter("contentShape", "contentShape field value\r\n");
			addParameter("modelId", String.valueOf(modelIdToAdd));
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);

			eventModels = this._dataObjectModelManager.getModelsForDataObjectType("EVN");
			assertEquals(1, eventModels.size());

			DataObjectModel model = eventModels.get(0);
			assertEquals("contentShape field value\r\n", model.getShape());
		} catch (Throwable t) {
			throw t;
		} finally {
			DataObjectModel model = this._dataObjectModelManager.getDataObjectModel(modelIdToAdd);
			if (null != model) {
				this._dataObjectModelManager.removeDataObjectModel(model);
			}
			eventModels = this._dataObjectModelManager.getModelsForDataObjectType("EVN");
			assertEquals(0, eventModels.size());
		}
	}

	public void testUpdateModel() throws Throwable {
		List<DataObjectModel> eventModels = this._dataObjectModelManager.getModelsForDataObjectType("EVN");
		assertEquals(0, eventModels.size());
		long modelId = 99;
		this.addModelForTest(modelId, "EVN");
		eventModels = this._dataObjectModelManager.getModelsForDataObjectType("EVN");
		assertEquals(1, eventModels.size());
		DataObjectModel model = (DataObjectModel) eventModels.get(0);
		try {
			this.setUserOnSession("admin");

			this.initAction("/do/dataobject/model", "save");
			this.addParameter("strutsAction", new Integer(ApsAdminSystemConstants.EDIT).toString());
			this.addParameter("modelId", new Long(modelId).toString());
			this.addParameter("description", "updated description");
			this.addParameter("contentType", model.getDataType());
			this.addParameter("contentShape", model.getShape());
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);

			eventModels = this._dataObjectModelManager.getModelsForDataObjectType("EVN");
			assertEquals(1, eventModels.size());

			model = this._dataObjectModelManager.getDataObjectModel(modelId);
			assertEquals("updated description", model.getDescription());
		} catch (Throwable t) {
			throw t;
		} finally {
			model = this._dataObjectModelManager.getDataObjectModel(modelId);
			if (null != model) {
				this._dataObjectModelManager.removeDataObjectModel(model);
			}
			eventModels = this._dataObjectModelManager.getModelsForDataObjectType("EVN");
			assertEquals(0, eventModels.size());
		}
	}

	public void testTrashModel() throws Throwable {
		long modelId = 1;
		this.setUserOnSession("admin");

		this.initAction("/do/dataobject/model", "trash");
		this.addParameter("modelId", String.valueOf(modelId));
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}

	public void testTrashReferencedModel() throws Throwable {
		long modelId = 2;
		this.setUserOnSession("admin");
		this.initAction("/do/dataobject/model", "trash");
		this.addParameter("modelId", String.valueOf(modelId));
		String result = this.executeAction();
		assertEquals("references", result);
	}

	public void testDeleteModel() throws Throwable {
		List<DataObjectModel> eventModels = this._dataObjectModelManager.getModelsForDataObjectType("EVN");
		assertEquals(0, eventModels.size());
		long modelId = 99;
		this.addModelForTest(modelId, "EVN");
		eventModels = this._dataObjectModelManager.getModelsForDataObjectType("EVN");
		assertEquals(1, eventModels.size());
		DataObjectModel model = (DataObjectModel) eventModels.get(0);
		try {
			this.setUserOnSession("admin");
			this.initAction("/do/dataobject/model", "delete");
			this.addParameter("modelId", String.valueOf(modelId));
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			model = this._dataObjectModelManager.getDataObjectModel(modelId);
			assertNull(model);

			eventModels = this._dataObjectModelManager.getModelsForDataObjectType("EVN");
			assertEquals(0, eventModels.size());
		} catch (Throwable t) {
			model = this._dataObjectModelManager.getDataObjectModel(modelId);
			if (null != model) {
				this._dataObjectModelManager.removeDataObjectModel(model);
			}
			eventModels = this._dataObjectModelManager.getModelsForDataObjectType("EVN");
			assertEquals(0, eventModels.size());
			throw t;
		}
	}

	public void testDeleteReferencedModel() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/dataobject/model", "trash");
		this.addParameter("modelId", "2");
		String result = this.executeAction();
		assertEquals("references", result);
		DataObjectModelAction action = (DataObjectModelAction) this.getAction();
		assertEquals(1, action.getReferencingPages().size());
	}

	private void addModelForTest(long id, String contentType) throws Throwable {
		DataObjectModel model = new DataObjectModel();
		model.setId(id);
		model.setDataType(contentType);
		model.setDescription("contentModel description");
		model.setShape("contentShape field value");
		this._dataObjectModelManager.addDataObjectModel(model);
	}

	private void init() {
		this._dataObjectModelManager = (IDataObjectModelManager) this.getService("DataObjectModelManager");
	}

	private IDataObjectModelManager _dataObjectModelManager;

}
