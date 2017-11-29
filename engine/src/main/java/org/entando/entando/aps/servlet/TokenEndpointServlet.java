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
import org.entando.entando.aps.system.services.oauth2.IApiOAuth2TokenManager;
import org.entando.entando.aps.system.services.oauth2.IApiOAuthorizationCodeManager;
import org.entando.entando.aps.system.services.oauth2.IOAuthConsumerManager;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

public class TokenEndpointServlet extends HttpServlet {

    private static final Logger _logger = LoggerFactory.getLogger(TokenEndpointServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        try {
            IApiOAuth2TokenManager tokenManager = (IApiOAuth2TokenManager) ApsWebApplicationUtils.getBean(IApiOAuth2TokenManager.BEAN_NAME, request);

            final OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);

            if (this.validateClient(oauthRequest, request, response)) {

                OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
                final String accessToken = oauthIssuerImpl.accessToken();
                final String refreshToken = oauthIssuerImpl.refreshToken();
                int expires = 3600;

                OAuth2Token oAuth2Token = new OAuth2Token();
                oAuth2Token.setAccessToken(accessToken);
                oAuth2Token.setRefreshToken(refreshToken);
                oAuth2Token.setClientId(oauthRequest.getClientId());
                Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
                calendar.add(Calendar.SECOND, expires);
                oAuth2Token.setExpiresIn(calendar.getTime());
                oAuth2Token.setGrantType(oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE));

                tokenManager.addApiOAuth2Token(oAuth2Token, false);

                OAuthResponse r = OAuthASResponse
                        .tokenResponse(HttpServletResponse.SC_OK)
                        .setAccessToken(accessToken)
                        .setExpiresIn(Long.toString(expires))
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
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (IOException e1) {
                _logger.error("OAuthProblemException - IOException exception {} ", e1);
            }

        } catch (OAuthSystemException e) {
            _logger.error("OAuthSystemException exception {} ", e.getMessage());
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (IOException e1) {
                _logger.error("OAuthSystemException - IOException exception {} ", e1);
            }
        } catch (Throwable throwable) {
            _logger.error("Throwable exception {} ", throwable.getMessage());
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (IOException e) {
                _logger.error("Throwable - IOException exception {} ", e);
            }
        }
    }


    private boolean validateClient(OAuthTokenRequest oauthRequest, HttpServletRequest request, HttpServletResponse response) throws Throwable {

        IOAuthConsumerManager consumerManager = (IOAuthConsumerManager) ApsWebApplicationUtils.getBean(SystemConstants.OAUTH_CONSUMER_MANAGER, request);
        IApiOAuthorizationCodeManager codeManager = (IApiOAuthorizationCodeManager) ApsWebApplicationUtils.getBean(SystemConstants.OAUTH2_AUTHORIZATION_CODE_MANAGER, request);

        final String clientId = oauthRequest.getClientId();
        final String authCode = oauthRequest.getParam(OAuth.OAUTH_CODE);
        final String clientSecret = oauthRequest.getClientSecret();


        //do checking for different grant types
        if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE)
                .equals(GrantType.AUTHORIZATION_CODE.toString()) ||
                oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE)
                        .equals(GrantType.REFRESH_TOKEN.toString())

                ) {

            if (!codeManager.verifyAccess(clientId, clientSecret, consumerManager)) {
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
