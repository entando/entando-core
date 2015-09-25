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
package com.agiletec.aps.system.services.page.widget;

import java.util.List;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * Classe di supporto per l'interpretazione delle espressioni che specificano sottoinsiemi
 * di pagine. Utilizzata dal tag per la generazione di navigatori; il risultato 
 * dell'interpretazione è una lista di oggetti NavigatorTarget, che wrappano
 * pagine del portale e possono essere utilizzati dai sub-tag. Le espressioni per
 * la specificazione delle pagine da selezionare possono essere assolute o relative o miste,
 * in riferimento alla pagina visualizzata (pagina corrente) e sono definite in base
 * alla struttura ad albero delle pagine del portale (ogni pagina è un nodo).<BR>
 * Le <b>espressioni</b> supportate seguono la seguente sintassi:<br>
 * <code>expr1+expr2+ ... +exprn</code><br>
 * dove ogni espressione rappresenta un insieme di pagine e il segno + è l'operatore
 * di unione di insiemi.<br>
 * Ogni espressione in forma di stringa ha la forma:<br>
 * <code>page_spec</code><br>
 * oppure:<br>
 * <code>page_spec.operator</code><br>
 * dove <code>page_spec</code> è una funzione di selezione di una singola pagina e
 * <code>.operator</code> è un operatore che seleziona un sottoinsieme di pagine correlate alla
 * pagina cui è applicato.<br><br>
 * Solo nel caso dell'operatore "Subtree" gli oggetti NavigatorTarget restituiti
 * hanno associato un valore significativo del livello nel sottoalbero; in tutti gli altri
 * casi il livello ha valore zero.
 * @author M.Diana
 */
public interface INavigatorParser {
	
	/**
	 * Crea e restituisce una lista di oggetti NavigatorTarget, che wrappano 
	 * pagine del portale e possono essere utilizzati dai sub-tag.
	 * @param spec L'espressione usata la specificazione delle pagine 
	 * da selezionare; possono essere assolute o relative o miste.
	 * @param reqCtx Il contesto della richiesta corrente.
	 * @return La lista di oggetti NavigatorTarget.
	 */
	public List<NavigatorTarget> parseSpec(String spec, RequestContext reqCtx);
	
	/**
	 * Crea e restituisce una lista di oggetti NavigatorTarget, che wrappano 
	 * pagine del portale e possono essere utilizzati dai sub-tag.
	 * @param spec L'espressione usata la specificazione delle pagine 
	 * da selezionare; possono essere assolute o relative o miste.
	 * @param currentPage La pagina corrente dove il tag è inserito.
	 * @param currentUser L'utente corrente.
	 * @return La lista di oggetti NavigatorTarget.
	 */
	public List<NavigatorTarget> parseSpec(String spec, IPage currentPage, UserDetails currentUser);
	
	public String getSpec(List<NavigatorExpression> expressions);
	
	public List<NavigatorExpression> getExpressions(String spec);
	
	public static final String EXPRESSION_SEPARATOR = "+";
	
}