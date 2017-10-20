package org.entando.entando.aps.servlet;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.entando.entando.aps.system.services.oauth2.IApiOAuth2TokenManager;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

    public class AuthorizationOAuth2Servlet extends HttpServlet {

    private static final Logger _logger = LoggerFactory.getLogger(AuthorizationOAuth2Servlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Make the OAuth Request out of this request
        try {
            OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(req, ParameterStyle.HEADER, ParameterStyle.BODY, ParameterStyle.BODY);

            // Get the access token
            String accessToken = oauthRequest.getAccessToken();

            IApiOAuth2TokenManager tokenManager = (IApiOAuth2TokenManager) ApsWebApplicationUtils.getBean(IApiOAuth2TokenManager.BEAN_NAME, req);
            final OAuth2Token token = tokenManager.getApiOAuth2Token(accessToken);


            // Validate the access token
            /* @Todo da implementare */
            if (!token.getAccessToken().equals(accessToken)) {

                // Return the OAuth error message
                OAuthResponse oauthResponse = OAuthRSResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        //.setRealm(TestContent.RESOURCE_SERVER_NAME)
                        .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                        .buildHeaderMessage();


                resp.setHeader(OAuth.HeaderType.WWW_AUTHENTICATE,
                        oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));

                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);

            }
            resp.setStatus(HttpServletResponse.SC_OK);
            PrintWriter pw = resp.getWriter();
            pw.print(accessToken);
            pw.flush();
            pw.close();

            //return Response.status(Response.Status.OK).entity(accessToken).build();

        } catch (OAuthSystemException | ApsSystemException ex) {
            _logger.error("System exception {} ", ex.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (OAuthProblemException ex) {
            _logger.error("OAuth2 error {} ", ex.getMessage());
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }
}
