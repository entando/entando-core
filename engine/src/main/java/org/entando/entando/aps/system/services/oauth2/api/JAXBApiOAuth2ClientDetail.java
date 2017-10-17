/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.aps.system.services.oauth2.api;

import org.entando.entando.aps.system.services.oauth2.model.ApiOAuth2ClientDetail;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name = "apiOAuth2ClientDetail")
@XmlType(propOrder = {"id", "name", "clientId", "clientSecret", "redirectUri", "clientUri", "description", "iconUri", "issuedAt", "expiresIn"})
public class JAXBApiOAuth2ClientDetail {

    public JAXBApiOAuth2ClientDetail() {
        super();
    }

    public JAXBApiOAuth2ClientDetail(ApiOAuth2ClientDetail apiOAuth2ClientDetail) {
		this.setId(apiOAuth2ClientDetail.getId());
		this.setName(apiOAuth2ClientDetail.getName());
		this.setClientId(apiOAuth2ClientDetail.getClientId());
		this.setClientSecret(apiOAuth2ClientDetail.getClientSecret());
		this.setRedirectUri(apiOAuth2ClientDetail.getRedirectUri());
		this.setClientUri(apiOAuth2ClientDetail.getClientUri());
		this.setDescription(apiOAuth2ClientDetail.getDescription());
		this.setIconUri(apiOAuth2ClientDetail.getIconUri());
		this.setIssuedAt(apiOAuth2ClientDetail.getIssuedAt());
		this.setExpiresIn(apiOAuth2ClientDetail.getExpiresIn());
    }
    
    public ApiOAuth2ClientDetail getApiOAuth2ClientDetail() {
    	ApiOAuth2ClientDetail apiOAuth2ClientDetail = new ApiOAuth2ClientDetail();
		apiOAuth2ClientDetail.setId(this.getId());
		apiOAuth2ClientDetail.setName(this.getName());
		apiOAuth2ClientDetail.setClientId(this.getClientId());
		apiOAuth2ClientDetail.setClientSecret(this.getClientSecret());
		apiOAuth2ClientDetail.setRedirectUri(this.getRedirectUri());
		apiOAuth2ClientDetail.setClientUri(this.getClientUri());
		apiOAuth2ClientDetail.setDescription(this.getDescription());
		apiOAuth2ClientDetail.setIconUri(this.getIconUri());
		apiOAuth2ClientDetail.setIssuedAt(this.getIssuedAt());
		apiOAuth2ClientDetail.setExpiresIn(this.getExpiresIn());
    	return apiOAuth2ClientDetail;
    }

	@XmlElement(name = "id", required = true)
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	@XmlElement(name = "name", required = true)
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}

	@XmlElement(name = "clientId", required = true)
	public String getClientId() {
		return _clientId;
	}
	public void setClientId(String clientId) {
		this._clientId = clientId;
	}

	@XmlElement(name = "clientSecret", required = true)
	public String getClientSecret() {
		return _clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this._clientSecret = clientSecret;
	}

	@XmlElement(name = "redirectUri", required = true)
	public String getRedirectUri() {
		return _redirectUri;
	}
	public void setRedirectUri(String redirectUri) {
		this._redirectUri = redirectUri;
	}

	@XmlElement(name = "clientUri", required = true)
	public String getClientUri() {
		return _clientUri;
	}
	public void setClientUri(String clientUri) {
		this._clientUri = clientUri;
	}

	@XmlElement(name = "description", required = true)
	public String getDescription() {
		return _description;
	}
	public void setDescription(String description) {
		this._description = description;
	}

	@XmlElement(name = "iconUri", required = true)
	public String getIconUri() {
		return _iconUri;
	}
	public void setIconUri(String iconUri) {
		this._iconUri = iconUri;
	}

	@XmlElement(name = "issuedAt", required = true)
	public Date getIssuedAt() {
		return _issuedAt;
	}
	public void setIssuedAt(Date issuedAt) {
		this._issuedAt = issuedAt;
	}

	@XmlElement(name = "expiresIn", required = true)
	public Date getExpiresIn() {
		return _expiresIn;
	}
	public void setExpiresIn(Date expiresIn) {
		this._expiresIn = expiresIn;
	}


	private int _id;
	private String _name;
	private String _clientId;
	private String _clientSecret;
	private String _redirectUri;
	private String _clientUri;
	private String _description;
	private String _iconUri;
	private Date _issuedAt;
	private Date _expiresIn;

}
