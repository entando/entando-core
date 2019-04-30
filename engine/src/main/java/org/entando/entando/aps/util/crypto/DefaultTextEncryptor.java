/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
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

import java.util.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

/**
 * TextEncryptor implementation that wraps the standard Spring encryptor
 * generating also the required salt.
 */
public class DefaultTextEncryptor implements TextEncryptor {

    private final String key;

    public DefaultTextEncryptor(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new IllegalStateException("Empty key provided to DefaultTextEncryptor");
        }
        this.key = key;
    }

    /**
     * Returns a Base64 string composed by the salt followed by the encrypted
     * data.
     */
    @Override
    public String encrypt(String plainText) {

        // default StringKeyGenerator returns a 8 bytes hex-encoded string
        String salt = KeyGenerators.string().generateKey();

        BytesEncryptor encryptor = Encryptors.standard(key, salt);
        byte[] encrypted = encryptor.encrypt(plainText.getBytes());

        byte[] saltAndSecret = ArrayUtils.addAll(Hex.decode(salt), encrypted);
        return Base64.getEncoder().encodeToString(saltAndSecret);
    }

    /**
     * Returns decrypted text from a Base64 string composed by the salt followed
     * by the encrypted data.
     */
    @Override
    public String decrypt(String base64Data) {

        byte[] bytes = Base64.getDecoder().decode(base64Data);
        byte[] saltBytes = ArrayUtils.subarray(bytes, 0, 8);
        byte[] encryptedBytes = ArrayUtils.subarray(bytes, 8, bytes.length);

        String salt = new String(Hex.encode(saltBytes));
        BytesEncryptor encryptor = Encryptors.standard(key, salt);

        return new String(encryptor.decrypt(encryptedBytes));
    }
}
