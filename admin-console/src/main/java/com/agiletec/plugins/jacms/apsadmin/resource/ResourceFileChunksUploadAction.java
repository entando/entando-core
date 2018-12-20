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
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;

import java.io.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceFileChunksUploadAction //extends ResourceAction {
        extends ActionSupport {

    private static final Logger logger = LoggerFactory.getLogger(ResourceFileChunksUploadAction.class);
    String RESULT_SUCCESS = "SUCCESS";
    String RESULT_FAILED = "FAILED";

    private String fileUploadContentType;
    private String fileName;
    private File fileUpload;

    private Long start;
    private Long end;
    private InputStream inputStream;
    private String descr;  
    private String uploadId;
    private String fileSize;
     
    public String newResource() {
        return SUCCESS;
    }
    
    //@Override
    public String save() {
        logger.info("---------------------------------------------------");
        logger.info("save");
        logger.info("start {}", start);
        logger.info("end {}", end);
        logger.info("fileUpload {}", fileUpload);
        logger.info("contentType {}", fileUploadContentType);
        logger.info("filename {}", fileName);
        logger.info("descr {}", descr);
        logger.info("uploadId {}", uploadId);
        logger.info("fileSize {}", fileSize);
        try {
            processChunk(fileUpload, uploadId +"_" +fileName , start, end);
        } catch (IOException ex) {
            inputStream = new ByteArrayInputStream(RESULT_FAILED.getBytes());
            logger.error("ex {}", ex);
        }
        logger.info("---------------------------------------------------");
      
        inputStream = new ByteArrayInputStream(RESULT_SUCCESS.getBytes());
        
        return SUCCESS;
    }

    
    
    protected void processChunk(File fileChunk, String filename, Long start, Long End) throws IOException {
        if (start==0){
            this.createTempFile(fileChunk,filename);
        }
        else   {
            this.appendChunk(fileChunk, filename);
        }
        
    }
    
    protected void appendChunk(File fileChunk, String filename) throws IOException{
        logger.info("appendChunk");
        String tempDir = System.getProperty("java.io.tmpdir");
        logger.info("******** file {}", tempDir + File.separator + filename);
        File file = new File(tempDir + File.separator + filename);
    
        byte[] fileChunkBytes = FileUtils.readFileToByteArray(fileChunk);
        logger.info("appendChunk bytes {}",fileChunkBytes.length);
 
        FileUtils.writeByteArrayToFile(file, fileChunkBytes,true);
        logger.info("appendChunk done");

    }
    
    
    protected void createTempFile(File firstChunk, String filename) throws IOException {
        logger.info("createTempFile");
        String tempDir = System.getProperty("java.io.tmpdir");

        logger.info("******** file {}", tempDir + File.separator + filename);


        File file = new File(tempDir + File.separator + filename);
        logger.info("file.exists() {}", file.exists());
        logger.info("file.isDirectory() {}", file.isDirectory());
        if (!file.exists() && !file.isDirectory()) {
            // do something

            logger.info("createTempFile START");

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

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
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

}
