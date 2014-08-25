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
package com.agiletec.aps.util;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.RefreshableBean;

/**
 * Classe di utilità.
 * @author E.Santoboni
 */
public class ApsWebApplicationUtils {

	private static final Logger _logger = LoggerFactory.getLogger(ApsWebApplicationUtils.class);
	
	/**
	 * Resolve the given location pattern into Resource objects. 
	 * @param locationPattern The location pattern to resolve.
	 * @param svCtx The Servlet Context
	 * @return The corresponding Resource objects.
	 * @throws IOException In case of exception
	 */
	public static Resource[] getResources(String locationPattern, ServletContext svCtx) throws IOException {
		WebApplicationContext wac = getWebApplicationContext(svCtx);
		return wac.getResources(locationPattern);
	}
	
	/**
	 * Resolve the given location pattern into Resource objects. 
	 * @param locationPattern The location pattern to resolve.
	 * @param pageContext The Page Context
	 * @return The corresponding Resource objects.
	 * @throws IOException In case of exception.
	 */
	public static Resource[] getResources(String locationPattern, PageContext pageContext) throws IOException {
		return getResources(locationPattern, pageContext.getServletContext());
	}
	
	/**
	 * Restituisce un servizio di sistema.
	 * @param serviceName Il nome del servizio richiesto.
	 * @param request La request.
	 * @return Il servizio richiesto.
	 * @deprecated use getBean
	 */
	public static AbstractService getService(String serviceName, HttpServletRequest request) {
		WebApplicationContext wac = getWebApplicationContext(request);
		return getService(serviceName, wac);
	}
	
	/**
	 * Restituisce un servizio di sistema.
	 * Il seguente metodo è in uso ai tag jsp del sistema.
	 * @param serviceName Il nome del servizio richiesto.
	 * @param pageContext Il Contesto di pagina,
	 * @return Il servizio richiesto.
	 * @deprecated use getBean
	 */
	public static AbstractService getService(String serviceName, PageContext pageContext) {
		WebApplicationContext wac = getWebApplicationContext(pageContext.getServletContext());
		return getService(serviceName, wac);
	}
	
	/**
	 * Restituisce un bean di sistema.
	 * Il seguente metodo è in uso ai tag jsp del sistema.
	 * @param beanName Il nome del servizio richiesto.
	 * @param request La request.
	 * @return Il servizio richiesto.
	 */
	public static Object getBean(String beanName, HttpServletRequest request) {
		WebApplicationContext wac = getWebApplicationContext(request);
		return wac.getBean(beanName);
	}
	
	/**
	 * Restituisce un bean di sistema.
	 * Il seguente metodo è in uso ai tag jsp del sistema.
	 * @param beanName Il nome del servizio richiesto.
	 * @param pageContext Il Contesto di pagina,
	 * @return Il servizio richiesto.
	 */
	public static Object getBean(String beanName, PageContext pageContext) {
		WebApplicationContext wac = getWebApplicationContext(pageContext.getServletContext());
		return wac.getBean(beanName);
	}
	
	/**
	 * Restituisce il WebApplicationContext del sistema.
	 * @param request La request.
	 * @return Il WebApplicationContext del sistema.
	 */
	public static WebApplicationContext getWebApplicationContext(HttpServletRequest request) {
		ServletContext svCtx = request.getSession().getServletContext();
		WebApplicationContext wac = getWebApplicationContext(svCtx);
		return wac;
	}
	
	private static WebApplicationContext getWebApplicationContext(ServletContext svCtx) {
		return WebApplicationContextUtils.getWebApplicationContext(svCtx);
	}
	
	private static AbstractService getService(String serviceName, WebApplicationContext wac) {
		return (AbstractService) wac.getBean(serviceName);
	}
	
	/**
	 * Esegue il refresh del sistema.
	 * @param request La request.
	 * @throws Throwable In caso di errori in fase di aggiornamento del sistema.
	 */
	public static void executeSystemRefresh(HttpServletRequest request) throws Throwable {
		WebApplicationContext wac = getWebApplicationContext(request);
		executeSystemRefresh(wac);
	}
	
	public static void executeSystemRefresh(ServletContext svCtx) throws Throwable {
		WebApplicationContext wac = getWebApplicationContext(svCtx);
		executeSystemRefresh(wac);
	}
	
	private static void executeSystemRefresh(WebApplicationContext wac) throws Throwable {
		RefreshableBean configManager = (RefreshableBean) wac.getBean(SystemConstants.BASE_CONFIG_MANAGER);
		configManager.refresh();
		String[] defNames = wac.getBeanNamesForType(RefreshableBean.class);
		for (int i=0; i<defNames.length; i++) {
			Object bean = null;
			try {
				bean = wac.getBean(defNames[i]);
			} catch (Throwable t) {
				_logger.error("error in executeSystemRefresh", t);
				//ApsSystemUtils.logThrowable(t, ApsWebApplicationUtils.class, "executeSystemRefresh");
				bean = null;
			}
			if (bean != null) {
				((RefreshableBean) bean).refresh();
			}
		}
	}
	
}
