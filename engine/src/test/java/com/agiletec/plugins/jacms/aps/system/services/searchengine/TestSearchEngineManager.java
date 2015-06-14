/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package com.agiletec.plugins.jacms.aps.system.services.searchengine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.attribute.TextAttribute;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * Test del servizio detentore delle operazioni sul motore di ricerca.
 * @author E.Santoboni
 */
public class TestSearchEngineManager extends BaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
    	super.setUp();
        this.init();
    }
    
    public void testSearchContentsId_1() throws Throwable {
    	try {
    		Content content_1 = this.createContent_1();
    		this._searchEngineManager.deleteIndexedEntity(content_1.getId());
    		this._searchEngineManager.addEntityToIndex(content_1);
            
        	Content content_2 = this.createContent_2();
    		this._searchEngineManager.deleteIndexedEntity(content_2.getId());
    		this._searchEngineManager.addEntityToIndex(content_2);
            
        	List<String> contentsId = this._searchEngineManager.searchEntityId("it", "San meravigliosa", null);
			assertNotNull(contentsId);
			assertTrue(contentsId.contains(content_1.getId()));
			contentsId = this._searchEngineManager.searchEntityId("en", "Petersburg wonderful", null);
			assertNotNull(contentsId);
			assertTrue(contentsId.contains(content_1.getId()));
			contentsId = this._searchEngineManager.searchEntityId("en", "meravigliosa", null);
			assertNotNull(contentsId);
			assertFalse(contentsId.contains(content_1.getId()));
        } catch (Throwable t) {
			throw t;
		}
    }
    
    public void testSearchContentsId_2() throws Throwable {
        try {
        	Thread thread = this._searchEngineManager.startReloadContentsReferences();
        	thread.join();
        	
        	Set<String> allowedGroup = new HashSet<String>();
        	List<String> contentsId = this._searchEngineManager.searchEntityId("it", "Corpo coach", allowedGroup);
			assertNotNull(contentsId);
			assertFalse(contentsId.contains("ART104"));
        	
			allowedGroup.add("coach");
			contentsId = this._searchEngineManager.searchEntityId("it", "testo coach", allowedGroup);
			assertNotNull(contentsId);
			assertTrue(contentsId.contains("ART104"));//coach content
			
			contentsId = this._searchEngineManager.searchEntityId("it", "Titolo Evento 4", allowedGroup);
        	assertNotNull(contentsId);
			assertTrue(contentsId.contains("EVN194"));//free content
			
			Set<String> allowedGroup2 = new HashSet<String>();
			allowedGroup2.add(Group.ADMINS_GROUP_NAME);
			contentsId = this._searchEngineManager.searchEntityId("it", "testo coach", allowedGroup2);
			assertNotNull(contentsId);
			assertTrue(contentsId.contains("ART104"));//coach content
			
        } catch (Throwable t) {
			throw t;
		}
    }
    
    public void testSearchContentsId_3() throws Throwable {
    	try {
    		Content content_1 = this.createContent_1();
    		content_1.setMainGroup(Group.ADMINS_GROUP_NAME);
    		this._searchEngineManager.deleteIndexedEntity(content_1.getId());
    		this._searchEngineManager.addEntityToIndex(content_1);
			
			Content content_2 = this.createContent_2();
    		this._searchEngineManager.deleteIndexedEntity(content_2.getId());
    		this._searchEngineManager.addEntityToIndex(content_2);
			
            List<String> allowedGroup = new ArrayList<String>();
            allowedGroup.add(Group.FREE_GROUP_NAME);
        	List<String> contentsId = this._searchEngineManager.searchEntityId("it", "San meravigliosa", allowedGroup);
			assertNotNull(contentsId);
			assertFalse(contentsId.contains(content_1.getId()));
			allowedGroup.add("secondaryGroup");
			contentsId = this._searchEngineManager.searchEntityId("it", "San meravigliosa", allowedGroup);
			assertNotNull(contentsId);
			assertTrue(contentsId.contains(content_1.getId()));
        } catch (Throwable t) {
			throw t;
		}
    }
	
    public void testFacetSearchContentsId_1() throws Throwable {
    	try {
    		Thread thread = this._searchEngineManager.startReloadContentsReferences();
        	thread.join();
			Category general_cat2 = this._categoryManager.getCategory("general_cat2");
            List<Category> categories = new ArrayList<Category>();
            categories.add(general_cat2);
            List<String> allowedGroup = new ArrayList<String>();
            allowedGroup.add(Group.FREE_GROUP_NAME);
        	List<String> contentsId = this._searchEngineManager.searchEntityId(null, categories, allowedGroup);
			assertNotNull(contentsId);
			assertTrue(contentsId.isEmpty());
			allowedGroup.add(Group.ADMINS_GROUP_NAME);
			contentsId = this._searchEngineManager.searchEntityId(null, categories, allowedGroup);
			String[] expected1 = {"ART111", "ART120"};
			this.verify(contentsId, expected1);
			Category general_cat1 = this._categoryManager.getCategory("general_cat1");
			categories.add(general_cat1);
			contentsId = this._searchEngineManager.searchEntityId(null, categories, allowedGroup);
			assertNotNull(contentsId);
			String[] expected2 = {"ART111"};
			this.verify(contentsId, expected2);
        } catch (Throwable t) {
			throw t;
		}
    }
	
	private void verify(List<String> contentsId, String[] array) {
		assertEquals(array.length, contentsId.size());
    	for (int i=0; i<array.length; i++) {
    		assertTrue(contentsId.contains(array[i]));
    	}
	}
	
    private Content createContent_1() {
        Content content = new Content();
        content.setId("100");
        content.setMainGroup(Group.FREE_GROUP_NAME);
        content.addGroup("secondaryGroup");
        TextAttribute text = new TextAttribute();
        text.setName("Articolo");
        text.setType("Text");
        text.setIndexingType(IndexableAttributeInterface.INDEXING_TYPE_TEXT);
        text.setText("San Pietroburgo è una città meravigliosa W3C-WAI", "it");
        text.setText("St. Petersburg is a wonderful city", "en");
        content.addAttribute(text);
		Category category1 = this._categoryManager.getCategory("resCat2");
		Category category2 = this._categoryManager.getCategory("general_cat3");
		content.addCategory(category1);
		content.addCategory(category2);
        return content;
    }
    
    private Content createContent_2() {
        Content content = new Content();
        content.setId("101");
        content.setMainGroup(Group.FREE_GROUP_NAME);
        content.addGroup("thirdGroup");
        TextAttribute text = new TextAttribute();
        text.setName("Articolo");
        text.setType("Text");
        text.setIndexingType(IndexableAttributeInterface.INDEXING_TYPE_TEXT);
        text.setText("Il turismo ha incrementato più del 20 per cento nel 2011-2013, quando la Croazia ha aderito all'Unione europea. Consegienda di questo aumento è una serie di modernizzazione di alloggi di recente costruzione, tra cui circa tre dozzine di ostelli.", "it");
        text.setText("Tourism had shot up more than 20 percent from 2011 to 2013, when Croatia joined the European Union. Accompanying that rise is a raft of modernized and recently built lodgings, including some three dozen hostels.", "en");
        content.addAttribute(text);
		Category category1 = this._categoryManager.getCategory("resCat1");
		Category category2 = this._categoryManager.getCategory("general_cat2");
		content.addCategory(category1);
		content.addCategory(category2);
        return content;
    }
    
    private void init() throws Exception {
        try {
        	this._searchEngineManager = (ICmsSearchEngineManager) this.getService(JacmsSystemConstants.SEARCH_ENGINE_MANAGER);
			this._categoryManager = (ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
        } catch (Exception e) {
			throw e;
        }
    }
    
    private ICmsSearchEngineManager _searchEngineManager = null;
	private ICategoryManager _categoryManager;
	
}