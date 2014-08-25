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
package com.agiletec.aps.system.services.cache;

import org.entando.entando.aps.system.services.cache.CacheInfoManager;
import org.entando.entando.aps.system.services.cache.ICacheInfoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;

/**
 * Manager of the System Cache
 * @author E.Santoboni
 */
public class OldCacheManager extends AbstractService implements ICacheManager {

	private static final Logger _logger = LoggerFactory.getLogger(OldCacheManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} (cache service initialized) ready", this.getClass().getName());
	}
	
	@Override
	public void flushAll() {
		if (this.getCacheInfoManager() instanceof CacheInfoManager) {
			((CacheInfoManager) this.getCacheInfoManager()).flushAll();
		}
	}
	
	@Override
	public void flushEntry(String key) {
		this.getCacheInfoManager().flushEntry(key);
	}
	
	@Override
	public void flushGroup(String group) {
		this.getCacheInfoManager().flushGroup(group);
	}
	
	@Override
	@Deprecated
	public Object getFromCache(String key, int myRefreshPeriod) {
		return this.getFromCache(key);
	}
	
	@Override
	public Object getFromCache(String key) {
		if (this.getCacheInfoManager() instanceof CacheInfoManager) {
			return ((CacheInfoManager) this.getCacheInfoManager()).getFromCache(key);
		}
		return null;
	}
	
	@Override
	public void putInCache(String key, Object obj, String[] groups) {
		if (this.getCacheInfoManager() instanceof CacheInfoManager) {
			((CacheInfoManager) this.getCacheInfoManager()).putInCache(key, obj, groups);
		}
	}
	
	@Override
	public void putInCacheGroups(String key, String[] groups) {
		this.getCacheInfoManager().putInGroup(key, groups);
	}
	
	@Override
	public void putInCache(String key, Object obj) {
		if (this.getCacheInfoManager() instanceof CacheInfoManager) {
			((CacheInfoManager) this.getCacheInfoManager()).putInCache(key, obj);
		}
	}
	
	@Override
	public void setExpirationTime(String key, long expiresInSeconds) {
		if (this.getCacheInfoManager() instanceof CacheInfoManager) {
			((CacheInfoManager) this.getCacheInfoManager()).setExpirationTime(key, expiresInSeconds);
		}
	}
	
	protected ICacheInfoManager getCacheInfoManager() {
		return _cacheInfoManager;
	}
	public void setCacheInfoManager(ICacheInfoManager cacheInfoManager) {
		this._cacheInfoManager = cacheInfoManager;
	}
	
	private ICacheInfoManager _cacheInfoManager;
	
}
