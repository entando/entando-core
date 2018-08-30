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
package com.agiletec.plugins.jacms.apsadmin.resource;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.BaseResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Tag;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultipleResourceAction extends ResourceAction {

    private static final Logger logger = LoggerFactory.getLogger(MultipleResourceAction.class);

    private int fieldCount = 0;
    private List<String> fileDescriptions;
    private List<File> fileUpload = new ArrayList<>();
    private List<String> fileUploadContentType = new ArrayList<>();
    private List<String> fileUploadFileName = new ArrayList<>();
    public final static String DESCR_FIELD = "descr_";
    public final static String FILE_UPLOAD_FIELD = "fileUpload_";
    private List savedId = new ArrayList();
    private Map<String, String> metadata = new HashMap<>();

    @Override
    public void validate() {
        savedId.clear();
        if (ApsAdminSystemConstants.EDIT == this.getStrutsAction()) {
            fetchFileDescriptions();
            if (null == getFileDescriptions()) {
                this.addFieldError(DESCR_FIELD + 0, getText("error.resource.file.descrEmpty"));
            }
            if (null == getFileUpload()) {
                this.addFieldError(FILE_UPLOAD_FIELD, this.getText("error.resource.file.fileEmpty"));
            }
            if (this.getFileUploadFileName().size() > 0) {
                ResourceInterface resourcePrototype = this.getResourceManager().createResourceType(this.getResourceType());
                this.checkRightFileType(resourcePrototype, this.getFileUploadFileName().get(0));
            }
        } else {
            try {
                fetchFileDescriptions();
                for (int i = 0; i < getFileDescriptions().size(); i++) {
                    if (null == getFileUploadInputStream(i)) {
                        this.addFieldError(FILE_UPLOAD_FIELD + i, this.getText("error.resource.file.void"));
                    }
                }
            } catch (Throwable ex) {
                this.addFieldError(FILE_UPLOAD_FIELD, this.getText("error.resource.file.void"));
            }
            if (null != getFileUpload()) {
                validateFileDescriptions();
                if (null != this.getResourceType()) {
                    ResourceInterface resourcePrototype = this.getResourceManager().createResourceType(this.getResourceType());
                    this.getFileUploadFileName().forEach(fileName
                            -> checkRightFileType(resourcePrototype, fileName));
                } else {
                    this.addFieldError(FILE_UPLOAD_FIELD, this.getText("error.resource.file.genericError"));
                }
            }
        }
    }

    @Override
    public String edit() {
        logger.debug("Edit");
        try {
            savedId.clear();
            ResourceInterface resource = this.loadResource(this.getResourceId());
            this.setResourceTypeCode(resource.getType());
            List<String> fileDescr = new ArrayList<>();
            fileDescr.add(resource.getDescription());
            setFileDescriptions(fileDescr);
            List<Category> resCategories = resource.getCategories();
            for (int i = 0; i < resCategories.size(); i++) {
                Category resCat = resCategories.get(i);
                this.getCategoryCodes().add(resCat.getCode());
            }
            this.setMainGroup(resource.getMainGroup());
            this.setStrutsAction(ApsAdminSystemConstants.EDIT);
            this.setMetadata(resource.getMetadata());
        } catch (Throwable t) {
            logger.error("error in edit", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    protected void validateFileDescriptions() {
        int fileUploadSize = 0;
        if (null != this.getFileUpload()) {
            fileUploadSize = this.getFileUpload().size();
        }
        for (int i = 0; i < fileUploadSize; i++) {
            if (null != fileDescriptions) {
                if (!(fileDescriptions.get(i).length() > 0)) {
                    this.addFieldError(DESCR_FIELD + i, this.getText("error.resource.file.descrEmpty"));
                }
                if (fileDescriptions.get(i).length() > 250) {
                    this.addFieldError(DESCR_FIELD + i, this.getText("error.resource.file.descrTooLong"));
                }
            } else {
                this.addFieldError(DESCR_FIELD + i, this.getText("error.resource.file.descrEmpty"));
            }
        }
    }

    @Override
    public String joinCategory() {
        logger.debug("joinCategory");
        fetchFileDescriptions();
        fetchMetadataEdit();
        return super.joinCategory();
    }

    @Override
    public String removeCategory() {
        logger.debug("removeCategory");
        fetchFileDescriptions();
        fetchMetadataEdit();
        return super.removeCategory();
    }

    protected void fetchMetadataEdit() {
        if (ApsAdminSystemConstants.EDIT == this.getStrutsAction()) {
            try {
                this.setMetadata(this.loadResource(this.getResourceId()).getMetadata());
            } catch (Throwable ex) {
                logger.error("error reading resource {} on fetchMetadataEdit {}", this.getResourceId(), ex);
            }
            logger.debug("resource {} metadata size: {}", this.getResourceId(), getMetadata().size());
        }
    }

    protected void checkRightFileType(ResourceInterface resourcePrototype, String fileName) {
        boolean isRight = false;
        if (fileName.length() > 0) {
            String docType = fileName.substring(fileName.lastIndexOf('.') + 1).trim();
            String[] types = resourcePrototype.getAllowedFileTypes();
            isRight = isValidType(docType, types);
        } else {
            isRight = true;
        }
        if (!isRight) {
            this.addFieldError("upload", this.getText("error.resource.file.wrongFormat"));
        }
    }

    @Override
    protected boolean isValidType(String docType, String[] rightTypes) {
        boolean isValid = false;
        if (rightTypes.length > 0) {
            for (int i = 0; i < rightTypes.length; i++) {
                if (docType.toLowerCase().equals(rightTypes[i])) {
                    isValid = true;
                    break;
                }
            }
        } else {
            isValid = true;
        }
        return isValid;
    }

    @Override
    public String save() {
        logger.debug("Save");
        int index = 0;
        savedId.clear();
        boolean hasError = false;
        try {
            this.fetchFileDescriptions();
            for (String fileDescription : getFileDescriptions()) {
                if (fileDescription.length() > 0) {
                    List<BaseResourceDataBean> baseResourceDataBeanList;
                    BaseResourceDataBean resourceFile = null;
                    File file = getFile(index);
                    Map imgMetadata = new HashMap();
                    if (null != file) {
                        logger.debug("file is not null");
                        imgMetadata = getImgMetadata(file);
                        resourceFile = new BaseResourceDataBean(file);
                        resourceFile.setFileName(getFileUploadFileName().get(index));
                        resourceFile.setMimeType(getFileUploadContentType().get(index));
                    } else {
                        logger.debug("file is null");
                        resourceFile = new BaseResourceDataBean();
                    }
                    resourceFile.setDescr(fileDescription);
                    resourceFile.setMainGroup(getMainGroup());
                    resourceFile.setResourceType(this.getResourceType());
                    resourceFile.setCategories(getCategories());
                    logger.debug("Save method, action {}", this.getStrutsAction());
                    if (ApsAdminSystemConstants.EDIT == this.getStrutsAction()) {
                        logger.debug("Edit resource > metadata size: {}", imgMetadata.size());
                        if (imgMetadata.size() > 0) {
                            logger.debug("Edit resource > metadata size: {}  -> update the metadata list", imgMetadata.size());
                            resourceFile.setMetadata(imgMetadata);
                        } else {
                            fetchMetadataEdit();
                            logger.debug("Edit resource > metadata size: 0  -> use previous metadata list", getMetadata());
                            resourceFile.setMetadata(getMetadata());
                        }
                    } else {
                        resourceFile.setMetadata(imgMetadata);
                    }

                    baseResourceDataBeanList = new ArrayList<BaseResourceDataBean>();
                    baseResourceDataBeanList.add(resourceFile);
                    try {
                        if (ApsAdminSystemConstants.ADD == this.getStrutsAction()) {
                            this.getResourceManager().addResources(baseResourceDataBeanList);
                            this.addActionMessage(this.getText("message.resource.filename.uploaded",
                                    new String[]{fileUploadFileName.get(index)}));
                            savedId.add(index);
                        } else if (ApsAdminSystemConstants.EDIT == this.getStrutsAction()) {
                            resourceFile.setResourceId(super.getResourceId());
                            this.getResourceManager().updateResource(resourceFile);
                        }
                    } catch (ApsSystemException ex) {
                        hasError = true;
                        logger.error("error loading file {} ", fileUploadFileName.get(index), ex);
                        this.addFieldError(String.valueOf(index), this.getText("error.resource.filename.uploadError",
                                new String[]{fileUploadFileName.get(index)}));
                    }
                }
                index++;
            }
        } catch (Throwable t) {
            logger.error("error in save", t);
            return FAILURE;
        }
        if (hasError) {
            logger.error("error uploading one or more resources");
            savedId.forEach(id -> fileDescriptions.remove(id.toString()));
            return FAILURE;
        }
        return SUCCESS;
    }

    public Map getImgMetadata(File file) {
        logger.debug("Get image Metadata");
        Map<String, String> meta = new HashMap<>();
        ResourceInterface resourcePrototype = this.getResourceManager().createResourceType(this.getResourceType());
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            String ignoreKeysConf = resourcePrototype.getMetadataIgnoreKeys();
            String[] ignoreKeys = null;
            if (null != ignoreKeysConf) {
                ignoreKeys = ignoreKeysConf.split(",");
                logger.debug("Metadata ignoreKeys: {}", ignoreKeys);
            } else {
                logger.debug("Metadata ignoreKeys not configured");
            }
            List<String> ignoreKeysList = new ArrayList<String>();
            if (null != ignoreKeys) {
                ignoreKeysList = Arrays.asList(ignoreKeys);
            }
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    if (!ignoreKeysList.contains(tag.getTagName())) {
                        logger.debug("Add Metadata with key: {}", tag.getTagName());
                        meta.put(tag.getTagName(), tag.getDescription());
                    } else {
                        logger.debug("Skip Metadata key {}", tag.getTagName());
                    }
                }
            }
        } catch (ImageProcessingException ex) {
            logger.error("Error reading metadata");
        } catch (IOException ioex) {
            logger.error("Error reading file");
        }
        return meta;
    }

    public String getFileDescription(int i) {
        if (null != fileDescriptions
                && !fileDescriptions.isEmpty()) {
            return fileDescriptions.get(i);
        }
        return "";
    }

    protected void fetchFileDescriptions() {
        if (null == fileDescriptions) {
            fileDescriptions = new ArrayList<>();
        }
        fileDescriptions.clear();
        Map<String, String[]> parameterMap = this.getRequest().getParameterMap();
        SortedSet<String> keys = new TreeSet<>(parameterMap.keySet());
        int i = 0;
        for (String key : keys) {
            if (key.startsWith(DESCR_FIELD)) {
                String descr = parameterMap.get(key)[0];
                fileDescriptions.add(i, descr);
                i++;
            }
        }
    }

    public File getFile(int index) {
        if (fileUpload.size() == 0) {
            return null;
        }
        return fileUpload.get(index);
    }

    public InputStream getFileUploadInputStream(int i) throws Throwable {
        if (null == this.getFileUpload()) {
            return null;
        }
        return new FileInputStream(getFile(i));
    }

    public int getFieldCount() {
        if (ApsAdminSystemConstants.EDIT == this.getStrutsAction()) {
            return 0;
        }

        fieldCount = 0;
        Map<String, String[]> parameterMap = this.getRequest().getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (entry.getKey().startsWith(DESCR_FIELD)) {
                fieldCount++;
            }
        }

        fieldCount = fieldCount - savedId.size();

        return fieldCount - 1;
    }

    public void setFieldCount(int fieldCount) {
        this.fieldCount = fieldCount;
    }

    public List<String> getFileDescriptions() {
        return fileDescriptions;
    }

    public void setFileDescriptions(List<String> fileDescriptions) {
        this.fileDescriptions = fileDescriptions;
    }

    public List<File> getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(List<File> fileUpload) {
        this.fileUpload = fileUpload;
    }

    public List<String> getFileUploadContentType() {
        return fileUploadContentType;
    }

    public void setFileUploadContentType(List<String> fileUploadContentType) {
        this.fileUploadContentType = fileUploadContentType;
    }

    public List<String> getFileUploadFileName() {
        return fileUploadFileName;
    }

    public String getFileUploadFileName(int i) {
        if (null != fileUploadFileName) {
            return fileUploadFileName.get(i);
        }
        return "";
    }

    public void setFileUploadFileName(List<String> fileUploadFileName) {
        this.fileUploadFileName = fileUploadFileName;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

}
