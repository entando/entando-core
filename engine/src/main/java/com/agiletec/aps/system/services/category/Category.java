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
package com.agiletec.aps.system.services.category;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import com.agiletec.aps.system.common.tree.TreeNode;
import com.agiletec.aps.util.ApsProperties;

/**
 * Rappresentazione di un'oggetto Categoria.
 * @author E.Santoboni
 */
public class Category extends TreeNode implements Comparable {
	
	@Override
	public Category getParent() {
		return (Category) super.getParent();
	}
	
	/**
	 * Restituisce il codice della categoria di livello superiore.
	 * @return il codice della categoria di livello superiore
	 */
	public String getParentCode() {
		return _parentCode;
	}
	
	/**
	 * Imposta il codice della categoria di livello superiore.
	 * @param parentCode Il codice della categoria di livello superiore.
	 */
	public void setParentCode(String parentCode) {
		this._parentCode = parentCode;
	}
    
    /**
	 * Restituisce l'insieme ordinato delle categorie di livello inferiore.
	 * @return L'insieme ordinato delle categorie
	 */
	@Override
    public Category[] getChildren() {
    	Category[] categories = new Category[super.getChildren().length];
    	for (int i=0; i<super.getChildren().length; i++) {
    		categories[i] = (Category) super.getChildren()[i];
    	}
		Arrays.sort(categories);
		return categories;
	}
	
	/**
	 * Restituisce il titolo della categoria nella lingua corrente 
	 * (precedentemente impostata con il metodo setRenderingLang) o, 
	 * se non disponibile, nella lingua di default. 
	 * @return Il titolo della categoria
	 */
	public String getTitle() {
		String title = null;
		if (this._renderingLang != null && null != this.getTitles().get(this._renderingLang)) {
			title = (String) this.getTitles().get(this._renderingLang);
		} else {
			title = (String) this.getTitles().get(this._defaultLang);
			if (title == null) {
				title = "";
			}
		}
		return title;
	}
	
	
	/**
	 * Restituisce il titolo (comprensivo delle progenitrici) della 
	 * singola categoria. Il titolo viene restituito nella lingua 
	 * corrente (precedentemente impostata con il metodo setRenderingLang) 
	 * o, se non disponibile, nella lingua di default.
	 * @return Il titolo della categoria.
	 */
	public String getFullTitle() {
		String title = this.getTitle();
		Category parent = this.getParent();
		if (parent != null && parent.getParent() != null && 
				!parent.getCode().equals(parent.getParentCode())) {
			String parentTitle = parent.getFullTitle();
			title = parentTitle + " / " + title;
		}
		return title;
	}
	
	/**
	 * Restituisce il titolo (comprensivo delle 
	 * progenitrici) della singola categoria nella lingua di default.
	 * @return Il titolo della categoria.
	 */
	public String getDefaultFullTitle() {
		return this.getFullTitle(this._defaultLang);
	}
	
	@Override
	public int compareTo(Object category) {
		return this.getTitle().compareTo(((Category) category).getTitle());
	}
	
	/**
	 * Crea un clone dell'oggetto categoria copiano solo 
	 * gli elementi necessari ad essere erogata.
	 * Il metodo viene invocato dal Wrapper dei contenuti esclusivamente 
	 * quando viene chiesto di erogare la lista di categorie.
	 * @return La categoria clonata.
	 */
	public Category getCloneForWrapper() {
		Category clone = new Category();
		clone.setCode(this.getCode());
		clone.setDefaultLang(this._defaultLang);
		ApsProperties cloneProperties = new ApsProperties();
		Set<Object> keySet = this.getTitles().keySet();
		Iterator<Object> iter = keySet.iterator();
		while (iter.hasNext()) {
			String currentLangCode = (String) iter.next();
			String title = (String) this.getTitles().get(currentLangCode);
			cloneProperties.put(currentLangCode, title);
		}
		clone.setTitles(cloneProperties);
		if (!this.getParent().getCode().equals(this.getCode())) {
			Category parent = this.getParent();
			clone.setParent(parent.getCloneForWrapper());
		}
		return clone;
	}
	
	/**
	 * Imposta la lingua di renderizzazione alla categoria ed alle progenitrici.
	 * Il metodo viene invocato dal Wrapper dei contenuti esclusivamente 
	 * quando viene chiesto di erogare la lista di categorie.
	 * @param langCode Il codice della lingua di renderizzazione.
	 */
	public void setRenderingLang(String langCode) {
		this._renderingLang = langCode;
		if (null != this.getParent()) 
			((Category) this.getParent()).setRenderingLang(langCode);
	}
	
	/**
	 * Setta il codice della lingua di default.
	 * @param langCode Il codice della lingua di default.
	 */
	public void setDefaultLang(String langCode) {
		this._defaultLang = langCode;
	}
	
	/**
	 * Il codice della categoria di livello superiore
	 */
	private String _parentCode;
	
	private String _renderingLang;
	private String _defaultLang;
	
}