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

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.cxf.staxutils.transform.IgnoreNamespacesWriter;
import org.entando.entando.aps.system.services.api.model.CDataAdapter;

/**
 * @author E.Santoboni
 */
public class CDataIgnoreNamespacesWriter extends IgnoreNamespacesWriter {

    public CDataIgnoreNamespacesWriter(XMLStreamWriter writer) {
        super(writer);
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