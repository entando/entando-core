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
package com.agiletec.plugins.jacms.aps.system.services.searchengine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.common.entity.model.attribute.TextAttribute;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * Test del servizio detentore 
 * delle operazioni sul motore di ricerca.
 * @author W.Ambu
 */
public class TestSearchEngineManager extends BaseTestCase {
	
	protected void setUp() throws Exception {
    	super.setUp();
        this.init();
    }
    
    public void testSearchContentsId_1() throws Throwable {
    	try {
    		Content content = this.createContent();
    		this._searchEngineManager.deleteIndexedEntity(content.getId());
    		this._searchEngineManager.addEntityToIndex(content);
            
        	List<String> contentsId = this._searchEngineManager.searchEntityId("it", "San meravigliosa", null);
			assertNotNull(contentsId);
			assertTrue(contentsId.contains(content.getId()));
			contentsId = this._searchEngineManager.searchEntityId("en", "Petersburg wonderful", null);
			assertNotNull(contentsId);
			assertTrue(contentsId.contains(content.getId()));
			contentsId = this._searchEngineManager.searchEntityId("en", "meravigliosa", null);
			assertNotNull(contentsId);
			assertFalse(contentsId.contains(content.getId()));
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
    		Content content = this.createContent();
    		content.setMainGroup(Group.ADMINS_GROUP_NAME);
    		this._searchEngineManager.deleteIndexedEntity(content.getId());
    		this._searchEngineManager.addEntityToIndex(content);
    		
            List<String> allowedGroup = new ArrayList<String>();
            allowedGroup.add(Group.FREE_GROUP_NAME);
            
        	List<String> contentsId = this._searchEngineManager.searchEntityId("it", "San meravigliosa", allowedGroup);
			assertNotNull(contentsId);
			assertFalse(contentsId.contains(content.getId()));
			
			allowedGroup.add("secondaryGroup");
			
			contentsId = this._searchEngineManager.searchEntityId("it", "San meravigliosa", allowedGroup);
			assertNotNull(contentsId);
			assertTrue(contentsId.contains(content.getId()));
			
        } catch (Throwable t) {
			throw t;
		}
    }
	
    private Content createContent() {
        Content content = new Content();
        content.setId("100");
        content.setMainGroup(Group.FREE_GROUP_NAME);
        content.addGroup("secondaryGroup");
        TextAttribute text = new TextAttribute();
        text.setName("Articolo");
        text.setType("");
        text.setIndexingType(IndexableAttributeInterface.INDEXING_TYPE_TEXT);
        text.setText("San Pietroburgo è una città meravigliosa W3C-WAI", "it");
        text.setText("St. Petersburg is a wonderful city", "en");
        content.addAttribute(text);
        return content;
    }
    
    private void init() throws Exception {
        try {
        	this._searchEngineManager = (ICmsSearchEngineManager) this.getService(JacmsSystemConstants.SEARCH_ENGINE_MANAGER);
        } catch (Exception e) {
			throw e;
        }
    }
    
    private ICmsSearchEngineManager _searchEngineManager = null;

}