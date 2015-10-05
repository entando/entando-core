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
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.provider.json.utils.JSONUtils;
import org.apache.cxf.jaxrs.utils.HttpUtils;
import org.apache.cxf.jaxrs.utils.InjectionUtils;
import org.apache.cxf.jaxrs.utils.JAXBUtils;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.staxutils.StaxUtils;
import org.codehaus.jettison.mapped.Configuration;
import org.codehaus.jettison.mapped.SimpleConverter;
import org.codehaus.jettison.mapped.TypeConverter;
import org.codehaus.jettison.util.StringIndenter;
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
    static {
        new SimpleConverter();
    }
    
    private ConcurrentHashMap<String, String> namespaceMap = new ConcurrentHashMap<String, String>();
    private boolean serializeAsArray;
    private List<String> arrayKeys;
    private boolean dropCollectionWrapperElement;
    private boolean ignoreMixedContent; 
    private boolean writeXsiType = true;
    private boolean ignoreNamespaces;
    private String convention = MAPPED_CONVENTION;
    private TypeConverter typeConverter;
    private boolean attributesToElements;
    
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
    public void setDropCollectionWrapperElement(boolean drop) {
		super.setDropCollectionWrapperElement(drop);
        this.dropCollectionWrapperElement = drop;
    }
    
    @Override
    public void setIgnoreMixedContent(boolean ignore) {
		super.setIgnoreMixedContent(ignore);
        this.ignoreMixedContent = ignore;
    }
    
	@Override
    public void setSerializeAsArray(boolean asArray) {
		super.setSerializeAsArray(asArray);
        this.serializeAsArray = asArray;
    }
    
    @Override
    public void setArrayKeys(List<String> keys) {
		super.setArrayKeys(keys);
        this.arrayKeys = keys;
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
                writer = this.createNewWriter(obj, cls, genericType, enc, os, false);
                copyReaderToWriter(StaxUtils.createXMLStreamReader((Document)obj), writer);
                return;
            }
            if (InjectionUtils.isSupportedCollectionOrArray(cls)) {
                executeMarshalCollection(cls, obj, genericType, enc, os, m, anns);
            } else {
                Object actualObject = checkAdapter(obj, cls, anns, true);
                Class<?> actualClass = obj != actualObject || cls.isInterface() 
                    ? actualObject.getClass() : cls;
                if (cls == genericType) {
                    genericType = actualClass;
                }
                this.executeMarshal(actualObject, actualClass, genericType, enc, os);
            }
        } catch (JAXBException e) {
            handleJAXBException(e, false);
        } catch (XMLStreamException e) {
            handleXMLStreamException(e, false);
        } catch (Exception e) {
            throw new InternalServerErrorException(e);
        } finally {
            StaxUtils.close(writer);
        }
    }
	
    protected void executeMarshalCollection(Class<?> originalCls, Object collection, 
			Type genericType, String encoding, OutputStream os, MediaType m, Annotation[] anns) throws Exception {
        Class<?> actualClass = InjectionUtils.getActualType(genericType);
        actualClass = getActualType(actualClass, genericType, anns);
        Collection<?> c = originalCls.isArray() ? Arrays.asList((Object[]) collection) 
                                             : (Collection<?>) collection;
        Iterator<?> it = c.iterator();
        Object firstObj = it.hasNext() ? it.next() : null;
        String startTag;
        String endTag;
        if (!dropCollectionWrapperElement) {
            QName qname;
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
            this.executeMarshalCollectionMember(JAXBUtils.useAdapter(firstObj, adapter, true),
                                    actualClass, genericType, encoding, os);
            while (it.hasNext()) {
                os.write(",".getBytes());
                this.executeMarshalCollectionMember(JAXBUtils.useAdapter(it.next(), adapter, true), 
                                        actualClass, genericType, encoding, os);
            }
        }
        os.write(endTag.getBytes());
    }
    
    protected void executeMarshalCollectionMember(Object obj, Class<?> cls, 
			Type genericType, String enc, OutputStream os) throws Exception {
        if (obj instanceof JAXBElement) {
            obj = ((JAXBElement<?>) obj).getValue();    
        } else {
            obj = convertToJaxbElementIfNeeded(obj, cls, genericType);
        }
        if (obj instanceof JAXBElement && cls != JAXBElement.class) {
            cls = JAXBElement.class;
        }
        Marshaller ms = createMarshaller(obj, cls, genericType, enc);
        this.executeMarshal(ms, obj, cls, genericType, enc, os, true);
    }
    
    protected void executeMarshal(Marshaller ms, Object actualObject, Class<?> actualClass, 
                  Type genericType, String enc, OutputStream os, boolean isCollection) throws Exception {
        OutputStream actualOs = os; 
        MessageContext mc = getContext();
        if (mc != null && MessageUtils.isTrue(mc.get(Marshaller.JAXB_FORMATTED_OUTPUT))) {
            actualOs = new CachedOutputStream();    
        }
        XMLStreamWriter writer = 
				this.createNewWriter(actualObject, actualClass, 
				genericType, enc, actualOs, isCollection);
        ms.marshal(actualObject, writer);
        writer.close();
        if (os != actualOs) {
            StringIndenter formatter = new StringIndenter(
                IOUtils.newStringFromBytes(((CachedOutputStream)actualOs).getBytes()));
            Writer outWriter = new OutputStreamWriter(os, enc);
            IOUtils.copy(new StringReader(formatter.result()), outWriter, 2048);
            outWriter.close();
        }
    }
    
    protected XMLStreamWriter createNewWriter(Object actualObject, Class<?> actualClass, 
        Type genericType, String enc, OutputStream os, boolean isCollection) throws Exception {
        if (BADGER_FISH_CONVENTION.equals(convention)) {
            return JSONUtils.createBadgerFishWriter(os);
        }
        boolean dropRootNeeded = isDropRootNeeded();
        QName qname = actualClass == Document.class ? null : getQName(actualClass, genericType, actualObject);
        if (qname != null && ignoreNamespaces && (isCollection  || dropRootNeeded)) {        
            qname = new QName(qname.getLocalPart());
        }
        Configuration config = JSONUtils.createConfiguration(namespaceMap, 
				writeXsiType && !ignoreNamespaces, attributesToElements, typeConverter);
        XMLStreamWriter writer = ApsJSONUtils.createStreamWriter(os, qname, 
             writeXsiType && !ignoreNamespaces, config, serializeAsArray, arrayKeys,
             isCollection || dropRootNeeded);
        writer = ApsJSONUtils.createIgnoreMixedContentWriterIfNeeded(writer, ignoreMixedContent);
        writer = ApsJSONUtils.createIgnoreNsWriterIfNeeded(writer, ignoreNamespaces);
        return createTransformWriterIfNeeded(writer, os);
    }
	
	@Override
    protected XMLStreamWriter createTransformWriterIfNeeded(XMLStreamWriter writer,
                                                            OutputStream os) {
		return createTransformWriterIfNeeded(writer, os,
                outElementsMap, outDropElements, outAppendMap, attributesToElements, null);
    }
	
	public static XMLStreamWriter createNewWriterIfNeeded(XMLStreamWriter writer, OutputStream os) {
        return writer == null ? StaxUtils.createXMLStreamWriter(os) : writer;
    }
	
	public static XMLStreamWriter createTransformWriterIfNeeded(
            XMLStreamWriter writer, OutputStream os,
            Map<String, String> outElementsMap, List<String> outDropElements,
            Map<String, String> outAppendMap, boolean attributesToElements, String defaultNamespace) {
        if (outElementsMap != null || outDropElements != null
                || outAppendMap != null || attributesToElements) {
            writer = createNewWriterIfNeeded(writer, os);
            writer = new CDataOutTransformWriter(writer, outElementsMap,
                    outAppendMap, outDropElements, attributesToElements, defaultNamespace);
        }
        return writer;
    }
	
    protected void executeMarshal(Object actualObject, Class<?> actualClass, 
                           Type genericType, String enc, OutputStream os) throws Exception {
        actualObject = convertToJaxbElementIfNeeded(actualObject, actualClass, genericType);
        if (actualObject instanceof JAXBElement && actualClass != JAXBElement.class) {
            actualClass = JAXBElement.class;
        }
        Marshaller ms = createMarshaller(actualObject, actualClass, genericType, enc);
        if (!namespaceMap.isEmpty()) {
            setNamespaceMapper(ms, namespaceMap);
        }
        this.executeMarshal(ms, actualObject, actualClass, genericType, enc, os, false);
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
    
}