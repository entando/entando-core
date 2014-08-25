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
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.staxutils.DelegatingXMLStreamWriter;
import org.codehaus.jettison.AbstractXMLStreamWriter;
import org.codehaus.jettison.mapped.Configuration;
import org.codehaus.jettison.mapped.MappedNamespaceConvention;
import org.entando.entando.aps.system.services.api.model.CDataAdapter;

/**
 * @author E.Santoboni
 */
public class ApsJSONUtils {

	private static final Charset UTF8 = Charset.forName("utf-8");
	
	public static XMLStreamWriter createIgnoreMixedContentWriterIfNeeded(XMLStreamWriter writer, boolean ignoreMixedContent) {
        return ignoreMixedContent ? new IgnoreMixedContentWriter(writer) : writer; 
    }
	
	public static XMLStreamWriter createIgnoreNsWriterIfNeeded(XMLStreamWriter writer, boolean ignoreNamespaces) {
        return ignoreNamespaces ? new CDataIgnoreNamespacesWriter(writer) : writer; 
    }
	
	public static XMLStreamWriter createStreamWriter(OutputStream os, 
			QName qname, boolean writeXsiType, Configuration config, 
			boolean serializeAsArray, List<String> arrayKeys, boolean dropRootElement, String enc) throws Exception {
        MappedNamespaceConvention convention = new MappedNamespaceConvention(config);
        AbstractXMLStreamWriter xsw = new CDataMappedXMLStreamWriter(convention, new OutputStreamWriter(os, enc));
        if (serializeAsArray) {
            if (arrayKeys != null) {
                for (String key : arrayKeys) {
                    xsw.serializeAsArray(key);
                }
            } else if (qname != null) {
                String key = convention.createKey(qname.getPrefix(), 
						qname.getNamespaceURI(), qname.getLocalPart());
                xsw.serializeAsArray(key);
            }
        }
        XMLStreamWriter writer = !writeXsiType || dropRootElement 
            ? new IgnoreContentJettisonWriter(xsw, writeXsiType, dropRootElement) : xsw;
        return writer;
    }    
	
	private static class IgnoreMixedContentWriter extends DelegatingXMLStreamWriter {
        
		String lastText;
        boolean isMixed;
        List<Boolean> mixed = new LinkedList<Boolean>();
        
		public IgnoreMixedContentWriter(XMLStreamWriter writer) {
            super(writer);
        }
		
		@Override
        public void writeCharacters(String text) throws XMLStreamException {
            if (CDataAdapter.isCdata(new String(text))) {
                text = CDataAdapter.parse(new String(text));
            }
			if (StringUtils.isEmpty(text.trim())) {
                lastText = text; 
            } else if (lastText != null) {
                lastText += text;
            } else if (!isMixed) {
                super.writeCharacters(text);                                
            } else {
                lastText = text;
            }
        }
        
		@Override
        public void writeStartElement(String prefix, String local, String uri) throws XMLStreamException {
            if (lastText != null) {
                isMixed = true;
            }
            mixed.add(0, isMixed);
            lastText = null;
            isMixed = false;
            super.writeStartElement(prefix, local, uri);
        }
		
		@Override
        public void writeStartElement(String uri, String local) throws XMLStreamException {
            if (lastText != null) {
                isMixed = true;
            }
            mixed.add(0, isMixed);
            lastText = null;
            isMixed = false;
            super.writeStartElement(uri, local);
        }
		
		@Override
        public void writeStartElement(String local) throws XMLStreamException {
            if (lastText != null) {
                isMixed = true;
            }
            mixed.add(0, isMixed);
            lastText = null;
            isMixed = false;
            super.writeStartElement(local);
        }
		
		@Override
        public void writeEndElement() throws XMLStreamException {
            if (lastText != null && (!isMixed || !StringUtils.isEmpty(lastText.trim()))) {
                super.writeCharacters(lastText.trim());                
            }
            super.writeEndElement();
            isMixed = mixed.get(0);
            mixed.remove(0);
        }
		
    }
	
	private static class IgnoreContentJettisonWriter extends DelegatingXMLStreamWriter {
        
        private boolean writeXsiType;
        private boolean dropRootElement;
        private boolean rootDropped;
        private int index;
		
        public IgnoreContentJettisonWriter(XMLStreamWriter writer, boolean writeXsiType, boolean dropRootElement) {
            super(writer);
            this.writeXsiType = writeXsiType;
            this.dropRootElement = dropRootElement;
        }
        
		@Override
        public void writeAttribute(String prefix, String uri, 
				String local, String value) throws XMLStreamException {
            if (!writeXsiType && "xsi".equals(prefix)
                    && ("type".equals(local) || "nil".equals(local))) {
                return;
            }
            super.writeAttribute(prefix, uri, local, value);
        }
        
        @Override
        public void writeStartElement(String prefix, String local, String uri) throws XMLStreamException {
            index++;
            if (dropRootElement && index - 1 == 0) {
                rootDropped = true;
                return;
            }
            super.writeStartElement(prefix, local, uri);
        }
        
        @Override
        public void writeStartElement(String local) throws XMLStreamException {
            this.writeStartElement("", local, "");
        }
        
        @Override
        public void writeEndElement() throws XMLStreamException {
            index--;
            if (rootDropped && index == 0) {
                return;
            }
            super.writeEndElement();
        }
		
		@Override
		public void writeCharacters(String text) throws XMLStreamException {
            if (CDataAdapter.isCdata(new String(text))) {
                String parsedCDataText = CDataAdapter.parse(new String(text));
                super.writeCharacters(parsedCDataText);
            } else {
                super.writeCharacters(text);
            }
        }
		
    }
	
}
