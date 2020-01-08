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
package com.agiletec.aps.system.services.category;

import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.util.ApsProperties;
import java.util.Arrays;

/**
 * Tests for Category Manager
 *
 * @author E.Santoboni
 */
public class TestCategoryManager extends BaseTestCase {

    @Override
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
            assertEquals(cat.getDefaultFullTitle(this._categoryManager), extractedCat.getDefaultFullTitle(this._categoryManager));
            assertEquals(cat.getParentCode(), cat.getParentCode());

            Category extractedParent = this._categoryManager.getCategory(cat.getParentCode());
            assertEquals(4, extractedParent.getChildrenCodes().length);
            assertTrue(Arrays.asList(extractedParent.getChildrenCodes()).containsAll(Arrays.asList("general_cat1", cat.getCode(), "general_cat2", "general_cat3")));
        } catch (Throwable t) {
            throw t;
        } finally {
            _categoryManager.deleteCategory(cat.getCode());
            assertNull(this._categoryManager.getCategory(cat.getCode()));
            Category extractedParent = this._categoryManager.getCategory(cat.getParentCode());
            assertEquals(3, extractedParent.getChildrenCodes().length);
            assertTrue(Arrays.asList(extractedParent.getChildrenCodes()).containsAll(Arrays.asList("general_cat1", "general_cat2", "general_cat3")));
        }
        ((IManager) this._categoryManager).refresh();
        Category extractedParent = this._categoryManager.getCategory(cat.getParentCode());
        assertEquals(3, extractedParent.getChildrenCodes().length);
        assertTrue(Arrays.asList(extractedParent.getChildrenCodes()).containsAll(Arrays.asList("general_cat1", "general_cat2", "general_cat3")));
    }

    public void testUpdateRemoveCategory() throws Throwable {
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
        assertTrue(categories.size() > 0);
    }
    
    public void testMove() throws Throwable {
        Category category1 = this.createCategory("st_move_1", "cat1", "AAAA Title start");
        Category category2 = this.createCategory("st_move_2", "cat1", "BBBB Title start");
        Category category3 = this.createCategory("st_move_3", "cat1", "CCCC Title start");
        Category category4 = this.createCategory("st_move_4", "cat1", "DDDD Title start");
        
        Category dt_category1 = this.createCategory("dt_move_1", "evento", "AAAA Title destination");
        Category dt_category2 = this.createCategory("dt_move_2", "evento", "BBBB Title destination");
        Category dt_category3 = this.createCategory("dt_move_3", "evento", "CCCC Title destination");
        Category dt_category4 = this.createCategory("dt_move_4", "evento", "DDDD Title destination");
        try {
            this._categoryManager.addCategory(category1);
            this._categoryManager.addCategory(category2);
            this._categoryManager.addCategory(category3);
            this._categoryManager.addCategory(category4);
            this._categoryManager.addCategory(dt_category1);
            this._categoryManager.addCategory(dt_category2);
            this._categoryManager.addCategory(dt_category3);
            this._categoryManager.addCategory(dt_category4);
            
            this._categoryManager.moveCategory("st_move_2", "evento");
            this.checkParent("cat1", Arrays.asList("st_move_1", "st_move_3", "st_move_4"));
            this.checkParent("evento", Arrays.asList("dt_move_1", "dt_move_2", "dt_move_3", "dt_move_4", "st_move_2"));
            ((IManager) this._categoryManager).refresh();
            this.checkParent("cat1", Arrays.asList("st_move_1", "st_move_3", "st_move_4"));
            this.checkParent("evento", Arrays.asList("dt_move_1", "dt_move_2", "dt_move_3", "dt_move_4", "st_move_2"));
            
            this._categoryManager.moveCategory("st_move_1", "evento");
            this.checkParent("cat1", Arrays.asList("st_move_3", "st_move_4"));
            this.checkParent("evento", Arrays.asList("dt_move_1", "dt_move_2", "dt_move_3", "dt_move_4", "st_move_2", "st_move_1"));
            ((IManager) this._categoryManager).refresh();
            this.checkParent("cat1", Arrays.asList("st_move_3", "st_move_4"));
            this.checkParent("evento", Arrays.asList("dt_move_1", "dt_move_2", "dt_move_3", "dt_move_4", "st_move_2", "st_move_1"));
            
            this._categoryManager.moveCategory("dt_move_3", "cat1");
            this.checkParent("cat1", Arrays.asList("st_move_3", "st_move_4", "dt_move_3"));
            this.checkParent("evento", Arrays.asList("dt_move_1", "dt_move_2", "dt_move_4", "st_move_2", "st_move_1"));
            ((IManager) this._categoryManager).refresh();
            this.checkParent("cat1", Arrays.asList("st_move_3", "st_move_4", "dt_move_3"));
            this.checkParent("evento", Arrays.asList("dt_move_1", "dt_move_2", "dt_move_4", "st_move_2", "st_move_1"));
            
        } catch (Exception e) {
            throw e;
        } finally {
            for (int i = 0; i < 4; i++) {
                this._categoryManager.deleteCategory("st_move_" + (i+1));
                this._categoryManager.deleteCategory("dt_move_" + (i+1));
            }
            for (int i = 0; i < 4; i++) {
                assertNull(this._categoryManager.getCategory("st_move_" + (i+1)));
                assertNull(this._categoryManager.getCategory("dt_move_" + (i+1)));
            }
            Category extractedParent = _categoryManager.getCategory("cat1");
            assertEquals(0, extractedParent.getChildrenCodes().length);
        }
        ((IManager) this._categoryManager).refresh();
        Category extractedParent = this._categoryManager.getCategory("cat1");
        assertEquals(0, extractedParent.getChildrenCodes().length);
    }
    
    private void checkParent(String code, List<String> childrenCodes) throws Throwable {
        Category extractedParent = this._categoryManager.getCategory(code);
        assertEquals(childrenCodes.size(), extractedParent.getChildrenCodes().length);
        assertTrue(childrenCodes.containsAll(Arrays.asList(extractedParent.getChildrenCodes())));
    }
    
    private Category createCategory() {
        return this.createCategory("tempCode", "general", "Titolo");
    }
    
    private Category createCategory(String code, String parentCode, String titlePrefix) {
        Category cat = new Category();
        cat.setDefaultLang("it");
        cat.setCode(code);
        cat.setParentCode(parentCode);
        ApsProperties titles = new ApsProperties();
        titles.put("it", titlePrefix + " in Italiano");
        titles.put("en", titlePrefix + " in Inglese");
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
