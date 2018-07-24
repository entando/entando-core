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
package com.agiletec.plugins.jacms.aps.system.services.content.parse.extraAttribute;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.agiletec.aps.system.common.entity.parse.attribute.TextAttributeHandler;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.LinkAttribute;

/**
 * Classe handler per l'interpretazione della porzione di xml 
 * relativo all'attributo di tipo link.
 * @author E.Santoboni
 */
public class LinkAttributeHandler extends TextAttributeHandler {
	
	@Override
	public void startAttribute(Attributes attributes, String qName) throws SAXException {
		if (qName.equals("link")) {
			startLink(attributes, qName);
		} else if (qName.equals("urldest")) {
			startUrlDest(attributes, qName);
		} else if (qName.equals("pagedest")) {
			startPageDest(attributes, qName);
		} else if (qName.equals("contentdest")) {
            startContentDest(attributes, qName);
        } else if (qName.equals("properties")) {
            startProperties(attributes, qName);
        } else if (qName.equals("property")) {
            startProperty(attributes, qName);
        } else {
			super.startAttribute(attributes, qName);
		}
	}

    private void startProperty(Attributes attributes, String qName) throws SAXException {
        this.propertyKey = extractAttribute(attributes, "key", qName, true);
    }

    private void startProperties(Attributes attributes, String qName) {
	    // nothing to do
    }

    private void startLink(Attributes attributes, String qName) throws SAXException {
		this.linkType = extractAttribute(attributes, "type", qName, true);
		((LinkAttribute) this.getCurrentAttr()).setSymbolicLink(new SymbolicLink());
	}
	
	private void startUrlDest(Attributes attributes, String qName) throws SAXException {
		return; // niente da fare
	}
	
	private void startPageDest(Attributes attributes, String qName) throws SAXException {
		return; // niente da fare
	}
	
	private void startContentDest(Attributes attributes, String qName) throws SAXException {
		return; // niente da fare
	}
	
	@Override
	public void endAttribute(String qName, StringBuffer textBuffer) {
		if (qName.equals("link")) {
			endLink();
		} else if (qName.equals("urldest")) {
			endUrlDest(textBuffer);
		} else if (qName.equals("pagedest")){
			endPageDest(textBuffer);
		} else if (qName.equals("contentdest")) {
            endContentDest(textBuffer);
        } else if (qName.equals("property")) {
            endProperty(textBuffer);
        } else if (qName.equals("properties")) {
            endProperties(textBuffer);
        } else {
			super.endAttribute(qName, textBuffer);
		}
	}

    private void endProperties(StringBuffer textBuffer) {
	    // nothing to do
    }

    private void endProperty(StringBuffer textBuffer) {
        if (null != textBuffer) {
            String propertyValue = textBuffer.toString();
            ((LinkAttribute) this.getCurrentAttr()).getLinkProperties().put(this.propertyKey, propertyValue);
        }
        this.propertyKey = null;
    }

    private void endLink() {
		SymbolicLink symLink = 
			((LinkAttribute) this.getCurrentAttr()).getSymbolicLink();
		if (null != symLink && null != linkType) {
			if (linkType.equals("content")) {
				symLink.setDestinationToContent(contentDest);
			} else if (linkType.equals("external")) {
				symLink.setDestinationToUrl(urlDest);
			} else if (linkType.equals("page")) {
				symLink.setDestinationToPage(pageDest);
			} else if (linkType.equals("contentonpage")) {
				symLink.setDestinationToContentOnPage(contentDest, pageDest);
			}

		}
		contentDest = null;
		urlDest = null;
		pageDest = null;
	}
	
	private void endUrlDest(StringBuffer textBuffer){
		if (null != textBuffer) {
			urlDest = textBuffer.toString();
		}
	}
	
	private void endPageDest(StringBuffer textBuffer){
		if (null != textBuffer) {
			pageDest = textBuffer.toString();
		}
	}
	
	private void endContentDest(StringBuffer textBuffer){
		if (null != textBuffer) {
			contentDest = textBuffer.toString();
		}
	}
	
	private String linkType;
	private String urlDest;
	private String pageDest;
	private String contentDest;
	private String propertyKey;
	
}
