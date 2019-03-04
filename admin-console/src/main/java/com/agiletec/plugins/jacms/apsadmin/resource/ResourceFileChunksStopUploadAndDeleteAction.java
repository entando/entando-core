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

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceFileChunksStopUploadAndDeleteAction extends ActionSupport {
    Charset charset = StandardCharsets.UTF_8;

    private static final Logger logger = LoggerFactory.getLogger(ResourceFileChunksStopUploadAndDeleteAction.class);
    private String resultMessage = "";

    private String RESULT_SUCCESS = "SUCCESS";
    private String RESULT_FAILED = "FAILED";
    private String RESULT_FILE_NOT_FOUND = "FILE_NOT_FOUND";
    private String fileID;
    private InputStream inputStream;

    public String stopUploadAndDelete() {
        logger.info("stopUploadAndDelete  fileId {}", fileID);        
        String tempDir = System.getProperty("java.io.tmpdir");
        logger.debug("file {}", tempDir + File.separator + fileID + ".tmp");
        File file = new File(tempDir + File.separator + fileID + ".tmp");
        if (file.exists() && !file.isDirectory()) {
            boolean fileDeleted = file.delete();
            if (fileDeleted) {
                resultMessage = RESULT_SUCCESS;
            } else {
                resultMessage = RESULT_FAILED;
            }
        } else {
            resultMessage= RESULT_FILE_NOT_FOUND;
        }
        inputStream = new ByteArrayInputStream(resultMessage.getBytes());
        logger.debug("resultMessage {}", resultMessage);
        return SUCCESS;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getResultMessage() {
        return resultMessage;
    }

}
