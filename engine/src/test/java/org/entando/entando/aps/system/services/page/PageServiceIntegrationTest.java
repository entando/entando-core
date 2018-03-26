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
package org.entando.entando.aps.system.services.page;

import com.agiletec.aps.BaseTestCase;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.web.page.model.PagePositionRequest;
import org.entando.entando.web.page.model.PageRequest;
import org.entando.entando.web.page.model.PageStatusRequest;
import org.junit.Test;

/**
 *
 * @author paddeo
 */
public class PageServiceIntegrationTest extends BaseTestCase {

    private IPageService pageService;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    private void init() throws Exception {
        try {
            pageService = (IPageService) this.getApplicationContext().getBean(IPageService.BEAN_NAME);
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void testGetPage() {
        PageDto page = pageService.getPage("pagina_1", IPageService.STATUS_ONLINE);
        assertNotNull(page);
        assertEquals(IPageService.STATUS_ONLINE, page.getStatus());
    }

    @Test
    public void testGetPageTree() {
        List<PageDto> pages = pageService.getPages("pagina_1");
        assertNotNull(pages);
        assertEquals(2, pages.size());
        assertEquals(IPageService.STATUS_ONLINE, pages.get(0).getStatus());
    }

    @Test
    public void testAddAndRemovePage() throws Throwable {
        PageDto pageToClone = pageService.getPage("pagina_11", "draft");
        assertNotNull(pageToClone);
        PageRequest pageRequest = this.createRequestFromDto(pageToClone);
        pageRequest.setCode("pagina_13");
        PageDto addedPage = pageService.addPage(pageRequest);
        assertNotNull(addedPage);
        assertEquals("pagina_13", addedPage.getCode());
        assertEquals("pagina_1", addedPage.getParentCode());

        addedPage = pageService.getPage("pagina_13", "draft");
        assertNotNull(addedPage);
        assertEquals("pagina_13", addedPage.getCode());
        assertEquals("pagina_1", addedPage.getParentCode());

        pageService.removePage("pagina_13");
        try {
            addedPage = null;
            addedPage = pageService.getPage("pagina_13", "draft");
            fail("RestRourceNotFoundException not thrown");
        } catch (RestRourceNotFoundException e) {
            assertNull(addedPage);
        }
    }

    @Test
    public void testUpdatePage() {
        PageDto oldPages = pageService.getPage("pagina_12", "draft");
        assertNotNull(oldPages);
        assertEquals(2, oldPages.getTitles().size());
        assertEquals("Pagina 1-2", oldPages.getTitles().get("it"));

        PageRequest pageRequest = this.createRequestFromDto(oldPages);
        pageRequest.getTitles().put("it", "Pagina 1-2 mod");
        PageDto modPage = pageService.updatePage("pagina_12", pageRequest);
        assertNotNull(modPage);
        assertEquals(2, modPage.getTitles().size());
        assertEquals("Pagina 1-2 mod", modPage.getTitles().get("it"));

        modPage = pageService.getPage("pagina_12", "draft");
        assertNotNull(modPage);
        assertEquals(2, modPage.getTitles().size());
        assertEquals("Pagina 1-2 mod", modPage.getTitles().get("it"));

        pageRequest.getTitles().put("it", "Pagina 1-2");
        modPage = pageService.updatePage("pagina_12", pageRequest);

        modPage = pageService.getPage("pagina_12", "draft");
        assertNotNull(modPage);
        assertEquals(2, modPage.getTitles().size());
        assertEquals("Pagina 1-2", modPage.getTitles().get("it"));
    }

    @Test
    public void testUpdatePageStatus() {
        PageDto pageToClone = pageService.getPage("pagina_11", "draft");
        assertNotNull(pageToClone);
        PageRequest pageRequest = this.createRequestFromDto(pageToClone);
        pageRequest.setCode("pagina_13");
        PageDto addedPage = pageService.addPage(pageRequest);
        assertNotNull(addedPage);
        assertEquals("pagina_13", addedPage.getCode());
        assertEquals("pagina_1", addedPage.getParentCode());

        addedPage = pageService.getPage("pagina_13", "draft");
        assertEquals("draft", addedPage.getStatus());

        PageStatusRequest pageStatusRequest = new PageStatusRequest();
        pageStatusRequest.setStatus("published");
        PageDto modPage = pageService.updatePageStatus("pagina_13", pageStatusRequest.getStatus());
        assertNotNull(modPage);
        assertEquals("published", modPage.getStatus());

        addedPage = pageService.getPage("pagina_13", "published");
        assertNotNull(addedPage);
        assertEquals("published", addedPage.getStatus());

        pageService.removePage("pagina_13");
    }

    @Test
    public void testChangeOnlyPosition() {
        PageDto pageToClone = pageService.getPage("pagina_11", "draft");
        assertNotNull(pageToClone);
        PageRequest pageRequest = this.createRequestFromDto(pageToClone);
        pageRequest.setCode("pagina_13");
        PageDto addedPage = pageService.addPage(pageRequest);
        assertNotNull(addedPage);
        assertEquals("pagina_1", addedPage.getParentCode());
        assertEquals(3, addedPage.getPosition());

        PagePositionRequest pagePosRequest = new PagePositionRequest();
        pagePosRequest.setCode("pagina_13");
        pagePosRequest.setParentCode("pagina_1");
        pagePosRequest.setPosition(1);
        addedPage = pageService.movePage("pagina_13", pagePosRequest);
        assertNotNull(addedPage);
        assertEquals("pagina_1", addedPage.getParentCode());
        assertEquals(1, addedPage.getPosition());

        pageService.removePage("pagina_13");
    }

    @Test
    public void testChangeNode() {
        PageDto pageToClone = pageService.getPage("pagina_11", "draft");
        assertNotNull(pageToClone);
        PageRequest pageRequest = this.createRequestFromDto(pageToClone);
        pageRequest.setCode("pagina_13");
        PageDto addedPage = pageService.addPage(pageRequest);
        assertNotNull(addedPage);
        assertEquals("pagina_1", addedPage.getParentCode());
        assertEquals(3, addedPage.getPosition());

        PagePositionRequest pagePosRequest = new PagePositionRequest();
        pagePosRequest.setCode("pagina_13");
        pagePosRequest.setParentCode("pagina_2");
        pagePosRequest.setPosition(1);
        addedPage = pageService.movePage("pagina_13", pagePosRequest);
        assertNotNull(addedPage);
        assertEquals("pagina_2", addedPage.getParentCode());
        assertEquals(1, addedPage.getPosition());

        List<PageDto> pages = pageService.getPages("pagina_2");
        assertNotNull(pages);
        assertEquals(1, pages.size());
        assertEquals("pagina_13", pages.get(0).getCode());

        pageService.removePage("pagina_13");
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
        request.setStatus(pageToClone.getStatus());
        Map<String, String> titles = new HashMap<>();
        pageToClone.getTitles().keySet().forEach(lang -> titles.put(lang, pageToClone.getTitles().get(lang)));
        request.setTitles(titles);
        return request;
    }

}
