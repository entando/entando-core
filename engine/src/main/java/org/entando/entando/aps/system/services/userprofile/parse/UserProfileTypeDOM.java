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
package org.entando.entando.aps.system.services.userprofile.parse;

import java.util.List;

import org.entando.entando.aps.system.services.oauth.OAuthConsumerManager;
import org.jdom.Document;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.parse.EntityTypeDOM;
import com.agiletec.aps.system.common.entity.parse.IApsEntityDOM;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public class UserProfileTypeDOM extends EntityTypeDOM {

	private static final Logger _logger =  LoggerFactory.getLogger(UserProfileTypeDOM.class);
	
	@Override
	@Deprecated (/** inserted to guarantee compatibility with previous version of jpuserprofile 1.6 plugin (of jAPS 2.2.0) */)
	protected void doParsing(Document document, Class entityClass, IApsEntityDOM entityDom) throws ApsSystemException {
		List<Element> contentElements = document.getRootElement().getChildren();
		for (int i=0; i<contentElements.size(); i++) {
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
	
	@Deprecated (/** inserted to guarantee compatibility with previous version of jpuserprofile 1.6 plugin (of jAPS 2.2.0) */)
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