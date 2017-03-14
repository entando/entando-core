package org.entando.entando.aps.system.common.command.thread;

import org.entando.entando.aps.system.common.command.BaseBulkCommand;

public class BulkCommandThread implements Runnable {

	public BulkCommandThread(BaseBulkCommand<?, ?> action) {
		this._action = action;
	}

	@Override
	public void run() {
		this._action.apply();
	}

	public void stop() {
		this._action.stopCommand();
	}

	private BaseBulkCommand<?, ?> _action;

}
