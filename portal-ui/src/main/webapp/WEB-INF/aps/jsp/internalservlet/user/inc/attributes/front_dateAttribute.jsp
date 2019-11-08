<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<s:if test="#attribute.failedDateString == null">
	<s:set var="dateAttributeValue" value="#attribute.getFormattedDate('dd/MM/yyyy')" />
</s:if>
<s:else>
	<s:set var="dateAttributeValue" value="#attribute.failedDateString" />
</s:else>
	<wpsf:textfield
		useTabindexAutoIncrement="true"
		id="%{attribute_id}"
		name="%{#attributeTracer.getFormFieldName(#attribute)}"
		value="%{#dateAttributeValue}"
		maxlength="10"
		cssClass="text userprofile-date" />
		&#32;

	<c:set var="js_for_datepicker">
		/* Italian initialisation for the jQuery UI date picker plugin. */
		/* Written by Antonello Pasella (antonello.pasella@gmail.com). */
		jQuery(function($){
			$.datepicker.regional['it'] = {
				closeText: 'Chiudi',
				prevText: '&#x3c;Prec',
				nextText: 'Succ&#x3e;',
				currentText: 'Oggi',
				monthNames: ['Gennaio','Febbraio','Marzo','Aprile','Maggio','Giugno',
					'Luglio','Agosto','Settembre','Ottobre','Novembre','Dicembre'],
				monthNamesShort: ['Gen','Feb','Mar','Apr','Mag','Giu',
					'Lug','Ago','Set','Ott','Nov','Dic'],
				dayNames: ['Domenica','Luned&#236','Marted&#236','Mercoled&#236','Gioved&#236','Venerd&#236','Sabato'],
				dayNamesShort: ['Dom','Lun','Mar','Mer','Gio','Ven','Sab'],
				dayNamesMin: ['Do','Lu','Ma','Me','Gi','Ve','Sa'],
				weekHeader: 'Sm',
				dateFormat: 'dd/mm/yy',
				firstDay: 1,
				isRTL: false,
				showMonthAfterYear: false,
				yearSuffix: ''};
		});

		jQuery(function($) {
			if (Modernizr.touch && Modernizr.inputtypes.date) {
				$.each(	$("input.userprofile-date"), function(index, item) {
					item.type = 'date';
				});
			} else {
				$.datepicker.setDefaults( $.datepicker.regional[ "<wp:info key="currentLang" />" ] );
				$("input.userprofile-date").datepicker({
						changeMonth: true,
						changeYear: true,
						dateFormat: "dd/mm/yyyy"
		    		});
			}
		});
	</c:set>
	<wp:headInfo type="JS" info="entando-misc-html5-essentials/modernizr-2.5.3-full.js" />
	<wp:headInfo type="JS_EXT" info="http://code.jquery.com/ui/1.12.1/jquery-ui.min.js" />
	<wp:headInfo type="CSS_EXT" info="http://code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.min.css" />
	<wp:headInfo type="JS_RAW" info="${js_for_datepicker}" />
