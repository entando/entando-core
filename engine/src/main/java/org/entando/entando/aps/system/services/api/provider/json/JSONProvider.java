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

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.provider.json.utils.JSONUtils;
import org.apache.cxf.jaxrs.utils.ExceptionUtils;
import org.apache.cxf.jaxrs.utils.HttpUtils;
import org.apache.cxf.jaxrs.utils.InjectionUtils;
import org.apache.cxf.staxutils.StaxUtils;
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
	private String namespaceSeparator;
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
    private boolean escapeForwardSlashesAlways;
    
    @Override
    public void setAttributesToElements(boolean value) {
		super.setAttributesToElements(value);
        this.attributesToElements = value;
    }
    
	@Override
    public void setConvention(String value) {
		super.setConvention(value);
        if (!MAPPED_CONVENTION.equals(value) && !BADGER_FISH_CONVENTION.equals(value)) {
            throw new IllegalArgumentException("Unsupported convention \"" + value);
        }
        convention = value;
    }
    
    @Override
    public void setConvertTypesToStrings(boolean convert) {
		super.setConvertTypesToStrings(convert);
        if (convert) {
            this.setTypeConverter(new SimpleConverter());
        }
    }
    
    @Override
    public void setTypeConverter(TypeConverter converter) {
		super.setTypeConverter(converter);
        this.typeConverter = converter;
    }
    
    @Override
    public void setIgnoreNamespaces(boolean ignoreNamespaces) {
		super.setIgnoreNamespaces(ignoreNamespaces);
        this.ignoreNamespaces = ignoreNamespaces;
    }
    
    @Context
    @Override
    public void setMessageContext(MessageContext mc) {
		super.setMessageContext(mc);
        super.setContext(mc);
    }
	
	@Override
    public void setDropRootElement(boolean drop) {
        super.setDropRootElement(drop);
        this.dropRootElement = drop;
    }
    
    @Override
    public void setIgnoreMixedContent(boolean ignore) {
		super.setIgnoreMixedContent(ignore);
        this.ignoreMixedContent = ignore;
    }
    
    @Override
    public void setNamespaceMap(Map<String, String> namespaceMap) {
		super.setNamespaceMap(namespaceMap);
        this.namespaceMap.putAll(namespaceMap);
    }
	
	@Override
    public void writeTo(T obj, Class<?> cls, Type genericType, Annotation[] anns,  
        MediaType m, MultivaluedMap<String, Object> headers, OutputStream os)
        throws IOException {
        if (os == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Jettison needs initialized OutputStream");
            if (getContext() != null && getContext().getContent(XMLStreamWriter.class) == null) {
                sb.append("; if you need to customize Jettison output with the custom XMLStreamWriter"
                          + " then extend JSONProvider or when possible configure it directly.");
            }
            throw new IOException(sb.toString());
        }
        XMLStreamWriter writer = null;
        try {
            String enc = HttpUtils.getSetEncoding(m, headers, "UTF-8");
            if (Document.class.isAssignableFrom(cls)) {
                writer = this.createWriter(obj, cls, genericType, enc, os, false);
                copyReaderToWriter(StaxUtils.createXMLStreamReader((Document)obj), writer);
                return;
            }
            if (InjectionUtils.isSupportedCollectionOrArray(cls)) {
                this.marshalCollection(cls, obj, genericType, enc, os, m, anns);
            } else {
                Object actualObject = checkAdapter(obj, cls, anns, true);
                Class<?> actualClass = obj != actualObject || cls.isInterface() 
                    ? actualObject.getClass() : cls;
                if (cls == genericType) {
                    genericType = actualClass;
                }
                this.marshal(actualObject, actualClass, genericType, enc, os);
            }
        } catch (JAXBException e) {
            handleJAXBException(e, false);
        } catch (XMLStreamException e) {
            handleXMLStreamException(e, false);
        } catch (Exception e) {
            throw ExceptionUtils.toInternalServerErrorException(e, null);
        } finally {
            StaxUtils.close(writer);
        }
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
		super.setWriteXsiType(writeXsiType);
        this.writeXsiType = writeXsiType;
    }
	
	@Override
    public void setDropElementsInXmlStream(boolean drop) {
		super.setDropElementsInXmlStream(drop);
        this.dropElementsInXmlStream = drop;
    }
	
	@Override
    public void setWriteNullAsString(boolean writeNullAsString) {
        super.setWriteNullAsString(writeNullAsString);
		this.writeNullAsString = writeNullAsString;
    }
	
	@Override
	public void setIgnoreEmptyArrayValues(boolean ignoreEmptyArrayElements) {
		super.setIgnoreEmptyArrayValues(ignoreEmptyArrayElements);
        this.ignoreEmptyArrayValues = ignoreEmptyArrayElements;
    }
	
	@Override
	public void setEscapeForwardSlashesAlways(boolean escape) {
        super.setEscapeForwardSlashesAlways(escape);
        this.escapeForwardSlashesAlways = escape;
    }
	
	@Override
	public void setNamespaceSeparator(String namespaceSeparator) {
        super.setNamespaceSeparator(namespaceSeparator);
        this.namespaceSeparator = namespaceSeparator;
    }
	
}