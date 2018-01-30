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

import com.agiletec.aps.system.common.AbstractCacheWrapper;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.lang.LangDOM;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

/**
 * @author E.Santoboni
 */
public class LangManagerCacheWrapper extends AbstractCacheWrapper implements ILangManagerCacheWrapper {

	private static final Logger logger = LoggerFactory.getLogger(LangManagerCacheWrapper.class);

	@Override
	public void initCache(String xmlConfig) throws ApsSystemException {
		try {
			Cache cache = this.getCache();
			this.releaseCachedObjects(cache);
			LangDOM langDom = new LangDOM(xmlConfig);
			List<Lang> systemLangs = langDom.getLangs();
			this.insertObjectsOnCache(cache, systemLangs);
		} catch (Throwable t) {
			logger.error("Error loading the system langs", t);
			throw new ApsSystemException("Error loading the system langs", t);
		}
	}

	protected void releaseCachedObjects(Cache cache) {
		List<String> codes = (List<String>) this.get(cache, LANG_CODES_CACHE_NAME, List.class);
		if (null != codes) {
			for (String code : codes) {
				cache.evict(LANG_CACHE_NAME_PREFIX + code);
			}
			cache.evict(LANG_CODES_CACHE_NAME);
		}
	}

	protected void insertObjectsOnCache(Cache cache, List<Lang> systemLangs) {
		List<String> langCodes = new ArrayList<String>();
		Iterator<Lang> langIterator = systemLangs.iterator();
		while (langIterator.hasNext()) {
			Lang lang = langIterator.next();
			cache.put(LANG_CACHE_NAME_PREFIX + lang.getCode(), lang);
			langCodes.add(lang.getCode());
			if (lang.isDefault()) {
				cache.put(LANG_DEFAULT_CACHE_NAME, lang);
			}
		}
		cache.put(LANG_CODES_CACHE_NAME, langCodes);
	}

	@Override
	public List<Lang> getLangs() {
		List<Lang> langs = new ArrayList<Lang>();
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
		this.manage(lang, Action.ADD);
	}

	@Override
	public void updateLang(Lang lang) {
		this.manage(lang, Action.UPDATE);
	}

	@Override
	public void removeLang(Lang lang) {
		this.manage(lang, Action.DELETE);
	}

	private void manage(Lang lang, Action operation) {
		if (null == lang) {
			return;
		}
		Cache cache = this.getCache();
		List<String> codes = (List<String>) this.get(cache, LANG_CODES_CACHE_NAME, List.class);
		if (Action.ADD.equals(operation)) {
			if (!codes.contains(lang.getCode())) {
				codes.add(lang.getCode());
				cache.put(LANG_CODES_CACHE_NAME, codes);
			}
			cache.put(LANG_CACHE_NAME_PREFIX + lang.getCode(), lang);
		} else if (Action.UPDATE.equals(operation) && codes.contains(lang.getCode())) {
			cache.put(LANG_CACHE_NAME_PREFIX + lang.getCode(), lang);
		} else if (Action.DELETE.equals(operation)) {
			codes.remove(lang.getCode());
			cache.evict(LANG_CACHE_NAME_PREFIX + lang.getCode());
			cache.put(LANG_CODES_CACHE_NAME, codes);
		}
	}

	@Override
	protected String getCacheName() {
		return LANG_MANAGER_CACHE_NAME;
	}

}
