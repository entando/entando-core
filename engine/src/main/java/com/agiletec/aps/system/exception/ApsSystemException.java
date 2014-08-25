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
package com.agiletec.aps.system.exception;

/**
 * Eccezione di di sistema
 * @version 1.0
 * @author 
 */
public class ApsSystemException extends ApsException {
	/**
	 * Costruttore con solo messaggio
	 * @param message Il messaggio associato all'eccezione
	 */
	public ApsSystemException(String message){
		super(message);
	}
	
	/**
	 * Costruttore con messaggio e causa (precedente eccezione).
	 * @param message Il messaggio associato all'eccezione
	 * @param cause L'eccezione che ha causato l'eccezione originale 
	 */
	public ApsSystemException(String message, Throwable cause){
		super(message, cause);
	}
}
