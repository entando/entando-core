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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class BlowfishEncryptorTest {

    private BlowfishEncryptor encryptor;

    @Before
    public void setUp() {
        encryptor = new BlowfishEncryptor();
        encryptor.setKey("my-test-key");
    }

    @Test
    public void testEncryptAndDecrypt() throws Exception {

        String secret = "my secret";

        String encrypted = encryptor.encrypt(secret);

        assertThat(encrypted).isNotNull()
                .isNotEqualTo(secret);

        String decrypted = encryptor.decrypt(encrypted);

        assertThat(decrypted).isEqualTo(secret);
    }
}
