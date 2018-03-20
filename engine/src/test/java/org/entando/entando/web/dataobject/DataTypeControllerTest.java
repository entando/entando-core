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
package org.entando.entando.web.dataobject;

import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.FileTextReader;
import java.io.InputStream;
import org.entando.entando.aps.system.services.dataobject.DataObjectManager;
import org.entando.entando.aps.system.services.dataobject.DataObjectService;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.dataobject.model.DataTypeDtoRequest;
import org.entando.entando.web.dataobject.validator.DataTypeValidator;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.validation.BindingResult;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class DataTypeControllerTest extends AbstractControllerTest {

    @Mock
    private DataObjectManager dataObjectManager;

    @Mock
    private DataObjectService dataObjectService;

    @InjectMocks
    private DataTypeValidator dataTypeValidator;

    @InjectMocks
    private DataTypeController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();
        controller.setDataTypeValidator(this.dataTypeValidator);
    }

    @Test
    public void testUpdateNoPayload() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(post("/dataTypes")
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void testPayloadOk() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        InputStream isJsonPost = this.getClass().getResourceAsStream("1_POST_valid.json");
        String jsonPost = FileTextReader.getText(isJsonPost);
        ResultActions result = mockMvc
                .perform(post("/dataTypes")
                        .content(jsonPost)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken));
        Mockito.verify(dataObjectService, Mockito.times(1)).addDataType(any(DataTypeDtoRequest.class), any(BindingResult.class));
        result.andExpect(status().isOk());
    }

}
