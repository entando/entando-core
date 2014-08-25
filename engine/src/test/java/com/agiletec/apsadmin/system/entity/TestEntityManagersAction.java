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
package com.agiletec.apsadmin.system.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.entity.ApsEntityManager;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.common.searchengine.IndexableAttributeInterface;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.entity.type.EntityAttributeConfigAction;
import com.agiletec.apsadmin.system.entity.type.IEntityTypeConfigAction;
import com.agiletec.apsadmin.system.entity.type.IEntityTypesAction;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestEntityManagersAction extends ApsAdminBaseTestCase {

	public void testExecuteViewServices() throws Throwable {
		this.setUserOnSession("supervisorCoach");
		this.initAction("/do/Entity", "viewManagers");
		String result = this.executeAction();
		assertEquals("userNotAllowed", result);

		this.setUserOnSession("admin");
		this.initAction("/do/Entity", "viewManagers");
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		EntityManagersAction action = (EntityManagersAction) this.getAction();
		List<String> entityManagers = action.getEntityManagers();
		assertNotNull(entityManagers);
		assertTrue(entityManagers.size()>=0);
	}

	public void testGetEntityPrototypes() throws Throwable {
		String[] defNames = this.getApplicationContext().getBeanNamesForType(ApsEntityManager.class);
		if (null == defNames || defNames.length == 0) return;
		this.setUserOnSession("admin");
		this.initAction("/do/Entity", "viewEntityTypes");
		this.addParameter("entityManagerName", defNames[0]);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		IEntityTypesAction action = (IEntityTypesAction) this.getAction();
		List<IApsEntity> entityPrototypes = action.getEntityPrototypes();
		assertNotNull(entityPrototypes);
		assertTrue(entityPrototypes.size()>=0);
	}

	public void testGetWrongEntityPrototypes() throws Throwable {
		this.executeGetWrongEntityPrototypes(null);
		this.executeGetWrongEntityPrototypes("");
		this.executeGetWrongEntityPrototypes("wrongEntityManagerName");
	}

	private void executeGetWrongEntityPrototypes(String entityManagerName) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/Entity", "viewEntityTypes");
		if (null != entityManagerName) {
			this.addParameter("entityManagerName", entityManagerName);
		}
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertEquals(1, fieldErrors.get("entityManagerName").size());
	}

	public void testInitAddEntityType() throws Throwable {
		String[] defNames = this.getApplicationContext().getBeanNamesForType(ApsEntityManager.class);
		if (null == defNames || defNames.length == 0) return;

		String result = this.executeInitAddEntityType("supervisorCoach", defNames[0]);
		assertEquals("userNotAllowed", result);

		result = this.executeInitAddEntityType("admin", defNames[0]);
		assertEquals(Action.SUCCESS, result);

		result = this.executeInitAddEntityType("admin", "");
		assertEquals(Action.INPUT, result);
		assertEquals(1, this.getAction().getFieldErrors().size());
	}

	private String executeInitAddEntityType(String username, String entityManagerName) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/Entity", "initAddEntityType");
		this.addParameter("entityManagerName", entityManagerName);
		return this.executeAction();
	}

	public void testInitEditEntityType() throws Throwable {
		String[] defNames = this.getApplicationContext().getBeanNamesForType(ApsEntityManager.class);
		if (null == defNames || defNames.length == 0) return;
		String entityManagerName = defNames[0];
		IEntityManager entityManager = (IEntityManager) this.getApplicationContext().getBean(entityManagerName);
		Map<String, IApsEntity> entityPrototypes = entityManager.getEntityPrototypes();
		if (null == entityPrototypes || entityPrototypes.size() == 0) return;
		IApsEntity realEntityPrototype = new ArrayList<IApsEntity>(entityPrototypes.values()).get(0);

		String result = this.executeInitEditEntityType("supervisorCoach", entityManagerName, realEntityPrototype.getTypeCode());
		assertEquals("userNotAllowed", result);

		result = this.executeInitEditEntityType("admin", null, realEntityPrototype.getTypeCode());
		assertEquals(Action.INPUT, result);
		assertEquals(1, this.getAction().getFieldErrors().size());
		assertEquals(1, this.getAction().getFieldErrors().get("entityManagerName").size());

		if (null == entityPrototypes.get("XXX")) {
			result = this.executeInitEditEntityType("admin", entityManagerName, "XXX");
			assertEquals("wrongEntityType", result);
			assertEquals(1, this.getAction().getFieldErrors().size());
			assertEquals(1, this.getAction().getFieldErrors().get("entityTypeCode").size());
		}

		result = this.executeInitEditEntityType("admin", entityManagerName, null);
		assertEquals("wrongEntityType", result);
		assertEquals(1, this.getAction().getFieldErrors().size());
		assertEquals(1, this.getAction().getFieldErrors().get("entityTypeCode").size());

		result = this.executeInitEditEntityType("admin", entityManagerName, realEntityPrototype.getTypeCode());
		assertEquals(Action.SUCCESS, result);
	}

	private String executeInitEditEntityType(String username, String entityManagerName, String entityTypeCode) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/Entity", "initEditEntityType");
		if (null != entityManagerName) {
			this.addParameter("entityManagerName", entityManagerName);
		}
		if (null != entityTypeCode) {
			this.addParameter("entityTypeCode", entityTypeCode);
		}
		return this.executeAction();
	}

	public void testEditAttribute() throws Throwable {
		//Search an entity manager
		String[] defNames = this.getApplicationContext().getBeanNamesForType(ApsEntityManager.class);
		if (null == defNames || defNames.length == 0) return;
		String entityManagerName = defNames[0];
		IEntityManager entityManager = (IEntityManager) this.getApplicationContext().getBean(entityManagerName);
		//get the entites managed by the ApsEntityManager
		Map<String, IApsEntity> entities = entityManager.getEntityPrototypes();
		if (null == entities || entities.size() == 0) return;
		List<String> enitiesTypeCodes =  new ArrayList<String>();
		enitiesTypeCodes.addAll(entities.keySet());
		//get the first entity type code available
		String entityTypeCode = enitiesTypeCodes.get(0);
		List<AttributeInterface> attributes =  entities.get(entityTypeCode).getAttributeList();
		//get the first attribute
		for (int i = 0; i < attributes.size(); i++) {
			AttributeInterface currentAttribute = attributes.get(i);
			String attributeName = currentAttribute.getName();
			String result = this.executeEditAttribute("admin", attributeName, entityTypeCode, entityManagerName);
			assertEquals(Action.SUCCESS, result);
			EntityAttributeConfigAction action = (EntityAttributeConfigAction) this.getAction();
			assertEquals(currentAttribute.getType(), action.getAttributeTypeCode());
			assertEquals(currentAttribute.isRequired(), action.getRequired().booleanValue());
			assertEquals(currentAttribute.isSearchable(), action.getSearchable().booleanValue());
			assertEquals(currentAttribute.getIndexingType().equalsIgnoreCase(IndexableAttributeInterface.INDEXING_TYPE_TEXT), action.getIndexable().booleanValue());
			if (currentAttribute.isTextAttribute()) {
				ITextAttribute attr = (ITextAttribute) currentAttribute;
				if (attr.getMaxLength() == -1) {
					assertNull(action.getMaxLength());
				} else {
					assertEquals(attr.getMaxLength(), action.getMaxLength().intValue());
				}
				if (attr.getMinLength() == -1) {
					assertNull(action.getMinLength());
				} else {
					assertEquals(attr.getMinLength(), action.getMinLength().intValue());
				}
				assertEquals(attr.getRegexp(), action.getRegexp());
			}
			assertEquals(ApsAdminSystemConstants.EDIT, action.getStrutsAction());
		}
	}
	
	private String executeEditAttribute(String username, String attributeName, String entityTypeCode, String entityManagerName) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/Entity/Attribute", "editAttribute");
		this.addParameter("attributeName", attributeName);
		IEntityManager entityManager = (IEntityManager) this.getApplicationContext().getBean(entityManagerName);
		IApsEntity entityType = entityManager.getEntityPrototype(entityTypeCode);
		this.getRequest().getSession().setAttribute(IEntityTypeConfigAction.ENTITY_TYPE_ON_EDIT_SESSION_PARAM, entityType);
		return this.executeAction();
	}
	
	public void testSaveNewAttribute() throws Throwable  {
		//Search an entity manager
		String[] defNames = this.getApplicationContext().getBeanNamesForType(ApsEntityManager.class);
		if (null == defNames || defNames.length == 0) return;
		String entityManagerName = defNames[0];
		IEntityManager entityManager = (IEntityManager) this.getApplicationContext().getBean(entityManagerName);
		//get the entites managed by the ApsEntityManager
		Map<String, IApsEntity> entities = entityManager.getEntityPrototypes();
		if (null == entities || entities.size() == 0) return;
		List<String> enitiesTypeCodes =  new ArrayList<String>();
		enitiesTypeCodes.addAll(entities.keySet());
		//get the first entity type code available
		String entityTypeCode = enitiesTypeCodes.get(0);
		try {
			//wrong name
			String attributeName="wrong name";
			String attributeTypeCode="Monotext";
			int strutsAction = ApsAdminSystemConstants.ADD;
			String result = this.executeSaveAttribute("admin", attributeName, attributeTypeCode, entityTypeCode, strutsAction, null, null, entityManagerName);
			assertEquals(Action.INPUT, result);
			EntityAttributeConfigAction action = (EntityAttributeConfigAction) this.getAction();
			Map<String, List<String>> fieldErrors = action.getFieldErrors();
			assertEquals(1, fieldErrors.size());
			assertTrue(fieldErrors.containsKey("attributeName"));
			//wrong length
			attributeName="right_name";
			result = this.executeSaveAttribute("admin", attributeName, attributeTypeCode, entityTypeCode, strutsAction, 3, 5, entityManagerName);
			assertEquals(Action.INPUT, result);
			fieldErrors = action.getFieldErrors();
			assertEquals(1, fieldErrors.size());
			assertTrue(fieldErrors.containsKey("minLength"));
			//insert ok
			attributeName="right_name";
			result = this.executeSaveAttribute("admin", attributeName, attributeTypeCode, entityTypeCode, strutsAction, 5, 3, entityManagerName);
			assertEquals(Action.SUCCESS, result);
			IApsEntity entity = entityManager.getEntityPrototype(entityTypeCode);
			assertTrue(entity.getAttributeMap().containsKey("right_name"));
			//duplicate attribute name
			attributeName="right_name";
			result = this.executeSaveAttribute("admin", attributeName, attributeTypeCode, entityTypeCode, strutsAction, null, null, entityManagerName);
			assertEquals(Action.INPUT, result);
			fieldErrors = action.getFieldErrors();
			assertEquals(1, fieldErrors.size());
			assertTrue(fieldErrors.containsKey("attributeName"));
		} catch (Throwable t) {
			//nothing to do
		} finally {
			IApsEntity entity = entityManager.getEntityPrototype(entityTypeCode);
			entity.getAttributeMap().remove("right_name");
			((IEntityTypesConfigurer) entityManager).updateEntityPrototype(entity);
		}
	}

	private String executeSaveAttribute(String username, String attributeName, String attributeTypeCode, String entityTypeCode, int strutsAction, Object maxLength, Object minLength, String entityManagerName) throws Throwable {
		IEntityManager entityManager = (IEntityManager) this.getApplicationContext().getBean(entityManagerName);
		IApsEntity entityType = entityManager.getEntityPrototype(entityTypeCode);
		this.setUserOnSession(username);
		this.initAction("/do/Entity/Attribute", "saveAttribute");
		this.addParameter("attributeName", attributeName);
		this.addParameter("strutsAction", strutsAction);
		this.addParameter("attributeTypeCode", attributeTypeCode);
		if (null != maxLength) {
			this.addParameter("maxLength", maxLength.toString());
		}
		if (null != minLength) {
			this.addParameter("minLength", minLength.toString());
		}
		this.getRequest().getSession().setAttribute(IEntityTypeConfigAction.ENTITY_TYPE_ON_EDIT_SESSION_PARAM, entityType);
		this.getRequest().getSession().setAttribute(IEntityTypeConfigAction.ENTITY_TYPE_MANAGER_SESSION_PARAM, entityManagerName);

		return this.executeAction();
	}

}