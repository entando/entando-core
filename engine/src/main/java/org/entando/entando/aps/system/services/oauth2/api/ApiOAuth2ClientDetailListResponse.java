/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.aps.system.services.oauth2.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponse;
import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;


@XmlRootElement(name = "response")
public class ApiOAuth2ClientDetailListResponse extends AbstractApiResponse {
    
    @Override
    @XmlElement(name = "result", required = true)
    public ApiOAuth2ClientDetailListResponseResult getResult() {
        return (ApiOAuth2ClientDetailListResponseResult) super.getResult();
    }
    
    @Override
    protected AbstractApiResponseResult createResponseResultInstance() {
        return new ApiOAuth2ClientDetailListResponseResult();
    }
    
}