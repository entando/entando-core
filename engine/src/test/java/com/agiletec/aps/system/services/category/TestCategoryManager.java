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
package com.agiletec.aps.system.services.category;

import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.util.ApsProperties;

/**
 * Test del servizio gestore categorie.
 * @version 1.0
 * @author E.Santoboni
 */
public class TestCategoryManager extends BaseTestCase {
	
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
    public void testGetCategory() {
    	Category category = _categoryManager.getCategory("cat1");
    	assertNotNull(category);
    	assertEquals(category.getTitle(), "Animali");
    }
    
    public void testAddCategory() throws Throwable {
    	Category cat = this.createCategory();
    	try {
    		assertNull(this._categoryManager.getCategory(cat.getCode()));
			_categoryManager.addCategory(cat);
			Category extractedCat = _categoryManager.getCategory(cat.getCode());
			assertNotNull(extractedCat);
			assertEquals(cat.getTitle(), extractedCat.getTitle());
			assertEquals(cat.getDefaultFullTitle(), extractedCat.getDefaultFullTitle());
			assertEquals(cat.getParentCode(), cat.getParentCode());
		} catch (Throwable t) {
			throw t;
		} finally {
			_categoryManager.deleteCategory(cat.getCode());
			assertNull(this._categoryManager.getCategory(cat.getCode()));
		}
    }
    
    public void testUpdateRemoveCategory() throws Throwable  {
    	Category cat = this.createCategory();
    	try {
    		assertNull(this._categoryManager.getCategory(cat.getCode()));
			_categoryManager.addCategory(cat);
			Category extractedCat = _categoryManager.getCategory(cat.getCode());
			assertNotNull(extractedCat);
			
			String newTitle = "Nuovo titolo dell categoria temporanea";
			extractedCat.getTitles().put("it", newTitle);
	    	_categoryManager.updateCategory(extractedCat);
	    	extractedCat = _categoryManager.getCategory(cat.getCode());
	    	assertEquals(extractedCat.getTitle(), newTitle);
	    	assertEquals(extractedCat.getParentCode(), cat.getParentCode());
		} catch (Throwable t) {
			throw t;
		} finally {
			_categoryManager.deleteCategory(cat.getCode());
			assertNull(this._categoryManager.getCategory(cat.getCode()));
		}
    }
    
    public void testGetCategories() {
    	List<Category> categories = _categoryManager.getCategoriesList();
    	assertNotNull(categories);
    	assertTrue(categories.size()>0);
    }
    
    private Category createCategory() {
    	Category cat = new Category();
    	cat.setDefaultLang("it");
    	cat.setCode("tempCode");
    	Category parent = _categoryManager.getCategory("cat1");
    	cat.setParent(parent);
    	cat.setParentCode(parent.getCode());
    	ApsProperties titles = new ApsProperties();
    	titles.put("it", "Titolo in Italiano");
    	titles.put("en", "Titolo in Inglese");
    	cat.setTitles(titles);
    	return cat;
    }
    
    private void init() throws Exception {
    	try {
    		_categoryManager = (ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
    	} catch (Throwable t) {
    		throw new Exception(t);
        }
    }
    
    private ICategoryManager _categoryManager = null;
    
}
