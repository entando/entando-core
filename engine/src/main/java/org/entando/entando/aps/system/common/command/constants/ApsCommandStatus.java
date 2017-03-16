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
	ENDED
}
