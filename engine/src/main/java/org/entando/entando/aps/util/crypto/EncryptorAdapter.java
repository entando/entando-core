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

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.springframework.security.crypto.encrypt.TextEncryptor;

/**
 * Encrypts/decrypts values during JAXB serialization/deserialization.
 * See also XMLFieldsEncryptor.
 */
public class EncryptorAdapter extends XmlAdapter<String, String> {

    private final TextEncryptor encryptor;

    public EncryptorAdapter(TextEncryptor encryptor) {
        this.encryptor = encryptor;
    }

    @Override
    public String unmarshal(String value) throws Exception {
        return encryptor.decrypt(value);
    }

    @Override
    public String marshal(String value) throws Exception {
        return encryptor.encrypt(value);
    }
}
