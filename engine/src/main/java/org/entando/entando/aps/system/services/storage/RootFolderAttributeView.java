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
package org.entando.entando.aps.system.services.storage;

/**
 * @author E.Santoboni
 */
public class RootFolderAttributeView extends BasicFileAttributeView {

    public RootFolderAttributeView(String name, boolean isProtected) {
        this.setName(name);
        this.setDirectory(true);
        this.setProtectedFolder(isProtected);
    }

    public boolean isProtectedFolder() {
        return _protectedFolder;
    }

    protected void setProtectedFolder(boolean protectedFolder) {
        this._protectedFolder = protectedFolder;
    }

    private boolean _protectedFolder;

}
