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
package org.entando.entando.apsadmin.system;

import com.agiletec.apsadmin.util.ApsRequestParamsUtil;
import com.opensymphony.xwork2.interceptor.ParametersInterceptor;
import com.opensymphony.xwork2.util.ValueStack;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.HttpParameters;
import org.apache.struts2.dispatcher.Parameter;

/**
 * Extension of default ParametersInterceptor.
 * Intercepts the parameters whose name is structured according to the syntax:<br />
 * &#60;ACTION_NAME&#62;?&#60;PARAM_NAME_1&#62;=&#60;PARAM_VALUE_1&#62;;&#60;PARAM_NAME_2&#62;=&#60;PARAM_VALUE_2&#62;;....;&#60;PARAM_NAME_N&#62;=&#60;PARAM_VALUE_N&#62;
 * <br />
 * @see com.opensymphony.xwork2.interceptor.ParametersInterceptor
 * @author E.Santoboni
 */
public class ApsParametersInterceptor extends ParametersInterceptor {
	
	@Override
	protected void setParameters(Object action, ValueStack stack, HttpParameters parameters) {
		HttpServletRequest request = ServletActionContext.getRequest();
		String entandoActionName = ApsRequestParamsUtil.extractEntandoActionName(request);
		if (null != entandoActionName) {
			this.createApsActionParam(entandoActionName, request, parameters);
		}
		super.setParameters(action, stack, parameters);
	}
	
	private HttpParameters createApsActionParam(String entandoActionName, HttpServletRequest request, HttpParameters parameters) {
		Map<String, Parameter> newParams = new TreeMap<String, Parameter>();
		Properties properties = ApsRequestParamsUtil.extractApsActionParameters(entandoActionName);
		Iterator<Object> iter = properties.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next().toString();
			Object value = properties.getProperty(key);
			request.setAttribute(key, value);
			Parameter.Request requestParams = new Parameter.Request(key, value);
			newParams.put(key, requestParams);
		}
		return parameters.appendAll(newParams);
	}
	
}
