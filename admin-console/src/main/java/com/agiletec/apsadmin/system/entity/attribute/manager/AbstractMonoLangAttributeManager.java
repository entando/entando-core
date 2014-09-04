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

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;

/**
 * Base abstract class for those manager which handle mono-language simple attributes.
 * @author E.Santoboni
 */
public abstract class AbstractMonoLangAttributeManager extends AbstractAttributeManager {

	@Override
    protected void updateAttribute(AttributeInterface attribute, AttributeTracer tracer, HttpServletRequest request) {
        String value = this.getValueFromForm(attribute, tracer, request);
        if (value != null) {
            if (value.trim().length() == 0) {
                value = null;
            }
            this.setValue(attribute, value);
        }
    }

    /**.
     * Set the value of the specified attribute.
     *
     * @param attribute The current attribute (simple or composed) to assign the value to.
     * @param value The value to assign to the attribute.
     */
    protected abstract void setValue(AttributeInterface attribute, String value);

}
