/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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