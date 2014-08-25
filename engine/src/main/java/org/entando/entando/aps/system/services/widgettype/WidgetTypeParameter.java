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
package org.entando.entando.aps.system.services.widgettype;

import java.io.Serializable;

/**
 * Rappresenta un parametro di configurazione del widget.
 * @author E.Santoboni
 */
public class WidgetTypeParameter implements Serializable {
	
	@Override
	public WidgetTypeParameter clone() {
		WidgetTypeParameter clone = new WidgetTypeParameter();
		clone.setDescr(this.getDescr());
		clone.setName(this.getName());
		return clone;
	}
	
	/**
	 * Restituisce la descrizione del parametro.
	 * @return La descrizione del parametro.
	 */
	public String getDescr() {
		return _descr;
	}

	/**
	 * Setta la descrizione del parametro.
	 * @param descr La descrizione del parametro.
	 */
	public void setDescr(String descr) {
		this._descr = descr;
	}

	/**
	 * Restituisce il nome del parametro.
	 * @return Returns the name.
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Setta il nome del parametro.
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this._name = name;
	}
	
	private String _name;
	private String _descr;
	
}
