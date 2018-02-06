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
package org.entando.entando.aps.system.services.dataobjectmodel;

import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import com.agiletec.aps.BaseTestCase;

public class TestDataObjectModelDAO extends BaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testLoadContentModels() throws Throwable {
		Map<Long, DataObjectModel> models = null;
		try {
			models = this._dataModelDAO.loadDataModels();
		} catch (Throwable t) {
			throw t;
		}
		String value = null;
		boolean contains = false;
		for (Iterator<DataObjectModel> iter = models.values().iterator(); iter.hasNext();) {
			DataObjectModel dataModel = iter.next();
			value = dataModel.getDataType();
			if (value.equals("ART")) {
				contains = true;
			}
		}
		assertEquals(contains, true);
	}

	public void testAddUpdateContentModel() throws Throwable {
		DataObjectModel contentModel = new DataObjectModel();
		contentModel.setId(99);
		contentModel.setDataType("ART");
		contentModel.setDescription("Descr_Prova");
		contentModel.setShape("<h2></h2>");
		Map<Long, DataObjectModel> models = this._dataModelDAO.loadDataModels();
		int initSize = models.size();
		try {
			this._dataModelDAO.addDataModel(contentModel);
			models = this._dataModelDAO.loadDataModels();
			assertEquals(initSize + 1, models.size());
			DataObjectModel extracted = models.get(new Long(99));
			assertNotNull(extracted);
			assertEquals(extracted.getDataType(), "ART");
			assertEquals(extracted.getDescription(), "Descr_Prova");
			assertEquals(extracted.getShape(), "<h2></h2>");
			this.updateContentModel(contentModel);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._dataModelDAO.deleteDataModel(contentModel);
			models = this._dataModelDAO.loadDataModels();
			assertEquals(initSize, models.size());
		}
	}

	private void updateContentModel(DataObjectModel modelToUpdate) throws Throwable {
		modelToUpdate.setDataType("RAH");
		modelToUpdate.setDescription("Nuova_Descr_Prova");
		modelToUpdate.setShape("<h1></h1>");
		try {
			this._dataModelDAO.updateDataModel(modelToUpdate);
			Map<Long, DataObjectModel> models = this._dataModelDAO.loadDataModels();
			DataObjectModel extracted = models.get(new Long(99));
			assertNotNull(extracted);
			assertEquals(extracted.getDataType(), "RAH");
			assertEquals(extracted.getDescription(), "Nuova_Descr_Prova");
			assertEquals(extracted.getShape(), "<h1></h1>");
		} catch (Throwable t) {
			throw t;
		}
	}

	private void init() throws Exception {
		try {
			DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
			_dataModelDAO = new DataObjectModelDAO();
			((DataObjectModelDAO) _dataModelDAO).setDataSource(dataSource);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}

	private IDataObjectModelDAO _dataModelDAO;

}
