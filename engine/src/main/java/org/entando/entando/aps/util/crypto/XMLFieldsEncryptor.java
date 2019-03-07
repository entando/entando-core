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

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.springframework.security.crypto.encrypt.TextEncryptor;

public class XMLFieldsEncryptor<T> {

    private final EncryptorAdapter encryptorAdapter;
    private final JAXBContext ctx;

    public XMLFieldsEncryptor(TextEncryptor encryptor, Class<T> type) {
        this.encryptorAdapter = new EncryptorAdapter(encryptor);
        try {
            ctx = JAXBContext.newInstance(type);
        } catch (JAXBException ex) {
            throw new DataBindingException(ex);
        }
    }

    public <T> String marshal(T object) {
        try {
            StringWriter sw = new StringWriter();
            Marshaller marshaller = ctx.createMarshaller();
            marshaller.setAdapter(encryptorAdapter);
            marshaller.marshal(object, sw);
            return sw.toString();
        } catch (JAXBException ex) {
            throw new DataBindingException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T unmarshal(String xml) {
        try {
            Unmarshaller unmarshaller = ctx.createUnmarshaller();
            unmarshaller.setAdapter(encryptorAdapter);
            StringReader reader = new StringReader(xml);
            return (T) unmarshaller.unmarshal(reader);
        } catch (JAXBException ex) {
            throw new DataBindingException(ex);
        }
    }
}
