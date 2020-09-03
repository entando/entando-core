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
package com.agiletec.aps.system.services.i18n;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsProperties;

/**
 * Interface for managers of local strings (i18n)
 * @author S.Didaci - E.Santoboni - S.Puddu
 */
public interface II18nManager {

	/**
	 * Renders a label, according to a given key, in the desired language.
	 * If the label is empty or not translated, is rendered in the default language.
	 * If the label is empty or not translated in the default language, 
	 * the value is returned accordingly to keyIfEmpty value:
	 * <ul>
	 * 	<li>false: return null</li>
	 * 	<li>true: return the key value</li>
	 * </ul>
	 * @param key The key.
	 * @param renderingLang The code of the rendering language.
	 * @param keyIfEmpty If the label is empty or not exists, returns the label key
	 * @return The value of the label, in the rendering language.
	 * @throws ApsSystemException
	 */
	public String renderLabel(String key, String renderingLang, boolean keyIfEmpty) throws ApsSystemException;
	
	/**
	 * Renders a label, according to a given key, in the desired language.
	 * If the label is empty or not translated, is rendered in the default language.
	 * If the label is empty or not translated in the default language, 
	 * the value is returned accordingly to keyIfEmpty value:
	 * <ul>
	 * 	<li>false: return null</li>
	 * 	<li>true: return the key value</li>
	 * </ul>
	 * @param key The key.
	 * @param renderingLang The code of the rendering language.
	 * @param keyIfEmpty If the label is empty or not exists, returns the label key
	 * @param params The parameters to be replaced into the label.
	 * The parameters, in the label text, must be as ${param_name}
	 * @return The value of the label, in the rendering language.
	 * @throws ApsSystemException
	 */
	public String renderLabel(String key, String renderingLang, boolean keyIfEmpty, Map<String, String> params) throws ApsSystemException;
	
	/**
	 * Restituisce una label in base alla chiave ed alla lingua specificata.
	 * @param key The key.
	 * @param langCode The code of the language.
	 * @return La label richiesta.
	 * @throws ApsSystemException
	 */
	public String getLabel(String key, String langCode) throws ApsSystemException;
	
	public ApsProperties getLabelGroup(String key) throws ApsSystemException;
	
	/**
	 * Add a group of labels on db.
	 * @param key The key of the labels.
	 * @param labels The labels to add.
	 * @throws ApsSystemException In case of Exception.
	 */
	public void addLabelGroup(String key, ApsProperties labels) throws ApsSystemException;
	
	/**
	 * Delete a group of labels from db.
	 * @param key The key of the labels to delete.
	 * @throws ApsSystemException In case of Exception.
	 */
	public void deleteLabelGroup(String key) throws ApsSystemException;
	
	/**
	 * Update a group of labels on db.
	 * @param key The key of the labels.
	 * @param labels The key of the labels to update.
	 * @throws ApsSystemException In case of Exception.
	 */
	public void updateLabelGroup(String key, ApsProperties labels) throws ApsSystemException;
	
	/**
	 * Returns the list of keys of groups of labels based on the parameters entered.
	 * @param insertedText The text to search by.
	 * @param doSearchByKey Specifies whether to search the keys, based on the entered text.
	 * @param doSearchByLang Specifies whether to search the text of a language, based on the entered text.
	 * @param langCode Specifies the language of the label on which to search, based on the entered text.
	 * @return La lista di chiavi di gruppi di labels in base ai parametri inseriti.
	 * @throws ApsSystemException In case of Exception.
	 */
	public List<String> searchLabelsKey(String insertedText, boolean doSearchByKey, 
			boolean doSearchByLang, String langCode) throws ApsSystemException;
	
	/**
	 * Return the group of labels.
	 * @return The group of labels.
	 */
	public Map<String, ApsProperties> getLabelGroups();
	
}
