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
package com.agiletec.aps.system.services.i18n;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.util.ApsProperties;

/**
 * @version 1.0
 * @author W.Ambu
 */
public class I18nManagerIntegrationTest extends BaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
    public void testGetLabels() throws Throwable {
		String label = _i18nManager.getLabel("PAGE_TITLE", "it");
		assertNotNull(label);
		assertEquals(label, "titolo pagina");
		label = _i18nManager.getLabel("not-exists", "it");
		assertNull(label);
	}
    
    public void testAddDeleteLabels() throws Throwable {
    	String key = "TEST_KEY";
    	ApsProperties labels = new ApsProperties();
    	labels.put("it", "Testo Italiano");
    	labels.put("en", "English Text");
    	try {
    		assertNull(_i18nManager.getLabelGroups().get(key));
    		_i18nManager.addLabelGroup(key, labels);
    		
    		ApsProperties extracted = (ApsProperties) _i18nManager.getLabelGroups().get(key);
    		assertNotNull(extracted);
    		assertEquals("Testo Italiano", extracted.getProperty("it"));
    		assertEquals("English Text", extracted.getProperty("en"));
    	} catch (Throwable t) {
			throw t;
		} finally {
			_i18nManager.deleteLabelGroup(key);
			assertNull(_i18nManager.getLabelGroups().get(key));
		}
    }
    
    public void testUpdateLabels() throws Throwable {
    	String key = "TEST_KEY";
    	ApsProperties labels = new ApsProperties();
    	labels.put("it", "Testo Italiano");
    	labels.put("en", "English Text");
    	try {
    		assertNull(_i18nManager.getLabelGroups().get(key));
    		_i18nManager.addLabelGroup(key, labels);
    		
    		ApsProperties toUpdate = (ApsProperties) _i18nManager.getLabelGroups().get(key);
    		assertNotNull(toUpdate);
    		toUpdate.put("it", "Testo Italiano Modificato");
    		toUpdate.put("en", "Modified English Text");
    		_i18nManager.updateLabelGroup(key, toUpdate);
    		
    		ApsProperties extracted = (ApsProperties) _i18nManager.getLabelGroups().get(key);
    		assertNotNull(extracted);
    		assertEquals("Testo Italiano Modificato", extracted.getProperty("it"));
    		assertEquals("Modified English Text", extracted.getProperty("en"));
    		
    	} catch (Throwable t) {
			throw t;
		} finally {
			_i18nManager.deleteLabelGroup(key);
			assertNull(_i18nManager.getLabelGroups().get(key));
		}
    }
    
    public void testGetLabelsKey() throws Throwable {
		assertEquals(9, _i18nManager.getLabelGroups().size());
    	
    	assertEquals(0, _i18nManager.searchLabelsKey("*", false, false, null).size());
		assertEquals(9, _i18nManager.searchLabelsKey("", false, false, null).size());
		assertEquals(3, _i18nManager.searchLabelsKey("pag", false, false, null).size());
    	
		assertEquals(4, _i18nManager.searchLabelsKey("age", true, false, null).size());
		
		assertEquals(3, _i18nManager.searchLabelsKey("pag", false, true, "it").size());
    }
    
    private void init() throws Exception {
    	try {
    		_i18nManager = (II18nManager) this.getService(SystemConstants.I18N_MANAGER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }
    
    private II18nManager _i18nManager = null;
    
}