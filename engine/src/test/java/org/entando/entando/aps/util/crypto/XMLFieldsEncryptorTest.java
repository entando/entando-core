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

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class XMLFieldsEncryptorTest {

    @XmlRootElement(name = "root")
    static class JaxbClass {

        private String secret;

        @XmlJavaTypeAdapter(EncryptorAdapter.class)
        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }
    }

    @Test
    public void testFieldEncryption() {

        BlowfishEncryptor encryptor = new BlowfishEncryptor("test-key");

        XMLFieldsEncryptor<JaxbClass> xmlFieldsEncryptor
                = new XMLFieldsEncryptor<>(encryptor, JaxbClass.class);

        String secret = "my secret";

        JaxbClass object = new JaxbClass();
        object.setSecret(secret);

        String xml = xmlFieldsEncryptor.marshal(object);

        assertThat(xml.contains(secret)).isFalse();

        JaxbClass unmarshalled = xmlFieldsEncryptor.unmarshal(xml);
        assertThat(unmarshalled.getSecret()).isEqualTo(secret);
    }
}
