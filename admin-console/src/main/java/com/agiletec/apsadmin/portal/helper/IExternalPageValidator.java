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
package com.agiletec.apsadmin.portal.helper;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * @author E.Santoboni
 */
public interface IExternalPageValidator {

    public void checkPageGroup(IPage page, boolean draftPageHepler, BaseAction currentAction);

    public boolean checkForSetOnline(IPage page, BaseAction action);

}
