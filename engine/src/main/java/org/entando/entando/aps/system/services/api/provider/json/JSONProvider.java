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
package org.entando.entando.aps.system.services.api.provider.json;

import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamWriter;
import org.apache.cxf.helpers.CastUtils;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.provider.json.utils.JSONUtils;
import org.apache.cxf.jaxrs.utils.InjectionUtils;
import org.apache.cxf.jaxrs.utils.JAXBUtils;
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
    private static final String ARRAY_KEYS_PROPERTY = "json.array.keys";
    private static final String ROOT_IS_ARRAY_PROPERTY = "json.root.is.array";
    private static final String DROP_ELEMENT_IN_XML_PROPERTY = "drop.xml.elements";
    private static final String IGNORE_EMPTY_JSON_ARRAY_VALUES_PROPERTY = "ignore.empty.json.array.values";
    static {
        new SimpleConverter();
    }
    
    private ConcurrentHashMap<String, String> namespaceMap = new ConcurrentHashMap<String, String>();
    private boolean serializeAsArray;
    private List<String> arrayKeys;
    private String namespaceSeparator;
    private boolean dropRootElement;
    private boolean dropElementsInXmlStream = true;
	private boolean dropCollectionWrapperElement;
    private boolean ignoreMixedContent;
    private boolean ignoreEmptyArrayValues;
    private boolean writeXsiType = true;
    private boolean ignoreNamespaces;
    private String convention = MAPPED_CONVENTION;
    private TypeConverter typeConverter;
    private boolean attributesToElements;
    private boolean writeNullAsString = true;
    private boolean escapeForwardSlashesAlways;
    
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
    public void setSerializeAsArray(boolean asArray) {
        this.serializeAsArray = asArray;
		super.setSerializeAsArray(asArray);
    }
    
	@Override
    public void setArrayKeys(List<String> keys) {
        this.arrayKeys = keys;
		super.setArrayKeys(keys);
    }
	
    @Override
    public void setNamespaceMap(Map<String, String> namespaceMap) {
        this.namespaceMap.putAll(namespaceMap);
		super.setNamespaceMap(namespaceMap);
    }
	
	@Override
	protected void marshalCollection(Class<?> originalCls, Object collection, 
                                     Type genericType, String encoding, 
                                     OutputStream os, MediaType m, Annotation[] anns) throws Exception {
        Class<?> actualClass = InjectionUtils.getActualType(genericType);
        actualClass = getActualType(actualClass, genericType, anns);
        Collection<?> c = originalCls.isArray() ? Arrays.asList((Object[]) collection) 
                                             : (Collection<?>) collection;
        Iterator<?> it = c.iterator();
        Object firstObj = it.hasNext() ? it.next() : null;
        String startTag = null;
        String endTag = null;
        if (!dropCollectionWrapperElement) {
            QName qname = null;
            if (firstObj instanceof JAXBElement) {
                JAXBElement<?> el = (JAXBElement<?>)firstObj;
                qname = el.getName();
                actualClass = el.getDeclaredType();
            } else {
                qname = getCollectionWrapperQName(actualClass, genericType, firstObj, false);
            }
            String prefix = "";
            if (!ignoreNamespaces) {
                prefix = namespaceMap.get(qname.getNamespaceURI());
                if (prefix != null) {
                    if (prefix.length() > 0) {
                        prefix += ".";
                    }
                } else if (qname.getNamespaceURI().length() > 0) {
                    prefix = "ns1.";
                }
            }
            prefix = (prefix == null) ? "" : prefix;
            startTag = "{\"" + prefix + qname.getLocalPart() + "\":[";
            endTag = "]}";
        } else if (serializeAsArray) {
            startTag = "[";
            endTag = "]";
        } else {
            startTag = "{";
            endTag = "}";
        }
        os.write(startTag.getBytes());
        if (firstObj != null) {
            XmlJavaTypeAdapter adapter = 
                org.apache.cxf.jaxrs.utils.JAXBUtils.getAdapter(firstObj.getClass(), anns);
            marshalCollectionMember(JAXBUtils.useAdapter(firstObj, adapter, true),
                                    actualClass, genericType, encoding, os);
            while (it.hasNext()) {
                os.write(",".getBytes());
                marshalCollectionMember(JAXBUtils.useAdapter(it.next(), adapter, true), 
                                        actualClass, genericType, encoding, os);
            }
        }
        os.write(endTag.getBytes());
    }
	
    @Override
    protected XMLStreamWriter createWriter(Object actualObject, Class<?> actualClass, 
        Type genericType, String enc, OutputStream os, boolean isCollection) throws Exception {
        if (BADGER_FISH_CONVENTION.equals(convention)) {
            return JSONUtils.createBadgerFishWriter(os, enc);
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
        if (namespaceSeparator != null) {
            config.setJsonNamespaceSeparator(namespaceSeparator);
        }
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
        if (escapeForwardSlashesAlways) {
            config.setEscapeForwardSlashAlways(escapeForwardSlashesAlways);
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
        writer = ApsJSONUtils.createIgnoreNsWriterIfNeeded(writer, ignoreNamespaces, !writeXsiType);
        return createTransformWriterIfNeeded(writer, os, dropElementsInXmlStreamProp);
    }
    
    protected List<String> getArrayKeys() {
        MessageContext mc = getContext();
        if (mc != null) {
            Object prop = mc.get(ARRAY_KEYS_PROPERTY);
            if (prop instanceof List) {
                return CastUtils.cast((List<?>)prop);
            }
        }
        return arrayKeys;
    }
    
    protected boolean isRootArray(List<String> theArrayKeys) {
        return theArrayKeys != null ? true : getBooleanJsonProperty(ROOT_IS_ARRAY_PROPERTY, serializeAsArray);
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
    
    @Override
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
    public void setDropCollectionWrapperElement(boolean drop) {
        this.dropCollectionWrapperElement = drop;
		super.setDropCollectionWrapperElement(drop);
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
	
    @Override
    public void setEscapeForwardSlashesAlways(boolean escape) {
        this.escapeForwardSlashesAlways = escape;
		super.setEscapeForwardSlashesAlways(escape);
    }
	
	
	@Override
    public void setNamespaceSeparator(String namespaceSeparator) {
        this.namespaceSeparator = namespaceSeparator;
		super.setNamespaceSeparator(namespaceSeparator);
    }
}