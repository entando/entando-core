package org.entando.entando.aps.system.common.command;

import java.util.Date;

import org.entando.entando.aps.system.common.command.constants.ApsCommandStatus;

/**
 * Class that applies a command on one or multiple items.
 * 
 * @author E.Mezzano
 */
public interface ApsCommand {

	/**
	 * Returns the ID of the given command.
	 * @return The ID of the given command.
	 */
	public String getId();

	/**
	 * Apply the command.
	 */
	public void apply();

	/**
	 * Stop the command.
	 */
	public void stopCommand();

	/**
	 * Returns the status of the command.
	 * @return The status of the command.
	 */
	public ApsCommandStatus getStatus();
	
	/**
	 * Returns the instant of the end of the command.
	 * @return The instant of the end of the command.
	 */
	public Date getEndingTime();

	/**
	 * Returns true if the Command is ended (whether it has been completed or stopped early), false otherwise.
	 * @return true if the Command is ended (whether it has been completed or stopped early), false otherwise.
	 */
	public boolean isEnded();

}
