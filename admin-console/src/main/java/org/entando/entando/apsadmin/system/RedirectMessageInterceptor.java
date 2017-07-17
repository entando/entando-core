/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.entando.entando.apsadmin.system;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.opensymphony.xwork2.interceptor.ValidationAware;
import org.apache.struts2.result.ServletActionRedirectResult;
import org.apache.struts2.result.ServletRedirectResult;
import org.entando.entando.aps.internalservlet.system.dispatcher.FrontServletActionRedirectResult;

/**
 * @author  http://glindholm.wordpress.com/2008/07/02/preserving-messages-across-a-redirect-in-struts-2/
 * 
 * An Interceptor to preserve an actions ValidationAware messages across a
 * redirect result.
 *
 * It makes the assumption that you always want to preserve messages across a
 * redirect and restore them to the next action if they exist.
 *
 * The way this works is it looks at the result type after a action has executed
 * and if the result was a redirect (ServletRedirectResult) or a redirectAction
 * (ServletActionRedirectResult) and there were any errors, messages, or
 * fieldErrors they are stored in the session. Before the next action executes
 * it will check if there are any messages stored in the session and add them to
 * the next action.
 * 
 * The action you are redirecting towards will need to configure a result with name="input" as the added messages will trigger the "workflow" interceptor to return a result of "input"
 *
 */
public class RedirectMessageInterceptor extends MethodFilterInterceptor
{
	private static final long  serialVersionUID    = -1847557437429753540L;

	public static final String FIELD_ERRORS_KEY    = "RedirectMessageInterceptor_FieldErrors";
	public static final String ACTION_ERRORS_KEY   = "RedirectMessageInterceptor_ActionErrors";
	public static final String ACTION_MESSAGES_KEY = "RedirectMessageInterceptor_ActionMessages";

	public RedirectMessageInterceptor() {}
	
	@Override
	public String doIntercept(ActionInvocation invocation) throws Exception {
		Object action = invocation.getAction();
		if (action instanceof ValidationAware) {
			this.before(invocation, (ValidationAware) action);
		}
		String result = invocation.invoke();
		if (action instanceof ValidationAware) {
			after(invocation, (ValidationAware) action);
		}
		return result;
	}

	/**
	 * Retrieve the errors and messages from the session and add them to the
	 * action.
	 * @param invocation
	 * @param validationAware
	 * @throws java.lang.Exception
	 */
	protected void before(ActionInvocation invocation, ValidationAware validationAware) throws Exception {
		Map<String, ?> session = invocation.getInvocationContext().getSession();
		if (session!=null) {
			Collection<String> actionErrors = (Collection) session.remove(ACTION_ERRORS_KEY);
			if (actionErrors != null && actionErrors.size() > 0) {
				for (String error : actionErrors) {
					validationAware.addActionError(error);
				}
			}
			Collection<String> actionMessages = (Collection) session.remove(ACTION_MESSAGES_KEY);
			if (actionMessages != null && actionMessages.size() > 0) {
				for (String message : actionMessages) {
					validationAware.addActionMessage(message);
				}
			}
			Map<String, List<String>> fieldErrors = (Map) session.remove(FIELD_ERRORS_KEY);
			if (fieldErrors != null && fieldErrors.size() > 0) {
				for (Map.Entry<String, List<String>> fieldError : fieldErrors.entrySet()) {
					for (String message : fieldError.getValue()) {
						validationAware.addFieldError(fieldError.getKey(), message);
					}
				}
			}
		}
	}

	/**
	 * If the result is a redirect then store error and messages in the session.
	 * @param invocation
	 * @param validationAware
	 * @throws java.lang.Exception
	 */
	protected void after(ActionInvocation invocation, ValidationAware validationAware) throws Exception {
		Result result = invocation.getResult();
		if (result != null && (result instanceof ServletRedirectResult || result instanceof ServletActionRedirectResult || result instanceof FrontServletActionRedirectResult)) {
			HttpServletRequest request = (HttpServletRequest) invocation.getInvocationContext().get(ServletActionContext.HTTP_REQUEST);
			HttpSession session = request.getSession();
			
			Collection<String> actionErrors = validationAware.getActionErrors();
			if (actionErrors != null && actionErrors.size() > 0) {
				session.setAttribute(ACTION_ERRORS_KEY, actionErrors);
			}
			Collection<String> actionMessages = validationAware.getActionMessages();
			if (actionMessages != null && actionMessages.size() > 0) {
				session.setAttribute(ACTION_MESSAGES_KEY, actionMessages);
			}
			Map<String, List<String>> fieldErrors = validationAware.getFieldErrors();
			if (fieldErrors != null && fieldErrors.size() > 0) {
				session.setAttribute(FIELD_ERRORS_KEY, fieldErrors);
			}
		}
	}
}