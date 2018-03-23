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
package org.entando.entando.aps.system.services.auth;

import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.web.common.model.PagedMetadata;

/**
 *
 * @author paddeo
 */
public abstract class AbstractAuthorizationService<T> implements IAuthorizationService<T> {

    public PagedMetadata<T> filterList(UserDetails user, PagedMetadata<T> metadata) {
        metadata.setBody(this.filterList(user, metadata.getBody()));
        return metadata;
    }

}
