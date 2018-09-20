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

import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import static junit.framework.TestCase.assertEquals;
import org.apache.struts2.dispatcher.HttpParameters;
import org.apache.struts2.util.TokenHelper;

/**
 * @author E.Santoboni
 */
public class TestCustomTokenInterceptor extends ApsAdminBaseTestCase {

    public void testExecuteValidation_1() throws Exception {
        ActionInvocation invocation = this.prepareAction();
        CustomTokenInterceptor interceptor = new CustomTokenInterceptor();
        String result = interceptor.intercept(invocation);
        assertEquals(Action.SUCCESS, result);
        String newResult = interceptor.intercept(invocation);
        assertEquals(CustomTokenInterceptor.INVALID_TOKEN_CODE, newResult);
        ActionSupport action = super.getAction();
        assertEquals(0, action.getActionErrors().size());
        assertEquals(0, action.getActionMessages().size());
    }

    public void testExecuteValidation_2() throws Exception {
        ActionInvocation invocation = this.prepareAction();
        CustomTokenInterceptor interceptor = new CustomTokenInterceptor();
        interceptor.setTypeMessages(CustomTokenInterceptor.TYPE_RETURN_ACTION_ERROR_MESSAGE);
        String result = interceptor.intercept(invocation);
        assertEquals(Action.SUCCESS, result);
        String newResult = interceptor.intercept(invocation);
        assertEquals(CustomTokenInterceptor.INVALID_TOKEN_CODE, newResult);
        ActionSupport action = super.getAction();
        assertEquals(1, action.getActionErrors().size());
        assertEquals(0, action.getActionMessages().size());
    }

    public void testExecuteValidation_3() throws Exception {
        ActionInvocation invocation = this.prepareAction();
        CustomTokenInterceptor interceptor = new CustomTokenInterceptor();
        interceptor.setTypeMessages(CustomTokenInterceptor.TYPE_RETURN_ACTION_MESSAGE);
        String result = interceptor.intercept(invocation);
        assertEquals(Action.SUCCESS, result);
        String newResult = interceptor.intercept(invocation);
        assertEquals(CustomTokenInterceptor.INVALID_TOKEN_CODE, newResult);
        ActionSupport action = super.getAction();
        assertEquals(0, action.getActionErrors().size());
        assertEquals(1, action.getActionMessages().size());
    }

    protected ActionInvocation prepareAction() throws Exception {
        this.setUserOnSession("admin");
        this.initAction("/do/User", "list");
        String token = TokenHelper.setToken();
        super.addParameter(TokenHelper.TOKEN_NAME_FIELD, new String[]{TokenHelper.DEFAULT_TOKEN_NAME});
        super.addParameter(TokenHelper.DEFAULT_TOKEN_NAME, new String[]{token});
        this.getActionContext().setParameters(HttpParameters.create(this.getRequest().getParameterMap()).build());
        return super.getActionInvocation();
    }

}
