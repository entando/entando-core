$(function() {

    var serviceUrl = PROPERTY.baseUrl + 'do/rs/PageModel/frames?code=' + PROPERTY.pagemodel,
        addWidgetUrl = PROPERTY.baseUrl + 'do/rs/Page/joinWidget?code=' + PROPERTY.pagemodel,
        moveWidgetUrl = PROPERTY.baseUrl + 'do/rs/Page/moveWidget?code=' + PROPERTY.pagemodel,
        deleteWidgetUrl = PROPERTY.baseUrl + 'do/rs/Page/deleteWidget?code=' + PROPERTY.pagemodel;


    // contains previous slots HTML
    var gridSlots = {};



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
     * Creates a widget element from a widget square
     * @param {jQuery} $widgetSquare
     */
    function getGridWidget($widgetSquare) {

        var html = '<div>' +
            '<div class="slot-name"></div>' +
            '<i class="remove-btn fa fa-close"></i>' +
            '</div>';

        var $elem = $(html)
            .addClass('grid-widget instance')
            .attr('data-widget-id', $widgetSquare.attr('data-widget-id'))
            .append($widgetSquare.find('.widget-icon').clone())
            .append($widgetSquare.find('.widget-name').clone());


        $elem.find('.remove-btn').click(function() {

            // delete the widget
            $.ajax(deleteWidgetUrl, {
                method: 'POST',
                contentType : 'application/json',
                data: JSON.stringify({
                    pageCode: PROPERTY.pagemodel,
                    frame: +$elem.parent().attr('data-pos')
                }),
                success: function(data) {
                    setEmptySlot($elem.parent());
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
        $($slot).droppable('enable');
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
                    $curWidget = getGridWidget($('.drag-helper[data-widget-id="'+widget.widgetType+'"]'));
                    $slot = $('.grid-slot[data-pos="' + widget.index + '"]');
                    populateSlot($slot, $curWidget);
                }
            });

            $( '.grid-slot' ).droppable({
                accept: function(draggable) {
                    var isFree = !$(this).hasClass('grid-slot-full'),
                        isWidget = $(draggable).hasClass('widget-square')
                            || $(draggable).hasClass('grid-widget');
                    return isWidget && isFree;
                },
                drop: function (ev, ui) {

                    var $prevSlot = $(ui.draggable).parent(),
                        $curSlot = $(ev.target),
                        $curWidget = $(ui.draggable);


                    if ($prevSlot.is($curSlot)) {
                        return;
                    } else {
                        // replaces the grid slot html with the old (empty) one
                        var html = gridSlots[+$prevSlot.attr('data-pos')];
                        $prevSlot.append(html);
                    }


                    $curSlot.droppable('disable');

                    // it's a widget square
                    if (!$curWidget.hasClass('instance')) {
                        $curWidget = getGridWidget($curWidget);

                        // add the widget
                        $.ajax(addWidgetUrl, {
                            method: 'POST',
                            contentType : 'application/json',
                            data: JSON.stringify({
                                pageCode: PROPERTY.pagemodel,
                                widgetTypeCode: $curWidget.attr('data-widget-id'),
                                frame: +$curSlot.attr('data-pos')
                            }),
                            success: function(data) {
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

                        // move the widget
                        $.ajax(moveWidgetUrl, {
                            method: 'POST',
                            contentType : 'application/json',
                            data: JSON.stringify({ swapWidgetRequest: {
                                pageCode: PROPERTY.pagemodel,
                                src: +$prevSlot.attr('data-pos'),
                                dest: +$curSlot.attr('data-pos')
                            }}),
                            success: function(data) {
                                setEmptySlot($prevSlot);
                                populateSlot($curSlot, $curWidget);
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


    function setDraggable($curWidget, $curSlot) {

        var revertDuration = 400;
        $curWidget.draggable({
            helper: function() {
                var id = $(this).attr('data-widget-id');
                return $('.drag-helper[data-widget-id="'+ id +'"]').clone();
            },
            cursorAt: { left: 30, top: 30 },
            revertDuration: revertDuration,
            revert: function(socketObj) {
                if ($curSlot) {
                    $curSlot.droppable('disable');
                }
                return (socketObj === false);
            },
            start: function(ev, ui) {
                if ($curSlot) {
                    $curSlot.droppable('enable');
                }
            }
        });

    }


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


    // an invisible div to store drag helpers
    $helperContainer = $('<div/>')
        .addClass('drag-helper-list')
        .css({ display: 'none' });
    $('.widget-square').each(function(index, el) {
        $helperContainer.append($(el).clone().addClass('drag-helper'));
    });

    $('.widget-list').append($helperContainer);


    setDraggable($('.widget-square'), null);


});//domready