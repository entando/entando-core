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
package com.agiletec.apsadmin.system.dispatcher;

import java.util.Arrays;
import java.util.List;

import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.apache.struts2.dispatcher.mapper.ActionMapper;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.opensymphony.xwork2.util.reflection.ReflectionExceptionHandler;

/**
 * Redirect Action Result con ancora.
 * Questo resultType è utilizzato per permettere il redirezionamento ad una url che invoca una specifica action 
 * (opzionalmente un namespace) con una specifica ancora.
 *
 * See examples below for an example of how request parameters could be passed in.
 *
 * <!-- END SNIPPET: description -->
 *
 * <b>This result type takes the following parameters:</b>
 *
 * <!-- START SNIPPET: params -->
 *
 * <ul>
 *
 * <li><b>actionName (default)</b> - the name of the action that will be redirect to</li>
 *
 * <li><b>namespace</b> - used to determine which namespace the action is in that we're redirecting to . If namespace is
 * null, this defaults to the current namespace</li>
 * 
 * <li><b>anchorDest</b> - the name of the anchor</li>
 *
 * <li><b>supressEmptyParameters</b> - optional boolean (defaults to false) that can prevent parameters with no values
 * from being included in the redirect URL.</li>
 *
 * </ul>
 *
 * <!-- END SNIPPET: params -->
 *
 * <b>Example:</b>
 *
 * <pre><!-- START SNIPPET: example -->
 * &lt;package name="public" extends="struts-default"&gt;
 *     &lt;action name="login" class="..."&gt;
 *         &lt;!-- Redirect to another namespace --&gt;
 *         &lt;result type="redirectActionWithAnchor"&gt;
 *             &lt;param name="actionName"&gt;dashboard&lt;/param&gt;
 *             &lt;param name="namespace"&gt;/secure&lt;/param&gt;
 *             &lt;param name="anchorDest"&gt;lang_it/param&gt;
 *         &lt;/result&gt;
 *     &lt;/action&gt;
 * &lt;/package&gt;
 *
 * <!-- END SNIPPET: example --></pre>
 *
 * @see ActionMapper
 */
public class ServletActionRedirectResultWithAnchor extends ServletRedirectResult implements ReflectionExceptionHandler {
	
	private static final long serialVersionUID = 5004297536492031384L;
	
	/** The default parameter */
    public static final String DEFAULT_PARAM = "actionName";

    private static final Logger LOG = LoggerFactory.getLogger(ServletActionRedirectResultWithAnchor.class);
    
    protected String actionName;
    protected String namespace;
    protected String method;
    protected String _anchorDest;

    public ServletActionRedirectResultWithAnchor() {
        super();
    }

    public ServletActionRedirectResultWithAnchor(String actionName) {
        this(null, actionName, null, null);
    }

    public ServletActionRedirectResultWithAnchor(String actionName, String method) {
        this(null, actionName, method, null);
    }


    public ServletActionRedirectResultWithAnchor(String namespace, String actionName, String method) {
        this(namespace, actionName, method, null);
    }

    public ServletActionRedirectResultWithAnchor(String namespace, String actionName, String method, String anchor) {
        super(null, anchor);
        this.namespace = namespace;
        this.actionName = actionName;
        this.method = method;
    }
    
    /**
     * @see com.opensymphony.xwork2.Result#execute(com.opensymphony.xwork2.ActionInvocation)
     */
    @Override
    public void execute(ActionInvocation invocation) throws Exception {
    	this.actionName = this.conditionalParse(this.actionName, invocation);
        if (this.namespace == null) {
        	this.namespace = invocation.getProxy().getNamespace();
        } else {
        	this.namespace = this.conditionalParse(this.namespace, invocation);
        }
        if (this.method == null) {
        	this.method = "";
        } else {
        	this.method = this.conditionalParse(this.method, invocation);
        }
        StringBuilder tmpLocation = new StringBuilder(this.actionMapper.getUriFromActionMapping(new ActionMapping(actionName, namespace, method, null)));
        if (null != this.getAnchorDest()) {
        	this.setAnchor(this.getAnchorDest());
        }
        setLocation(tmpLocation.toString());
        super.execute(invocation);
    }
    
    /**
     * Sets the action name
     * @param actionName The name
     */
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
    
    /**
     * Sets the namespace
     * @param namespace The namespace
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    
    /**
     * Sets the method
     * @param method The method
     */
    public void setMethod(String method) {
        this.method = method;
    }
    
    protected String getAnchorDest() {
		return _anchorDest;
	}
    
	/**
     * Sets the anchor destination
     * @param method The anchor destination
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