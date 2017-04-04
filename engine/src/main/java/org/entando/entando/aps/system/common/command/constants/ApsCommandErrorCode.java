package org.entando.entando.aps.system.common.command.constants;

/**
 * Codes used in the event of errors in the execution of a command, relative to a single item.
 * @author E.Mezzano
 *
 */
public enum ApsCommandErrorCode {
	/**
	 * Returned when the item on which to run the command is not found.
	 */
	NOT_FOUND,
	/**
	 * Returned when some parameters of the command are not valid.
	 */
	PARAMS_NOT_VALID,
	/**
	 * Returned when the command is not executable on the given item.
	 */
	NOT_ALLOWED,
	/**
	 * Returned on all the other error cases.
	 */
	ERROR
}
