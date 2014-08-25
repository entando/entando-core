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

import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;

/**
 * Rappresenta un attributo di entità di tipo immagine. L'immagine e 
 * il testo associato possono essere diversi per ciascun lingua.
 * @author W.Ambu - E.Santoboni
 */
public class ImageAttribute extends AbstractResourceAttribute {
	
	/**
	 * Restituisce il path URL dell'immagine 
	 * relativo all'istanza specificata dal size.
	 * @param size L'istanza della risorsa immagine.
	 * @return Il path dell'immagine.
	 * @deprecated use getImagePath
	 */
	public String imagePath(String size) {
		return this.getImagePath(size);
	}
	
	/**
	 * Restituisce il path URL dell'immagine 
	 * relativo all'istanza specificata dal size.
	 * @param size L'istanza della risorsa immagine.
	 * @return Il path dell'immagine.
	 */
	public String getImagePath(String size) {
		String imagePath = "";
		ResourceInterface res = this.getResource();
		if (null != res) {
			imagePath = ((ImageResource) res).getImagePath(size);
			imagePath = this.appendContentReference(imagePath);
		}
		return imagePath;
	}
	
	@Override
	protected String getDefaultPath() {
		return this.getImagePath("0");
	}
	
}