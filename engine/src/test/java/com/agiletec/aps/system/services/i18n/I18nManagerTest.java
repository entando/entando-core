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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.agiletec.aps.system.services.i18n.cache.II18nManagerCacheWrapper;
import com.agiletec.aps.util.ApsProperties;

public class I18nManagerTest {

	@Mock
	private II18nManagerCacheWrapper cacheWrapper;

	@Mock
	private II18nDAO ii18nDAO;

	@InjectMocks
	private I18nManager i18nManager = null;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetLabels() throws Throwable {
		when(cacheWrapper.getLabelGroups()).thenReturn(createMockLabels());
		assertThat(this.i18nManager.getLabelGroups().size(), is(3));
		String label = i18nManager.getLabel("ONE", "it");
		assertNotNull(label);
		assertEquals(label, "uno");
		label = i18nManager.getLabel("not-exists", "it");
		assertNull(label);
	}

	@Test
	public void testAddLabelGroup() throws Throwable {
		String key = "TEST_KEY";
		ApsProperties labels = createLabel("prova", "test");
		i18nManager.addLabelGroup(key, labels);
		Mockito.verify(cacheWrapper, Mockito.times(1)).addLabelGroup(key, labels);
	}

	@Test
	public void testUpdateLabels() throws Throwable {
		String key = "TEST_KEY";
		ApsProperties labels = createLabel("prova", "test");
		i18nManager.updateLabelGroup(key, labels);
		Mockito.verify(cacheWrapper, Mockito.times(1)).updateLabelGroup(key, labels);
	}

	@Test
	public void testGetLabelsKey() throws Throwable {
		when(cacheWrapper.getLabelGroups()).thenReturn(createMockLabels());
		assertThat(this.i18nManager.getLabelGroups().size(), is(3));
		assertEquals(0, i18nManager.searchLabelsKey("*", false, false, null).size());
		assertEquals(3, i18nManager.searchLabelsKey("", false, false, null).size());
		assertEquals(1, i18nManager.searchLabelsKey("one", false, false, null).size());
		assertEquals(1, i18nManager.searchLabelsKey("one", true, false, null).size());
		assertEquals(2, i18nManager.searchLabelsKey("e", false, true, "it").size());
	}

	@Test
	public void testDeleteLabels() throws Throwable {
		String key = "ONE";
		i18nManager.deleteLabelGroup(key);
		Mockito.verify(cacheWrapper, Mockito.times(1)).removeLabelGroup(key);
	}

	//

	private Map<String, ApsProperties> createMockLabels() {
		Map<String, ApsProperties> labelsGroup = new HashMap<>();
		labelsGroup.put("ONE", createLabel("uno", "one"));
		labelsGroup.put("TWO", createLabel("due", "two"));
		labelsGroup.put("3", createLabel("tre", "three"));
		return labelsGroup;
	}

	public static ApsProperties createLabel(String it, String en) {
		ApsProperties labelOne = new ApsProperties();
		labelOne.put("it", it);
		labelOne.put("en", en);
		return labelOne;
	}

}

