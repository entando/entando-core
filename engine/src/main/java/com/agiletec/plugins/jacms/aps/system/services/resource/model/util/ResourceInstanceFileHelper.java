/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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
            //ApsSystemUtils.logThrowable(t, this, "save");
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
        String resDiskFolder = resource.getDiskFolder();
        File dir = new File(resDiskFolder);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }
        return resDiskFolder;
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
