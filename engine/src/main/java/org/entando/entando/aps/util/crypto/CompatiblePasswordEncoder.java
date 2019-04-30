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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CompatiblePasswordEncoder implements PasswordEncoder {

    private static final String BCRYPT_PREFIX = "{bcrypt}";
    private static final String ARGON2_PREFIX = "$argon2";

    private final BCryptPasswordEncoder bcryptEncoder;
    private final Argon2PasswordEncoder argon2Encoder;
    private final LegacyPasswordEncryptor legacyEncryptor;

    @Autowired
    public CompatiblePasswordEncoder(BCryptPasswordEncoder bcryptEncoder,
            Argon2PasswordEncoder argon2Encoder, LegacyPasswordEncryptor legacyEncryptor) {
        this.bcryptEncoder = bcryptEncoder;
        this.argon2Encoder = argon2Encoder;
        this.legacyEncryptor = legacyEncryptor;
    }

    @Override
    public String encode(CharSequence password) {
        return BCRYPT_PREFIX + bcryptEncoder.encode(password);
    }

    @Override
    public boolean matches(CharSequence password, String encodedPassword) {
        if (isBCrypt(encodedPassword)) {
            String encodedWithoutPrefix = encodedPassword.substring(BCRYPT_PREFIX.length());
            return bcryptEncoder.matches(password, encodedWithoutPrefix);
        } else if (isArgon2(encodedPassword)) {
            return argon2Encoder.matches(password, encodedPassword);
        } else {
            return encodedPassword.equals(legacyEncryptor.encrypt(password.toString()));
        }
    }

    public static boolean isBCrypt(String encodedPassword) {
        return encodedPassword.startsWith(BCRYPT_PREFIX);
    }

    public static boolean isArgon2(String encodedPassword) {
        return encodedPassword.startsWith(ARGON2_PREFIX);
    }
}
