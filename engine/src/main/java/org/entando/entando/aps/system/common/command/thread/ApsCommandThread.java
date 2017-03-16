package org.entando.entando.aps.system.common.command.thread;

import org.entando.entando.aps.system.common.command.ApsCommand;

/**
 * A thread for {@link ApsCommand} execution.
 * 
 * @author E.Mezzano
 *
 */
public class ApsCommandThread implements Runnable {

	/**
	 * @param command The command to execute.
	 */
	public ApsCommandThread(ApsCommand command) {
		this._command = command;
	}

	@Override
	public void run() {
		this._command.apply();
	}

	/**
	 * Stop the current command execution.
	 * The behaviour is given by the {@link ApsCommand} implementation.
	 */
	public void stop() {
		this._command.stopCommand();
	}

	private ApsCommand _command;

}
