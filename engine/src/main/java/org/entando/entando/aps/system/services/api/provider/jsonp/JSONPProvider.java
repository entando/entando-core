/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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
