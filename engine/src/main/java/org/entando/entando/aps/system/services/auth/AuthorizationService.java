/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.aps.system.services.auth;

import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.web.common.model.PagedMetadata;

/**
 *
 * @author paddeo
 */
public abstract class AuthorizationService<T> implements IAuthorizationService<T> {

    public PagedMetadata<T> filterList(UserDetails user, PagedMetadata<T> metadata) {
        metadata.setBody(this.filterList(user, metadata.getBody()));
        return metadata;
    }

}
