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
package org.entando.entando.apsadmin.api.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

/**
 * @author E.Santoboni
 */
public class StreamSchemaOutputResolver extends SchemaOutputResolver {
    
    public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {
        StreamResult result = new StreamResult(this.getOs());
        result.setSystemId(suggestedFileName);
        return result;
    }
    
    public InputStream getStream() {
        return new ByteArrayInputStream(this.getOs().toByteArray());
    }
    
    protected ByteArrayOutputStream getOs() {
        return _os;
    }
    
    private ByteArrayOutputStream _os = new ByteArrayOutputStream();
    
}