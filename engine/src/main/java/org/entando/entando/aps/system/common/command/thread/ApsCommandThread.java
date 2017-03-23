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
