package org.entando.entando.aps.system.services.command.util;

import org.entando.entando.aps.system.common.command.BaseBulkCommand;

/**
 * 
 * @author E.Mezzano
 *
 */
public class BulkCommandContainer {

	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}

	public BaseBulkCommand<?, ?, ?> getCommand() {
		return command;
	}
	public void setCommand(BaseBulkCommand<?, ?, ?> command) {
		this.command = command;
	}

	private String owner;
	private BaseBulkCommand<?, ?, ?> command;

}
