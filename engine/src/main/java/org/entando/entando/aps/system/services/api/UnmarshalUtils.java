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
package org.entando.entando.aps.system.services.api;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.aps.system.services.api.provider.json.JSONProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public class UnmarshalUtils {

	private static final Logger _logger =  LoggerFactory.getLogger(UnmarshalUtils.class);
	
	public static Object unmarshal(ApiMethod apiMethod, HttpServletRequest request, MediaType contentType) throws Throwable {
		return unmarshal(apiMethod, request.getInputStream(), contentType);
	}

	public static Object unmarshal(ApiMethod apiMethod, String requestBody, MediaType contentType) throws Throwable {
		InputStream stream = new ByteArrayInputStream(requestBody.getBytes());
		return unmarshal(apiMethod, stream, contentType);
	}

	public static Object unmarshal(ApiMethod apiMethod, InputStream bodyStream, MediaType contentType) throws Throwable {
		Object bodyObject = null;
		try {
			Class expectedType = apiMethod.getExpectedType();
            if (MediaType.APPLICATION_JSON_TYPE.equals(contentType)) {
                JSONProvider jsonProvider = new JSONProvider();
                bodyObject = jsonProvider.readFrom(expectedType, expectedType.getGenericSuperclass(),
                        expectedType.getAnnotations(), contentType, null, bodyStream);
            } else {
                JAXBContext context = JAXBContext.newInstance(expectedType);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                bodyObject = (Object) unmarshaller.unmarshal(bodyStream);
            }
		} catch (Throwable t) {
			_logger.error("Error unmarshalling request body", t);
			//ApsSystemUtils.logThrowable(t, UnmarshalUtils.class, "unmarshal");
			throw new ApsSystemException("Error unmarshalling request body", t);
		}
		return bodyObject;
	}

}
