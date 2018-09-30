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
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Base64;
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
    private List<File> fileUpload;
    private List<String> fileUploadContentType;
    private List<String> fileUploadFileName;
    private List<String> base64Image;

    private List<String> fileUploadBase64ImageContentType;
    private List<String> fileUploadBase64ImageFileName;

    public final static String DESCR_FIELD = "descr_";
    public final static String FILE_UPLOAD_FIELD = "fileUpload_";

    private List savedId = new ArrayList();
    private Map<String, String> metadata = new HashMap<>();

    @Override
    public void validate() {
        logger.debug("MultipleResourceAction validate");
        savedId.clear();
        if (ApsAdminSystemConstants.EDIT == this.getStrutsAction()) {
            logger.debug("MultipleResourceAction validate EDIT");

            fetchFileDescriptions();
            if (null == getFileDescriptions()) {
                this.addFieldError(DESCR_FIELD + 0, getText("error.resource.file.descrEmpty"));
                logger.error("Add error -> descriptions are empty, null == getFileDescriptions()");
            }
            if (this.isImageUpload()) {
                if (null != this.getFileUploadBase64ImageFileName()) {
                    ResourceInterface resourcePrototype = this.getResourceManager().createResourceType(this.getResourceType());
                    this.checkRightFileType(resourcePrototype, this.getFileUploadBase64ImageFileName().get(0));
                }
            }
            else{
                if (null != this.getFileUploadFileName()) {
                    ResourceInterface resourcePrototype = this.getResourceManager().createResourceType(this.getResourceType());
                    this.checkRightFileType(resourcePrototype, this.getFileUploadFileName().get(0));
                }
            }
        } else {

            if (this.isImageUpload()) {
                validateImages();
            } else {
                validateAttachments();
            }
        }
    }

    public void validateImages() {
        logger.debug("MultipleResourceAction validateImages MULTIPLE UPLOAD IMAGES");
        try {
            fetchFileDescriptions();
            for (int i = 0; i < getFileDescriptions().size(); i++) {
                if (null == getBase64Image(i)) {
                    this.addFieldError(FILE_UPLOAD_FIELD + i, this.getText("error.resource.file.void"));
                    logger.error("Add error -> files in base64 string is empty, null == getBase64Image({})", i);
                }
            }
        } catch (Throwable ex) {
            this.addFieldError(FILE_UPLOAD_FIELD, this.getText("error.resource.file.void"));
            logger.error("Add error -> files in base64 string is empty. Exception:\n{}", ex);
        }
        if (null != getBase64Image()) {
            validateFileDescriptions();
            if (null != this.getResourceType()) {
                ResourceInterface resourcePrototype = this.getResourceManager().createResourceType(this.getResourceType());
                this.getBase64Image().forEach(image
                        -> checkImageFileType(resourcePrototype, image));
            } else {
                this.addFieldError(FILE_UPLOAD_FIELD, this.getText("error.resource.file.genericError"));
                logger.error("Add error -> genericError");
            }
        }

    }

    public void validateAttachments() {
        logger.debug("MultipleResourceAction validateAttachments MULTIPLE UPLOAD FILES");
        try {
            fetchFileDescriptions();
            for (int i = 0; i < getFileDescriptions().size(); i++) {
                if (null == getFileUploadInputStream(i)) {
                    this.addFieldError(FILE_UPLOAD_FIELD + i, this.getText("error.resource.file.void"));
                    logger.error("Add error -> files is void, null == getFileUploadInputStream({})", i);

                }
            }
        } catch (Throwable ex) {
            this.addFieldError(FILE_UPLOAD_FIELD, this.getText("error.resource.file.void"));
            logger.error("Add error -> files is void. Exception:\n{}", ex);

        }
        if (null != getFileUpload()) {
            validateFileDescriptions();
            if (null != this.getResourceType()) {
                ResourceInterface resourcePrototype = this.getResourceManager().createResourceType(this.getResourceType());
                this.getFileUploadFileName().forEach(fileName
                        -> checkRightFileType(resourcePrototype, fileName));
            } else {
                this.addFieldError(FILE_UPLOAD_FIELD, this.getText("error.resource.file.genericError"));
                logger.error("Add error -> genericError");
            }
        }

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
        logger.debug("JoinCategory in multiple resource action for id {}", this.getResourceId());
        fetchFileDescriptions();
        return super.joinCategory();
    }

    @Override
    public String removeCategory() {
        logger.debug("RemoveCategory in multiple resource action for id {}", this.getResourceId());
        fetchFileDescriptions();
        return super.removeCategory();
    }

    protected void checkRightFileType(ResourceInterface resourcePrototype, String fileName) {
        boolean isRight = false;
        if (fileName.length() > 0) {
            String docType = fileName.substring(fileName.lastIndexOf('.') + 1).trim();
            String[] types = resourcePrototype.getAllowedFileTypes();
            isRight = isValidType(docType, types);
        } else {
            isRight = true;
            logger.debug("checkRightFileType -> file format allowed");
        }
        if (!isRight) {
            this.addFieldError("upload", this.getText("error.resource.file.wrongFormat"));
            logger.debug("Add error -> image wrongFormat");
        }
    }

    protected void checkImageFileType(ResourceInterface resourcePrototype, String imageBase64) {
        logger.debug("checkImageFileType");

        boolean isRight = false;
        if (imageBase64.length() > 0) {
            String partSeparator = ",";
            if (imageBase64.contains(partSeparator)) {
                String imgBase64FileType = imageBase64.split(partSeparator)[0];
                logger.debug("Split string image: File Format {}", imageBase64.split(partSeparator)[0]);

                String imgType = imgBase64FileType.substring(imgBase64FileType.lastIndexOf("/") + 1, imgBase64FileType.indexOf("base64") - 1).trim();
                logger.debug("imgType {}", imgType);

                String[] types = resourcePrototype.getAllowedFileTypes();
                isRight = isValidType(imgType, types);

            } else {
                isRight = false;
                logger.debug("Add error -> file wrongFormat");
            }

        } else {
            isRight = true;
            logger.debug("checkImageFileType -> file format allowed");
        }
        if (!isRight) {
            this.addFieldError("upload", this.getText("error.resource.file.wrongFormat"));
            logger.debug("Add error -> file wrongFormat");
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

    public boolean isImageUpload() {
        if (null != this.getBase64Image()) {
            return true;
        }
        return false;
    }

    public File createImageTempFile(String imageBase64) {
        logger.debug("createImageTempFile for multiple resource action");
        File file = null;
        try {
            String partSeparator = ",";
            byte[] decodedImg;
            if (imageBase64.contains(partSeparator)) {
                String encodedImg = imageBase64.split(partSeparator)[1];
                logger.debug("Split string image: File Format {}", imageBase64.split(partSeparator)[0]);
                decodedImg = Base64.getDecoder().decode(encodedImg.getBytes(StandardCharsets.UTF_8));
            } else {
                logger.warn("partSeparator not found");
                decodedImg = Base64.getDecoder().decode(imageBase64.getBytes(StandardCharsets.UTF_8));
            }

            String tempDir = System.getProperty("java.io.tmpdir");
            logger.debug("File tempDir get from System.getProperty(\"java.io.tmpdir\"): {}", tempDir);

            file = File.createTempFile("tmp", null, new File(tempDir));

            logger.debug("Temp File path: {}", file.getAbsolutePath());
            URI pathUri = new URI("file://" + file.getAbsolutePath());

            Path destinationFile = Paths.get(pathUri);
            logger.debug("Temp file destinationFile path: {}", destinationFile);

            Files.write(destinationFile, decodedImg);

        } catch (IOException ex1) {
            logger.error("{}", ex1);
        } catch (URISyntaxException ex2) {
            logger.error("{}", ex2);
        }
        return file;
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
            this.fetchFileDescriptions();
            for (String fileDescription : getFileDescriptions()) {
                if (fileDescription.length() > 0) {
                    List<BaseResourceDataBean> baseResourceDataBeanList;
                    BaseResourceDataBean resourceFile = null;
                    if (this.isImageUpload()) {
                        file = createImageTempFile(getBase64Image().get(index));
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
                            logger.debug("getFileUploadBase64ImageFileName().get({}): {}",index,getFileUploadBase64ImageFileName().get(index));
                            logger.debug("getFileUploadBase64ImageContentType().get({}): {}",index, getFileUploadBase64ImageContentType().get(index));
                            resourceFile.setFileName(getFileUploadBase64ImageFileName().get(index));
                            resourceFile.setMimeType(getFileUploadBase64ImageContentType().get(index));
                        }
                        else{
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
        if (null == fileUpload) {
            return null;
        }
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

    public List<String> getFileUploadBase64ImageContentType() {
        return fileUploadBase64ImageContentType;
    }

    public void setFileUploadBase64ImageContentType(List<String> fileUploadBase64ImageContentType) {
        this.fileUploadBase64ImageContentType = fileUploadBase64ImageContentType;
    }

    public List<String> getFileUploadBase64ImageFileName() {
        return fileUploadBase64ImageFileName;
    }
    public String getFileUploadBase64ImageFileName(int i) {
        if (null != fileUploadBase64ImageFileName) {
            return fileUploadBase64ImageFileName.get(i);
        }
        return "";
    }
    public void setFileUploadBase64ImageFileName(List<String> fileUploadBase64ImageFileName) {
        this.fileUploadBase64ImageFileName = fileUploadBase64ImageFileName;
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

    public List<String> getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(List<String> base64Image) {
        this.base64Image = base64Image;
    }

    public String getBase64Image(int index) {
        if (base64Image.isEmpty()) {
            return null;
        }
        return base64Image.get(index);
    }

}

