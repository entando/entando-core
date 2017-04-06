package org.entando.entando.plugins.jacms.aps.system.services.content.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

public class ContentHelper implements IContentHelper, ApplicationContextAware {

	private static final Logger _logger = LoggerFactory.getLogger(ContentHelper.class);

	@Override
	public Map<String, List<?>> getReferencingObjects(Content content) throws ApsSystemException {
		List<ContentUtilizer> contentUtilizers = this.getContentUtilizers();
    	Map<String, List<?>> references = this.getReferencingObjects(content, contentUtilizers);
    	return references;
    }

	@Override
	public Map<String, List<?>> getReferencingObjects(Content content, List<ContentUtilizer> contentUtilizers) throws ApsSystemException {
    	Map<String, List<?>> references = new HashMap<String, List<?>>();
    	try {
    		for (ContentUtilizer contentUtilizer : contentUtilizers) {
    			if (contentUtilizer != null) {
    				List<?> utilizers = contentUtilizer.getContentUtilizers(content.getId());
    				if (utilizers != null && !utilizers.isEmpty()) {
    					references.put(contentUtilizer.getName()+"Utilizers", utilizers);
    				}
    			}
    		}
    	} catch (Throwable t) {
			_logger.error("Error extracting referencing object", t);
    		throw new ApsSystemException("Error searching content referencing objects", t);
    	}
    	return references;
	}

	@Override
	public List<ContentUtilizer> getContentUtilizers() {
		ApplicationContext applicationContext = this.getApplicationContext();
		String[] defNames = applicationContext.getBeanNamesForType(ContentUtilizer.class);
		List<ContentUtilizer> contentUtilizers = new ArrayList<ContentUtilizer>(defNames.length);
		for (String defName : defNames) {
			Object service = null;
			try {
				service = applicationContext.getBean(defName);
				if (service != null) {
					ContentUtilizer contentUtilizer = (ContentUtilizer) service;
					contentUtilizers.add(contentUtilizer);
				}
			} catch (Throwable t) {
				_logger.error("error loading ReferencingObject {}", defName, t);
			}
		}
		return contentUtilizers;
	}

	protected ApplicationContext getApplicationContext() throws BeansException {
		return _applicationContext;
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this._applicationContext = applicationContext;
	}

	private ApplicationContext _applicationContext;

}
