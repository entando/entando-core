package org.entando.entando.aps.system.common.command;

import java.util.Collection;

import org.entando.entando.aps.system.common.command.constants.ApsCommandErrorCode;
import org.entando.entando.aps.system.common.command.constants.ApsCommandStatus;
import org.entando.entando.aps.system.common.command.report.BulkCommandReport;
import org.entando.entando.aps.system.common.command.report.DefaultBulkCommandReport;
import org.entando.entando.aps.system.common.command.tracer.BulkCommandTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * A base class for the execution of a {@link ApsCommand} on multiple items.
 * 
 * @author E.Mezzano
 * 
 * @param <I> The type of items on which to apply the command.
 * @param <A> The applier of the command (for example: the Content Manager that execute the update of a Content).
 */
public abstract class BaseBulkCommand<I, A> implements ApsCommand {

	private static final Logger _logger = LoggerFactory.getLogger(BaseBulkCommand.class);

	/**
	 * The constructor of the Command. 
	 * 
	 * @param items The items on which to run the command.
	 * @param applier The applier of the command (for example: the Content Manager that execute the update of a Content).
	 * @param tracer The tracer of the command execution.
	 */
	public BaseBulkCommand(Collection<I> items, A applier, BulkCommandTracer<I> tracer) {
		this.setItems(items);
		this.setTotal(items != null ? items.size() : 0);
		this.setApplier(applier);
		this.setTracer(tracer);
		this.setStatus(ApsCommandStatus.NEW);
	}

	@Override
	public void apply() {
		if (this.getItems() == null) {
			this._status = ApsCommandStatus.ENDED;
		} else {
			for (I item : this.getItems()) {
				if (this.checkStatus()) {
					boolean result = false;
					try {
						result = this.apply(item);
					} catch (Throwable t) {
						this.getTracer().traceError(item, ApsCommandErrorCode.ERROR);
						_logger.error("Error performig {} action on {}", this.getClass().getName(), item, t);
					}
					if (result) {
						this._applySuccesses++;
					} else {
						this.applyErrors++;
					}
				} else {
					break;
				}
			}
			if (!ApsCommandStatus.STOPPED.equals(this._status)) {
				this._status = ApsCommandStatus.ENDED;
			}
		}
	}

	/**
	 * Check the status of the Command before execute the next command on the current item.
	 * 
	 * @return True if the command is allowed.
	 */
	protected synchronized boolean checkStatus() {
		boolean allowed = true;
		if (ApsCommandStatus.NEW.equals(this._status)) {
			this._status = ApsCommandStatus.RUNNING;
		} else if (!ApsCommandStatus.RUNNING.equals(this._status)) {
			allowed = false;
			if (!ApsCommandStatus.STOPPING.equals(this._status)) {
				this._status = ApsCommandStatus.STOPPED;
			}
		}
		return allowed;
	}

	/**
	 * Apply the command on the given item.
	 * This method is delegated tracking the outcome of the command.
	 * 
	 * @param item The item on which to apply the command.
	 * @return The result of the command. True in case of success (also with warnings), error instead.
	 * @throws ApsSystemException In case of error during the execution of the command.
	 */
	protected abstract boolean apply(I item) throws ApsSystemException;

	@Override
	public synchronized void stopCommand() {
		this.setStatus(ApsCommandStatus.STOPPING);
	}

	/**
	 * Returns the items on which to apply the command.
	 * @return The items on which to apply the command.
	 */
	public Collection<I> getItems() {
		return _items;
	}
	/**
	 * Sets the items on which to apply the command.
	 * @param items The items on which to apply the command.
	 */
	protected void setItems(Collection<I> items) {
		this._items = items;
	}

	/**
	 * Returns the applier of the command (for example: the Content Manager that execute the update of a Content)
	 * @return The applier of the command (for example: the Content Manager that execute the update of a Content)
	 */
	public A getApplier() {
		return _applier;
	}
	/**
	 * Sets the applier of the command (for example: the Content Manager that execute the update of a Content)
	 * @param applier The applier of the command (for example: the Content Manager that execute the update of a Content)
	 */
	protected void setApplier(A applier) {
		this._applier = applier;
	}

	/**
	 * Returns the number items onto apply the command.
	 * @return The number items onto apply the command.
	 */
	public int getTotal() {
		return _total;
	}
	/**
	 * Sets the number items onto apply the command.
	 * @param total The number items onto apply the command.
	 */
	protected void setTotal(int total) {
		this._total = total;
	}

	/**
	 * Returns the number items onto the command is succesfully applied.
	 * @return The number items onto the command is succesfully applied.
	 */
	public int getApplySuccesses() {
		return _applySuccesses;
	}
	/**
	 * Sets the number items onto the command is succesfully applied.
	 * @param applySuccesses The number items onto the command is succesfully applied.
	 */
	protected void setApplySuccesses(int applySuccesses) {
		this._applySuccesses = applySuccesses;
	}

	/**
	 * Returns the number items onto the command is applied with errors.
	 * @return The number items onto the command is applied with errors.
	 */
	public int getApplyErrors() {
		return applyErrors;
	}
	/**
	 * Sets the number items onto the command is applied with errors.
	 * @param applyErrors The number items onto the command is applied with errors.
	 */
	protected void setApplyErrors(int applyErrors) {
		this.applyErrors = applyErrors;
	}

	@Override
	public ApsCommandStatus getStatus() {
		return _status;
	}
	/**
	 * Sets the status of the command execution.
	 * @param status The status of the command execution.
	 */
	protected void setStatus(ApsCommandStatus status) {
		this._status = status;
	}

	/**
	 * Returns the tracer of the command execution.
	 * @return The tracer of the command execution.
	 */
	public BulkCommandTracer<I> getTracer() {
		return _tracer;
	}
	/**
	 * Sets the tracer of the command execution.
	 * @param tracer The tracer of the command execution.
	 */
	protected void setTracer(BulkCommandTracer<I> tracer) {
		this._tracer = tracer;
	}

	/**
	 * Returns the report of the command
	 * @return The report of the command
	 */
	public BulkCommandReport<I> getReport() {
		return _report;
	}

	private Collection<I> _items;
	private A _applier;
	private int _total = 0;
	private int _applySuccesses = 0;
	private int applyErrors = 0;
	private volatile ApsCommandStatus _status = ApsCommandStatus.NEW;
	private BulkCommandTracer<I> _tracer;
	private BulkCommandReport<I> _report = new DefaultBulkCommandReport<I>(this);

}
