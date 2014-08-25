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
package com.agiletec.plugins.jacms.aps.system.services.resource.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Classe rappresentante una risorsa Attach.
 * @author W.Ambu - E.Santoboni
 */
public class AttachResource extends AbstractMonoInstanceResource  {

	private static final Logger _logger = LoggerFactory.getLogger(AttachResource.class);
	
    /**
     * Restituisce il path della risorsa attach.
     * La stringa restituita è comprensiva del folder della risorsa e 
     * del nome del file dell'istanza richiesta.
     * @return Il path della risorsa attach.
     */
    public String getAttachPath() {
    	ResourceInstance instance = this.getInstance();
    	String path = this.getUrlPath(instance);
    	return path;
    }
    
    /**
     * Restituisce il path della risorsa attach.
     * La stringa restituita è comprensiva del folder della risorsa e 
     * del nome del file dell'istanza richiesta.
     * @return Il path della risorsa attach.
     * @deprecated use getAttachPath
     */
    public String getDocumentPath() {
    	return this.getAttachPath();
    }
    
    @Override
	public void saveResourceInstances(ResourceDataBean bean) throws ApsSystemException {
		try {
			String fileName = this.getInstanceFileName(bean.getFileName());
			String subPath = this.getDiskSubFolder() + fileName;
			this.getStorageManager().saveFile(subPath, this.isProtectedResource(), bean.getInputStream());
			ResourceInstance instance = new ResourceInstance();
			instance.setSize(0);
			instance.setFileName(fileName);
			String mimeType = bean.getMimeType();
			instance.setMimeType(mimeType);
			instance.setFileLength(bean.getFileSize() + " Kb");
			this.addInstance(instance);
		} catch (Throwable t) {
			_logger.error("Error on saving attach resource instances", t);
			//ApsSystemUtils.logThrowable(t, this, "saveResourceInstances");
			throw new ApsSystemException("Error on saving attach resource instances", t);
		}
	}
    
    @Override
	public void reloadResourceInstances() throws ApsSystemException {
		//Not supported
	}
    
}