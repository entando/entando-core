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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.entity.type.IEntityTypeConfigAction;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestJacmsEntityTypeConfigAction extends ApsAdminBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testFailureAddEntityPrototype() throws Throwable {
		String result = this.executeAddEntityPrototype("wrongEntityManagerName");
		assertEquals("wrongEntityManager", result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertEquals(1, fieldErrors.get("entityManagerName").size());
	}
	
	private String executeAddEntityPrototype(String entityManagerName) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jacms/Entity", "addEntityType");
		this.addParameter("entityManagerName", entityManagerName);
		return this.executeAction();
	}
	
	public void testFailureEditEntityPrototype() throws Throwable {
		String result = this.executeEditEntityPrototype("ART", "wrongEntityManagerName");
		assertEquals("wrongEntityManager", result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertEquals(1, fieldErrors.get("entityManagerName").size());
	}
	
	public void testEditEntityPrototype() throws Throwable {
		String result = this.executeEditEntityPrototype("ART", JacmsSystemConstants.CONTENT_MANAGER);
		assertEquals(Action.SUCCESS, result);
		IApsEntity contentType = (IApsEntity) this.getRequest().getSession().getAttribute(IEntityTypeConfigAction.ENTITY_TYPE_ON_EDIT_SESSION_PARAM);
		assertNotNull(contentType);
		List<AttributeInterface> attributes = contentType.getAttributeList();
		assertEquals(7, attributes.size());
	}
	
	public void testEditNullEntityPrototype() throws Throwable {
		String result = this.executeEditEntityPrototype("WWW", JacmsSystemConstants.CONTENT_MANAGER);
		assertEquals(Action.INPUT, result);
		IApsEntity contentType = (IApsEntity) this.getRequest().getSession().getAttribute(IEntityTypeConfigAction.ENTITY_TYPE_ON_EDIT_SESSION_PARAM);
		assertNull(contentType);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertEquals(1, fieldErrors.get("entityTypeCode").size());
	}
	
	public void testMoveAttribute() throws Throwable {
		String result = this.executeEditEntityPrototype("ART", JacmsSystemConstants.CONTENT_MANAGER);
		assertEquals(Action.SUCCESS, result);
		IApsEntity contentType = (IApsEntity) this.getRequest().getSession().getAttribute(IEntityTypeConfigAction.ENTITY_TYPE_ON_EDIT_SESSION_PARAM);
		assertNotNull(contentType);
		List<AttributeInterface> attributes = contentType.getAttributeList();
		assertEquals(7, attributes.size());
		assertEquals("VediAnche", attributes.get(2).getName());
		assertEquals("CorpoTesto", attributes.get(3).getName());
		
		this.initAction("/do/jacms/Entity", "moveAttribute");
		this.addParameter("attributeIndex", "2");
		this.addParameter("movement", ApsAdminSystemConstants.MOVEMENT_DOWN_CODE);
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		
		contentType = (IApsEntity) this.getRequest().getSession().getAttribute(IEntityTypeConfigAction.ENTITY_TYPE_ON_EDIT_SESSION_PARAM);
		assertNotNull(contentType);
		attributes = contentType.getAttributeList();
		assertEquals(7, attributes.size());
		assertEquals("VediAnche", attributes.get(3).getName());
		assertEquals("CorpoTesto", attributes.get(2).getName());
	}
	
	public void testRemoveAttribute() throws Throwable {
		String result = this.executeEditEntityPrototype("ART", JacmsSystemConstants.CONTENT_MANAGER);
		assertEquals(Action.SUCCESS, result);
		IApsEntity contentType = (IApsEntity) this.getRequest().getSession().getAttribute(IEntityTypeConfigAction.ENTITY_TYPE_ON_EDIT_SESSION_PARAM);
		assertNotNull(contentType);
		List<AttributeInterface> attributes = contentType.getAttributeList();
		assertEquals(7, attributes.size());
		assertEquals("VediAnche", attributes.get(2).getName());
		assertEquals("CorpoTesto", attributes.get(3).getName());
		
		this.initAction("/do/jacms/Entity", "removeAttribute");
		this.addParameter("attributeIndex", "2");
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		
		contentType = (IApsEntity) this.getRequest().getSession().getAttribute(IEntityTypeConfigAction.ENTITY_TYPE_ON_EDIT_SESSION_PARAM);
		assertNotNull(contentType);
		attributes = contentType.getAttributeList();
		assertEquals(6, attributes.size());
		assertEquals("CorpoTesto", attributes.get(2).getName());
	}
	
	private String executeEditEntityPrototype(String entityTypeCode, String entityManagerName) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jacms/Entity", "editEntityType");
		this.addParameter("entityManagerName", entityManagerName);
		this.addParameter("entityTypeCode", entityTypeCode);
		return this.executeAction();
	}
	
	
	
	public void testValidateSaveEntityType() throws Throwable {
		Set<String> initEntityTypeCodes = this._contentManager.getEntityPrototypes().keySet();
		this.setUserOnSession("admin");
		this.initAction("/do/jacms/Entity", "addEntityType");
		this.addParameter("entityManagerName", JacmsSystemConstants.CONTENT_MANAGER);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		try {
			result = this.executeSaveEntityType("", "");
			assertEquals(Action.INPUT, result);
			Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
			assertEquals(2, fieldErrors.size());
			assertEquals(1, fieldErrors.get("entityTypeCode").size());
			assertEquals(1, fieldErrors.get("entityTypeDescription").size());
			
			result = this.executeSaveEntityType("", "Description for Test");//special Characters on entity Type Code
			assertEquals(Action.INPUT, result);
			fieldErrors = this.getAction().getFieldErrors();
			assertEquals(1, fieldErrors.size());
			assertEquals(1, fieldErrors.get("entityTypeCode").size());
			
			result = this.executeSaveEntityType("uF#", "Description for Test");//special Characters on entity Type Code
			assertEquals(Action.INPUT, result);
			fieldErrors = this.getAction().getFieldErrors();
			assertEquals(1, fieldErrors.size());
			assertEquals(1, fieldErrors.get("entityTypeCode").size());
			
			result = this.executeSaveEntityType("uF2", "Description for Test");//special Characters on entity Type Code
			assertEquals(Action.INPUT, result);
			fieldErrors = this.getAction().getFieldErrors();
			assertEquals(1, fieldErrors.size());
			assertEquals(1, fieldErrors.get("entityTypeCode").size());
			
			result = this.executeSaveEntityType("UF33", "Description for Test");//Entity type excedees max length
			assertEquals(Action.INPUT, result);
			fieldErrors = this.getAction().getFieldErrors();
			assertEquals(1, fieldErrors.size());
			assertEquals(1, fieldErrors.get("entityTypeCode").size());
			
		} catch (Throwable t) {
			Iterator<String> iter = this._contentManager.getEntityPrototypes().keySet().iterator();
			while (iter.hasNext()) {
				String typeCode = (String) iter.next();
				if (!initEntityTypeCodes.contains(typeCode)) {
					((IEntityTypesConfigurer) this._contentManager).removeEntityPrototype(typeCode);
				}
			}
			throw t;
		}
	}
	
	public void testSaveEntityType() throws Throwable {
		String typeCode = "TST";
		assertNull(this._contentManager.getEntityPrototype(typeCode));
		try {
			IApsEntity prototype = this._contentManager.getEntityPrototype("ART");
			prototype.setTypeCode(typeCode);
			prototype.setTypeDescr("Entity type Description");
			this.getRequest().getSession().setAttribute(IEntityTypeConfigAction.ENTITY_TYPE_MANAGER_SESSION_PARAM, JacmsSystemConstants.CONTENT_MANAGER);
			this.getRequest().getSession().setAttribute(IEntityTypeConfigAction.ENTITY_TYPE_OPERATION_ID_SESSION_PARAM, new Integer(ApsAdminSystemConstants.ADD));
			this.getRequest().getSession().setAttribute(IEntityTypeConfigAction.ENTITY_TYPE_ON_EDIT_SESSION_PARAM, prototype);
			
			String result = this.executeSaveEntityType(prototype.getTypeCode(), prototype.getTypeDescr());
			assertEquals(Action.SUCCESS, result);
			assertNotNull(this._contentManager.getEntityPrototype(typeCode));
			assertNull(this.getRequest().getSession().getAttribute(IEntityTypeConfigAction.ENTITY_TYPE_ON_EDIT_SESSION_PARAM));
			
			result = this.executeEditEntityPrototype(typeCode, JacmsSystemConstants.CONTENT_MANAGER);
			assertEquals(Action.SUCCESS, result);
			assertNotNull(this.getRequest().getSession().getAttribute(IEntityTypeConfigAction.ENTITY_TYPE_ON_EDIT_SESSION_PARAM));
		} catch (Throwable t) {
			throw t;
		} finally {
			if (null != this._contentManager.getEntityPrototype(typeCode)) {
				((IEntityTypesConfigurer) this._contentManager).removeEntityPrototype(typeCode);
			}
		}
	}
	
	private String executeSaveEntityType(String entityTypeCode, String entityTypeDescription) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jacms/Entity", "saveEntityType");
		this.addParameter("entityTypeCode", entityTypeCode);
		this.addParameter("entityTypeDescription", entityTypeDescription);
		return this.executeAction();
	}
	
	public void testFailureAddAttribute() throws Throwable {
		String result = this.executeEditEntityPrototype("ART", JacmsSystemConstants.CONTENT_MANAGER);
		assertEquals(Action.SUCCESS, result);
		result = this.executeAddAttribute("WrongAttributeTypeCode");
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertEquals(1, fieldErrors.get("attributeTypeCode").size());
		
		result = this.executeAddAttribute(null);
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertEquals(1, fieldErrors.get("attributeTypeCode").size());
	}
	
	public void testAddAttribute() throws Throwable {
		String result = this.executeEditEntityPrototype("ART", JacmsSystemConstants.CONTENT_MANAGER);
		assertEquals(Action.SUCCESS, result);
		result = this.executeAddAttribute("Monotext");
		assertEquals(Action.SUCCESS, result);
	}
	
	private String executeAddAttribute(String attributeTypeCode) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jacms/Entity", "addAttribute");
		if (null != attributeTypeCode) {
			this.addParameter("attributeTypeCode", attributeTypeCode);
		}
		return this.executeAction();
	}
	
	public void testFailureEditAttribute() throws Throwable {
		String result = this.executeEditEntityPrototype("ART", JacmsSystemConstants.CONTENT_MANAGER);
		assertEquals(Action.SUCCESS, result);
		IApsEntity contentType = (IApsEntity) this.getRequest().getSession().getAttribute(IEntityTypeConfigAction.ENTITY_TYPE_ON_EDIT_SESSION_PARAM);
		assertNotNull(contentType);
		assertTrue(!contentType.getAttributeMap().containsKey("Abstract"));
		
		result = this.executeEditAttribute("Abstract");
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertEquals(1, fieldErrors.get("attributeName").size());
		
		result = this.executeEditAttribute(null);
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertEquals(1, fieldErrors.get("attributeName").size());
	}
	
	public void testEditAttribute() throws Throwable {
		String result = this.executeEditEntityPrototype("ART", JacmsSystemConstants.CONTENT_MANAGER);
		assertEquals(Action.SUCCESS, result);
		IApsEntity contentType = (IApsEntity) this.getRequest().getSession().getAttribute(IEntityTypeConfigAction.ENTITY_TYPE_ON_EDIT_SESSION_PARAM);
		assertNotNull(contentType);
		assertTrue(contentType.getAttributeMap().containsKey("Data"));
		
		result = this.executeEditAttribute("Data");
		assertEquals(Action.SUCCESS, result);
	}
	
	private String executeEditAttribute(String attributeName) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jacms/Entity", "editAttribute");
		if (null != attributeName) {
			this.addParameter("attributeName", attributeName);
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