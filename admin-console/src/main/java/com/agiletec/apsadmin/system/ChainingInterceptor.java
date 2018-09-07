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
package com.agiletec.apsadmin.system;

import com.opensymphony.xwork2.Unchainable;
import com.opensymphony.xwork2.util.TextParseUtil;
import com.opensymphony.xwork2.util.reflection.ReflectionProvider;

import java.util.Set;

/**
 * <!-- START SNIPPET: description -->
 * <p>
 * An interceptor that copies all the properties of every object in the value stack to the currently executing object,
 * except for any object that implements {@link Unchainable}. A collection of optional <i>includes</i> and
 * <i>excludes</i> may be provided to control how and which parameters are copied. Only includes or excludes may be
 * specified. Specifying both results in undefined behavior. See the javadocs for {@link ReflectionProvider#copy(Object, Object,
 * java.util.Map, java.util.Collection, java.util.Collection)} for more information.
 * </p>
 *
 * <p>
 * <b>Note:</b> It is important to remember that this interceptor does nothing if there are no objects already on the stack.
 * <br>This means two things:
 * <br><b>One</b>, you can safely apply it to all your actions without any worry of adverse affects.
 * <br><b>Two</b>, it is up to you to ensure an object exists in the stack prior to invoking this action. The most typical way this is done
 * is through the use of the <b>chain</b> result type, which combines with this interceptor to make up the action
 * chaining feature.
 * </p>
 *
 * <p>
 * <b>Note:</b> By default Errors, Field errors and Message aren't copied during chaining, to change the behaviour you can specify
 * the below three constants in struts.properties or struts.xml:
 * </p>
 *
 * <ul>
 * <li>struts.xwork.chaining.copyErrors - set to true to copy Action Errors</li>
 * <li>struts.xwork.chaining.copyFieldErrors - set to true to copy Field Errors</li>
 * <li>struts.xwork.chaining.copyMessages - set to true to copy Action Messages</li>
 * </ul>
 *
 * <p>
 * <u>Example:</u>
 * </p>
 *
 * <pre>
 * &lt;constant name="struts.xwork.chaining.copyErrors" value="true"/&gt;
 * </pre>
 *
 * <p>
 * <b>Note:</b> By default actionErrors and actionMessages are excluded when copping object's properties.
 * </p>
 * <!-- END SNIPPET: description -->
 * <u>Interceptor parameters:</u>
 * <!-- START SNIPPET: parameters -->
 * <ul>
 * <li>excludes (optional) - the list of parameter names to exclude from copying (all others will be included).</li>
 * <li>includes (optional) - the list of parameter names to include when copying (all others will be excluded).</li>
 * </ul>
 * <!-- END SNIPPET: parameters -->
 * <u>Extending the interceptor:</u>
 * <!-- START SNIPPET: extending -->
 * <p>
 * There are no known extension points to this interceptor.
 * </p>
 * <!-- END SNIPPET: extending -->
 * <u>Example code:</u>
 *
 * <!-- START SNIPPET: example -->
 * <pre>
 * &lt;action name="someAction" class="com.examples.SomeAction"&gt;
 *     &lt;interceptor-ref name="basicStack"/&gt;
 *     &lt;result name="success" type="chain"&gt;otherAction&lt;/result&gt;
 * &lt;/action&gt;
 * </pre>
 *
 * <pre>
 * &lt;action name="otherAction" class="com.examples.OtherAction"&gt;
 *     &lt;interceptor-ref name="chain"/&gt;
 *     &lt;interceptor-ref name="basicStack"/&gt;
 *     &lt;result name="success"&gt;good_result.ftl&lt;/result&gt;
 * &lt;/action&gt;
 * </pre>
 * <!-- END SNIPPET: example -->
 *
 *
 * @author mrdon
 * @author tm_jee ( tm_jee(at)yahoo.co.uk )
 * @author E.Santoboni
 * @see com.opensymphony.xwork2.ActionChainResult
 */
public class ChainingInterceptor extends com.opensymphony.xwork2.interceptor.ChainingInterceptor {
	
    public void setExcludeParameters(String parametersToExclude) {
		Set<String> parameters = TextParseUtil.commaDelimitedStringToSet(parametersToExclude);
		if (parameters.contains("fieldErrors")) {
			super.setCopyFieldErrors(Boolean.FALSE.toString());
		}
		if (parameters.contains("actionErrors")) {
			super.setCopyErrors(Boolean.FALSE.toString());
		}
		if (parameters.contains("actionMessages")) {
			super.setCopyMessages(Boolean.FALSE.toString());
		}
		super.setExcludesCollection(parameters);
	}
	
	public void setIncludeParameters(String parametersToInclude) {
		Set<String> parameters = TextParseUtil.commaDelimitedStringToSet(parametersToInclude);
		if (parameters.contains("fieldErrors")) {
			super.setCopyFieldErrors(Boolean.TRUE.toString());
		}
		if (parameters.contains("actionErrors")) {
			super.setCopyErrors(Boolean.TRUE.toString());
		}
		if (parameters.contains("actionMessages")) {
			super.setCopyMessages(Boolean.TRUE.toString());
		}
		super.setIncludesCollection(parameters);
	}
	
}