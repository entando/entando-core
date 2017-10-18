package org.entando.entando.aps.servlet;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.entando.entando.aps.system.services.oauth2.IApiOAuth2ClientDetailManager;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2ClientDetail;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class AuthEndpointServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OAuthAuthzRequest oauthRequest = null;
        OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

        try {

            oauthRequest = new OAuthAuthzRequest(request);

            final String clientSecret = validateClientandReturnClientSecret(oauthRequest, request);

            //build response according to response_type
            String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE) == null ? OAuth.OAUTH_RESPONSE_TYPE : oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
            OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse
                    .authorizationResponse(request, HttpServletResponse.SC_FOUND);

            final String authorizationCode = oauthIssuerImpl.authorizationCode();
            final Long expires = 3600l;

            request.getSession().setAttribute(OAuth.OAUTH_CODE, authorizationCode);
            request.getSession().setAttribute(OAuth.OAUTH_CLIENT_ID, oauthRequest.getClientId());
            request.getSession().setAttribute(OAuth.OAUTH_EXPIRES_IN, System.currentTimeMillis() + expires);

            if (responseType.equals(ResponseType.CODE.toString())) {
                builder.setCode(authorizationCode);
            }
            if (responseType.equals(ResponseType.TOKEN.toString())) {
                builder.setAccessToken(authorizationCode);
                builder.setExpiresIn(expires);
            }

            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();

            urlParameters.add(new BasicNameValuePair(OAuth.OAUTH_GRANT_TYPE, GrantType.AUTHORIZATION_CODE.toString()));
            urlParameters.add(new BasicNameValuePair(OAuth.OAUTH_REDIRECT_URI, oauthRequest.getRedirectURI()));
            urlParameters.add(new BasicNameValuePair(OAuth.OAUTH_CODE, authorizationCode));
            urlParameters.add(new BasicNameValuePair(OAuth.OAUTH_CLIENT_ID, oauthRequest.getClientId()));
            urlParameters.add(new BasicNameValuePair(OAuth.OAUTH_CLIENT_SECRET, clientSecret));

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(oauthRequest.getRedirectURI());

            post.setHeader("User-Agent", HTTP.USER_AGENT);
            post.setHeader(OAuth.HeaderType.CONTENT_TYPE, OAuth.ContentType.URL_ENCODED);

            UrlEncodedFormEntity parameter = new UrlEncodedFormEntity(urlParameters, "UTF-8");


            post.setEntity(parameter);
            HttpResponse httpResponse = client.execute(post);


            System.out.println("Response Code : " + httpResponse.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(httpResponse.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            PrintWriter pw = response.getWriter();
            pw.print(result);
            pw.flush();
            pw.close();


        } catch (OAuthProblemException e) {

            String redirectUri = e.getRedirectUri();
            if (OAuthUtils.isEmpty(redirectUri)) {
                try {
                    throw OAuthUtils.handleBadContentTypeException("OAuth callback url needs to be provided by client!!!");
                } catch (OAuthProblemException e1) {
                    e1.printStackTrace();
                }
            }
            final OAuthResponse resp;
            try {
                resp = OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_FOUND)
                        .error(e)
                        .location(redirectUri).buildQueryMessage();
                PrintWriter pw = response.getWriter();
                pw.print(resp.getBody());
                pw.flush();
                pw.close();

            } catch (OAuthSystemException e1) {
                e1.printStackTrace();
            }
        } catch (OAuthSystemException | IOException e) {
            e.printStackTrace();
        }


    }

    private String validateClientandReturnClientSecret(final OAuthAuthzRequest oauthRequest, HttpServletRequest request) throws OAuthProblemException, ServletException {
        final IApiOAuth2ClientDetailManager clientDetailManager =
                (IApiOAuth2ClientDetailManager) ApsWebApplicationUtils.getBean(SystemConstants.OAUTH2_CLIENT_DETAIL_MANAGER, request);
        final String clientId = oauthRequest.getClientId();

        try {
            final OAuth2ClientDetail clientDetail = clientDetailManager.getApiOAuth2ClientDetail(clientId);
            if (clientDetail != null) {
                boolean check = clientDetail.getClientId().equals(oauthRequest.getClientId()) &&
                        clientDetail.getRedirectUri().equals(oauthRequest.getRedirectURI());
                if (!check) {
                    throw OAuthUtils.handleOAuthProblemException("Error in the request paramaters clientid and redirecturi");
                }
                return clientDetail.getClientSecret();
            }

        } catch (ApsSystemException e) {
            throw new ServletException("Error ", e);

        }
        return null;
    }
}
