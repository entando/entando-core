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

import java.io.File;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.resource.parse.ResourceDOM;

/**
 * Classe astratta di base per l'implementazione 
 * di oggetti Risorsa composti da una singola istanza.
 * @author E.Santoboni
 */
public abstract class AbstractMonoInstanceResource extends AbstractResource {

	private static final Logger _logger = LoggerFactory.getLogger(AbstractMonoInstanceResource.class);
	
	/**
     * Implementazione del metodo isMultiInstance() di AbstractResource.
     * Restituisce sempre false in quanto questa classe astratta è 
     * alla base di tutte le risorse SingleInstance.
     * @return false in quanto la risorsa è composta da una singola istanza. 
     */
	@Override
	public boolean isMultiInstance() {
    	return false;
    }
	
	@Override
	public InputStream getResourceStream(int size, String langCode) {
		return this.getResourceStream();
	}
	
	@Override
	public InputStream getResourceStream() {
		ResourceInstance instance = this.getInstance();
		String subPath = super.getDiskSubFolder() + instance.getFileName();
		try {
			return this.getStorageManager().getStream(subPath, this.isProtectedResource());
		} catch (Throwable t) {
			_logger.error("Error on extracting resource Stream", t);
			//ApsSystemUtils.logThrowable(t, this, "getResourceStream");
			throw new RuntimeException("Error on extracting resource Stream", t);
		}
	}
    
	@Override
	@Deprecated
	public File getFile() {
		ResourceInstance instance = this.getInstance();
		try {
			InputStream stream = this.getResourceStream();
			if (null != stream) {
				return this.saveTempFile("temp_" + instance.getFileName(), stream);
			}
			return null;
		} catch (Throwable t) {
			_logger.error("Error on extracting file", t);
			//ApsSystemUtils.logThrowable(t, this, "getFile");
			throw new RuntimeException("Error on extracting file", t);
		}
	}
    
	@Override
	public void deleteResourceInstances() throws ApsSystemException {
		try {
			if (null == this.getInstance()) {
				_logger.debug("Null instance for resource {}", this.getId());
				return;
			}
			String docName = this.getInstance().getFileName();
		    String subPath = this.getDiskSubFolder() + docName;
			this.getStorageManager().deleteFile(subPath, this.isProtectedResource());
		} catch (Throwable t) {
			_logger.error("Error on deleting resource instances", t);
			//ApsSystemUtils.logThrowable(t, this, "deleteResourceInstances");
			throw new ApsSystemException("Error on deleting resource instances", t);
		}
	}
    
	@Override
	public boolean exists(String masterFormFileName) throws ApsSystemException {
		String fileName = this.getInstanceFileName(masterFormFileName);
		String subPath = this.getDiskSubFolder() + fileName;
		return this.getStorageManager().exists(subPath, this.isProtectedResource());
	}
	
	/**
     * Setta l'istanza alla risorsa.
     * @param instance L'istanza da settare alla risorsa.
     */
	@Override
	public void addInstance(ResourceInstance instance) {
    	this._instance = instance;
    }
    
    /**
     * Restituisce l'istanza della risorsa.
     * @return L'istanza della risorsa.
     */
    public ResourceInstance getInstance() {
    	return _instance ;
    }
    
    @Override
	public String getXML() {
        ResourceDOM resourceDom = this.getResourceDOM();
        resourceDom.addInstance(this.getInstance().getJDOMElement());
        return resourceDom.getXMLDocument();
    }
    
    public String getInstanceFileName(String masterFileName) {
    	return masterFileName;
	}
    
	private ResourceInstance _instance;

}
