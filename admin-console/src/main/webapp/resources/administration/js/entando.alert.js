
(function(context){


	/**
	 * @constructor
	 * @param {string} containerSelector - a selector to the alerts container
	 */
	function EntandoAlert(containerSelector) {

		var me = this;

		me.$container = $(containerSelector);


	}

	// Static methods

	/**
	 * Creates a patternfly alert element
	 * @param {string} alertType - one of { info | success | warning | danger }
	 * @param {string} message - HTML formatted message to show
	 * @param {boolean} dismissable - if true, the alert will be dismissable
	 * @returns {*|jQuery} the element
	 */
	EntandoAlert.createAlert = function createAlert (alertType, message, dismissable) {

		var typeClassMap = {
			danger: 'alert-danger',
			warning: 'alert-warning',
			info: 'alert-info',
			success: 'alert-success'
		};
		var typeIconMap = {
			danger: 'pficon pficon-error-circle-o',
			warning: 'pficon pficon-warning-triangle-o',
			info: 'pficon pficon-info',
			success: 'pficon pficon-ok'
		};

		var $alert = $('<div class="alert">')
			.addClass(typeClassMap[alertType])
			.append('<span class="' + typeIconMap[alertType] + '"></span>')
			.append(message);


		if (dismissable) {
			$alert
				.addClass('alert-dismissable')
				.append('<button type="button" class="close" data-dismiss="alert" aria-hidden="true"><span class="pficon pficon-close"></span></button>');;
		}

		return $alert;
	};


	/**
	 * Convenience method to append a dismissable error alert to the container
	 * @param {string} message - the message
	 */
	EntandoAlert.prototype.addDismissableError = function addDismissableError (message) {
		this.$container.append(EntandoAlert.createAlert('danger', message, true));
	};

	/**
	 * Convenience method to append a dismissable warning alert to the container
	 * @param {string} message - the message
	 */
	EntandoAlert.prototype.addDismissableWarning = function addDismissableWarning (message) {
		this.$container.append(EntandoAlert.createAlert('warning', message, true));
	};

	/**
	 * Convenience method to append a dismissable info alert to the container
	 * @param {string} message - the message
	 */
	EntandoAlert.prototype.addDismissableInfo = function addDismissableInfo (message) {
		this.$container.append(EntandoAlert.createAlert('info', message, true));
	};

	/**
	 * Convenience method to append a dismissable success alert to the container
	 * @param {string} message - the message
	 */
	EntandoAlert.prototype.addDismissableSuccess = function addDismissableSuccess (message) {
		this.$container.append(EntandoAlert.createAlert('success', message, true));
	};

	/**
	 * Convenience method to append an error alert to the container
	 * @param {string} message - the message
	 */
	EntandoAlert.prototype.addError = function addError (message) {
		this.$container.append(EntandoAlert.createAlert('danger', message, false));
	};

	/**
	 * Convenience method to append a warning alert to the container
	 * @param {string} message - the message
	 */
	EntandoAlert.prototype.addWarning = function addWarning (message) {
		this.$container.append(EntandoAlert.createAlert('warning', message, false));
	};

	/**
	 * Convenience method to append an info alert to the container
	 * @param {string} message - the message
	 */
	EntandoAlert.prototype.addInfo = function addInfo (message) {
		this.$container.append(EntandoAlert.createAlert('info', message, false));
	};

	/**
	 * Convenience method to append a success alert to the container
	 * @param {string} message - the message
	 */
	EntandoAlert.prototype.addSuccess = function addSuccess (message) {
		this.$container.append(EntandoAlert.createAlert('success', message, false));
	};


	/**
	 * If there are errors / infos in the endpoint response, append alerts
	 * @param {Object} data - the endpoint response
	 * @param {Array} data.actionErrors - the action errors
	 * @param {Array} data.actionMessages - the action messages
	 * @return {boolean} true if there were errors, false otherwise
	 */
	EntandoAlert.prototype.showResponseAlerts = function showResponseAlerts (data) {
		var i;
		if (data && data.actionErrors) {
			for (i=0; i<data.actionErrors.length; ++i) {
				this.addDismissableError(data.actionErrors[i]);
			}
		}
		if (data && data.actionMessages) {
			for (i=0; i<data.actionMessages.length; ++i) {
				this.addDismissableInfo(data.actionMessages[i]);
			}
		}
		return (data && data.actionErrors && data && data.actionErrors.length > 0);
	};


	if (context) {
		context.EntandoAlert = EntandoAlert;
	}

	return EntandoAlert;
})(window);
