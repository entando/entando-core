/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
