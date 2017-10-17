package org.entando.entando.aps.system.services.oauth2;

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;
import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.services.oauth2.model.OAuth2AuthorizationCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OAuth2AuthorizationCodeDAO extends AbstractSearcherDAO implements IOAuth2AuthorizationCodeDAO {

    private static final Logger _logger = LoggerFactory.getLogger(OAuth2AuthorizationCodeDAO.class);

    @Override
    protected String getTableFieldName(String metadataFieldKey) {
        return metadataFieldKey;
    }

    @Override
    protected String getMasterTableName() {
        return "api_oauth2authorization_code";
    }

    @Override
    protected String getMasterTableIdFieldName() {
        return "authorizationCode";
    }

    @Override
    protected boolean isForceCaseInsensitiveLikeSearch() {
        return true;
    }

    @Override
    public List<String> searchOAuth2AuthorizationCodes(FieldSearchFilter[] filters) {
        List oAuth2AuthorizationCodesId = null;
        try {
            oAuth2AuthorizationCodesId = super.searchId(filters);
        } catch (Throwable t) {
            _logger.error("error in searchOAuth2AuthorizationCodes", t);
            throw new RuntimeException("error in searchOAuth2AuthorizationCodes", t);
        }
        return oAuth2AuthorizationCodesId;
    }

    @Override
    public List<String> loadOAuth2AuthorizationCodes() {
        List<String> oAuth2AuthorizationCodesId = new ArrayList<String>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            conn = this.getConnection();
            stat = conn.prepareStatement(LOAD_OAUTH2AUTHORIZATIONCODES_ID);
            res = stat.executeQuery();
            while (res.next()) {
                final String authorizationCode = res.getString("authorizationcode");
                oAuth2AuthorizationCodesId.add(authorizationCode);
            }
        } catch (Throwable t) {
            _logger.error("Error loading OAuth2AuthorizationCode list", t);
            throw new RuntimeException("Error loading OAuth2AuthorizationCode list", t);
        } finally {
            closeDaoResources(res, stat, conn);
        }
        return oAuth2AuthorizationCodesId;
    }

    @Override
    public void insertOAuth2AuthorizationCode(OAuth2AuthorizationCode oAuth2AuthorizationCode) {
        PreparedStatement stat = null;
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.insertOAuth2AuthorizationCode(oAuth2AuthorizationCode, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error on insert oAuth2AuthorizationCode", t);
            throw new RuntimeException("Error on insert oAuth2AuthorizationCode", t);
        } finally {
            this.closeDaoResources(null, stat, conn);
        }
    }

    public void insertOAuth2AuthorizationCode(OAuth2AuthorizationCode oAuth2AuthorizationCode, Connection conn) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(ADD_OAUTH2AUTHORIZATIONCODE);
            int index = 1;
            stat.setString(index++, oAuth2AuthorizationCode.getAuthorizationCode());
            if (StringUtils.isNotBlank(oAuth2AuthorizationCode.getClientId())) {
                stat.setString(index++, oAuth2AuthorizationCode.getClientId());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if (null != oAuth2AuthorizationCode.getExpires()) {
                Timestamp expiresTimestamp = new Timestamp(oAuth2AuthorizationCode.getExpires().getTime());
                stat.setTimestamp(index++, expiresTimestamp);
            } else {
                stat.setNull(index++, Types.DATE);
            }
            stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error on insert oAuth2AuthorizationCode", t);
            throw new RuntimeException("Error on insert oAuth2AuthorizationCode", t);
        } finally {
            this.closeDaoResources(null, stat, null);
        }
    }

    @Override
    public void updateOAuth2AuthorizationCode(OAuth2AuthorizationCode oAuth2AuthorizationCode) {
        PreparedStatement stat = null;
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.updateOAuth2AuthorizationCode(oAuth2AuthorizationCode, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error updating oAuth2AuthorizationCode {}", oAuth2AuthorizationCode.getAuthorizationCode(), t);
            throw new RuntimeException("Error updating oAuth2AuthorizationCode", t);
        } finally {
            this.closeDaoResources(null, stat, conn);
        }
    }

    public void updateOAuth2AuthorizationCode(OAuth2AuthorizationCode oAuth2AuthorizationCode, Connection conn) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(UPDATE_OAUTH2AUTHORIZATIONCODE);
            int index = 1;

            if (StringUtils.isNotBlank(oAuth2AuthorizationCode.getClientId())) {
                stat.setString(index++, oAuth2AuthorizationCode.getClientId());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if (null != oAuth2AuthorizationCode.getExpires()) {
                Timestamp expiresTimestamp = new Timestamp(oAuth2AuthorizationCode.getExpires().getTime());
                stat.setTimestamp(index++, expiresTimestamp);
            } else {
                stat.setNull(index++, Types.DATE);
            }
            stat.setString(index++, oAuth2AuthorizationCode.getAuthorizationCode());
            stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error updating oAuth2AuthorizationCode {}", oAuth2AuthorizationCode.getAuthorizationCode(), t);
            throw new RuntimeException("Error updating oAuth2AuthorizationCode", t);
        } finally {
            this.closeDaoResources(null, stat, null);
        }
    }

    @Override
    public void removeOAuth2AuthorizationCode(final String authorizationCode) {
        PreparedStatement stat = null;
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.removeOAuth2AuthorizationCode(authorizationCode, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error deleting oAuth2AuthorizationCode {}", authorizationCode, t);
            throw new RuntimeException("Error deleting oAuth2AuthorizationCode", t);
        } finally {
            this.closeDaoResources(null, stat, conn);
        }
    }

    public void removeOAuth2AuthorizationCode(final String authorizationCode, Connection conn) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(DELETE_OAUTH2AUTHORIZATIONCODE);
            int index = 1;
            stat.setString(index++, authorizationCode);
            stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error deleting oAuth2AuthorizationCode {}", authorizationCode, t);
            throw new RuntimeException("Error deleting oAuth2AuthorizationCode", t);
        } finally {
            this.closeDaoResources(null, stat, null);
        }
    }

    public OAuth2AuthorizationCode loadOAuth2AuthorizationCode(final String authorizationCode) {
        OAuth2AuthorizationCode oAuth2AuthorizationCode = null;
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            conn = this.getConnection();
            oAuth2AuthorizationCode = this.loadOAuth2AuthorizationCode(authorizationCode, conn);
        } catch (Throwable t) {
            _logger.error("Error loading oAuth2AuthorizationCode with authorizationCode {}", authorizationCode, t);
            throw new RuntimeException("Error loading oAuth2AuthorizationCode with authorizationCode " + authorizationCode, t);
        } finally {
            closeDaoResources(res, stat, conn);
        }
        return oAuth2AuthorizationCode;
    }

    public OAuth2AuthorizationCode loadOAuth2AuthorizationCode(final String authorizationCode, Connection conn) {
        OAuth2AuthorizationCode oAuth2AuthorizationCode = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            stat = conn.prepareStatement(LOAD_OAUTH2AUTHORIZATIONCODE);
            int index = 1;
            stat.setString(index++, authorizationCode);
            res = stat.executeQuery();
            if (res.next()) {
                oAuth2AuthorizationCode = this.buildOAuth2AuthorizationCodeFromRes(res);
            }
        } catch (Throwable t) {
            _logger.error("Error loading oAuth2AuthorizationCode with authorizationCode {}", authorizationCode, t);
            throw new RuntimeException("Error loading oAuth2AuthorizationCode with authorizationCode " + authorizationCode, t);
        } finally {
            closeDaoResources(res, stat, null);
        }
        return oAuth2AuthorizationCode;
    }

    protected OAuth2AuthorizationCode buildOAuth2AuthorizationCodeFromRes(ResultSet res) {
        OAuth2AuthorizationCode oAuth2AuthorizationCode = null;
        try {
            oAuth2AuthorizationCode = new OAuth2AuthorizationCode();
            oAuth2AuthorizationCode.setAuthorizationCode(res.getString("authorizationcode"));
            oAuth2AuthorizationCode.setClientId(res.getString("clientid"));
            Timestamp expiresValue = res.getTimestamp("expires");
            if (null != expiresValue) {
                oAuth2AuthorizationCode.setExpires(new Date(expiresValue.getTime()));
            }
        } catch (Throwable t) {
            _logger.error("Error in buildOAuth2AuthorizationCodeFromRes", t);
        }
        return oAuth2AuthorizationCode;
    }

    private static final String ADD_OAUTH2AUTHORIZATIONCODE = "INSERT INTO api_oauth2authorization_code (authorizationcode, clientid, expires ) VALUES (?, ?, ? )";

    private static final String UPDATE_OAUTH2AUTHORIZATIONCODE = "UPDATE api_oauth2authorization_code SET  clientid=?, expires=? WHERE authorizationcode = ?";

    private static final String DELETE_OAUTH2AUTHORIZATIONCODE = "DELETE FROM api_oauth2authorization_code WHERE authorizationcode = ?";

    private static final String LOAD_OAUTH2AUTHORIZATIONCODE = "SELECT authorizationcode, clientid, expires  FROM api_oauth2authorization_code WHERE authorizationcode = ?";

    private static final String LOAD_OAUTH2AUTHORIZATIONCODES_ID = "SELECT authorizationcode FROM api_oauth2authorization_code";

}