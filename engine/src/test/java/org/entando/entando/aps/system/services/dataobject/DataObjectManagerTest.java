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
package org.entando.entando.aps.system.services.dataobject;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.parse.IEntityTypeFactory;
import com.agiletec.aps.system.common.notify.INotifyManager;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobject.parse.DataObjectDOM;
import org.entando.entando.aps.system.services.dataobject.parse.DataTypeDOM;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class DataObjectManagerTest {

	private class FakeKeyGeneratorManager extends AbstractService implements IKeyGeneratorManager {

		private int key = 1;

		@Override
		public void init() throws Exception {
			//
		}

		@Override
		public int getUniqueKeyCurrentValue() throws ApsSystemException {
			return key++;
		}

	}

	@Mock
	private IEntityTypeFactory entityTypeFactory;

	@Mock
	private DataTypeDOM entityTypeDom;

	@Mock
	private DataObjectDOM entityDom;

	@Mock
	private IDataObjectDAO dataObjectDao;

	@Mock
	private BeanFactory beanFactory;

	@Mock
	private INotifyManager notifyManager;

	private String beanName = "DataObjectManager";

	private String className = "org.entando.entando.aps.system.services.dataobject.model.DataObject";

	@InjectMocks
	private DataObjectManager dataObjectManager;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.dataObjectManager.setEntityClassName(className);
		//this.dataObjectManager.setConfigItemName(configItemName);
		this.dataObjectManager.setBeanName(this.beanName);
	}

	@Test
	public void testCreateDataObject() throws ApsSystemException {
		String typeCode = "ART";
		// @formatter:off
		when(entityTypeFactory.extractEntityType(
				"ART", 
				DataObject.class, 
				dataObjectManager.getConfigItemName(), 
				this.entityTypeDom, 
				dataObjectManager.getName(), 
				this.entityDom))
		.thenReturn(this.createFakeEntity(typeCode, null, null));
		// @formatter:on
		DataObject dataObjectType = dataObjectManager.createDataObject(typeCode);
		assertThat(dataObjectType, is(not(nullValue())));
	}

	@Test
	public void testCrateWithDefaultModel() throws ApsSystemException {
		String typeCode = "ART";
		// @formatter:off
		when(entityTypeFactory.extractEntityType(
				"ART", 
				DataObject.class, 
				dataObjectManager.getConfigItemName(), 
				this.entityTypeDom, 
				dataObjectManager.getName(), 
				this.entityDom))
		.thenReturn(this.createFakeEntity(typeCode, "1", null));
		// @formatter:on
		DataObject dataObject = dataObjectManager.createDataObject(typeCode);
		assertThat(dataObject, is(not(nullValue())));

		String defaultModel = dataObject.getDefaultModel();
		assertThat(defaultModel, is("1"));
	}

	@Test
	public void testCrateWithDefaultViewPage() throws ApsSystemException {
		String typeCode = "ART";
		// @formatter:off
		when(entityTypeFactory.extractEntityType(
				"ART", 
				DataObject.class, 
				dataObjectManager.getConfigItemName(), 
				this.entityTypeDom, 
				dataObjectManager.getName(), 
				this.entityDom))
		.thenReturn(this.createFakeEntity(typeCode, "1", "dataObjectview"));
		// @formatter:on
		DataObject dataObject = dataObjectManager.createDataObject(typeCode);
		String viewPage = dataObject.getViewPage();
		assertThat(viewPage, is("dataObjectview"));
	}

	@Test
	public void testSave() throws ApsSystemException {
		String typeCode = "ART";
		// @formatter:off
		when(beanFactory.getBean(SystemConstants.KEY_GENERATOR_MANAGER)).thenReturn(new FakeKeyGeneratorManager()); 
		when(entityTypeFactory.extractEntityType(
				"ART", 
				DataObject.class, 
				dataObjectManager.getConfigItemName(), 
				
				this.entityTypeDom, 
				dataObjectManager.getName(), 
				this.entityDom))
		.thenReturn(this.createFakeEntity(typeCode, "1", "dataObjectview"));
		// @formatter:on

		DataObject dataObject = dataObjectManager.createDataObject(typeCode);
		dataObjectManager.saveDataObject(dataObject);
		assertThat(dataObject.getId(), is("ART1"));

		Mockito.verify(dataObjectDao, Mockito.times(1)).addEntity(dataObject);
	}


	private IApsEntity createFakeEntity(String typeCode, String defaultModel, String viewPage) {
		DataObject dataObject = new DataObject();
		dataObject.setTypeCode(typeCode);
		dataObject.setDefaultModel(defaultModel);
		dataObject.setViewPage(viewPage);
		return dataObject;
	}

}
