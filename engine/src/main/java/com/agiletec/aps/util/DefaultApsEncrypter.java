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

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Default Encrypter Engine.
 * @author M.Minnai
 */
public class DefaultApsEncrypter implements IApsEncrypter {

	public static String decrypt(String source) {
		try {
			Key key = getKey(); 
			Cipher desCipher = Cipher.getInstance(TRIPLE_DES);
			byte[] dec = Base64.decodeBase64(source.getBytes());
			desCipher.init(Cipher.DECRYPT_MODE, key);
			byte[] cleartext = desCipher.doFinal(dec);
			// Return the clear text
			return new String(cleartext);
		} catch (Throwable t) {
			throw new RuntimeException("Error decrypting string", t);
		}
	}
	
	public static String encryptString(String plainText) throws ApsSystemException {
		String encryptedString = null;
		try {
			Key key = getKey();
			Cipher desCipher = Cipher.getInstance(TRIPLE_DES);
			desCipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] cleartext = plainText.getBytes();
			byte[] ciphertext = desCipher.doFinal(cleartext);
			encryptedString = new String(Base64.encodeBase64(ciphertext));
		} catch (Throwable t) {
			throw new ApsSystemException("Error detcted while encoding a string", t);
		}
		return encryptedString;
	}
	
	public String encrypt(String text) throws ApsSystemException {
		return DefaultApsEncrypter.encryptString(text);
	}
	
	public static Key getKey() {
		try {
			byte[] bytes = KEY_STRING.getBytes();
			DESedeKeySpec pass = new DESedeKeySpec(bytes);
			SecretKeyFactory skf = SecretKeyFactory.getInstance(TRIPLE_DES_KEY_SPEC); 
			SecretKey s = skf.generateSecret(pass); 
			return s;
		} catch (Throwable t) {
			throw new RuntimeException("Error creating key", t);
		}
	}
	
	public static final String TRIPLE_DES_KEY_SPEC = "DESede";
	public static final String TRIPLE_DES = "DESede/ECB/PKCS5Padding";
	private static final String KEY_STRING =
		"21-199-217-127-162-182-251-137-227-56-131-242-191-224-21-97-146-158-152-21-124-70-127-91";
	
}
