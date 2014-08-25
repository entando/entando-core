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
package com.agiletec.aps;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;

import com.agiletec.ConfigTestUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.NotifyManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * @author W.Ambu - E.Santoboni
 */
public class BaseTestCase extends TestCase {
	
	@Override
	protected void setUp() throws Exception {
		try {
			super.setUp();
			ServletContext srvCtx = new MockServletContext("", new FileSystemResourceLoader());
			ApplicationContext applicationContext = this.getConfigUtils().createApplicationContext(srvCtx);
			this.setApplicationContext(applicationContext);
			RequestContext reqCtx = this.createRequestContext(applicationContext, srvCtx);
			this.setRequestContext(reqCtx);
			this.setUserOnSession("guest");
		} catch (Exception e) {
			throw e;
		}
	}
	
	protected RequestContext createRequestContext(ApplicationContext applicationContext, ServletContext srvCtx) {
    	RequestContext reqCtx = new RequestContext();
    	srvCtx.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
    	MockHttpServletRequest request = new MockHttpServletRequest();
		request.setAttribute(RequestContext.REQCTX, reqCtx);
    	MockHttpServletResponse response = new MockHttpServletResponse();
		MockHttpSession session = new MockHttpSession(srvCtx);
		request.setSession(session);
		reqCtx.setRequest(request);
		reqCtx.setResponse(response);
		
		ILangManager langManager = (ILangManager) this.getService(SystemConstants.LANGUAGE_MANAGER);
		Lang defaultLang = langManager.getDefaultLang();
		reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG, defaultLang);
		
    	return reqCtx;
    }
	
	@Override
	protected void tearDown() throws Exception {
		this.waitThreads(SystemConstants.ENTANDO_THREAD_NAME_PREFIX);
		super.tearDown();
		this.getConfigUtils().closeDataSources(this.getApplicationContext());
		this.getConfigUtils().destroyContext(this.getApplicationContext());
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
		UserDetails currentUser = this.getUser(username);
		HttpSession session = _reqCtx.getRequest().getSession();
		session.setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, currentUser);
    }
    
	protected RequestContext getRequestContext() {
        return this._reqCtx;
    }
    
	protected void setRequestContext(RequestContext reqCtx) {
        this._reqCtx = reqCtx;
    }
    
	protected IManager getService(String name) {
		return (IManager) this.getApplicationContext().getBean(name);
	}
	
	protected ApplicationContext getApplicationContext() {
    	return this._applicationContext;
    }
    
	protected void setApplicationContext(ApplicationContext applicationContext) {
    	this._applicationContext = applicationContext;
    }
	
	protected ConfigTestUtils getConfigUtils() {
		return new ConfigTestUtils();
	}
    
    private RequestContext _reqCtx;
    
    private ApplicationContext _applicationContext;
    
}