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

import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModel;
import org.entando.entando.aps.system.services.dataobjectmodel.IDataObjectModelManager;

/**
 * @author E.Santoboni
 */
public class TestDataObjectModelFinderAction extends ApsAdminBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testList() throws Throwable {
		this.initAction("/do/dataobject/model", "list");
		this.setUserOnSession("admin");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		DataObjectModelFinderAction contentModelFinderAction = (DataObjectModelFinderAction) this.getAction();
		List<DataObjectModel> contentModels = contentModelFinderAction.getContentModels();
		assertEquals(4, contentModels.size());
	}

	public void testSearch_1() throws Throwable {
		String result = this.executeSearch("ART");
		assertEquals(Action.SUCCESS, result);
		DataObjectModelFinderAction contentModelFinderAction = (DataObjectModelFinderAction) this.getAction();
		List<DataObjectModel> contentModels = contentModelFinderAction.getContentModels();
		assertEquals(4, contentModels.size());

		result = this.executeSearch("CNG");
		assertEquals(Action.SUCCESS, result);
		contentModelFinderAction = (DataObjectModelFinderAction) this.getAction();
		contentModels = contentModelFinderAction.getContentModels();
		assertEquals(0, contentModels.size());
	}

	public void testSearch_2() throws Throwable {
		DataObjectModel contentModel = this.createContentModel(MODEL_ID, "EVN");
		this._dataObjectModelManager.addDataObjectModel(contentModel);

		String result = this.executeSearch("");
		assertEquals(Action.SUCCESS, result);
		DataObjectModelFinderAction contentModelFinderAction = (DataObjectModelFinderAction) this.getAction();
		List<DataObjectModel> contentModels = contentModelFinderAction.getContentModels();
		assertEquals(5, contentModels.size());

		result = this.executeSearch("EVN");
		assertEquals(Action.SUCCESS, result);
		contentModelFinderAction = (DataObjectModelFinderAction) this.getAction();
		contentModels = contentModelFinderAction.getContentModels();
		assertEquals(1, contentModels.size());
	}

	private String executeSearch(String contentType) throws Throwable {
		this.initAction("/do/dataobject/model", "search");
		this.setUserOnSession("admin");
		this.addParameter("contentType", contentType);
		return this.executeAction();
	}

	private DataObjectModel createContentModel(int id, String contentType) {
		DataObjectModel model = new DataObjectModel();
		model.setId(id);
		model.setDataType(contentType);
		model.setDescription("Event test Model");
		model.setShape("test shape");
		return model;
	}

	private void init() {
		this._dataObjectModelManager = (IDataObjectModelManager) this.getService("DataObjectModelManager");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		DataObjectModel model = this._dataObjectModelManager.getDataObjectModel(MODEL_ID);
		if (null != model) {
			this._dataObjectModelManager.removeDataObjectModel(model);
		}
	}

	private IDataObjectModelManager _dataObjectModelManager;
	private static final int MODEL_ID = 99;

}
