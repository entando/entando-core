/*
 * Copyright 2015-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.page;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class PageTokenManager extends AbstractService implements IPageTokenMager {

	private static final Logger logger = LoggerFactory.getLogger(PageTokenManager.class);
	private static final String SALT_DEFAULT = "AAAAAAAA";
	private static final String PASSWORD_DEFAULT = "ABC12345678";
	private String salt;
	private String password;

	@Override
	public void init() throws Exception {
		logger.debug("ready");
		if (StringUtils.isBlank(this.salt)) {
			this.salt = SALT_DEFAULT;
			logger.warn("Using default salt. Please define a value");
		}
		if (StringUtils.isBlank(this.password)) {
			this.password = PASSWORD_DEFAULT;
			logger.warn("Using default password. Please define a value");
		}
	}
	
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String encrypt(String property) {
		SecretKeyFactory keyFactory;
		try {
			keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey key = keyFactory.generateSecret(new PBEKeySpec(this.getPasswordCharArray()));
			Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

			pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(this.getSalt().getBytes(), 20));
			return base64Encode(pbeCipher.doFinal(property.getBytes("UTF-8")));

		} catch (GeneralSecurityException e) {
			logger.error("Error in encrypt", e);
		} catch (UnsupportedEncodingException e) {
			logger.error("Error in encrypt", e);
		}
		return null;
	}

	public String decrypt(String property) {
		SecretKeyFactory keyFactory;
		try {
			keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey key = keyFactory.generateSecret(new PBEKeySpec(this.getPasswordCharArray()));
			Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
			pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(this.getSalt().getBytes(), 20));
			return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
		} catch (GeneralSecurityException e) {
			logger.error("Error in decrypt", e);
		} catch (IOException e) {
			logger.error("Error in decrypt", e);
		} 
		return null;
	}

	private char[] getPasswordCharArray() {
		return this.getPassword().toCharArray();
	}

	@SuppressWarnings("restriction")
	private static byte[] base64Decode(String property) throws IOException {
		return new BASE64Decoder().decodeBuffer(property);
	}

	@SuppressWarnings("restriction")
	private static String base64Encode(byte[] bytes) {
		return new BASE64Encoder().encode(bytes);
	}
}
