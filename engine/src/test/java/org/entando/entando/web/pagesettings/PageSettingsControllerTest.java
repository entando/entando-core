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
package org.entando.entando.web.pagesettings;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import java.util.ArrayList;
import java.util.List;
import org.entando.entando.aps.system.services.pagesettings.PageSettingsService;
import org.entando.entando.aps.system.services.pagesettings.model.PageSettingsDto;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.pagesettings.model.PageSettingsRequest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 *
 * @author paddeo
 */
public class PageSettingsControllerTest extends AbstractControllerTest {

    @Mock
    protected PageSettingsService pageSettingsService;

    @InjectMocks
    private PageSettingsController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();
    }

    @Test
    public void should_load_the_list_of_settings() throws Exception {
        UserDetails user = createManagePagesUser();

        String accessToken = mockOAuthInterceptor(user);
        when(pageSettingsService.getPageSettings()).thenReturn(createMockDto());
        ResultActions result = mockMvc.perform(
                get("/pageSettings")
                        .header("Authorization", "Bearer " + accessToken)
        );
        result.andExpect(status().isOk());
    }

    @Test
    public void should_not_update_with_empty_list_of_settings() throws Exception {
        UserDetails user = createManagePagesUser();
        String accessToken = mockOAuthInterceptor(user);
        when(pageSettingsService.updatePageSettings(createMockRequestEmptyParams())).thenReturn(createMockDto());
        ResultActions result = mockMvc.perform(
                put("/pageSettings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(createMockRequestEmptyParams()))
                        .header("Authorization", "Bearer " + accessToken)
        );
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void should_update_with_a_valid_list_of_settings() throws Exception {
        UserDetails user = createManagePagesUser();
        String accessToken = mockOAuthInterceptor(user);
        when(pageSettingsService.updatePageSettings(createMockRequest())).thenReturn(createMockDto());
        ResultActions result = mockMvc.perform(
                put("/pageSettings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(createMockRequest()))
                        .header("Authorization", "Bearer " + accessToken)
        );
        result.andExpect(status().isOk());
    }

    @Test
    public void should_be_unauthorized() throws Exception {
        UserDetails user = createGuestUser();
        String accessToken = mockOAuthInterceptor(user);

        ResultActions result = mockMvc.perform(
                get("/pageSettings")
                        .header("Authorization", "Bearer " + accessToken)
        );
        result.andExpect(status().isForbidden());
    }

    private PageSettingsDto createMockDto() {
        PageSettingsDto dto = new PageSettingsDto();
        for (int i = 1; i <= 5; i++) {
            dto.put("param_" + i, "value_" + i);
        }
        return dto;
    }

    private PageSettingsRequest createMockRequestEmptyParams() {
        PageSettingsRequest request = new PageSettingsRequest();
        return request;
    }

    private PageSettingsRequest createMockRequest() {
        PageSettingsRequest request = new PageSettingsRequest();
        for (int i = 1; i <= 5; i++) {
            request.put("param_" + i, "value_" + i);
        }
        return request;
    }

    private UserDetails createManagePagesUser() {
        return new OAuth2TestUtils.UserBuilder("managePages", "0x01")
                .withAuthorization(Group.FREE_GROUP_NAME, Permission.MANAGE_PAGES, Permission.MANAGE_PAGES)
                .build();
    }

    private UserDetails createGuestUser() {
        return new OAuth2TestUtils.UserBuilder("guest", "0x00")
                .withGroup(Group.FREE_GROUP_NAME)
                .build();
    }

}
