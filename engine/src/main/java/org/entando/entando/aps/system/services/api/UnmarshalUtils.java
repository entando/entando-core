/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.api;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsWebApplicationUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.aps.system.services.api.provider.json.JSONProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author E.Santoboni
 */
public class UnmarshalUtils {

	private static final Logger _logger =  LoggerFactory.getLogger(UnmarshalUtils.class);

	private UnmarshalUtils() {
		//
	}

	@Deprecated
	public static Object unmarshal(ApiMethod apiMethod, HttpServletRequest request, MediaType contentType) throws Throwable {
		return unmarshal(apiMethod.getExpectedType(), request, contentType);
	}
	
	public static Object unmarshal(Class expectedType, HttpServletRequest request, MediaType contentType) throws Throwable {
		ApplicationContext applicationContext = ApsWebApplicationUtils.getWebApplicationContext(request);
		return unmarshal(applicationContext, expectedType, request.getInputStream(), contentType);
	}
	
	@Deprecated
	public static Object unmarshal(ApiMethod apiMethod, String requestBody, MediaType contentType) throws Throwable {
		return unmarshal(apiMethod.getExpectedType(), requestBody, contentType);
	}
	
	@Deprecated
	public static Object unmarshal(Class expectedType, String requestBody, MediaType contentType) throws Throwable {
		InputStream stream = new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));
		return unmarshal(null, expectedType, stream, contentType);
	}
	
	@Deprecated
	public static Object unmarshal(ApiMethod apiMethod, InputStream bodyStream, MediaType contentType) throws Throwable {
		return unmarshal(null, apiMethod.getExpectedType(), bodyStream, contentType);
	}
	
	public static Object unmarshal(ApplicationContext applicationContext, 
			Class expectedType, InputStream bodyStream, MediaType contentType) throws Throwable {
		Object bodyObject = null;
		try {
			String body = IOUtils.toString(bodyStream, "UTF-8");
			InputStream stream = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
            if (MediaType.APPLICATION_JSON_TYPE.equals(contentType)) {
                JSONProvider jsonProvider = (null != applicationContext) ? 
						(JSONProvider) applicationContext.getBean("jsonProvider"): 
						new JSONProvider();
                bodyObject = jsonProvider.readFrom(expectedType, expectedType.getGenericSuperclass(),
                        expectedType.getAnnotations(), contentType, null, stream/*bodyStream*/);
            } else {
                JAXBContext context = JAXBContext.newInstance(expectedType);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                bodyObject = unmarshaller.unmarshal(stream);
            }
		} catch (Throwable t) {
			_logger.error("Error unmarshalling request body", t);
			throw new ApsSystemException("Error unmarshalling request body", t);
		}
		return bodyObject;
	}
	
}
