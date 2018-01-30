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
package com.agiletec.aps.system.services.lang;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.lang.cache.ILangManagerCacheWrapper;
import com.agiletec.aps.system.services.lang.events.LangsChangedEvent;
import com.agiletec.aps.util.FileTextReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servizio di gestione delle lingue.
 *
 * @author M.Diana - E.Santoboni
 */
public class LangManager extends AbstractService implements ILangManager {

	private static final Logger logger = LoggerFactory.getLogger(LangManager.class);

	private Map<String, Lang> assignableLangs;

	private ConfigInterface configManager;

	private ILangManagerCacheWrapper cacheWrapper;

	@Override
	public void init() throws Exception {
		String xmlConfig = this.getConfigManager().getConfigItem(SystemConstants.CONFIG_ITEM_LANGS);
		this.getCacheWrapper().initCache(xmlConfig);
		logger.debug("{} ready: initialized", this.getClass().getName());
	}

	/**
	 * Return the list of assignable langs to system ordered by lang's
	 * description.
	 *
	 * @return The List of assignable langs.
	 * @throws ApsSystemException
	 */
	@Override
	public List<Lang> getAssignableLangs() throws ApsSystemException {
		if (assignableLangs == null) {
			this.loadAssignableLangs();
		}
		List<Lang> assignables = new ArrayList<Lang>(assignableLangs.values());
		Collections.sort(assignables);
		return assignables;
	}

	private void loadAssignableLangs() throws ApsSystemException {
		try {
			InputStream is = this.getClass().getResourceAsStream("ISO_639-1_langs.xml");
			String xmlConfig = FileTextReader.getText(is);
			LangDOM langDom = new LangDOM(xmlConfig);
			List<Lang> langs = langDom.getLangs();
			this.assignableLangs = new HashMap<String, Lang>();
			for (Lang lang : langs) {
				this.assignableLangs.put(lang.getCode(), lang);
			}
		} catch (ApsSystemException | IOException e) {
			logger.error("Error loading langs from iso definition", e);
			throw new ApsSystemException("Error loading langs from iso definition", e);
		}
	}

	/**
	 * Add a lang on system.
	 *
	 * @param code The code of the lang to add.
	 * @throws ApsSystemException In case of error on update config.
	 */
	@Override
	public void addLang(String code) throws ApsSystemException {
		if (this.assignableLangs == null) {
			this.loadAssignableLangs();
		}
		Lang lang = (Lang) this.assignableLangs.get(code);
		if (lang != null) {
			this.getCacheWrapper().addLang(lang);
			this.updateConfig();
		}
	}

	/**
	 * Update the description of a system langs.
	 *
	 * @param code The code of the lang to update.
	 * @param description The new description.
	 * @throws ApsSystemException In case of error on update config.
	 */
	@Override
	public void updateLang(String code, String description) throws ApsSystemException {
		Lang lang = this.getLang(code);
		if (lang != null) {
			lang.setDescr(description);
			this.getCacheWrapper().updateLang(lang);
			this.updateConfig();
		}
	}

	/**
	 * Remove a lang from the system.
	 *
	 * @param code The code of the lang to remove.
	 * @throws ApsSystemException In case of error on update config.
	 */
	@Override
	public void removeLang(String code) throws ApsSystemException {
		Lang lang = this.getLang(code);
		if (lang != null) {
			this.getCacheWrapper().removeLang(lang);
			this.updateConfig();
		}
	}

	private void updateConfig() throws ApsSystemException {
		LangDOM langDom = new LangDOM();
		langDom.addLangs(this.getLangs());
		String xml = langDom.getXMLDocument();
		this.getConfigManager().updateConfigItem(SystemConstants.CONFIG_ITEM_LANGS, xml);
		LangsChangedEvent event = new LangsChangedEvent();
		this.notifyEvent(event);
	}

	/**
	 * Restituisce un oggetto lingua in base al codice
	 *
	 * @param code Il codice della lingua
	 * @return La lingua richiesta
	 */
	@Override
	public Lang getLang(String code) {
		return this.getCacheWrapper().getLang(code);
	}

	/**
	 * Return the default lang.
	 *
	 * @return The default lang.
	 */
	@Override
	public Lang getDefaultLang() {
		return this.getCacheWrapper().getDefaultLang();
	}

	/**
	 * Restituisce la lista (ordinata) delle lingue. La lingua di default è in
	 * prima posizione.
	 *
	 * @return La lista delle lingue
	 */
	@Override
	public List<Lang> getLangs() {
		return this.getCacheWrapper().getLangs();
	}

	protected ConfigInterface getConfigManager() {
		return configManager;
	}

	public void setConfigManager(ConfigInterface configManager) {
		this.configManager = configManager;
	}

	protected ILangManagerCacheWrapper getCacheWrapper() {
		return cacheWrapper;
	}

	public void setCacheWrapper(ILangManagerCacheWrapper cacheWrapper) {
		this.cacheWrapper = cacheWrapper;
	}

}
