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
package com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute;

import com.agiletec.plugins.jacms.aps.system.services.resource.model.AttachResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;

/**
 * Rappresenta un attributo di entità di tipo attachment. L'attachment 
 * e il testo associato possono essere diversi per ciascun lingua.
 * @author M.Diana - S.Didaci - E.Santoboni
 */
public class AttachAttribute extends AbstractResourceAttribute {
    
	/**
	 * Restituisce il path URL dell'attachment.
	 * @return Il path dell'attachment.
	 */
	public String getAttachPath() {
	    String attachPath = "";
	    ResourceInterface res = this.getResource();
		if (null != res) {
	    	attachPath = ((AttachResource) res).getAttachPath();
	    	attachPath = this.appendContentReference(attachPath);
		}
	    return attachPath;
	}
	
	@Override
	protected String getDefaultPath() {
		return this.getAttachPath();
	}
	
}