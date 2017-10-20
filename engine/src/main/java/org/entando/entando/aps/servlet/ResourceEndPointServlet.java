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

public class ResourceEndPointServlet extends HttpServlet {

    private static final Logger _logger = LoggerFactory.getLogger(ResourceEndPointServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Make the OAuth Request out of this request
        try {
            OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(req, ParameterStyle.HEADER, ParameterStyle.QUERY, ParameterStyle.BODY);

            // Get the access token
            String accessToken = oauthRequest.getAccessToken();


            IApiOAuth2TokenManager tokenManager = (IApiOAuth2TokenManager) ApsWebApplicationUtils.getBean(IApiOAuth2TokenManager.BEAN_NAME, req);
            final OAuth2Token token = tokenManager.getApiOAuth2Token(accessToken);
            if (token != null) {

                // Validate the access token
                if (!token.getAccessToken().equals(accessToken)) {

                    // Return the OAuth error message
                    OAuthResponse oauthResponse = OAuthRSResponse
                            .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                            .setRealm(token.getClientId())
                            .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                            .buildHeaderMessage();

                    resp.setHeader(OAuth.HeaderType.WWW_AUTHENTICATE,
                            oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));

                    this.responseHandler(resp, HttpServletResponse.SC_UNAUTHORIZED, OAuthError.ResourceResponse.INVALID_TOKEN);

                }
                // check if access token is expired
                else if (token.getExpiresIn().getTime() < System.currentTimeMillis()) {
                    // Return the OAuth error message
                    OAuthResponse oauthResponse = OAuthRSResponse
                            .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                            .setRealm(token.getClientId())
                            .setError(OAuthError.ResourceResponse.EXPIRED_TOKEN)
                            .buildHeaderMessage();

                    resp.setHeader(OAuth.HeaderType.WWW_AUTHENTICATE,
                            oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));

                    this.responseHandler(resp, HttpServletResponse.SC_UNAUTHORIZED, OAuthError.ResourceResponse.EXPIRED_TOKEN);
                } else {
                    //this.responseHandler(resp, HttpServletResponse.SC_OK, accessToken);
                    resp.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
                    resp.setHeader("Location", req.getRequestURI());
                }
            }


        } catch (OAuthSystemException | ApsSystemException ex) {
            _logger.error("System exception {} ", ex.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (OAuthProblemException ex) {
            _logger.error("OAuth2 error {} ", ex.getMessage());
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    private void responseHandler(HttpServletResponse resp, int status, String body) throws IOException {
        resp.setStatus(status);
        PrintWriter pw = resp.getWriter();
        pw.print(body);
        pw.flush();
        pw.close();

    }
}
