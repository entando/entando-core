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
package org.entando.entando.aps.system.init.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.entando.entando.aps.system.init.model.Component;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public class ComponentDefDOM {

	private static final Logger _logger = LoggerFactory.getLogger(ComponentDefDOM.class);
	
    protected ComponentDefDOM(String xmlText, String configPath) throws ApsSystemException {
        this.validate(xmlText, configPath);
        _logger.debug("Loading Component from file : {}", configPath);
        this.decodeDOM(xmlText);
    }
    
    private void validate(String xmlText, String configPath) throws ApsSystemException {
        SchemaFactory factory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        InputStream schemaIs = null;
        InputStream xmlIs = null;
        try {
            schemaIs = this.getClass().getResourceAsStream("componentDef-3.0.xsd");
            Source schemaSource = new StreamSource(schemaIs);
            Schema schema = factory.newSchema(schemaSource);
            Validator validator = schema.newValidator();
            xmlIs = new ByteArrayInputStream(xmlText.getBytes("UTF-8"));
            Source source = new StreamSource(xmlIs);
            validator.validate(source);
            _logger.debug("Valid Component definition : {}", configPath);
        } catch (Throwable t) {
            _logger.error("Error validating Component definition : {}", configPath, t);
        	String message = "Error validating Component definition : " + configPath;
            //ApsSystemUtils.logThrowable(t, this, "this", message);
            throw new ApsSystemException(message, t);
        } finally {
            try {
                if (null != schemaIs) {
                    schemaIs.close();
                }
                if (null != xmlIs) {
                    xmlIs.close();
                }
            } catch (IOException e) {
            	_logger.error("error in validate", e);
                //ApsSystemUtils.logThrowable(e, this, "this");
            }
        }
    }
    
    protected Component getComponent(Map<String, String> postProcessClasses) {
        Component component = null;
        try {
            Element rootElement = this._doc.getRootElement();
			component = new Component(rootElement, postProcessClasses);
        } catch (Throwable t) {
        	_logger.error("Error loading component", t);
            //ApsSystemUtils.logThrowable(t, this, "getComponent", "Error loading component");
        }
        return component;
    }
	
    private void decodeDOM(String xmlText) throws ApsSystemException {
        SAXBuilder builder = new SAXBuilder();
        builder.setValidation(false);
        StringReader reader = new StringReader(xmlText);
        try {
            this._doc = builder.build(reader);
        } catch (Throwable t) {
        	_logger.error("Error detected while parsing the XML {}", xmlText, t);
            //ApsSystemUtils.logThrowable(t, this, "decodeDOM", "Error while parsing: " + t.getMessage());
            throw new ApsSystemException("Error detected while parsing the XML", t);
        }
    }
	
    private Document _doc;
    
}
