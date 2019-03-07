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
package org.entando.entando.aps.util.crypto;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.security.crypto.encrypt.TextEncryptor;

public class BlowfishEncryptor extends KeyHolder implements TextEncryptor {

    private static final String TRIPLE_BLOWFISH_KEY_SPEC = "Blowfish";
    private static final String TRIPLE_BLOWFISH = "Blowfish/ECB/PKCS5Padding";

    @Override
    public String decrypt(String source) {
        return decrypt(getKey(), source);
    }

    public String decrypt(String key, String source) {
        return decrypt(getKey(key), source);
    }

    private String decrypt(Key key, String source) {
        try {
            Cipher bfCipher = Cipher.getInstance(TRIPLE_BLOWFISH);
            byte[] dec = Base64.getDecoder().decode(source.getBytes());
            bfCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] cleartext = bfCipher.doFinal(dec);
            // Return the clear text
            return new String(cleartext);
        } catch (Throwable t) {
            throw new CryptoException("Error decrypting string", t);
        }
    }

    @Override
    public String encrypt(String plainText) {
        return encrypt(getKey(), plainText);
    }

    public String encryptString(String key, String plainText) {
        return encrypt(getKey(key), plainText);
    }

    private String encrypt(Key key, String plainText) {
        try {
            Cipher bfCipher = Cipher.getInstance(TRIPLE_BLOWFISH);
            bfCipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cleartext = plainText.getBytes();
            byte[] ciphertext = bfCipher.doFinal(cleartext);
            return new String(Base64.getEncoder().encode(ciphertext));
        } catch (Throwable t) {
            throw new CryptoException("Error detected while encrypting a string", t);
        }
    }

    @Override
    protected Key getKey(String key) {
        try {
            // hack for JCE Unlimited Strength
            Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
            field.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.set(null, false);

            byte[] bytes = key.getBytes();
            SecretKey s = new SecretKeySpec(bytes, TRIPLE_BLOWFISH_KEY_SPEC);
            return s;
        } catch (Throwable t) {
            throw new CryptoException("Error creating key", t);
        }
    }
}
