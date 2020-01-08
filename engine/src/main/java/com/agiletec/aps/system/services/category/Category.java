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
package com.agiletec.aps.system.services.category;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.tree.ITreeNodeManager;
import com.agiletec.aps.system.common.tree.TreeNode;
import com.agiletec.aps.util.ApsProperties;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * Rappresentazione di un'oggetto Categoria.
 *
 * @author E.Santoboni
 */
public class Category extends TreeNode implements Comparable, Serializable {

    private String _renderingLang;
    private String _defaultLang;
    
    @Override
    public Category clone() {
        Category clone = (Category) super.clone();
        clone.setDefaultLang(this._defaultLang);
        clone.setRenderingLang(this._renderingLang);
        return clone;
    }

    /**
     * Restituisce l'insieme ordinato delle categorie di livello inferiore.
     *
     * @return L'insieme ordinato delle categorie
     */
    @Override
    public String[] getChildrenCodes() {
        String[] categories = new String[super.getChildrenCodes().length];
        for (int i = 0; i < super.getChildrenCodes().length; i++) {
            categories[i] = (String) super.getChildrenCodes()[i];
        }
        Arrays.sort(categories);
        return categories;
    }

    /**
     * Restituisce il titolo della categoria nella lingua corrente
     * (precedentemente impostata con il metodo setRenderingLang) o, se non
     * disponibile, nella lingua di default.
     *
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
     * Restituisce il titolo (comprensivo delle progenitrici) della singola
     * categoria.Il titolo viene restituito nella lingua corrente
 (precedentemente impostata con il metodo setRenderingLang) o, se non
 disponibile, nella lingua di default.
     *
     * @param treeNodeManager
     * @return Il titolo della categoria.
     */
    public String getFullTitle(ITreeNodeManager treeNodeManager) {
        String title = this.getTitle();
        Category parent = (Category) treeNodeManager.getNode(this.getParentCode());
        if (parent != null && parent.getParentCode() != null
                && !parent.getCode().equals(parent.getParentCode())) {
            String parentTitle = parent.getFullTitle(treeNodeManager);
            title = parentTitle + " / " + title;
        }
        return title;
    }

    /**
     * Restituisce il titolo (comprensivo delle progenitrici) della singola
     * categoria nella lingua di default.
     *
     * @param treeNodeManager
     * @return Il titolo della categoria.
     */
    public String getDefaultFullTitle(ITreeNodeManager treeNodeManager) {
        return this.getFullTitle(this._defaultLang, treeNodeManager);
    }

    @Override
    public int compareTo(Object category) {
        return this.getTitle().compareTo(((Category) category).getTitle());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Category other = (Category) obj;
        if (!Objects.equals(this.getCode(), other.getCode())) {
            return false;
        }
        return true;
    }

    /**
     * Crea un clone dell'oggetto categoria copiano solo gli elementi necessari
     * ad essere erogata.Il metodo viene invocato dal Wrapper dei contenuti
 esclusivamente quando viene chiesto di erogare la lista di categorie.
     *
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
        clone.setParentCode(this.getParentCode());
        return clone;
    }

    /**
     * Imposta la lingua di renderizzazione alla categoria ed alle progenitrici.
     * Il metodo viene invocato dal Wrapper dei contenuti esclusivamente quando
     * viene chiesto di erogare la lista di categorie.
     *
     * @param langCode Il codice della lingua di renderizzazione.
     */
    public void setRenderingLang(String langCode) {
        this._renderingLang = langCode;
    }

    /**
     * Setta il codice della lingua di default.
     *
     * @param langCode Il codice della lingua di default.
     */
    public void setDefaultLang(String langCode) {
        this._defaultLang = langCode;
    }

    @Override
    public String getManagerBeanCode() {
        return SystemConstants.CATEGORY_MANAGER;
    }
    
}
