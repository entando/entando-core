+function ($) { "use strict";
	var selector = '[data-swapon]';

	var Swapon = function(el) {
		this.element = $(el);
	}

	Swapon.prototype.swap = function() {
		var $this = this.element;
		var target = $this.attr('data-swapon');

		if (!target) {
			target = $this.attr('data-href')
			target = target && target.replace(/.*(?=#[^\s]*$)/, '') // strip for ie7
		}
		target = $('#'+target);

		var current = $($this);
		var fade = $($this).attr('data-swapon-fade') == 'true' ? true : false;
		if ($this.attr('data-container')) {
			current = $( '#'+$this.attr('data-container') )
		}
		if (fade) {
			var duration = 160;
			var that = $this;
			current.fadeOut({
				duration: duration,
				complete: function() {
					that.trigger('swapon', ['hide']);
					target.fadeIn({
						duration: duration,
						complete: function() {
							target.trigger('swapon', ['show']);
						}
					});
				}
			});
		}
		else {
			current.css('display', 'none');
			$this.trigger('swapon', ['hide']);
			target.css('display', "");
			target.trigger('swapon', ['show']);
		}
	}

	var old = $.fn.swapon;

	$.fn.swapon = function (option) {
		return this.each(function () {
			var $this = $(this)
			var data  = $this.data('bs.swapon');
			if (!data) {
				data = new Swapon(this);
				$this.data('swapon', data);
			}
			if (typeof option == 'string') data[option]();
		})
	}

	$.fn.swapon.Constructor = Swapon

	// Swapon No Conflict
	// ===============

	$.fn.swapon.noConflict = function () {
		$.fn.swapon = old
		return this
	}

	$(document).on('click.bs.swapon', '[data-swapon]', function (e) {
		e.preventDefault();
		$(this).swapon('swap');
	})

}(window.jQuery);