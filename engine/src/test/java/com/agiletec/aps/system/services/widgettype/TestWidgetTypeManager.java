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
package com.agiletec.aps.system.services.widgettype;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.services.mock.MockWidgetTypeDAO;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsProperties;

/**
 * @author M.Diana - E.Santoboni
 */
public class TestWidgetTypeManager extends BaseTestCase {
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	public void testGetWidgetTypes() throws ApsSystemException {
		List<WidgetType> list = _widgetTypeManager.getWidgetTypes();
		Iterator<WidgetType> iter = list.iterator();
		Map<String, String> widgetTypes = new HashMap<String, String>();
		while (iter.hasNext()) {
			WidgetType widgetType = iter.next();
			widgetTypes.put(widgetType.getCode(), widgetType.getTitles().getProperty("it"));
		}
		boolean containsKey = widgetTypes.containsKey("content_viewer_list");
		boolean containsValue = widgetTypes.containsValue("Contenuti - Pubblica una Lista di Contenuti");
		assertTrue(containsKey);
		assertTrue(containsValue);
		containsKey = widgetTypes.containsKey("content_viewer");
		containsValue = widgetTypes.containsValue("Contenuti - Pubblica un Contenuto");
		assertTrue(containsKey);
		assertTrue(containsValue);		
	}
    
    public void testGetWidgetType_1() throws ApsSystemException {
    	WidgetType widgetType = _widgetTypeManager.getWidgetType("content_viewer");
		assertEquals("content_viewer", widgetType.getCode());
		assertEquals("Contenuti - Pubblica un Contenuto", widgetType.getTitles().get("it"));
		assertTrue(widgetType.isLocked());
		assertFalse(widgetType.isLogic());
		assertFalse(widgetType.isUserType());
		assertNull(widgetType.getParentType());
		assertNull(widgetType.getConfig());
		String action = widgetType.getAction();
		assertEquals(action, "viewerConfig");
		List<WidgetTypeParameter> list = widgetType.getTypeParameters();
		Iterator<WidgetTypeParameter> iter = list.iterator();
		Map<String, String> parameters = new HashMap<String, String>();
		while (iter.hasNext()) {
			WidgetTypeParameter parameter = (WidgetTypeParameter) iter.next();
			parameters.put(parameter.getName(), parameter.getDescr());
		}
		boolean containsKey = parameters.containsKey("contentId");
		boolean containsValue = parameters.containsValue("Identificativo del Contenuto");
		assertEquals(containsKey, true);
		assertEquals(containsValue, true);
		containsKey = parameters.containsKey("modelId");
		containsValue = parameters.containsValue("Identificativo del Modello di Contenuto");
		assertEquals(containsKey, true);
		assertEquals(containsValue, true);				
	}
	
    public void testGetWidgetType_2() throws ApsSystemException {
    	WidgetType widgetType = _widgetTypeManager.getWidgetType("90_events");
		assertEquals("90_events", widgetType.getCode());
		assertEquals("Lista contenuti anni '90", widgetType.getTitles().get("it"));
		assertFalse(widgetType.isLocked());
		assertTrue(widgetType.isLogic());
		assertTrue(widgetType.isUserType());
		assertNull(widgetType.getAction());
		assertNull(widgetType.getTypeParameters());
		assertNotNull(widgetType.getParentType());
		assertEquals("content_viewer_list", widgetType.getParentType().getCode());
		assertNotNull(widgetType.getConfig());
		String contentTypeParam = widgetType.getConfig().getProperty("contentType");
		assertEquals("EVN", contentTypeParam);
		String filtersParam = widgetType.getConfig().getProperty("filters");
		assertTrue(filtersParam.contains("start=01/01/1990"));			
	}
    
    public void testFailureDeleteWidgetType_1() throws Throwable {
    	String widgetTypeCode = "content_viewer";
    	assertNotNull(this._widgetTypeManager.getWidgetType(widgetTypeCode));
    	try {
    		this._widgetTypeManager.deleteWidgetType(widgetTypeCode);
		} catch (Throwable t) {
			
		}
		assertNotNull(this._widgetTypeManager.getWidgetType(widgetTypeCode));
    }
    
    public void testFailureDeleteWidgetType_2() throws Throwable {
    	String widgetTypeCode = "test_widgetType";
    	assertNull(this._widgetTypeManager.getWidgetType(widgetTypeCode));
    	try {
			WidgetType type = this.createNewWidgetType(widgetTypeCode);
			type.setLocked(true);
			this._widgetTypeManager.addWidgetType(type);
			assertNotNull(this._widgetTypeManager.getWidgetType(widgetTypeCode));
			try {
				this._widgetTypeManager.deleteWidgetType(widgetTypeCode);
				fail();
			} catch (Throwable t) {
				assertNotNull(this._widgetTypeManager.getWidgetType(widgetTypeCode));
			}
		} catch (Throwable t) {
			throw t;
		} finally {
			if (null != this._widgetTypeManager.getWidgetType(widgetTypeCode)) {
				this._mockWidgetTypeDAO.deleteWidgetType(widgetTypeCode);
			}
			((IManager) this._widgetTypeManager).refresh();
			assertNull(this._widgetTypeManager.getWidgetType(widgetTypeCode));
		}
    }
    
    public void testAddDeleteWidgetType() throws Throwable {
    	String widgetTypeCode = "test_widgetType";
    	assertNull(this._widgetTypeManager.getWidgetType(widgetTypeCode));
    	try {
    		this._widgetTypeManager.deleteWidgetType(widgetTypeCode);
			WidgetType type = this.createNewWidgetType(widgetTypeCode);
			this._widgetTypeManager.addWidgetType(type);
			assertNotNull(this._widgetTypeManager.getWidgetType(widgetTypeCode));
		} catch (Throwable t) {
			throw t;
		} finally {
			if (null != this._widgetTypeManager.getWidgetType(widgetTypeCode)) {
				this._widgetTypeManager.deleteWidgetType(widgetTypeCode);
			}
			assertNull(this._widgetTypeManager.getWidgetType(widgetTypeCode));
		}
    }

    public void testUpdateTitles() throws Throwable {
    	String widgetTypeCode = "test_widgetType";
    	assertNull(this._widgetTypeManager.getWidgetType(widgetTypeCode));
    	try {
			WidgetType type = this.createNewWidgetType(widgetTypeCode);
			this._widgetTypeManager.addWidgetType(type);
			WidgetType extracted = this._widgetTypeManager.getWidgetType(widgetTypeCode);
			assertNotNull(extracted);
			assertEquals("Titolo", extracted.getTitles().get("it"));
			assertEquals("Title", extracted.getTitles().get("en"));
			ApsProperties newTitles = new ApsProperties();
			newTitles.put("it", "Titolo modificato");
			newTitles.put("en", "Modified title");
	    	this._widgetTypeManager.updateShowletTypeTitles(widgetTypeCode, newTitles);
	    	extracted = this._widgetTypeManager.getWidgetType(widgetTypeCode);
			assertNotNull(extracted);
			assertEquals("Titolo modificato", extracted.getTitles().get("it"));
			assertEquals("Modified title", extracted.getTitles().get("en"));
		} catch (Throwable t) {
			throw t;
		} finally {
			if (null != this._widgetTypeManager.getWidgetType(widgetTypeCode)) {
				this._widgetTypeManager.deleteWidgetType(widgetTypeCode);
			}
			assertNull(this._widgetTypeManager.getWidgetType(widgetTypeCode));
		}
    }
    
    public void testUpdate() throws Throwable {
    	String widgetTypeCode = "test_showletType";
    	assertNull(this._widgetTypeManager.getWidgetType(widgetTypeCode));
    	try {
			WidgetType type = this.createNewWidgetType(widgetTypeCode);
			this._widgetTypeManager.addWidgetType(type);
			WidgetType extracted = this._widgetTypeManager.getWidgetType(widgetTypeCode);
			assertNotNull(extracted);
			assertEquals("content_viewer", extracted.getParentType().getCode());
			assertEquals("ART112", extracted.getConfig().get("contentId"));
			
			ApsProperties newProperties = new ApsProperties();
	    	this._widgetTypeManager.updateWidgetType(widgetTypeCode, extracted.getTitles(), newProperties, type.getMainGroup());
	    	extracted = this._widgetTypeManager.getWidgetType(widgetTypeCode);
			assertNotNull(extracted);
			assertNotNull(extracted.getConfig());
			assertEquals(0, extracted.getConfig().size());
			
			newProperties.put("contentId", "EVN103");
			this._widgetTypeManager.updateWidgetType(widgetTypeCode, extracted.getTitles(), newProperties, type.getMainGroup());
			extracted = this._widgetTypeManager.getWidgetType(widgetTypeCode);
			assertNotNull(extracted);
			assertEquals("EVN103", extracted.getConfig().get("contentId"));
		} catch (Throwable t) {
			throw t;
		} finally {
			if (null != this._widgetTypeManager.getWidgetType(widgetTypeCode)) {
				this._widgetTypeManager.deleteWidgetType(widgetTypeCode);
			}
			assertNull(this._widgetTypeManager.getWidgetType(widgetTypeCode));
		}
    }
    
    private WidgetType createNewWidgetType(String code) {
    	WidgetType type = new WidgetType();
    	type.setCode(code);
    	ApsProperties titles = new ApsProperties();
    	titles.put("it", "Titolo");
    	titles.put("en", "Title");
    	type.setTitles(titles);
    	WidgetType parent = this._widgetTypeManager.getWidgetType("content_viewer");
    	assertNotNull(parent);
    	type.setParentType(parent);
    	type.setPluginCode("jacms");
    	ApsProperties config = new ApsProperties();
    	config.put("contentId", "ART112");
    	type.setConfig(config);
    	return type;
    }
    
    private void init() throws Exception {
		try {
			this._widgetTypeManager = (IWidgetTypeManager) this.getService(SystemConstants.WIDGET_TYPE_MANAGER);
			DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
			this._mockWidgetTypeDAO = new MockWidgetTypeDAO();
			this._mockWidgetTypeDAO.setDataSource(dataSource);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
    
    private IWidgetTypeManager _widgetTypeManager = null;
    private MockWidgetTypeDAO _mockWidgetTypeDAO;
    
}