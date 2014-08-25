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
package org.entando.entando.apsadmin.api.helper;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;

import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.aps.system.services.api.model.StringApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.aps.util.FileTextReader;

/**
 * @author E.Santoboni
 */
public class SchemaGeneratorActionHelper {

	private static final Logger _logger =  LoggerFactory.getLogger(SchemaGeneratorActionHelper.class);
	
    public Class extractResponseClass(ApiMethod method, HttpServletRequest request) throws ApsSystemException {
        Class responseClass = null;
        try {
            if (null != method.getResponseClassName()) {
                responseClass = Class.forName(method.getResponseClassName());
            } else {
                responseClass = this.extractReturnType(method, request);
            }
            if (null == responseClass) {
                responseClass = StringApiResponse.class;
            }
        } catch (Throwable t) {
        	_logger.error("Error extracting response Class", t);
            //ApsSystemUtils.logThrowable(t, this, "extractResponseClass", "Error extracting response Class");
            throw new ApsSystemException("Error extracting response Class", t);
        }
        return responseClass;
    }
    
    protected Class extractReturnType(ApiMethod method, HttpServletRequest request) throws ApsSystemException {
        Class returnType = null;
        try {
            Object bean =  ApsWebApplicationUtils.getBean(method.getSpringBean(), request);
            if (null == bean) {
                throw new ApsSystemException("Null bean '" + method.getSpringBean() 
                        + "' from method " + method.getHttpMethod() + " of resource " + method.getResourceName());
            }
            Class beanClass = bean.getClass();
            String methodName = method.getSpringBeanMethod();
            Method beanMethod = null;
            if (method.getHttpMethod().equals(ApiMethod.HttpMethod.GET) || method.getHttpMethod().equals(ApiMethod.HttpMethod.DELETE)) {
                Class[] parameterTypes = new Class[]{Properties.class};
                beanMethod = beanClass.getDeclaredMethod(methodName, parameterTypes);
            } else {
                Class expectedType = method.getExpectedType();
                if (null == expectedType) {
                    throw new ApsSystemException("Null expectedType for Method " + method.getHttpMethod() + " for resource " + method.getResourceName());
                }
                try {
                    //special case - put or post method with properties
                    Class[] parameterTypes = new Class[]{expectedType, Properties.class};
                    beanMethod = beanClass.getDeclaredMethod(methodName, parameterTypes);
                } catch (Exception e) {
                    //nothing to catch
                }
                if (null == beanMethod) {
                    Class[] parameterTypes2 = new Class[]{expectedType};
                    beanMethod = beanClass.getDeclaredMethod(methodName, parameterTypes2);
                }
            }
            if (null != beanMethod) {
                returnType = beanMethod.getReturnType();
            }
            if (null != returnType && returnType.getName().equals("void")) {
                return null;
            }
        } catch (Throwable t) {
        	_logger.error("Error extracting return type", t);
            //ApsSystemUtils.logThrowable(t, this, "extractReturnType", "Error extracting return type ");
            return null;
        }
        return returnType;
    }
    
    public String generateSchema(Class jaxbObject) throws Exception {
        InputStream mainStream = null;
        String schema = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(jaxbObject);
            StreamSchemaOutputResolver sor = new StreamSchemaOutputResolver();
            jaxbContext.generateSchema(sor);
            mainStream = sor.getStream();
            schema = FileTextReader.getText(mainStream);
        } catch (Throwable t) {
        	_logger.error("Error extracting generating schema from class {}", jaxbObject, t);
            //ApsSystemUtils.logThrowable(t, this, "generateSchema", "Error extracting generating schema from class " + jaxbObject);
            throw new RuntimeException("Error extracting generating schema", t);
        } finally {
            if (null != mainStream) mainStream.close();
        }
        return schema;
    }
    
}
