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

import java.io.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultipleResourceAction extends ResourceAction {

    private static final Logger logger = LoggerFactory.getLogger(MultipleResourceAction.class);

    private int fieldCount = 0;

    private List<String> fileDescriptions;
    private List<String> fileUploadIDs;
    private List<String> fileUploadContentTypes;
    private List<String> fileUploadFileNames;

    public final static String FILE_DESCR_FIELD = "descr_";
    public final static String FILE_UPLOAD_ID_FIELD = "fileUploadId_";
    public final static String FILE_NAME_FIELD = "fileUploadName_";
    public final static String FILE_CONTENT_TYPE_FIELD = "fileUploadContentType_";

    private List savedId = new ArrayList();
    private Map<String, String> metadata = new HashMap<>();

    @Override
    public void validate() {
        logger.debug("MultipleResourceAction validate");
        savedId.clear();
        if (ApsAdminSystemConstants.EDIT == this.getStrutsAction()) {
            this.fetchFileDescriptions();
            addFieldErrors(validateFileDescriptions());
        } else {
            this.fetchFileFields();
            addFieldErrors(validateFileDescriptions());
            addFieldErrors(validateFileUploadIDs());
            addFieldErrors(validateFileUploaNames());
            addFieldErrors(validateFileUploadContentType());
        }
    }

    private void addFieldErrors(List<FieldError> fieldErrors) {
        if (fieldErrors.isEmpty()) {
            return; // Nothing to do
        }
        for (FieldError fieldError : fieldErrors) {
            addFieldError(fieldError.fieldName, fieldError.errorMessage);
        }
    }

    private List<FieldError> validateFileDescriptions() {
        List<FieldError> errors = new ArrayList<>();
        if (fileDescriptions == null) {
            errors.add(new FieldError(FILE_DESCR_FIELD + "0", getText("error.resource.file.descrEmpty")));
            return errors;
        }

        if (fileDescriptions.isEmpty()) {
            errors.add(new FieldError(FILE_DESCR_FIELD + "0", getText("error.resource.file.descrEmpty")));
            return errors;
        }

        for (int i = 0; i < fileDescriptions.size(); i++) {
            String fileDescription = fileDescriptions.get(i);

            if (StringUtils.isEmpty(fileDescription)) {
                errors.add(new FieldError(FILE_DESCR_FIELD + i, getText("error.resource.file.descrEmpty")));
            }
            if (fileDescription.length() > 250) {
                errors.add(new FieldError(FILE_DESCR_FIELD + i, getText("error.resource.file.descrTooLong")));
            }
        }

        return errors;
    }

    private List<FieldError> validateFileUploadIDs() {
        List<FieldError> errors = new ArrayList<>();
        if (fileUploadIDs == null) {
            errors.add(new FieldError(FILE_UPLOAD_ID_FIELD + "0", getText("error.resource.filename.uploadError")));
            return errors;
        }

        if (fileUploadIDs.isEmpty()) {
            errors.add(new FieldError(FILE_UPLOAD_ID_FIELD + "0", getText("error.resource.filename.uploadError")));
            return errors;
        }

        for (int i = 0; i < fileUploadIDs.size(); i++) {
            String fileUploadID = fileUploadIDs.get(i);

            if (StringUtils.isEmpty(fileUploadID)) {
                errors.add(new FieldError(FILE_UPLOAD_ID_FIELD + i, getText("error.resource.filename.uploadError")));
            }
        }

        return errors;
    }

    private List<FieldError> validateFileUploaNames() {
        List<FieldError> errors = new ArrayList<>();
        if (fileUploadFileNames == null) {
            errors.add(new FieldError(FILE_NAME_FIELD + "0", getText("error.resource.filename.uploadError")));
            return errors;
        }

        if (fileUploadFileNames.isEmpty()) {
            errors.add(new FieldError(FILE_NAME_FIELD + "0", getText("error.resource.filename.uploadError")));
            return errors;
        }

        for (int i = 0; i < fileUploadFileNames.size(); i++) {
            String fileUploadFileName = fileUploadFileNames.get(i);

            if (StringUtils.isEmpty(fileUploadFileName)) {
                errors.add(new FieldError(FILE_NAME_FIELD + i, getText("error.resource.filename.uploadError")));
            }
        }

        return errors;
    }

    private List<FieldError> validateFileUploadContentType() {
        List<FieldError> errors = new ArrayList<>();
        if (fileUploadContentTypes == null) {
            errors.add(new FieldError(FILE_CONTENT_TYPE_FIELD + "0", getText("error.resource.filename.uploadError")));
            return errors;
        }

        if (fileUploadContentTypes.isEmpty()) {
            errors.add(new FieldError(FILE_CONTENT_TYPE_FIELD + "0", getText("error.resource.filename.uploadError")));
            return errors;
        }

        for (int i = 0; i < fileUploadContentTypes.size(); i++) {
            String fileUploadContentType = fileUploadContentTypes.get(i);

            if (StringUtils.isEmpty(fileUploadContentType)) {
                errors.add(new FieldError(FILE_CONTENT_TYPE_FIELD + i, getText("error.resource.filename.uploadError")));
            }
        }

        return errors;
    }

    @Override
    public String edit() {
        logger.debug("Edit in multiple resource action for id {}", this.getResourceId());
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

    @Override
    public String joinCategory() {
        logger.debug("JoinCategory in multiple resource action for id {}", this.getResourceId());
        fetchFileFields();
        return super.joinCategory();
    }

    @Override
    public String removeCategory() {
        logger.debug("RemoveCategory in multiple resource action for id {}", this.getResourceId());
        fetchFileFields();
        return super.removeCategory();
    }

    @Override
    public String save() {
        logger.debug("Save in multiple resource action for id {}", this.getResourceId());
        int index = 0;
        savedId.clear();
        boolean hasError = false;
        boolean deleteTempFile = false;
        File file = null;
        try {
            this.fetchFileFields();
            for (String fileDescription : getFileDescriptions()) {
                if (fileDescription.length() > 0) {
                    List<BaseResourceDataBean> baseResourceDataBeanList;
                    BaseResourceDataBean resourceFile = null;

                    String tempDir = System.getProperty("java.io.tmpdir");

                    if (listContains(fileUploadIDs, index)) {
                        if (null != getFileUploadId(index)) {
                            logger.info(" file {}", tempDir + File.separator + getFileUploadId(index) + ".tmp");
                            file = new File(tempDir + File.separator + getFileUploadId(index) + ".tmp");
                            deleteTempFile = true;
                        }
                    }
                    Map imgMetadata = new HashMap();
                    if (null != file) {
                        logger.debug("file is not null");
                        imgMetadata = super.getImgMetadata(file);
                        resourceFile = new BaseResourceDataBean(file);
                        logger.debug("getFileUploadFileName().get({}): {}", index, getFileUploadFileName(index));
                        logger.debug("getFileUploadContentType().get({}): {}", index, getFileUploadContentType(index));
                        resourceFile.setFileName(getFileUploadFileName(index));
                        resourceFile.setMimeType(getFileUploadContentType(index));
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
                    String fileUploadName = getFileUploadFileName(index);

                    try {
                        if (ApsAdminSystemConstants.ADD == this.getStrutsAction()) {
                            this.getResourceManager().addResources(baseResourceDataBeanList);
                            this.addActionMessage(this.getText("message.resource.filename.uploaded",
                                    new String[]{fileUploadName}));
                            savedId.add(index);
                        } else if (ApsAdminSystemConstants.EDIT == this.getStrutsAction()) {
                            resourceFile.setResourceId(super.getResourceId());
                            this.getResourceManager().updateResource(resourceFile);
                        }
                    } catch (ApsSystemException ex) {
                        hasError = true;
                        logger.error("error loading file {} ", fileUploadName, ex);
                        this.addFieldError(String.valueOf(index), this.getText("error.resource.filename.uploadError",
                                new String[]{fileUploadName}));
                    }
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
            return FAILURE;
        }
        if (hasError) {
            logger.error("error uploading one or more resources");
            savedId.forEach(id -> fileDescriptions.remove(id.toString()));
            return FAILURE;
        }
        return SUCCESS;
    }

    @Override
    public int getFieldCount() {
        if (ApsAdminSystemConstants.EDIT == this.getStrutsAction()) {
            return 0;
        }
        fieldCount = 0;
        Map<String, String[]> parameterMap = this.getRequest().getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (entry.getKey().startsWith(FILE_DESCR_FIELD)) {
                fieldCount++;
            }
        }
        fieldCount = fieldCount - savedId.size();
        return fieldCount - 1;
    }

    @Override
    public void setFieldCount(int fieldCount) {
        this.fieldCount = fieldCount;
    }

    public List<String> getFileDescriptions() {
        return fileDescriptions;
    }

    public void setFileDescriptions(List<String> fileDescriptions) {
        this.fileDescriptions = fileDescriptions;
    }

    public List<String> getFileUploadID() {
        return fileUploadIDs;
    }

    public void setFileUpload(List<String> fileUploadIDs) {
        this.fileUploadIDs = fileUploadIDs;
    }

    public List<String> getFileUploadIDs() {
        return fileUploadIDs;
    }

    public void setFileUploadIDs(List<String> fileUploadIDs) {
        this.fileUploadIDs = fileUploadIDs;
    }

    public List<String> getFileUploadFileNames() {
        return fileUploadFileNames;
    }

    public void setFileUploadFileNames(List<String> fileUploadFileNames) {
        this.fileUploadFileNames = fileUploadFileNames;
    }

    public List<String> getFileUploadContentTypes() {
        return fileUploadContentTypes;
    }

    public void setFileUploadContentTypes(List<String> fileUploadContentTypes) {
        this.fileUploadContentTypes = fileUploadContentTypes;
    }

    public List<String> getFileUploadFileName() {
        return fileUploadFileNames;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    protected List<String> fetchFields(String fieldPrefix) {
        logger.debug("fetchFields with prefix {}", fieldPrefix);
        List<String> values = new ArrayList<>();
        Map<String, String[]> parameterMap = this.getRequest().getParameterMap();
        logger.debug("parameterMap {}", parameterMap);
        SortedSet<String> keys = new TreeSet<>(parameterMap.keySet());
        int i = 0;
        for (String key : keys) {
            if (key.startsWith(fieldPrefix)) {
                String val = parameterMap.get(key)[0];
                values.add(i, val);
                logger.debug("values.add {} {}", i, val);
                i++;
            }
        }
        return values;
    }

    protected void fetchFileFields() {
        logger.debug("fetchFileFields");
        fetchFileDescriptions();
        fetchFileUploadIDs();
        fetchFileUploadContentTypes();
        fetchFileUploadFileNames();
    }

    protected void fetchFileDescriptions() {
        fileDescriptions = fetchFields(FILE_DESCR_FIELD);
        logger.debug("fetchFileDescriptions {}", fileDescriptions);
    }

    protected void fetchFileUploadIDs() {
        fileUploadIDs = fetchFields(FILE_UPLOAD_ID_FIELD);
        logger.debug("fetchFileUploadIDs {}", fileUploadIDs);
    }

    protected void fetchFileUploadContentTypes() {
        fileUploadContentTypes = fetchFields(FILE_CONTENT_TYPE_FIELD);
        logger.debug("fetchFileUploadContentTypes {}", fileUploadContentTypes);
    }

    protected void fetchFileUploadFileNames() {
        fileUploadFileNames = fetchFields(FILE_NAME_FIELD);
        logger.debug("fetchFileUploadFileNames {}", fileUploadContentTypes);

    }

    protected boolean listContains(List list, int i) {
        return list != null && i <= list.size();
    }

    public String getFileDescription(int i) {
        if (null != fileDescriptions
                && !fileDescriptions.isEmpty()) {
            return fileDescriptions.get(i);
        }
        return null;
    }

    public String getFileUploadId(int i) {
        if (null != fileUploadIDs
                && !fileUploadIDs.isEmpty()) {
            return fileUploadIDs.get(i);
        }
        return null;
    }

    public String getFileUploadFileName(int i) {
        if (null != fileUploadFileNames
                && !fileUploadFileNames.isEmpty()) {
            return fileUploadFileNames.get(i);
        }
        return null;
    }

    public String getFileUploadContentType(int i) {
        if (null != fileUploadContentTypes
                && !fileUploadContentTypes.isEmpty()) {
            return fileUploadContentTypes.get(i);
        }
        return null;
    }
}
