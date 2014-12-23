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
package com.agiletec.aps.system.services.controller.control;

import java.io.Serializable;

import org.springframework.beans.factory.InitializingBean;

import com.agiletec.aps.system.RequestContext;

/**
 * Interfaccia per i sottoservizi di controllo del controller (Controller).
 * I sottoservizi contengono le fasi operative per esaudire la richiesta del
 * client, e possono eseguire operazioni di autorizzazione, di verifica,
 * di preparazione, di generazione dell'output, di gestione e segnalazione
 * errori. Per ulteriori informazioni vedere la documentazione del controller.
 * I sottoservizi possono fornire informazioni ad altri sottoservizi o
 * alle classi di livello superiore inserendole nel contesto di richiesta
 * (RequestContext). Le classi implementanti devono esporre i soli 
 * metodi previsti nell'interfaccia.
 * @author M.Diana - E.Santoboni
 */
public interface ControlServiceInterface extends InitializingBean, Serializable {
	
	/**
	 * Esegue le operazioni specifiche del sottoservizio.
	 * @param reqCtx Il contesto di richiesta
	 * @param status Lo stato di uscita del sottoservizio precedente
	 * @return Lo stato di uscita, una delle costanti di Controller
	 */
	public int service(RequestContext reqCtx, int status);

}
