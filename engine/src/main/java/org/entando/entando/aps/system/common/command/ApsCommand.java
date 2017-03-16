package org.entando.entando.aps.system.common.command;

import org.entando.entando.aps.system.common.command.constants.ApsCommandStatus;

/**
 * Class that applies a command on one or multiple items.
 * 
 * @author E.Mezzano
 */
public interface ApsCommand {

	/**
	 * Apply the command. At the end of the command, 
	 */
	public void apply();

	/**
	 * Stop the command
	 */
	public void stopCommand();

	/**
	 * Returns the status of the command
	 * @return The status of the command
	 */
	public ApsCommandStatus getStatus();

}
