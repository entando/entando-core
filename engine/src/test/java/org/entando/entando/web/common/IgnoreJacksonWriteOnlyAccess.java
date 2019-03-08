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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

/**
 * Ignores the JsonProperty.Access.WRITE_ONLY.
 *
 * Using Jackson it is possible to prevent the serialization of a field
 * annotating it in the following way:
 * <code>@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)</code>. This is
 * very useful for fields like passwords, however this annotation can became a
 * problem in tests, when often an ObjectMapper is used to build the payload for
 * the test (and in that case we want to have the property serialized). To
 * bypass this behavior in tests set this class on the ObjectMapper in this way:
 * <code>mapper.setAnnotationIntrospector(new IgnoreJacksonWriteOnlyAccess());</code>
 */
public class IgnoreJacksonWriteOnlyAccess extends JacksonAnnotationIntrospector {

    @Override
    public JsonProperty.Access findPropertyAccess(Annotated m) {
        JsonProperty.Access access = super.findPropertyAccess(m);
        if (access == JsonProperty.Access.WRITE_ONLY) {
            return JsonProperty.Access.AUTO;
        }
        return access;
    }
}
