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
package com.agiletec.plugins.jacms.apsadmin.system.entity;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.entity.type.EntityTypesAction;
import com.agiletec.apsadmin.system.entity.type.IEntityTypesAction;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestJacmsEntityManagersAction extends ApsAdminBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testFailureGetEntityPrototypes() throws Throwable {
		this.executeTestFailureGetEntityPrototypes("wrongEntityServiceName");
		this.executeTestFailureGetEntityPrototypes(null);
	}
	
	private void executeTestFailureGetEntityPrototypes(String wrongManagerName) throws Throwable {
		String result = this.executeViewModels(wrongManagerName);
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertEquals(1, fieldErrors.get("entityManagerName").size());
	}
	
	public void testGetEntityPrototypes() throws Throwable {
		String result = this.executeViewModels(JacmsSystemConstants.CONTENT_MANAGER);
		assertEquals(Action.SUCCESS, result);
		IEntityTypesAction action = (IEntityTypesAction) this.getAction();
		List<IApsEntity> entityPrototypes = action.getEntityPrototypes();
		assertNotNull(entityPrototypes);
		assertEquals(4, entityPrototypes.size());
		
		IApsEntity firstType = entityPrototypes.get(0);
		assertEquals("ART", firstType.getTypeCode());
		assertEquals("Articolo rassegna stampa", firstType.getTypeDescr());
		
		IApsEntity lastType = entityPrototypes.get(3);
		assertEquals("RAH", lastType.getTypeCode());
		assertEquals("Tipo_Semplice", lastType.getTypeDescr());
	}
	
	private String executeViewModels(String entityManagerName) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/Entity", "viewEntityTypes");
		if (null != entityManagerName) {
			this.addParameter("entityManagerName", entityManagerName);
		}
		return this.executeAction();
	}
	
	public void testTrashManagedEntityType() throws Throwable {
		String result = this.trashEntityPrototype("ART");
		assertEquals("hasReferences", result);
		EntityTypesAction action = (EntityTypesAction) this.getAction();
		List<String> references = action.getReferences();
		assertEquals(11, references.size());
	}

	public void testDeleteManagedEntityType() throws Throwable {
		String result = this.deleteEntityPrototype("ART");
		assertEquals("hasReferences", result);
		EntityTypesAction action = (EntityTypesAction) this.getAction();
		List<String> references = action.getReferences();
		assertEquals(11, references.size());
	}
	
	public void testTrashAndDeleteEntityType() throws Throwable {
		int initEntityTypes = this._contentManager.getEntityPrototypes().size();
		String typeCode = "TST";
		try {
			this.addEntityTypeForTest(typeCode, "Description");
			assertEquals(initEntityTypes+1, this._contentManager.getEntityPrototypes().size());
			String result = this.trashEntityPrototype(typeCode);
			assertEquals(Action.SUCCESS, result);
			
			result = this.deleteEntityPrototype(typeCode);
			assertEquals(Action.SUCCESS, result);
			assertEquals(initEntityTypes, this._contentManager.getEntityPrototypes().size());
		} catch (Throwable t) {
			if (null != this._contentManager.getEntityPrototype(typeCode)) {
				((IEntityTypesConfigurer) this._contentManager).removeEntityPrototype(typeCode);
				assertEquals(initEntityTypes, this._contentManager.getEntityPrototypes().size());
			}
			throw t;
		}
	}
	
	private String trashEntityPrototype(String entityTypeCode) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/Entity", "trashEntityType");
		this.addParameter("entityManagerName", JacmsSystemConstants.CONTENT_MANAGER);
		this.addParameter("entityTypeCode", entityTypeCode);
		return this.executeAction();
	}
	
	private String deleteEntityPrototype(String entityTypeCode) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/Entity", "removeEntityType");
		this.addParameter("entityManagerName", JacmsSystemConstants.CONTENT_MANAGER);
		this.addParameter("entityTypeCode", entityTypeCode);
		return this.executeAction();
	}
	
	private void addEntityTypeForTest(String typeCode, String typeDescr) throws Throwable {
		assertNull(this._contentManager.getEntityPrototype(typeCode));
		IApsEntity prototype = this._contentManager.getEntityPrototype("ART");
		prototype.setTypeCode(typeCode);
		prototype.setTypeDescr(typeDescr);
		((IEntityTypesConfigurer) this._contentManager).addEntityPrototype(prototype);
	}
	
	public void testFailureInitEditEntityPrototype() throws Throwable {
		this.testExecuteFailureInitEditEntityPrototype(null);
		this.testExecuteFailureInitEditEntityPrototype("wrongEntityTypeCode");
	}
	
	private void testExecuteFailureInitEditEntityPrototype(String wrongEntityTypeCode) throws Throwable {
		assertNull(this._contentManager.getEntityPrototype(wrongEntityTypeCode));
		String result = this.executeInitEntityPrototype(wrongEntityTypeCode);
		assertEquals("wrongEntityType", result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertEquals(1, fieldErrors.get("entityTypeCode").size());
	}
	
	public void testInitEditEntityPrototype() throws Throwable {
		String result = this.executeInitEntityPrototype("ART");
		assertEquals(Action.SUCCESS, result);
	}
	
	private String executeInitEntityPrototype(String entityTypeCode) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/Entity", "initEditEntityType");
		this.addParameter("entityManagerName", JacmsSystemConstants.CONTENT_MANAGER);
		if (null != entityTypeCode) {
			this.addParameter("entityTypeCode", entityTypeCode);
		}
		return this.executeAction();
	}
	
	private void init() throws Exception {
    	try {
    		this._contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }
	
    private IContentManager _contentManager = null;
	
}