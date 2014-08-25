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
package com.agiletec.plugins.jacms.aps.system.services.page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.page.PageDAO;

/**
 * Estensione del Data Access Object per gli oggetti pagina {@link PageDAO}.
 * @author E.Santoboni
 */
public class CmsPageDAO extends PageDAO implements ICmsPageDAO {
	
	private static final Logger _logger =  LoggerFactory.getLogger(CmsPageDAO.class);
	
	@Override
	public List<String> getContentUtilizers(String contentId) {
		Connection conn = null;
		List<String> pageCodes = new ArrayList<String>();
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_REFERENCING_PAGES_FOR_CONTENT);
			stat.setString(1, contentId);
			res = stat.executeQuery();
			while (res.next()) {
				pageCodes.add(res.getString(1));
			}
		} catch (Throwable t) {
			_logger.error("Error loading pages referenced by content {}", contentId,  t);
			throw new RuntimeException("Error loading pages referenced by content " + contentId, t);
			//this.processDaoException(t, "Errore in caricamento lista pagine referenziate con contenuto " + contentId, "getContentUtilizers");
		} finally {
			this.closeDaoResources(res, stat, conn);
		}
		return pageCodes;
	}
    
    private static final String LOAD_REFERENCING_PAGES_FOR_CONTENT = 
		"SELECT pagecode FROM widgetconfig WHERE publishedcontent = ? ";
    
}