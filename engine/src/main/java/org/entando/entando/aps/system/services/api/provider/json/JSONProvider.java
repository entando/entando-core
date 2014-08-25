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
package org.entando.entando.aps.system.services.api.provider.json;

import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamWriter;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.provider.json.utils.JSONUtils;
import org.codehaus.jettison.mapped.Configuration;
import org.codehaus.jettison.mapped.SimpleConverter;
import org.codehaus.jettison.mapped.TypeConverter;

import org.w3c.dom.Document;

/**
 * @author E.Santoboni
 */
@Produces("application/json")
@Consumes("application/json")
@Provider
public class JSONProvider<T> extends org.apache.cxf.jaxrs.provider.json.JSONProvider<T>  {
    
    private static final String MAPPED_CONVENTION = "mapped";
    private static final String BADGER_FISH_CONVENTION = "badgerfish";
    private static final String DROP_ROOT_CONTEXT_PROPERTY = "drop.json.root.element";
    private static final String DROP_ELEMENT_IN_XML_PROPERTY = "drop.xml.elements";
    private static final String IGNORE_EMPTY_JSON_ARRAY_VALUES_PROPERTY = "ignore.empty.json.array.values";
    static {
        new SimpleConverter();
    }
    
    private ConcurrentHashMap<String, String> namespaceMap = new ConcurrentHashMap<String, String>();
    private boolean dropRootElement;
    private boolean dropElementsInXmlStream = true;
    private boolean ignoreMixedContent;
    private boolean ignoreEmptyArrayValues;
    private boolean writeXsiType = true;
    private boolean ignoreNamespaces;
    private String convention = MAPPED_CONVENTION;
    private TypeConverter typeConverter;
    private boolean attributesToElements;
    private boolean writeNullAsString = true;
    
    @Override
    public void setAttributesToElements(boolean value) {
        this.attributesToElements = value;
		super.setAttributesToElements(value);
    }
    
	@Override
    public void setConvention(String value) {
        if (!MAPPED_CONVENTION.equals(value) && !BADGER_FISH_CONVENTION.equals(value)) {
            throw new IllegalArgumentException("Unsupported convention \"" + value);
        }
        convention = value;
		super.setConvention(value);
    }
    
	@Override
    public void setConvertTypesToStrings(boolean convert) {
        if (convert) {
            this.setTypeConverter(new SimpleConverter());
        }
		super.setConvertTypesToStrings(convert);
    }
    
	@Override
    public void setTypeConverter(TypeConverter converter) {
        this.typeConverter = converter;
		super.setTypeConverter(converter);
    }
    
	@Override
    public void setIgnoreNamespaces(boolean ignoreNamespaces) {
        this.ignoreNamespaces = ignoreNamespaces;
		super.setIgnoreNamespaces(ignoreNamespaces);
    }
    
	@Override
    @Context
    public void setMessageContext(MessageContext mc) {
        super.setContext(mc);
    }
    
	@Override
    public void setDropRootElement(boolean drop) {
        this.dropRootElement = drop;
		super.setDropRootElement(drop);
    }
    
	@Override
    public void setIgnoreMixedContent(boolean ignore) {
        this.ignoreMixedContent = ignore;
		super.setIgnoreMixedContent(ignore);
    }
    
	@Override
    public void setNamespaceMap(Map<String, String> namespaceMap) {
        this.namespaceMap.putAll(namespaceMap);
		super.setNamespaceMap(namespaceMap);
    }
	
	@Override
    protected XMLStreamWriter createWriter(Object actualObject, Class<?> actualClass, 
        Type genericType, String enc, OutputStream os, boolean isCollection) throws Exception {
        if (BADGER_FISH_CONVENTION.equals(convention)) {
            return JSONUtils.createBadgerFishWriter(os);
        }
        boolean dropElementsInXmlStreamProp = getBooleanJsonProperty(DROP_ELEMENT_IN_XML_PROPERTY, 
                                                                     dropElementsInXmlStream);
        boolean dropRootNeeded = getBooleanJsonProperty(DROP_ROOT_CONTEXT_PROPERTY, dropRootElement);
        boolean dropRootInXmlNeeded = dropRootNeeded && dropElementsInXmlStreamProp;
        QName qname = actualClass == Document.class 
            ? org.apache.cxf.helpers.DOMUtils.getElementQName(((Document)actualObject).getDocumentElement()) 
            : this.getQName(actualClass, genericType, actualObject);
        if (qname != null && ignoreNamespaces && (isCollection || dropRootInXmlNeeded)) {        
            qname = new QName(qname.getLocalPart());
        }
        Configuration config = 
            JSONUtils.createConfiguration(namespaceMap, 
                                          writeXsiType && !ignoreNamespaces,
                                          attributesToElements,
                                          typeConverter);
        if (!dropElementsInXmlStreamProp && super.outDropElements != null) {
            config.setIgnoredElements(outDropElements);
        }
        if (!writeNullAsString) {
            config.setWriteNullAsString(writeNullAsString);
        }
        boolean ignoreEmpty = getBooleanJsonProperty(IGNORE_EMPTY_JSON_ARRAY_VALUES_PROPERTY, ignoreEmptyArrayValues);
        if (ignoreEmpty) {
            config.setIgnoreEmptyArrayValues(ignoreEmpty);
        }
        boolean dropRootInJsonStream = dropRootNeeded && !dropElementsInXmlStreamProp;
        if (dropRootInJsonStream) {
            config.setDropRootElement(true);
        }
        List<String> theArrayKeys = getArrayKeys();
        boolean rootIsArray = isRootArray(theArrayKeys);
        if (ignoreNamespaces && rootIsArray && (theArrayKeys == null || dropRootInJsonStream)) {
            if (theArrayKeys == null) {
                theArrayKeys = new LinkedList<String>();
            } else if (dropRootInJsonStream) {
                theArrayKeys = new LinkedList<String>(theArrayKeys);
            }
            if (qname != null) {
                theArrayKeys.add(qname.getLocalPart());
            }
        }
        XMLStreamWriter writer = ApsJSONUtils.createStreamWriter(os, qname, 
             writeXsiType && !ignoreNamespaces, config, rootIsArray, theArrayKeys,
             isCollection || dropRootInXmlNeeded, enc);
        writer = ApsJSONUtils.createIgnoreMixedContentWriterIfNeeded(writer, ignoreMixedContent);
        writer = ApsJSONUtils.createIgnoreNsWriterIfNeeded(writer, ignoreNamespaces);
        return createTransformWriterIfNeeded(writer, os, dropElementsInXmlStreamProp);
    }
    
    private QName getQName(Class<?> cls, Type type, Object object) 
        throws Exception {
        QName qname = getJaxbQName(cls, type, object, false);
        if (qname != null) {
            String prefix = getPrefix(qname.getNamespaceURI());
            return new QName(qname.getNamespaceURI(), qname.getLocalPart(), prefix);
        }
        return null;
    }
    
    private String getPrefix(String namespace) {
        String prefix = namespaceMap.get(namespace);
        return prefix == null ? "" : prefix;
    }
    
    public void setWriteXsiType(boolean writeXsiType) {
        this.writeXsiType = writeXsiType;
		super.setWriteXsiType(writeXsiType);
    }
    
	@Override
    public void setDropElementsInXmlStream(boolean drop) {
        this.dropElementsInXmlStream = drop;
		super.setDropElementsInXmlStream(drop);
    }
	
	@Override
    public void setWriteNullAsString(boolean writeNullAsString) {
        this.writeNullAsString = writeNullAsString;
		super.setWriteNullAsString(writeNullAsString);
    }
	
	@Override
    public void setIgnoreEmptyArrayValues(boolean ignoreEmptyArrayElements) {
        this.ignoreEmptyArrayValues = ignoreEmptyArrayElements;
		super.setIgnoreEmptyArrayValues(ignoreEmptyArrayElements);
    }
	
}