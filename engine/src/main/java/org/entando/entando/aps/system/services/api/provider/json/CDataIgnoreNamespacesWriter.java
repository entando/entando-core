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

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.cxf.staxutils.transform.IgnoreNamespacesWriter;
import org.entando.entando.aps.system.services.api.model.CDataAdapter;

/**
 * @author E.Santoboni
 */
public class CDataIgnoreNamespacesWriter extends IgnoreNamespacesWriter {
	
    public CDataIgnoreNamespacesWriter(XMLStreamWriter writer, boolean ignoreNamespaces) {
        super(writer, ignoreNamespaces);
    }
    
	@Override
    public void writeCharacters(String text) throws XMLStreamException {
        if (CDataAdapter.isCdata(text)) {
            String parsedCDataText = CDataAdapter.parse(text);
            super.writeCharacters(parsedCDataText);
        } else {
            super.writeCharacters(text);
        }
    }
	
}