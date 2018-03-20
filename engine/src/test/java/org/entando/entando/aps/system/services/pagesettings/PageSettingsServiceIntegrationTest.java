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
import org.entando.entando.web.pagesettings.model.Param;
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
        assertNotNull(settings.getParams());
        assertTrue(settings.getParams().size() > 0);
        assertTrue(settings.getParams().stream().anyMatch(param -> param.getName().equals("baseUrl")));
        assertEquals("static", settings.getParams().stream().filter(param -> param.getName().equals("baseUrl")).findFirst().get().getValue());
    }

    @Test
    public void testUpdateSettings() {
        PageSettingsRequest request = new PageSettingsRequest();
        List<Param> params = new ArrayList<>();
        Param baseUrl = new Param();
        baseUrl.setName("baseUrlContext");
        baseUrl.setValue("false");
        params.add(baseUrl);
        request.setParams(params);
        PageSettingsDto settings = pageSettingsService.updatePageSettings(request);
        assertNotNull(settings);
        assertNotNull(settings.getParams());
        assertTrue(settings.getParams().size() > 1);
        assertTrue(settings.getParams().stream().anyMatch(param -> param.getName().equals("baseUrlContext")));
        assertEquals("false", settings.getParams().stream().filter(param -> param.getName().equals("baseUrlContext")).findFirst().get().getValue());

        settings = pageSettingsService.getPageSettings();
        assertNotNull(settings);
        assertNotNull(settings.getParams());
        assertTrue(settings.getParams().size() > 1);
        assertTrue(settings.getParams().stream().anyMatch(param -> param.getName().equals("baseUrlContext")));
        assertEquals("false", settings.getParams().stream().filter(param -> param.getName().equals("baseUrlContext")).findFirst().get().getValue());
    }

}
