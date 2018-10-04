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

import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import static com.agiletec.apsadmin.system.BaseAction.FAILURE;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.BaseResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import com.agiletec.plugins.jacms.apsadmin.resource.MultipleResourceAction;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe action a servizio della gestione attributi risorsa, estensione della
 * action gestrice delle operazioni sulle risorse. La classe ha il compito di
 * permettere l'aggiunta diretta di una nuova risorsa sia nell'archivio
 * (corrispondente al tipo) che nel contenuto che si stÃ  editando.
 *
 * @author E.Santoboni
 */
public class ExtendedResourceAction extends MultipleResourceAction {

    private static final Logger logger = LoggerFactory.getLogger(ExtendedResourceAction.class);
    private String contentOnSessionMarker;
    private String entryContentAnchorDest;

    public String entryFindResource() {
        this.setCategoryCode(null);
        return SUCCESS;
    }

    @Override
    public String save() {
        logger.debug("Save in Extended resource action for id {}", this.getResourceId());
        /*
        Map imgMetadata = new HashMap();
        if (null != getFile()) {
            logger.debug("Read Metadata file is not null");
            imgMetadata = this.getImgMetadata(this.getFile());
        } else {
            logger.debug("Read Metadata file is null");
        }
         */
        try {
            if (ApsAdminSystemConstants.ADD == this.getStrutsAction()) {
                List<ResourceInterface> saved = this.saveFiles();
                System.out.println("------------------------------");
                System.out.println(saved);
                System.out.println("------------------------------");
                /*
                this.setMetadata(imgMetadata);
                ResourceInterface resource = this.getResourceManager().addResource(this);
                 */
                this.buildEntryContentAnchorDest();
                ResourceAttributeActionHelper.joinResource(saved.get(0), this.getRequest());
            }
        } catch (ApsSystemException ex) {
            // TO IMPROVE
            return INPUT;
        } catch (Throwable t) {
            logger.error("error in save", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    protected List<ResourceInterface> saveFiles() throws ApsSystemException {
        List<ResourceInterface> addedResources = new ArrayList<>();
        logger.debug("Save in multiple resource action for id {}", this.getResourceId());
        int index = 0;
        boolean hasError = false;
        boolean deleteTempFile = false;
        File file = null;
        try {
            this.fetchFileDescriptions();
            for (String fileDescription : getFileDescriptions()) {
                BaseResourceDataBean resourceFile = null;
                if (this.isImageUpload()) {
                    file = this.createImageTempFile(getBase64Image().get(index), getFileUploadBase64ImageFileName().get(index));
                    deleteTempFile = true;
                } else {
                    file = getFile(index);
                    deleteTempFile = false;
                }
                Map imgMetadata = new HashMap();
                if (null != file) {
                    logger.debug("file is not null");
                    imgMetadata = super.getImgMetadata(file);
                    resourceFile = new BaseResourceDataBean(file);
                    if (this.isImageUpload()) {
                        logger.debug("getFileUploadBase64ImageFileName().get({}): {}", index, getFileUploadBase64ImageFileName().get(index));
                        logger.debug("getFileUploadBase64ImageContentType().get({}): {}", index, getFileUploadBase64ImageContentType().get(index));
                        resourceFile.setFileName(getFileUploadBase64ImageFileName().get(index));
                        resourceFile.setMimeType(getFileUploadBase64ImageContentType().get(index));
                    } else {
                        logger.debug("getFileUploadFileName().get({}): {}", index, getFileUploadFileName().get(index));
                        logger.debug("getFileUploadContentType().get({}): {}", index, getFileUploadFileName().get(index));
                        resourceFile.setFileName(getFileUploadFileName().get(index));
                        resourceFile.setMimeType(getFileUploadContentType().get(index));
                    }
                } else {
                    logger.debug("file is null");
                    resourceFile = new BaseResourceDataBean();
                }
                resourceFile.setDescr(fileDescription);
                resourceFile.setMainGroup(getMainGroup());
                resourceFile.setResourceType(this.getResourceType());
                resourceFile.setCategories(getCategories());
                logger.debug("Save method, action {}", this.getStrutsAction());
                resourceFile.setMetadata(imgMetadata);
                try {
                    if (ApsAdminSystemConstants.ADD == this.getStrutsAction()) {
                        ResourceInterface addedResource = this.getResourceManager().addResource(resourceFile);
                        this.addActionMessage(this.getText("message.resource.filename.uploaded",
                                new String[]{this.getFileUploadFileName().get(index)}));
                        addedResources.add(addedResource);
                    }
                } catch (ApsSystemException ex) {
                    hasError = true;
                    logger.error("error loading file {} ", this.getFileUploadFileName().get(index), ex);
                    this.addFieldError(String.valueOf(index), this.getText("error.resource.filename.uploadError",
                            new String[]{this.getFileUploadFileName().get(index)}));
                }
                if (deleteTempFile) {
                    logger.debug("Delete temp file {}", file.getAbsolutePath());
                    file.delete();
                    deleteTempFile = false;
                }
                index++;
            }
        } catch (Throwable t) {
            logger.error("error in save", t);
            throw new ApsSystemException("Error validating");
        }
        if (hasError) {
            logger.error("error uploading one or more resources");
            throw new ApsSystemException("Error validating");
        }
        return addedResources;
    }

    private void buildEntryContentAnchorDest() {
        HttpSession session = this.getRequest().getSession();
        String anchorDest = ResourceAttributeActionHelper.buildEntryContentAnchorDest(session);
        this.setEntryContentAnchorDest(anchorDest);
    }

    protected List<String> getGroupCodesForSearch() {
        List<Group> groups = this.getAllowedGroups();
        List<String> codesForSearch = new ArrayList<>();
        for (int i = 0; i < groups.size(); i++) {
            Group group = groups.get(i);
            codesForSearch.add(group.getName());
        }
        return codesForSearch;
    }

    @Override
    public List<Group> getAllowedGroups() {
        List<Group> groups = new ArrayList<Group>();
        if (this.isCurrentUserMemberOf(Group.FREE_GROUP_NAME)) {
            groups.add(this.getGroupManager().getGroup(Group.FREE_GROUP_NAME));
        }
        String contentMainGroup = this.getContent().getMainGroup();
        if (contentMainGroup != null && !contentMainGroup.equals(Group.FREE_GROUP_NAME)) {
            groups.add(this.getGroupManager().getGroup(contentMainGroup));
        }
        return groups;
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

    @Override
    public boolean isOnEditContent() {
        return true;
    }

    public boolean isContentListAttribute() {
        return (null != this.getRequest().getSession().getAttribute(ResourceAttributeActionHelper.LIST_ELEMENT_INDEX_SESSION_PARAM));
    }

    public String getContentOnSessionMarker() {
        return contentOnSessionMarker;
    }

    public void setContentOnSessionMarker(String contentOnSessionMarker) {
        this.contentOnSessionMarker = contentOnSessionMarker;
    }

    public String getEntryContentAnchorDest() {
        if (null == this.entryContentAnchorDest) {
            this.buildEntryContentAnchorDest();
        }
        return entryContentAnchorDest;
    }

    protected void setEntryContentAnchorDest(String entryContentAnchorDest) {
        this.entryContentAnchorDest = entryContentAnchorDest;
    }

}
