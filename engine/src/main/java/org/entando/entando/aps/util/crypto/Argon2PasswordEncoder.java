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

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;
import java.nio.charset.Charset;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Argon2PasswordEncoder implements PasswordEncoder {

    private int hashLength;
    private int saltLength;
    private int iterations; // -t N
    private int memory; // -m N
    private int parallelism; // -p N

    private final Charset charset = Charset.forName("UTF-8");
    private Argon2Types type = Argon2Types.ARGON2i;

    public Argon2PasswordEncoder() {

        String saltLengthConfig = System.getProperties().getProperty("algo.argon2.salt.length");
        if (StringUtils.isNotBlank(saltLengthConfig)) {
            this.saltLength = Integer.valueOf(saltLengthConfig);
        }

        String hashLengthConfig = System.getProperties().getProperty("algo.argon2.hash.length");
        if (StringUtils.isNotBlank(hashLengthConfig)) {
            hashLength = Integer.valueOf(hashLengthConfig);
        }

        String iterationsConfig = System.getProperties().getProperty("algo.argon2.iterations");
        if (StringUtils.isNotBlank(iterationsConfig)) {
            iterations = Integer.valueOf(iterationsConfig);
        }

        String memoryConfig = System.getProperties().getProperty("algo.argon2.memory");
        if (StringUtils.isNotBlank(memoryConfig)) {
            memory = Integer.valueOf(memoryConfig);
        }

        String parallelismConfig = System.getProperties().getProperty("algo.argon2.parallelism");
        if (StringUtils.isNotBlank(parallelismConfig)) {
            parallelism = Integer.valueOf(parallelismConfig);
        }

        String algoType = System.getProperties().getProperty("algo.argon2.type");
        if (StringUtils.isNotBlank(algoType)) {
            type = Argon2Types.valueOf(algoType);
        }
    }

    @Override
    public String encode(CharSequence password) {
        return getArgon2().hash(iterations, memory, parallelism, password.toString(), charset);
    }

    @Override
    public boolean matches(CharSequence password, String hash) {
        return getArgon2().verify(hash, password.toString());
    }

    private Argon2 getArgon2() {
        return Argon2Factory.create(type, saltLength, hashLength);
    }
}
