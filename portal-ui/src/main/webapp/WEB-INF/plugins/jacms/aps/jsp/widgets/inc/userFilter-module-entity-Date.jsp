<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<wp:info key="currentLang" var="currentLang" />

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
		dateFormat: 'yy-mm-dd',
		firstDay: 1,
		isRTL: false,
		showMonthAfterYear: false,
		yearSuffix: ''};
});

jQuery(function($){
	if (Modernizr.touch && Modernizr.inputtypes.date) {
		$.each(	$("input[data-isdate=true]"), function(index, item) {
			item.type = 'date';
		});
	} else {
		$.datepicker.setDefaults( $.datepicker.regional[ "<c:out value="${currentLang}" />" ] );
		$("input[data-isdate=true]").datepicker({
      			changeMonth: true,
      			changeYear: true,
      			dateFormat: "yyyy-mm-dd"
    		});
	}
});
</c:set>
<wp:headInfo type="JS" info="entando-misc-html5-essentials/modernizr-2.5.3-full.js" />
<wp:headInfo type="JS_EXT" info="http://code.jquery.com/ui/1.10.0/jquery-ui.min.js" />
<wp:headInfo type="CSS_EXT" info="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.min.css" />
<wp:headInfo type="JS_RAW" info="${js_for_datepicker}" />

<fieldset>
<legend>
<c:set var="i18n_Attribute_Key" value="${userFilterOptionVar.attribute.name}" />
<wp:i18n key="${i18n_Attribute_Key}" />
</legend>

<div class="control-group">
	<c:set var="formFieldStartNameVar" value="${userFilterOptionVar.formFieldNames[0]}" />
	<label for="<c:out value="${formFieldStartNameVar}" />" class="control-label">
		<wp:i18n key="DATE_FROM" />
	</label>
	<div class="controls">
		<input id="<c:out value="${formFieldStartNameVar}" />" name="<c:out value="${formFieldStartNameVar}" />" value="${userFilterOptionVar.formFieldValues[formFieldStartNameVar]}" type="text" data-isdate="true" class="input-xlarge" />
	</div>
</div>
<div class="control-group">
	<c:set var="formFieldEndNameVar" value="${userFilterOptionVar.formFieldNames[1]}" />
	<label for="<c:out value="${formFieldEndNameVar}" />" class="control-label">
		<wp:i18n key="DATE_TO" />
	</label>
	<div class="controls">
		<input id="<c:out value="${formFieldEndNameVar}" />" name="<c:out value="${formFieldEndNameVar}" />" value="${userFilterOptionVar.formFieldValues[formFieldEndNameVar]}" type="text" data-isdate="true" class="input-xlarge" />
	</div>
</div>

</fieldset>

