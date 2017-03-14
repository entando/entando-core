package org.entando.entando.aps.system.common.command;

import java.util.Collection;

import org.entando.entando.aps.system.common.command.constants.CommandErrorCode;
import org.entando.entando.aps.system.common.command.constants.CommandStatus;
import org.entando.entando.aps.system.common.command.report.BulkCommandReport;
import org.entando.entando.aps.system.common.command.report.DefaultBulkCommandReport;
import org.entando.entando.aps.system.common.command.tracer.BulkCommandTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

public abstract class BaseBulkCommand<I, A> {

	private static final Logger _logger = LoggerFactory.getLogger(BaseBulkCommand.class);

	public BaseBulkCommand(Collection<I> items, A manager, BulkCommandTracer<I> tracer) {
		this.setItems(items);
		this.setTotal(items != null ? items.size() : 0);
		this.setApplier(manager);
		this.setTracer(tracer);
		this.setStatus(CommandStatus.NEW);
	}

	public void apply() {
		if (this.getItems() == null) {
			this._status = CommandStatus.ENDED;
		} else {
			for (I item : this.getItems()) {
				if (this.checkStatus()) {
					boolean result = false;
					try {
						result = this.apply(item);
					} catch (Throwable t) {
						this.getTracer().traceError(item, CommandErrorCode.ERROR);
						_logger.error("Error performig {} action on {}", this.getClass().getName(), item, t);
					}
					if (result) {
						this._performedSuccess++;
					} else {
						this._performedErrors++;
					}
				} else {
					break;
				}
			}
			if (!CommandStatus.STOPPED.equals(this._status)) {
				this._status = CommandStatus.ENDED;
			}
		}
	}

	protected synchronized boolean checkStatus() {
		boolean allowed = true;
		if (CommandStatus.NEW.equals(this._status)) {
			this._status = CommandStatus.RUNNING;
		} else if (!CommandStatus.RUNNING.equals(this._status)) {
			allowed = false;
			if (!CommandStatus.STOPPING.equals(this._status)) {
				this._status = CommandStatus.STOPPED;
			}
		}
		return allowed;
	}

	protected abstract boolean apply(I item) throws ApsSystemException;

	public synchronized void stopCommand() {
		this.setStatus(CommandStatus.STOPPING);
	}

	public Collection<I> getItems() {
		return _items;
	}
	protected void setItems(Collection<I> items) {
		this._items = items;
	}

	public A getApplier() {
		return _applier;
	}
	protected void setApplier(A applier) {
		this._applier = applier;
	}

	public int getTotal() {
		return _total;
	}
	protected void setTotal(int total) {
		this._total = total;
	}

	public int getPerformedSuccess() {
		return _performedSuccess;
	}
	protected void setPerformedSuccess(int performedSuccess) {
		this._performedSuccess = performedSuccess;
	}

	public int getPerformedErrors() {
		return _performedErrors;
	}
	protected void setPerformedErrors(int performedErrors) {
		this._performedErrors = performedErrors;
	}

	public CommandStatus getStatus() {
		return _status;
	}
	protected void setStatus(CommandStatus status) {
		this._status = status;
	}

	public BulkCommandTracer<I> getTracer() {
		return _tracer;
	}
	protected void setTracer(BulkCommandTracer<I> tracer) {
		this._tracer = tracer;
	}
	
	public BulkCommandReport<I> getReport() {
		return _report;
	}

	private Collection<I> _items;
	private A _applier;
	private int _total = 0;
	private int _performedSuccess = 0;
	private int _performedErrors = 0;
	private volatile CommandStatus _status = CommandStatus.NEW;
	private BulkCommandTracer<I> _tracer;
	private BulkCommandReport<I> _report = new DefaultBulkCommandReport<I>(this);

}
