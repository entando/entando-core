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
package org.entando.entando.aps.internalservlet.system.dispatcher;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.tags.InternalServletTag;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.config.entities.ResultConfig;
import com.opensymphony.xwork2.util.reflection.ReflectionExceptionHandler;

/**
 * Redirect Action Result with ancot for internal servlet actions.
 * @author E.Santoboni
 */
public class FrontServletActionRedirectResult extends ServletRedirectResult implements ReflectionExceptionHandler {

	private static final Logger _logger = LoggerFactory.getLogger(FrontServletActionRedirectResult.class);
	
    protected String _actionName;
    protected String _namespace;
    protected String _method;
    protected String _anchorDest;

    public FrontServletActionRedirectResult() {
        super();
    }
    
    public FrontServletActionRedirectResult(String actionName) {
        this(null, actionName, null, null);
    }

    public FrontServletActionRedirectResult(String actionName, String method) {
        this(null, actionName, method, null);
    }

    public FrontServletActionRedirectResult(String namespace, String actionName, String method) {
        this(namespace, actionName, method, null);
    }

    public FrontServletActionRedirectResult(String namespace, String actionName, String method, String anchor) {
        super(null, anchor);
        this._namespace = namespace;
        this._actionName = actionName;
        this._method = method;
    }
    
	@Override
    public void execute(ActionInvocation invocation) throws Exception {
        try {
            this._actionName = this.conditionalParse(this._actionName, invocation);
            if (this._namespace == null) {
                this._namespace = invocation.getProxy().getNamespace();
            } else {
                this._namespace = this.conditionalParse(this._namespace, invocation);
            }
            if (this._method == null) {
                this._method = "";
            } else {
                this._method = this.conditionalParse(this._method, invocation);
            }
            String anchorDest = null;
            Map<String, String> redirectParams = new HashMap<String, String>();
            ResultConfig resultConfig = invocation.getProxy().getConfig().getResults().get(invocation.getResultCode());
            if (resultConfig != null) {
                this.extractResultParams(redirectParams, resultConfig, invocation);
                anchorDest = this.extractAnchorDest(resultConfig, invocation);
            }
            HttpServletRequest request = ServletActionContext.getRequest();
            RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
            this.extractInternalServletParams(redirectParams, reqCtx);
            IURLManager urlManager = (IURLManager) ApsWebApplicationUtils.getBean(SystemConstants.URL_MANAGER, request);
            Page currentPage = (Page) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
            Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
            String url = urlManager.createUrl(currentPage, currentLang, redirectParams, false, request);
            if (null != anchorDest) {
                url += "#" + anchorDest;
            }
            this.setLocation(url);
        } catch (Throwable t) {
        	_logger.error("error in execute", t);
        }
        super.execute(invocation);
    }

    protected void extractResultParams(Map<String, String> redirectParams, ResultConfig resultConfig, ActionInvocation invocation) {
        Map resultConfigParams = resultConfig.getParams();
        for (Iterator i = resultConfigParams.entrySet().iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry) i.next();
            if (!this.getProhibitedResultParams().contains(e.getKey())) {
                String potentialValue = e.getValue() == null ? "" : conditionalParse(e.getValue().toString(), invocation);
				if (!suppressEmptyParameters || ((potentialValue != null) && (potentialValue.length() > 0))) {
                    redirectParams.put(e.getKey().toString(), potentialValue);
                }
            }
        }
    }

    protected String extractAnchorDest(ResultConfig resultConfig, ActionInvocation invocation) {
        Map<String, String> resultConfigParams = resultConfig.getParams();
        for (Iterator i = resultConfigParams.entrySet().iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry) i.next();
            if (e.getKey().equals("anchorDest")) {
                String potentialValue = e.getValue() == null ? "" : conditionalParse(e.getValue().toString(), invocation);
                if (potentialValue != null && potentialValue.length() > 0) {
                    return potentialValue;
                }
            }
        }
        return null;
    }

    protected void extractInternalServletParams(Map<String, String> redirectParams, RequestContext reqCtx) {
        String actionPath = "/ExtStr2" + this._namespace + "/" + this._actionName;
        Integer currentFrame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
        redirectParams.put(InternalServletTag.REQUEST_PARAM_ACTIONPATH, actionPath);
        redirectParams.put(InternalServletTag.REQUEST_PARAM_FRAMEDEST, currentFrame.toString());
    }

    /**
     * Sets the action name
     * @param actionName The name
     */
    public void setActionName(String actionName) {
        this._actionName = actionName;
    }

    /**
     * Sets the namespace
     * @param namespace The namespace
     */
    public void setNamespace(String namespace) {
        this._namespace = namespace;
    }

    /**
     * Sets the method
     * @param method The method
     */
    public void setMethod(String method) {
        this._method = method;
    }

    protected String getAnchorDest() {
        return _anchorDest;
    }
	
    /**
     * Sets the anchor destination
     * @param anchorDest The anchor destination
     */
    public void setAnchorDest(String anchorDest) {
        this._anchorDest = anchorDest;
    }
    
	@Override
    protected List<String> getProhibitedResultParams() {
        return Arrays.asList(new String[]{
                    DEFAULT_PARAM, "namespace", "method", "encode", "parse", "location",
                    "prependServletContext", "supressEmptyParameters", "anchor", "anchorDest"});
    }
    
}
