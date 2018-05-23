/*
 * Copyright 2018-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EntandoMessageCodesResolverTest {

    @Spy
    private Map<String, String> validationErrorCodeMapping = new HashMap<>();

    @InjectMocks
    private EntandoMessageCodesResolver messageCodesResolver;

    @Before
    public void setUp() throws Exception {
        validationErrorCodeMapping.put("NotNull", "51");
        validationErrorCodeMapping.put("Size", "52");
        validationErrorCodeMapping.put("Min", "53");
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testResolveCode() {
        String[] codes = messageCodesResolver.resolveMessageCodes("Size", "test");
        assertThat(codes[codes.length - 1], is("52"));
    }
}
