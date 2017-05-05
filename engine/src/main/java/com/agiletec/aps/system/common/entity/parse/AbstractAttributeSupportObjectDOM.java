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
package com.agiletec.aps.system.common.entity.parse;

import com.agiletec.aps.system.exception.ApsSystemException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * Abstract Dom class parser of Attribute support Objects.
 * @author E.Santoboni
 */
public abstract class AbstractAttributeSupportObjectDOM {
	
	private static final Logger _logger = LoggerFactory.getLogger(AbstractAttributeSupportObjectDOM.class);
	
	protected void validate(String xmlText, String definitionPath) throws ApsSystemException {
		SchemaFactory factory = 
            SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		InputStream schemaIs = null;
		InputStream xmlIs = null;
		try {
			schemaIs = this.getClass().getResourceAsStream(this.getSchemaFileName());
			Source schemaSource = new StreamSource(schemaIs);
			Schema schema = factory.newSchema(schemaSource);
	        Validator validator = schema.newValidator();
	        xmlIs = new ByteArrayInputStream(xmlText.getBytes(StandardCharsets.UTF_8));
	        Source source = new StreamSource(xmlIs);
	        validator.validate(source);
	        _logger.debug("Valid definition : {}", definitionPath);
        } catch (SAXException | IOException t) {
        	String message = "Error validating definition : " + definitionPath;
        	_logger.error("Error validating definition : {}", definitionPath, t);
        	throw new ApsSystemException(message, t);
        } finally {
        	try {
				if (null != schemaIs) schemaIs.close();
				if (null != xmlIs) xmlIs.close();
			} catch (IOException e) {
				_logger.error("Error validating definition path: {} - xml: {}", definitionPath, xmlText, e);
			}
        }
	}
	
	protected Document decodeDOM(String xmlText) throws ApsSystemException {
		Document doc = null;
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		try {
			doc = builder.build(reader);
		} catch (JDOMException | IOException ex) {
			throw new ApsSystemException("Error while parsing: " + ex.getMessage(), ex);
		}
		return doc;
	}
	
	protected abstract String getSchemaFileName();
	
}