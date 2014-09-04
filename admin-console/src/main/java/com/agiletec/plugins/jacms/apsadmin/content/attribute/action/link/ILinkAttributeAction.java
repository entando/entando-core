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
package com.agiletec.plugins.jacms.apsadmin.content.attribute.action.link;

/**
 * Interfaccia base per le classi action delegate alla gestione delle operazioni base sugli attributi Link.
 * Le azioni gestite rappresentano ciascuna un entry point dall'interfaccia di redazione contenuto. 
 * @author E.Santoboni
 */
public interface ILinkAttributeAction {
	
	/**
	 * Esegue l'operazione di richiesta scelta link. 
	 * L'azione viene eseguita nell'interfaccia di redazione contenuti e 
	 * rappresenta l'entry point delle interfaccie di gestione link.
	 * @return Il codice del risultato dell'azione.
	 */
	public String chooseLink();
	
	/**
	 * Esegue l'operazione di richiesta scelta tipo link.
	 * @return Il codice del risultato dell'azione.
	 */
	public String chooseLinkType();
	
	/**
	 * Esegue l'operazione di richiesta rimozione link.
	 * @return Il codice del risultato dell'azione.
	 */
	public String removeLink();
	
	/**
	 * Restituisce il nome dell'attributo tipo Link.
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
	 * Restituisce il codice della lingua corrente.
	 * @return Il codice della lingua corrente.
	 */
	public String getLangCode();
	
}