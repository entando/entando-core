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
package com.agiletec.plugins.jacms.aps.system.services.content.parse.extraAttribute;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.agiletec.aps.system.common.entity.parse.attribute.TextAttributeHandler;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.ResourceAttributeInterface;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;

/**
 * Classe handler per l'interpretazione della porzione di xml 
 * relativo all'attributo di tipo risorsa (Image o Attach).
 * @author E.Santoboni
 */
public class ResourceAttributeHandler extends TextAttributeHandler {
	
	@Override
	public Object getAttributeHandlerPrototype() {
		ResourceAttributeHandler handler = (ResourceAttributeHandler) super.getAttributeHandlerPrototype();
		handler.setResourceManager(this.getResourceManager());
		return handler;
	}
	
	@Override
	public void startAttribute(Attributes attributes, String qName) throws SAXException {
		if (qName.equals("resource")) {
			this.startResource(attributes, qName);
		} else {
			super.startAttribute(attributes, qName);
		}
	}
	
	private void startResource(Attributes attributes, String qName) throws SAXException {
		String id = extractAttribute(attributes, "id", qName, true);
		String langCode = extractAttribute(attributes, "lang", qName, false);
		try {
			ResourceInterface resource = this.getResourceManager().loadResource(id);
			if (null != this.getCurrentAttr() && null != resource) {
				((ResourceAttributeInterface) this.getCurrentAttr()).setResource(resource, langCode);
			}
		} catch (Exception e) {
			throw new SAXException(e);
		}
	}
	
	@Override
	public void endAttribute(String qName, StringBuffer textBuffer) {
		if (qName.equals("resource")) {
			this.endResource();
		} else {
			super.endAttribute(qName, textBuffer);
		}
	}
	
	private void endResource() {
		return; // nulla da fare
	}
	
	/**
	 * Restituisce il manager delle risorse.
	 * @return Il Manager delle risorse.
	 */
	protected IResourceManager getResourceManager() {
		return this._resourceManager;
	}
	
	/**
	 * Setta il Manager delle risorse.
	 * @param resourceManager Il manager delle risorse.
	 */
	public void setResourceManager(IResourceManager resourceManager) {
		this._resourceManager = resourceManager;
	}
	
	private IResourceManager _resourceManager;
	
}
