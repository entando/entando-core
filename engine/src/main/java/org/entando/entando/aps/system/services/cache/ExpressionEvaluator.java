/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.entando.entando.aps.system.services.cache;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.aop.support.AopUtils;
import org.springframework.cache.Cache;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * Utility class handling the SpEL expression parsing.
 * Meant to be used as a reusable, thread-safe component.
 *
 * <p>Performs internal caching for performance reasons.
 * 
 * The private class CacheExpressionRootObject describing the root object used during the expression evaluation.
 *
 * Evaluation context class that adds a method parameters as SpEL
 * variables, in a lazy manner. The lazy nature eliminates unneeded
 * parsing of classes byte code for parameter discovery.
 * 
 * @author Costin Leau
 * @author Phillip Webb
 * @since 3.1
 */
class ExpressionEvaluator {

	public static final Object NO_RESULT = new Object();

	private final SpelExpressionParser parser = new SpelExpressionParser();

	// shared param discoverer since it caches data internally
	private final ParameterNameDiscoverer paramNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
	
	private final Map<String, Method> targetMethodCache = new ConcurrentHashMap<String, Method>(64);
	
	private final Map<String, Expression> _expressions = new ConcurrentHashMap<String, Expression>(64);
	
	/**
	 * Create an {@link EvaluationContext}.
	 *
	 * @param caches the current caches
	 * @param method the method
	 * @param args the method arguments
	 * @param target the target object
	 * @param targetClass the target class
	 * @param result the return value (can be {@code null}) or
	 *        {@link #NO_RESULT} if there is no return at this time
	 * @return the evalulation context
	 */
	public EvaluationContext createEvaluationContext(Collection<Cache> caches,
			Method method, Object[] args, Object target, Class<?> targetClass,
			final Object result) {
		CacheExpressionRootObject rootObject = new CacheExpressionRootObject(caches,
				method, args, target, targetClass);
		LazyParamAwareEvaluationContext evaluationContext = new LazyParamAwareEvaluationContext(rootObject,
				this.paramNameDiscoverer, method, args, targetClass, this.targetMethodCache);
		if (result != NO_RESULT) {
			evaluationContext.setVariable("result", result);
		}
		return evaluationContext;
	}
	
	public Object evaluateExpression(String keyExpression, Method method, EvaluationContext evalContext) {
		return getExpression(keyExpression, method).getValue(evalContext);
	}
	
	private Expression getExpression(String expression, Method method) {
		String key = toString(method, expression);
		Expression rtn = this._expressions.get(key);
		if (rtn == null) {
			rtn = this.parser.parseExpression(expression);
			this._expressions.put(key, rtn);
		}
		return rtn;
	}

	private String toString(Method method, String expression) {
		StringBuilder sb = new StringBuilder();
		sb.append(method.getDeclaringClass().getName());
		sb.append("#");
		sb.append(method.toString());
		sb.append("#");
		sb.append(expression);
		return sb.toString();
	}
	
	private class CacheExpressionRootObject {
		
		private final Collection<Cache> caches;
		private final Method method;
		private final Object[] args;
		private final Object target;
		private final Class<?> targetClass;
		
		public CacheExpressionRootObject(
				Collection<Cache> caches, Method method, Object[] args, Object target, Class<?> targetClass) {
			Assert.notNull(method, "Method is required");
			Assert.notNull(targetClass, "targetClass is required");
			this.method = method;
			this.target = target;
			this.targetClass = targetClass;
			this.args = args;
			this.caches = caches;
		}
		
		public Collection<Cache> getCaches() {
			return this.caches;
		}
		
		public Method getMethod() {
			return this.method;
		}
		
		public String getMethodName() {
			return this.method.getName();
		}
		
		public Object[] getArgs() {
			return this.args;
		}
		
		public Object getTarget() {
			return this.target;
		}
		
		public Class<?> getTargetClass() {
			return this.targetClass;
		}
		
	}
	
	private class LazyParamAwareEvaluationContext extends StandardEvaluationContext {
		
		private final ParameterNameDiscoverer paramDiscoverer;
		private final Method method;
		private final Object[] args;
		private final Class<?> targetClass;
		private final Map<String, Method> methodCache;
		private boolean paramLoaded = false;
		
		LazyParamAwareEvaluationContext(Object rootObject, ParameterNameDiscoverer paramDiscoverer, Method method,
				Object[] args, Class<?> targetClass, Map<String, Method> methodCache) {
			super(rootObject);
			this.paramDiscoverer = paramDiscoverer;
			this.method = method;
			this.args = args;
			this.targetClass = targetClass;
			this.methodCache = methodCache;
		}

		/**
		 * Load the param information only when needed.
		 */
		@Override
		public Object lookupVariable(String name) {
			Object variable = super.lookupVariable(name);
			if (variable != null) {
				return variable;
			}
			if (!this.paramLoaded) {
				loadArgsAsVariables();
				this.paramLoaded = true;
				variable = super.lookupVariable(name);
			}
			return variable;
		}

		private void loadArgsAsVariables() {
			// shortcut if no args need to be loaded
			if (ObjectUtils.isEmpty(this.args)) {
				return;
			}
			String mKey = toString(this.method);
			Method targetMethod = this.methodCache.get(mKey);
			if (targetMethod == null) {
				targetMethod = AopUtils.getMostSpecificMethod(this.method, this.targetClass);
				if (targetMethod == null) {
					targetMethod = this.method;
				}
				this.methodCache.put(mKey, targetMethod);
			}
			// save arguments as indexed variables
			for (int i = 0; i < this.args.length; i++) {
				setVariable("a" + i, this.args[i]);
				setVariable("p" + i, this.args[i]);
			}
			String[] parameterNames = this.paramDiscoverer.getParameterNames(targetMethod);
			// save parameter names (if discovered)
			if (parameterNames != null) {
				for (int i = 0; i < parameterNames.length; i++) {
					setVariable(parameterNames[i], this.args[i]);
				}
			}
		}
		
		private String toString(Method m) {
			StringBuilder sb = new StringBuilder();
			sb.append(m.getDeclaringClass().getName());
			sb.append("#");
			sb.append(m.toString());
			return sb.toString();
		}
		
	}
	
}