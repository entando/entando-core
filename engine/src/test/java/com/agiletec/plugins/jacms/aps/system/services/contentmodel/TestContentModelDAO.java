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
package com.agiletec.plugins.jacms.aps.system.services.contentmodel;

import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import com.agiletec.aps.BaseTestCase;

/**
 * @version 1.1
 * @author M.Morini - C.Siddi
 */
public class TestContentModelDAO extends BaseTestCase {
	
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
    public void testLoadContentModels() throws Throwable {  
    	Map<Long, ContentModel> models = null;
        try {
        	models = this._contentModelDAO.loadContentModels();
        } catch (Throwable t) {
			throw t;
		}
        String value = null;
        boolean contains = false;
		for (Iterator<ContentModel> iter = models.values().iterator(); iter.hasNext();) {
			ContentModel contentModel = iter.next();
			value = contentModel.getContentType();
			if (value.equals("ART")) {
				contains = true;
			}
		}        
        assertEquals(contains, true);
    }
    
    public void testAddUpdateContentModel() throws Throwable {
		ContentModel contentModel = new ContentModel();
		contentModel.setId(99);
    	contentModel.setContentType("ART");
    	contentModel.setDescription("Descr_Prova");
    	contentModel.setContentShape("<h2></h2>");
    	Map<Long, ContentModel> models = this._contentModelDAO.loadContentModels();
    	int initSize = models.size();
    	try {
			this._contentModelDAO.addContentModel(contentModel);
        	models = this._contentModelDAO.loadContentModels();
        	assertEquals(initSize + 1, models.size());
        	ContentModel extracted = models.get(new Long(99));
        	assertNotNull(extracted);
        	assertEquals(extracted.getContentType(), "ART");
			assertEquals(extracted.getDescription(), "Descr_Prova");
			assertEquals(extracted.getContentShape(), "<h2></h2>");
			this.updateContentModel(contentModel);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._contentModelDAO.deleteContentModel(contentModel);
			models = this._contentModelDAO.loadContentModels();
        	assertEquals(initSize, models.size());
		}
	}
    
    private void updateContentModel(ContentModel modelToUpdate) throws Throwable {
    	modelToUpdate.setContentType("RAH");
    	modelToUpdate.setDescription("Nuova_Descr_Prova");
    	modelToUpdate.setContentShape("<h1></h1>");
		try {
			this._contentModelDAO.updateContentModel(modelToUpdate);
			Map<Long, ContentModel> models = this._contentModelDAO.loadContentModels();
			ContentModel extracted = models.get(new Long(99));
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
			DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
			_contentModelDAO = new ContentModelDAO();
			((ContentModelDAO)_contentModelDAO).setDataSource(dataSource);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
    
	private IContentModelDAO _contentModelDAO;
	
}
