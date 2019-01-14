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
package org.entando.entando.web.page;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.web.page.model.PagePatchRequest;
import org.springframework.data.rest.webmvc.json.patch.JsonPatchPatchConverter;
import org.springframework.data.rest.webmvc.json.patch.Patch;
import org.springframework.stereotype.Component;

@Component
public class JsonPatchDeserializer extends JsonDeserializer<PagePatchRequest> {

    private ObjectMapper mapper;
    private JsonPatchPatchConverter jsonPatchConverter;

    public JsonPatchDeserializer() {
        mapper = new ObjectMapper();
        jsonPatchConverter = new JsonPatchPatchConverter(mapper);
    }

    @Override
    public PagePatchRequest deserialize(JsonParser jsonParser,
            DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node =  jsonParser.readValueAsTree();
        Patch patch = jsonPatchConverter.convert(node);
        return new PagePatchRequest().setPatch(patch);
    }

}
