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
import java.util.List;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractJAXBAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.TextAttribute;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.CmsAttributeReference;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.util.SymbolicLinkValidator;
import com.agiletec.plugins.jacms.aps.system.services.linkresolver.ILinkResolverManager;

/**
 * Rappresenta una informazione di tipo "link". La destinazione del link è la
 * stessa per tutte le lingue, ma il testo associato varia con la lingua.
 *
 * @author W.Ambu - S.Didaci
 */
public class LinkAttribute extends TextAttribute implements IReferenceableAttribute {

    private static final Logger logger = LoggerFactory.getLogger(LinkAttribute.class);

    @Override
    public Object getAttributePrototype() {
        LinkAttribute prototype = (LinkAttribute) super.getAttributePrototype();
        prototype.setContentManager(this.getContentManager());
        prototype.setLinkResolverManager(this.getLinkResolverManager());
        prototype.setPageManager(this.getPageManager());
        return prototype;
    }

    @Override
    public Element getJDOMElement() {
        Element attributeElement = this.createRootElement("attribute");
        if (null != this.getSymbolicLink()) {
            Element linkElement = new Element("link");
            attributeElement.addContent(linkElement);
            Element dest;
            int type = this.getSymbolicLink().getDestType();
            switch (type) {
                case SymbolicLink.URL_TYPE:
                    linkElement.setAttribute("type", "external");
                    dest = new Element("urldest");
                    dest.addContent(this.getSymbolicLink().getUrlDest());
                    linkElement.addContent(dest);
                    break;
                case SymbolicLink.PAGE_TYPE:
                    linkElement.setAttribute("type", "page");
                    dest = new Element("pagedest");
                    dest.addContent(this.getSymbolicLink().getPageDest());
                    linkElement.addContent(dest);
                    break;
                case SymbolicLink.CONTENT_TYPE:
                    linkElement.setAttribute("type", "content");
                    dest = new Element("contentdest");
                    dest.addContent(this.getSymbolicLink().getContentDest());
                    linkElement.addContent(dest);
                    break;
                case SymbolicLink.CONTENT_ON_PAGE_TYPE:
                    linkElement.setAttribute("type", "contentonpage");
                    dest = new Element("pagedest");
                    dest.addContent(this.getSymbolicLink().getPageDest());
                    linkElement.addContent(dest);
                    dest = new Element("contentdest");
                    dest.addContent(this.getSymbolicLink().getContentDest());
                    linkElement.addContent(dest);
                    break;
                default:
                    linkElement.setAttribute("type", "");
            }
        }
        super.addTextElements(attributeElement);
        return attributeElement;
    }

    /**
     * Restituisce la stringa rappresentante la destinazione simbolica. Il
     * metodo è atto ad essere utilizzato nel modello di renderizzazione e la
     * stringa restituita sarà successivamente risolta in fase di
     * renderizzazione dal servizio linkResolver.
     *
     * @return La stringa rappresentante la destinazione simbolica.
     */
    public String getDestination() {
        String destination = "";
        if (null != this.getSymbolicLink()) {
            destination = this.getSymbolicLink().getSymbolicDestination();
            if (this.getSymbolicLink().getDestType() == SymbolicLink.URL_TYPE) {
                destination = destination.replaceAll("&(?![a-z]+;)", "&amp;");
            }
        }
        return destination;
    }

    /**
     * Sovrascrittura del metodo della classe astratta da cui deriva. Poichè
     * questo tipo di attributo non può mai essere "searchable", restituisce
     * sempre false.
     *
     * @return Restituisce sempre false
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
    public List<CmsAttributeReference> getReferences(List<Lang> systemLangs) {
        List<CmsAttributeReference> refs = new ArrayList<>();
        SymbolicLink symbLink = this.getSymbolicLink();
        if (null != symbLink && (symbLink.getDestType() != SymbolicLink.URL_TYPE)) {
            CmsAttributeReference ref = new CmsAttributeReference(symbLink.getPageDest(),
                    symbLink.getContentDest(), null);
            refs.add(ref);
        }
        return refs;
    }

    @Override
    public Object getValue() {
        return this.getSymbolicLink();
    }

    @Override
    protected AbstractJAXBAttribute getJAXBAttributeInstance() {
        return new JAXBLinkAttribute();
    }

    @Override
    public AbstractJAXBAttribute getJAXBAttribute(String langCode) {
        JAXBLinkAttribute jaxbAttribute = (JAXBLinkAttribute) super.createJAXBAttribute(langCode);
        if (null == jaxbAttribute || null == this.getSymbolicLink()) {
            return jaxbAttribute;
        }
        JAXBLinkValue value = new JAXBLinkValue();
        String text = this.getTextForLang(langCode);
        value.setText(text);
        value.setUrl(this.getLinkResolverManager().resolveLink(this.getSymbolicLink(), this.getParentEntity().getId(), null));
        value.setSymbolicLink(this.getSymbolicLink());
        jaxbAttribute.setLinkValue(value);
        return jaxbAttribute;
    }

    @Override
    public void valueFrom(AbstractJAXBAttribute jaxbAttribute) {
        super.valueFrom(jaxbAttribute);
        JAXBLinkValue value = ((JAXBLinkAttribute) jaxbAttribute).getLinkValue();
        if (null == value) {
            return;
        }
        this.setSymbolicLink(value.getSymbolicLink());
        String textValue = value.getText();
        if (null == textValue) {
            return;
        }
        this.getTextMap().put(this.getDefaultLangCode(), textValue);
    }

    @Override
    public Status getStatus() {
        Status textStatus = super.getStatus();
        Status linkStatus = (null != this.getSymbolicLink()) ? Status.VALUED : Status.EMPTY;
        if (!textStatus.equals(linkStatus)) {
            return Status.INCOMPLETE;
        }
        if (textStatus.equals(linkStatus) && textStatus.equals(Status.VALUED)) {
            return Status.VALUED;
        }
        return Status.EMPTY;
    }

    @Override
    public List<AttributeFieldError> validate(AttributeTracer tracer) {
        List<AttributeFieldError> errors = super.validate(tracer);
        try {
            SymbolicLink symbolicLink = this.getSymbolicLink();
            if (null == symbolicLink) {
                return errors;
            }
            SymbolicLinkValidator sler = new SymbolicLinkValidator(this.getContentManager(), this.getPageManager());
            String linkErrorCode = sler.scan(symbolicLink, (Content) this.getParentEntity());
            if (null != linkErrorCode) {
                AttributeFieldError error = new AttributeFieldError(this, linkErrorCode, tracer);
                error.setMessage("Invalid link - page " + symbolicLink.getPageDest()
                        + " - content " + symbolicLink.getContentDest() + " - Error code " + linkErrorCode);
                errors.add(error);
            }
        } catch (Throwable t) {
            logger.error("Error validating link attribute", t);
            throw new RuntimeException("Error validating link attribute", t);
        }
        return errors;
    }

    /**
     * Setta il link simbolico caratterizzante l'attributo.
     *
     * @param symbolicLink Il link simbolico.
     */
    public void setSymbolicLink(SymbolicLink symbolicLink) {
        this.symbolicLink = symbolicLink;
    }

    /**
     * Restituisce il link simbolico caratterizzante l'attributo.
     *
     * @return Il link simbolico.
     */
    public SymbolicLink getSymbolicLink() {
        return symbolicLink;
    }

    protected IContentManager getContentManager() {
        return contentManager;
    }

    public void setContentManager(IContentManager contentManager) {
        this.contentManager = contentManager;
    }

    protected IPageManager getPageManager() {
        return pageManager;
    }

    public void setPageManager(IPageManager pageManager) {
        this.pageManager = pageManager;
    }

    protected ILinkResolverManager getLinkResolverManager() {
        return linkResolverManager;
    }

    public void setLinkResolverManager(ILinkResolverManager linkResolverManager) {
        this.linkResolverManager = linkResolverManager;
    }

    private transient SymbolicLink symbolicLink;
    private transient IContentManager contentManager;
    private transient IPageManager pageManager;
    private transient ILinkResolverManager linkResolverManager;

}
