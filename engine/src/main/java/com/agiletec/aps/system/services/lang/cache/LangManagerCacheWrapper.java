/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.aps.system.services.lang.cache;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import com.agiletec.aps.system.common.AbstractGenericCacheWrapper;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.lang.LangDOM;
import java.util.HashMap;
import java.util.Map;

/**
 * @author E.Santoboni
 */
public class LangManagerCacheWrapper extends AbstractGenericCacheWrapper<Lang> implements ILangManagerCacheWrapper {

	private static final Logger logger = LoggerFactory.getLogger(LangManagerCacheWrapper.class);

	@Override
	public void initCache(String xmlConfig) throws ApsSystemException {
		try {
			Cache cache = this.getCache();
			this.releaseCachedObjects(cache);
			LangDOM langDom = new LangDOM(xmlConfig);
			Map<String, Lang> langMap = new HashMap<>();
			List<Lang> systemLangs = langDom.getLangs();
			for (Lang lang : systemLangs) {
				if (lang.isDefault()) {
					cache.put(LANG_DEFAULT_CACHE_NAME, lang);
				}
				langMap.put(lang.getCode(), lang);
			}
			super.insertObjectsOnCache(cache, langMap);
		} catch (Throwable t) {
			logger.error("Error loading the system langs", t);
			throw new ApsSystemException("Error loading the system langs", t);
		}
	}

	@Override
	public List<Lang> getLangs() {
		List<Lang> langs = new ArrayList<>();
		Cache cache = this.getCache();
		List<String> codes = (List<String>) this.get(cache, LANG_CODES_CACHE_NAME, List.class);
		if (null != codes) {
			for (String code : codes) {
				Lang lang = this.get(cache, LANG_CACHE_NAME_PREFIX + code, Lang.class);
				if (lang.isDefault()) {
					langs.add(0, lang);
				} else {
					langs.add(lang);
				}
			}
		}
		return langs;
	}

	@Override
	public Lang getDefaultLang() {
		return this.get(this.getCache(), LANG_DEFAULT_CACHE_NAME, Lang.class);
	}

	@Override
	public Lang getLang(String code) {
		return this.get(this.getCache(), LANG_CACHE_NAME_PREFIX + code, Lang.class);
	}

	@Override
	public void addLang(Lang lang) {
		this.manage(lang.getCode(), lang, Action.ADD);
	}

	@Override
	public void updateLang(Lang lang) {
		this.manage(lang.getCode(), lang, Action.UPDATE);
	}

	@Override
	public void removeLang(Lang lang) {
		this.manage(lang.getCode(), lang, Action.DELETE);
	}

	@Override
	protected String getCodesCacheKey() {
		return LANG_CODES_CACHE_NAME;
	}

	@Override
	protected String getCacheKeyPrefix() {
		return LANG_CACHE_NAME_PREFIX;
	}

	@Override
	protected String getCacheName() {
		return LANG_MANAGER_CACHE_NAME;
	}

}
