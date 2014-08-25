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
package com.agiletec.aps.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;

/**
 * SEVERE (highest value)	ERROR, FATAL
 * WARNING					WARN
 * INFO						INFO
 * CONFIG					
 * FINE
 * FINER					DEBUG
 * FINEST (lowest value)  	TRACE
 * @author 
 */
public class ForJLogger implements Log {

	/**
	 * 
	 */
	public ForJLogger(Logger log) {
		_log = log;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.logging.Log#isDebugEnabled()
	 */
	public boolean isDebugEnabled() {
		return _log.isLoggable(Level.FINER);
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.logging.Log#isErrorEnabled()
	 */
	public boolean isErrorEnabled() {
		return _log.isLoggable(Level.SEVERE);
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.logging.Log#isFatalEnabled()
	 */
	public boolean isFatalEnabled() {
		return _log.isLoggable(Level.SEVERE);
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.logging.Log#isInfoEnabled()
	 */
	public boolean isInfoEnabled() {
		return _log.isLoggable(Level.INFO);
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.logging.Log#isTraceEnabled()
	 */
	public boolean isTraceEnabled() {
		return _log.isLoggable(Level.FINEST);
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.logging.Log#isWarnEnabled()
	 */
	public boolean isWarnEnabled() {
		return _log.isLoggable(Level.WARNING);
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.logging.Log#trace(java.lang.Object)
	 */
	public void trace(Object arg0) {
		_log.finest(arg0.toString());
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.logging.Log#trace(java.lang.Object, java.lang.Throwable)
	 */
	public void trace(Object arg0, Throwable arg1) {
		_log.throwing(null, null, arg1);
		_log.finest(arg0.toString());
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.logging.Log#debug(java.lang.Object)
	 */
	public void debug(Object arg0) {
		_log.finer(arg0.toString());

	}

	/* (non-Javadoc)
	 * @see org.apache.commons.logging.Log#debug(java.lang.Object, java.lang.Throwable)
	 */
	public void debug(Object arg0, Throwable arg1) {
		_log.throwing(null, null, arg1);
		_log.finer(arg0.toString());
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.logging.Log#info(java.lang.Object)
	 */
	public void info(Object arg0) {
		_log.info(arg0.toString());
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.logging.Log#info(java.lang.Object, java.lang.Throwable)
	 */
	public void info(Object arg0, Throwable arg1) {
		_log.throwing(null, null, arg1);
		_log.info(arg0.toString());
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.logging.Log#warn(java.lang.Object)
	 */
	public void warn(Object arg0) {
		_log.warning(arg0.toString());

	}

	/* (non-Javadoc)
	 * @see org.apache.commons.logging.Log#warn(java.lang.Object, java.lang.Throwable)
	 */
	public void warn(Object arg0, Throwable arg1) {
		_log.throwing(null, null, arg1);
		_log.warning(arg0.toString());
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.logging.Log#error(java.lang.Object)
	 */
	public void error(Object arg0) {
		_log.severe(arg0.toString());
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.logging.Log#error(java.lang.Object, java.lang.Throwable)
	 */
	public void error(Object arg0, Throwable arg1) {
		_log.throwing(null, null, arg1);
		_log.severe(arg0.toString());
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.logging.Log#fatal(java.lang.Object)
	 */
	public void fatal(Object arg0) {
		_log.severe(arg0.toString());
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.logging.Log#fatal(java.lang.Object, java.lang.Throwable)
	 */
	public void fatal(Object arg0, Throwable arg1) {
		_log.throwing(null, null, arg1);
		_log.severe(arg0.toString());
	}

	private Logger _log;
}
