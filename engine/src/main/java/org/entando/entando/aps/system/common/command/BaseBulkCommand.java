package org.entando.entando.aps.system.common.command;

import java.util.Collection;
import java.util.Date;

import org.entando.entando.aps.system.common.command.constants.ApsCommandErrorCode;
import org.entando.entando.aps.system.common.command.constants.ApsCommandStatus;
import org.entando.entando.aps.system.common.command.context.BulkCommandContext;
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
public abstract class BaseBulkCommand<I, A, C extends BulkCommandContext<I>> implements ApsCommand<C> {

	private static final Logger _logger = LoggerFactory.getLogger(BaseBulkCommand.class);

	@Override
	public void init(C context) {
		this.setContext(context);
	}

	@Override
	public void apply() {
		if (this.getItems() == null) {
			this._status = ApsCommandStatus.COMPLETED;
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
				this._status = ApsCommandStatus.COMPLETED;
			}
			this.setEndingTime(new Date());
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
		if (!ApsCommandStatus.COMPLETED.equals(this._status)) {
			this.setStatus(ApsCommandStatus.STOPPING);
		}
	}
	
	/**
	 * Returns the ID of the given command. Can coincide with the thread name.
	 * @return The ID of the given command.
	 */
	public String getId() {
		return _id;
	}
	/**
	 * Sets the ID of the given command. Can coincide with the thread name.
	 * @param id The ID of the given command.
	 */
	public void setId(String id) {
		this._id = id;
	}
	
	/**
	 * Returns the items on which to apply the command.
	 * @return The items on which to apply the command.
	 */
	public Collection<I> getItems() {
		return this.getContext().getItems();
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
		return this.getItems().size();
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

	@Override
	public Date getEndingTime() {
		return endingTime;
	}
	protected void setEndingTime(Date endingTime) {
		this.endingTime = endingTime;
	}

	@Override
	public boolean isEnded() {
		return ApsCommandStatus.COMPLETED.equals(this._status) || ApsCommandStatus.STOPPED.equals(this._status);
	}

	protected C getContext() {
		return _context;
	}
	protected void setContext(C context) {
		this._context = context;
	}

	/**
	 * Returns the tracer of the command execution.
	 * @return The tracer of the command execution.
	 */
	public BulkCommandTracer<I> getTracer() {
		return this.getContext().getTracer();
	}

	/**
	 * Returns the report of the command
	 * @return The report of the command
	 */
	public BulkCommandReport<I> getReport() {
		return _report;
	}

	private String _id;
	private A _applier;
	private int _applySuccesses = 0;
	private int applyErrors = 0;
	private Date endingTime;
	private volatile ApsCommandStatus _status = ApsCommandStatus.NEW;
	
	private C _context;
	private BulkCommandReport<I> _report = new DefaultBulkCommandReport<I>(this);

}
