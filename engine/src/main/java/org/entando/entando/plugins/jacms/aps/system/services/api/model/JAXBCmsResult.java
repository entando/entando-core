/*
*
* @author Entando
*
*/
package org.entando.entando.plugins.jacms.aps.system.services.api.model;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "cms")
@XmlType(propOrder = {"id", "status"})
public class JAXBCmsResult {
    
    @XmlElement(name = "id", required = true)
    public String getId() {
        return _id;
    }
    public void setId(String id) {
        this._id = id;
    }
    
    @XmlElement(name = "status", required = true)
    public String getStatus() {
        return _status;
    }
    public void setStatus(String status) {
        this._status = status;
    }
     
    private String _id;
    private String _status;
    
}
