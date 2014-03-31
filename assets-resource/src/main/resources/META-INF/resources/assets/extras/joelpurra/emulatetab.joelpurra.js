/*!
* @license EmulateTab
* Copyright (c) 2011, 2012 The Swedish Post and Telecom Authority (PTS)
* Developed for PTS by Joel Purra <http://joelpurra.se/>
* Released under the BSD license.
*
* A jQuery plugin to emulate tabbing between elements on a page.
*/

/*jslint vars: true, white: true, browser: true*/
/*global jQuery*/

// Set up namespace, if needed
var JoelPurra = JoelPurra || {};

(function ($, namespace)
{

    namespace.EmulateTab = function ()
    {
    };

    var eventNamespace = ".EmulateTab";

    // TODO: get code for :focusable, :tabbable from jQuery UI?
    var focusable = ":input, a[href]";

    // Keep a reference to the last focused element, use as a last resort.
    var lastFocusedElement = null;

    // Private methods
    {
        function escapeSelectorName(str) {

            // Based on http://api.jquery.com/category/selectors/
            // Still untested
            return str.replace(/(!"#$%&'\(\)\*\+,\.\/:;<=>\?@\[\]^`\{\|\}~)/g, "\\\\$1");
        }

        function findNextFocusable($from, offset) {

            var $focusable = $(focusable)
                        .not(":disabled")
                        .not(":hidden")
                        .not("a[href]:empty");

            if ($from[0].tagName === "INPUT"
                && $from[0].type === "radio"
                && $from[0].name !== "") {

                var name = escapeSelectorName($from[0].name);

                $focusable = $focusable
                        .not("input[type=radio][name=" + name + "]")
                        .add($from);
            }

            var currentIndex = $focusable.index($from);

            var nextIndex = (currentIndex + offset) % $focusable.length;

            if (nextIndex <= -1) {

                nextIndex = $focusable.length + nextIndex;
            }

            var $next = $focusable.eq(nextIndex);

            return $next;
        }

        function focusInElement(event) {

            lastFocusedElement = event.target;
        }

        function tryGetElementAsNonEmptyJQueryObject(selector) {

            try {

                var $element = $(selector);

                if (!!$element
                    && $element.size() !== 0) {

                    return $element;
                }

            } catch (e) {

                // Could not use element. Do nothing.
            }

            return null;
        }

        // Fix for EmulateTab Issue #2
        // https://github.com/joelpurra/emulatetab/issues/2
        // Combined function to get the focused element, trying as long as possible.
        // Extra work done trying to avoid problems with security features around
        // <input type="file" /> in Firefox (tested using 10.0.1).
        // http://stackoverflow.com/questions/9301310/focus-returns-no-element-for-input-type-file-in-firefox
        // Problem: http://jsfiddle.net/joelpurra/bzsv7/
        // Fixed:   http://jsfiddle.net/joelpurra/bzsv7/2/
        function getFocusedElement() {

            var $focused = null

                // Try the well-known, recommended method first.
                || tryGetElementAsNonEmptyJQueryObject(':focus')

                // Fall back to a fast method that might fail.
                // Known to fail for Firefox (tested using 10.0.1) with
                // Permission denied to access property 'nodeType'.
                || tryGetElementAsNonEmptyJQueryObject(document.activeElement)

                // As a last resort, use the last known focused element.
                // Has not been tested enough to be sure it works as expected
                // in all browsers and scenarios.
                || tryGetElementAsNonEmptyJQueryObject(lastFocusedElement)
                
                // Empty fallback
                || $();

            return $focused;
        }

        function emulateTabbing($from, offset) {

            var $next = findNextFocusable($from, offset);

            $next.focus();
        }

        function initializeAtLoad() {

            // Start listener that keep track of the last focused element.
            $(document)
                .on("focusin" + eventNamespace, focusInElement);
        }
    }

    // Public functions
    {
        namespace.EmulateTab.forwardTab = function ($from) {

            return namespace.EmulateTab.tab($from, +1);
        };

        namespace.EmulateTab.reverseTab = function ($from) {

            return namespace.EmulateTab.tab($from, -1);
        };

        namespace.EmulateTab.tab = function ($from, offset) {

            // Tab from focused element with offset, .tab(-1)
            if ($.isNumeric($from)) {

                offset = $from;
                $from = undefined;
            }

            $from = $from || namespace.EmulateTab.getFocused();

            offset = offset || +1;

            emulateTabbing($from, offset);
        };

        namespace.EmulateTab.getFocused = function () {

            return getFocusedElement();
        };

        $.extend({
            emulateTab: function ($from, offset) {

                return namespace.EmulateTab.tab($from, offset);
            }
        });

        $.fn.extend({
            emulateTab: function (offset) {

                return namespace.EmulateTab.tab(this, offset);
            }
        });
    }

    // EmulateTab initializes listener(s) when jQuery is ready
    $(initializeAtLoad);

} (jQuery, JoelPurra));