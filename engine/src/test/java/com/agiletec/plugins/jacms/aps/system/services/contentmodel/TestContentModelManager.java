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
package com.agiletec.plugins.jacms.aps.system.services.contentmodel;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;

/**
 * @version 1.1
 * @author W.Ambu - S.Didaci - C.Siddi
 */
public class TestContentModelManager extends BaseTestCase {
	
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void testGetContentModel() {
    	ContentModel model = this._contentModelManager.getContentModel(1);
    	assertNotNull(model);
    }
    
    public void testGetContentModels() {
    	List<ContentModel> models = this._contentModelManager.getContentModels();
    	assertNotNull(models);
    	assertEquals(4, models.size());
    }
    
    public void testGetModelsForContentType() {
    	List<ContentModel> models = this._contentModelManager.getModelsForContentType("ART");
    	assertNotNull(models);
    	assertEquals(4, models.size());
    }
    
    public void testAddDeleteContentModel() throws Throwable {
    	List<ContentModel> contentModels = this._contentModelManager.getContentModels();
    	int size = contentModels.size();
    	ContentModel contentModel = new ContentModel();
    	contentModel.setId(99);
    	contentModel.setContentType("ART");
    	contentModel.setDescription("Descr_Prova");
    	contentModel.setContentShape("<h2></h2>");
    	try {
    		assertNull(this._contentModelManager.getContentModel(99));
    		this._contentModelManager.addContentModel(contentModel);
    		contentModels = this._contentModelManager.getContentModels();
    		assertEquals((size + 1), contentModels.size());
    		assertNotNull(this._contentModelManager.getContentModel(3));
    		this._contentModelManager.removeContentModel(contentModel);
    		contentModels = this._contentModelManager.getContentModels();
    		assertEquals(size, contentModels.size());
    		assertNull(this._contentModelManager.getContentModel(99));
    	} catch (Throwable t) {
			throw t;
		} finally {
			this._contentModelManager.removeContentModel(contentModel);
		}
    }
    
    public void testUpdateContentModel() throws Throwable {
    	List<ContentModel> contentModels = _contentModelManager.getContentModels();
    	int size = contentModels.size();
    	ContentModel contentModel = new ContentModel();
    	contentModel.setId(99);
    	contentModel.setContentType("ART");
    	contentModel.setDescription("Descr_Prova");
    	contentModel.setContentShape("<h2></h2>");
    	try {
    		assertNull(this._contentModelManager.getContentModel(99));
    		this._contentModelManager.addContentModel(contentModel);
    		contentModels = this._contentModelManager.getContentModels();
    		assertEquals((size + 1), contentModels.size());
    		
    		ContentModel contentModelNew = new ContentModel();
			contentModelNew.setId(contentModel.getId());
			contentModelNew.setContentType("RAH");
	    	contentModelNew.setDescription("Descr_Prova");
	    	contentModelNew.setContentShape("<h1></h1>");
	    	this._contentModelManager.updateContentModel(contentModelNew);
    		ContentModel extracted = this._contentModelManager.getContentModel(99);
    		assertEquals(contentModel.getDescription(), extracted.getDescription());
    		
    		this._contentModelManager.removeContentModel(contentModel);
    		contentModels = this._contentModelManager.getContentModels();
    		assertEquals(size, contentModels.size());
    		assertNull(this._contentModelManager.getContentModel(99));
    	} catch (Throwable t) {
			throw t;
		} finally {
			this._contentModelManager.removeContentModel(contentModel);
		}
    }
    
    public void testGetReferencingPages() {
    	Map<String, List<IPage>> utilizers = this._contentModelManager.getReferencingPages(2);
    	assertNotNull(utilizers);
    	assertEquals(1, utilizers.size());
    }
    
    public void testGetTypeUtilizer() throws Throwable {
    	SmallContentType utilizer = this._contentModelManager.getDefaultUtilizer(1);
    	assertNotNull(utilizer);
    	assertEquals("ART", utilizer.getCode());
    	
    	utilizer = this._contentModelManager.getDefaultUtilizer(11);
    	assertNotNull(utilizer);
    	assertEquals("ART", utilizer.getCode());
    	
    	utilizer = this._contentModelManager.getDefaultUtilizer(126);
    	assertNotNull(utilizer);
    	assertEquals("RAH", utilizer.getCode());
    }
    
    private void init() throws Exception {
    	try {
    		this._contentModelManager = (IContentModelManager) this.getService(JacmsSystemConstants.CONTENT_MODEL_MANAGER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }
    
    private IContentModelManager _contentModelManager;
    
}