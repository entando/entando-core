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

import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.opensymphony.xwork2.Action;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestResourceFileChunkUploadAction extends ApsAdminBaseTestCase {

    private String FILE_UPLOAD_CONTENT_TYPE = "content-type";
    private String FILE_NAME_ATTACHMENT = "filename.txt";
    private String FILE_NAME_IMAGE = "filename.png";
    private Long START = 0L;
    private Long END = 2000L;
    private String UPLOAD_ID = "aaaa-bbbb-cccc-dddd";
    private String FILE_SIZE = "2000";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //   this.init();
    }

    public void testUploadNotValidTypeCodeValidation() throws Throwable {
        this.setUserOnSession("admin");
        this.initAction("/do/jacms/Resource", "upload");
        this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
        this.addParameter("resourceTypeCode", "test");
        this.addParameter("mainGroup", "test");
        this.addParameter("descr_0", "test");

        String result = this.executeAction();
        ResourceFileChunksUploadAction action = (ResourceFileChunksUploadAction) this.getAction();
        assertEquals(Action.SUCCESS, result);
        assertEquals("VALIDATION_ERROR", action.getResultMessage());
    }

    public void testUploadImageValidation() throws Throwable {
        this.setUserOnSession("admin");
        this.initAction("/do/jacms/Resource", "upload");
        this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
        this.addParameter("resourceTypeCode", "Image");
        this.addParameter("mainGroup", "test");
        this.addParameter("descr_0", "test");

        String result = this.executeAction();
        ResourceFileChunksUploadAction action = (ResourceFileChunksUploadAction) this.getAction();
        assertEquals(Action.SUCCESS, result);
        assertEquals("VALIDATION_ERROR", action.getResultMessage());
    }

    public void testUploadImageNotValidTypeValidation() throws Throwable {
        this.setUserOnSession("admin");
        this.initAction("/do/jacms/Resource", "upload");
        this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
        this.addParameter("resourceTypeCode", "Image");
        this.addParameter("fileName", FILE_NAME_ATTACHMENT);

        String result = this.executeAction();
        ResourceFileChunksUploadAction action = (ResourceFileChunksUploadAction) this.getAction();
        assertEquals(Action.SUCCESS, result);
        assertEquals("VALIDATION_ERROR", action.getResultMessage());
    }

    public void testUploadAttachmentValidation() throws Throwable {
        this.setUserOnSession("admin");
        this.initAction("/do/jacms/Resource", "upload");
        this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
        this.addParameter("resourceTypeCode", "Attach");
        String result = this.executeAction();
        ResourceFileChunksUploadAction action = (ResourceFileChunksUploadAction) this.getAction();
        assertEquals(Action.SUCCESS, result);
        assertEquals("VALIDATION_ERROR", action.getResultMessage());
    }

    public void testUploadAttachmentNotValidTypeValidation() throws Throwable {
        this.setUserOnSession("admin");
        this.initAction("/do/jacms/Resource", "upload");
        this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
        this.addParameter("resourceTypeCode", "Attach");
        this.addParameter("fileName", FILE_NAME_IMAGE);

        String result = this.executeAction();
        ResourceFileChunksUploadAction action = (ResourceFileChunksUploadAction) this.getAction();
        assertEquals(Action.SUCCESS, result);

        assertEquals("VALIDATION_ERROR", action.getResultMessage());
    }

    public void testUploadNotValidDescriptionValidation() throws Throwable {
        this.setUserOnSession("admin");
        this.initAction("/do/jacms/Resource", "upload");
        this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
        this.addParameter("resourceTypeCode", "test");
        String result = this.executeAction();
        ResourceFileChunksUploadAction action = (ResourceFileChunksUploadAction) this.getAction();
        assertEquals(Action.SUCCESS, result);

        assertEquals("VALIDATION_ERROR", action.getResultMessage());
    }
    
}
