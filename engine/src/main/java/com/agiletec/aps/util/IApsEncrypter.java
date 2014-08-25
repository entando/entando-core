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
package com.agiletec.aps.util;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Encrypter Interface.
 * @author E.Santoboni
 */
public interface IApsEncrypter {
	
	/**
	 * Encrypt the given plain text using the default algorithm
	 * @param plainText the string to encrypt
	 * @return the given string encrypted
	 * @throws ApsSystemException in caso d'errore
	 */
	public String encrypt(String plainText) throws ApsSystemException;
	
}