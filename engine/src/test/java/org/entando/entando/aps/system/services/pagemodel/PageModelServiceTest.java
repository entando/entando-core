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
package org.entando.entando.aps.system.services.pagemodel;

import java.io.IOException;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.aps.system.services.pagemodel.model.PageModelDtoBuilder;
import org.entando.entando.web.pagemodel.model.PageModelRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class PageModelServiceTest {

    @Mock
    private IPageModelManager pageModelManager;

    //@Mock
    private PageModelDtoBuilder dtoBuilder = new PageModelDtoBuilder();

    @InjectMocks
    private PageModelService pageModelService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        pageModelService.setDtoBuilder(dtoBuilder);
    }

    @Test
    public void test_add_page_model() throws ApsSystemException, JsonParseException, JsonMappingException, IOException {

        String payload = " {\n"
                + "            \"code\": \"test\",\n"
                + "            \"description\": \"test\",\n"
                + "            \"configuration\": {\n"
                + "                \"frames\": [\n"
                + "                    {\n"
                + "                        \"pos\": 0,\n"
                + "                        \"description\": \"test_frame\",\n"
                + "                        \"mainFrame\": false,\n"
                + "                        \"defaultWidget\": null,\n"
                + "                        \"sketch\": null\n"
                + "                    }\n"
                + "                ]\n"
                + "            },\n"
                + "            \"pluginCode\": null,\n"
                + "            \"template\": \"hello world\"\n"
                + "        }";

        ObjectMapper objectMapper = new ObjectMapper();
        PageModelRequest pageModelRequest = objectMapper.readValue(payload, PageModelRequest.class);

        this.pageModelService.addPageModel(pageModelRequest);

        Mockito.verify(pageModelManager, Mockito.times(1)).addPageModel(Mockito.any());

    }

}
