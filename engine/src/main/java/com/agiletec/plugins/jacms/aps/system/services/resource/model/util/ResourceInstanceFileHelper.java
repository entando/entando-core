/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package com.agiletec.plugins.jacms.aps.system.services.resource.model.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;

/**
 * Classe Helper per la gestione dei file relativi alle istanze delle risorse.
 * @author E.Santoboni
 * @deprecated Since Entando 3.2.1. Use IStorageManager
 */
public class ResourceInstanceFileHelper implements IResourceInstanceHelper {

	private static final Logger _logger = LoggerFactory.getLogger(ResourceInstanceFileHelper.class);
	
	@Override
	@Deprecated
    public void save(String filePath, ResourceDataBean bean) throws ApsSystemException {
        try {
            this.save(filePath, bean.getInputStream());
        } catch (Throwable t) {
        	_logger.error("Error on saving file. path: {}", filePath, t);
            //ApsSystemUtils.logThrowable(t, this, "save");
            throw new ApsSystemException("Error on saving file", t);
        }
    }
    
	@Override
    @Deprecated
    public void save(String filePath, InputStream is) throws ApsSystemException {
        try {
            byte[] buffer = new byte[1024];
            int length = -1;
            FileOutputStream outStream = new FileOutputStream(filePath);
            while ((length = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
                outStream.flush();
            }
            outStream.close();
            is.close();
        } catch (Throwable t) {
        	_logger.error("Error on saving file {}", filePath, t);
            throw new ApsSystemException("Error on saving file", t);
        }
    }
    
	@Override
    @Deprecated
    public String getFileExtension(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).trim();
        return extension;
    }
    
	@Override
    @Deprecated
    public String getResourceDiskFolder(ResourceInterface resource) {
        throw new RuntimeException("getResourceDiskFolder - Deprecated method");
    }
    
	@Override
    @Deprecated
    public boolean delete(String filePath) throws ApsSystemException {
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
    
	@Override
    @Deprecated
    public boolean exists(String filePath) throws ApsSystemException {
        File file = new File(filePath);
        return file.exists();
    }
    
    protected ConfigInterface getConfigManager() {
        return _configManager;
    }
    public void setConfigManager(ConfigInterface configService) {
        this._configManager = configService;
    }
    
    private ConfigInterface _configManager;
    
}
