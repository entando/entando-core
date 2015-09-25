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
