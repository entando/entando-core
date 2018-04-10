/*
 * Copyright 2015-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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

import java.util.List;
import org.entando.entando.aps.system.services.storage.model.BasicFileAttributeViewDto;
import org.entando.entando.web.filebrowser.model.FileBrowserFileRequest;
import org.entando.entando.web.filebrowser.model.FileBrowserRequest;
import org.springframework.validation.BindingResult;

/**
 * @author E.Santoboni
 */
public interface IFileBrowserService {

    public List<BasicFileAttributeViewDto> browseFolder(String currentPath, boolean protectedFolder);

    public byte[] getFileStream(String currentPath, boolean protectedFolder);

    public void addFile(FileBrowserFileRequest request, BindingResult bindingResult);

    public void updateFile(FileBrowserFileRequest request, BindingResult bindingResult);

    public void deleteFile(String currentPath, boolean protectedResource);

    public void addDirectory(FileBrowserRequest request, BindingResult bindingResult);

    public void deleteDirectory(String currentPath, boolean protectedFolder);

}
