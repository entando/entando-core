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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import static org.assertj.core.api.Assertions.assertThat;
import org.entando.entando.TestEntandoJndiUtils;
import org.junit.BeforeClass;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath*:spring/testpropertyPlaceholder.xml",
    "classpath*:spring/baseSystemConfig.xml",
    "classpath*:spring/aps/**/**.xml",
    "classpath*:spring/plugins/**/aps/**/**.xml",
    "classpath*:spring/web/**.xml"
})
@WebAppConfiguration(value = "")
public class DefaultTextEncryptorTest {

    @BeforeClass
    public static void setup() throws Exception {
        TestEntandoJndiUtils.setupJndi();
    }

    @Autowired
    private DefaultTextEncryptor encryptor;

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
