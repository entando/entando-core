package org.entando.entando.aps.system.common.command.constants;

/**
 * The status of a command
 * @author E.Mezzano
 *
 */
public enum CommandStatus {
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
	ENDED
}
