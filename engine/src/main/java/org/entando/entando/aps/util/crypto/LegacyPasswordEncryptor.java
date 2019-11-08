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

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

/**
 * Legacy Encryptor Engine (kept for retro-compatibility with old passwords).
 */
@Component
@Deprecated
public class LegacyPasswordEncryptor implements TextEncryptor {

    @Override
    public String encrypt(String plainText) {
        try {
            Key key = getKey();
            Cipher desCipher = Cipher.getInstance(TRIPLE_DES);
            desCipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cleartext = plainText.getBytes();
            byte[] ciphertext = desCipher.doFinal(cleartext);
            return new String(Base64.getEncoder().encode(ciphertext));
        } catch (Throwable t) {
            throw new CryptoException("Error detected while encoding a string", t);
        }
    }

    @Override
    public String decrypt(String source) {
        try {
            Key key = getKey();
            Cipher desCipher = Cipher.getInstance(TRIPLE_DES);
            byte[] dec = Base64.getDecoder().decode(source.getBytes());
            desCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] cleartext = desCipher.doFinal(dec);
            // Return the clear text
            return new String(cleartext);
        } catch (Throwable t) {
            throw new CryptoException("Error decrypting string", t);
        }
    }

    public static Key getKey() {
        try {
            byte[] bytes = KEY_STRING.getBytes();
            DESedeKeySpec pass = new DESedeKeySpec(bytes);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(TRIPLE_DES_KEY_SPEC);
            return skf.generateSecret(pass);
        } catch (Throwable t) {
            throw new CryptoException("Error creating key", t);
        }
    }

    public static final String TRIPLE_DES_KEY_SPEC = "DESede";
    public static final String TRIPLE_DES = "DESede/ECB/PKCS5Padding";
    private static final String KEY_STRING
            = "21-199-217-127-162-182-251-137-227-56-131-242-191-224-21-97-146-158-152-21-124-70-127-91";
}
