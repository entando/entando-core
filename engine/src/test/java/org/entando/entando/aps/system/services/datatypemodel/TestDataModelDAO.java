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

import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import com.agiletec.aps.BaseTestCase;

public class TestDataModelDAO extends BaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testLoadContentModels() throws Throwable {
		Map<Long, DataModel> models = null;
		try {
			models = this._dataModelDAO.loadDataModels();
		} catch (Throwable t) {
			throw t;
		}
		String value = null;
		boolean contains = false;
		for (Iterator<DataModel> iter = models.values().iterator(); iter.hasNext();) {
			DataModel dataModel = iter.next();
			value = dataModel.getContentType();
			if (value.equals("ART")) {
				contains = true;
			}
		}
		assertEquals(contains, true);
	}

	public void testAddUpdateContentModel() throws Throwable {
		DataModel contentModel = new DataModel();
		contentModel.setId(99);
		contentModel.setContentType("ART");
		contentModel.setDescription("Descr_Prova");
		contentModel.setContentShape("<h2></h2>");
		Map<Long, DataModel> models = this._dataModelDAO.loadDataModels();
		int initSize = models.size();
		try {
			this._dataModelDAO.addDataModel(contentModel);
			models = this._dataModelDAO.loadDataModels();
			assertEquals(initSize + 1, models.size());
			DataModel extracted = models.get(new Long(99));
			assertNotNull(extracted);
			assertEquals(extracted.getContentType(), "ART");
			assertEquals(extracted.getDescription(), "Descr_Prova");
			assertEquals(extracted.getContentShape(), "<h2></h2>");
			this.updateContentModel(contentModel);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._dataModelDAO.deleteDataModel(contentModel);
			models = this._dataModelDAO.loadDataModels();
			assertEquals(initSize, models.size());
		}
	}

	private void updateContentModel(DataModel modelToUpdate) throws Throwable {
		modelToUpdate.setContentType("RAH");
		modelToUpdate.setDescription("Nuova_Descr_Prova");
		modelToUpdate.setContentShape("<h1></h1>");
		try {
			this._dataModelDAO.updateDataModel(modelToUpdate);
			Map<Long, DataModel> models = this._dataModelDAO.loadDataModels();
			DataModel extracted = models.get(new Long(99));
			assertNotNull(extracted);
			assertEquals(extracted.getContentType(), "RAH");
			assertEquals(extracted.getDescription(), "Nuova_Descr_Prova");
			assertEquals(extracted.getContentShape(), "<h1></h1>");
		} catch (Throwable t) {
			throw t;
		}
	}

	private void init() throws Exception {
		try {
			DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
			_dataModelDAO = new DataModelDAO();
			((DataModelDAO) _dataModelDAO).setDataSource(dataSource);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}

	private IDataModelDAO _dataModelDAO;

}
