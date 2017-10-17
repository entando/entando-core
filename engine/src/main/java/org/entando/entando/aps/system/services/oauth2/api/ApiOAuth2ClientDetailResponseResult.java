/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.aps.system.services.oauth2.api;

import javax.xml.bind.annotation.XmlElement;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;


public class ApiOAuth2ClientDetailResponseResult extends AbstractApiResponseResult {
    
    @Override
    @XmlElement(name = "apiOAuth2ClientDetail", required = false)
    public JAXBApiOAuth2ClientDetail getResult() {
        return (JAXBApiOAuth2ClientDetail) this.getMainResult();
    }
    
}