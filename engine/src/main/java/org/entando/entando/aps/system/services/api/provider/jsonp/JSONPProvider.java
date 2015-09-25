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
package org.entando.entando.aps.system.services.api.provider.jsonp;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.apache.cxf.jaxrs.utils.HttpUtils;
import org.entando.entando.aps.system.services.api.provider.json.JSONProvider;

/**
 * @author E.Santoboni
 */
@Produces("application/javascript")
@Provider
public class JSONPProvider<T> extends JSONProvider<T> {
	
	@Override
	public void writeTo(T t, Class<?> cls, Type genericType, Annotation[] anns, MediaType m, MultivaluedMap<String, Object> headers,
			OutputStream os) throws IOException {
		String prefix = getContext().getHttpServletRequest().getParameter("_jsonp");
		boolean hasPrefix = !(null == prefix || prefix.isEmpty());//!isEmpty(prefix);
		if (hasPrefix) {
			os.write(prefix.getBytes(HttpUtils.getSetEncoding(m, headers, "UTF-8")));
			os.write('(');
		}
		super.writeTo(t, cls, genericType, anns, m, headers, os);
		if (hasPrefix) {
			os.write(')');
		}
	}
	
}
