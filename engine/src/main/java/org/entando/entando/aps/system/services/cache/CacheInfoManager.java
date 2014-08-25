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
package org.entando.entando.aps.system.services.cache;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.events.PageChangedEvent;
import com.agiletec.aps.system.services.page.events.PageChangedObserver;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.expression.EvaluationContext;

/**
 * Manager of the System Cache
 * @author E.Santoboni
 */
@Aspect
public class CacheInfoManager extends AbstractService implements ICacheInfoManager, PageChangedObserver {
	
	private static final Logger _logger =  LoggerFactory.getLogger(CacheInfoManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} (cache info service initialized) ready", this.getClass().getName());
	}
	
	@Around("@annotation(cacheableInfo)")
	public Object aroundCacheableMethod(ProceedingJoinPoint pjp, CacheableInfo cacheableInfo) throws Throwable {
		Object result = pjp.proceed();
		if (cacheableInfo.expiresInMinute() < 0 && (cacheableInfo.groups() == null || cacheableInfo.groups().trim().length() == 0)) {
			return result;
		}
		try {
			MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
			Method targetMethod = methodSignature.getMethod();
			Class targetClass = pjp.getTarget().getClass();
			Method effectiveTargetMethod = targetClass.getMethod(targetMethod.getName(), targetMethod.getParameterTypes());
			Cacheable cacheable = effectiveTargetMethod.getAnnotation(Cacheable.class);
			if (null == cacheable) {
				return result;
			}
			String[] cacheNames = cacheable.value();
			Object key = this.evaluateExpression(cacheable.key().toString(), targetMethod, pjp.getArgs(), effectiveTargetMethod, targetClass);
			for (int j = 0; j < cacheNames.length; j++) {
				String cacheName = cacheNames[j];
				if (cacheableInfo.groups() != null && cacheableInfo.groups().trim().length() > 0) {
					Object groupsCsv = this.evaluateExpression(cacheableInfo.groups().toString(), targetMethod, pjp.getArgs(), effectiveTargetMethod, targetClass);
					if (null != groupsCsv && groupsCsv.toString().trim().length() > 0) {
						String[] groups = groupsCsv.toString().split(",");
						this.putInGroup(cacheName, key.toString(), groups);
					}
				}
				if (cacheableInfo.expiresInMinute() > 0) {
					this.setExpirationTime(cacheName, key.toString(), cacheableInfo.expiresInMinute());
				}
			}
		} catch (Throwable t) {
			_logger.error("Error while evaluating cacheableInfo annotation", t);
			throw new ApsSystemException("Error while evaluating cacheableInfo annotation", t);
		}
		return result;
	}
	
	@Around("@annotation(cacheInfoEvict)")
	public Object aroundCacheInfoEvictMethod(ProceedingJoinPoint pjp, CacheInfoEvict cacheInfoEvict) throws Throwable {
		try {
			MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
			Method targetMethod = methodSignature.getMethod();
			Class targetClass = pjp.getTarget().getClass();
			Method effectiveTargetMethod = targetClass.getMethod(targetMethod.getName(), targetMethod.getParameterTypes());
			String[] cacheNames = cacheInfoEvict.value();
			Object groupsCsv = this.evaluateExpression(cacheInfoEvict.groups().toString(), targetMethod, pjp.getArgs(), effectiveTargetMethod, targetClass);
			if (null != groupsCsv && groupsCsv.toString().trim().length() > 0) {
				String[] groups = groupsCsv.toString().split(",");
				for (int i = 0; i < groups.length; i++) {
					String group = groups[i];
					for (int j = 0; j < cacheNames.length; j++) {
						String cacheName = cacheNames[j];
						this.flushGroup(cacheName, group);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error while flushing group", t);
			throw new ApsSystemException("Error while flushing group", t);
		}
		return pjp.proceed();
	}
	
	@Deprecated
	public void setExpirationTime(String key, int expiresInMinute) {
		this.setExpirationTime(DEFAULT_CACHE_NAME, key, expiresInMinute);
	}
	
	public void setExpirationTime(String targhetCache, String key, int expiresInMinute) {
		Date expirationTime = DateUtils.addMinutes(new Date(), expiresInMinute);
		this.setExpirationTime(targhetCache, key, expirationTime);
	}
	
	@Deprecated
	public void setExpirationTime(String key, long expiresInSeconds) {
		this.setExpirationTime(DEFAULT_CACHE_NAME, key, expiresInSeconds);
	}
	
	public void setExpirationTime(String targhetCache, String key, long expiresInSeconds) {
		Date expirationTime = DateUtils.addSeconds(new Date(), (int) expiresInSeconds);
		this.setExpirationTime(targhetCache, key, expirationTime);
	}
	
	public void setExpirationTime(String targhetCache, String key, Date expirationTime) {
		Map<String, Date> expirationTimes = _objectExpirationTimes.get(targhetCache);
		if (null == expirationTimes) {
			expirationTimes = new HashMap<String, Date>();
			_objectExpirationTimes.put(targhetCache, expirationTimes);
		}
		expirationTimes.put(key.toString(), expirationTime);
	}
	
	@Override
	public void updateFromPageChanged(PageChangedEvent event) {
		IPage page = event.getPage();
		String pageCacheGroupName = SystemConstants.PAGES_CACHE_GROUP_PREFIX + page.getCode();
		this.flushGroup(DEFAULT_CACHE_NAME, pageCacheGroupName);
	}
	
	@Override
	protected void release() {
		super.release();
		this.destroy();
	}
	
	@Override
	public void destroy() {
		this.flushAll();
		super.destroy();
	}
	
	public void flushAll() {
		Collection<String> cacheNames = this.getSpringCacheManager().getCacheNames();
		Iterator<String> iter = cacheNames.iterator();
		while (iter.hasNext()) {
			String cacheName = iter.next();
			this.flushAll(cacheName);
		}
	}
	
	public void flushAll(String cacheName) {
		Cache cache = this.getCache(cacheName);
		cache.clear();
		Map<String, List<String>> objectsByGroup = this._cachesObjectGroups.get(cacheName);
		if (null != objectsByGroup) {
			objectsByGroup.clear();
		}
	}
	
	@Override
	@Deprecated
	public void flushEntry(String key) {
		this.flushEntry(null, key);
	}
	
	@Override
	public void flushEntry(String targhetCache, String key) {
		this.getCache(targhetCache).evict(key);
	}
	
	/**
	 * Put an object on the default cache.
	 * @param key The key
	 * @param obj The object to put into cache.
	 * @deprecated use putInCache(String, String, Object) instead
	 */
	public void putInCache(String key, Object obj) {
		this.putInCache(DEFAULT_CACHE_NAME, key, obj);
	}
	
	/**
	 * Put an object on the given cache.
	 * @param targhetCache The cache name
	 * @param key The key
	 * @param obj The object to put into cache.
	 */
	public void putInCache(String targhetCache, String key, Object obj) {
		Cache cache = this.getCache(targhetCache);
		cache.put(key, obj);
	}
	
	@Deprecated
	public void putInCache(String key, Object obj, String[] groups) {
		this.putInCache(DEFAULT_CACHE_NAME, key, obj, groups);
	}
	
	public void putInCache(String targhetCache, String key, Object obj, String[] groups) {
		Cache cache = this.getCache(targhetCache);
		cache.put(key, obj);
		this.accessOnGroupMapping(targhetCache, 1, groups, key);
	}
	
	@Deprecated
	public Object getFromCache(String key) {
		return getFromCache(DEFAULT_CACHE_NAME, key);
	}
	
	public Object getFromCache(String targhetCache, String key) {
		Cache cache = this.getCache(targhetCache);
		Cache.ValueWrapper element = cache.get(key);
		if (null == element) {
			return null;
		}
		if (isExpired(targhetCache, key)) {
			this.flushEntry(targhetCache, key);
			return null;
		}
		return element.get();
	}
	
	@Deprecated
	public Object getFromCache(String key, int myRefreshPeriod) {
		return this.getFromCache(key);
	}
	
	@Override
	@Deprecated
	public void flushGroup(String group) {
		this.flushGroup(DEFAULT_CACHE_NAME, group);
	}
	
	@Override
	public void flushGroup(String targhetCache, String group) {
		String[] groups = {group};
		this.accessOnGroupMapping(targhetCache, -1, groups, null);
	}
	
	@Override
	@Deprecated
	public void putInGroup(String key, String[] groups) {
		this.accessOnGroupMapping(1, groups, key);
	}
	
	@Override
	public void putInGroup(String targhetCache, String key, String[] groups) {
		this.accessOnGroupMapping(DEFAULT_CACHE_NAME, 1, groups, key);
	}
	
	@Deprecated
	protected synchronized void accessOnGroupMapping(int operationId, String[] groups, String key) {
		this.accessOnGroupMapping(DEFAULT_CACHE_NAME, operationId, groups, key);
	}
	
	protected synchronized void accessOnGroupMapping(String targhetCache, int operationId, String[] groups, String key) {
		Map<String, List<String>> objectsByGroup = this._cachesObjectGroups.get(targhetCache);
		if (operationId>0) {
			//add
			if (null == objectsByGroup) {
				objectsByGroup = new HashMap<String, List<String>>();
				this._cachesObjectGroups.put(targhetCache, objectsByGroup);
			}
			for (int i = 0; i < groups.length; i++) {
				String group = groups[i];
				List<String> objectKeys = objectsByGroup.get(group);
				if (null == objectKeys) {
					objectKeys = new ArrayList<String>();
					objectsByGroup.put(group, objectKeys);
				}
				if (!objectKeys.contains(key)) {
					objectKeys.add(key);
				}
			}
		} else {
			//remove
			if (null == objectsByGroup) {
				return;
			}
			for (int i = 0; i < groups.length; i++) {
				String group = groups[i];
				List<String> objectKeys = objectsByGroup.get(group);
				if (null != objectKeys) {
					for (int j = 0; j < objectKeys.size(); j++) {
						String extractedKey = objectKeys.get(j);
						this.flushEntry(targhetCache, extractedKey);
					}
					objectsByGroup.remove(group);
				}
			}
		}
	}
	
	protected Object evaluateExpression(String expression, Method method, Object[] args, Object target, Class<?> targetClass) {
		Collection<Cache> caches = this.getCaches();
		ExpressionEvaluator evaluator = new ExpressionEvaluator();
		EvaluationContext context = evaluator.createEvaluationContext(caches, 
				method, args, target, targetClass, ExpressionEvaluator.NO_RESULT);
		return evaluator.evaluateExpression(expression.toString(), method, context);
	}
	
	protected Collection<Cache> getCaches() {
		Collection<Cache> caches = new ArrayList<Cache>();
		Iterator<String> iter = this.getSpringCacheManager().getCacheNames().iterator();
		while (iter.hasNext()) {
			String cacheName = iter.next();
			caches.add(this.getSpringCacheManager().getCache(cacheName));
		}
		return caches;
	}
	
	@Deprecated
	public static boolean isNotExpired(String key) {
		return isNotExpired(DEFAULT_CACHE_NAME, key);
	}
	
	public static boolean isNotExpired(String targhetCache, String key) {
		return !isExpired(targhetCache, key);
	}
	
	@Deprecated
	public static boolean isExpired(String key) {
		return isExpired(DEFAULT_CACHE_NAME, key);
	}
	
	public static boolean isExpired(String targhetCache, String key) {
		if (StringUtils.isBlank(targhetCache)) {
			targhetCache = DEFAULT_CACHE_NAME;
		}
		Map<String, Date> expirationTimes = _objectExpirationTimes.get(targhetCache);
		if (null == expirationTimes) {
			return false;
		}
		Date expirationTime = expirationTimes.get(key);
		if (null == expirationTime) {
			return false;
		}
		if (expirationTime.before(new Date())) {
			expirationTimes.remove(key);
			return true;
		} else {
			return false;
		}
	}
	
	protected Cache getCache(String cacheName) {
		if (StringUtils.isBlank(cacheName)) {
			cacheName = DEFAULT_CACHE_NAME;
		}
		return this.getSpringCacheManager().getCache(cacheName);
	}
	
	protected AbstractCacheManager getSpringCacheManager() {
		return _springCacheManager;
	}
	public void setSpringCacheManager(AbstractCacheManager springCacheManager) {
		this._springCacheManager = springCacheManager;
	}
	
	private AbstractCacheManager _springCacheManager;
	
	private Map<String, Map<String, List<String>>> _cachesObjectGroups = new HashMap<String, Map<String, List<String>>>();
	
	private static Map<String, Map<String, Date>> _objectExpirationTimes = new HashMap<String, Map<String, Date>>();
	
}
