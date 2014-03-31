/*!
* @license PlusAsTab
* Copyright (c) 2011, 2012 The Swedish Post and Telecom Authority (PTS)
* Developed for PTS by Joel Purra <http://joelpurra.se/>
* Released under the BSD license.
*
* A jQuery plugin to use the numpad plus key as a tab key equivalent.
*/

/*jslint vars: true, white: true, browser: true*/
/*global jQuery*/

// Set up namespace, if needed
var JoelPurra = JoelPurra || {};

(function ($, namespace)
{
    namespace.PlusAsTab = {};

    var eventNamespace = ".PlusAsTab";

    // Keys from
    // http://api.jquery.com/event.which/
    // https://developer.mozilla.org/en/DOM/KeyboardEvent#Virtual_key_codes
    var KEY_NUM_PLUS = 107;

    // Add options defaults here
    var internalDefaults = {
        key: KEY_NUM_PLUS
    };

    var options = $.extend(true, {}, internalDefaults);

    var enablePlusAsTab = ".plus-as-tab, [data-plus-as-tab=true]";
    var disablePlusAsTab = ".disable-plus-as-tab, [data-plus-as-tab=false]";

    // Private functions
    {
        function performEmulatedTabbing(isTab, isReverse, $target) {

            isTab = (isTab === true);
            isReverse = (isReverse === true);

            if (isTab
                && $target !== undefined
                && $target.length !== 0) {

                $target.emulateTab(isReverse ? -1 : +1);

                return true;
            }

            return false;
        }

        function isChosenTabkey(key) {
            if (key === options.key
                || ($.isArray(options.key)
                    && $.inArray(key, options.key) !== -1)) {

                return true;
            }

            return false;
        }

        function isEmulatedTabkey(event) {

            // Checked later for reverse tab
            //&& !event.shiftKey

            if (!event.altKey
                && !event.ctrlKey
                && !event.metaKey
                && isChosenTabkey(event.which)) {

                return true;
            }

            return false;
        }

        function checkEmulatedTabKeyDown(event) {

            if (!isEmulatedTabkey(event)) {

                return;
            }

            var $target = $(event.target);

            if ($target.is(disablePlusAsTab)
                || $target.parents(disablePlusAsTab).length > 0
                || (!$target.is(enablePlusAsTab)
                    && $target.parents(enablePlusAsTab).length === 0)) {

                return;
            }

            var wasDone = performEmulatedTabbing(true, event.shiftKey, $target);

            if (wasDone) {

                event.preventDefault();
                event.stopPropagation();
                event.stopImmediatePropagation();

                return false;
            }

            return;
        }
        
        function initializeAtLoad() {

            $(document).on("keydown" + eventNamespace, checkEmulatedTabKeyDown);
        }
    }

    // Public functions
    {
        namespace.PlusAsTab.setOptions = function (userOptions) {

            // Merge the options onto the current options (usually the default values)
            $.extend(true, options, userOptions);
        };

        namespace.PlusAsTab.plusAsTab = function ($elements, enable) {

            enable = (enable === undefined ? true : enable === true);

            return $elements.each(function () {
                    
                var $this = $(this);

                $this
                    .not(disablePlusAsTab)
                    .not(enablePlusAsTab)
                    .attr("data-plus-as-tab", enable ? "true" : "false");
            });
        };

        $.fn.extend({
            plusAsTab: function (enable) {

                return namespace.PlusAsTab.plusAsTab(this, enable);
            }
        });
    }

    // PlusAsTab initializes listeners when jQuery is ready
    $(initializeAtLoad);

}(jQuery, JoelPurra));