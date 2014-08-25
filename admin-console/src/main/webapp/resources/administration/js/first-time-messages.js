jQuery(function($){

	var timout = null;

	var firstTimeMessagesEl = $('#first-time-messages');
	var undoEl = $('#first-time-messages-undo');
	var callUpdate = function(value, completeCallback) {
		$.ajax({
			data: { "firstTimeMessages": value},
			url: Entando.info.applicationBaseURL+'do/BaseAdmin/Ajax/updateSystemParams',
			cache: false,
			crossoDomain: false,
			beforeSend: function() {
			},
			complete: completeCallback,
			error: function(jqXHR, textStatus, errorThrown) {
				//window.location.reload(true);
				//console.log('error', textStatus, errorThrown);
			},
			processData: true,
			type: 'post',
			dataType: 'json'
		});
	};

	$(document).delegate('#first-time-messages', 'closed.bs.alert', function(ev){
		callUpdate(false, function(resp, status) {
			if (status == 'success') {
				undoEl.appendTo('#sidebar');
				undoEl.removeClass('hide');
				undoEl.addClass('in');
				undoEl.removeClass('out');
				if (timout!==null) {
					clearInterval(timeout);
				}
				var timeout = setTimeout(function() {
					//undoEl.addClass('out');
					undoEl.remove();
				},50000);
			}
		});
	});

	$(document).delegate('#first-time-messages-undo', 'closed.bs.alert', function(ev){
		callUpdate(true, function(resp, status) {
			if (status == 'success') {
				firstTimeMessagesEl.appendTo('#sidebar');
				firstTimeMessagesEl.removeClass('hide');
				firstTimeMessagesEl.addClass('in');
				firstTimeMessagesEl.removeClass('out');
			}
		});
	});

});