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
package com.agiletec.aps.system.services.pagemodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.services.guifragment.GuiFragmentUtilizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.events.PageModelChangedEvent;
import java.util.Map;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * The manager of the page models.
 *
 * @author M.Diana - E.Santoboni
 */
public class PageModelManager extends AbstractService implements IPageModelManager, GuiFragmentUtilizer {

	private static final Logger _logger = LoggerFactory.getLogger(PageModelManager.class);

	private IPageModelDAO _pageModelDao;

	private CacheManager _springCacheManager;

	@Override
	public void init() throws Exception {
		this.loadPageModels();
		_logger.debug("{} ready. initialized", this.getClass().getName());
	}

	private void loadPageModels() throws ApsSystemException {
		try {
			Map<String, PageModel> models = this.getPageModelDAO().loadModels();
			List<String> modelCodes = new ArrayList<String>();
			Iterator<PageModel> iterator = models.values().iterator();
			while (iterator.hasNext()) {
				PageModel pageModel = iterator.next();
				this.getCache().put("PageModelManager_model_" + pageModel.getCode(), pageModel);
				modelCodes.add(pageModel.getCode());
			}
			this.getCache().put("PageModelManager_models", modelCodes);
		} catch (Throwable t) {
			_logger.error("Error loading page models", t);
			throw new ApsSystemException("Error loading page models", t);
		}
	}

	/**
	 * Restituisce il modello di pagina con il codice dato
	 *
	 * @param name Il nome del modelo di pagina
	 * @return Il modello di pagina richiesto
	 */
	@Override
	public PageModel getPageModel(String name) {
		return this.getCache().get("PageModelManager_model_" + name, PageModel.class); //this._models.get(name);
	}

	/**
	 * Restituisce la Collection completa di modelli.
	 *
	 * @return la collection completa dei modelli disponibili in oggetti
	 * PageModel.
	 */
	@Override
	public Collection<PageModel> getPageModels() {
		List<PageModel> models = new ArrayList<PageModel>();
		Cache cache = this.getCache();
		List<String> codes = cache.get("PageModelManager_models", List.class);
		System.out.println("LISTA -> " + codes);
		for (int i = 0; i < codes.size(); i++) {
			String code = codes.get(i);
			models.add(cache.get("PageModelManager_model_" + code, PageModel.class));
		}
		System.out.println("MODELLI -> " + models);
		return models;
	}

	@Override
	public void addPageModel(PageModel pageModel) throws ApsSystemException {
		if (null == pageModel) {
			_logger.debug("Null page model can be add");
			return;
		}
		try {
			Cache cache = this.getCache();
			this.getPageModelDAO().addModel(pageModel);
			cache.put("PageModelManager_model_" + pageModel.getCode(), pageModel);
			List<String> codes = cache.get("PageModelManager_models", List.class);
			codes.add(pageModel.getCode());
			cache.put("PageModelManager_models", codes);
			this.notifyPageModelChangedEvent(pageModel, PageModelChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error adding page models", t);
			throw new ApsSystemException("Error adding page models", t);
		}
	}

	@Override
	public void updatePageModel(PageModel pageModel) throws ApsSystemException {
		if (null == pageModel) {
			_logger.debug("Null page model can be update");
			return;
		}
		try {
			Cache cache = this.getCache();
			PageModel pageModelToUpdate = cache.get("PageModelManager_model_" + pageModel.getCode(), PageModel.class);
			if (null == pageModelToUpdate) {
				_logger.debug("Page model {} does not exist", pageModel.getCode());
				return;
			}
			this.getPageModelDAO().updateModel(pageModel);
			//pageModelToUpdate.setDefaultWidget(pageModel.getDefaultWidget());
			pageModelToUpdate.setDescription(pageModel.getDescription());
			//pageModelToUpdate.setFrames(pageModel.getFrames());
			pageModelToUpdate.setConfiguration(pageModel.getConfiguration());
			pageModelToUpdate.setMainFrame(pageModel.getMainFrame());
			pageModelToUpdate.setPluginCode(pageModel.getPluginCode());
			pageModelToUpdate.setTemplate(pageModel.getTemplate());
			cache.put("PageModelManager_model_" + pageModelToUpdate.getCode(), pageModelToUpdate);
			this.notifyPageModelChangedEvent(pageModelToUpdate, PageModelChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error updating page model {}", pageModel.getCode(), t);
			throw new ApsSystemException("Error updating page model " + pageModel.getCode(), t);
		}
	}

	@Override
	public void deletePageModel(String code) throws ApsSystemException {
		try {
			Cache cache = this.getCache();
			PageModel model = this.getPageModel(code);
			this.getPageModelDAO().deleteModel(code);
			cache.evict("PageModelManager_model_" + code);
			List<String> codes = cache.get("PageModelManager_models", List.class);
			codes.remove(code);
			cache.put("PageModelManager_models", codes);
			this.notifyPageModelChangedEvent(model, PageModelChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error deleting page models", t);
			throw new ApsSystemException("Error deleting page models", t);
		}
	}

	private void notifyPageModelChangedEvent(PageModel pageModel, int operationCode) {
		PageModelChangedEvent event = new PageModelChangedEvent();
		event.setPageModel(pageModel);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}

	@Override
	public List getGuiFragmentUtilizers(String guiFragmentCode) throws ApsSystemException {
		List<PageModel> utilizers = new ArrayList<PageModel>();
		try {
			Iterator<PageModel> it = this.getPageModels().iterator();
			while (it.hasNext()) {
				PageModel pModel = it.next();
				String template = pModel.getTemplate();
				if (StringUtils.isNotBlank(template)) {
					Pattern pattern = Pattern.compile("<@wp\\.fragment.*code=\"" + guiFragmentCode + "\".*/>", Pattern.MULTILINE);
					Matcher matcher = pattern.matcher(template);
					if (matcher.find()) {
						utilizers.add(pModel);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting utilizers", t);
			throw new ApsSystemException("Error extracting utilizers", t);
		}
		return utilizers;
	}

	protected Cache getCache() {
		return this.getSpringCacheManager().getCache(PAGE_MODEL_MANAGER_CACHE_NAME);
	}

	protected IPageModelDAO getPageModelDAO() {
		return _pageModelDao;
	}

	public void setPageModelDAO(IPageModelDAO pageModelDAO) {
		this._pageModelDao = pageModelDAO;
	}

	protected CacheManager getSpringCacheManager() {
		return _springCacheManager;
	}

	public void setSpringCacheManager(CacheManager springCacheManager) {
		this._springCacheManager = springCacheManager;
	}

}
