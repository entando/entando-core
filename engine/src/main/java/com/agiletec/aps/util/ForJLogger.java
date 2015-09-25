/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
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
 * @author W.Ambu
 */
public class ForJLogger implements Log {

	public ForJLogger(Logger log) {
		_log = log;
	}
	
	@Override
	public boolean isDebugEnabled() {
		return _log.isLoggable(Level.FINER);
	}
	
	@Override
	public boolean isErrorEnabled() {
		return _log.isLoggable(Level.SEVERE);
	}

	@Override
	public boolean isFatalEnabled() {
		return _log.isLoggable(Level.SEVERE);
	}

	@Override
	public boolean isInfoEnabled() {
		return _log.isLoggable(Level.INFO);
	}

	@Override
	public boolean isTraceEnabled() {
		return _log.isLoggable(Level.FINEST);
	}

	@Override
	public boolean isWarnEnabled() {
		return _log.isLoggable(Level.WARNING);
	}

	@Override
	public void trace(Object arg0) {
		_log.finest(arg0.toString());
	}

	@Override
	public void trace(Object arg0, Throwable arg1) {
		_log.throwing(null, null, arg1);
		_log.finest(arg0.toString());
	}

	@Override
	public void debug(Object arg0) {
		_log.finer(arg0.toString());

	}

	@Override
	public void debug(Object arg0, Throwable arg1) {
		_log.throwing(null, null, arg1);
		_log.finer(arg0.toString());
	}

	@Override
	public void info(Object arg0) {
		_log.info(arg0.toString());
	}

	@Override
	public void info(Object arg0, Throwable arg1) {
		_log.throwing(null, null, arg1);
		_log.info(arg0.toString());
	}

	@Override
	public void warn(Object arg0) {
		_log.warning(arg0.toString());
	}

	@Override
	public void warn(Object arg0, Throwable arg1) {
		_log.throwing(null, null, arg1);
		_log.warning(arg0.toString());
	}

	@Override
	public void error(Object arg0) {
		_log.severe(arg0.toString());
	}

	@Override
	public void error(Object arg0, Throwable arg1) {
		_log.throwing(null, null, arg1);
		_log.severe(arg0.toString());
	}

	@Override
	public void fatal(Object arg0) {
		_log.severe(arg0.toString());
	}

	@Override
	public void fatal(Object arg0, Throwable arg1) {
		_log.throwing(null, null, arg1);
		_log.severe(arg0.toString());
	}
	
	private Logger _log;
	
}
