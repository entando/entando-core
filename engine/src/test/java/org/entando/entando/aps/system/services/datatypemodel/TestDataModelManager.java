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
package org.entando.entando.aps.system.services.datatypemodel;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.services.page.IPage;
import org.entando.entando.aps.system.services.datatype.model.SmallContentType;

public class TestDataModelManager extends BaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testGetContentModel() {
		DataModel model = this._dataModelManager.getContentModel(1);
		assertNotNull(model);
	}

	public void testGetContentModels() {
		List<DataModel> models = this._dataModelManager.getContentModels();
		assertNotNull(models);
		assertEquals(4, models.size());
	}

	public void testGetModelsForContentType() {
		List<DataModel> models = this._dataModelManager.getModelsForContentType("ART");
		assertNotNull(models);
		assertEquals(4, models.size());
	}

	public void testAddDeleteContentModel() throws Throwable {
		List<DataModel> contentModels = this._dataModelManager.getContentModels();
		int size = contentModels.size();
		DataModel dataModel = new DataModel();
		dataModel.setId(99);
		dataModel.setContentType("ART");
		dataModel.setDescription("Descr_Prova");
		dataModel.setContentShape("<h2></h2>");
		try {
			assertNull(this._dataModelManager.getContentModel(99));
			this._dataModelManager.addContentModel(dataModel);
			contentModels = this._dataModelManager.getContentModels();
			assertEquals((size + 1), contentModels.size());
			assertNotNull(this._dataModelManager.getContentModel(3));
			this._dataModelManager.removeContentModel(dataModel);
			contentModels = this._dataModelManager.getContentModels();
			assertEquals(size, contentModels.size());
			assertNull(this._dataModelManager.getContentModel(99));
		} catch (Throwable t) {
			throw t;
		} finally {
			this._dataModelManager.removeContentModel(dataModel);
		}
	}

	public void testUpdateContentModel() throws Throwable {
		List<DataModel> contentModels = _dataModelManager.getContentModels();
		int size = contentModels.size();
		DataModel dataModel = new DataModel();
		dataModel.setId(99);
		dataModel.setContentType("ART");
		dataModel.setDescription("Descr_Prova");
		dataModel.setContentShape("<h2></h2>");
		try {
			assertNull(this._dataModelManager.getContentModel(99));
			this._dataModelManager.addContentModel(dataModel);
			contentModels = this._dataModelManager.getContentModels();
			assertEquals((size + 1), contentModels.size());

			DataModel contentModelNew = new DataModel();
			contentModelNew.setId(dataModel.getId());
			contentModelNew.setContentType("RAH");
			contentModelNew.setDescription("Descr_Prova");
			contentModelNew.setContentShape("<h1></h1>");
			this._dataModelManager.updateContentModel(contentModelNew);
			DataModel extracted = this._dataModelManager.getContentModel(99);
			assertEquals(dataModel.getDescription(), extracted.getDescription());

			this._dataModelManager.removeContentModel(dataModel);
			contentModels = this._dataModelManager.getContentModels();
			assertEquals(size, contentModels.size());
			assertNull(this._dataModelManager.getContentModel(99));
		} catch (Throwable t) {
			throw t;
		} finally {
			this._dataModelManager.removeContentModel(dataModel);
		}
	}

	public void testGetReferencingPages() {
		Map<String, List<IPage>> utilizers = this._dataModelManager.getReferencingPages(2);
		assertNotNull(utilizers);
		assertEquals(1, utilizers.size());
	}

	public void testGetTypeUtilizer() throws Throwable {
		SmallContentType utilizer = this._dataModelManager.getDefaultUtilizer(1);
		assertNotNull(utilizer);
		assertEquals("ART", utilizer.getCode());

		utilizer = this._dataModelManager.getDefaultUtilizer(11);
		assertNotNull(utilizer);
		assertEquals("ART", utilizer.getCode());

		utilizer = this._dataModelManager.getDefaultUtilizer(126);
		assertNotNull(utilizer);
		assertEquals("RAH", utilizer.getCode());
	}

	private void init() throws Exception {
		try {
			this._dataModelManager = (IDataModelManager) this.getService("DataModelManager");
		} catch (Throwable t) {
			throw new Exception(t);
		}
	}

	private IDataModelManager _dataModelManager;

}
