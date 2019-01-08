/*
* Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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

import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import static com.opensymphony.xwork2.Action.SUCCESS;

import java.io.*;
import java.util.Arrays;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceFileChunksUploadAction extends AbstractResourceAction {
    private static final Logger logger = LoggerFactory.getLogger(ResourceFileChunksUploadAction.class);
    
    private String RESULT_SUCCESS = "SUCCESS";
    private String RESULT_FAILED = "FAILED";
    private String RESULT_VALIDATION_ERROR = "VALIDATION_ERROR";

    private String resultMessage = "";
    private String resourceTypeCode;
    private String fileUploadContentType;
    private String fileName;
    private File fileUpload;
    private Long start;
    private Long end;
    private InputStream inputStream;
    private String uploadId;
    private String fileSize;
    private boolean valid = true;    
   
    public String newResource() {
        return SUCCESS;
    }

    @Override
    public void validate() {
        logger.info("ResourceFileChunksUploadAction validate");
        logger.info("resourceTypeCode {}", resourceTypeCode);
        if (null==resourceTypeCode || null==fileName)
        {
            valid = false;
        }
        else if (resourceTypeCode.equals("Image") || resourceTypeCode.equals("Attach")) {

            setResourceTypeCode(resourceTypeCode);

            if (null != this.getResourceTypeCode()) {
                ResourceInterface resourcePrototype = this.getResourceManager().createResourceType(this.getResourceTypeCode());
                if (null != resourcePrototype) {
                    valid = this.checkRightFileType(resourcePrototype, fileName);
                } else {
                    valid = false;
                }
            } else {
                valid = false;
            }
        } else {
            valid = false;
        }
        logger.info("valid {}", valid);
    }

    protected boolean checkRightFileType(ResourceInterface resourcePrototype, String fileName) {
        logger.debug("Check Right File Type {} for filename {}", resourcePrototype.getType(), fileName);
        logger.debug("Allowed File Types length {}", resourcePrototype.getAllowedFileTypes().length);
        logger.debug("Allowed File Types {}", Arrays.toString(resourcePrototype.getAllowedFileTypes()));

        if (fileName.length() > 0) {
            String docType = fileName.substring(fileName.lastIndexOf('.') + 1).trim();
            String[] types = resourcePrototype.getAllowedFileTypes();
            return this.isValidType(docType, types);
        } else {
            return false;
        }
    }

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

    public String upload() {
        if (valid) {
            logger.info("ResourceFileChunksUploadAction Save {}",fileName);
            logger.debug("start {}", start);
            logger.debug("end {}", end);
            logger.debug("fileUpload {}", fileUpload);
            logger.debug("contentType {}", fileUploadContentType);
            logger.debug("filename {}", fileName);
            logger.debug("uploadId {}", uploadId);
            logger.debug("fileSize {}", fileSize);
            logger.debug("resourceTypeCode {}", resourceTypeCode);
            try {
                processChunk(fileUpload, uploadId + ".tmp", start, end);
            } catch (IOException ex) {
                resultMessage = RESULT_FAILED;
                inputStream = new ByteArrayInputStream(RESULT_FAILED.getBytes());
                logger.error("Error processing the file chunk {}", ex);
            }
            resultMessage = RESULT_SUCCESS;
        } else {
            resultMessage = RESULT_VALIDATION_ERROR;
        }
        inputStream = new ByteArrayInputStream(resultMessage.getBytes());
        logger.debug("result {}", resultMessage);

        return SUCCESS;
    }

    protected void processChunk(File fileChunk, String filename, Long start, Long End) throws IOException {
        if (start == 0) {
            this.createTempFile(fileChunk, filename);
        } else {
            this.appendChunk(fileChunk, filename);
        }

    }

    protected void appendChunk(File fileChunk, String filename) throws IOException {        
        String tempDir = System.getProperty("java.io.tmpdir");
        logger.info("appendChunk to file {}", tempDir + File.separator + filename);
        File file = new File(tempDir + File.separator + filename);

        byte[] fileChunkBytes = FileUtils.readFileToByteArray(fileChunk);
        logger.debug("appendChunk bytes {}", fileChunkBytes.length);

        FileUtils.writeByteArrayToFile(file, fileChunkBytes, true);
        logger.debug("appendChunk done");

    }

    protected void createTempFile(File firstChunk, String filename) throws IOException {
        logger.info("createTempFile");
        String tempDir = System.getProperty("java.io.tmpdir");

        logger.debug("file {}", tempDir + File.separator + filename);

        File file = new File(tempDir + File.separator + filename);
        logger.debug("file.exists() {}", file.exists());
        logger.debug("file.isDirectory() {}", file.isDirectory());
        if (!file.exists() && !file.isDirectory()) {
            // do something

            logger.debug("createTempFile START");

            InputStream inputStream = new FileInputStream(firstChunk);
            OutputStream out = null;
            try {
                out = new FileOutputStream(file);
                byte buf[] = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } catch (IOException ex) {
                logger.error("Error creating file from byte array", ex);
                throw ex;
            } finally {
                if (null != out) {
                    out.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
            }
        }
    }

    public String getFileUploadContentType() {
        return fileUploadContentType;
    }

    public void setFileUploadContentType(String fileUploadContentType) {
        this.fileUploadContentType = fileUploadContentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(File fileUpload) {
        this.fileUpload = fileUpload;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getResourceTypeCode() {
        return resourceTypeCode;
    }

    public void setResourceTypeCode(String resourceTypeCode) {
        this.resourceTypeCode = resourceTypeCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public boolean isValid() {
        return valid;
    }
  
}
