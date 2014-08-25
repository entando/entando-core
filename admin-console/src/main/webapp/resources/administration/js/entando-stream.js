jQuery(function(){ //dom is ready...
//settings
	var $ = jQuery;
	var WINDOW_TITLE_DEFAULT = window.document.title;
	var routineInterval = null;
	var CLOCK = 4 * 1000;
	var ANIMATION_DURATION = 600;
	var TIMESTAMP_ATTR = 'data-entando-creationdate';
	var TIMESTAMP_LAST_UPDATE_ATTR = 'data-entando-updatedate';
	var ID_ATTR = 'data-entando-id';
	var COMMENT_ID_ATTR = 'data-entando-commentid';
	var AJAX_UPDATE_SELECTOR = '[data-entando="ajax-update"]';
	var STREAM_ROOT = $('#activity-stream');
	var STREAM_UPDATE_EL = $('#stream-updates-alert');
	var TMP_CONTAINER = $('<ul class="ajax tmp"></ul>');
	var LOAD_MORE_BUTTON_EL = $('[data-entando="load-more-button"]');
	var STREAM_ITEM_EL_SELECTOR = 'li'
	var LIST_UPDATE_URL = Entando.backoffice.stream.list.updateUrl;
	var LIST_LOAD_MORE_URL = Entando.backoffice.stream.list.loadMoreUrl;
	var COMMENT_ADD_URL = Entando.backoffice.stream.comments.addUrl;
	var COMMENT_DELETE_URL = Entando.backoffice.stream.comments.deleteUrl;


//utility
	var dateFromString = function(myString) {
		//example string 2014-01-15 10:01:08|423
		var ts = new Date(
			myString.substring(0,4),
			new Number(myString.substring(5,7))-1,
			myString.substring(8,10),
			myString.substring(11,13),
			myString.substring(14,16),
			myString.substring(17,19),
			myString.substring(20,23)
		);
		if (!ts.getTime() > 0) { ts = new Date('1970-01-01'); }
		return ts;
	};

	var getTsFromStreamEl = function(streamEl) {
		var attrTm = $(streamEl).attr(TIMESTAMP_ATTR);
		return dateFromString(attrTm);
	};

	var getLastUpdateTs = function(streamEl) {
		var attrTm = $(streamEl).attr(TIMESTAMP_LAST_UPDATE_ATTR);
		return dateFromString(attrTm);
	};

	var getTsStringFromDate = function(date) {
		//var date = new Date(date);
		if (!date.getTime()>0) return;
		return date.getFullYear()
			+'-'+ (date.getMonth()+1<10 ? ('0'+(date.getMonth()+1)) : date.getMonth()+1)
			+'-'+ (date.getDate()<10? ('0'+date.getDate()) : date.getDate())
			+' '+ (date.getHours()<10? ('0'+date.getHours()) : date.getHours())
			+':'+ (date.getMinutes()<10? ('0'+date.getMinutes()) : date.getMinutes())
			+':'+ (date.getSeconds()<10? ('0'+date.getSeconds()) : date.getSeconds())
			+'|'+ ((0<=date.getMilliseconds()&&date.getMilliseconds()<10) ? ('00'+date.getMilliseconds()) :
						(10<=date.getMilliseconds()&&date.getMilliseconds()<100) ? ('0'+date.getMilliseconds()) :
								date.getMilliseconds())
	};
	var setWindowTitle = function(title){
		if (title!==undefined) {
			window.document.title = '('+title+') ' + WINDOW_TITLE_DEFAULT;
		}
		else {
			window.document.title = WINDOW_TITLE_DEFAULT;
		}
	};
	var checkIfNewOrUpdateStreamItem = function(stream) {
		var id = $(stream).attr(ID_ATTR);
		var findstring = STREAM_ITEM_EL_SELECTOR+'['+ID_ATTR+'="'+id+'"]';
		var stream_ts = getTsFromStreamEl(stream);
		var found = 0;
		//try to ignore ignore
		var older_el = $(STREAM_ITEM_EL_SELECTOR+'['+ID_ATTR+']', STREAM_ROOT).last();
		var older_ts = getTsFromStreamEl(older_el);
		//check if its date is older than the last (the older) stream published
		if (older_ts.getTime() > stream_ts.getTime()) {
			return {
				ignore: true,
				update: false,
				newone: false
			};
		}
		else {//(if not ignored) check update
			found = STREAM_ROOT.children(findstring);
			if (found.length == 0) {
				found = STREAM_UPDATE_EL.children(findstring);
			}
			if (found.length > 0) {
				return {
					ignore: false,
					update: true,
					newone: false,
					updateEl: found
				};
			}
			else {
				return {
					ignore: false,
					update: false,
					newone: true
				};
			}
		}
	};


//stream
	var LAST_UPDATE_TS = getLastUpdateTs(STREAM_ROOT.children(STREAM_ITEM_EL_SELECTOR).first());
	var updateStream = function(elementsArray) {
		var els = elementsArray;
		if (els!==undefined) {
			els = els.get();
			if (els.length>0) {
				preUpdate(els);
				$.each(els.reverse(), function(index, item){
					item = $(item);
					if (index==0) {
						var ts = getLastUpdateTs(item);
						if ( ts.getTime() > LAST_UPDATE_TS.getTime() ) {
							LAST_UPDATE_TS = ts;
						}
					}
					var check = checkIfNewOrUpdateStreamItem(item);
					if (check.update) { // update item
						var oldItem = check.updateEl;
						var newItem = item;
						var oldRepl = $(AJAX_UPDATE_SELECTOR, oldItem).get();
						var newRepl  = $(AJAX_UPDATE_SELECTOR, newItem).get();
						$.each(oldRepl, function(index, el) {
							var el = $(el);
							el.replaceWith(newRepl[index])
						})
						els[index]=oldItem;
					}
					else if(check.newone) { //new item
						item.addClass('hide hidden');
						$('.insert-comment.hide.hidden', item).removeClass('hide hidden');
						item.appendTo(STREAM_UPDATE_EL);
					}
					/*
					else if(check.ignore) {
						//do nothing...
					}
					*/
				});
				postUpdate(els);
			}
		}
	};
	var preUpdate = function(elementsArray) {
		var news = 0;
		var newsReadyToGo = STREAM_UPDATE_EL.children(STREAM_ITEM_EL_SELECTOR).get().length;
		$.each(elementsArray, function(index, item){
			var check = checkIfNewOrUpdateStreamItem(item);
			if (check.newone) { news = news +1; }
		});
		var numberEl = $('.n', STREAM_UPDATE_EL);
		numberEl.text(news + newsReadyToGo);
		if (news + newsReadyToGo) {
			STREAM_UPDATE_EL.removeClass('hide hidden');
			setWindowTitle(news + newsReadyToGo);
		}
		else {
			STREAM_UPDATE_EL.addClass('hide hidden');
			setWindowTitle();
		}
	};
	var postUpdate = function(elementsArray){
		TMP_CONTAINER.empty();
		$.each(elementsArray, function(index, item){
			$('[data-toggle="tooltip"]', item).tooltip({trigger: 'hover'});
		});
	};
	var displayUpdates = function(elementsArray) {
		$.each(elementsArray, function(index, item){
			item = $(item);
			item.removeClass('hide hidden');
			item.prependTo(STREAM_ROOT);
		})
		STREAM_UPDATE_EL.addClass('hide hidden');
	};
	var ajaxUpdateStreamRequest = function(data) {
		return $.ajax({
				method: 'post',
				dataType: 'html',
				async: true,
				url: LIST_UPDATE_URL,
				data: data || {
					ajax: true,
					timestamp: getTsStringFromDate(LAST_UPDATE_TS)
				},
				success: function(data, textStatus, jqXHR) {
					var streamElements = TMP_CONTAINER.html(data).children(STREAM_ITEM_EL_SELECTOR);
					updateStream(streamElements);
				}
			})
	};
	var askForUpdate = function() { ajaxUpdateStreamRequest(); }
	var startRoutine = function() {
		routineInterval = setInterval(askForUpdate, CLOCK);
		return routineInterval;
	};
	var pauseRoutine = function() {
		clearInterval(routineInterval);
	}
	STREAM_UPDATE_EL.on('click touchstart', function(){
		pauseRoutine();
		displayUpdates(STREAM_UPDATE_EL.children(STREAM_ITEM_EL_SELECTOR));
		STREAM_UPDATE_EL.remove(ID_ATTR+'['+STREAM_ITEM_EL_SELECTOR+']');
		setWindowTitle();
		startRoutine();
	});


//comment ajax
	var addCommentLoadingState = function(button, load) {
		var button = $(button);
		if (load == true) {
			button.button('loading');
		}
		else {
			setTimeout(function() {
				button.button('reset');
			}, ANIMATION_DURATION);
		}
	};
	STREAM_ROOT.delegate('.insert-comment textarea', 'keydown', function(ev){
		if(ev.shiftKey===false && ev.keyCode==13){
			ev.preventDefault();
			return false;
		}
	});
	STREAM_ROOT.delegate('.insert-comment form', 'submit', function(ev){
		ev.preventDefault();
		var textarea = $('textarea', ev.target);
		var button = $('[data-entando="add-comment-button"]', ev.target);
		if ($.trim(textarea.val()).length >0) {
			$.ajax({
				url: COMMENT_ADD_URL,
				method: 'post',
				data: $(this).serialize(),
				formButton: button,
				formTextarea: textarea,
				beforeSend: function(jqXHR, settings) {
					pauseRoutine();
					addCommentLoadingState(this.formButton, true);
				},
				success: function() {
					var textarea = $('textarea', ev.target);
					textarea.val("");
					restoreTextareaOverflow(textarea);
					askForUpdate();
				},
				complete: function(jqXHR, textStatus, we) {
					startRoutine();
					addCommentLoadingState(this.formButton);
					this.formTextarea.focus();
				}
			});
		}
	});
	STREAM_ROOT.delegate('[data-entando="delete-comment-ajax"]', 'click touchstart', function(ev){
		ev.preventDefault();
		var button = $(ev.target);
		var commentToDeleteEl = button.parents('['+COMMENT_ID_ATTR+']').first();
		var streamId = commentToDeleteEl.parents(STREAM_ITEM_EL_SELECTOR+'['+ID_ATTR+']').attr(ID_ATTR);
		var commentToDeleteId = commentToDeleteEl.attr(COMMENT_ID_ATTR);
		$.ajax({
			url: COMMENT_DELETE_URL,
			method: 'post',
			dataType: 'json',
			data: {
				'streamRecordId': streamId,
				'commentId': commentToDeleteId
			},
			success: function(data, textStatus, jqXHR) {
				$('['+COMMENT_ID_ATTR+'="'+data.commentId+'"]').slideToggle(ANIMATION_DURATION, function(){
					$(this).remove();
				})
			}
		})
	});
	var checkOverflow = function(el) {
		var curOverflow = el.style.overflow;
		if ( !curOverflow || curOverflow === "visible" )
			el.style.overflow = "hidden";

		var isOverflowing = el.clientWidth < el.scrollWidth
			|| el.clientHeight < el.scrollHeight;

		el.style.overflow = curOverflow;
		return isOverflowing;
	};
	var expandTextareaOverflow = function(el) {
		var textarea = $(el);
		var rows = parseInt(textarea.attr('rows')||0);
		var t = textarea.get(0);
		while(checkOverflow( t ) && rows < 10) {
			rows = rows+1;
			textarea.attr('rows', rows);
		}
	}
	var restoreTextareaOverflow = function(el) {
		var textarea = $(el);
		var rows = parseInt(textarea.attr('rows')||0);
		var t = textarea.get(0);
		rows = rows+1;
		textarea.attr('rows', rows);
		var done = false;
		while(!checkOverflow( t ) && rows > 1) {
			rows = rows-1;
			textarea.attr('rows', rows);
			done = true;
		}
		if (done) {
			textarea.attr('rows', rows+1);
		}
	};
	$('#activity-stream').delegate('.insert-comment textarea', 'keydown', function(ev) {
		expandTextareaOverflow(this);
		if (ev.shiftKey===false && ev.keyCode==13) {
			var form = $(this).parents('form');
			form.submit();
		}
	});
	$('#activity-stream').delegate('.insert-comment textarea', 'cut paste', function(ev) {
		var el = this;
		setTimeout(function(){
			expandTextareaOverflow(el);
		},200);
	});
	$('#activity-stream').delegate('.insert-comment textarea', 'blur', function(ev) {
			restoreTextareaOverflow(this);
	});
	$('#activity-stream').delegate('.insert-comment textarea', 'focus', function(ev) {
		expandTextareaOverflow(this);
	});


//start stream routine
	startRoutine();


//load more
	var updateMoreStream = function(elementsArray) {
		$.each(elementsArray, function(index, item) {
			var item = $(item);
			item.removeClass('hide hidden');
			$('.insert-comment.hide.hidden', item).removeClass('hide hidden');
			item.appendTo(STREAM_ROOT);
		});
	};
	var loadMoreLoadingState = function(load) {
		if (load == true) {
			LOAD_MORE_BUTTON_EL.button('loading');
		}
		else {
			setTimeout(function() {
				LOAD_MORE_BUTTON_EL.button('reset');
			}, ANIMATION_DURATION);
		}
	};
	var ajaxLoadMoreRequest = function() {
		return $.ajax({
			url: LIST_LOAD_MORE_URL,
			method: 'post',
			data: {
				'ajax': true,
				timestamp: STREAM_ROOT.children(STREAM_ITEM_EL_SELECTOR).last().attr(TIMESTAMP_ATTR)
			},
			beforeSend: function(){ loadMoreLoadingState(true) },
			success: function(data, textStatus, jqXHR) {
				var streamElements = $('<ul class="ajax tmp"></ul>').html(data).children(STREAM_ITEM_EL_SELECTOR);
				updateMoreStream(streamElements);
				postUpdate(streamElements);
				loadMoreLoadingState();
			}
		});
	};
	LOAD_MORE_BUTTON_EL.on('click touchstart', function(){
		ajaxLoadMoreRequest();
	});


//like
	STREAM_ROOT.delegate('[data-entando="like-link"]', 'click touchstart', function(ev){
		ev.preventDefault();
		var likeLinkEl = $(ev.target);
		$.ajax({
			url: likeLinkEl.attr('href'),
			method: 'get',
			beforeSend: pauseRoutine,
			success: askForUpdate,
			complete: startRoutine
		});
	});


//...domready
});
