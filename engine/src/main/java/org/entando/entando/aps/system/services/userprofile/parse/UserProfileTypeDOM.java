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
package org.entando.entando.aps.system.services.userprofile.parse;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.parse.EntityTypeDOM;
import com.agiletec.aps.system.common.entity.parse.IApsEntityDOM;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.jdom.Document;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author E.Santoboni
 */
public class UserProfileTypeDOM extends EntityTypeDOM {

    private static final Logger _logger = LoggerFactory.getLogger(UserProfileTypeDOM.class);

    @Override
    @Deprecated(/** inserted to guarantee compatibility with previous version of jpuserprofile 1.6 plugin (of jAPS 2.2.0) */)
    protected void doParsing(Document document, Class entityClass, IApsEntityDOM entityDom) throws ApsSystemException {
        List<Element> contentElements = document.getRootElement().getChildren();
        for (int i = 0; i < contentElements.size(); i++) {
            Element currentContentElem = contentElements.get(i);
            IApsEntity entity = this.createEntityType(currentContentElem, entityClass);
            entity.setEntityDOM(entityDom);
            this.getEntityTypes().put(entity.getTypeCode(), entity);
            this.fillEntityType(entity, currentContentElem);
            entity.setDefaultLang(this.getLangManager().getDefaultLang().getCode());
            //this.extractRole(currentContentElem, entity, "firstNameAttributeName", SystemConstants.ATTRIBUTE_ROLE_FIRST_NAME);
            //this.extractRole(currentContentElem, entity, "surnameAttributeName", SystemConstants.ATTRIBUTE_ROLE_SURNAME);
            this.extractRole(currentContentElem, entity, "mailAttributeName", SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_MAIL);
            _logger.debug("Definining the Entity Type: {}", entity.getTypeCode());
        }
    }

    @Deprecated(/** inserted to guarantee compatibility with previous version of jpuserprofile 1.6 plugin (of jAPS 2.2.0) */)
    private void extractRole(Element contentElem,
                             IApsEntity userProfile, String xmlAttribute, String roleName) throws ApsSystemException {
        if (null == userProfile.getAttributeByRole(roleName)) {
            String attributeName = this.extractXmlAttribute(contentElem, xmlAttribute, false);
            if (null != attributeName && !attributeName.equals(NULL_VALUE)) {
                AttributeInterface attribute = (AttributeInterface) userProfile.getAttribute(attributeName);
                if (null != attribute) {
                    String[] roles = {roleName};
                    attribute.setRoles(roles);
                }
            }
        }
    }

    @Override
    protected String getEntityTypeRootElementName() {
        return "profiletype";
    }

    @Override
    protected String getEntityTypesRootElementName() {
        return "profiletypes";
    }

    private static final String NULL_VALUE = "**NULL**";

}