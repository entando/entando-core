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
package org.entando.entando.aps.system.services.api.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Frédéric Barmes - E.Santoboni
 */
public class CDataXmlTypeAdapter extends XmlAdapter<String, String> {
    
	@Override
	public String unmarshal(String value) {
        return (CDataAdapter.parse(value));
    }
	
	@Override
    public String marshal(String value) {
        return (CDataAdapter.print(value));
    }
	
}