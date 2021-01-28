/*
 * Copyright 2015-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.expression.EvaluationContext;

/**
 * Manager of the System Cache
 *
 * @author E.Santoboni
 */
@Aspect
public class CacheInfoManager extends AbstractService implements ICacheInfoManager, PageChangedObserver {

    private static final Logger logger = LoggerFactory.getLogger(CacheInfoManager.class);

    private CacheManager springCacheManager;

    @Override
    public void init() throws Exception {
        logger.debug("{} (cache info service initialized) ready", this.getClass().getName());
    }

    @Around("@annotation(cacheableInfo)")
    public Object aroundCacheableMethod(ProceedingJoinPoint pjp, CacheableInfo cacheableInfo) throws Throwable {
        boolean skipCacheableInfo = (cacheableInfo.expiresInMinute() < 0 && (StringUtils.isBlank(cacheableInfo.groups())));
        if (skipCacheableInfo) {
            return pjp.proceed();
        }
        Object result = null;
        try {
            MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
            Method targetMethod = methodSignature.getMethod();
            Class targetClass = pjp.getTarget().getClass();
            Method effectiveTargetMethod = targetClass.getMethod(targetMethod.getName(), targetMethod.getParameterTypes());
            Cacheable cacheable = effectiveTargetMethod.getAnnotation(Cacheable.class);
            String keyExpression = null;
            String[] cacheNames = null;
            if (null == cacheable) {
                CachePut cachePut = effectiveTargetMethod.getAnnotation(CachePut.class);
                if (null == cachePut) {
                    return pjp.proceed();
                }
                cacheNames = cachePut.value();
                keyExpression = cachePut.key();
            } else {
                if (!StringUtils.isBlank(cacheable.condition())) {
                    Object isCacheable = this.evaluateExpression(cacheable.condition(), targetMethod, pjp.getArgs(), effectiveTargetMethod, targetClass);
                    Boolean check = Boolean.valueOf(isCacheable.toString());
                    if (null != check && !check) {
                        return pjp.proceed();
                    }
                }
                cacheNames = cacheable.value();
                keyExpression = cacheable.key();
            }
            Object key = this.evaluateExpression(keyExpression, targetMethod, pjp.getArgs(), effectiveTargetMethod, targetClass);
            result = pjp.proceed();
            for (String cacheName : cacheNames) {
                if (cacheableInfo.groups() != null && cacheableInfo.groups().trim().length() > 0) {
                    Object groupsCsv = this.evaluateExpression(cacheableInfo.groups(), targetMethod, pjp.getArgs(), effectiveTargetMethod, targetClass);
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
            logger.error("Error while evaluating cacheableInfo annotation", t);
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
                for (String group : groups) {
                    for (String cacheName : cacheNames) {
                        this.flushGroup(cacheName, group);
                    }
                }
            }
        } catch (Throwable t) {
            logger.error("Error while flushing group", t);
            throw new ApsSystemException("Error while flushing group", t);
        }
        return pjp.proceed();
    }

    public void setExpirationTime(String targetCache, String key, int expiresInMinute) {
        Date expirationTime = DateUtils.addMinutes(new Date(), expiresInMinute);
        this.setExpirationTime(targetCache, key, expirationTime);
    }

    public void setExpirationTime(String targetCache, String key, long expiresInSeconds) {
        Date expirationTime = DateUtils.addSeconds(new Date(), (int) expiresInSeconds);
        this.setExpirationTime(targetCache, key, expirationTime);
    }

    public void setExpirationTime(String targetCache, String key, Date expirationTime) {
        Cache cache = this.getCache(CACHE_INFO_MANAGER_CACHE_NAME);
        Map<String, Date> expirationTimes = this.get(cache, EXPIRATIONS_CACHE_NAME_PREFIX + targetCache, Map.class);
        if (null == expirationTimes) {
            expirationTimes = new HashMap<String, Date>();
        }
        expirationTimes.put(key, expirationTime);
        cache.put(EXPIRATIONS_CACHE_NAME_PREFIX + targetCache, expirationTimes);
    }

    @Override
    public void updateFromPageChanged(PageChangedEvent event) {
        IPage page = event.getPage();
        if (null != page) {
            String pageCacheGroupName = SystemConstants.PAGES_CACHE_GROUP_PREFIX + page.getCode();
            this.flushGroup(DEFAULT_CACHE_NAME, pageCacheGroupName);
        }
    }

    @Override
    protected void release() {
        super.release();
        this.destroy();
    }

    @Override
    public void destroy() {
        this.flushAll(CACHE_INFO_MANAGER_CACHE_NAME);
        this.flushAll(DEFAULT_CACHE_NAME);
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
        Cache cacheOfGroup = this.getCache(CACHE_INFO_MANAGER_CACHE_NAME);
        cacheOfGroup.evict(GROUP_CACHE_NAME_PREFIX + cacheName);
        Cache cache = this.getCache(cacheName);
        cache.clear();
    }

    @Override
    public void flushEntry(String targetCache, String key) {
        this.getCache(targetCache).evict(key);
    }

    /**
     * Put an object on the given cache.
     *
     * @param targetCache The cache name
     * @param key The key
     * @param obj The object to put into cache.
     */
    public void putInCache(String targetCache, String key, Object obj) {
        Cache cache = this.getCache(targetCache);
        cache.put(key, obj);
    }

    public void putInCache(String targetCache, String key, Object obj, String[] groups) {
        Cache cache = this.getCache(targetCache);
        cache.put(key, obj);
        this.accessOnGroupMapping(targetCache, 1, groups, key);
    }

    @Override
    public void putInGroup(String targetCache, String key, String[] groups) {
        this.accessOnGroupMapping(targetCache, 1, groups, key);
    }

    @Override
    public void flushGroup(String targetCache, String group) {
        String[] groups = {group};
        this.accessOnGroupMapping(targetCache, -1, groups, null);
    }

    protected synchronized void accessOnGroupMapping(String targetCache, int operationId, String[] groups, String key) {
        Cache cache = this.getCache(CACHE_INFO_MANAGER_CACHE_NAME);
        Map<String, List<String>> objectsByGroup = this.get(cache, GROUP_CACHE_NAME_PREFIX + targetCache, Map.class);
        boolean updateMapInCache = false;
        if (operationId > 0) {
            //add
            if (null == objectsByGroup) {
                objectsByGroup = new HashMap<String, List<String>>();
            }
            for (String group : groups) {
                List<String> objectKeys = objectsByGroup.get(group);
                if (null == objectKeys) {
                    objectKeys = new ArrayList<String>();
                    objectsByGroup.put(group, objectKeys);
                }
                if (!objectKeys.contains(key)) {
                    objectKeys.add(key);
                    updateMapInCache = true;
                }
            }
        } else {
            //remove
            if (null == objectsByGroup) {
                return;
            }
            for (String group : groups) {
                List<String> objectKeys = objectsByGroup.get(group);
                if (null != objectKeys) {
                    for (String extractedKey : objectKeys) {
                        this.flushEntry(targetCache, extractedKey);
                    }
                    objectsByGroup.remove(group);
                    updateMapInCache = true;
                }
            }
        }
        if (updateMapInCache) {
            cache.put(GROUP_CACHE_NAME_PREFIX + targetCache, objectsByGroup);
        }
    }

    protected Object evaluateExpression(String expression, Method method, Object[] args, Object target, Class<?> targetClass) {
        Collection<Cache> caches = this.getCaches();
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        EvaluationContext context = evaluator.createEvaluationContext(caches,
                method, args, target, targetClass, ExpressionEvaluator.NO_RESULT);
        return evaluator.evaluateExpression(expression, method, context);
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

    protected boolean isNotExpired(String targetCache, String key) {
        return !isExpired(targetCache, key);
    }

    @Override
    public boolean isExpired(String targetCache, String key) {
        if (StringUtils.isBlank(targetCache)) {
            targetCache = DEFAULT_CACHE_NAME;
        }
        Map<String, Date> expirationTimes = this.get(EXPIRATIONS_CACHE_NAME_PREFIX + targetCache, Map.class);
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

    public Object getFromCache(String targetCache, String key) {
        if (isExpired(targetCache, key)) {
            this.flushEntry(targetCache, key);
            return null;
        }
        Cache cache = this.getCache(targetCache);
        return this.get(cache, key, Object.class);
    }

    protected <T> T get(String name, Class<T> requiredType) {
        Cache cache = this.getCache(CACHE_INFO_MANAGER_CACHE_NAME);
        return this.get(cache, name, requiredType);
    }

    protected <T> T get(Cache cache, String name, Class<T> requiredType) {
        Object value = cache.get(name);
        if (value instanceof Cache.ValueWrapper) {
            value = ((Cache.ValueWrapper) value).get();
        }
        return (T) value;
    }

    protected CacheManager getSpringCacheManager() {
        return springCacheManager;
    }
    @Autowired
    public void setSpringCacheManager(CacheManager springCacheManager) {
        this.springCacheManager = springCacheManager;
    }

    protected String getCacheName() {
        return CACHE_INFO_MANAGER_CACHE_NAME;
    }

}
