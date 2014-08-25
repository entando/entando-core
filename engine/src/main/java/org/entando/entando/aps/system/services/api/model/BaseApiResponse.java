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
package org.entando.entando.aps.system.services.api.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author E.Santoboni
 * @deprecated use StringResponse class
 */
@XmlRootElement(name = "response")
public class BaseApiResponse extends AbstractApiResponse {
    
    @XmlElement(name = "result", required = false)
    public String getResult() {
        if (null != super.getResult()) {
            return super.getResult().toString();
        }
        return null;
    }
    
    public void setResult(Object result, String html) {
        super.setResult(result);
    }
    
    protected AbstractApiResponseResult createResponseResultInstance() {
        throw new UnsupportedOperationException("Unsupported method");
    }
    
}