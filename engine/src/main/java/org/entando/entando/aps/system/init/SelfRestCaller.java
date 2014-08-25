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
package org.entando.entando.aps.system.init;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Properties;

import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.entando.entando.aps.system.init.model.IPostProcess;
import org.entando.entando.aps.system.init.model.InvalidPostProcessResultException;
import org.entando.entando.aps.system.init.model.SelfRestCallPostProcess;
import org.entando.entando.aps.system.services.api.UnmarshalUtils;
import org.entando.entando.aps.system.services.api.model.AbstractApiResponse;
import org.entando.entando.aps.system.services.api.model.ApiError;
import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.aps.system.services.api.model.StringApiResponse;
import org.entando.entando.aps.system.services.api.server.IResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.FileTextReader;

/**
 * @author E.Santoboni
 */
public class SelfRestCaller implements IPostProcessor, BeanFactoryAware {

	private static final Logger _logger = LoggerFactory.getLogger(SelfRestCaller.class);
	
	@Override
	public int executePostProcess(IPostProcess postProcess) throws InvalidPostProcessResultException, ApsSystemException {
		if (!(postProcess instanceof SelfRestCallPostProcess)) {
			return 0;
		}
		SelfRestCallPostProcess selfRestCall = (SelfRestCallPostProcess) postProcess;
		IResponseBuilder responseBuilder = this.getResponseBuilder();
        try {
			Object result = null;
			ApiMethod method = responseBuilder.extractApiMethod(selfRestCall.getMethod(), selfRestCall.getNamespace(), selfRestCall.getResourceName());
			Properties properties = this.extractParameters(selfRestCall);
			if (method.getHttpMethod().equals(ApiMethod.HttpMethod.GET) || method.getHttpMethod().equals(ApiMethod.HttpMethod.DELETE)) {
				result = responseBuilder.createResponse(method, properties);
			} else {
				String contentBody = this.getContentBody(selfRestCall);
				Object bodyObject = UnmarshalUtils.unmarshal(method, contentBody, selfRestCall.getContentType());
				result = responseBuilder.createResponse(method, bodyObject, properties);
			}
			Response.Status responseStatus = this.extractResponseStatusCode(result);
			if (selfRestCall.isPrintResponse()) {
				this.printResponse(selfRestCall, result, responseStatus, method, properties);
			}
        } catch (InvalidPostProcessResultException t) {
        	_logger.error("error in executePostProcess", t);
			//ApsSystemUtils.logThrowable(t, this, "executePostProcess", t.getMessage());
			throw t;
		} catch (Throwable t) {
			_logger.error("Error invoking api method", t);
			//ApsSystemUtils.logThrowable(t, this, "executePostProcess", "Error invoking api method");
			throw new ApsSystemException("Error invoking api method", t);
        }
		return 1;
	}
	
	protected Properties extractParameters(SelfRestCallPostProcess selfRestCall) throws ApsSystemException {
		Properties properties = new Properties();
		try {
			ILangManager langManager = this.getLangManager();
			String langCode = selfRestCall.getLangCode();
			if (null == langCode || null == langManager.getLang(langCode)) {
				langCode = langManager.getDefaultLang().getCode();
			}
			if (null != selfRestCall.getQueryParameters()) {
				properties.putAll(selfRestCall.getQueryParameters());
			}
            properties.put(SystemConstants.API_LANG_CODE_PARAMETER, langCode);
            UserDetails user = this.getAuthenticationProvider().getUser(SystemConstants.ADMIN_USER_NAME);
            if (null != user) {
                properties.put(SystemConstants.API_USER_PARAMETER, user);
            } else {
				_logger.error("Admin user missing");
			}
		} catch (Throwable t) {
			_logger.error("Error extracting parameters", t);
			//ApsSystemUtils.logThrowable(t, this, "extractParameters", "Error extracting parameters");
			throw new ApsSystemException("Error extracting parameters", t);
		}
		return properties;
	}
	
	private String getContentBody(SelfRestCallPostProcess selfRestCall) throws Throwable {
		String contentBody = selfRestCall.getContentBody();
		if ((null == contentBody || contentBody.trim().length() == 0) && null != selfRestCall.getContentBodyPath()) {
			String path = selfRestCall.getContentBodyPath();
			InputStream is = null;
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			Resource resource = resolver.getResource(path);
			try {
				is = resource.getInputStream();
				contentBody = FileTextReader.getText(is);
			} catch (Throwable t) {
				_logger.error("Error loading contentBody from file '{}'", path, t);
				//ApsSystemUtils.logThrowable(t, this, "getContentBody", "Error loading contentBody from file '" + path + "'");
				throw t;
			} finally {
				if (null != is) {
					is.close();
				}
			}
		}
		return contentBody;
	}
	
	private void printResponse(SelfRestCallPostProcess selfRestCall, Object result, 
			Response.Status responseStatus, ApiMethod method, Properties properties) throws InvalidPostProcessResultException, Throwable {
		String responseClassName = method.getResponseClassName();
		Class responseClass = (null != responseClassName) ? Class.forName(responseClassName) : StringApiResponse.class;
		JAXBContext context = JAXBContext.newInstance(responseClass);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		boolean validResponse = (responseStatus.getStatusCode() == selfRestCall.getExpectedResult());
		StringBuilder path = new StringBuilder();	
		path.append("/api/rs/");
		path.append(properties.get(SystemConstants.API_LANG_CODE_PARAMETER)).append("/");
		if (null != method.getNamespace()) {
			path.append(method.getNamespace()).append("/");
		}
		path.append(method.getResourceName());
		StringBuilder log = new StringBuilder();
		StringWriter writer = new StringWriter();
		marshaller.marshal(result, writer);
		log.append("*************** Self Rest Call - response ***************\n");
		log.append(method.getHttpMethod().toString()).append(" ");
		log.append(path.toString()).append("\n");
		log.append("Result   ").append(responseStatus.getStatusCode()).append("\n");
		log.append("Expected ").append(selfRestCall.getExpectedResult()).append("\n");
		if (!validResponse) {
			log.append("******** INVALID RESPONSE STATUS ********\n");
		}
		log.append("---------------------------------------------------------\n");
		log.append(writer.toString()).append("\n");
		log.append("*********************************************************\n");
		if (!validResponse) {
			if (!selfRestCall.isFailOnError()) {
				log.append("failonerror was set to false: continuing anyway.\n");
			} else {
				log.append("the post processes will be stopped.\n");
			}
		}
		_logger.info(log.toString());
		System.out.println(log.toString());
		if (!validResponse && selfRestCall.isFailOnError()) {
			throw new InvalidPostProcessResultException(responseStatus.getStatusCode(), 
					selfRestCall.getExpectedResult(), path.toString(), method.getHttpMethod());
		}
	}
	
	protected Response.Status extractResponseStatusCode(Object responseObject) {
		if (responseObject instanceof AbstractApiResponse) {
			Response.Status status = Response.Status.OK;
			AbstractApiResponse mainResponse = (AbstractApiResponse) responseObject;
			if (null != mainResponse.getErrors()) {
				for (int i = 0; i < mainResponse.getErrors().size(); i++) {
					ApiError error = mainResponse.getErrors().get(i);
					Response.Status errorStatus = error.getStatus();
					if (null != errorStatus && status.getStatusCode() < errorStatus.getStatusCode()) {
						status = errorStatus;
					}
				}
			}
			return status;
		} else {
			return Response.Status.OK;
		}
	}
    
	protected IResponseBuilder getResponseBuilder() {
		return (IResponseBuilder) this.getBeanFactory().getBean(SystemConstants.API_RESPONSE_BUILDER);
	}
	
	protected ILangManager getLangManager() {
		return (ILangManager) this.getBeanFactory().getBean(SystemConstants.LANGUAGE_MANAGER);
	}
	
	protected IAuthenticationProviderManager getAuthenticationProvider() {
		return (IAuthenticationProviderManager) this.getBeanFactory().getBean(SystemConstants.AUTHENTICATION_PROVIDER_MANAGER);
	}
	
	protected BeanFactory getBeanFactory() {
		return _beanFactory;
	}
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this._beanFactory = beanFactory;
	}
	
	private BeanFactory _beanFactory;
	
}
