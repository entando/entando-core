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
package org.entando.entando.apsadmin.system.services.activitystream;

import com.agiletec.aps.system.common.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.entando.entando.apsadmin.system.services.activitystream.model.ActivityStreamComment;
import org.entando.entando.apsadmin.system.services.activitystream.model.ActivityStreamLikeInfo;
import org.entando.entando.apsadmin.system.services.activitystream.model.ActivityStreamLikeInfos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class SocialActivityStreamDAO extends AbstractDAO implements ISocialActivityStreamDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(SocialActivityStreamDAO.class);
	
	@Override
	public void editActionLikeRecord(int id, String username, boolean add) {
		if (add) {
			this.addActionLikeRecord(id, username);
		} else {
			this.deleteActionLikeRecord(id, username);
		}
	}
	
	@Override
	public void addActionLikeRecord(int id, String username) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			super.executeQueryWithoutResultset(conn, DELETE_LOG_LIKE_RECORD, id, username);
			Timestamp timestamp = new Timestamp(new Date().getTime());
			super.executeQueryWithoutResultset(conn, ADD_ACTION_LIKE_RECORD, id, username, timestamp);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error on insert actionlogger like record",  t);
			throw new RuntimeException("Error on insert actionlogger like record", t);
		} finally {
			this.closeConnection(conn);
		}
	}
	
	@Override
	public void deleteActionLikeRecord(int id, String username) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			super.executeQueryWithoutResultset(conn, DELETE_LOG_LIKE_RECORD, id, username);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error on delete like record: {}", id, t);
			throw new RuntimeException("Error on delete like record: " + id, t);
		} finally {
			this.closeConnection(conn);
		}
	}
	
	@Override
	public List<ActivityStreamLikeInfo> getActionLikeRecords(int id) {
		List<ActivityStreamLikeInfo> infos = new ActivityStreamLikeInfos();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet result = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_ACTION_LIKE_RECORDS);
			stat.setInt(1, id);
			result = stat.executeQuery();
			while (result.next()) {
				ActivityStreamLikeInfo asli = new ActivityStreamLikeInfo();
				asli.setUsername(result.getString(1));
				infos.add(asli);
			}
		} catch (Throwable t) {
			_logger.error("Error while loading activity stream like records",  t);
			throw new RuntimeException("Error while loading activity stream like records", t);
		} finally {
			closeDaoResources(result, stat, conn);
		}
		return infos;
	}

	@Override
	public List<ActivityStreamComment> getActionCommentRecords(int id) {
		List<ActivityStreamComment> comments = new ArrayList<ActivityStreamComment>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet result = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_ACTION_COMMENT_RECORDS);
			stat.setInt(1, id);
			result = stat.executeQuery();
			while (result.next()) {
				ActivityStreamComment comment = new ActivityStreamComment();
				comment.setId(result.getInt(1));
				comment.setUsername(result.getString(2));
				comment.setCommentText(result.getString(3));
				Timestamp timestamp = result.getTimestamp(4);
				comment.setCommentDate(new Date(timestamp.getTime()));
				comments.add(comment);
			}
		} catch (Throwable t) {
			_logger.error("Error while loading activity stream comment records",  t);
			throw new RuntimeException("Error while loading activity stream comment records", t);
		} finally {
			closeDaoResources(result, stat, conn);
		}
		return comments;
	}

	@Override
	public void addActionCommentRecord(int id, int recordId, String username, String comment) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_ACTION_COMMENT_RECORD);
			stat.setInt(1, id);
			stat.setInt(2, recordId);
			stat.setString(3, username);
			stat.setString(4, comment);
			Timestamp timestamp = new Timestamp(new Date().getTime());
			stat.setTimestamp(5, timestamp);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error on insert actionlogger comment record",  t);
			throw new RuntimeException("Error on insert actionlogger comment record", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void deleteSocialRecordsRecord(int streamId) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			super.executeQueryWithoutResultset(conn, DELETE_LOG_LIKE_RECORDS, streamId);
			super.executeQueryWithoutResultset(conn, DELETE_ACTION_COMMENT_RECORDS_BY_ID, streamId);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error on delete social records {}", streamId, t);
			throw new RuntimeException("Error on delete social records: " + streamId, t);
		} finally {
			this.closeConnection(conn);
		}
	}
	
	@Override
	public void deleteActionCommentRecord(int id) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			super.executeQueryWithoutResultset(conn, DELETE_ACTION_COMMENT_RECORD, id);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error on delete comment record {}", id, t);
			throw new RuntimeException("Error on delete comment record: " + id, t);
		} finally {
			this.closeConnection(conn);
		}
	}
	
	private static final String ADD_ACTION_LIKE_RECORD
			= "INSERT INTO actionloglikerecords ( recordid, username, likedate) VALUES ( ? , ? , ? )";
	
	private static final String DELETE_LOG_LIKE_RECORDS
			= "DELETE from actionloglikerecords where recordid = ? ";

	private static final String DELETE_LOG_LIKE_RECORD
			= DELETE_LOG_LIKE_RECORDS + "AND username = ? ";

	private static final String GET_ACTION_LIKE_RECORDS
			= "SELECT username from actionloglikerecords where recordid = ? ";
	
	private static final String ADD_ACTION_COMMENT_RECORD
			= "INSERT INTO actionlogcommentrecords (id, recordid, username, comment, commentdate) VALUES ( ? , ? , ? , ? , ? )";
	
	private static final String DELETE_ACTION_COMMENT_RECORD
			= "DELETE from actionlogcommentrecords where id = ?";
	
	private static final String DELETE_ACTION_COMMENT_RECORDS_BY_ID
			= "DELETE from actionlogcommentrecords where recordid = ?";
	
	private static final String GET_ACTION_COMMENT_RECORDS
			= "SELECT id, username, comment, commentdate FROM actionlogcommentrecords WHERE recordid = ? ORDER BY commentdate ASC";
	
}
