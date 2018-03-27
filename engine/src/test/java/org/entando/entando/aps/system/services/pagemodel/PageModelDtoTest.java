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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.aps.system.services.pagemodel.model.FrameDto;
import org.entando.entando.aps.system.services.pagemodel.model.PageModelDto;
import org.junit.Test;

public class PageModelDtoTest {

    @Test
    public void test_json() throws JsonProcessingException {

        FrameDto frameDto = new FrameDto();
        frameDto.setPos(0);
        frameDto.setDescr("description");

        PageModelDto dto = new PageModelDto();
        dto.setCode("code");
        dto.setDescr("description");
        dto.getConfiguration().getFrames().add(frameDto);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writer().writeValueAsString(dto);

        String actual = "{\"code\":\"code\",\"description\":\"description\",\"mainFrame\":-1,\"pluginCode\":null,\"template\":null,\"configuration\":{\"frames\":[{\"pos\":0,\"description\":\"description\",\"mainFrame\":false,\"defaultWidget\":{\"code\":null,\"properties\":{}},\"sketch\":{\"x1\":0,\"y1\":0,\"x2\":0,\"y2\":0}}]}}";
        //assertThat(json, is(actual));
        //TODO replace a string compare with a json compare
    }
}
