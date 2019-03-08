/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.web.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class IgnoreJacksonWriteOnlyAccessTest {

    private static final Logger logger = LoggerFactory.getLogger(IgnoreJacksonWriteOnlyAccessTest.class);

    public static class JsonTestClass {

        @JsonProperty(value = "val", access = JsonProperty.Access.WRITE_ONLY)
        String value1;

        String value2;
    }

    @Test
    public void test() throws Exception {

        String testValue = "testValue";
        JsonTestClass obj = new JsonTestClass();
        obj.value1 = testValue;

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

        String serialized = mapper.writeValueAsString(obj);
        logger.info(serialized);

        JsonTestClass deserialized = mapper.readValue(serialized, JsonTestClass.class);
        assertThat(deserialized.value1).isNull();

        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        mapper.setAnnotationIntrospector(new IgnoreJacksonWriteOnlyAccess());

        serialized = mapper.writeValueAsString(obj);
        logger.info(serialized);

        deserialized = mapper.readValue(serialized, JsonTestClass.class);
        assertThat(deserialized.value1).isEqualTo(testValue);
    }
}
