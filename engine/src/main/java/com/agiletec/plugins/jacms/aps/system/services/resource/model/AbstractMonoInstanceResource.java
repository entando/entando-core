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
package com.agiletec.plugins.jacms.aps.system.services.resource.model;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.resource.parse.ResourceDOM;

import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
			throw new RuntimeException("Error on extracting resource Stream", t);
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
			throw new ApsSystemException("Error on deleting resource instances", t);
		}
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
	public ResourceInstance getDefaultInstance() {
		return this.getInstance();
	}
    
    @Override
	public String getXML() {
        ResourceDOM resourceDom = this.getResourceDOM();
        resourceDom.addInstance(this.getInstance().getJDOMElement());
        return resourceDom.getXMLDocument();
    }
    
    String getNewInstanceFileName(String masterFileName) throws Throwable {
		String baseName = getUniqueBaseName(masterFileName);

		String extension = FilenameUtils.getExtension(masterFileName);
		if (StringUtils.isNotEmpty(extension)) {
			baseName += "." + extension;
		}

    	return baseName;
	}
    
	private ResourceInstance _instance;
	
}