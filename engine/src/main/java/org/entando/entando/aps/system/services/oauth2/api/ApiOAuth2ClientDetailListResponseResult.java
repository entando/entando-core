/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.aps.system.services.oauth2.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;
import org.entando.entando.aps.system.services.api.model.ListResponse;

@XmlSeeAlso({JAXBApiOAuth2ClientDetail.class})
public class ApiOAuth2ClientDetailListResponseResult extends AbstractApiResponseResult {
    
    @XmlElement(name = "items", required = false)
    public ListResponse<JAXBApiOAuth2ClientDetail> getResult() {
        if (this.getMainResult() instanceof Collection) {
            List<JAXBApiOAuth2ClientDetail> apiOAuth2ClientDetails = new ArrayList<JAXBApiOAuth2ClientDetail>();
            apiOAuth2ClientDetails.addAll((Collection<JAXBApiOAuth2ClientDetail>) this.getMainResult());
            ListResponse<JAXBApiOAuth2ClientDetail> entity = new ListResponse<JAXBApiOAuth2ClientDetail>(apiOAuth2ClientDetails) {};
            return entity;
        }
        return null;
    }

}