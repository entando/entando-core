/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.aps.system.services.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.aps.system.services.api.model.ApiMethod.HttpMethod;
import org.entando.entando.aps.system.services.api.model.ApiResource;
import org.entando.entando.aps.system.services.api.model.ApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.util.ApsProperties;

/**
 * @author E.Santoboni
 */
public class ApiCatalogDAO extends AbstractDAO implements IApiCatalogDAO {
	
	private static final Logger _logger =  LoggerFactory.getLogger(ApiCatalogDAO.class);
	
	@Override
    public void loadApiStatus(Map<String, ApiResource> resources) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet res = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            stat = conn.prepareStatement(LOAD_API_STATUS);
            //resourcecode, httpmethod, isactive, authenticationrequired, authorizationrequired
            //"SELECT method, isactive FROM apicatalog_status";
            res = stat.executeQuery();
            while (res.next()) {
                String resourceCode = res.getString("resourcecode");
                String httpMethodString = res.getString("httpmethod");
                ApiMethod.HttpMethod httpMethod = Enum.valueOf(ApiMethod.HttpMethod.class, httpMethodString.toUpperCase());
                ApiMethod method = null;
                ApiResource resource = resources.get(resourceCode);
                if (null != resource) {
                    method = resource.getMethod(httpMethod);
                }
                if (null == method) {
                    this.resetApiStatus(resourceCode, httpMethod, conn);
                    continue;
                }
                boolean active = (res.getInt("isactive") == 1);
                method.setStatus(active);
                boolean authenticationRequired = (res.getInt("authenticationrequired") == 1);
                method.setRequiredAuth(authenticationRequired);
                String requiredPermission = res.getString("authorizationrequired");
                if (null != requiredPermission && requiredPermission.trim().length() > 0) {
                    method.setRequiredPermission(requiredPermission);
                } else {
                    method.setRequiredPermission(null);
                }
				boolean hidden = (res.getInt("ishidden") == 1);
                method.setHidden(hidden);
            }
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error while loading api status",  t);
			throw new RuntimeException("Error while loading api status", t);
			//processDaoException(t, "Error while loading api status ", "loadApiStatus");
        } finally {
            closeDaoResources(res, stat, conn);
        }
    }
	
    @Override
    public void resetApiStatus(String resourceCode, HttpMethod httpMethod) {
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            this.resetApiStatus(resourceCode, httpMethod, conn);
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error resetting status : resource '{}' method '{}'",resourceCode, httpMethod.toString(), t);
			throw new RuntimeException("Error resetting status : resource '" + resourceCode + "' method " + httpMethod.toString(), t);
			//processDaoException(t, "Error resetting status : resource '" + resourceCode + "' method " + httpMethod.toString(), "resetApiStatus");
        } finally {
            closeConnection(conn);
        }
    }
	
    protected void resetApiStatus(String resourceCode, HttpMethod httpMethod, Connection conn) {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(RESET_API_STATUS);
            stat.setString(1, resourceCode);
            stat.setString(2, httpMethod.toString());
            stat.executeUpdate();
        } catch (Throwable t) {
            _logger.error("Error resetting status : resource '{}' method '{}'",resourceCode, httpMethod.toString(), t);
			throw new RuntimeException("Error resetting status : resource '" + resourceCode + "' method " + httpMethod.toString(), t);
            //processDaoException(t, "Error resetting status : resource '" + resourceCode + "' method " + httpMethod.toString(), "resetApiStatus");
        } finally {
            closeDaoResources(null, stat);
        }
    }
	
    @Override
    public void saveApiStatus(ApiMethod method) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
			String resourceCode = ApiResource.getCode(method.getNamespace(), method.getResourceName());
            this.resetApiStatus(resourceCode, method.getHttpMethod(), conn);
            stat = conn.prepareStatement(SAVE_API_STATUS);
            int isActive = (method.isActive()) ? 1 : 0;
            stat.setString(1, resourceCode);
            stat.setString(2, method.getHttpMethod().toString());
            stat.setInt(3, isActive);
            int authentication = (method.getRequiredAuth()) ? 1 : 0;
            stat.setInt(4, authentication);
            if (null != method.getRequiredPermission() && method.getRequiredPermission().trim().length() > 0) {
				stat.setString(5, method.getRequiredPermission());
			} else {
				stat.setNull(5, Types.VARCHAR);
			}
			int isHidden = (null != method.getHidden() && method.getHidden()) ? 1 : 0;
			stat.setInt(6, isHidden);
            stat.executeUpdate();
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error while saving api status",  t);
			throw new RuntimeException("Error while saving api status", t);
			//processDaoException(t, "Error while saving api status", "saveApiStatus");
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }
	
    @Deprecated
    @Override
    public Map<String, ApiService> loadServices(Map<String, ApiMethod> methods) {
        return this.loadServices(new ArrayList<ApiMethod>(methods.values()));
    }
	
    @Override
    public Map<String, ApiService> loadServices(List<ApiMethod> methods) {
        Map<String, ApiMethod> methodMap = new HashMap<String, ApiMethod>();
        for (int i = 0; i < methods.size(); i++) {
            ApiMethod method = methods.get(i);
			String resourceCode = ApiResource.getCode(method.getNamespace(), method.getResourceName());
			methodMap.put(resourceCode, method);
        }
        Map<String, ApiService> services = new HashMap<String, ApiService>();
        Connection conn = null;
        Statement stat = null;
        ResultSet res = null;
        List<String> invalidServices = new ArrayList<String>();
        try {
            conn = this.getConnection();
            stat = conn.createStatement();
            res = stat.executeQuery(LOAD_SERVICES);
            while (res.next()) {
                this.buildService(methodMap, services, invalidServices, res);
            }
        } catch (Throwable t) {
            _logger.error("Error while loading services", t);
			throw new RuntimeException("Error while loading services", t);
			//processDaoException(t, "Error while loading services", "loadServices");
        } finally {
            closeDaoResources(res, stat, conn);
        }
        return services;
    }
	
    private void buildService(Map<String, ApiMethod> methods,
            Map<String, ApiService> services, List<String> invalidServices, ResultSet res) {
        String key = null;
        try {
            key = res.getString(1);
            String parentCode = res.getString(2);
            ApiMethod masterMethod = methods.get(parentCode);
            if (null != masterMethod) {
                ApsProperties description = new ApsProperties();
                description.loadFromXml(res.getString(3));
                ApsProperties parameters = new ApsProperties();
                parameters.loadFromXml(res.getString(4));
                String tag = res.getString(5);
                String[] freeParameters = null;
                String freeParamString = res.getString(6);
                if (null != freeParamString && freeParamString.trim().length() > 0) {
                    ServiceExtraConfigDOM dom = new ServiceExtraConfigDOM(freeParamString);
                    freeParameters = dom.extractFreeParameters();
                }
                boolean isActive = (1 == res.getInt(7)) ? true : false;
                boolean isHidden = (1 == res.getInt(8)) ? true : false;
                boolean isMyEntando = (1 == res.getInt(9)) ? true : false;
                ApiService apiService = new ApiService(key, description, masterMethod,
                        parameters, freeParameters, tag, !isHidden, isActive, isMyEntando);
				boolean authRequired = (1 == res.getInt(10)) ? true : false;
				apiService.setRequiredAuth(authRequired);
				String requiredPermission = res.getString(11);
				if (null != requiredPermission && requiredPermission.trim().length() > 0) {
					apiService.setRequiredPermission(requiredPermission);
				}
				String requiredGroup = res.getString(12);
				if (null != requiredGroup && requiredGroup.trim().length() > 0) {
					apiService.setRequiredGroup(requiredGroup);
				}
                services.put(key, apiService);
            } else {
                invalidServices.add(key);
            }
        } catch (Throwable t) {
        	_logger.error("Error building service - key '{}'",key, t);
            //ApsSystemUtils.logThrowable(t, this, "buildService", "Error building service - key '" + key + "'");
        }
    }

    @Override
    public void addService(ApiService service) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            stat = conn.prepareStatement(ADD_SERVICE);
            stat.setString(1, service.getKey());
			this.valorizeStatement(service, stat, 1);
            stat.executeUpdate();
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error while adding a service",  t);
			throw new RuntimeException("Error while adding a service", t);
			//processDaoException(t, "Error while adding a service", "addService");
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }

    @Override
    public void updateService(ApiService service) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            stat = conn.prepareStatement(UPDATE_SERVICE);
			int index = this.valorizeStatement(service, stat, 0);
            stat.setString(++index, service.getKey());
            stat.executeUpdate();
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error while updating a service",  t);
			throw new RuntimeException("Error while updating a service", t);
			//processDaoException(t, "Error while updating a service", "updateService");
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }
	
	private int valorizeStatement(ApiService service, PreparedStatement stat, int index) throws Throwable {
		String resourceCode = ApiResource.getCode(service.getMaster().getNamespace(), service.getMaster().getResourceName());
		stat.setString(++index, resourceCode);
		stat.setString(++index, service.getDescription().toXml());
		stat.setString(++index, service.getParameters().toXml());
		stat.setString(++index, service.getTag());
		if (null == service.getFreeParameters() || service.getFreeParameters().length == 0) {
			stat.setNull(++index, Types.VARCHAR);
		} else {
			ServiceExtraConfigDOM dom = new ServiceExtraConfigDOM();
			stat.setString(++index, dom.extractXml(service.getFreeParameters()));
		}
		int isActive = (service.isActive()) ? 1 : 0;
		stat.setInt(++index, isActive);
		int isHidden = (service.isHidden()) ? 1 : 0;
		stat.setInt(++index, isHidden);
		int isMyEntando = (service.isMyEntando()) ? 1 : 0;
		stat.setInt(++index, isMyEntando);
		int authRequired = (service.getRequiredAuth()) ? 1 : 0;
		stat.setInt(++index, authRequired);
		if (null != service.getRequiredPermission() && service.getRequiredPermission().trim().length() > 0) {
			stat.setString(++index, service.getRequiredPermission());
		} else {
			stat.setNull(++index, Types.VARCHAR);
		}
		if (null != service.getRequiredGroup() && service.getRequiredGroup().trim().length() > 0) {
			stat.setString(++index, service.getRequiredGroup());
		} else {
			stat.setNull(++index, Types.VARCHAR);
		}
		return index;
    }

    @Override
    public void deleteService(String key) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false);
            stat = conn.prepareStatement(DELETE_SERVICE);
            stat.setString(1, key);
            stat.executeUpdate();
            conn.commit();
        } catch (Throwable t) {
            this.executeRollback(conn);
            _logger.error("Error while deleting service {}", key,  t);
			throw new RuntimeException("Error while deleting a service", t);
			//processDaoException(t, "Error while deleting a service", "deleteService");
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }
    
    private static final String LOAD_API_STATUS =
            "SELECT resourcecode, httpmethod, isactive, authenticationrequired, authorizationrequired, ishidden "
            + "FROM apicatalog_methods";
    
    private static final String SAVE_API_STATUS =
            "INSERT INTO apicatalog_methods(resourcecode, httpmethod, isactive, "
            + "authenticationrequired, authorizationrequired, ishidden) VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String RESET_API_STATUS =
            "DELETE FROM apicatalog_methods WHERE resourcecode = ? AND httpmethod = ?";
    
    private static final String LOAD_SERVICES =
			"SELECT servicekey, resourcecode, description, parameters, tag, freeparameters, isactive, "
			+ "ishidden, myentando, authenticationrequired, requiredpermission, requiredgroup FROM apicatalog_services";
    
    private static final String ADD_SERVICE =
            "INSERT INTO apicatalog_services(servicekey, resourcecode, description, parameters, tag, "
			+ "freeparameters, isactive, ishidden, myentando, authenticationrequired, requiredpermission, requiredgroup) "
			+ "VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ) ";
    
    private static final String UPDATE_SERVICE =
            "UPDATE apicatalog_services SET resourcecode = ? , description = ? , parameters = ? , tag = ? , "
			+ "freeparameters = ? , isactive = ? , ishidden = ? , myentando = ? , authenticationrequired = ? , requiredpermission = ? , requiredgroup = ? WHERE servicekey = ? ";
    
    private static final String DELETE_SERVICE =
            "DELETE FROM apicatalog_services WHERE servicekey = ? ";
    
}
