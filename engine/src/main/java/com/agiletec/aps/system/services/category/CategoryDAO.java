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
package com.agiletec.aps.system.services.category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.util.ApsProperties;

/**
 * Data Access Object per gli oggetti Categoria.
 * @author E.Santoboni
 */
public class CategoryDAO extends AbstractDAO implements ICategoryDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(CategoryDAO.class);
	
	/**
	 * Carica la lista delle categorie inserite nel sistema.
	 * @param langManager Il manager delle lingue.
	 * @return La lista delle categorie inserite nel sistema.
	 */
	@Override
    public List<Category> loadCategories(ILangManager langManager) {
		Connection conn = null;
		Statement stat = null;
		ResultSet res = null;
		List<Category> categories = new ArrayList<Category>();
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(this.getLoadCategoriesQuery());
			while(res.next()) {
				Category category = this.loadCategory(res, langManager);
				categories.add(category);
			}
		} catch (Throwable t) {
			_logger.error("Error loading categories",  t);
			throw new RuntimeException("Error loading categories", t);
			//processDaoException(t, "Error loading categories", "loadCategories");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return categories;
	}
	
	/**
	 * Costruisce e restituisce una categoria leggendo una riga di recordset.
	 * @param res Il resultset da leggere
	 * @param langManager Il manager delle lingue.
	 * @return La categoria generata.
	 * @throws ApsSystemException 
	 */
	protected Category loadCategory(ResultSet res, ILangManager langManager) 
			throws ApsSystemException {
		Category category = new Category();
		try {
			category.setCode(res.getString(1));
			category.setParentCode(res.getString(2));
			ApsProperties prop = new ApsProperties();
			prop.loadFromXml(res.getString(3));
			category.setTitles(prop);
			category.setDefaultLang(langManager.getDefaultLang().getCode());
		} catch (Throwable t) {
			throw new ApsSystemException("Error while loading a category", t);
		}
	    return category;
	}
	
	/**
     * Cancella la categoria corrispondente al codice immesso.
     * @param code Il codice relativo alla categoria da cancellare.
     */
	@Override
    public void deleteCategory(String code) {
    	Connection conn = null;
		PreparedStatement stat = null;
        try {
        	conn = this.getConnection();
        	conn.setAutoCommit(false);
			stat = conn.prepareStatement(this.getDeleteCategoryQuery());
            stat.setString(1, code);
            stat.executeUpdate();
            conn.commit();
        } catch (Throwable t) {
        	this.executeRollback(conn);
			_logger.error("Error detected while deleting category '{}'", code,  t);
			throw new RuntimeException("Error detected while deleting a category", t);
			//processDaoException(t, "Error detected while deleting a category", "deleteCategory");
        } finally {
            closeDaoResources(null, stat, conn);
        }
    }
    
    /**
     * Inserisce una nuova Categoria.
     * @param category La nuova Categoria da inserire.
     */
	@Override
    public void addCategory(Category category) {
    	Connection conn = null;
		PreparedStatement stat = null;
    	try {
    		conn = this.getConnection();
    		conn.setAutoCommit(false);
			stat = conn.prepareStatement(this.getAddCategoryQuery());
    		stat.setString(1, category.getCode());
    		stat.setString(2, category.getParentCode());
    		stat.setString(3, category.getTitles().toXml());
    		stat.executeUpdate();
    		conn.commit();
    	} catch (Throwable t) {
    		this.executeRollback(conn);
			_logger.error("Error while inserting a new category",  t);
			throw new RuntimeException("Error while inserting a new category", t);
			//processDaoException(t, "Error while inserting a new category", "addCategory");
    	} finally {
    		closeDaoResources(null, stat, conn);
    	}
    }
    
    /**
     * Aggiorna una categoria sul db.
     * @param category La categoria da aggiornare.
     */
	@Override
    public void updateCategory(Category category) {
    	Connection conn = null;
		PreparedStatement stat = null;
     	try {
     		conn = this.getConnection();
     		conn.setAutoCommit(false);
			stat = conn.prepareStatement(this.getUpdateCategoryQuery());
    		stat.setString(1, category.getParentCode());
    		stat.setString(2, category.getTitles().toXml());
    		stat.setString(3, category.getCode());
    		stat.executeUpdate();
    		conn.commit();
    	} catch (Throwable t) {
    		this.executeRollback(conn);
			_logger.error("Error detected while updating a category",  t);
			throw new RuntimeException("Error detected while updating a category", t);
			//processDaoException(t, "Error detected while updating a category",  "updateCategory");
    	} finally {
    		closeDaoResources(null, stat, conn);
    	}
    }
	
	protected String getLoadCategoriesQuery() {
		return ALL_CATEGORIES;
	}
	
	protected String getAddCategoryQuery() {
		return ADD_CATEGORY;
	}
	
	protected String getDeleteCategoryQuery() {
		return DELETE_CATEGORY;
	}
	
	/**
	 * Restituisce la query corretta per aggiornare una categorie.
	 * @return La query corretta.
	 */
	protected String getUpdateCategoryQuery() {
		return UPDATE_CATEGORY;
	}
	
	private static final String ALL_CATEGORIES = 
		"SELECT catcode, parentcode, titles FROM categories ORDER BY parentcode, catcode";
	
	private static final String ADD_CATEGORY = 
		"INSERT INTO categories (catcode, parentcode, titles) VALUES ( ? , ? , ? )";
	
	private static final String DELETE_CATEGORY = 
		"DELETE FROM categories WHERE catcode = ? ";
	
	private static final String UPDATE_CATEGORY = 
		"UPDATE categories SET parentcode = ? , titles = ? WHERE catcode = ? ";
    
}
