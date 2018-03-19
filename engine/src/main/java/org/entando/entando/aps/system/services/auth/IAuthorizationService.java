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
import java.util.List;

/**
 *
 * @author paddeo
 */
public interface IAuthorizationService<T> {

    String BEAN_NAME_FOR_PAGE = "PageAuthorizationService";

    boolean isAuth(UserDetails user, T entityDto);

    boolean isAuth(UserDetails user, String entityCode);

    List<T> filterList(UserDetails user, List<T> toBeFiltered);
}
