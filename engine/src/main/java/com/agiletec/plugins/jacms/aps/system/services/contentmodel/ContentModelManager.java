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
package com.agiletec.plugins.jacms.aps.system.services.contentmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.cache.IContentModelManagerCacheWrapper;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.event.ContentModelChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager dei modelli di contenuto.
 *
 * @author S.Didaci - C.Siddi - C.Sirigu
 */
public class ContentModelManager extends AbstractService implements IContentModelManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private IContentModelManagerCacheWrapper cacheWrapper;
    private IContentModelDAO contentModelDao;
    private IPageManager pageManager;
    private IContentManager contentManager;

    protected IContentModelDAO getContentModelDAO() {
        return contentModelDao;
    }

    public void setContentModelDAO(IContentModelDAO contentModelDao) {
        this.contentModelDao = contentModelDao;
    }

    protected IPageManager getPageManager() {
        return pageManager;
    }

    public void setPageManager(IPageManager pageManager) {
        this.pageManager = pageManager;
    }

    protected IContentManager getContentManager() {
        return contentManager;
    }

    public void setContentManager(IContentManager contentManager) {
        this.contentManager = contentManager;
    }


    protected IContentModelManagerCacheWrapper getCacheWrapper() {
        return cacheWrapper;
    }

    public void setCacheWrapper(IContentModelManagerCacheWrapper cacheWrapper) {
        this.cacheWrapper = cacheWrapper;
    }

    @Override
    public void init() throws Exception {
        this.cacheWrapper.initCache(this.getContentModelDAO());
        logger.debug("{} ready. Initialized {} content models", this.getClass().getName(), this.getCacheWrapper().getContentModels().size());
    }

    /**
     * Aggiunge un modello di contenuto nel sistema.
     *
     * @param model Il modello da aggiungere.
     * @throws ApsSystemException In caso di errori in accesso al db.
     */
    @Override
    public void addContentModel(ContentModel model) throws ApsSystemException {
        try {
            this.getContentModelDAO().addContentModel(model);
            this.getCacheWrapper().addContentModel(model);
            this.notifyContentModelChanging(model, ContentModelChangedEvent.INSERT_OPERATION_CODE);
        } catch (Throwable t) {
            logger.error("Error saving a contentModel", t);
            throw new ApsSystemException("Error saving a contentModel", t);
        }
    }

    /**
     * Rimuove un modello di contenuto dal sistema.
     *
     * @param model Il modello di contenuto da rimuovere.
     * @throws ApsSystemException In caso di errori in accesso al db.
     */
    @Override
    public void removeContentModel(ContentModel model) throws ApsSystemException {
        try {
            this.getContentModelDAO().deleteContentModel(model);
            this.getCacheWrapper().removeContentModel(model);
            this.notifyContentModelChanging(model, ContentModelChangedEvent.REMOVE_OPERATION_CODE);
        } catch (Throwable t) {
            logger.error("Error deleting a content model", t);
            throw new ApsSystemException("Error deleting a content model", t);
        }
    }

    /**
     * Aggiorna un modello di contenuto.
     *
     * @param model Il modello di contenuto da aggiornare.
     * @throws ApsSystemException In caso di errori in accesso al db.
     */
    @Override
    public void updateContentModel(ContentModel model) throws ApsSystemException {
        try {
            this.getContentModelDAO().updateContentModel(model);
            this.getCacheWrapper().updateContentModel(model);
            this.notifyContentModelChanging(model, ContentModelChangedEvent.UPDATE_OPERATION_CODE);
        } catch (Throwable t) {
            logger.error("Error updating a content model", t);
            throw new ApsSystemException("Error updating a content model", t);
        }
    }

    private void notifyContentModelChanging(ContentModel contentModel, int operationCode) throws ApsSystemException {
        ContentModelChangedEvent event = new ContentModelChangedEvent();
        event.setContentModel(contentModel);
        event.setOperationCode(operationCode);
        this.notifyEvent(event);
    }

    /**
     * Restituisce il modello relativo all'identificativo immesso.
     *
     * @param contentModelId L'identificativo del modello da estrarre.
     * @return Il modello cercato.
     */
    @Override
    public ContentModel getContentModel(long contentModelId) {
        return this.getCacheWrapper().getContentModel(String.valueOf(contentModelId));
    }

    /**
     * Restituisce la lista dei modelli di contenuto presenti nel sistema.
     *
     * @return La lista dei modelli di contenuto presenti nel sistema.
     */
    @Override
    public List<ContentModel> getContentModels() {
        List<ContentModel> models = new ArrayList<ContentModel>(this.getCacheWrapper().getContentModels());
        Collections.sort(models);
        return models;
    }

    /**
     * Restituisce la lista di modelli compatibili con il tipo di contenuto
     * specificato.
     *
     * @param contentType Il codice del tipo di contenuto.
     * @return La lista di modelli compatibili con il tipo di contenuto
     * specificato.
     */
    @Override
    public List<ContentModel> getModelsForContentType(String contentType) {
        List<ContentModel> models = new ArrayList<ContentModel>();
        Object[] allModels = this.getCacheWrapper().getContentModels().toArray();
        for (int i = 0; i < allModels.length; i++) {
            ContentModel contentModel = (ContentModel) allModels[i];
            if (null == contentType || contentModel.getContentType().equals(contentType)) {
                models.add(contentModel);
            }
        }
        return models;
    }

    /**
     * Restituisce la mappa delle pagine referenziate dal modello di contenuto
     * specificato. La mappa è indicizzata in base ai codici dei contenuti
     * pubblicati tramite il modello specificato, ed il valore è rappresentato
     * dalla lista di pagine nel quale è pubblicato esplicitamente il contenuto
     * (traite il modello specificato).
     *
     * @param modelId Identificativo del modello di contenuto
     * @return La Mappa delle pagine referenziate.
     */
    @Override
    public Map<String, List<IPage>> getReferencingPages(long modelId) {
        Map<String, List<IPage>> utilizers = new HashMap<String, List<IPage>>();
        IPage root = this.getPageManager().getDraftRoot();
        this.searchReferencingPages(modelId, root, utilizers);
        root = this.getPageManager().getOnlineRoot();
        this.searchReferencingPages(modelId, root, utilizers);
        return utilizers;
    }

    /**
     * Verifica se il modello di contenuto è utilizzato nella pagina specificata
     * e in caso affermativo aggiunge la pagina alla lista delle pagine che
     * utilizzano quel modello di contenuto. La ricerca viene estesa anche alle
     * pagine figlie di quella specificata.
     *
     * @param modelId Identificativo del modello di contenuto
     * @param page La pagina nel qual cercare il modello di contenuto
     * @param utilizers La lista delle pagine in cui è utilizzato il modello di
     * contenuto
     */
    private void searchReferencingPages(long modelId, IPage page, Map<String, List<IPage>> utilizers) {
        this.addReferencingPage(modelId, page, utilizers);
        String[] children = page.getChildrenCodes();
        boolean isOnline = page.isOnline();
        for (int i = 0; i < children.length; i++) {
            IPage child = (isOnline)
                    ? this.getPageManager().getOnlinePage(children[i])
                    : this.getPageManager().getDraftPage(children[i]);
            if (null != child) {
                this.searchReferencingPages(modelId, child, utilizers);
            }
        }
    }

    private void addReferencingPage(long modelId, IPage page, Map<String, List<IPage>> utilizers) {
        if (null != page && null != page.getWidgets()) {
            Widget[] widgets = page.getWidgets();
            for (int i = 0; i < widgets.length; i++) {
                Widget widget = widgets[i];
                if (null != widget) {
                    if (null != widget.getConfig()) {
                        String id = widget.getConfig().getProperty("modelId");
                        String contentId = widget.getConfig().getProperty("contentId");
                        if (null != id && null != contentId) {
                            long longId = new Long(id).longValue();
                            if (modelId == longId) {
                                List<IPage> pages = (List<IPage>) utilizers.get(contentId);
                                if (null == pages) {
                                    pages = new ArrayList<IPage>();
                                }
                                pages.add(page);
                                utilizers.put(contentId, pages);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public SmallContentType getDefaultUtilizer(long modelId) {
        String modelIdString = String.valueOf(modelId);
        List<SmallContentType> smallContentTypes = this.getContentManager().getSmallContentTypes();
        for (int i = 0; i < smallContentTypes.size(); i++) {
            SmallContentType smallContentType = (SmallContentType) smallContentTypes.get(i);
            Content prototype = this.getContentManager().createContentType(smallContentType.getCode());
            if ((null != prototype.getListModel() && prototype.getListModel().equals(modelIdString)) || (null != prototype.getDefaultModel() && prototype.getDefaultModel().equals(modelIdString))) {
                return smallContentType;
            }
        }
        return null;
    }

}
