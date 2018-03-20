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
package org.entando.entando.aps.system.services.user;

import com.agiletec.aps.BaseTestCase;
import java.util.List;
import org.entando.entando.aps.system.services.user.model.UserAuthorityDto;
import org.entando.entando.web.user.model.UserAuthoritiesRequest;
import org.entando.entando.web.user.model.UserAuthority;
import org.junit.Test;

/**
 *
 * @author paddeo
 */
public class UserServiceIntegrationTest extends BaseTestCase {

    private IUserService userService;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    private void init() throws Exception {
        try {
            userService = (IUserService) this.getApplicationContext().getBean(IUserService.BEAN_NAME);
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void testAddAndRemoveUserAuthorities() throws Throwable {
        try {
            UserAuthoritiesRequest request = new UserAuthoritiesRequest();
            UserAuthority auth = new UserAuthority();
            auth.setGroup("management");
            auth.setRole("pageManager");
            request.add(auth);
            List<UserAuthorityDto> resp = userService.addUserAuthorities("editorCustomers", request);
            assertNotNull(resp);
            assertEquals(1, resp.size());
            assertEquals("management", resp.get(0).getGroup());

        } finally {
            UserAuthoritiesRequest request = new UserAuthoritiesRequest();
            UserAuthority auth = new UserAuthority();
            auth.setGroup("customers");
            auth.setRole("editor");
            request.add(auth);
            List<UserAuthorityDto> resp = userService.addUserAuthorities("editorCustomers", request);
            assertNotNull(resp);
            assertEquals(1, resp.size());
            assertEquals("customers", resp.get(0).getGroup());
        }
    }
}
