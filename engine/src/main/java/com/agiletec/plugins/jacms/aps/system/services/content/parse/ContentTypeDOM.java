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
package com.agiletec.plugins.jacms.aps.system.services.content.parse;

import org.jdom.Element;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.parse.EntityTypeDOM;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * Classe delegata al caricamento dei tipi di contenuto dalla configurazione. 
 * Il risultato è una mappa dei "prototipi" dei contenuti.
 * La classe deve essere utilizzata per un parsing e poi abbandonata.
 * Nota sul codice sorgente: purtroppo il conflitto di nomenclatura tra "Attribute"
 * dei contenuti e "Attribute" dei tag XML rende difficoltosa la lettura del codice;
 * occorre molta attenzione nell'interpretazione dei nomi di variabili e metodi privati.
 * @author M.Diana - E.Santoboni
 */
public class ContentTypeDOM extends EntityTypeDOM {
	
	@Override
	protected IApsEntity createEntityType(Element contentElem, Class entityClass) throws ApsSystemException {
		Content content = (Content) super.createEntityType(contentElem, entityClass);
		content.setId(null);
		String viewPage = this.extractXmlAttribute(contentElem, "viewpage", true);
		if (!viewPage.equals(NULL_VALUE)) {
			content.setViewPage(viewPage);
		}
		String listModel = this.extractXmlAttribute(contentElem, "listmodel", true);
		if (!listModel.equals(NULL_VALUE)) {
			content.setListModel(listModel);
		}
		String defaultModel = this.extractXmlAttribute(contentElem, "defaultmodel", true);
		if (!defaultModel.equals(NULL_VALUE)) {
			content.setDefaultModel(defaultModel);
		}
		content.setStatus(Content.STATUS_NEW);
		return content;
	}
	
	@Override
	protected Element createRootTypeElement(IApsEntity currentEntityType) {
		Element typeElement = super.createRootTypeElement(currentEntityType);
		Content content = (Content) currentEntityType;
		this.setXmlAttribute(typeElement, "viewpage", content.getViewPage());
		this.setXmlAttribute(typeElement, "listmodel", content.getListModel());
		this.setXmlAttribute(typeElement, "defaultmodel", content.getDefaultModel());
		return typeElement;
	}
	
	private void setXmlAttribute(Element element, String name, String value) {
		String valueToSet = value;
		if (null == value || value.trim().length() == 0) {
			valueToSet = NULL_VALUE;
		}
		element.setAttribute(name, valueToSet);
	}
	
	@Override
	protected String getEntityTypeRootElementName() {
		return "contenttype";
	}
	
	@Override
	protected String getEntityTypesRootElementName() {
		return "contenttypes";
	}
	
	private static final String NULL_VALUE = "**NULL**";
	
}
