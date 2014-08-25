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
package org.entando.entando.aps.system.services.userprofile.api.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.entando.entando.aps.system.common.entity.api.JAXBEntityType;

import com.agiletec.aps.system.common.entity.model.IApsEntity;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "userProfileType")
public class JAXBUserProfileType extends JAXBEntityType {
    
    public JAXBUserProfileType() {}
    
    public JAXBUserProfileType(IApsEntity entityType) {
        super(entityType);
    }
    
}