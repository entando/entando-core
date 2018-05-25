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
package org.entando.entando.aps.system.services.pagesettings;

import com.agiletec.aps.BaseTestCase;
import java.util.ArrayList;
import java.util.List;
import org.entando.entando.aps.system.services.pagesettings.model.PageSettingsDto;
import org.entando.entando.web.pagesettings.model.PageSettingsRequest;
import org.junit.Test;

/**
 *
 * @author paddeo
 */
public class PageSettingsServiceIntegrationTest extends BaseTestCase {

    private IPageSettingsService pageSettingsService;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    private void init() throws Exception {
        try {
            pageSettingsService = (IPageSettingsService) this.getApplicationContext().getBean(IPageSettingsService.BEAN_NAME);
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void testGetSettings() {
        PageSettingsDto settings = pageSettingsService.getPageSettings();
        assertNotNull(settings);
        assertTrue(settings.size() > 0);
        assertTrue(settings.keySet().stream().anyMatch(param -> param.equals("baseUrl")));
        assertEquals("static", settings.get(settings.keySet().stream().filter(param -> param.equals("baseUrl")).findFirst().get()));
    }

    @Test
    public void testUpdateSettings() {
        PageSettingsRequest request = new PageSettingsRequest();
        request.put("baseUrlContext", "false");
        PageSettingsDto settings = pageSettingsService.updatePageSettings(request);
        assertNotNull(settings);
        assertTrue(settings.size() > 1);
        assertTrue(settings.keySet().stream().anyMatch(param -> param.equals("baseUrlContext")));
        assertEquals("false", settings.get(settings.keySet().stream().filter(param -> param.equals("baseUrlContext")).findFirst().get()));

        settings = pageSettingsService.getPageSettings();
        assertNotNull(settings);
        assertTrue(settings.size() > 1);
        assertTrue(settings.keySet().stream().anyMatch(param -> param.equals("baseUrlContext")));
        assertEquals("false", settings.get(settings.keySet().stream().filter(param -> param.equals("baseUrlContext")).findFirst().get()));
    }

}
