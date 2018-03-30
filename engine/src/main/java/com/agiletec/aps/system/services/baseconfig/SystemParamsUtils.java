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
package com.agiletec.aps.system.services.baseconfig;

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
 *
 * @author E.Santoboni
 */
public class SystemParamsUtils {

    private static final Logger _logger = LoggerFactory.getLogger(SystemParamsUtils.class);

    /**
     * Return the configuration params contained in the given XML.
     *
     * @param xmlParams XML string containing the parameters to fetch.
     * @return The resulting parameters map.
     * @throws Exception if errors are detected.
     */
    public static Map<String, String> getParams(String xmlParams) throws Exception {
        Map<String, String> params = new HashMap<>();
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
     * Return the XML of the configuration with updated parameters. NOTE: All
     * the values contained in the map but NOT in the given XML are NOT ignored
     * and will be added into the new xml.
     *
     * @param oldXmlParams The old configuration XML.
     * @param newSystemParams The map with updated values
     * @return The new system configuration string.
     * @throws Exception if errors are detected.
     */
    public static String getNewXmlParams(String oldXmlParams, Map<String, String> newSystemParams) throws Exception {
        return getNewXmlParams(oldXmlParams, newSystemParams, true);
    }

    /**
     * Return the XML of the configuration with updated parameters.
     *
     * @param oldXmlParams The old configuration XML.
     * @param newSystemParams The map with updated values
     * @param addNewParams True if the new parameters have to be added into the
     * xml, otherwise false.
     * @return The new system configuration string.
     * @throws Exception if errors are detected.
     */
    public static String getNewXmlParams(String oldXmlParams, Map<String, String> newSystemParams, boolean addNewParams) throws Exception {
        Document doc = decodeDOM(oldXmlParams);
        Element element = doc.getRootElement();
        updateParameters(element, newSystemParams, addNewParams);
        return getXMLDocument(doc);
    }

    private static void updateParameters(Element rootElement, Map<String, String> parameters, boolean addNewParams) {
        Iterator<String> newParamsName = parameters.keySet().iterator();
        while (newParamsName.hasNext()) {
            String paramName = newParamsName.next();
            Element paramElement = searchParamElement(rootElement, paramName);
            if (null != paramElement) {
                String value = parameters.get(paramName);
                paramElement.setText(value);
            } else if (addNewParams) {
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
