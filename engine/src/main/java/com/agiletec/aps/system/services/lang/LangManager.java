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
package com.agiletec.aps.system.services.lang;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.lang.events.LangsChangedEvent;
import com.agiletec.aps.util.FileTextReader;

/**
 * Servizio di gestione delle lingue.
 * @author M.Diana - E.Santoboni
 */
public class LangManager extends AbstractService implements ILangManager {

	private static final Logger _logger = LoggerFactory.getLogger(LangManager.class);
	
	@Override
	public void init() throws Exception {
		this.loadSystemLangs();
		_logger.debug("{} ready: initialized {} languages", this.getClass().getName(),this._langList.size());
	}
	
	/**
	 * Carica le lingue dalla configurazione
	 * @throws ApsSystemException in caso di errori di parsing
	 */
	private void loadSystemLangs() throws ApsSystemException {
		String xmlConfig = this.getConfigManager().getConfigItem(SystemConstants.CONFIG_ITEM_LANGS);
		List<Lang> tempList = this.parse(xmlConfig);
		this._langs = new HashMap<String, Lang>(tempList.size());
		this._langList = new ArrayList<Lang>(tempList.size());
		for (int i=0; i<tempList.size(); i++){
			Lang lang = (Lang) tempList.get(i);
			this._langs.put(lang.getCode(), lang);
			if(lang.isDefault()){
				this._defaultLang = lang;
				this._langList.add(0, lang);
			} else {
				this._langList.add(lang);
			}
		}
	}
	
	/**
	 * Esegue il parsing della voce di configurazione per estrarre le lingue.
	 * @param xmlConfig L'xml di configurazione.
	 * @return La lista delle lingue di configurazione.
	 * @throws ApsSystemException in caso di errori di parsing
	 */
	private List<Lang> parse(String xmlConfig) throws ApsSystemException {
		LangDOM langDom = new LangDOM(xmlConfig);
		List<Lang> langs = langDom.getLangs();
		return langs;
	}
	
	/**
	 * Return the list of assignable langs to system ordered by lang's description.
	 * @return The List of assignable langs.
	 * @throws ApsSystemException 
	 */
	public List<Lang> getAssignableLangs() throws ApsSystemException {
		if (_assignableLangs == null) {
			this.loadAssignableLangs();
		}
		List<Lang> assignables = new ArrayList<Lang>(_assignableLangs.values());
		Collections.sort(assignables);
		return assignables;
	}
	
	private void loadAssignableLangs() throws ApsSystemException {
		InputStream is = this.getClass().getResourceAsStream("ISO_639 -1_langs.xml");
		String xmlConfig = FileTextReader.getText(is);
		LangDOM langDom = new LangDOM(xmlConfig);
		List<Lang> tempList = langDom.getLangs();
		this._assignableLangs = new HashMap<String, Lang>();
		for (int i=0; i<tempList.size(); i++){
			Lang lang = (Lang) tempList.get(i);
			this._assignableLangs.put(lang.getCode(), lang);
		}
	}
	
	/**
	 * Add a lang on system.
	 * @param code The code of the lang to add.
	 * @throws ApsSystemException In case of error on update config.
	 */
	@Override
	public void addLang(String code) throws ApsSystemException {
		if (this._assignableLangs == null) {
			this.loadAssignableLangs();
		}
		Lang lang = (Lang) this._assignableLangs.get(code);
		if (lang != null) {
			this._langList.add(lang);
			this._langs.put(lang.getCode(), lang);
			this.updateConfig();
		}
	}
	
	/**
	 * Update the description of a system langs.
	 * @param code The code of the lang to update.
	 * @param descr The new description.
	 * @throws ApsSystemException In case of error on update config.
	 */
	@Override
	public void updateLang(String code, String descr) throws ApsSystemException {
		Lang lang = this.getLang(code);
		if (lang != null) {
			lang.setDescr(descr);
			this.updateConfig();
		}
	}
	
	/**
	 * Remove a lang from the system.
	 * @param code The code of the lang to remove.
	 * @throws ApsSystemException In case of error on update config.
	 */
	@Override
	public void removeLang(String code) throws ApsSystemException {
		Lang lang = this.getLang(code);
		if (lang != null) {
			this._langList.remove(lang);
			this._langs.remove(code);
			this.updateConfig();
		}
	}
	
	private void updateConfig() throws ApsSystemException {
		LangDOM langDom = new LangDOM();
		langDom.addLangs(this._langList);
		String xml = langDom.getXMLDocument();
		this.getConfigManager().updateConfigItem(SystemConstants.CONFIG_ITEM_LANGS, xml);
		LangsChangedEvent event = new LangsChangedEvent();
		this.notifyEvent(event);
	}
	
	/**
	 * Restituisce un oggetto lingua in base al codice
	 * @param code Il codice della lingua
	 * @return La lingua richiesta
	 */
	@Override
	public Lang getLang(String code) {
		return this._langs.get(code);
	}
	
	/**
	 * Return the default lang.
	 * @return The default lang.
	 */
	@Override
	public Lang getDefaultLang(){
		return _defaultLang;
	}
	
	/**
	 * Restituisce la lista (ordinata) delle lingue. La lingua di
	 * default è in prima posizione.
	 * @return La lista delle lingue
	 */
	@Override
	public List<Lang> getLangs(){
		return _langList;
	}
	
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	/**
	 * Map delle lingue, per il recupero in base al codice.
	 */
	private Map<String, Lang> _langs;
	
	/**
	 * Map delle lingue assegnabili al sistema, 
	 * per il recupero in base al codice.
	 */
	private Map<String, Lang> _assignableLangs;
	
	/**
	 * List delle lingue, per il recupero in base all'ordine.
	 */
	private List<Lang> _langList;
	
	/**
	 * Lingua di default.
	 */
	private Lang _defaultLang;
	
	private ConfigInterface _configManager;
	
}