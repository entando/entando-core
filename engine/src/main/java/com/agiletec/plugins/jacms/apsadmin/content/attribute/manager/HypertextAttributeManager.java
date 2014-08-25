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
package com.agiletec.plugins.jacms.apsadmin.content.attribute.manager;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.util.ICmsAttributeErrorCodes;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Classe manager degli attributi tipo Hypertext.
 * @author E.Santoboni
 */
public class HypertextAttributeManager extends com.agiletec.apsadmin.system.entity.attribute.manager.TextAttributeManager {

	@Override
    protected String getCustomAttributeErrorMessage(AttributeFieldError attributeFieldError, ActionSupport action) {
        String errorCode = attributeFieldError.getErrorCode();
        String messageKey = null;
        if (errorCode.equals(ICmsAttributeErrorCodes.INVALID_PAGE)) {
            messageKey = "HypertextAttribute.fieldError.linkToPage";
        } else if (errorCode.equals(ICmsAttributeErrorCodes.VOID_PAGE)) {
            messageKey = "HypertextAttribute.fieldError.linkToPage.voidPage";
        } else if (errorCode.equals(ICmsAttributeErrorCodes.INVALID_CONTENT)) {
            messageKey = "HypertextAttribute.fieldError.linkToContent";
        } else if (errorCode.equals(ICmsAttributeErrorCodes.INVALID_PAGE_GROUPS)) {
            messageKey = "HypertextAttribute.fieldError.linkToPage.wrongGroups";
        } else if (errorCode.equals(ICmsAttributeErrorCodes.INVALID_CONTENT_GROUPS)) {
            messageKey = "HypertextAttribute.fieldError.linkToContent.wrongGroups";
        }
        if (null != messageKey) {
            String[] args = {attributeFieldError.getTracer().getLang().getDescr()};
            return action.getText(messageKey, args);
        } else {
            return super.getCustomAttributeErrorMessage(attributeFieldError, action);
        }
    }

}