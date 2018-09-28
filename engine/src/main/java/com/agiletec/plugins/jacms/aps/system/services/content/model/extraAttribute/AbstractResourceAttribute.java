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
package com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractJAXBAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.TextAttribute;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.model.CmsAttributeReference;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.util.ICmsAttributeErrorCodes;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import org.apache.commons.lang3.StringUtils;

/**
 * Classe astratta di appoggio agli attributi di tipo Risorsa.
 *
 * @author E.Santoboni
 */
public abstract class AbstractResourceAttribute extends TextAttribute
        implements IReferenceableAttribute, ResourceAttributeInterface {

    private static final Logger logger = LoggerFactory.getLogger(AbstractResourceAttribute.class);

    private Map<String, ResourceInterface> resources = new HashMap<>();

    private transient ConfigInterface configManager;
    private transient IResourceManager resourceManager;

    private Map<String, Map<String, String>> metadatas;

    public AbstractResourceAttribute() {
        this.metadatas = new HashMap<>();
    }

    @Override
    public String getResourceAlt() {
        return this.getMetadata(this.getResourceAltMap());
    }

    @Override
    public String getResourceAltForLang(String langCode) {
        return this.getMetadataForLang(this.getResourceAltMap(), langCode);
    }

    @Override
    public String getResourceDescription() {
        return this.getMetadata(this.getResourceDescriptionMap());
    }

    @Override
    public String getResourceDescriptionForLang(String langCode) {
        return this.getMetadataForLang(this.getResourceDescriptionMap(), langCode);
    }

    @Override
    public String getResourceLegend() {
        return this.getMetadata(this.getResourceLegendMap());
    }

    @Override
    public String getResourceLegendForLang(String langCode) {
        return this.getMetadataForLang(this.getResourceLegendMap(), langCode);
    }

    @Override
    public String getResourceTitle() {
        return this.getMetadata(this.getResourceTitleMap());
    }

    @Override
    public String getResourceTitleForLang(String langCode) {
        return this.getMetadataForLang(this.getResourceTitleMap(), langCode);
    }

    private String getMetadataForLang(Map<String, String> map, String langCode) {
        String text = (String) map.get(langCode);
        if (text == null) {
            text = "";
        }
        return text;
    }

    private String getMetadata(Map<String, String> map) {
        String text = (String) map.get(this.getRenderingLang());
        if (text == null) {
            text = (String) map.get(this.getDefaultLangCode());
            if (text == null) {
                text = "";
            }
        }
        return text;
    }

    public Map<String, Map<String, String>> getMetadatas() {
        return metadatas;
    }

    public void setMetadatas(Map<String, Map<String, String>> metadatas) {
        this.metadatas = metadatas;
    }

    private Map<String, String> getMetadataMap(String key, boolean addNewMap) {
        if (this.getMetadatas().containsKey(key)) {
            return this.getMetadatas().get(key);
        } else {
            Map<String, String> map = new HashMap<>();
            if (addNewMap) {
                this.getMetadatas().put(key, map);
            }
            return map;
        }
    }

    public void setMetadataMap(String key, Map<String, String> map) {
        this.getMetadatas().put(key, map);
    }

    @Override
    public String getMetadataForLang(String key, String langCode) {
        Map<String, String> map = this.getMetadataMap(key, false);
        return map.get(langCode);
    }

    @Override
    public String getMetadata(String key) {
        return this.getMetadata(this.getMetadataMap(key, false));
    }

    @Override
    public void setMetadata(String key, String langCode, String value) {
        Map<String, String> map = this.getMetadataMap(key, true);
        map.put(langCode, value);
    }

    public Map<String, String> getResourceAltMap() {
        return this.getMetadataMap(IResourceManager.ALT_METADATA_KEY, false);
    }

    public Map<String, String> getResourceDescriptionMap() {
        return this.getMetadataMap(IResourceManager.DESCRIPTION_METADATA_KEY, false);
    }

    public Map<String, String> getResourceLegendMap() {
        return this.getMetadataMap(IResourceManager.LEGEND_METADATA_KEY, false);
    }

    public Map<String, String> getResourceTitleMap() {
        return this.getMetadataMap(IResourceManager.TITLE_METADATA_KEY, false);
    }

    @Override
    public Object getAttributePrototype() {
        AbstractResourceAttribute prototype = (AbstractResourceAttribute) super.getAttributePrototype();
        prototype.setConfigManager(this.getConfigManager());
        prototype.setResourceManager(this.getResourceManager());
        return prototype;
    }

    /**
     * Setta una risorsa sull'attributo.
     *
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
     *
     * @param langCode il codice della lingua.
     * @return la risorsa associata all'attributo.
     */
    @Override
    public ResourceInterface getResource(String langCode) {
        return (ResourceInterface) this.getResources().get(langCode);
    }

    /**
     * Restituisce la risorsa associata all'attributo.
     *
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
     * questo tipo di attributo non può mai essere "searchable", restituisce
     * sempre false.
     *
     * @return Restituisce sempre false
     * @see
     * com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface#isSearchable()
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
        this.addResourceMetadatasElement(attributeElement);
        return attributeElement;
    }

    /*
    <metadatas>
        <metadata key="xxxxx" lang="en">value</metadata>
    </metadatas>
     */
    protected void addResourceMetadatasElement(Element attributeElement) {
        if (null == this.getMetadatas() || this.getMetadatas().isEmpty()) {
            return;
        }
        Element metadatasElement = new Element("metadatas");
        this.getMetadatas().keySet().stream().forEach(key -> {
            Map<String, String> map = this.getMetadatas().get(key);
            map.keySet().stream().forEach(langCode -> {
                Element metadataElement = new Element("metadata");
                metadataElement.setAttribute("key", key);
                metadataElement.setAttribute("lang", langCode);
                metadataElement.setText(map.get(langCode).trim());
                metadatasElement.addContent(metadataElement);
            });
        });
        attributeElement.addContent(metadatasElement);
    }

    protected void addResourceTextElements(Element attributeElement, String elementName, Map<String, String> map) {
        if (null == map) {
            return;
        }
        Iterator<String> langIter = map.keySet().iterator();
        while (langIter.hasNext()) {
            String currentLangCode = langIter.next();
            String text = map.get(currentLangCode);
            if (!StringUtils.isEmpty(text)) {
                Element textElement = new Element(elementName);
                textElement.setAttribute("lang", currentLangCode);
                textElement.setText(text.trim());
                attributeElement.addContent(textElement);
            }
        }
    }

    /**
     * Appende, nella stringa rappresentante l'url della risorsa interna ad un
     * entità, il riferimento al entità padre con la sintassi
     * <baseUrl>/<REFERENCED_RESOURCE_INDICATOR>/<PARENT_CONTENT_ID>/. Tale
     * operazione viene effettuata nel caso che la risorsa non sia libera.
     *
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
        List<CmsAttributeReference> refs = new ArrayList<>();
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
        return this.getResources();
    }

    @Override
    protected AbstractJAXBAttribute getJAXBAttributeInstance() {
        return new JAXBResourceAttribute();
    }

    @Override
    public AbstractJAXBAttribute getJAXBAttribute(String langCode) {
        JAXBResourceAttribute jaxbResourceAttribute = (JAXBResourceAttribute) super.createJAXBAttribute(langCode);
        if (null == jaxbResourceAttribute) {
            return null;
        }
        if (null == langCode) {
            langCode = this.getDefaultLangCode();
        }
        ResourceInterface resource = this.getResource(langCode);
        if (null == resource) {
            return jaxbResourceAttribute;
        }
        JAXBResourceValue value = new JAXBResourceValue();
        try {
            String text = this.getTextForLang(langCode);
            value.setText(text);
            this.setRenderingLang(langCode);
            String path = this.getDefaultPath();
            value.setPath(path);
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
        } catch (Throwable t) {
            logger.error("Error creating jaxb response. lang: {}", langCode, t);
            throw new RuntimeException("Error creating jaxb response", t);
        }
        jaxbResourceAttribute.setResource(value);
        return jaxbResourceAttribute;
    }

    @Override
    public void valueFrom(AbstractJAXBAttribute jaxbAttribute, String langCode) {
        super.valueFrom(jaxbAttribute, langCode);
        JAXBResourceValue value = ((JAXBResourceAttribute) jaxbAttribute).getResource();
        if (null == value) {
            return;
        }
        Object resourceId = value.getResourceId();
        if (null == resourceId) {
            return;
        }
        try {
            ResourceInterface resource = this.getResourceManager().loadResource(resourceId.toString());
            if (null != resource) {
                this.setResource(resource, this.getDefaultLangCode());
            }
            Object text = value.getText();
            if (null == text) {
                return;
            }
            String langToSet = (null != langCode) ? langCode : this.getDefaultLangCode();
            this.getTextMap().put(langToSet, text.toString());
        } catch (Exception e) {
            logger.error("Error extracting resource from jaxbAttribute", e);
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
        return this.resources;
    }

    @Override
    public List<AttributeFieldError> validate(AttributeTracer tracer) {
        List<AttributeFieldError> errors = super.validate(tracer);
        try {
            if (null == this.getResources()) {
                return errors;
            }
            List<Lang> langs = super.getLangManager().getLangs();
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
            logger.error("Error validating text attribute", t);
            throw new RuntimeException("Error validating text attribute", t);
        }
        return errors;
    }

    protected ConfigInterface getConfigManager() {
        return configManager;
    }

    public void setConfigManager(ConfigInterface configManager) {
        this.configManager = configManager;
    }

    protected IResourceManager getResourceManager() {
        return resourceManager;
    }

    public void setResourceManager(IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

}
