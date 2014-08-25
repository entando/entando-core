/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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