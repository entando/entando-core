package org.entando.entando.aps.util;

import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.web.common.exceptions.EntandoAuthorizationException;
import org.entando.entando.web.common.exceptions.EntandoTokenException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class HttpSessionHelper {

    private static final String USER_KEY = "user";

    /**
     * extracts the logged user from the session
     * if there is no user in session an EntandoTokenException is thrown
     *
     * @param request the HttpServletRequest from which extract the current logged user
     * @return the current logged user cast to UserDetails
     * @throws EntandoTokenException if there is no user in session
     */
    public static UserDetails extractCurrentUser(HttpServletRequest request) {

        Object userObj = request.getSession().getAttribute(USER_KEY);

        if (! (userObj instanceof UserDetails)) {
            throw new EntandoTokenException("", request, "");
        }

        return (UserDetails) userObj;
    }


    /**
     * extracts the logged user from the session
     * if there is no user in session an EntandoTokenException is thrown
     *
     * @param session the HttpSession from which extract the current logged user
     * @return the current logged user cast to UserDetails
     * @throws EntandoTokenException if there is no user in session
     */
    public static UserDetails extractCurrentUser(HttpSession session) {

        return (UserDetails) session.getAttribute(USER_KEY);
    }
}
