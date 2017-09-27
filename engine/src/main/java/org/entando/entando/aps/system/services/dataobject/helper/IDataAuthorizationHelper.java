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
package org.entando.entando.aps.system.services.dataobject.helper;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;

/**
 * Return informations of content authorization
 *
 * @author E.Santoboni
 */
public interface IDataAuthorizationHelper {

    /**
     * Return true if the given user can view the given content.
     *
     * @param user The user
     * @param content The content.
     * @return True if the given user can view the content.
     * @throws ApsSystemException In case of error
     */
    public boolean isAuth(UserDetails user, DataObject content) throws ApsSystemException;

    /**
     * Return true if the given user can view the given content.
     *
     * @param user The user
     * @param dataId The content id.
     * @param publicVersion true if the control should be carried out in the
     * public version
     * @return True if the given user can view the content.
     * @throws ApsSystemException In case of error
     */
    public boolean isAuth(UserDetails user, String dataId, boolean publicVersion) throws ApsSystemException;

    /**
     * Return true if the given user can view the given content.
     *
     * @param user The user
     * @param info The content authorization info.
     * @return True if the given user can view the content.
     * @throws ApsSystemException In case of error
     */
    public boolean isAuth(UserDetails user, PublicDataTypeAuthorizationInfo info) throws ApsSystemException;

    /**
     * Return true if the given user can edit the given content.
     *
     * @param user The user
     * @param content The content.
     * @return True if the given user can edit the content.
     * @throws ApsSystemException In case of error
     */
    public boolean isAuthToEdit(UserDetails user, DataObject content) throws ApsSystemException;

    /**
     * Return true if the given user can edit the given content.
     *
     * @param user The user
     * @param dataId The content id.
     * @param publicVersion true if the control should be carried out in the
     * public version
     * @return True if the given user can edit the content.
     * @throws ApsSystemException In case of error
     */
    public boolean isAuthToEdit(UserDetails user, String dataId, boolean publicVersion) throws ApsSystemException;

    /**
     * Return true if the given user can edit the given content.
     *
     * @param user The user
     * @param info The content authorization info.
     * @return True if the given user can edit the content.
     * @throws ApsSystemException In case of error
     */
    public boolean isAuthToEdit(UserDetails user, PublicDataTypeAuthorizationInfo info) throws ApsSystemException;

    /**
     * Return the object that contains the authorization info of the content.
     *
     * @param dataId The content that extract the info.
     * @return The authorization info.
     */
    public PublicDataTypeAuthorizationInfo getAuthorizationInfo(String dataId);

    public PublicDataTypeAuthorizationInfo getAuthorizationInfo(String dataId, boolean cacheable);

}
