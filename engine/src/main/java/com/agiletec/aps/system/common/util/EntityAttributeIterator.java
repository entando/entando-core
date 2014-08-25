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
package com.agiletec.aps.system.common.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractComplexAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;

/**
 * Un iteratore su tutti gli attributi elementari di un'entità. 
 * Gli attributi di tipo complesso non vengono restituiti 
 * da questo iteratore; vengono restituiti invece gli attributi elementari 
 * presenti all'interno dei singoli attributi complessi.
 * E' una classe helper da utilizzare quando si devono eseguire operazioni
 * su tutti gli attributi elementari di un'entità.
 * L'entità è passata come parametro nel costruttore.
 * Il metodo remove non è supportato.
 * @version 1.0
 * @author M.Diana - E.Santoboni
 */
public class EntityAttributeIterator implements Iterator {
	
	/**
	 * Inizializza l'iteratore in base all'entità.
	 * @param entity L'entità dal quale estrarre 
	 * tutti gli attributi elementari.
	 */
	public EntityAttributeIterator(IApsEntity entity){
		this._attributes = new ArrayList<AttributeInterface>();
		List<AttributeInterface> entityAttributes = entity.getAttributeList();
		for (int i=0; i<entityAttributes.size(); i++) {
			this.addAttribute(entityAttributes.get(i));
		}
		this._attributesIterator = this._attributes.iterator(); 
	}
	
	private void addAttribute(AttributeInterface attribute) {
		if (attribute.isSimple()) {
			this._attributes.add(attribute);
		} else {
			List<AttributeInterface> attributes = ((AbstractComplexAttribute) attribute).getAttributes();
			for (int i=0; i<attributes.size(); i++) {
				this.addAttribute(attributes.get(i));
			}
		}
	}
	
	/**
	 * Metodo dell'interfaccia Iterator, non utilizzabile. Lancia un'eccezione 
	 * UnsupportedOperationException.
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException("Operazione non consentita");		
	}

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return _attributesIterator.hasNext();
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public Object next() {
		return _attributesIterator.next();
	}
	
	private Iterator<AttributeInterface> _attributesIterator;
	private List<AttributeInterface> _attributes;
}
