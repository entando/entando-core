/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
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
package com.agiletec.apsadmin.system.entity.attribute.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.ListAttribute;
import com.agiletec.aps.system.services.lang.Lang;

/**
 * Manager class for the 'Multi-Language List' Attributes
 * @author E.Santoboni
 */
public class ListAttributeManager extends AbstractAttributeManager {

	@Override
    protected void updateAttribute(AttributeInterface attribute, AttributeTracer tracer, HttpServletRequest request) {
        List<Lang> langs = this.getLangManager().getLangs();
        for (int i = 0; i < langs.size(); i++) {
            Lang lang = langs.get(i);
            List<AttributeInterface> attributeList = ((ListAttribute) attribute).getAttributeList(lang.getCode());
            for (int j = 0; j < attributeList.size(); j++) {
                AttributeInterface attributeElement = attributeList.get(j);
                AttributeTracer elementTracer = (AttributeTracer) tracer.clone();
                elementTracer.setListElement(true);
                elementTracer.setListLang(lang);
                elementTracer.setListIndex(j);
                AbstractAttributeManager elementManager = (AbstractAttributeManager) this.getManager(attributeElement);
                if (elementManager != null) {
                    elementManager.updateAttribute(attributeElement, elementTracer, request);
                }
            }
        }
    }

}