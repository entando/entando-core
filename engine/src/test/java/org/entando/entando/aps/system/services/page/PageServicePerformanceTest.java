/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.page;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPageManager;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.web.page.model.PageRequest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author paddeo
 */
public class PageServicePerformanceTest extends BaseTestCase {

    private IPageService pageService;
    private IPageManager pageManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    private void init() throws Exception {
        try {
            pageService = (IPageService) this.getApplicationContext().getBean(IPageService.BEAN_NAME);
            pageManager = (IPageManager) this.getApplicationContext().getBean(SystemConstants.PAGE_MANAGER);
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void testAddPages() throws Throwable {

        long start = System.currentTimeMillis();
        List<Long> addTimes = new ArrayList<>();
        for(int i = 0; i<3000; i++) {

            long pageStart = System.currentTimeMillis();
            PageDto pageToClone = pageService.getPage("pagina_11", "draft");
            assertNotNull(pageToClone);
            PageRequest pageRequest = this.createRequestFromDto(pageToClone);
            pageRequest.setCode("perf_pagina_"+i);
            PageDto addedPage = pageService.addPage(pageRequest);
            assertNotNull(addedPage);
            assertEquals("perf_pagina_"+i, addedPage.getCode());
            assertEquals("pagina_1", addedPage.getParentCode());

            try {
                addedPage = null;
                addedPage = pageService.getPage("pagina_13", "draft");
                fail("RestRourceNotFoundException not thrown");
            } catch (ResourceNotFoundException e) {
                assertNull(addedPage);
            }

            long pageEnd = System.currentTimeMillis();
            addTimes.add(pageEnd-pageStart);
        }

        long total = 0;
        for(int i =0; i<addTimes.size(); i++) {
            total+=addTimes.get(i);
            System.out.println("Add page "+i+": "+addTimes.get(i));
        }
        System.out.println("Total "+total);
    }

    private PageRequest createRequestFromDto(PageDto pageToClone) {
        PageRequest request = new PageRequest();
        request.setCharset(pageToClone.getCharset());
        request.setCode(pageToClone.getCode());
        request.setContentType(pageToClone.getContentType());
        request.setDisplayedInMenu(pageToClone.isDisplayedInMenu());
        request.setJoinGroups(pageToClone.getJoinGroups());
        request.setOwnerGroup(pageToClone.getOwnerGroup());
        request.setPageModel(pageToClone.getPageModel());
        request.setParentCode(pageToClone.getParentCode());
        request.setSeo(pageToClone.isSeo());
        //request.setStatus(pageToClone.getStatus());
        Map<String, String> titles = new HashMap<>();
        pageToClone.getTitles().keySet().forEach(lang -> titles.put(lang, pageToClone.getTitles().get(lang)));
        request.setTitles(titles);
        return request;
    }

}
