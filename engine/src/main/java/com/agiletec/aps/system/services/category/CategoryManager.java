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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;

/**
 * Manager delle Categorie.
 * @author E.Santoboni
 */
public class CategoryManager extends AbstractService implements ICategoryManager {

	private static final Logger _logger = LoggerFactory.getLogger(CategoryManager.class);
	
	@Override
    public void init() throws Exception {
		this.loadCategories();
		_logger.debug("{} ready. {} categories initialized", this.getClass().getName(),this._categories.size());
	}
	
	/**
     * Caricamento della lista di categorie da db
	 * @throws ApsSystemException in caso di errore nell'accesso al db.
	 */
	protected void loadCategories() throws ApsSystemException {
		List<Category> categories = null;
		try {
			categories = this.getCategoryDAO().loadCategories(this.getLangManager());
			if (categories.isEmpty()) {
				Category root = this.createRoot();
				this.addCategory(root);
			} else this.build(categories);
		} catch (Throwable t) {
			_logger.error("Error loading the category tree", t);
			//ApsSystemUtils.logThrowable(t, this, "loadCategories");
            throw new ApsSystemException("Error loading the category tree.", t);
		}
	}
	
	private void build(List<Category> categories) throws ApsSystemException {
		try {
			Category root = null;
            Map<String, Category> categoryMap = new HashMap<String, Category>();
            for (int i = 0; i < categories.size(); i++) {
            	Category cat = (Category) categories.get(i);
            	categoryMap.put(cat.getCode(), cat);
                if (cat.getCode().equals(cat.getParentCode())) {
                    root = cat;
                }
            }
            for (int i = 0; i < categories.size(); i++) {
            	Category cat = (Category) categories.get(i);
            	Category parent = (Category) categoryMap.get(cat.getParentCode());
                if (cat != root) {
                    parent.addChild(cat);
                }
                cat.setParent(parent);
            }
            if (root == null) {
                throw new ApsSystemException( "Error found in the category tree: undefined root");
            }
            _root = root;
            _categories = categoryMap;
        } catch (ApsSystemException e) {
            throw e;
        } catch (Throwable t) {
        	_logger.error("Error building the category tree", t);
        	//ApsSystemUtils.logThrowable(t, this, "loadCategories");
            throw new ApsSystemException("Error building the category tree", t);
        }
	}
	
	/**
     * Aggiunge una categoria al db.
     * @param category La categoria da aggiungere.
     * @throws ApsSystemException In caso di errore nell'accesso al db.
     */
	@Override
    public void addCategory(Category category) throws ApsSystemException {
		try {
			this.getCategoryDAO().addCategory(category);
		} catch (Throwable t) {
			_logger.error("Error detected while adding a category", t);
			//ApsSystemUtils.logThrowable(t, this, "addCategory");
			throw new ApsSystemException("Error detected while adding a category", t);
		}
		this.loadCategories();
	}
    
    /**
     * Cancella una categoria.
     * @param code Il codice della categoria da eliminare.
     * @throws ApsSystemException in caso di errore nell'accesso al db.
     */
	@Override
    public void deleteCategory(String code) throws ApsSystemException {
        Category cat =  (Category) this.getCategories().get(code);
        if (cat != null && cat.getChildren().length <= 0) {
            try {
                this.getCategoryDAO().deleteCategory(code);
            } catch (Throwable t) {
            	_logger.error("Error detected while removing the category {}", code, t);
            	//ApsSystemUtils.logThrowable(t, this, "deleteCategory");
    			throw new ApsSystemException("Error detected while removing a category", t);
            }
        }
        this.loadCategories();
    }
    
    /**
     * Aggiorna una categoria nel db.
     * @param category La categoria da modificare.
     * @throws ApsSystemException In caso di errore nell'accesso al db.
     */
	@Override
    public void updateCategory(Category category) throws ApsSystemException {
		try {
			this.getCategoryDAO().updateCategory(category);
		} catch (Throwable t) {
			_logger.error("Error detected while updating a category", t);
			//ApsSystemUtils.logThrowable(t, this, "updateCategory");
			throw new ApsSystemException("Error detected while updating a category", t);
		}
		this.loadCategories();
	}
	
    /**
     * Restituisce la mappa delle categorie, indicizzate per codice.
     * @return La mappa delle categorie.
     */
	private Map<String, Category> getCategories() {
		return this._categories;
	}
	
	/**
     * Restituisce la radice dell'albero delle categorie
     * @return la categoria radice
     */
	@Override
    public Category getRoot() {
        return _root;
    }
	
    /**
	 * Metodo di utilità per l'inizializzazione del servizio.
	 * Il metodo viene invocato esclusivamente quando non esiste nessuna 
	 * categoria nel db e vi è la necessità di creare la Categoria radice dell'albero.
	 * @return La categoria radice creata.
	 */
	protected Category createRoot() {
		Category root = new Category();
		root.setCode("home");
		root.setParentCode("home");
		List<Lang> langs = this.getLangManager().getLangs();
		ApsProperties titles = new ApsProperties();
		for (int i=0; i<langs.size(); i++) {
			Lang lang = (Lang) langs.get(i);
			titles.setProperty(lang.getCode(), "Home");
		}
		root.setTitles(titles);
		return root;
	}
	
	/**
	 * Restituisce una lista piatta delle categorie disponibili, 
	 * ordinate secondo la gerarchia dell'albero delle categorie.
	 * La categoria root non viene inclusa nella lista.
	 * @return La lista piatta delle categorie disponibili.
	 */
	@Override
    public List<Category> getCategoriesList() {
		List<Category> categories = new ArrayList<Category>();
		if (null != this.getRoot()) {
			for (int i=0; i<this.getRoot().getChildren().length; i++) {
				this.addCategories(categories, (Category) this.getRoot().getChildren()[i]);
			}
		}
		return categories;
	}
	
	private void addCategories(List<Category> categories, Category category){
		categories.add(category);
		for (int i=0; i<category.getChildren().length; i++) {
			this.addCategories(categories, (Category) category.getChildren()[i]);
		}
	}
    
    /**
	 * Restituisce la categoria corrispondente al codice immesso.
	 * @param categoryCode Il codice della categoria da restituire.
	 * @return La categoria richiesta.
	 */
	@Override
    public Category getCategory(String categoryCode) {
		Category category = (Category) this.getCategories().get(categoryCode);
		return category;
	}
	
	@Override
	public ITreeNode getNode(String code) {
		return this.getCategory(code);
	}
	
	protected ILangManager getLangManager() {
		return _langManager;
	}
	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}
	
	/**
	 * Restituisce la classe DAO specifica per la gestione 
	 * delle operazioni sulle categorie definite sul db.
	 * @return La classe DAO specifica.
	 */
	protected ICategoryDAO getCategoryDAO() {
		return this._categoryDao;
	}
	
	/**
	 * Setta la classe DAO specifica per la gestione 
	 * delle operazioni sulle categorie definite sul db.
	 * @param categoryDao La classe DAO specifica.
	 */
	public void setCategoryDAO(ICategoryDAO categoryDao) {
		this._categoryDao = categoryDao;
	}
    
	private ILangManager _langManager;
	
    /**
     * La radice dell'albero delle categorie
     */
    private Category _root;
	
	/**
	 * Mappa delle categorie, indicizzata in base al codice della categoria.
	 */
	private Map<String, Category> _categories;
	
	private ICategoryDAO _categoryDao;
	
}
