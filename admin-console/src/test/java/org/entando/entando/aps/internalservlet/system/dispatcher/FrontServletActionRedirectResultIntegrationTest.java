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
package org.entando.entando.aps.internalservlet.system.dispatcher;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.struts2.views.util.UrlHelper;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author E.Santoboni
 */
public class FrontServletActionRedirectResultIntegrationTest extends ApsAdminBaseTestCase {

    private ConfigInterface configManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
        IPageManager pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
        ILangManager langManager = (ILangManager) this.getService(SystemConstants.LANGUAGE_MANAGER);
        RequestContext reqCtx = (RequestContext) super.getRequest().getAttribute(RequestContext.REQCTX);
        reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE, pageManager.getOnlineRoot());
        reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG, langManager.getDefaultLang());
        reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME, 0);
        ((MockHttpServletRequest) super.getRequest()).setScheme("https");
        ((MockHttpServletRequest) super.getRequest()).setServerName("www.myproduct.com");
        ((MockHttpServletRequest) super.getRequest()).addHeader("Host", "www.myproduct.com");
        ((MockHttpServletRequest) super.getRequest()).setContextPath("/entando");
    }

    public void testExecuteServlet_1() throws Throwable {
        this.executeServlet("https", "www.myproduct.com", "/entando/it/homepage.page");
    }

    public void testExecuteServlet_2() throws Throwable {
        this.configManager.updateParam(SystemConstants.CONFIG_PARAM_BASE_URL, SystemConstants.CONFIG_PARAM_BASE_URL_RELATIVE);
        try {
            this.executeServlet("http", "www.entando.com", "/Entando/it/homepage.page");
        } catch (Exception e) {
            throw e;
        } finally {
            this.configManager.updateParam(SystemConstants.CONFIG_PARAM_BASE_URL, SystemConstants.CONFIG_PARAM_BASE_URL_FROM_REQUEST);
        }
    }

    public void testExecuteServlet_3() throws Throwable {
        this.configManager.updateParam(SystemConstants.CONFIG_PARAM_BASE_URL, SystemConstants.CONFIG_PARAM_BASE_URL_STATIC);
        try {
            this.executeServlet("http", "www.entando.com", "/Entando/it/homepage.page");
        } catch (Exception e) {
            throw e;
        } finally {
            this.configManager.updateParam(SystemConstants.CONFIG_PARAM_BASE_URL, SystemConstants.CONFIG_PARAM_BASE_URL_FROM_REQUEST);
        }
    }

    private void executeServlet(String expectedProtocol, String expectedAuthority, String expectedPath) throws Exception {
        UrlHelper urlHelper = this.getContainerObject(UrlHelper.class);
        this.initAction("/do/Api/Service", "list");
        FrontServletActionRedirectResult servlet = new FrontServletActionRedirectResult();
        servlet.setActionName("details");
        servlet.setUrlHelper(urlHelper);
        servlet.execute(super.getActionContext().getActionInvocation());
        URL aURL = new URL(servlet.getLocation());
        assertEquals(expectedProtocol, aURL.getProtocol());
        assertEquals(expectedAuthority, aURL.getAuthority());
        assertEquals(expectedPath, aURL.getPath());
        List<NameValuePair> params = URLEncodedUtils.parse(aURL.getQuery(), Charset.forName("UTF-8"));
        for (NameValuePair param : params) {
            System.out.println(param.getName() + " : " + param.getValue());
            if (param.getName().equals("internalServletActionPath")) {
                assertEquals("/ExtStr2/do/Api/Service/details", param.getValue());
            } else if (param.getName().equals("internalServletFrameDest")) {
                assertEquals("0", param.getValue());
            } else {
                fail();
            }
        }
    }

}
