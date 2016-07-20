	var Entando = Entando || {};
	Entando.UpdateBar = Entando.UpdateBar || function(bars){
		$.each(bars, function(index, bar){
			var bar = $(bar);
			var url = bar.attr('data-entando-progress-url');
			var wipTemplate = bar.attr('data-entando-progress-template-wip')||'{{done}} / {{total}}';
			var doneTemplate = bar.attr('data-entando-progress-template-done')||'ok finished.';
			var errorTemplate = bar.attr('data-entando-progress-template-error')||'error occurred. Try to refresh the page.';
			var delay = parseInt(bar.attr('data-entando-progress-template-done'))||1500;
			$.ajax({
				entandoBarElement: bar,
				wipTemplate: wipTemplate,
				doneTemplate: doneTemplate,
				errorTemplate: errorTemplate,
				delay: delay,
				url: url,
				success: function( data, textStatus, jqXHR ) {
					var resultCompiledTemplate = "";
					if(data.done!=data.total) {
						var percentage = parseInt((data.done*100/data.total));
						resultCompiledTemplate = this.wipTemplate.replace(/\{\{percentage\}\}/g, percentage).replace(/\{\{done\}\}/g, data.done).replace(/\{\{total\}\}/g, data.total);
						this.entandoBarElement.trigger('entando.progress', [ percentage ]);
						setTimeout(Entando.UpdateBar, delay,  this.entandoBarElement);
					}
					else {
						resultCompiledTemplate = this.doneTemplate.replace(/\{\{done\}\}/g, data.done).replace(/\{\{total\}\}/g, data.total);
						var  x = $('[data-dismiss="alert"]', this.entandoBarElement.parent());
						x.removeClass('hidden');
						this.entandoBarElement.trigger('entando.progress', [ 100 ]);
					}
					this.entandoBarElement.html(resultCompiledTemplate);
				},
				error: function( jqXHR, textStatus, errorThrown ){
					this.entandoBarElement.html(errorTemplate);
				}
			})
		})//each
	};
