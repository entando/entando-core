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
package com.agiletec.plugins.jacms.aps.system.services.resource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.CategoryUtilizer;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.services.resource.event.ResourceChangedEvent;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AbstractMonoInstanceResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AbstractMultiInstanceResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInstance;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.resource.parse.ResourceHandler;

/**
 * Servizio gestore tipi di risorse (immagini, audio, video, etc..).
 * @author W.Ambu - E.Santoboni
 */
public class ResourceManager extends AbstractService 
		implements IResourceManager, GroupUtilizer, CategoryUtilizer {
	
	private static final Logger _logger = LoggerFactory.getLogger(ResourceManager.class);
	
	@Override
	public void init() throws Exception {
    	_logger.debug("{} ready. Initialized {} resource types", this.getClass().getName(), this._resourceTypes.size());
	}
	
	/**
     * Crea una nuova istanza di un tipo di risorsa del tipo richiesto. Il nuovo
     * tipo di risorsa è istanziato mediante clonazione del prototipo corrispondente.
     * @param typeCode Il codice del tipo di risorsa richiesto, come definito in configurazione.
     * @return Il tipo di risorsa istanziato (vuoto).
     */
	@Override
	public ResourceInterface createResourceType(String typeCode) {
    	ResourceInterface resource = (ResourceInterface) _resourceTypes.get(typeCode);
        return resource.getResourcePrototype();
    }
    
    /**
     * Restituisce la lista delle chiavi dei tipi risorsa presenti nel sistema.
     * @return La lista delle chiavi dei tipi risorsa esistenti.
     */
	@Override
	public List<String> getResourceTypeCodes() {
    	return new ArrayList<String>(this._resourceTypes.keySet());
    }
    
    /**
     * Salva una risorsa nel db con incluse nel filesystem, indipendentemente dal tipo.
     * @param bean L'oggetto detentore dei dati della risorsa da inserire.
     * @throws ApsSystemException in caso di errore.
     */
	@Override
	public ResourceInterface addResource(ResourceDataBean bean) throws ApsSystemException {
		ResourceInterface newResource = this.createResource(bean);
		try {
			this.generateAndSetResourceId(newResource, bean.getResourceId());
			newResource.saveResourceInstances(bean);
    		this.getResourceDAO().addResource(newResource);
    	} catch (Throwable t) {
			newResource.deleteResourceInstances();
			_logger.error("Error adding resource", t);
			//ApsSystemUtils.logThrowable(t, this, "addResource");
			throw new ApsSystemException("Error adding resource", t);
    	}
		return newResource;
    }
	
    /**
     * Salva una risorsa nel db, indipendentemente dal tipo.
     * @param resource La risorsa da salvare.
     * @throws ApsSystemException in caso di errore.
     */
	@Override
	public void addResource(ResourceInterface resource) throws ApsSystemException {
    	try {
			this.generateAndSetResourceId(resource, resource.getId());
    		this.getResourceDAO().addResource(resource);
    	} catch (Throwable t) {
    		_logger.error("Error adding resource", t);
			//ApsSystemUtils.logThrowable(t, this, "addResource");
			throw new ApsSystemException("Error adding resource", t);
    	}
    }
	
	protected void generateAndSetResourceId(ResourceInterface resource, String id) throws ApsSystemException {
		if (null == id || id.trim().length() == 0) {
			IKeyGeneratorManager keyGenerator = 
					(IKeyGeneratorManager) this.getBeanFactory().getBean(SystemConstants.KEY_GENERATOR_MANAGER);
			int newId = keyGenerator.getUniqueKeyCurrentValue();
			resource.setId(String.valueOf(newId));
		}
	}
    
    @Override
	public void updateResource(ResourceDataBean bean) throws ApsSystemException {
		ResourceInterface oldResource = this.loadResource(bean.getResourceId());
		try {
			if (null == bean.getInputStream()) {
				oldResource.setDescr(bean.getDescr());
				oldResource.setCategories(bean.getCategories());
				this.getResourceDAO().updateResource(oldResource);
				this.notifyResourceChanging(oldResource);
			} else {
				ResourceInterface updatedResource = this.createResource(bean);//this.saveResource(bean);
				updatedResource.saveResourceInstances(bean);
				this.getResourceDAO().updateResource(updatedResource);
				if (!updatedResource.getMasterFileName().equals(oldResource.getMasterFileName())) {
					oldResource.deleteResourceInstances();
				}
				this.notifyResourceChanging(updatedResource);
			}
		} catch (Throwable t) {
			_logger.error("Error updating resource", t);
			//ApsSystemUtils.logThrowable(t, this, "updateResource");
			throw new ApsSystemException("Error updating resource", t);
		}
	}
    
	/**
	 * Aggiorna una risorsa nel db.
	 * @param resource Il contenuto da aggiungere o modificare.
	 * @throws ApsSystemException in caso di errore nell'accesso al db.
	 */	
	@Override
	public void updateResource(ResourceInterface resource) throws ApsSystemException {
		try {
			this.getResourceDAO().updateResource(resource);
			this.notifyResourceChanging(resource);
		} catch (Throwable t) {
			_logger.error("Error updating resource", t);
			//ApsSystemUtils.logThrowable(t, this, "updateResource");
			throw new ApsSystemException("Error updating resource", t);
		}
	}
	
	protected ResourceInterface createResource(ResourceDataBean bean) throws ApsSystemException {
		ResourceInterface resource = this.createResourceType(bean.getResourceType());
    	resource.setDescr(bean.getDescr());
    	resource.setMainGroup(bean.getMainGroup());
    	resource.setCategories(bean.getCategories());
    	resource.setMasterFileName(bean.getFileName());
		resource.setId(bean.getResourceId());
		return resource;
	}
	
	protected void notifyResourceChanging(ResourceInterface resource) throws ApsSystemException {
		ResourceChangedEvent event = new ResourceChangedEvent();
		event.setResource(resource);
		this.notifyEvent(event);
	}
	
    /**
     * Carica una lista di identificativi di risorse 
	 * in base al tipo, ad una parola chiave e dalla categoria della risorsa. 
	 * @param type Tipo di risorsa da cercare.
	 * @param text Testo immesso per il raffronto con la descrizione della risorsa. null o 
	 * stringa vuota nel caso non si voglia ricercare le risorse per parola chiave. 
	 * @param categoryCode Il codice della categoria delle risorse. null o 
	 * stringa vuota nel caso non si voglia ricercare le risorse per categoria.
	 * @param groupCodes I codici dei gruppi consentiti tramite il quale 
	 * filtrare le risorse.
     * @return La lista di identificativi di risorse.
     * @throws ApsSystemException In caso di errore.
     */
	@Override
	public List<String> searchResourcesId(String type, String text, 
    		String categoryCode, Collection<String> groupCodes) throws ApsSystemException {
    	return this.searchResourcesId(type, text, null, categoryCode, groupCodes);
    }
    
	@Override
	public List<String> searchResourcesId(String type, String text, 
			String filename, String categoryCode, Collection<String> groupCodes) throws ApsSystemException {
		if (null == groupCodes || groupCodes.isEmpty()) {
			return new ArrayList<String>();
		}
		List<String> resourcesId = null;
    	try {
    		resourcesId = this.getResourceDAO().searchResourcesId(type, text, filename, categoryCode, groupCodes);
    	} catch (Throwable t) {
    		_logger.error("Error searching resources id", t);
			//ApsSystemUtils.logThrowable(t, this, "searchResourcesId");
			throw new ApsSystemException("Error searching resources id", t);
    	}
    	return resourcesId;
    }
    
	@Override
	public List<String> searchResourcesId(FieldSearchFilter[] filters, String categoryCode, Collection<String> groupCodes) throws ApsSystemException {
		this.checkFilterKeys(filters);
		List<String> resourcesId = null;
    	try {
    		resourcesId = this.getResourceDAO().searchResourcesId(filters, categoryCode, groupCodes);
    	} catch (Throwable t) {
    		_logger.error("Error searching resources id", t);
			//ApsSystemUtils.logThrowable(t, this, "searchResourcesId");
			throw new ApsSystemException("Error searching resources id", t);
    	}
    	return resourcesId;
	}
    
	protected void checkFilterKeys(FieldSearchFilter[] filters) {
		if (null != filters && filters.length > 0) {
			String[] allowedFilterKeys = {RESOURCE_ID_FILTER_KEY, RESOURCE_TYPE_FILTER_KEY, RESOURCE_DESCR_FILTER_KEY, 
				RESOURCE_MAIN_GROUP_FILTER_KEY, RESOURCE_FILENAME_FILTER_KEY, RESOURCE_CREATION_DATE_FILTER_KEY, RESOURCE_MODIFY_DATE_FILTER_KEY};
			List<String> allowedFilterKeysList = Arrays.asList(allowedFilterKeys);
			for (int i = 0; i < filters.length; i++) {
				FieldSearchFilter filter = filters[i];
				if (!allowedFilterKeysList.contains(filter.getKey())) {
					throw new RuntimeException("Invalid filter key - '" + filter.getKey() + "'");
				}
			}
		}
	}
	
    /**
     * Restituisce la risorsa con l'id specificato.
     * @param id L'identificativo della risorsa da caricare.
     * @return La risorsa cercata. null se non vi è nessuna risorsa con l'identificativo immesso.
     * @throws ApsSystemException in caso di errore.
     */
	@Override
	public ResourceInterface loadResource(String id) throws ApsSystemException {
    	ResourceInterface resource = null;
    	try {
    		ResourceRecordVO resourceVo = this.getResourceDAO().loadResourceVo(id);
    		if (null != resourceVo) {
    			resource = this.createResource(resourceVo);
    			resource.setMasterFileName(resourceVo.getMasterFileName());
    		}
    	} catch (Throwable t) {
    		_logger.error("Error loading resource : id {}", id, t);
    		//ApsSystemUtils.logThrowable(t, this, "loadResource");
    		throw new ApsSystemException("Error loading resource : id " + id, t);
    	}
    	return resource;
    }
    
    /**
     * Metodo di servizio. Restituisce una risorsa 
     * in base ai dati del corrispondente record.
     * @param resourceVo Il vo relativo al record.
     * @return La risorsa valorizzata.
     * @throws ApsSystemException in caso di errore.
     */
    protected ResourceInterface createResource(ResourceRecordVO resourceVo) throws ApsSystemException {
		String resourceType = resourceVo.getResourceType();
		String resourceXML = resourceVo.getXml();
		ResourceInterface resource = this.createResourceType(resourceType);
		this.fillEmptyResourceFromXml(resource, resourceXML);
		resource.setMainGroup(resourceVo.getMainGroup());
		resource.setCreationDate(resourceVo.getCreationDate());
		resource.setLastModified(resourceVo.getLastModified());
		return resource;
	}
    
    /**
     * Valorizza una risorsa prototipo con gli elementi 
     * dell'xml che rappresenta una risorsa specifica. 
     * @param resource Il prototipo di risorsa da specializzare con gli attributi dell'xml.
     * @param xml L'xml della risorsa specifica. 
     * @throws ApsSystemException
     */	
    protected void fillEmptyResourceFromXml(ResourceInterface resource, String xml) throws ApsSystemException {
    	try {
			SAXParserFactory parseFactory = SAXParserFactory.newInstance();			
    		SAXParser parser = parseFactory.newSAXParser();
    		InputSource is = new InputSource(new StringReader(xml));
    		ResourceHandler handler = new ResourceHandler(resource, this.getCategoryManager());
    		parser.parse(is, handler);
    	} catch (Throwable t) {
    		_logger.error("Error loading resource", t);
    		//ApsSystemUtils.logThrowable(t, this, "fillEmptyResourceFromXml");
    		throw new ApsSystemException("Error loading resource", t);
    	}
    }
    
    /**
     * Cancella una risorsa dal db ed i file di ogni istanza dal filesystem.
     * @param resource La risorsa da cancellare.
     * @throws ApsSystemException in caso di errore nell'accesso al db.
     */
    @Override
	public void deleteResource(ResourceInterface resource) throws ApsSystemException {
    	try {
    		this.getResourceDAO().deleteResource(resource.getId());
    		resource.deleteResourceInstances();
    	} catch (Throwable t) {
    		_logger.error("Error deleting resource", t);
    		//ApsSystemUtils.logThrowable(t, this, "deleteResource");
    		throw new ApsSystemException("Error deleting resource", t);
    	}
    }
    
    @Override
	public void refreshMasterFileNames() throws ApsSystemException {
    	this.startResourceReloaderThread(null, ResourceReloaderThread.RELOAD_MASTER_FILE_NAME);
	}
    
    @Override
    public void refreshResourcesInstances(String resourceTypeCode) throws ApsSystemException {
    	this.startResourceReloaderThread(resourceTypeCode, ResourceReloaderThread.REFRESH_INSTANCE);
	}
    
    protected void startResourceReloaderThread(String resourceTypeCode, int operationCode) throws ApsSystemException {
    	if (this.getStatus() != STATUS_READY) {
    		_logger.info("Service not ready : status {}", this.getStatus());
    		return;
		}
    	String threadName = this.getName() + "_resourceReloader_" + DateConverter.getFormattedDate(new Date(), "yyyyMMdd");
    	try {
    		List<String> resources = this.getResourceDAO().searchResourcesId(resourceTypeCode, null, null, null);
			ResourceReloaderThread thread = new ResourceReloaderThread(this, operationCode, resources);
    		thread.setName(threadName);
			thread.start();
			_logger.info("Reloader started");
		} catch (Throwable t) {
			_logger.error("Error refreshing Resource of type {} - Thread Name '{}'",resourceTypeCode, threadName, t);
			//ApsSystemUtils.logThrowable(t, this, "startResourceReloaderThread", "Error refreshing Resource of type " + resourceTypeCode + " - Thread Name '" + threadName + "'");
		}
	}
    
    protected void refreshMasterFileNames(String resourceId) {
    	try {
    		ResourceInterface resource = this.loadResource(resourceId);
    		if (resource.isMultiInstance()) {
    			ResourceInstance instance = 
    				((AbstractMultiInstanceResource) resource).getInstance(0, null);
    			String filename = instance.getFileName();
    			int index = filename.lastIndexOf("_d0.");
    			String masterFileName = filename.substring(0, index) + filename.substring(index+3);
    			resource.setMasterFileName(masterFileName);
    		} else {
    			ResourceInstance instance = 
    				((AbstractMonoInstanceResource) resource).getInstance();
    			resource.setMasterFileName(instance.getFileName());
    		}
    		this.updateResource(resource);
		} catch (Throwable t) {
			_logger.error("Error reloading master file name of resource {}", resourceId, t);
			//ApsSystemUtils.logThrowable(t, this, "refreshMasterFileNames", 	"Error reloading master file name of resource " + resourceId);
		}
    }
    
    protected void refreshResourceInstances(String resourceId) {
    	try {
    		ResourceInterface resource = this.loadResource(resourceId);
    		resource.reloadResourceInstances();
    		this.updateResource(resource);
		} catch (Throwable t) {
			_logger.error("Error refreshing resource instances of resource {}", resourceId, t);
			//ApsSystemUtils.logThrowable(t, this, "refreshResourceInstances", "Error refreshing resource instances of resource " + resourceId);
		}
    }
    
	@Override
	public List getGroupUtilizers(String groupName) throws ApsSystemException {
		List<String> resourcesId = null;
    	try {
	    	List<String> allowedGroups = new ArrayList<String>(1);
	    	allowedGroups.add(groupName);
	    	resourcesId = this.getResourceDAO().searchResourcesId(null, null, null, null, allowedGroups);
		} catch (Throwable t) {
			_logger.error("Error searching group utilizers : group '{}'", groupName, t);
			//ApsSystemUtils.logThrowable(t, this, "getGroupUtilizers");
			throw new ApsSystemException("Error searching group utilizers : group '" + groupName + "'", t);
		}
		return resourcesId;
	}
    
    @Override
	public List getCategoryUtilizers(String categoryCode) throws ApsSystemException {
    	List<String> resourcesId = null;
    	try {
	    	resourcesId = this.getResourceDAO().searchResourcesId(null, null, null, categoryCode, null);
    	} catch (Throwable t) {
    		_logger.error("Error searching category utilizers : category code '{}'", categoryCode, t);
			//ApsSystemUtils.logThrowable(t, this, "getCategoryUtilizers");
			throw new ApsSystemException("Error searching category utilizers : category code '" + categoryCode + "'", t);
		}
    	return resourcesId;
	}
    
    @Override
	public int getStatus() {
		return this._status;
	}
	protected void setStatus(int status) {
		this._status = status;
	}
    
	/**
     * Restutuisce il dao in uso al manager.
     * @return Il dao in uso al manager.
     */
    protected IResourceDAO getResourceDAO() {
		return _resourceDao;
	}
    
    /**
     * Setta il dao in uso al manager.
     * @param resourceDao Il dao in uso al manager.
     */
	public void setResourceDAO(IResourceDAO resourceDao) {
		this._resourceDao = resourceDao;
	}
	
	public void setResourceTypes(Map<String, ResourceInterface> resourceTypes) {
		this._resourceTypes = resourceTypes;
	}
    
	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}
	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}
	
	/**
     * Mappa dei prototipi dei tipi di risorsa
     */
    private Map<String, ResourceInterface> _resourceTypes;
    
    private int _status;
    
    private IResourceDAO _resourceDao;
    
    private ICategoryManager _categoryManager;
	
}