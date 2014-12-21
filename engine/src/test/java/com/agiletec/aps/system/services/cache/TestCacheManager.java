/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package com.agiletec.aps.system.services.cache;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;

/**
 * Classe test del servizio gestore cache.
 * @version 1.0
 * @author E.Santoboni
 */
public class TestCacheManager extends BaseTestCase {
	
    protected void setUp() throws Exception {
    	super.setUp();
    	this.init();
    }
    
    public void testPutGetFromCache_1() {
    	String value = "Stringa prova";
    	String key = "Chiave_prova";
    	this._cacheManager.putInCache(key, value);
    	Object extracted = this._cacheManager.getFromCache(key);
    	assertEquals(value, extracted);
    	this._cacheManager.flushEntry(key);
    	extracted = this._cacheManager.getFromCache(key);
    	assertNull(extracted);
    }
    
    public void testPutGetFromCache_2() {
    	String key = "Chiave_prova";
    	Object extracted = this._cacheManager.getFromCache(key);
    	assertNull(extracted);
    	extracted = this._cacheManager.getFromCache(key);
    	assertNull(extracted);
    	
    	String value = "Stringa prova";
    	this._cacheManager.putInCache(key, value);
    	
    	extracted = this._cacheManager.getFromCache(key);
    	assertNotNull(extracted);
    	assertEquals(value, extracted);
    	this._cacheManager.flushEntry(key);
    	extracted = this._cacheManager.getFromCache(key);
    	assertNull(extracted);
    }
    
    public void testPutGetFromCacheOnRefreshPeriod() throws Throwable {
    	String value = "Stringa prova";
    	String key = "Chiave prova";
    	this._cacheManager.putInCache(key, value);
		this._cacheManager.setExpirationTime(key, 2);
		Object extracted = this._cacheManager.getFromCache(key);
    	assertEquals(value, extracted);
    	synchronized (this) {
    		this.wait(3000);
		}
    	extracted = this._cacheManager.getFromCache(key);
    	assertNull(extracted);
    }
    
    public void testPutGetFromCacheGroup() {
    	String value = "Stringa prova";
    	String key = "Chiave prova";
    	String group1 = "group1";
    	String[] groups = {group1};
    	_cacheManager.putInCache(key, value, groups);
    	Object extracted = _cacheManager.getFromCache(key);
    	assertEquals(value, extracted);
    	_cacheManager.flushGroup(group1);
    	extracted = _cacheManager.getFromCache(key);
    	assertNull(extracted);
    }
    
    private void init() throws Exception {
    	try {
    		_cacheManager = (ICacheManager) this.getService(SystemConstants.CACHE_MANAGER);
    	} catch (Throwable t) {
    		throw new Exception(t);
        }
    }
    
    private ICacheManager _cacheManager = null;
    
}