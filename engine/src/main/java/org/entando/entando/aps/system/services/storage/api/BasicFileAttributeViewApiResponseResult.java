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
package org.entando.entando.aps.system.services.storage.api;

import javax.xml.bind.annotation.XmlElement;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;

public class BasicFileAttributeViewApiResponseResult extends AbstractApiResponseResult {
	
	@Override
	@XmlElement(name = "basicFileAttributeView", required = false)
	public JAXBBasicFileAttributeView getResult() {
		return (JAXBBasicFileAttributeView) this.getMainResult();
	}

}