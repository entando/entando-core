/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.apsadmin.portal.helper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.apsadmin.system.BaseAction;
import org.springframework.beans.factory.annotation.Autowired;

public class PageActionReferencesHelper implements IPageActionReferencesHelper {

    private static final Logger _logger = LoggerFactory.getLogger(PageActionReferencesHelper.class);

    @Autowired(required = false)
    private List<IExternalPageValidator> externalValidators;

    @Override
    public boolean checkForSetOnline(IPage page, BaseAction action) {
        boolean isValid = true;
        if (null != this.getExternalValidators()) {
            for (IExternalPageValidator externalValidator : this.getExternalValidators()) {
                boolean result = externalValidator.checkForSetOnline(page, action);
                if (!result) {
                    isValid = false;
                }
            }
        }
        return isValid;
    }

    protected List<IExternalPageValidator> getExternalValidators() {
        return externalValidators;
    }

    public void setExternalValidators(List<IExternalPageValidator> externalValidators) {
        this.externalValidators = externalValidators;
    }

}
