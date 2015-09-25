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