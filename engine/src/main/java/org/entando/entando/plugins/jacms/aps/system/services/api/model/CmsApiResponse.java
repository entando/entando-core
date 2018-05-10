/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package org.entando.entando.plugins.jacms.aps.system.services.api.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.entando.entando.aps.system.services.api.model.AbstractApiResponse;
import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;

/**
 *
 * @author super
 */
@XmlRootElement(name = "response")
public class CmsApiResponse extends AbstractApiResponse {
    
    @Override
    @XmlElement(name = "result", required = false)
    public JAXBCmsResult getResult() {
        return (JAXBCmsResult) super.getResult();
    }
    
    @Override
    public void setResult(Object result, String html) {
        super.setResult(result);
    }
    
    public void setResult(String result, String html) {
        JAXBCmsResult res = new JAXBCmsResult();
        res.setStatus(html);
        super.setResult(res);
    }
    
    @Override
    protected AbstractApiResponseResult createResponseResultInstance() {
        throw new UnsupportedOperationException("Unsupported method");
    }
    
}
