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
package org.entando.entando.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/**
 * An instance of this class can be obtained calling the createAuthRequest()
 * method from a controller test class. This builder can be used for performing
 * mocked HTTP requests using a valid authorization header. JSON is the default
 * content type.
 *
 * Example:
 * <code>createAuthRequest(post(BASE_URL)).setContent(payload).execute()</code>
 */
public class AuthRequestBuilder {

    private final MockMvc mockMvc;
    private final String accessToken;
    private final ObjectMapper mapper;
    private MockHttpServletRequestBuilder requestBuilder;
    private String mediaType;
    private String content;

    public AuthRequestBuilder(MockMvc mockMvc, String accessToken,
            MockHttpServletRequestBuilder requestBuilder) {

        this.mockMvc = mockMvc;
        this.accessToken = accessToken;
        this.mapper = new ObjectMapper();

        this.requestBuilder = requestBuilder;

        // default
        mediaType = MediaType.APPLICATION_JSON_VALUE;
    }

    public AuthRequestBuilder setStringContent(String content) {
        this.content = content;
        return this;
    }

    public <T> AuthRequestBuilder setContent(T content) {
        try {
            this.content = mapper.writeValueAsString(content);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException();
        }
        return this;
    }

    public AuthRequestBuilder setMediaType(String mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public ResultActions execute() {

        requestBuilder = requestBuilder
                .contentType(mediaType)
                .header("Authorization", "Bearer " + accessToken);

        if (content != null) {
            requestBuilder = requestBuilder.content(content);
        }

        try {
            return mockMvc.perform(requestBuilder);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
