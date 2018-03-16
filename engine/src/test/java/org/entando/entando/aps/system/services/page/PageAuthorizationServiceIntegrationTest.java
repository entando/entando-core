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
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import java.util.List;
import org.entando.entando.aps.system.services.auth.IAuthorizationService;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.junit.Test;

/**
 *
 * @author paddeo
 */
public class PageAuthorizationServiceIntegrationTest extends BaseTestCase {

    private PageAuthorizationService authorizationService;

    private IPageService pageService;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    private void init() throws Exception {
        try {
            authorizationService = (PageAuthorizationService) this.getApplicationContext().getBean(IAuthorizationService.BEAN_NAME_FOR_PAGE);
            pageService = (IPageService) this.getApplicationContext().getBean(IPageService.BEAN_NAME);
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void testIsAuthOnPage() throws Throwable {
        UserDetails admin = this.getUser("admin");
        UserDetails customer = this.getUser("editorCustomers");
        assertTrue(authorizationService.isAuth(admin, "coach_page"));
        assertFalse(authorizationService.isAuth(customer, "coach_page"));
        assertTrue(authorizationService.isAuth(admin, "customers_page"));
        assertTrue(authorizationService.isAuth(customer, "customers_page"));
    }

    @Test
    public void testFilteredPageTree() throws Throwable {
        UserDetails admin = this.getUser("admin");
        UserDetails customer = this.getUser("editorCustomers");
        List<PageDto> pages = this.pageService.getPages("homepage");
        List<PageDto> pagesForAdmin = this.authorizationService.filterList(admin, pages);
        List<PageDto> pagesForCustomer = this.authorizationService.filterList(customer, pages);
        assertEquals(7, pagesForAdmin.size());
        assertEquals(5, pagesForCustomer.size());
        pagesForCustomer.forEach(page -> assertTrue((page.getOwnerGroup().equals(Group.FREE_GROUP_NAME)
                || page.getOwnerGroup().equals("customers"))));
    }
}
