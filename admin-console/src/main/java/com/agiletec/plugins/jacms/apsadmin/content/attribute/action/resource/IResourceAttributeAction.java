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
package com.agiletec.plugins.jacms.apsadmin.content.attribute.action.resource;

/**
 * Interfaccia base per le classi action delegate alla gestione delle operazioni base sugli attributi risorsa.
 * Le azioni gestite rappresentano ciascuna un entry point dall'interfaccia di redazione contenuto. 
 * @author E.Santoboni
 */
public interface IResourceAttributeAction {
	
	/**
	 * Esegue l'operazione di richiesta scelta risorsa.
	 * @return Il codice del risultato dell'azione.
	 */
	public String chooseResource();
	
	/**
	 * Esegue l'operazione di richiesta rimozione risorsa.
	 * @return Il codice del risultato dell'azione.
	 */
	public String removeResource();
	
	/**
	 * Restituisce il nome dell'attributo tipo Risorsa.
	 * @return Il nome dell'attributo.
	 */
	public String getAttributeName();
	
	/**
	 * Restituisce il nome dell'attributo parent nel caso che l'attributo 
	 * in oggetto sia inserito all'interno di un'attributo tipo "Composito".
	 * @return Il nome dell'attributo parent.
	 */
	public String getParentAttributeName();
	
	/**
	 * Restituisce l'indice dell'attributo nel caso che l'attributo 
	 * in oggetto sia inserito all'interno di una lista di attributo tipo "List" o "Monolist".
	 * @return L'indice dell'attributo nel caso di lista di attributi.
	 */
	public int getElementIndex();
	
	/**
	 * Restituisce il codice della lingua tramite il quale associare la risorsa all'attributo.
	 * @return Il codice della lingua dell'attributo corrente.
	 */
	public String getResourceLangCode();
	
	/**
	 * Restituisce il codice del tipo di risorsa.
	 * @return Il codice del tipo di risorsa.
	 */
	public String getResourceTypeCode();
	
}