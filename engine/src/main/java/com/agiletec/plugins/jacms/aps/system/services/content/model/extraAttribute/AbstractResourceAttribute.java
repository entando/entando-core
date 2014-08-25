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
package com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.attribute.DefaultJAXBAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.TextAttribute;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.model.CmsAttributeReference;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.util.ICmsAttributeErrorCodes;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;

/**
 * Classe astratta di appoggio agli attributi di tipo Risorsa.
 * @author E.Santoboni
 */
public abstract class AbstractResourceAttribute extends TextAttribute
        implements IReferenceableAttribute, ResourceAttributeInterface {

	private static final Logger _logger = LoggerFactory.getLogger(AbstractResourceAttribute.class);
	
    /**
     * Setta una risorsa sull'attributo.
     * @param resource La risorsa da associare all'attributo.
     * @param langCode il codice della lingua.
     */
	@Override
    public void setResource(ResourceInterface resource, String langCode) {
        if (null == langCode) {
            langCode = this.getDefaultLangCode();
        }
        if (null == resource) {
            this.getResources().remove(langCode);
        } else {
            this.getResources().put(langCode, resource);
        }
    }

    /**
     * Restituisce la risorsa associata all'attributo.
     * @param langCode il codice della lingua.
     * @return la risorsa associata all'attributo.
     */
	@Override
    public ResourceInterface getResource(String langCode) {
        return (ResourceInterface) this.getResources().get(langCode);
    }

    /**
     * Restituisce la risorsa associata all'attributo.
     * @return la risorsa associata all'attributo.
     */
	@Override
    public ResourceInterface getResource() {
        ResourceInterface res = this.getResource(this.getRenderingLang());
        if (null == res) {
            res = this.getResource(this.getDefaultLangCode());
        }
        return res;
    }
	
	/**
     * Sovrascrittura del metodo della classe astratta da cui deriva. Poichè
     * questo tipo di attributo non può mai essere "searchable", restituisce sempre false.
     * @return Restituisce sempre false
     * @see com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface#isSearchable()
     */
	@Override
	public boolean isSearchable() {
        return false;
    }
	
	@Override
    public boolean isSearchableOptionSupported() {
        return false;
    }
    
	@Override
    public Element getJDOMElement() {
		Element attributeElement = this.createRootElement("attribute");
        Iterator<String> langIter = this.getResources().keySet().iterator();
        while (langIter.hasNext()) {
            String currentLangCode = (String) langIter.next();
            ResourceInterface res = this.getResource(currentLangCode);
            if (null != res) {
                Element resourceElement = new Element("resource");
                resourceElement.setAttribute("resourcetype", res.getType());
                String resourceId = String.valueOf(res.getId());
                resourceElement.setAttribute("id", resourceId);
                resourceElement.setAttribute("lang", currentLangCode);
                attributeElement.addContent(resourceElement);
            }
        }
        super.addTextElements(attributeElement);
        return attributeElement;
    }
	
    /**
     * Appende, nella stringa rappresentante l'url della risorsa interna ad un entità, 
     * il riferimento al entità padre con la sintassi 
     * <baseUrl>/<REFERENCED_RESOURCE_INDICATOR>/<PARENT_CONTENT_ID>/. 
     * Tale operazione viene effettuata nel caso che la risorsa non sia libera.
     * @param basePath Il path base della risorsa.
     * @return Il path corretto.
     */
    protected String appendContentReference(String basePath) {
        ResourceInterface res = this.getResource();
        if (null == res) {
            return "";
        }
        String resourceGroup = res.getMainGroup();
        if (!Group.FREE_GROUP_NAME.equals(resourceGroup)
                && !this.getParentEntity().getGroups().isEmpty()) {
            if (!basePath.endsWith("/")) {
                basePath += "/";
            }
            basePath += REFERENCED_RESOURCE_INDICATOR
                    + "/" + this.getParentEntity().getId() + "/";
        }
        return basePath;
    }
    
	@Override
    public List<CmsAttributeReference> getReferences(List<Lang> systemLangs) {
        List<CmsAttributeReference> refs = new ArrayList<CmsAttributeReference>();
        for (int i = 0; i < systemLangs.size(); i++) {
            Lang lang = systemLangs.get(i);
            ResourceInterface res = this.getResource(lang.getCode());
            if (null != res) {
                CmsAttributeReference ref = new CmsAttributeReference(null, null, res.getId());
                refs.add(ref);
            }
        }
        return refs;
    }
    
	@Override
    public Object getValue() {
        if (null == this.getResources() || this.getResources().isEmpty()) {
            return null;
        }
        return this;
    }
    
	@Override
    protected JAXBResourceValue getJAXBValue(String langCode) {
		JAXBResourceValue value = new JAXBResourceValue();
		try {
			Object text = super.getJAXBValue(langCode);
			value.setText(text);
			if (null == langCode) {
				langCode = this.getDefaultLangCode();
			}
			this.setRenderingLang(langCode);
			String path = this.getDefaultPath();
			value.setPath(path);
			ResourceInterface resource = this.getResource();
			if (null != resource) {
				value.setResourceId(resource.getId());
				StringBuilder restResourcePath = new StringBuilder();
				restResourcePath.append(this.getConfigManager().getParam("applicationBaseURL"));
				restResourcePath.append("api/rs/").append(langCode).append("/jacms/");
				if (this.getType().equals(JacmsSystemConstants.RESOURE_ATTACH_CODE)) {
					restResourcePath.append("attachment");
				} else {
					restResourcePath.append("image");
				}
				restResourcePath.append("?id=").append(resource.getId());
				value.setRestResourcePath(restResourcePath.toString());
			}
		} catch (Throwable t) {
			_logger.error("Error creating jaxb response. lang: {}", langCode, t);
			//ApsSystemUtils.logThrowable(t, this, "getJAXBValue");
			throw new RuntimeException("Error creating jaxb response", t);
		}
        return value;
    }
    
	@Override
    public void valueFrom(DefaultJAXBAttribute jaxbAttribute) {
        JAXBResourceValue value = (JAXBResourceValue) jaxbAttribute.getValue();
        if (null == value) {
			return;
		}
        Object resourceId = value.getResourceId();
        if (null == resourceId) {
			return;
		}
        try {
            IResourceManager resourceManager = this.getResourceManager();
            ResourceInterface resource = resourceManager.loadResource(resourceId.toString());
            if (null != resource) {
                this.setResource(resource, this.getDefaultLangCode());
            }
            Object text = value.getText();
            if (null == text) {
				return;
			}
            this.getTextMap().put(this.getDefaultLangCode(), text.toString());
        } catch (Exception e) {
        	_logger.error("Error extracting resource from jaxbAttribute", e);
            //ApsSystemUtils.logThrowable(e, this, "valueFrom", "Error extracting resource from jaxbAttribute");
        }
    }
    
	@Override
    public Status getStatus() {
        Status textStatus = super.getStatus();
        Status resourceStatus = (null != this.getResource()) ? Status.VALUED : Status.EMPTY;
        if (!textStatus.equals(resourceStatus)) {
			return Status.INCOMPLETE;
		}
        if (textStatus.equals(resourceStatus) && textStatus.equals(Status.VALUED)) {
			return Status.VALUED;
		}
        return Status.EMPTY;
    }
    
    protected abstract String getDefaultPath();
    
    public Map<String, ResourceInterface> getResources() {
        return this._resources;
    }
    
    protected ConfigInterface getConfigManager() {
        return (ConfigInterface) this.getBeanFactory().getBean(SystemConstants.BASE_CONFIG_MANAGER);
    }
    
    protected IResourceManager getResourceManager() {
        return (IResourceManager) this.getBeanFactory().getBean(JacmsSystemConstants.RESOURCE_MANAGER);
    }
    
	@Override
    public List<AttributeFieldError> validate(AttributeTracer tracer) {
        List<AttributeFieldError> errors = super.validate(tracer);
        try {
            if (null == this.getResources()) {
				return errors;
			}
            ILangManager langManager = (ILangManager) this.getBeanFactory().getBean(SystemConstants.LANGUAGE_MANAGER, ILangManager.class);
            List<Lang> langs = langManager.getLangs();
            for (int i = 0; i < langs.size(); i++) {
                Lang lang = langs.get(i);
                ResourceInterface resource = this.getResource(lang.getCode());
                if (null == resource) {
					continue;
				}
                AttributeTracer resourceTracer = (AttributeTracer) tracer.clone();
                resourceTracer.setLang(lang);
                String resourceMainGroup = resource.getMainGroup();
                Content parentContent = (Content) this.getParentEntity();
                if (!resourceMainGroup.equals(Group.FREE_GROUP_NAME) 
                        && !resourceMainGroup.equals(parentContent.getMainGroup()) 
                        && !parentContent.getGroups().contains(resourceMainGroup)) {
                    AttributeFieldError fieldError = new AttributeFieldError(this, ICmsAttributeErrorCodes.INVALID_RESOURCE_GROUPS, resourceTracer);
                    fieldError.setMessage("Invalid resource group - " + resourceMainGroup);
                    errors.add(fieldError);
                }
            }
        } catch (Throwable t) {
        	_logger.error("Error validating text attribute", t);
            //ApsSystemUtils.logThrowable(t, this, "validate");
            throw new RuntimeException("Error validating text attribute", t);
        }
        return errors;
    }
    
    private Map<String, ResourceInterface> _resources = new HashMap<String, ResourceInterface>();
    public static final String REFERENCED_RESOURCE_INDICATOR = "ref";
    
}
