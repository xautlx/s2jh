/**
 *  Zebra_Dialog
 *
 *  A small (4KB minified), compact (one JS file, no dependencies other than jQuery 1.5.2+) and highly configurable
 *  dialog box plugin for jQuery meant to replace JavaScript's "alert" and "confirmation" dialog boxes.
 *
 *  Can also be used as a notification widget - when configured to show no buttons and to close automatically - for updates
 *  or errors, without distracting users from their browser experience by displaying obtrusive alerts.
 *
 *  Features:
 *
 *  -   great looks - out of the box
 *  -   5 types of dialog boxes available: confirmation, information, warning, error and question;
 *  -   easily customizable appearance by changing the CSS file
 *  -   create modal or non-modal dialog boxes
 *  -   easily add custom buttons
 *  -   position the dialog box wherever you want - not just in the middle of the screen
 *  -   use callback functions to handle user's choice
 *  -   works in all major browsers (Firefox, Opera, Safari, Chrome, Internet Explorer 6, 7, 8, 9)
 *
 *  Visit {@link http://stefangabos.ro/jquery/zebra-dialog/} for more information.
 *
 *  For more resources visit {@link http://stefangabos.ro/}
 *
 *  @author     Stefan Gabos <contact@stefangabos.ro>
 *  @version    1.2 (last revision: April 07, 2012)
 *  @copyright  (c) 2011 - 2012 Stefan Gabos
 *  @license    http://www.gnu.org/licenses/lgpl-3.0.txt GNU LESSER GENERAL PUBLIC LICENSE
 *  @package    Zebra_Dialog
 */
;(function($) {

    $.Zebra_Dialog = function() {

        // default options
        var defaults = {

            animation_speed:            250,            //  The speed, in milliseconds, by which the overlay and the
                                                        //  dialog box will be animated when closing.
                                                        //
                                                        //  Default is 250

            auto_close:                 false,          //  The number of milliseconds after which to automatically
                                                        //  close the dialog box or FALSE to not automatically close the
                                                        //  dialog box.
                                                        //
                                                        //  Default is FALSE

            buttons:                    true,           //  Use this for localization and for adding custom buttons.
                                                        //
                                                        //  If set to TRUE, the default buttons will be used, depending
                                                        //  on the type of the dialog box: ['Yes', 'No'] for "question"
                                                        //  type and ['Ok'] for the other dialog box types.
                                                        //
                                                        //  For custom buttons, use an array containing the captions of
                                                        //  the buttons to display: ['My button 1', 'My button 2'].
                                                        //
                                                        //  Set to FALSE if you want no buttons.
                                                        //
                                                        //  Note that when the dialog box is closed as a result of clicking
                                                        //  a button, the "onClose" event is triggered and the callback
                                                        //  function (if any) receives as argument the caption of the
                                                        //  clicked button.
                                                        //
                                                        //  See the comments for the "onClose" event below for more
                                                        //  information.
                                                        //
                                                        //  You can also attach callback functions to individual buttons
                                                        //  by using objects in the form of:
                                                        //
                                                        //  [
                                                        //   {caption: 'My button 1', callback: function() { // code }},
                                                        //   {caption: 'My button 2', callback: function() { // code }}
                                                        //  ]
                                                        //
                                                        //  The main difference is that a callback function attached this
                                                        //  way ia executed as soon as the button is clicked rather than
                                                        //  *after* the dialog box is closed, as it is the case with the
                                                        //  "onClose" event.
                                                        //
                                                        //  Callback functions attached to buttons get as argument the
                                                        //  entire dialog box jQuery object.

            custom_class:                false,         //  An extra class to add to the dialog box's container. Useful
                                                        //  for customizing a dialog box elements' styles at run-time.
                                                        //
                                                        //  For example, setting this value to "mycustom" and in the
                                                        //  CSS file having something like
                                                        //  .mycustom .ZebraDialog_Title { background: red }
                                                        //  would set the dialog box title's background to red.
                                                        //
                                                        //  See the CSS file for what can be changed.
                                                        //
                                                        //  Default is FALSE

            keyboard:                   true,           //  When set to TRUE, pressing the ESC key will close the dialog
                                                        //  box.
                                                        //
                                                        //  Default is TRUE.

            message:                    '',             //  The message in the dialog box - this is passed as argument
                                                        //  when the plugin is called.

            modal:                      true,           //  When set to TRUE there will be a semitransparent overlay
                                                        //  behind the dialog box, preventing users from clicking 
                                                        //  the page's content.
                                                        //
                                                        //  Default is TRUE

            overlay_close:              true,           //  Should the dialog box close when the overlay is clicked?
                                                        //
                                                        //  Default is TRUE

            overlay_opacity:            .9,             //  The opacity of the overlay (between 0 and 1)
                                                        //
                                                        //  Default is .9

            position:                   'center',       //  Position of the dialog box.
                                                        //
                                                        //  Can be either "center" (which would center the dialog box) or
                                                        //  an array with 2 elements, in the form of
                                                        //  {'horizontal_position +/- offset', 'vertical_position +/- offset'}
                                                        //  (notice how everything is enclosed in quotes) where
                                                        //  "horizontal_position" can be "left", "right" or "center",
                                                        //  "vertical_position" can be "top", "bottom" or "middle", and
                                                        //  "offset" represents an optional number of pixels to add/substract
                                                        //  from the respective horizontal or vertical position.
                                                        //
                                                        //  Positions are relative to the viewport (the area of the
                                                        //  browser that is visible to the user)!
                                                        //
                                                        //  Examples:
                                                        //
                                                        //  ['left + 20', 'top + 20'] would position the dialog box in the
                                                        //  top-left corner, shifted 20 pixels inside.
                                                        //
                                                        //  ['right - 20', 'bottom - 20'] would position the dialog box
                                                        //  in the bottom-right corner, shifted 20 pixels inside.
                                                        //
                                                        //  ['center', 'top + 20'] would position the dialog box in
                                                        //  center-top, shifted 20 pixels down.
                                                        //
                                                        //  Default is "center".

            title:                      '',             //  Title of the dialog box
                                                        //
                                                        //  Default is "" (an empty string - no title)

            type:                       'information',  //  Dialog box type.
                                                        //
                                                        //  Can be any of the following:
                                                        //
                                                        //  - confirmation
                                                        //  - error
                                                        //  - information
                                                        //  - question
                                                        //  - warning
                                                        //
                                                        //  If you don't want an icon, set the "type" property to FALSE.
                                                        //
                                                        //  By default, all types except "question" have a single button
                                                        //  with the caption "Ok"; type "question" has two buttons, with
                                                        //  the captions "Ok" and "Cancel" respectively.
                                                        //
                                                        //  Default is "information".

            vcenter_short_message:      true,           //  Should short messages be vertically centered?
                                                        //
                                                        //  Default is TRUE

            width:                      0,              //  By default, the width of the dialog box is set in the CSS
                                                        //  file. Use this property to override the default width at
                                                        //  run-time.
                                                        //
                                                        //  Must be an integer, greater than 0. Anything else will instruct
                                                        //  the script to use the default value, as set in the CSS file.
                                                        //  Value should be no less than 200 for optimal output.
                                                        //
                                                        //  Default is 0 - use the value from the CSS file.

            onClose:                null                //  Event fired when *after* the dialog box is closed.
                                                        //
                                                        //  For executing functions *before* the closing of the dialog
                                                        //  box, see the "buttons" attribute.
                                                        //
                                                        //  The callback function (if any) receives as argument the
                                                        //  caption of the clicked button or boolean FALSE if the dialog
                                                        //  box is closed by pressing the ESC key or by clicking on the
                                                        //  overlay.

        }

        // to avoid confusions, we use "plugin" to reference the current instance of the object
        var plugin = this;

        // this will hold the merged default, and user-provided options
        plugin.settings = {};

        // by default, we assume there are no custom options provided
        options = {};

        // if plugin is initialized so that first argument is a string
        // that string is the message to be shown in the dialog box
        if (typeof arguments[0] == 'string') options.message = arguments[0];
            
        // if plugin is initialized so that first or second argument is an object
        if (typeof arguments[0] == 'object' || typeof arguments[1] == 'object')

            // extend the options object with the user-provided options
            options = $.extend(options, (typeof arguments[0] == 'object' ? arguments[0] : arguments[1]));

        /**
         *  Constructor method
         *
         *  @return object  Returns a reference to the plugin
         */
        plugin.init = function() {

            // the plugin's final properties are the merged default and user-provided options (if any)
            plugin.settings = $.extend({}, defaults, options);

            // check if browser is Internet Explorer 6 and set a flag accordingly as we need to perform some extra tasks
            // later on for Internet Explorer 6
            plugin.isIE6 = ($.browser.msie && parseInt($.browser.version, 10) == 6) || false;

            // if dialog box should be modal
            if (plugin.settings.modal) {

                // create the overlay
                plugin.overlay = jQuery('<div>', {

                    'class':    'ZebraDialogOverlay'

                // set some css properties of the overlay
                }).css({

                    'position': (plugin.isIE6 ? 'absolute' : 'fixed'),  //  for IE6 we emulate the "position:fixed" behaviour
                    'left':     0,                                      //  the overlay starts at the top-left corner of the
                    'top':      0,                                      //  browser window (later on we'll stretch it)
                    'opacity':  plugin.settings.overlay_opacity,        //  set the overlay's opacity
                    'z-index':  9991000                                    //  set a high value for z-index

                });

                // if dialog box can be closed by clicking the overlay
                if (plugin.settings.overlay_close)

                    // when the overlay is clicked
                    // remove the overlay and the dialog box from the DOM
                    plugin.overlay.bind('click', function() {plugin.close()});

                // append the overlay to the DOM
                plugin.overlay.appendTo('body');

            }

            // create the dialog box
            plugin.dialog = jQuery('<div>', {

                'class':        'ZebraDialog' + (plugin.settings.custom_class ? ' ' + plugin.settings.custom_class : '')

            // set some css properties of the dialog box
            }).css({

                'position':     (plugin.isIE6 ? 'absolute' : 'fixed'),  //  for IE6 we emulate the "position:fixed" behaviour
                'left':         0,                                      //  by default, place it in the top-left corner of the
                'top':          0,                                      //  browser window (we'll position it later)
                'z-index':      9991001,                                   //  the z-index must be higher than the overlay's z-index
                'visibility':   'hidden'                                //  the dialog box is hidden for now

            });

            // if a notification message
            if (!plugin.settings.buttons && plugin.settings.auto_close)

                // assign a unique id to each notification
                plugin.dialog.attr('id', 'ZebraDialog_' + Math.floor(Math.random() * 9999999));

            // check to see if the "width" property is given as an integer
            // try to convert to a integer
            var tmp = parseInt(plugin.settings.width);

            // if converted value is a valid number
            if (!isNaN(tmp) && tmp == plugin.settings.width && tmp.toString() == plugin.settings.width.toString() && tmp > 0)

                // set the dialog box's width
                plugin.dialog.css({'width' : plugin.settings.width});

            // if dialog box has a title
            if (plugin.settings.title)

                // create the title
                jQuery('<h3>', {

                    'class':    'ZebraDialog_Title'

                // set the title's text
                // and append the title to the dialog box
                }).html(plugin.settings.title).appendTo(plugin.dialog);

            // create the container of the actual message
            // we save it as a reference because we'll use it later in the "draw" method
            // if the "vcenter_short_message" property is TRUE
            plugin.message = jQuery('<div>', {

                // if a known dialog box type is specified, also show the appropriate icon
                'class':    'ZebraDialog_Body' + (get_type() != '' ? ' ZebraDialog_Icon ZebraDialog_' + get_type() : '')

            });

            // if short messages are to be centered vertically
            if (plugin.settings.vcenter_short_message)

                // create a secondary container for the message and add everything to the message container
                // (we'll later align the container vertically)
                jQuery('<div>').html(plugin.settings.message).appendTo(plugin.message);

            // if short messages are not to be centered vertically
            else

                // add the message to the message container
                plugin.message.html(plugin.settings.message);

            // add the message container to the dialog box
            plugin.message.appendTo(plugin.dialog);

            // get the buttons that are to be added to the dialog box
            var buttons = get_buttons();

            // if there are any buttons to be added to the dialog box
            if (buttons) {

                // create the button bar
                var button_bar = jQuery('<div>', {

                    'class':    'ZebraDialog_Buttons'

                // append it to the dialog box
                }).appendTo(plugin.dialog);

                // iterate through the buttons that are to be attached to the dialog box
                $.each(buttons, function(index, value) {

                    // create button
                    var button = jQuery('<a>', {

                        'href':     'javascript:void(0)',
                        'class':    'ZebraDialog_Button' + index

                    });

                    // if button is given as an object, with a caption and a callback function
                    // set the button's caption
                    if ($.isPlainObject(value)) button.html(value.caption);

                    // if button is given as a plain string, set the button's caption accordingly
                    else button.html(value);

                    // handle the button's click event
                    button.bind('click', function() {

                        // execute the callback function when button is clicked
                        if (undefined != value.callback) value.callback(plugin.dialog);

                        // remove the overlay and the dialog box from the DOM
                        // also pass the button's label as argument
                        plugin.close(undefined != value.caption ? value.caption : value)

                    });

                    // append the button to the button bar
                    button.appendTo(button_bar);

                });

                // since buttons are floated,
                // we need to clear floats
                jQuery('<div>', {

                    'style':    'clear:both'

                }).appendTo(button_bar);

            }

            // insert the dialog box in the DOM
            plugin.dialog.appendTo('body');

            // if the browser window is resized
            $(window).bind('resize', draw);

            // if dialog box can be closed by pressing the ESC key
            if (plugin.settings.keyboard)

                // if a key is pressed
                $(document).bind('keyup', _keyup);

            // if browser is Internet Explorer 6 we attach an event to be fired whenever the window is scrolled
            // that is because in IE6 we have to simulate "position:fixed"
            if (plugin.isIE6)

                // if window is scrolled
                $(window).bind('scroll', _scroll);

            // if plugin is to be closed automatically after a given number of milliseconds
            if (plugin.settings.auto_close !== false) {

                // if, in the meantime, the box is clicked
                plugin.dialog.bind('click', function(e) {

                    // stop the timeout
                    clearTimeout(plugin.timeout);

                    // close the box now
                    plugin.close();

                });

                // call the "close" method after the given number of milliseconds
                plugin.timeout = setTimeout(plugin.close, plugin.settings.auto_close);

            }

            // draw the overlay and the dialog box
            draw();

            // return a reference to the object itself
            return plugin;

        }

        /**
         *  Close the dialog box
         *
         *  @return void
         */
        plugin.close = function(caption) {

            // remove all the event listeners
            if (plugin.settings.keyboard) $(document).unbind('keyup', _keyup);

            if (plugin.isIE6) $(window).unbind('scroll', _scroll);

            $(window).unbind('resize', draw);

            // if an overlay exists
            if (plugin.overlay)

                // animate overlay's css properties
                plugin.overlay.animate({

                    opacity: 0  // fade out the overlay

                },

                // animation speed
                plugin.settings.animation_speed,

                // when the animation is complete
                function() {

                    // remove the overlay from the DOM
                    plugin.overlay.remove();

                });

            // animate dialog box's css properties
            plugin.dialog.animate({

                top: 0,     // move the dialog box to the top
                opacity: 0  // fade out the dialog box

            },

            // animation speed
            plugin.settings.animation_speed,

            // when the animation is complete
            function() {

                // remove the dialog box from the DOM
                plugin.dialog.remove();

                // if a callback function exists for when closing the dialog box
                if (plugin.settings.onClose && typeof plugin.settings.onClose == 'function')

                    // execute the callback function
                    plugin.settings.onClose(undefined != caption ? caption : '');

            });

        }

        /**
         *  Draw the overlay and the dialog box
         *
         *  @return void
         *
         *  @access private
         */
        var draw = function() {

            var

                // get the viewport width and height
                viewport_width = $(window).width(),
                viewport_height = $(window).height(),

                // get dialog box's width and height
                dialog_width = plugin.dialog.width(),
                dialog_height = plugin.dialog.height(),

                // the numeric representations for some constants that may exist in the "position" property
                values = {

                    'left':     0,
                    'top':      0,
                    'right':    viewport_width - dialog_width,
                    'bottom':   viewport_height - dialog_height,
                    'center':   (viewport_width - dialog_width) / 2,
                    'middle':   (viewport_height - dialog_height) / 2

                };

            // reset these values
            plugin.dialog_left = undefined;
            plugin.dialog_top = undefined;

            // if there is an overlay
            // (the dialog box is modal)
            if (plugin.settings.modal)

                // stretch the overlay to cover the entire viewport
                plugin.overlay.css({

                    'width':    viewport_width,
                    'height':   viewport_height

                });

            // if
            if (

                // the position is given as an array
                $.isArray(plugin.settings.position) &&

                // the array has exactly two elements
                plugin.settings.position.length == 2 &&

                // first element is a string
                typeof plugin.settings.position[0] == 'string' &&

                // first element contains only "left", "right", "center", numbers, spaces, plus and minus signs
                plugin.settings.position[0].match(/^(left|right|center)[\s0-9\+\-]*$/) &&

                // second element is a string
                typeof plugin.settings.position[1] == 'string' &&

                // second element contains only "top", "bottom", "middle", numbers, spaces, plus and minus signs
                plugin.settings.position[1].match(/^(top|bottom|middle)[\s0-9\+\-]*$/)

            ) {

                // make sure both entries are lowercase
                plugin.settings.position[0] = plugin.settings.position[0].toLowerCase();
                plugin.settings.position[1] = plugin.settings.position[1].toLowerCase();

                // iterate through the array of replacements
                $.each(values, function(index, value) {

                    // we need to check both the horizontal and vertical values
                    for (var i = 0; i < 2; i++) {

                        // replace if there is anything to be replaced
                        var tmp = plugin.settings.position[i].replace(index, value);

                        // if anything could be replaced
                        if (tmp != plugin.settings.position[i])

                            // evaluate string as a mathematical expression and set the appropriate value
                            if (i == 0) plugin.dialog_left = eval(tmp); else plugin.dialog_top = eval(tmp);

                    }

                });

            }

            // if "dialog_left" and/or "dialog_top" values are still not set
            if (undefined == plugin.dialog_left || undefined == plugin.dialog_top) {

                // the dialog box will be in its default position, centered
                plugin.dialog_left = values['center'];
                plugin.dialog_top = values['middle'];

            }

            // if short messages are to be centered vertically
            if (plugin.settings.vcenter_short_message) {

                var

                    // the secondary container - the one that contains the message
                    message = plugin.message.find('div:first'),

                    // the height of the secondary container
                    message_height = message.height(),

                    // the main container's height
                    container_height = plugin.message.height();

                // if we need to center the message vertically
                if (message_height < container_height)

                    // center the message vertically
                    message.css({

                        'margin-top':   (container_height - message_height) / 2

                    });

            }

            // position the dialog box and make it visible
            plugin.dialog.css({

                'left':         plugin.dialog_left,
                'top':          plugin.dialog_top,
                'visibility':   'visible'

            });

            // move the focus to the first of the dialog box's buttons
            plugin.dialog.find('a[class^=ZebraDialog_Button]:first').focus();

            // if the browser is Internet Explorer 6, call the "emulate_fixed_position" method
            // (if we do not apply a short delay, it sometimes does not work as expected)
            if (plugin.isIE6) setTimeout(emulate_fixed_position, 500);

        }

        /**
         *  Emulates "position:fixed" for Internet Explorer 6.
         *
         *  @return void
         *
         *  @access private
         */
        var emulate_fixed_position = function() {

            var

                // get how much the window is scrolled both horizontally and vertically
                scroll_top = $(window).scrollTop(),
                scroll_left = $(window).scrollLeft();

            // if an overlay exists
            if (plugin.settings.modal)

                // make sure the overlay is stays positioned at the top of the viewport
                plugin.overlay.css({

                    'top':  scroll_top,
                    'left': scroll_left

                });

            // make sure the dialog box always stays in the correct position
            plugin.dialog.css({

                'left': plugin.dialog_left + scroll_left,
                'top':  plugin.dialog_top + scroll_top

            });

        }

        /**
         *  Returns an array containing the buttons that are to be added to the dialog box.
         *
         *  If no custom buttons are specified, the returned array depends on the type of the dialog box.
         *
         *  @return array       Returns an array containing the buttons that are to be added to the dialog box.
         *
         *  @access private
         */
        var get_buttons = function() {

            // if plugin.settings.buttons is not TRUE and is not an array either, don't go further
            if (plugin.settings.buttons !== true && !$.isArray(plugin.settings.buttons)) return false;

            // if default buttons are to be used
            if (plugin.settings.buttons === true)

                // there are different buttons for different dialog box types
                switch (plugin.settings.type) {

                    // for "question" type
                    case 'question':

                        // there are two buttons
                        plugin.settings.buttons = ['Yes', 'No'];

                        break;

                    // for the other types
                    default:

                        // there is only one button
                        plugin.settings.buttons = ['Ok'];

                }

            // return the buttons in reversed order
            // (buttons need to be added in reverse order because they are floated to the right)
            return plugin.settings.buttons.reverse();

        }

        /**
         *  Returns the type of the dialog box, or FALSE if type is not one of the five known types.
         *
         *  Values that may be returned by this method are:
         *  -   Confirmation
         *  -   Error
         *  -   Information
         *  -   Question
         *  -   Warning
         *
         *  @return boolean     Returns the type of the dialog box, or FALSE if type is not one of the five known types.
         *
         *  @access private
         */
        var get_type = function() {

            // see what is the type of the dialog box
            switch (plugin.settings.type) {

                // if one of the five supported types
                case 'confirmation':
                case 'error':
                case 'information':
                case 'question':
                case 'warning':

                    // return the dialog box's type, first letter capital
                    return plugin.settings.type.charAt(0).toUpperCase() + plugin.settings.type.slice(1).toLowerCase();

                    break;

                // if unknown type
                default:

                    // return FALSE
                    return false;

            }

        }

        /**
         *  Function to be called when the "onKeyUp" event occurs
         *
         *  Why as a separate function and not inline when binding the event? Because only this way we can "unbind" it
         *  when we close the dialog box
         *
         *  @return boolean     Returns TRUE
         *
         *  @access private
         */
        var _keyup = function(e) {

            // if pressed key is ESC
            // remove the overlay and the dialog box from the DOM
            if (e.which == 27) plugin.close();

            // let the event bubble up
            return true;

        }

        /**
         *  Function to be called when the "onScroll" event occurs in Internet Explorer 6.
         *
         *  Why as a separate function and not inline when binding the event? Because only this way we can "unbind" it
         *  when we close the dialog box
         *
         *  @return void
         *
         *  @access private
         */
        var _scroll = function() {

            // make sure the overlay and the dialog box always stay in the correct position
            emulate_fixed_position();

        }

        // fire up the plugin!
        // call the "constructor" method
        return plugin.init();

    }

})(jQuery);