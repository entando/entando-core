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
package com.agiletec.aps.system.services.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.services.controller.control.ControlServiceInterface;

/**
 * Il controller è il servizio di controllo dell'esecuzione relativa ad una 
 * richiesta del client. L'esecuzione è realizzata invocando in sequenza
 * i sottoservizi di controllo definiti in configurazione (che implementano 
 * ControlServiceInterface).<br>
 * Il controller costituisce la logica della servlet di control-dispatching.
 * Per ulteriori dettagli vedere il metodo service().
 * @author M.Diana
 */
public class ControllerManager extends AbstractService {

	private static final Logger _logger = LoggerFactory.getLogger(ControllerManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{}: initialized {} controller services", this.getClass().getName(), this.getControllerServices().size());
	}
	
	/**
	 * Esegue le azioni conseguenti alla richiesta del client.
	 * L'esecuzione è realizzata invocando in sequenza
	 * i sottoservizi di controllo definiti in configurazione (implementano 
	 * ControlServiceInterface); ogni sottoservizio termina con un valore di
	 * ritorno compreso fra le costanti definite in questa classe. Il valore di
	 * uscita di ogni sottoservizio è inviato in ingresso al sottoservizio
	 * successivo. Il valore di ritorno dell'ultimo sottoservizio eseguito 
	 * è restituito al chiamante.<br>
	 * Le regole sono:
	 * <ul>
	 * <li> i sottoservizi non devono lanciare eccezioni;
	 * <li> se un sottoservizio riceve in ingresso ERROR deve terminare
	 * immediatamente restituendo ERROR, a meno che non sia un servizio di
	 * gestione degli errori;
	 * <li> se un servizio restituisce OUTPUT o REDIRECT o SYS_ERROR la sequenza
	 * di esecuzione termina;
	 * <li> se un servizio restituisce CONTINUE la sequenza continua.
	 * </ul>
	 * @param reqCtx Il contesto della richiesta.
	 * @return Uno dei valori definiti dalle costanti della classe.
	 */
	public int service(RequestContext reqCtx) {
		int status = INVALID_STATUS;
		int srvIndex = 0;
		try {
			do {
				ControlServiceInterface srv = this.getControllerServices().get(srvIndex);
				srvIndex++;
				status = srv.service(reqCtx, status);
			} while (srvIndex < this.getControllerServices().size()
					&& status != OUTPUT 
					&& status != REDIRECT 
					&& status != SYS_ERROR);
		} catch (Throwable t) {
			_logger.error("generic error", t);
			//ApsSystemUtils.logThrowable(t, this, "service");
			status = SYS_ERROR;
		}
		return status;
	}
	
	/**
	 * Restituisce una descrizione dello stato passato come argomento.
	 * @param status Lo stato di cui si vuole la descrizione. 
	 * Deve essere una delle costanti di questa classe.
	 * @return La descrizione dello stato, oppure "non definito" 
	 * se lo stato non ha un valore previsto.
	 */
	public static String getStatusDescription(int status) {
		switch (status) {
			case INVALID_STATUS: return "INVALID_STATUS";
			case OUTPUT: return "OUTPUT";
			case REDIRECT: return "REDIRECT";
			case ERROR: return "ERROR";
			case CONTINUE: return "CONTINUE";
			case RESTART: return "RESTART";
			case SYS_ERROR: return "SYS_ERROR";
			default: return "non definito";
		}
	}
	
	protected List<ControlServiceInterface> getControllerServices() {
		return _controllerServices;
	}
	public void setControllerServices(List<ControlServiceInterface> controllerServices) {
		this._controllerServices = controllerServices;
	}
	
	/**
	 * La lista interna dei sottoservizi di controllo
	 */
	private List<ControlServiceInterface> _controllerServices;
	
	/**
	 * Stato di uscita dei sottoservizi di controllo: stato non valido. E'
	 * il valore iniziale, passato in ingresso al primo sottoservizio.
	 */
	public static final int INVALID_STATUS = 0;
	
	/**
	 * Stato di uscita dei sottoservizi di controllo: generato output. 
	 * Lo stato, nella esecuzione della catena di sottoservizi, corrisponde ad una uscita dalla sequenza.
	 */
	public static final int OUTPUT = 1;
	
	/**
	 * Stato di uscita dei sottoservizi di controllo: richiesta redirezione.
	 * Lo stato, nella esecuzione della catena di sottoservizi, corrisponde ad una uscita dalla sequenza.
	 */
	public static final int REDIRECT = 2;
	
	/**
	 * Stato di uscita dei sottoservizi di controllo: errore. 
	 * Lo stato, nella esecuzione della catena di sottoservizi, NON corrisponde ad una uscita dalla sequenza.
	 */
	public static final int ERROR = 3;
	
	/**
	 * Stato di uscita dei sottoservizi di controllo: continuare con l'esecuzione.
	 */
	public static final int CONTINUE = 4;
	
	/**
	 * Stato di uscita dei sottoservizi di controllo: riprendere la sequenza di
	 * esecuzione dall'inizio (deprecato). 
	 * @deprecated
	 */
	public static final int RESTART = 5;
	
	/**
	 * Stato di uscita finale: errore di sistema.
	 * Lo stato, nella esecuzione della catena di sottoservizi, corrisponde ad una uscita dalla sequenza.
	 */
	public static final int SYS_ERROR = 6;
	
}
