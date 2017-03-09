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
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.baseconfig.SystemParamsUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class PageTokenManager extends AbstractService implements IPageTokenMager {

	private static final Logger logger = LoggerFactory.getLogger(PageTokenManager.class);

	private static final int SALT_LENGTH = 8;
	private static final int HASH_LENGTH = 20;

	private String salt;
	private String password;

	private ConfigInterface configManager;


	public String getSalt() {
		return salt;
	}

	public String getPassword() {
		return password;
	}

	protected ConfigInterface getConfigManager() {
		return configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this.configManager = configManager;
	}

	@Override
	public void init() throws Exception {
		String param = this.getConfigManager().getParam(PREVIEW_HASH);
		if (StringUtils.isBlank(param)) {
			param = this.generateRandomHash();
		}
		this.generateInternalSaltAndPassword(param);
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

	public void updateHash() throws Exception {
		try {
			String param = this.generateRandomHash();
			this.generateInternalSaltAndPassword(param);
		} catch (Throwable t) {
			throw new Exception("error in updateHash", t);
		}
	}

	protected void generateInternalSaltAndPassword(String param) {
		this.salt = StringUtils.left(param, SALT_LENGTH);
		this.password = StringUtils.substring(param, SALT_LENGTH);
	}

	protected String generateRandomHash() throws Exception  {
		String param = "";
		try {
			String xmlParams = this.getConfigManager().getConfigItem(SystemConstants.CONFIG_ITEM_PARAMS);
			Map<String, String> params = SystemParamsUtils.getParams(xmlParams);
			if (!params.containsKey(IPageTokenMager.PREVIEW_HASH)) {
				param = RandomStringUtils.randomAlphanumeric(HASH_LENGTH);
				params.put(PREVIEW_HASH, param);
				String newXmlParams = SystemParamsUtils.getNewXmlParams(xmlParams, params);
				this.getConfigManager().updateConfigItem(SystemConstants.CONFIG_ITEM_PARAMS, newXmlParams);
			}
			logger.info("Successfully created a random page_preview_hash");
		} catch (Throwable t) {
			throw new Exception("Error occurred generating a random page_preview_hash", t);
		}
		return param;
	}

	protected char[] getPasswordCharArray() {
		return this.getPassword().toCharArray();
	}

	@SuppressWarnings("restriction")
	protected static byte[] base64Decode(String property) throws IOException {
		return new BASE64Decoder().decodeBuffer(property);
	}

	@SuppressWarnings("restriction")
	protected static String base64Encode(byte[] bytes) {
		return new BASE64Encoder().encode(bytes);
	}

}
