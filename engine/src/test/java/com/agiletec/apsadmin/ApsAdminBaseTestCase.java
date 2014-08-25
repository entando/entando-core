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
package com.agiletec.apsadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.spring.StrutsSpringObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;

import com.agiletec.ConfigTestUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.NotifyManager;
import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionProxyFactory;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.ContainerBuilder;

/**
 * The base Class for all test of admin area.
 * A Spacial thanks to Arsenalist.
 * @author Zarar Siddiqi - E.Santoboni
 */
public class ApsAdminBaseTestCase extends TestCase {

	@Override
	protected void setUp() throws Exception {
		boolean refresh = false;
		if (null == _applicationContext) {
			// Link the servlet context and the Spring context
			_servletContext = new MockServletContext("", new FileSystemResourceLoader());

			_applicationContext = this.getConfigUtils().createApplicationContext(_servletContext);

			_servletContext.setAttribute(
					WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, _applicationContext);
		} else {
			refresh = true;
		}

		this._request = new MockHttpServletRequest();
		this._response = new MockHttpServletResponse();
		this._request.setSession(new MockHttpSession(this._servletContext));

		if (refresh) {
			try {
				ApsWebApplicationUtils.executeSystemRefresh(this._request);
				this.waitNotifyingThread();
			} catch (Throwable e) {}
		}

		// Use spring as the object factory for Struts
		StrutsSpringObjectFactory ssf = new StrutsSpringObjectFactory(null, null, null, _servletContext, null, this.createContainer());
		ssf.setApplicationContext(_applicationContext);

		// Dispatcher is the guy that actually handles all requests.  Pass in
		// an empty Map as the parameters but if you want to change stuff like
		// what config files to read, you need to specify them here
		// (see Dispatcher's source code)
		java.net.URL url = ClassLoader.getSystemResource("struts.properties");
	    Properties props = new Properties();
		props.load(url.openStream());
		this.setInitParameters(props);

		Map params = new HashMap(props);
		this._dispatcher = new Dispatcher(_servletContext, params);
		this._dispatcher.init();
		Dispatcher.setInstance(this._dispatcher);
	}

	protected Container createContainer() {
        ContainerBuilder builder = new ContainerBuilder();
        builder.constant("devMode", "false");
        return builder.create(true);
    }

	@Override
	protected void tearDown() throws Exception {
		this.waitThreads(SystemConstants.ENTANDO_THREAD_NAME_PREFIX);
		super.tearDown();
		this.getConfigUtils().closeDataSources(this.getApplicationContext());
	}

	protected void waitNotifyingThread() throws InterruptedException {
		this.waitThreads(NotifyManager.NOTIFYING_THREAD_NAME);
	}

	protected void waitThreads(String threadNamePrefix) throws InterruptedException {
		Thread[] threads = new Thread[20];
	    Thread.enumerate(threads);
	    for (int i=0; i<threads.length; i++) {
	    	Thread currentThread = threads[i];
			if (currentThread != null &&
	    			currentThread.getName().startsWith(threadNamePrefix)) {
	    		currentThread.join();
	    	}
	    }
	}

	/**
	 * Created action class based on namespace and name
	 */
	protected void initAction(String namespace, String name) throws Exception {
		// create a proxy class which is just a wrapper around the action call.
		// The proxy is created by checking the namespace and name against the
		// struts.xml configuration
	    ActionProxyFactory proxyFactory = (ActionProxyFactory) this._dispatcher.getContainer().getInstance(ActionProxyFactory.class);
	    this._proxy = proxyFactory.createActionProxy(namespace, name, null, null, true, false);

		// set to true if you want to process Freemarker or JSP results
		this._proxy.setExecuteResult(false);
		// by default, don't pass in any request parameters
		this._proxy.getInvocation().getInvocationContext().setParameters(new HashMap());

		// set the actions context to the one which the proxy is using
		ServletActionContext.setContext(_proxy.getInvocation().getInvocationContext());
		ServletActionContext.setRequest(_request);
		ServletActionContext.setResponse(_response);
		ServletActionContext.setServletContext(_servletContext);
		this._action = (ActionSupport) this._proxy.getAction();

		//reset previsious params
		List<String> paramNames = new ArrayList<String>(this._request.getParameterMap().keySet());
		for (int i=0; i<paramNames.size(); i++) {
			String paramName = (String) paramNames.get(i);
			this.removeParameter(paramName);
		}
	}

    /**
     * Metodo da estendere in caso che si voglia impiantare un'altro struts-config.
     */
    protected void setInitParameters(Properties params) {
    	params.setProperty("config",
    			"struts-default.xml,struts-plugin.xml,struts.xml,entando-struts-plugin.xml,japs-struts-plugin.xml");
    }

    /**
     * Return a user (with his autority) by username.
     * @param username The username
     * @param password The password
     * @return The required user.
     * @throws Exception In case of error.
     */
    protected UserDetails getUser(String username, String password) throws Exception {
		IAuthenticationProviderManager provider = (IAuthenticationProviderManager) this.getService(SystemConstants.AUTHENTICATION_PROVIDER_MANAGER);
		IUserManager userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
		UserDetails user = null;
		if (username.equals(SystemConstants.GUEST_USER_NAME)) {
			user = userManager.getGuestUser();
		} else {
			user = provider.getUser(username, password);
		}
		return user;
    }

    /**
     * Return a user (with his autority) by username, with the password equals than username.
     * @param username The username
     * @return The required user.
     * @throws Exception In case of error.
     */
    protected UserDetails getUser(String username) throws Exception {
		return this.getUser(username, username);
    }

	protected void setUserOnSession(String username) throws Exception {
		if (null == username) {
			this.removeUserOnSession();
			return;
		}
		UserDetails currentUser = this.getUser(username, username);//nel database di test, username e password sono uguali
		HttpSession session = this._request.getSession();
		session.setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, currentUser);
    }

	protected void removeUserOnSession() throws Exception {
    	HttpSession session = this._request.getSession();
		session.removeAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
    }

	protected void addParameters(Map params) {
		Iterator iter = params.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			this.addParameter(key, params.get(key).toString());
		}
	}

	protected void addParameter(String name, Object value) {
		this._request.removeParameter(name);
		if (null == value) return;
		this._request.addParameter(name, value.toString());
		Map parameters = this._proxy.getInvocation().getInvocationContext().getParameters();
		parameters.put(name, value);
	}

	protected void addAttribute(String name, Object value) {
		this._request.removeAttribute(name);
		if (null == value) return;
		this._request.setAttribute(name, value);
	}

	private void removeParameter(String name) {
		this._request.removeParameter(name);
		this._request.removeAttribute(name);
		Map parameters = this._proxy.getInvocation().getInvocationContext().getParameters();
		parameters.remove(name);
	}

	protected String executeAction() throws Throwable {
		String result = this._proxy.execute();
		return result;
	}

	protected IManager getService(String name) {
		return (IManager) this.getApplicationContext().getBean(name);
	}

    protected ApplicationContext getApplicationContext() {
    	return _applicationContext;
    }

    protected ConfigTestUtils getConfigUtils() {
		return new ConfigTestUtils();
	}

    protected ActionSupport getAction() {
    	return this._action;
    }

    protected HttpServletRequest getRequest() {
    	return this._request;
    }

    private static ApplicationContext _applicationContext;
    private Dispatcher _dispatcher;
	private ActionProxy _proxy;
	private static MockServletContext _servletContext;
	private MockHttpServletRequest _request;
	private MockHttpServletResponse _response;
	private ActionSupport _action;

}