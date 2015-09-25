/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system;

import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.StrutsConstants;
import org.apache.struts2.spring.StrutsSpringObjectFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * @author M.Casari
 */
public class EntandoStrutsSpringObjectFactory extends StrutsSpringObjectFactory {

	@Inject
	public EntandoStrutsSpringObjectFactory(
			@Inject(value = StrutsConstants.STRUTS_OBJECTFACTORY_SPRING_AUTOWIRE, required = false) String autoWire,
			@Inject(value = StrutsConstants.STRUTS_OBJECTFACTORY_SPRING_AUTOWIRE_ALWAYS_RESPECT, required = false) String alwaysAutoWire,
			@Inject(value = StrutsConstants.STRUTS_OBJECTFACTORY_SPRING_USE_CLASS_CACHE, required = false) String useClassCacheStr,
			@Inject ServletContext servletContext,
			@Inject(StrutsConstants.STRUTS_DEVMODE) String devMode,
			@Inject Container container) {
		super(autoWire, alwaysAutoWire, useClassCacheStr, servletContext, devMode, container);
	}
	
	@Override
	public Object buildBean(String beanName, Map<String, Object> extraContext, boolean injectInternal) throws Exception {
		XmlWebApplicationContext xmlWebApplicationContext = (XmlWebApplicationContext) this.appContext;
		List<ClassPathXmlApplicationContext> contexts = (List<ClassPathXmlApplicationContext>) xmlWebApplicationContext.getServletContext().getAttribute("pluginsContextsList");
		Object o = null;
		if (this.appContext.containsBean(beanName)) {
			o = this.appContext.getBean(beanName);
		} else {
			if (contexts != null) {
				for (ClassPathXmlApplicationContext classPathXmlApplicationContext : contexts) {
					if (classPathXmlApplicationContext.containsBean(beanName)) {
						try {
							o = classPathXmlApplicationContext.getBean(beanName);
							return o;
						} catch (Exception ex) {
						}
					}
				}
			}
		}
		if (o == null) {
			Class beanClazz = getClassInstance(beanName);
			o = buildBean(beanClazz, extraContext);
		}
		if (injectInternal) {
			injectInternalBeans(o);
		}
		return o;
	}

}
