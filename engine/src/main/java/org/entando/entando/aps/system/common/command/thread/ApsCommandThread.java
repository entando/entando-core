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
package org.entando.entando.aps.system.common.command.thread;

import org.entando.entando.aps.system.common.command.ApsCommand;

/**
 * A thread for {@link ApsCommand} execution.
 * 
 * @author E.Mezzano
 *
 */
public class ApsCommandThread extends Thread {

	/**
	 * @param command The command to execute.
	 */
	public ApsCommandThread(ApsCommand command) {
		this._command = command;
		this.setName(command.getId());
	}

	@Override
	public void run() {
		this._command.apply();
	}

	/**
	 * Stop the current command execution.
	 * The behaviour is given by the {@link ApsCommand} implementation.
	 */
	public void stopCommand() {
		this._command.stopCommand();
	}

	@Override
	public void destroy() {
		this.stopCommand();
		super.destroy();
	}

	@Override
	public void interrupt() {
		this.stopCommand();
		super.interrupt();
	}

	private ApsCommand _command;

}
