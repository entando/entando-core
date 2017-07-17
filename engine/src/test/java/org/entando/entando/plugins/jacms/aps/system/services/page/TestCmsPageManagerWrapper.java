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
package org.entando.entando.plugins.jacms.aps.system.services.page;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.services.page.IPage;

/**
 * @author E.Santoboni - M.Diana
 */
public class TestCmsPageManagerWrapper extends BaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testGetContentUtilizers() throws Throwable {
		List<IPage> utilizers = this._pageManagerWrapper.getContentUtilizers("ART187");
		assertEquals(3, utilizers.size());
		List<String> codes = new ArrayList<String>();
		for (int i = 0; i < utilizers.size(); i++) {
			IPage page = utilizers.get(i);
			assertTrue(page.isOnlineInstance());
			codes.add(page.getCode());
		}
		assertTrue(codes.contains("coach_page"));
		assertTrue(codes.contains("pagina_11"));
		assertTrue(codes.contains("pagina_2"));

		utilizers = this._pageManagerWrapper.getContentUtilizers("ART111");
		assertEquals(1, utilizers.size());
		assertEquals("customers_page", utilizers.get(0).getCode());
		assertTrue(utilizers.get(0).isOnlineInstance());
	}

	private void init() throws Exception {
		try {
			this._pageManagerWrapper = (CmsPageManagerWrapper) super.getApplicationContext().getBean("jacmPageManagerWrapper");
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}

	private CmsPageManagerWrapper _pageManagerWrapper;

}
