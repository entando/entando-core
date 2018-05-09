/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.aps.system.services.userprofile.api.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.JAXBEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractTextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.services.category.ICategoryManager;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "userProfile")
@XmlType(propOrder = {"fullname", "mail"})
public class JAXBUserProfile extends JAXBEntity {

    public JAXBUserProfile() {
    }

    public JAXBUserProfile(IApsEntity mainEntity, String langCode) {
        super(mainEntity, langCode);
        IUserProfile profile = (IUserProfile) mainEntity;
        //String fnan = profile.getFirstNameAttributeName();
        //String snan = profile.getSurnameAttributeName();
        String fn = profile.getFullNameAttributeName();
        String man = profile.getMailAttributeName();
        this.setFullname((String) profile.getValue(fn));
        //this.setFirstname((String) profile.getValue(fnan));
        //this.setSurname((String) profile.getValue(snan));
        this.setMail((String) profile.getValue(man));
    }

    @Override
    public IApsEntity buildEntity(IApsEntity prototype, ICategoryManager categoryManager, String langCode) {
        IUserProfile profile = (IUserProfile) super.buildEntity(prototype, categoryManager, langCode);
        //this.valorizeTextAttribute(profile.getFirstNameAttributeName(), this.getFirstname(), profile);
        //this.valorizeTextAttribute(profile.getSurnameAttributeName(), this.getSurname(), profile);
        this.valorizeTextAttribute(profile.getMailAttributeName(), this.getMail(), profile);
        return profile;
    }

    private void valorizeTextAttribute(String attributeName, String value, IUserProfile profile) {
        if (null == attributeName || value == null) {
            return;
        }
        AttributeInterface attribute = (AttributeInterface) profile.getAttribute(attributeName);
        if (null == attribute || !(attribute instanceof AbstractTextAttribute)) {
            return;
        }
        AbstractTextAttribute textAttribute = (AbstractTextAttribute) attribute;
        textAttribute.setText(value, null);
    }

    @XmlElement(name = "fullname", required = false)
    public String getFullname() {
        return _fullname;
    }

    public void setFullname(String fullname) {
        this._fullname = fullname;
    }

    /*
	@XmlElement(name = "firstname", required = false)
	public String getFirstname() {
		return _firstname;
	}
	public void setFirstname(String firstname) {
		this._firstname = firstname;
	}

	@XmlElement(name = "surname", required = false)
	public String getSurname() {
		return _surname;
	}
	public void setSurname(String surname) {
		this._surname = surname;
	}
     */
    @XmlElement(name = "mail", required = false)
    public String getMail() {
        return _mail;
    }

    public void setMail(String mail) {
        this._mail = mail;
    }
    //private String _firstname;
    //private String _surname;
    private String _fullname;
    private String _mail;

}
