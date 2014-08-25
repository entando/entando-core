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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.services.resource.parse.ResourceDOM;

/**
 * Classe astratta di base per gli oggetti Resource.
 * @author W.Ambu - E.Santoboni
 */
public abstract class AbstractResource implements ResourceInterface, Serializable {

	private static final Logger _logger = LoggerFactory.getLogger(AbstractResource.class);
	
	/**
	 * Inizializza gli elementi base costituenti la Risorsa.
	 */
    public AbstractResource() {
		this.setId("");
		this.setType("");
		this.setDescr("");
		this.setMainGroup("");
		this.setMasterFileName("");
		this._categories = new ArrayList<Category>();
		this.setFolder("");
		this.setCreationDate(null);
		this.setLastModified(null);
	}
	
	/**
	 * Restituisce l'identificativo della risorsa.
	 * @return L'identificativo della risorsa.
	 */
    @Override
	public String getId() {
		return _id;
	}
	
	/**
	 * Setta l'identificativo della risorsa.
	 * @param id L'identificativo della risorsa.
	 */
    @Override
	public void setId(String id) {
		this._id = id;
	}
	
	/**
	 * Restituisce il codice del tipo di risorsa.
	 * @return Il codice del tipo di risorsa.
	 */
    @Override
	public String getType() {
		return _typeCode;
	}
    
	/**
	 * Setta il codice del tipo di risorsa.
	 * @param typeCode Il codice del tipo di risorsa.
	 */
    @Override
	public void setType(String typeCode) {
		this._typeCode = typeCode;
	}
	
	/**
	 * Restituisce la descrizione della risorsa.
	 * @return La descrizione della risorsa.
	 */
    @Override
	public String getDescr() {
		return _descr;
	}
	
	/**
	 * Setta la descrizione della risorsa.
	 * @param descr La descrizione della risorsa.
	 */
    @Override
	public void setDescr(String descr) {
		this._descr = descr;
	}
	
	/**
     * Restituisce la stringa identificante 
     * il gruppo principale di cui la risorsa è membro.
     * @return Il gruppo principale di cui la risorsa è membro.
     */
    @Override
	public String getMainGroup() {
		return _mainGroup;
	}
	
    /**
	 * Setta la stringa identificante 
     * il gruppo principale di cui il contenuto è membro.
	 * @param mainGroup Il gruppo principale di cui il contenuto è membro.
	 */
    @Override
	public void setMainGroup(String mainGroup) {
		this._mainGroup = mainGroup;
	}
    
    @Override
	public String getMasterFileName() {
		return _masterFileName;
	}
    @Override
	public void setMasterFileName(String masterFileName) {
		this._masterFileName = masterFileName;
	}
	
	@Override
	public Date getCreationDate() {
		return _creationDate;
	}
	@Override
	public void setCreationDate(Date creationDate) {
		this._creationDate = creationDate;
	}
	
	@Override
	public Date getLastModified() {
		return _lastModified;
	}
	@Override
	public void setLastModified(Date lastModified) {
		this._lastModified = lastModified;
	}
	
	/**
	 * Aggiunge una categoria alla lista delle categorie della risorsa.
	 * @param category La categoria da aggiungere.
	 */
    @Override
	public void addCategory(Category category) {
		this._categories.add(category);
	}
	
	/**
	 * Restituisce la lista di categorie associate alla risorsa.
	 * @return La lista di categorie associate alla risorsa.
	 */
    @Override
	public List<Category> getCategories() {
		return _categories;
	}
	
	/**
	 * Setta la lista di categorie associate alla risorsa.
	 * @param categories La lista di categorie associate alla risorsa.
	 */
    @Override
	public void setCategories(List<Category> categories) {
		this._categories = categories;
	}
	
	/**
	 * Rimuove una categoria alla lista delle categorie della risorsa.
	 * @param category La categoria da rimuovere.
	 */
	public void removeCategory(Category category) {
		this._categories.remove(category);
	}
	
	/**
	 * Restituisce il nome della cartella contenitore delle risorse.
	 * @return Il nome della cartella contenitore delle risorse.
	 */
	@Override
	public String getFolder() {
		return _folder;
	}
	
	/**
	 * Setta il nome della cartella contenitore delle risorse.
	 * @param folder Il nome della cartella contenitore delle risorse.
	 */
	@Override
	public void setFolder(String folder) {
		if (!folder.endsWith("/")) {
			folder += "/";
		}
		this._folder = folder;
	}
	
	/**
	 * Restituisce l'url base della cartella delle risorse pretette.
	 * @return L'url base della cartella delle risorse protette.
	 */
    protected String getProtectedBaseURL() {
		return _protectedBaseURL;
	}
    
	public void setProtectedBaseURL(String protBaseURL) {
		this._protectedBaseURL = protBaseURL;
	}
	
    @Override
	public String[] getAllowedFileTypes() {
		return this.getAllowedExtensions().split(",");
	}
	
	/**
	 * Setta la stringa rappresentante l'insieme delle estensioni consentite separate da virgola.
	 * @return L'insieme delle estensioni consentite.
	 */
	protected String getAllowedExtensions() {
		return _allowedExtensions;
	}
	
	/**
	 * Setta la stringa rappresentante l'insieme delle estensioni consentite separate da virgola.
	 * @param allowedExtensions L'insieme delle estensioni consentite.
	 */
	public void setAllowedExtensions(String allowedExtensions) {
		this._allowedExtensions = allowedExtensions;
	}
	
	@Override
	public ResourceInterface getResourcePrototype() {
		AbstractResource prototype = null;
		try {
			Class resourceClass = Class.forName(this.getClass().getName());
			prototype = (AbstractResource) resourceClass.newInstance();
		} catch (Throwable t) {
			throw new RuntimeException("Errore in creazione prototipo " +
					"Risorsa tipo '" + this.getType() + "'", t);
		}
		prototype.setId("");
		prototype.setType(this.getType());
		prototype.setDescr("");
		prototype.setMainGroup("");
		prototype.setMasterFileName("");
		prototype.setCategories(new ArrayList<Category>());
		prototype.setFolder(this.getFolder());
		prototype.setProtectedBaseURL(this.getProtectedBaseURL());
		prototype.setAllowedExtensions(this.getAllowedExtensions());
		prototype.setStorageManager(this.getStorageManager());
		prototype.setCreationDate(null);
		prototype.setLastModified(null);
		return prototype;
	}
	
	/**
	 * Restituisce la classe dom (necessaria per la generazione dell'xml della risorsa) 
	 * preparata con gli attributi base della risorsa.
	 * @return La classe dom preparata con gli attributi base della risorsa.
	 */
	protected ResourceDOM getResourceDOM() {
		ResourceDOM resourceDom = this.getNewResourceDOM();
    	resourceDom.setTypeCode(this.getType());
    	resourceDom.setId(this.getId());
    	resourceDom.setDescr(this.getDescr());
    	resourceDom.setMainGroup(this.getMainGroup());
    	resourceDom.setMasterFileName(this.getMasterFileName());
    	if (null != this.getCategories()) {
    		for (int i=0; i<this.getCategories().size(); i++) {
        		Category cat = (Category) this.getCategories().get(i);
        		resourceDom.addCategory(cat.getCode());
        	}
    	}
    	return resourceDom;
	}
	
	protected ResourceDOM getNewResourceDOM() {
		return new ResourceDOM();
	}
	
	/**
     * Restituisce il path assoluto su disco del folder contenitore 
     * dei file delle istanze relative alla risorsa specificata. 
     * Questo path è necessario al salvataggio o alla rimozione
     * dei file associati ad ogni istanza della risorse.
     * @return Il path assoluto su disco completo.
	 * @deprecated Since Entando 3.2.1. Use StrorageManager
     */
	@Override
	public String getDiskFolder() {
		/*
    	StringBuilder diskFolder = new StringBuilder();
    	if (!Group.FREE_GROUP_NAME.equals(this.getMainGroup())) {
    		//RISORSA PROTETTA
    		diskFolder.append(this.getProtectedBaseDiskRoot());
    	} else {
    		//RISORSA LIBERA
    		diskFolder.append(this.getBaseDiskRoot());
    	}
    	String folder = this.getFolder();
    	if ((!diskFolder.toString().endsWith("\\") || !diskFolder.toString().endsWith("/")) 
    			&& (!folder.startsWith("\\") || !folder.startsWith("/"))) {
    		diskFolder.append(File.separator);
    	}
    	diskFolder.append(folder);
    	if (!Group.FREE_GROUP_NAME.equals(this.getMainGroup())) {
    		//RISORSA PROTETTA
    		diskFolder.append(this.getMainGroup() + File.separator);
    	}
    	return diskFolder.toString();
		*/
		throw new RuntimeException("Deprecated method");
    }
    
    protected String getDiskSubFolder() {
    	StringBuilder diskFolder = new StringBuilder(this.getFolder());
    	if (this.isProtectedResource()) {
    		//PROTECTED Resource
    		diskFolder.append(this.getMainGroup()).append(File.separator);
    	}
    	return diskFolder.toString();
    }
    
	@Override
	public InputStream getResourceStream(ResourceInstance instance) {
		return this.getResourceStream(instance.getSize(), instance.getLangCode());
	}
	
    /**
     * Restitituisce il nome file corretto da utilizzare 
     * per i salvataggi di istanze risorse all'interno del fileSystem.
     * @param masterFileName Il nome del file principale.
     * @return Il nome file corretto.
     * @deprecated from jAPS 2.1
     */
    protected String getRevisedInstanceFileName(String masterFileName) {
		String instanceFileName = masterFileName.replaceAll("[^ _.a-zA-Z0-9]", "");
		instanceFileName = instanceFileName.trim().replace(' ', '_');
		return instanceFileName;
	}
    
    /**
	 * Repurn the url path of the given istance.
	 * @param instance the resource instance
	 * @return Il path del file relativo all'istanza.
	 */
	protected String getUrlPath(ResourceInstance instance) {
		if (null == instance) return null;
		StringBuilder urlPath = new StringBuilder();
		if (this.isProtectedResource()) {
			//PATH di richiamo della servlet di autorizzazione
			//Sintassi /<RES_ID>/<SIZE>/<LANG_CODE>/
			String DEF = "def";
			urlPath.append(this.getProtectedBaseURL());
			if (!urlPath.toString().endsWith("/")) urlPath.append("/");
			urlPath.append(this.getId()).append("/");
			if (instance.getSize() < 0) {
				urlPath.append(DEF);
			} else {
				urlPath.append(instance.getSize());
			}
			urlPath.append("/");
			if (instance.getLangCode() == null) {
				urlPath.append(DEF);
			} else {
				urlPath.append(instance.getLangCode());
			}
			urlPath.append("/");
    	} else {
			StringBuilder subFolder = new StringBuilder(this.getFolder());
    		if (!subFolder.toString().endsWith("/")) {
				subFolder.append("/");
			}
    		subFolder.append(instance.getFileName());
			String path = this.getStorageManager().getResourceUrl(subFolder.toString(), false);
			urlPath.append(path);
    	}
		return urlPath.toString();
	}
	
	protected boolean isProtectedResource() {
		return (!Group.FREE_GROUP_NAME.equals(this.getMainGroup()));
	}
	
	protected File saveTempFile(String filename, InputStream is) throws ApsSystemException {
		String tempDir = System.getProperty("java.io.tmpdir");
		String filePath = tempDir + File.separator + filename;
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
			_logger.error("Error on saving temporary file '{}'", filename, t);
			//ApsSystemUtils.logThrowable(t, this, "saveTempFile");
			throw new ApsSystemException("Error on saving temporary file", t);
		}
		return new File(filePath);
	}
    
	protected IStorageManager getStorageManager() {
		return _storageManager;
	}
	public void setStorageManager(IStorageManager storageManager) {
		this._storageManager = storageManager;
	}
	
	private String _id;
	private String _typeCode;
	private String _descr;
	private String _mainGroup;
	private String _masterFileName;
	private List<Category> _categories;
	private String _folder;
	private String _protectedBaseURL;
	
	private String _allowedExtensions;
	
	private Date _creationDate;
	private Date _lastModified;
	
	private IStorageManager _storageManager;
	
}
