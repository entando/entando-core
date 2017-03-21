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


/**
 * <!-- START SNIPPET: description -->
 * <p/>
 * An interceptor that copies all the properties of every object in the value stack to the currently executing object,
 * except for any object that implements {@link Unchainable}. A collection of optional <i>includes</i> and
 * <i>excludes</i> may be provided to control how and which parameters are copied. Only includes or excludes may be
 * specified. Specifying both results in undefined behavior. See the javadocs for {@link ReflectionProvider#copy(Object, Object,
 * java.util.Map, java.util.Collection, java.util.Collection)} for more information.
 * <p/>
 * <p/>
 * <b>Note:</b> It is important to remember that this interceptor does nothing if there are no objects already on the stack.
 * <br/>This means two things:
 * <br/><b>One</b>, you can safely apply it to all your actions without any worry of adverse affects.
 * <br/><b/>Two</b>, it is up to you to ensure an object exists in the stack prior to invoking this action. The most typical way this is done
 * is through the use of the <b>chain</b> result type, which combines with this interceptor to make up the action
 * chaining feature.
 * <p/>
 * <!-- END SNIPPET: description -->
 * <p/>
 * <p/> <u>Interceptor parameters:</u>
 * <p/>
 * <!-- START SNIPPET: parameters -->
 * <p/>
 * <ul>
 * <p/>
 * <li>excludes (optional) - the list of parameter names to exclude from copying (all others will be included).</li>
 * <p/>
 * <li>includes (optional) - the list of parameter names to include when copying (all others will be excluded).</li>
 * <p/>
 * <li>includeParameters (optional) - parameter names (comma delimited string) to include when copying (all others will be excluded).</li>
 * <p/>
 * <li>excludeParameters (optional) - parameter names (comma delimited string) to exclude from copying (all others will be included).</li>
 * <p/>
 * <li>compoundRootMinSize (optional) - min size of chain action classes to exclude or include parameters.</li>
 * <p/>
 * </ul>
 * <p/>
 * <!-- END SNIPPET: parameters -->
 * <p/>
 * <p/> <u>Extending the interceptor:</u>
 * <p/>
 * <p/>
 * <p/>
 * <!-- START SNIPPET: extending -->
 * <p/>
 * There are no known extension points to this interceptor.
 * <p/>
 * <!-- END SNIPPET: extending -->
 * <p/>
 * <p/> <u>Example code:</u>
 * <p/>
 * <pre>
 * <!-- START SNIPPET: example -->
 * <p/>
 * &lt;action name="someAction" class="com.examples.SomeAction"&gt;
 *     &lt;interceptor-ref name="basicStack"/&gt;
 *     &lt;result name="success" type="chain"&gt;otherAction&lt;/result&gt;
 * &lt;/action&gt;
 * <p/>
 * &lt;action name="otherAction" class="com.examples.OtherAction"&gt;
 *     &lt;interceptor-ref name="chain"/&gt;
 *     &lt;interceptor-ref name="basicStack"/&gt;
 *     &lt;result name="success"&gt;good_result.ftl&lt;/result&gt;
 * &lt;/action&gt;
 * <p/>
 * <!-- END SNIPPET: example -->
 * </pre>
 *
 * @author mrdon
 * @author tm_jee ( tm_jee(at)yahoo.co.uk )
 * @author E.Santoboni
 * @see com.opensymphony.xwork2.ActionChainResult
 */
public class ChainingInterceptor extends com.opensymphony.xwork2.interceptor.ChainingInterceptor {
	
    public void setExcludeParameters(String parametersToExclude) {
		super.setExcludes(TextParseUtil.commaDelimitedStringToSet(parametersToExclude));
	}
	
	public void setIncludeParameters(String parametersToInclude) {
		super.setIncludes(TextParseUtil.commaDelimitedStringToSet(parametersToInclude));
	}
	
}