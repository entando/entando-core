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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.cache.ICategoryManagerCacheWrapper;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.util.DateConverter;

/**
 * Manager delle Categorie.
 *
 * @author E.Santoboni
 */
public class CategoryManager extends AbstractService implements ICategoryManager {

	private static final Logger _logger = LoggerFactory.getLogger(CategoryManager.class);

	private ILangManager _langManager;

	private ICategoryManagerCacheWrapper _cacheWrapper;

	private ICategoryDAO _categoryDao;

	@Override
	public void init() throws Exception {
		this.initCache();
		_logger.debug("{} initialized", this.getClass().getName());
	}

	private void initCache() throws ApsSystemException {
		this.getCacheWrapper().initCache(this.getCategoryDAO(), this.getLangManager());
	}

	/**
	 * Aggiunge una categoria al db.
	 *
	 * @param category La categoria da aggiungere.
	 * @throws ApsSystemException In caso di errore nell'accesso al db.
	 */
	@Override
	public void addCategory(Category category) throws ApsSystemException {
		try {
			this.getCategoryDAO().addCategory(category);
            this.getCacheWrapper().addCategory(category);
		} catch (Throwable t) {
			_logger.error("Error detected while adding a category", t);
			throw new ApsSystemException("Error detected while adding a category", t);
		}
	}

	/**
	 * Cancella una categoria.
	 *
	 * @param code Il codice della categoria da eliminare.
	 * @throws ApsSystemException in caso di errore nell'accesso al db.
	 */
	@Override
	public void deleteCategory(String code) throws ApsSystemException {
		Category cat = this.getCategory(code);
		if (cat != null && cat.getChildrenCodes().length <= 0) {
			try {
				this.getCategoryDAO().deleteCategory(code);
				this.getCacheWrapper().deleteCategory(code);
			} catch (Throwable t) {
				_logger.error("Error detected while removing the category {}", code, t);
				throw new ApsSystemException("Error detected while removing a category", t);
			}
		}
	}

	/**
	 * Aggiorna una categoria nel db.
	 *
	 * @param category La categoria da modificare.
	 * @throws ApsSystemException In caso di errore nell'accesso al db.
	 */
	@Override
	public void updateCategory(Category category) throws ApsSystemException {
		try {
			this.getCategoryDAO().updateCategory(category);
            this.getCacheWrapper().updateCategory(category);
		} catch (Throwable t) {
			_logger.error("Error detected while updating a category", t);
			throw new ApsSystemException("Error detected while updating a category", t);
		}
	}

	/**
	 * Restituisce la radice dell'albero delle categorie
	 *
	 * @return la categoria radice
	 */
	@Override
	public Category getRoot() {
		return this.getCacheWrapper().getRoot();
	}

	/**
	 * Restituisce una lista piatta delle categorie disponibili, ordinate
	 * secondo la gerarchia dell'albero delle categorie. La categoria root non
	 * viene inclusa nella lista.
	 *
	 * @return La lista piatta delle categorie disponibili.
	 */
	@Override
	public List<Category> getCategoriesList() {
		List<Category> categories = new ArrayList<>();
		if (null != this.getRoot()) {
			for (int i = 0; i < this.getRoot().getChildrenCodes().length; i++) {
				this.addCategories(categories, this.getRoot().getChildrenCodes()[i]);
			}
		}
		return categories;
	}

	private void addCategories(List<Category> categories, String categoryCode) {
		Category category = this.getCategory(categoryCode);
		categories.add(category);
		for (int i = 0; i < category.getChildrenCodes().length; i++) {
			this.addCategories(categories, category.getChildrenCodes()[i]);
		}
	}

	/**
	 * Restituisce la categoria corrispondente al codice immesso.
	 *
	 * @param categoryCode Il codice della categoria da restituire.
	 * @return La categoria richiesta.
	 */
	@Override
	public Category getCategory(String categoryCode) {
		return this.getCacheWrapper().getCategory(categoryCode);
	}

	@Override
	public ITreeNode getNode(String code) {
		return this.getCategory(code);
	}

	@Override
	public List<Category> searchCategories(String categoryCodeToken) throws ApsSystemException {
		List<Category> searchResult = new ArrayList<>();
		try {
			Category root = this.getRoot();
			this.searchCategories(root, categoryCodeToken, searchResult);
		} catch (Throwable t) {
			String message = "Error during searching categories with token " + categoryCodeToken;
			_logger.error("Error during searching categories with token {}", categoryCodeToken, t);
			throw new ApsSystemException(message, t);
		}
		return searchResult;
	}

	@Override
	public boolean moveCategory(Category currentCategory, Category newParent) throws ApsSystemException {
		boolean resultOperation = false;
		_logger.debug("start move category {} under {}", currentCategory, newParent);
		try {
			this.getCategoryDAO().moveCategory(currentCategory, newParent);
			resultOperation = true;
		} catch (Throwable t) {
			_logger.error("Error while moving  page {} under the node {}", currentCategory, newParent, t);
			throw new ApsSystemException("Error while moving a category under a different node", t);
		}
		this.initCache();
		this.startReloadCategoryReferences(currentCategory.getCode());
		return resultOperation;
	}

	private void searchCategories(Category currentTarget, String categoryCodeToken, List<Category> searchResult) {
		if ((null == categoryCodeToken || currentTarget.getCode().toLowerCase().contains(categoryCodeToken.toLowerCase()))) {
			searchResult.add(currentTarget);
		}
		String[] children = currentTarget.getChildrenCodes();
		for (int i = 0; i < children.length; i++) {
			Category category = this.getCategory(children[i]);
			this.searchCategories(category, categoryCodeToken, searchResult);
		}
	}

	@Override
	public int getMoveTreeStatus() {
		String[] utilizers = this.loadCategoryUtilizers();
		if (null == utilizers || utilizers.length == 0) {
			return STATUS_READY;
		}
		Map<String, Integer> moveNodeStatus = this.getCacheWrapper().getMoveNodeStatus();
		for (int i = 0; i < utilizers.length; i++) {
			String beanName = utilizers[i];
			if (null != moveNodeStatus && moveNodeStatus.containsKey(beanName) && moveNodeStatus.get(beanName) == STATUS_RELOADING_REFERENCES_IN_PROGRESS) {
				return STATUS_RELOADING_REFERENCES_IN_PROGRESS;
			}
		}
		return STATUS_READY;
	}

	public int getMoveTreeStatus(String currentBeanName) {
		String[] utilizers = this.loadCategoryUtilizers();
		if (null == utilizers || utilizers.length == 0) {
			return STATUS_READY;
		}
		Map<String, Integer> moveNodeStatus = this.getCacheWrapper().getMoveNodeStatus();
		if (null == moveNodeStatus) {
			return STATUS_READY;
		}
		Integer status = moveNodeStatus.get(currentBeanName);
		if (null != status && status == STATUS_RELOADING_REFERENCES_IN_PROGRESS) {
			return STATUS_RELOADING_REFERENCES_IN_PROGRESS;
		}
		return STATUS_READY;
	}

	@Override
	public Map<String, Integer> getReloadStatus() {
		Map<String, Integer> status = new HashMap<>();
		String[] utilizers = this.loadCategoryUtilizers();
		for (int i = 0; i < utilizers.length; i++) {
			String beanName = utilizers[i];
			status.put(beanName, this.getMoveTreeStatus(beanName));
		}
		return status;
	}

	private String[] loadCategoryUtilizers() {
		String[] beans = null;
		try {
			beans = BeanFactoryUtils.beanNamesForTypeIncludingAncestors((ListableBeanFactory) this.getBeanFactory(), CategoryUtilizer.class);
		} catch (Throwable t) {
			_logger.error("error loading CategoryUtilizer bean names");
			throw new RuntimeException(t);
		}
		return beans;
	}

	/**
	 * Entrypoint after category moved
	 *
	 * @param categoryCode
	 */
	public void startReloadCategoryReferences(String categoryCode) {
		if (this.getMoveTreeStatus() == STATUS_READY) {
			String[] utilizers = this.loadCategoryUtilizers();
			for (int i = 0; i < utilizers.length; i++) {
				this.createAndRunReloadCategoryReferencesThread(utilizers[i], categoryCode);
			}
		} else {
			_logger.warn("Attention: Reload Thread not stated. Expexted status {} but now it's {}", STATUS_READY, STATUS_RELOADING_REFERENCES_IN_PROGRESS);
		}
	}

	private Thread createAndRunReloadCategoryReferencesThread(String beanName, String categoryCode) {
		ReloadingCategoryReferencesThread reloadThread = null;
		int status = this.getMoveTreeStatus(beanName);
		if (status == STATUS_READY) {
			try {
				reloadThread = new ReloadingCategoryReferencesThread(this, beanName, categoryCode);
				String threadName = RELOAD_CATEGORY_REFERENCES_THREAD_NAME_PREFIX + this.getName() + "_" + beanName + "_" + DateConverter.getFormattedDate(new Date(), "yyyyMMddHHmmss");
				reloadThread.setName(threadName);
				reloadThread.start();
				_logger.info("Reloading category references for {} started", beanName);
			} catch (Throwable t) {
				throw new RuntimeException("Error while starting up the category reference reload procedure ", t);
			}
		} else {
			_logger.warn("Reloading category references for {} NOT STARTED: current status {}", beanName, status);
		}
		return reloadThread;
	}

	protected synchronized void reloadCategoryReferencesByBeanName(String beanName, String categoryCode) throws ApsSystemException {
		if (StringUtils.isBlank(beanName)) {
			throw new ApsSystemException("Error: null beanName");
		}
		this.getCacheWrapper().updateMoveNodeStatus(beanName, STATUS_RELOADING_REFERENCES_IN_PROGRESS);
		try {
			Object service = this.getBeanFactory().getBean(beanName);
			if (service != null) {
				CategoryUtilizer categoryUtilizer = (CategoryUtilizer) service;
				_logger.info("reload category references for {} started at {}", beanName, DateConverter.getFormattedDate(new Date(), "yyyyMMddHHmmss"));
				categoryUtilizer.reloadCategoryReferences(categoryCode);
				this.getCacheWrapper().updateMoveNodeStatus(beanName, STATUS_READY);
				_logger.info("reload category references for {} end at {}", beanName, DateConverter.getFormattedDate(new Date(), "yyyyMMddHHmmss"));
			}
		} catch (Throwable t) {
			_logger.error("Reload category references for: {} caused an error", beanName, t);
			throw new ApsSystemException("Error reloading entity references by bean: " + beanName, t);
		}
	}

	protected ILangManager getLangManager() {
		return _langManager;
	}

	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}

	/**
	 * Restituisce la classe DAO specifica per la gestione delle operazioni
	 * sulle categorie definite sul db.
	 *
	 * @return La classe DAO specifica.
	 */
	protected ICategoryDAO getCategoryDAO() {
		return this._categoryDao;
	}

	/**
	 * Setta la classe DAO specifica per la gestione delle operazioni sulle
	 * categorie definite sul db.
	 *
	 * @param categoryDao La classe DAO specifica.
	 */
	public void setCategoryDAO(ICategoryDAO categoryDao) {
		this._categoryDao = categoryDao;
	}

	protected ICategoryManagerCacheWrapper getCacheWrapper() {
		return _cacheWrapper;
	}

	public void setCacheWrapper(ICategoryManagerCacheWrapper cacheWrapper) {
		this._cacheWrapper = cacheWrapper;
	}

}
