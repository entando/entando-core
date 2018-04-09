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
package org.entando.entando.web.filebrowser.validator;

import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.entando.entando.web.filebrowser.model.FileBrowserFileRequest;
import org.entando.entando.web.filebrowser.model.FileBrowserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author eu
 */
@Component
public class FileBrowserValidator extends AbstractPaginationValidator implements Validator {

    public static final String ERRCODE_RESOURCE_DOES_NOT_EXIST = "1";

    @Autowired
    private IStorageManager storageManager;

    @Override
    public boolean supports(Class<?> paramClass) {
        return (FileBrowserFileRequest.class.equals(paramClass) || FileBrowserRequest.class.equals(paramClass));
    }

    @Override
    public void validate(Object target, Errors errors) {
    }

    public IStorageManager getStorageManager() {
        return storageManager;
    }

    public void setStorageManager(IStorageManager storageManager) {
        this.storageManager = storageManager;
    }

}
