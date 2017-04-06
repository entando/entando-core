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
package org.entando.entando.aps.system.common.command.constants;

import org.entando.entando.aps.system.common.command.ApsCommand;

/**
 * The status of an {@link ApsCommand}
 * @author E.Mezzano
 *
 */
public enum ApsCommandStatus {
	/**
	 * A new command, not yet sent running
	 */
	NEW,
	/**
	 * A running command
	 */
	RUNNING,
	/**
	 * A running command, that you want to stop
	 */
	STOPPING,
	/**
	 * A command interrupted before its completion
	 */
	STOPPED,
	/**
	 * A command completely executed
	 */
	COMPLETED
}
