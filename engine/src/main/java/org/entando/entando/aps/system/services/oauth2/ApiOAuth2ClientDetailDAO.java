/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.aps.system.services.oauth2;

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;
import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.services.oauth2.model.ApiOAuth2ClientDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ApiOAuth2ClientDetailDAO extends AbstractSearcherDAO implements IApiOAuth2ClientDetailDAO {

    private static final Logger _logger = LoggerFactory.getLogger(ApiOAuth2ClientDetailDAO.class);

    @Override
    protected String getTableFieldName(String metadataFieldKey) {
        return metadataFieldKey;
    }

    @Override
    protected String getMasterTableName() {
        return "jpapioauth2clientdetail_apioauth2clientdetail";
    }

    @Override
    protected String getMasterTableIdFieldName() {
        return "id";
    }

    @Override
    protected boolean isForceCaseInsensitiveLikeSearch() {
        return true;
    }

    @Override
    public List<Integer> searchApiOAuth2ClientDetails(FieldSearchFilter[] filters) {
        List apiOAuth2ClientDetailsId = null;
        try {
            apiOAuth2ClientDetailsId = super.searchId(filters);
        } catch (Throwable t) {
            _logger.error("error in searchApiOAuth2ClientDetails", t);
            throw new RuntimeException("error in searchApiOAuth2ClientDetails", t);
        }
        return apiOAuth2ClientDetailsId;
    }

    @Override
    public List<Integer> loadApiOAuth2ClientDetails() {
        List<Integer> apiOAuth2ClientDetailsId = new ArrayList<Integer>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            conn = this.getConnection();
            stat = conn.prepareStatement(LOAD_APIOAUTH2CLIENTDETAILS_ID);
            res = stat.executeQuery();
            while (res.next()) {
                int id = res.getInt("id");
                apiOAuth2ClientDetailsId.add(id);
            }
        } catch (Throwable t) {
            _logger.error("Error loading ApiOAuth2ClientDetail list", t);
            throw new RuntimeException("Error loading ApiOAuth2ClientDetail list", t);
        } finally {
            closeDaoResources(res, stat, conn);
        }
        return apiOAuth2ClientDetailsId;
    }

    @Override
    public void insertApiOAuth2ClientDetail(ApiOAuth2ClientDetail apiOAuth2ClientDetail) {
        PreparedStatement stat = null;
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.insertApiOAuth2ClientDetail(apiOAuth2ClientDetail, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error on insert apiOAuth2ClientDetail", t);
            throw new RuntimeException("Error on insert apiOAuth2ClientDetail", t);
        } finally {
            this.closeDaoResources(null, stat, conn);
        }
    }

    public void insertApiOAuth2ClientDetail(ApiOAuth2ClientDetail apiOAuth2ClientDetail, Connection conn) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(ADD_APIOAUTH2CLIENTDETAIL);
            int index = 1;
            stat.setInt(index++, apiOAuth2ClientDetail.getId());
            if (StringUtils.isNotBlank(apiOAuth2ClientDetail.getName())) {
                stat.setString(index++, apiOAuth2ClientDetail.getName());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if (StringUtils.isNotBlank(apiOAuth2ClientDetail.getClientId())) {
                stat.setString(index++, apiOAuth2ClientDetail.getClientId());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if (StringUtils.isNotBlank(apiOAuth2ClientDetail.getClientSecret())) {
                stat.setString(index++, apiOAuth2ClientDetail.getClientSecret());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if (StringUtils.isNotBlank(apiOAuth2ClientDetail.getRedirectUri())) {
                stat.setString(index++, apiOAuth2ClientDetail.getRedirectUri());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if (StringUtils.isNotBlank(apiOAuth2ClientDetail.getClientUri())) {
                stat.setString(index++, apiOAuth2ClientDetail.getClientUri());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if (StringUtils.isNotBlank(apiOAuth2ClientDetail.getDescription())) {
                stat.setString(index++, apiOAuth2ClientDetail.getDescription());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if (StringUtils.isNotBlank(apiOAuth2ClientDetail.getIconUri())) {
                stat.setString(index++, apiOAuth2ClientDetail.getIconUri());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if (null != apiOAuth2ClientDetail.getIssuedAt()) {
                Timestamp issuedAtTimestamp = new Timestamp(apiOAuth2ClientDetail.getIssuedAt().getTime());
                stat.setTimestamp(index++, issuedAtTimestamp);
            } else {
                stat.setNull(index++, Types.DATE);
            }
            if (null != apiOAuth2ClientDetail.getExpiresIn()) {
                Timestamp expiresInTimestamp = new Timestamp(apiOAuth2ClientDetail.getExpiresIn().getTime());
                stat.setTimestamp(index++, expiresInTimestamp);
            } else {
                stat.setNull(index++, Types.DATE);
            }
            stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error on insert apiOAuth2ClientDetail", t);
            throw new RuntimeException("Error on insert apiOAuth2ClientDetail", t);
        } finally {
            this.closeDaoResources(null, stat, null);
        }
    }

    @Override
    public void updateApiOAuth2ClientDetail(ApiOAuth2ClientDetail apiOAuth2ClientDetail) {
        PreparedStatement stat = null;
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.updateApiOAuth2ClientDetail(apiOAuth2ClientDetail, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error updating apiOAuth2ClientDetail {}", apiOAuth2ClientDetail.getId(), t);
            throw new RuntimeException("Error updating apiOAuth2ClientDetail", t);
        } finally {
            this.closeDaoResources(null, stat, conn);
        }
    }

    public void updateApiOAuth2ClientDetail(ApiOAuth2ClientDetail apiOAuth2ClientDetail, Connection conn) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(UPDATE_APIOAUTH2CLIENTDETAIL);
            int index = 1;

            if (StringUtils.isNotBlank(apiOAuth2ClientDetail.getName())) {
                stat.setString(index++, apiOAuth2ClientDetail.getName());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if (StringUtils.isNotBlank(apiOAuth2ClientDetail.getClientId())) {
                stat.setString(index++, apiOAuth2ClientDetail.getClientId());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if (StringUtils.isNotBlank(apiOAuth2ClientDetail.getClientSecret())) {
                stat.setString(index++, apiOAuth2ClientDetail.getClientSecret());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if (StringUtils.isNotBlank(apiOAuth2ClientDetail.getRedirectUri())) {
                stat.setString(index++, apiOAuth2ClientDetail.getRedirectUri());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if (StringUtils.isNotBlank(apiOAuth2ClientDetail.getClientUri())) {
                stat.setString(index++, apiOAuth2ClientDetail.getClientUri());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if (StringUtils.isNotBlank(apiOAuth2ClientDetail.getDescription())) {
                stat.setString(index++, apiOAuth2ClientDetail.getDescription());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if (StringUtils.isNotBlank(apiOAuth2ClientDetail.getIconUri())) {
                stat.setString(index++, apiOAuth2ClientDetail.getIconUri());
            } else {
                stat.setNull(index++, Types.VARCHAR);
            }
            if (null != apiOAuth2ClientDetail.getIssuedAt()) {
                Timestamp issuedAtTimestamp = new Timestamp(apiOAuth2ClientDetail.getIssuedAt().getTime());
                stat.setTimestamp(index++, issuedAtTimestamp);
            } else {
                stat.setNull(index++, Types.DATE);
            }
            if (null != apiOAuth2ClientDetail.getExpiresIn()) {
                Timestamp expiresInTimestamp = new Timestamp(apiOAuth2ClientDetail.getExpiresIn().getTime());
                stat.setTimestamp(index++, expiresInTimestamp);
            } else {
                stat.setNull(index++, Types.DATE);
            }
            stat.setInt(index++, apiOAuth2ClientDetail.getId());
            stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error updating apiOAuth2ClientDetail {}", apiOAuth2ClientDetail.getId(), t);
            throw new RuntimeException("Error updating apiOAuth2ClientDetail", t);
        } finally {
            this.closeDaoResources(null, stat, null);
        }
    }

    @Override
    public void removeApiOAuth2ClientDetail(int id) {
        PreparedStatement stat = null;
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.removeApiOAuth2ClientDetail(id, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error deleting apiOAuth2ClientDetail {}", id, t);
            throw new RuntimeException("Error deleting apiOAuth2ClientDetail", t);
        } finally {
            this.closeDaoResources(null, stat, conn);
        }
    }

    public void removeApiOAuth2ClientDetail(int id, Connection conn) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(DELETE_APIOAUTH2CLIENTDETAIL);
            int index = 1;
            stat.setInt(index++, id);
            stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error deleting apiOAuth2ClientDetail {}", id, t);
            throw new RuntimeException("Error deleting apiOAuth2ClientDetail", t);
        } finally {
            this.closeDaoResources(null, stat, null);
        }
    }

    public ApiOAuth2ClientDetail loadApiOAuth2ClientDetail(int id) {
        ApiOAuth2ClientDetail apiOAuth2ClientDetail = null;
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            conn = this.getConnection();
            apiOAuth2ClientDetail = this.loadApiOAuth2ClientDetail(id, conn);
        } catch (Throwable t) {
            _logger.error("Error loading apiOAuth2ClientDetail with id {}", id, t);
            throw new RuntimeException("Error loading apiOAuth2ClientDetail with id " + id, t);
        } finally {
            closeDaoResources(res, stat, conn);
        }
        return apiOAuth2ClientDetail;
    }

    public ApiOAuth2ClientDetail loadApiOAuth2ClientDetail(int id, Connection conn) {
        ApiOAuth2ClientDetail apiOAuth2ClientDetail = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            stat = conn.prepareStatement(LOAD_APIOAUTH2CLIENTDETAIL);
            int index = 1;
            stat.setInt(index++, id);
            res = stat.executeQuery();
            if (res.next()) {
                apiOAuth2ClientDetail = this.buildApiOAuth2ClientDetailFromRes(res);
            }
        } catch (Throwable t) {
            _logger.error("Error loading apiOAuth2ClientDetail with id {}", id, t);
            throw new RuntimeException("Error loading apiOAuth2ClientDetail with id " + id, t);
        } finally {
            closeDaoResources(res, stat, null);
        }
        return apiOAuth2ClientDetail;
    }

    protected ApiOAuth2ClientDetail buildApiOAuth2ClientDetailFromRes(ResultSet res) {
        ApiOAuth2ClientDetail apiOAuth2ClientDetail = null;
        try {
            apiOAuth2ClientDetail = new ApiOAuth2ClientDetail();
            apiOAuth2ClientDetail.setId(res.getInt("id"));
            apiOAuth2ClientDetail.setName(res.getString("name"));
            apiOAuth2ClientDetail.setClientId(res.getString("clientid"));
            apiOAuth2ClientDetail.setClientSecret(res.getString("clientsecret"));
            apiOAuth2ClientDetail.setRedirectUri(res.getString("redirecturi"));
            apiOAuth2ClientDetail.setClientUri(res.getString("clienturi"));
            apiOAuth2ClientDetail.setDescription(res.getString("description"));
            apiOAuth2ClientDetail.setIconUri(res.getString("iconuri"));
            Timestamp issuedAtValue = res.getTimestamp("issuedat");
            if (null != issuedAtValue) {
                apiOAuth2ClientDetail.setIssuedAt(new Date(issuedAtValue.getTime()));
            }
            Timestamp expiresInValue = res.getTimestamp("expiresin");
            if (null != expiresInValue) {
                apiOAuth2ClientDetail.setExpiresIn(new Date(expiresInValue.getTime()));
            }
        } catch (Throwable t) {
            _logger.error("Error in buildApiOAuth2ClientDetailFromRes", t);
        }
        return apiOAuth2ClientDetail;
    }

    private static final String ADD_APIOAUTH2CLIENTDETAIL = "INSERT INTO jpapioauth2clientdetail_apioauth2clientdetail (id, name, clientid, clientsecret, redirecturi, clienturi, description, iconuri, issuedat, expiresin ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

    private static final String UPDATE_APIOAUTH2CLIENTDETAIL = "UPDATE jpapioauth2clientdetail_apioauth2clientdetail SET  name=?,  clientid=?,  clientsecret=?,  redirecturi=?,  clienturi=?,  description=?,  iconuri=?,  issuedat=?, expiresin=? WHERE id = ?";

    private static final String DELETE_APIOAUTH2CLIENTDETAIL = "DELETE FROM jpapioauth2clientdetail_apioauth2clientdetail WHERE id = ?";

    private static final String LOAD_APIOAUTH2CLIENTDETAIL = "SELECT id, name, clientid, clientsecret, redirecturi, clienturi, description, iconuri, issuedat, expiresin  FROM jpapioauth2clientdetail_apioauth2clientdetail WHERE id = ?";

    private static final String LOAD_APIOAUTH2CLIENTDETAILS_ID = "SELECT id FROM jpapioauth2clientdetail_apioauth2clientdetail";

}