package org.entando.entando.aps.servlet;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.entando.entando.aps.system.services.oauth2.IApiAuthorizationCodeManager;
import org.entando.entando.aps.system.services.oauth2.IApiOAuth2ClientDetailManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TokenEndPointServlet extends HttpServlet {

    private static final Logger _logger = LoggerFactory.getLogger(TokenEndPointServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        OAuthTokenRequest oauthRequest = null;
        try {
            oauthRequest = new OAuthTokenRequest(request);
            if (this.validateClient(oauthRequest, request, response)) {

                OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
                final String accessToken = oauthIssuerImpl.accessToken();
                final String refreshToken = oauthIssuerImpl.refreshToken();

                OAuthResponse r = OAuthASResponse
                        .tokenResponse(HttpServletResponse.SC_OK)
                        .setAccessToken(accessToken)
                        .setExpiresIn("3600")
                        .setRefreshToken(refreshToken)
                        .buildJSONMessage();

                response.setStatus(r.getResponseStatus());
                PrintWriter pw = response.getWriter();
                pw.print(r.getBody());
                pw.flush();
                pw.close();
            } else {
                _logger.warn("OAuth2 authentication failed");
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }


        } catch (OAuthProblemException e) {
            _logger.error("OAuthProblemException exception {} ", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        } catch (OAuthSystemException e) {
            _logger.error("OAuthSystemException exception {} ", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Throwable throwable) {
            _logger.error("Throwable exception {} ", throwable.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    private boolean validateClient(OAuthTokenRequest oauthRequest, HttpServletRequest request, HttpServletResponse response) throws Throwable {

        IApiOAuth2ClientDetailManager clientDetailManager = (IApiOAuth2ClientDetailManager) ApsWebApplicationUtils.getBean(SystemConstants.OAUTH2_CLIENT_DETAIL_MANAGER, request);
        IApiAuthorizationCodeManager codeManager = (IApiAuthorizationCodeManager) ApsWebApplicationUtils.getBean(SystemConstants.OAUTH2_AUTHORIZATION_CODE_MANAGER, request);

        final String clientId = oauthRequest.getClientId();
        final String authCode = oauthRequest.getParam(OAuth.OAUTH_CODE);
        final String clientSecret = oauthRequest.getClientSecret();

        //do checking for different grant types
        if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE)
                .equals(GrantType.AUTHORIZATION_CODE.toString())) {

            if (!codeManager.verifyAccess(clientId, clientSecret, clientDetailManager)) {
                _logger.error("OAuth2 authentication failed");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            } else if (!codeManager.verifyCode(authCode, request.getRemoteAddr())) {
                _logger.error("OAuth2 authcode does not match or the source of client is different");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }
        return true;
    }


}
