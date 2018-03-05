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

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.cache.IPageModelManagerCacheWrapper;
import com.agiletec.aps.system.services.pagemodel.events.PageModelChangedEvent;
import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.services.guifragment.GuiFragmentUtilizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The manager of the page models.
 *
 * @author M.Diana - E.Santoboni
 */
public class PageModelManager extends AbstractService implements IPageModelManager, GuiFragmentUtilizer {

    private static final Logger _logger = LoggerFactory.getLogger(PageModelManager.class);

    private IPageModelDAO _pageModelDao;

    private IPageModelManagerCacheWrapper _cacheWrapper;

    @Override
    public void init() throws Exception {
        this.getCacheWrapper().initCache(this.getPageModelDAO());
        _logger.debug("{} ready. initialized", this.getClass().getName());
    }

    /**
     * Restituisce il modello di pagina con il codice dato
     *
     * @param name Il nome del modelo di pagina
     * @return Il modello di pagina richiesto
     */
    @Override
    public PageModel getPageModel(String name) {
        return this.getCacheWrapper().getPageModel(name);
    }

    /**
     * Restituisce la Collection completa di modelli.
     *
     * @return la collection completa dei modelli disponibili in oggetti
     * PageModel.
     */
    @Override
    public Collection<PageModel> getPageModels() {
        return this.getCacheWrapper().getPageModels();
    }

    @Override
    public void addPageModel(PageModel pageModel) throws ApsSystemException {
        if (null == pageModel) {
            _logger.debug("Null page model can be add");
            return;
        }
        try {
            this.getPageModelDAO().addModel(pageModel);
            this.getCacheWrapper().addPageModel(pageModel);
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
            PageModel pageModelToUpdate = this.getCacheWrapper().getPageModel(pageModel.getCode());
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
            this.getCacheWrapper().updatePageModel(pageModelToUpdate);
            this.notifyPageModelChangedEvent(pageModelToUpdate, PageModelChangedEvent.UPDATE_OPERATION_CODE);
        } catch (Throwable t) {
            _logger.error("Error updating page model {}", pageModel.getCode(), t);
            throw new ApsSystemException("Error updating page model " + pageModel.getCode(), t);
        }
    }

    @Override
    public void deletePageModel(String code) throws ApsSystemException {
        try {
            PageModel model = this.getPageModel(code);
            this.getPageModelDAO().deleteModel(code);
            this.getCacheWrapper().deletePageModel(code);
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

    protected IPageModelManagerCacheWrapper getCacheWrapper() {
        return _cacheWrapper;
    }

    public void setCacheWrapper(IPageModelManagerCacheWrapper cacheWrapper) {
        this._cacheWrapper = cacheWrapper;
    }

    protected IPageModelDAO getPageModelDAO() {
        return _pageModelDao;
    }

    public void setPageModelDAO(IPageModelDAO pageModelDAO) {
        this._pageModelDao = pageModelDAO;
    }

    @Override
    public SearcherDaoPaginatedResult<PageModel> searchPageModels(List<FieldSearchFilter> filtersList) throws ApsSystemException {
        SearcherDaoPaginatedResult<PageModel> pagedResult = null;
        try {
            FieldSearchFilter[] filters = null;
            if (null != filtersList) {
                filters = filtersList.toArray(new FieldSearchFilter[filtersList.size()]);
            }
            List<PageModel> pageModels = new ArrayList<>();
            int count = this.getPageModelDAO().count(filters);

            List<String> pageModelCodes = this.getPageModelDAO().search(filters);
            for (String code : pageModelCodes) {
                pageModels.add(this.getPageModel(code));
            }
            pagedResult = new SearcherDaoPaginatedResult<PageModel>(count, pageModels);
        } catch (Throwable t) {
            _logger.error("Error searching groups", t);
            throw new ApsSystemException("Error searching groups", t);
        }
        return pagedResult;

    }

}
