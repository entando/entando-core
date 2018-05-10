/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.plugins.jacms.aps.system.services.api.model;

import javax.xml.bind.annotation.XmlElement;
import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;

/**
 *
 * @author super
 */
public class CmsApiResponseResult extends AbstractApiResponseResult {

    @Override
    @XmlElement(name = "cmsResponse", required = false)
    public JAXBCmsResult getResult() {
        return (JAXBCmsResult) this.getResult();
    }
    
}
