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
package org.entando.entando.aps.system.init;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.entando.entando.aps.system.init.cache.IInitializerManagerCacheWrapper;
import org.entando.entando.aps.system.init.model.SystemInstallationReport;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanFactory;

public class InitializerManagerTest {

	@Mock
	private IInitializerManagerCacheWrapper cacheWrapper;

	@Mock
	private BeanFactory beanFactory;

	@InjectMocks
	private InitializerManager initializerManager = null;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void should_get_currentReport() {
		when(cacheWrapper.getReport()).thenReturn(createMockReport());
		SystemInstallationReport report = this.initializerManager.getCurrentReport();
		assertThat(report, is(not(nullValue())));
	}

	private SystemInstallationReport createMockReport() {
		SystemInstallationReport report = new SystemInstallationReport("<report />");
		return report;
	}

}
