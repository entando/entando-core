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
package com.agiletec.apsadmin.admin;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * DOM support class used to handle the configuration parameters.
 * @author E.Santoboni
 */
public class SystemParamsUtils {

	private static final Logger _logger = LoggerFactory.getLogger(SystemParamsUtils.class);
	
    /**
     * Return the configuration params contained in the given XML.
     * @param xmlParams XML string containing the parameters to fetch.
     * @return The resulting parameters map.
     * @throws Throwable if errors are detected.
     */
    public static Map<String, String> getParams(String xmlParams) throws Throwable {
        Map<String, String> params = new HashMap<String, String>();
        Document doc = decodeDOM(xmlParams);
        Element element = doc.getRootElement();
        insertParams(element, params);
        return params;
    }
    
    private static void insertParams(Element currentElement, Map<String, String> params) {
        if ("Param".equals(currentElement.getName())) {
            String key = currentElement.getAttributeValue("name");
            String value = currentElement.getText();
            params.put(key, value);
        }
        List<Element> elements = currentElement.getChildren();
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            insertParams(element, params);
        }
    }
    
    /**
     * Return the XML of the configuration with updated parameters.
     * NOTE: All the values contained in the map but NOT in the given XML are ignored. 
     * @param oldXmlParams The old configuration XML.
     * @param newSystemParams The map with updated values
     * @return String The new system configuration string.
     * @throws Throwable if errors are detected.
     */
    public static String getNewXmlParams(String oldXmlParams, Map<String, String> newSystemParams) throws Throwable {
        Document doc = decodeDOM(oldXmlParams);
        Element element = doc.getRootElement();
        updateParameters(element, newSystemParams);
        return getXMLDocument(doc);
    }
    
    private static void updateParameters(Element rootElement, Map<String, String> parameters) {
        Iterator<String> newParamsName = parameters.keySet().iterator();
        while (newParamsName.hasNext()) {
            String paramName = newParamsName.next();
            Element paramElement = searchParamElement(rootElement, paramName);
            if (null != paramElement) {
                String value = parameters.get(paramName);
                paramElement.setText(value);
            } else {
                Element extraParamsElement = rootElement.getChild(EXTRA_PARAMS_ELEMENT);
                if (null == extraParamsElement) {
                    extraParamsElement = new Element(EXTRA_PARAMS_ELEMENT);
                    rootElement.addContent(extraParamsElement);
                }
                Element extraParamElement = new Element(PARAM_ELEMENT);
                extraParamElement.setText(parameters.get(paramName));
                extraParamElement.setAttribute("name", paramName);
                extraParamsElement.addContent(extraParamElement);
            }
        }
    }
    
    public static String getXMLDocument(Document doc) {
        XMLOutputter out = new XMLOutputter();
        Format format = Format.getPrettyFormat();
        format.setIndent("\t");
        out.setFormat(format);
        return out.outputString(doc);
    }
    
    private static Element searchParamElement(Element currentElement, String paramName) {
        String elementName = currentElement.getName();
        String key = currentElement.getAttributeValue("name");
        if (PARAM_ELEMENT.equals(elementName) && paramName.equals(key)) {
            return currentElement;
        } else {
            List<Element> elements = currentElement.getChildren();
            for (int i = 0; i < elements.size(); i++) {
                Element element = elements.get(i);
                Element result = searchParamElement(element, paramName);
                if (null != result) {
                    return result;
                }
            }
        }
        return null;
    }
    
    private static Document decodeDOM(String xmlText) throws ApsSystemException {
        Document doc = null;
        try {
            SAXBuilder builder = new SAXBuilder();
            builder.setValidation(false);
            StringReader reader = new StringReader(xmlText);
            doc = builder.build(reader);
        } catch (Throwable t) {
            _logger.error("Error parsing xml: {} ", xmlText, t);
            throw new ApsSystemException("Error parsing document", t);
        }
        return doc;
    }
    
    public static final String PARAMS_ELEMENT = "Params";
    public static final String PARAM_ELEMENT = "Param";
    public static final String EXTRA_PARAMS_ELEMENT = "ExtraParams";
    
}
