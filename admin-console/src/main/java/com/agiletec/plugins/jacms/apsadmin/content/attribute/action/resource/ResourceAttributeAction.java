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
package com.agiletec.plugins.jacms.apsadmin.content.attribute.action.resource;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import com.agiletec.plugins.jacms.apsadmin.content.helper.IContentActionHelper;

/**
 * Classe action base delegata alla gestione base delle operazione sugli
 * attributi risorsa. L'azione rappresenta l'entry point per tutte le richieste
 * effettuate dall'interfaccia di redazione contenuto.
 *
 * @author E.Santoboni
 */
public class ResourceAttributeAction extends BaseAction implements IResourceAttributeAction {

    private static final Logger _logger = LoggerFactory.getLogger(ResourceAttributeAction.class);

    @Override
    public String chooseResource() {
        try {
            ResourceAttributeActionHelper.removeSessionParams(this.getRequest().getSession());
            this.getContentActionHelper().updateEntity(this.getContent(), this.getRequest());
            ResourceAttributeActionHelper.initSessionParams(this, this.getRequest());
            String resourceTypeCode = (String) this.getRequest().getSession().getAttribute(ResourceAttributeActionHelper.RESOURCE_TYPE_CODE_SESSION_PARAM);
            this.setResourceTypeCode(resourceTypeCode);
        } catch (Throwable t) {
            _logger.error("error in findResource", t);
            //ApsSystemUtils.logThrowable(t, this, "findResource");
            return FAILURE;
        }
        //POI FA IL FORWARD ALLA FINDER Resource
        return SUCCESS;
    }

    @Override
    public String removeResource() {
        try {
            this.getContentActionHelper().updateEntity(this.getContent(), this.getRequest());
            ResourceAttributeActionHelper.initSessionParams(this, this.getRequest());
            ResourceAttributeActionHelper.removeResource(this.getRequest());
        } catch (Throwable t) {
            _logger.error("error in removeResource", t);
            //ApsSystemUtils.logThrowable(t, this, "removeResource");
            return FAILURE;
        }
        return SUCCESS;
    }

    public String backToEntryContent() {
        HttpSession session = this.getRequest().getSession();
        String anchorDest = ResourceAttributeActionHelper.buildEntryContentAnchorDest(session);
        this.setEntryContentAnchorDest(anchorDest);
        ResourceAttributeActionHelper.removeSessionParams(session);
        return SUCCESS;
    }

    /**
     * Restituisce il contenuto in sesione.
     *
     * @return Il contenuto in sesione.
     */
    public Content getContent() {
        return (Content) this.getRequest().getSession()
                .getAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + this.getContentOnSessionMarker());
    }

    public String getEntryContentAnchorDestFromRemove() {
        StringBuilder buffer = new StringBuilder("contentedit_");
        buffer.append(this.getResourceLangCode());
        buffer.append("_");
        if (null != this.getParentAttributeName()) {
            buffer.append(this.getParentAttributeName());
        } else {
            buffer.append(this.getAttributeName());
        }
        return buffer.toString();
    }

    public String getContentOnSessionMarker() {
        return _contentOnSessionMarker;
    }

    public void setContentOnSessionMarker(String contentOnSessionMarker) {
        this._contentOnSessionMarker = contentOnSessionMarker;
    }

    @Override
    public String getAttributeName() {
        return _attributeName;
    }

    public void setAttributeName(String attributeName) {
        this._attributeName = attributeName;
    }

    @Override
    public String getParentAttributeName() {
        return _parentAttributeName;
    }

    public void setParentAttributeName(String parentAttributeName) {
        this._parentAttributeName = parentAttributeName;
    }

    @Override
    public int getElementIndex() {
        return _elementIndex;
    }

    public void setElementIndex(int elementIndex) {
        this._elementIndex = elementIndex;
    }

    @Override
    public String getResourceLangCode() {
        return _resourceLangCode;
    }

    public void setResourceLangCode(String resourceLangCode) {
        this._resourceLangCode = resourceLangCode;
    }

    @Override
    public String getResourceTypeCode() {
        return _resourceTypeCode;
    }

    public void setResourceTypeCode(String resourceTypeCode) {
        this._resourceTypeCode = resourceTypeCode;
    }

    public String getEntryContentAnchorDest() {
        if (null == this._entryContentAnchorDest) {
            HttpSession session = this.getRequest().getSession();
            String anchorDest = ResourceAttributeActionHelper.buildEntryContentAnchorDest(session);
            this.setEntryContentAnchorDest(anchorDest);
        }
        return _entryContentAnchorDest;
    }

    protected void setEntryContentAnchorDest(String entryContentAnchorDest) {
        this._entryContentAnchorDest = entryContentAnchorDest;
    }

    /**
     * Restituisce la classe helper della gestione contenuti.
     *
     * @return La classe helper della gestione contenuti.
     */
    protected IContentActionHelper getContentActionHelper() {
        return _contentActionHelper;
    }

    /**
     * Setta la classe helper della gestione contenuti.
     *
     * @param contentActionHelper La classe helper della gestione contenuti.
     */
    public void setContentActionHelper(IContentActionHelper contentActionHelper) {
        this._contentActionHelper = contentActionHelper;
    }

    private String _contentOnSessionMarker;

    private String _attributeName;
    private String _parentAttributeName;
    private int _elementIndex = -1;
    private String _resourceLangCode;
    private String _resourceTypeCode;

    private String _entryContentAnchorDest;

    private IContentActionHelper _contentActionHelper;

}
