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

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.agiletec.aps.system.common.entity.parse.EntityHandler;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * Classe "handler" di supporto all'interpretazione 
 * dell'XML che rappresenta un contenuto.
 * @author M.Diana - E.Santoboni
 */
public class ContentHandler extends EntityHandler {

	private static final Logger _logger = LoggerFactory.getLogger(ContentHandler.class);
	
	@Override
	protected void startEntityElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		try {
			if (qName.equals("status")) {
				this.startStatus(attributes, qName);
			} else if (qName.equals("version")) {
				this.startVersion(attributes, qName);
			} else if (qName.equals("lastEditor")) {
				this.startLastEditor(attributes, qName);
			} else if (qName.equals("created")) {
				this.startCreated(attributes, qName);
			} else if (qName.equals("lastModified")) {
				this.startLastModified(attributes, qName);
			}
		} catch (SAXException e) {
			_logger.error("error in start element", e);
			throw e;
		} catch (Throwable t) {
			_logger.error("error in start element", t);
			throw new SAXException(t.getMessage());
		}
	}
	
	@Override
	protected void endEntityElement(String uri, String localName, String qName) throws SAXException {
		try {
			if (qName.equals("status")) {
				this.endStatus();
			} else if (qName.equals("version")) {
				this.endVersion();
			} else if (qName.equals("lastEditor")) {
				this.endLastEditor();
			} else if (qName.equals("created")) {
				this.endCreated();
			} else if (qName.equals("lastModified")) {
				this.endLastModified();
			}
		} catch (Throwable t) {
			_logger.error("error in end element", t);
			throw new SAXException(t.getMessage());
		}
	}
	
	private void startStatus(Attributes attributes, String qName) throws SAXException {
		return; // nothing to do
	}
	
	private void startVersion(Attributes attributes, String qName) throws SAXException {
		return; // nothing to do
	}
	
	private void startLastEditor(Attributes attributes, String qName) throws SAXException {
		return; // nothing to do
	}
	
	private void startCreated(Attributes attributes, String qName) throws SAXException {
		return; // nothing to do
	}
	
	private void startLastModified(Attributes attributes, String qName) throws SAXException {
		return; // nothing to do
	}
	
	private void endStatus() {
		StringBuffer textBuffer = this.getTextBuffer();
		if (null != textBuffer) {
			((Content) this.getCurrentEntity()).setStatus(textBuffer.toString());
		}
	}
	
	private void endVersion() {
		StringBuffer textBuffer = this.getTextBuffer();
		if (null != textBuffer) {
			((Content) this.getCurrentEntity()).setVersion(textBuffer.toString());
		}
	}
	
	private void endLastEditor() {
		StringBuffer textBuffer = this.getTextBuffer();
		if (null != textBuffer) {
			((Content) this.getCurrentEntity()).setLastEditor(textBuffer.toString());
		}
	}
	
	private void endCreated() {
		StringBuffer textBuffer = this.getTextBuffer();
		if (null != textBuffer) {
			Date date = DateConverter.parseDate(textBuffer.toString(), JacmsSystemConstants.CONTENT_METADATA_DATE_FORMAT);
			((Content) this.getCurrentEntity()).setCreated(date);
		}
	}
	
	private void endLastModified() {
		StringBuffer textBuffer = this.getTextBuffer();
		if (null != textBuffer) {
			Date date = DateConverter.parseDate(textBuffer.toString(), JacmsSystemConstants.CONTENT_METADATA_DATE_FORMAT);
			((Content) this.getCurrentEntity()).setLastModified(date);
		}
	}
	
}