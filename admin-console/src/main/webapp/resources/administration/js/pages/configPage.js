$(function() {

    var serviceUrl = PROPERTY.baseUrl + 'do/rs/PageModel/frames?code=' + PROPERTY.pagemodel,
        addWidgetUrl = PROPERTY.baseUrl + 'do/rs/Page/joinWidget?code=' + PROPERTY.pagemodel,
        moveWidgetUrl = PROPERTY.baseUrl + 'do/rs/Page/moveWidget?code=' + PROPERTY.pagemodel,
        deleteWidgetUrl = PROPERTY.baseUrl + 'do/rs/Page/deleteWidget?code=' + PROPERTY.pagemodel,
        getWidgetListUrl = PROPERTY.baseUrl + 'do/rs/Portal/WidgetType/list.action';


    // contains previous slots HTML
    var gridSlots = {};
    var widgetList = [];


    /**
     * Initializes drag helpers (to call after the widget list is populated)
     */
    function initDragHelpers() {
        // an invisible div to store drag helpers
        var $helperContainer = $('<div/>')
            .addClass('drag-helper-list')
            .css({ display: 'none' });
        $('.widget-square').each(function(index, el) {
            $helperContainer.append($(el).clone().addClass('drag-helper'));
        });

        $('.widget-list').append($helperContainer);
    }

    function initWidgetList() {
        // get the widget list
        $.ajax(getWidgetListUrl, {
            method: 'GET',
            success: function(data) {
                // populate global widgetList
                widgetList = _.flatten(data);
                _.forEach(widgetList, function(widget) {
                    var widgetSquare = $('<div/>')
                        .addClass('widget-square')
                        .attr('data-widget-id', _.escape(widget.key))
                        .append('<i class="widget-icon fa fa-picture-o"></i>')
                        .append('<div class="widget-name">' + _.escape(widget.value) + '</div>');
                    $('.widget-list').append(widgetSquare);
                });
                initDragHelpers();
                setDraggable($('.widget-square'), null);
                initGrid();
            }
        });
    }


    function initGrid() {
        // Initializes the grid
        $.ajax(serviceUrl, {
            method: 'GET',
            success: function(data) {
                updateGridPreview(data);
            },
            error: function() {
                showGridWarning('Error getting template data.');
            }
        });
    }

    function isEmptySlot(slot) {
        return _.isEmpty($(slot).find('.grid-widget'));
    }

    /**
     * Tells if a call response has action errors
     * @param {Object} data - the response
     * @returns {boolean}
     */
    function hasErrors(data) {
        return !_.isEmpty(data.actionErrors);
    }


    /**
     * Shows a warning instead of a grid
     * @param {string} alertText
     */
    function showGridWarning(alertText) {
        var alert = '<div class="alert alert-warning">' +
            '<span class="pficon pficon-warning-triangle-o"></span>' +
            '<strong>' + alertText + '</strong>' +
            '</div>';
        $('.grid-container').html(alert);
    }

    /**
     * Shows a warning instead of a grid
     * @param {string} alertText
     */
    function getMessageText(key, args) {
        var msg = TEXT[key] || '';
        if (_.isArray(args)) {
            for (var i=0; i<args.length; ++i) {
                msg = msg.replace('{' + i + '}', args[i]);
            }
        }
        return msg;
    }


    /**
     * Creates a grid widget element
     * @param {string} widgetCode
     */
    function createGridWidget(widgetCode) {

        var widget = _.find(widgetList, { key: widgetCode });

        var html = '<div>' +
            '<div class="slot-name"></div>' +
            '<i class="remove-btn fa fa-close"></i>' +
            '</div>';

        var $elem = $(html)
            .addClass('grid-widget instance')
            .attr('data-widget-id', widgetCode)
            .append('<i class="widget-icon fa fa-picture-o"></i>')
            .append('<div class="widget-name">' + _.escape(widget.value) + '</div>');


        $elem.find('.remove-btn').click(function() {

            var framePos = +$elem.parent().attr('data-pos');

            // FIXME use styled modal
//            if (!confirm('Delete widget "'+widgetCode+'" from page "'+PROPERTY.code+'" position "'+framePos+'"?')) {
//                return;
//            }
                        var msgDelete = 'Delete widget "' + widgetCode + '" from page "' + PROPERTY.code + '" position "' + framePos + '"?';

                    $.confirm({
                        title: 'Confirm!',
                        content: msgDelete,
                        buttons: {
                            confirm: { 
                                btnClass: 'btn-red',
                                action: function(){
                                    // delete the widget
                                    $.ajax(deleteWidgetUrl, {
                                        method: 'POST',
                                        contentType: 'application/json',
                                        data: JSON.stringify({
                                            pageCode: PROPERTY.code,
                                            frame: framePos
                                        }),
                                        success: function (data) {
                                            if (alertService.showResponseAlerts(data)) {
						return;
                                            }
                                            setEmptySlot($elem.parent());
                                            
                                            // update local draft status
                                            pageData.draftWidgets[framePos] = null;
                                            updatePageStatus(data.page);
                                        }
                                    });
                                }
                            },
                            cancel: {
                                action: function(){
                                    return;
                                }
                            }
                        }
                    });
        });

        return $elem;
    }


    /**
     * Sets the slot name in the widget
     */
    function setSlotName($widget, html) {
        $widget.find('.slot-name').html(html);
    }


    /**
     * Populates a slot with the provided widget
     * @param {jQuery} $slot
     * @param {jQuery} $widget
     */
    function populateSlot($slot, $widget) {
        setDraggable($widget, $slot);
        setSlotName($widget, _.unescape($slot.attr('data-description')));
        $slot.html($widget);
    }

    /**
     * Empties a slot
     * @param {jQuery} $slot
     */
    function setEmptySlot($slot) {
        var key = $slot.attr('data-pos');
        $slot.html(gridSlots[key]);
    }

    /**
     * Creates the grid given the frames data
     */
    function updateGridPreview(data) {

        try {
            var gen = new GridGenerator({
                frames: data,
                rowHeight: 80
            });

            var gridHtml = '<div class="grid-preview">' + gen.getHtml() + '</div>';
            $('.grid-container').html(gridHtml);

            // init original html map (empty slot)
            $('.grid-slot').each(function(index, el){
                var pos = $(el).attr('data-pos');
                gridSlots[pos] = $(el).html();
            });

            // populates the slots
            _.forEach(curWidgets, function (widget) {
                if (widget.widgetType) {
                    var $curWidget = createGridWidget(widget.widgetType);
                    var $slot = $('.grid-slot[data-pos="' + widget.index + '"]');
                    populateSlot($slot, $curWidget);
                }
            });

            $( '.grid-slot' ).droppable({
                accept: function(draggable) {

                    var isFree = isEmptySlot(this) || !isEmptySlot($(draggable).parent()),
                        isWidget = $(draggable).hasClass('widget-square')
                            || $(draggable).hasClass('grid-widget');
                    return isWidget && isFree;
                },
                drop: function (ev, ui) {

                    var $prevSlot = $(ui.draggable).parent(),
                        $curSlot = $(ev.target),
                        $curWidget = $(ui.draggable),
                        curWidgetType = $curWidget.attr('data-widget-id');


                    if ($prevSlot.is($curSlot)) {
                        return;
                    } else {
                        // replaces the grid slot html with the old (empty) one
                        var html = gridSlots[+$prevSlot.attr('data-pos')];
                        $prevSlot.append(html);
                    }




                    // it's a widget square
                    if (!$curWidget.hasClass('instance')) {
                        $curWidget = createGridWidget(curWidgetType);

                        // add the widget
                        $.ajax(addWidgetUrl, {
                            method: 'POST',
                            contentType : 'application/json',
                            data: JSON.stringify({
                                pageCode: PROPERTY.code,
                                widgetTypeCode: curWidgetType,
                                frame: +$curSlot.attr('data-pos')
                            }),
                            success: function(data) {
                                if (hasErrors(data)) {
                                    return;
                                }
                                // widget needs configuration
                                if (data.redirectLocation) {
                                    window.location = PROPERTY.baseUrl + data.redirectLocation.replace(/^\//, '');
                                    return;
                                }

                                // no need for configuration
                                populateSlot($curSlot, $curWidget);
                            }
                        });
                    } else {

                        // move/swap the widget
                        $.ajax(moveWidgetUrl, {
                            method: 'POST',
                            contentType : 'application/json',
                            data: JSON.stringify({ swapWidgetRequest: {
                                pageCode: PROPERTY.code,
                                src: +$prevSlot.attr('data-pos'),
                                dest: +$curSlot.attr('data-pos')
                            }}),
                            success: function(data) {
                                if (hasErrors(data)) {
                                    return;
                                }
                                var $prevWidget = $curSlot.find('.grid-widget');
                                setEmptySlot($prevSlot);
                                setEmptySlot($curSlot);


                                if (!_.isEmpty($prevWidget)) {
                                    var $otherWidget = createGridWidget($prevWidget.attr('data-widget-id'));
                                    populateSlot($prevSlot, $otherWidget);
                                }

                                var $newCurWidget = createGridWidget(curWidgetType);
                                populateSlot($curSlot, $newCurWidget);
                            }
                        });
                    }

                }
            });

        } catch (e) {
            console.error(e, e.data);
            var alertText;
            switch (e.type) {
                case GridGenerator.ERROR.OVERLAPPING_FRAMES:
                    alertText = getMessageText('error.grid.overlappingFrames',
                        [e.data[0].a.description, e.data[0].b.description]);
                    break;
                case GridGenerator.ERROR.MALFORMED_FRAMES:
                    alertText = getMessageText('error.grid.malformedFrames', [e.data[0].description]);
                    break;
                default:
                    alertText = getMessageText('error.grid.gridError');
                    break;

            }

            showGridWarning(alertText);
        }

    }


    function setDraggable(selector) {
        $(selector).draggable({
            helper: function() {
                var id = $(this).attr('data-widget-id');
                return $('.drag-helper[data-widget-id="'+ id +'"]').clone();
            },
            cursorAt: { left: 30, top: 30 },
            revert: 'invalid'
        });
    }

    initWidgetList();




});//domready