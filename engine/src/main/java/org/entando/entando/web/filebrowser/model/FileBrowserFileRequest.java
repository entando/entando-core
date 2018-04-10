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
package org.entando.entando.web.filebrowser.model;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author E.Santoboni
 */
public class FileBrowserFileRequest extends FileBrowserRequest {

    @NotBlank(message = "fileBrowser.filename.notBlank")
    private String filename;
    @NotNull(message = "fileBrowser.base64.notBlank")
    private byte[] base64;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getBase64() {
        return base64;
    }

    public void setBase64(byte[] base64) {
        this.base64 = base64;
    }

}
