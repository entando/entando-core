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
package org.entando.entando.web.category;

import com.agiletec.aps.system.services.category.CategoryManager;
import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.aps.system.services.category.CategoryService;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.category.validator.CategoryValidator;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class CategoryControllerTest extends AbstractControllerTest {

    @Mock
    private CategoryManager categoryManager;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryValidator categoryValidator;

    @InjectMocks
    private CategoryController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();
        controller.setCategoryValidator(this.categoryValidator);
    }

    @Test
    public void testGetTreeOk() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/categories")
                        .param("parentCode", "home")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());

        result = mockMvc
                .perform(get("/categories")
                        .param("parentCode", "home"));
        result.andExpect(status().isOk());
    }

    @Test
    public void testGetCategory() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/categories/{code}", "home")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());

        result = mockMvc
                .perform(get("/categories/{code}", "home")
                        .param("parentCode", "home"));
        result.andExpect(status().isUnauthorized());
    }

}
