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
package org.entando.entando.aps.system.services.api.response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;
import org.entando.entando.aps.system.services.api.model.ListResponse;
import org.entando.entando.aps.system.services.api.model.ServiceInfo;

/**
 * @author E.Santoboni
 */
@XmlSeeAlso({ServiceInfo.class})
public class ServicesResponseResult extends AbstractApiResponseResult {
    
    @XmlElement(name = "services", required = false)
    public ListResponse getResult() {
        if (this.getMainResult() instanceof Collection) {
            List<ServiceInfo> services = new ArrayList<ServiceInfo>();
            services.addAll((Collection<ServiceInfo>) this.getMainResult());
            ListResponse listResponse = new ListResponse(services) {};
            return listResponse;
        }
        return null;
    }
    
}