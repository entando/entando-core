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
package com.agiletec.aps.system.services.i18n;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsProperties;

/**
 * Servizio che fornisce stringhe "localizzate". Le stringhe sono specificate 
 * da una chiave di identificazione e dalla lingua di riferimento.
 * @author S.Didaci - E.Santoboni - S.Puddu
 */
public class I18nManager extends AbstractService implements II18nManager {

	private static final Logger _logger = LoggerFactory.getLogger(I18nManager.class);
	
	@Override
	public void init() throws Exception {
		this.loadLabels();
		_logger.debug("{} : initialized {} labels", this.getClass().getName(), this._labelGroups.size());
	}

	/**
	 * Carica le label dalla configurazione riguardanti tutte le 
	 * lingue caricate nel sistema e le inserisce in un'HashMap
	 * @throws ApsSystemException in caso di errori di parsing
	 */
	private void loadLabels() throws ApsSystemException {
		try {
			this._labelGroups = this.getI18nDAO().loadLabelGroups();
		} catch (Throwable t) {
			throw new ApsSystemException("Error loading labels", t);
		}
	}

	/**
	 * Restituisce una label in base alla chiave ed alla lingua specificata.
	 * @param key La chiave
	 * @param langCode Il codice della lingua.
	 * @return La label richiesta.
	 * @throws ApsSystemException
	 */
	@Override
	public String getLabel(String key, String langCode) throws ApsSystemException {
		String label = null;
		ApsProperties labelsProp = (ApsProperties) _labelGroups.get(key);
		if (labelsProp != null) {
			label = labelsProp.getProperty(langCode);
		}
		return label;
	}
	
	@Override
	public ApsProperties getLabelGroup(String key) throws ApsSystemException {
		ApsProperties labelsProp = (ApsProperties) this._labelGroups.get(key);
		if (null == labelsProp) {
			return null;
		}
		return labelsProp.clone();
	}

	/**
	 * Add a group of labels on db.
	 * @param key The key of the labels.
	 * @param labels The labels to add.
	 * @throws ApsSystemException In case of Exception.
	 */
	@Override
	public void addLabelGroup(String key, ApsProperties labels) throws ApsSystemException {
		try {
			this.getI18nDAO().addLabelGroup(key, labels);
			this._labelGroups.put(key, labels);
		} catch (Throwable t) {
			_logger.error("Error while adding a group of labels by key '{}'", key, t);
			//ApsSystemUtils.logThrowable(t, this, "addLabelGroup");
			throw new ApsSystemException("Error while adding a group of labels", t);
		}
	}

	/**
	 * Delete a group of labels from db.
	 * @param key The key of the labels to delete.
	 * @throws ApsSystemException In case of Exception.
	 */
	@Override
	public void deleteLabelGroup(String key) throws ApsSystemException {
		try {
			this.getI18nDAO().deleteLabelGroup(key);
			this._labelGroups.remove(key);
		} catch (Throwable t) {
			_logger.error("Error while deleting a label by key {}", key, t);
			//ApsSystemUtils.logThrowable(t, this, "deleteLabelGroup");
			throw new ApsSystemException("Error while deleting a label", t);
		}
	}

	/**
	 * Update a group of labels on db.
	 * @param key The key of the labels.
	 * @param labels The key of the labels to update.
	 * @throws ApsSystemException In case of Exception.
	 */
	@Override
	public void updateLabelGroup(String key, ApsProperties labels) throws ApsSystemException {
		try {
			this.getI18nDAO().updateLabelGroup(key, labels);
			this._labelGroups.put(key, labels);
		} catch (Throwable t) {
			_logger.error("Error while updating label with key {}", key, t);
			//ApsSystemUtils.logThrowable(t, this, "updateLabel");
			throw new ApsSystemException("Error while updating a label", t);
		}
	}

	/**
	 * Restituisce la lista di chiavi di gruppi di labels 
	 * in base ai parametri segbalati.
	 * @param insertedText Il testo tramite il quale effettuare la ricerca.
	 * @param doSearchByKey Specifica se effettuare la ricerca sulle chiavi, 
	 * in base al testo inserito.
	 * @param doSearchByLang Specifica se effettuare la ricerca 
	 * sul testo di una lingua, in base al testo inserito.
	 * @param langCode Specifica la lingua della label sulla quale effettuare 
	 * la ricerca, in base al testo inserito.
	 * @return La lista di chiavi di gruppi di labels 
	 * in base ai parametri segbalati.
	 */
	@Override
	public List<String> searchLabelsKey(String insertedText, boolean doSearchByKey, 
			boolean doSearchByLang, String langCode) {
		List<String> keys = new ArrayList<String>();
		Pattern pattern = Pattern.compile(insertedText,Pattern.CASE_INSENSITIVE + Pattern.LITERAL);
		Matcher matcher = pattern.matcher("");
		List<String> allKeys = new ArrayList<String>(this.getLabelGroups().keySet());
		for (int i=0; i<allKeys.size(); i++) {
			String key = allKeys.get(i);
			ApsProperties properies = (ApsProperties) this.getLabelGroups().get(key);
			if (!doSearchByKey && !doSearchByLang) {
				matcher = matcher.reset(key);
				if (matcher.find()) {
					keys.add(key);
				} else {
					Enumeration<Object> langKeys = properies.keys();
					while (langKeys.hasMoreElements()) {
						String lang = (String) langKeys.nextElement();
						String target = properies.getProperty(lang);
						if (this.labelMatch(target, matcher)) {
							keys.add(key);
							break;
						}
					}
				}
			} else if (doSearchByKey && !doSearchByLang) {
				matcher = matcher.reset(key);
				if (matcher.find()) {
					keys.add(key);
				}
			} else if (!doSearchByKey && doSearchByLang) {
				String target = properies.getProperty(langCode);
				if(this.labelMatch(target, matcher)) {
					keys.add(key);
				}
			}
		}
		return keys;
	}

	private boolean labelMatch(String target, Matcher matcher){
		boolean match = false;
		if(null != target) {
			matcher = matcher.reset(target);
			if(matcher.find()) {
				match =true;
			}
		}
		return match;
	}
	
	/**
	 * Return the group of labels.
	 * @return The group of labels.
	 */
	@Override
	public Map<String, ApsProperties> getLabelGroups() {
		return this._labelGroups;
	}

	protected II18nDAO getI18nDAO() {
		return _i18nDAO;
	}
	public void setI18nDAO(II18nDAO i18nDao) {
		_i18nDAO = i18nDao;
	}

	/**
	 * Map delle label internazionalizzate.
	 */
	private Map<String, ApsProperties> _labelGroups;

	private II18nDAO _i18nDAO;

}
