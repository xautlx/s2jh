/**
 * Wizard plugin for Boostrap Require the carousel plugin
 * 
 * An example of HTML to use it:
 * 
 * <div id="wizard" data-role="wizard"> <div class="active" data-title="step 1">
 * Your content </div>
 * 
 * <div data-title="step 2"> Your content </div> </div>
 * 
 * 
 * Another one:
 * 
 * <div id="wizard" data-role="wizard"> <div class="active" data-title="step 1">
 * Your content </div>
 * 
 * <div data-title="step 2"> Your content </div> </div>
 * 
 * 
 * And an example of JavaScript:
 * 
 * $("#wizard").wizard({ validNextStep: function(index) { return true; },
 * changeStep: function(index) { alert("Step " + (index + 1)) }, finish:
 * function(event) { event.preventDefault(); alert("Finished !") } });
 * 
 */

!function($) {
    "use strict";

    /*
     * WIZARD PUBLIC CLASS DEFINITION ==============================
     */

    var Wizard = function(element, options) {
        var self = this;

        // Base
        this.$element = $(element);
        this.options = $.extend({}, $.fn.wizard.defaults, options);
        this.idx = 0;
        this.maxIdx = 0;

        // Get some informations
        this.$items = $("> div[data-title]", this.$element);
        this.maxIdx = this.$items.length - 1;

        // Initialization
        this.$element.find("> *").wrapAll("<div class='carousel' data-interval='false'></div>");
        this.$element.addClass("wizard");
        this.$base = this.$element.find("> div.carousel");
        this.$base.carousel({
            interval : false
        });

        if (this.$element.attr("id")) {
            this.$base.attr("id", "wizard-" + this.$element.attr("id"));

        } else {
            this.$base.attr("id", "wizard-" + new Date().getTime() + "-" + window.parseInt((Math.random() * 1000)));
        }

        // Wizard header
        this.$wizardHeader = $("<div class='wizard-header'></div>");
        this.$items.each(function(index, item) {
            var $item = $(item), active = index == 0;

            $item.addClass("item");
            active && $item.addClass("active");

            var link =

            self.$wizardHeader.append("<a href='#'" + (active ? " class='active' " : " class='disabled' ") + " data-index='" + index + "'><span class='badge" +  "'>"
                    + (index + 1) + "</span> " + $item.data("title") + "</a>");
        });

        this.$base.before(this.$wizardHeader);

        // Wizard core
        this.$items.wrapAll("<div class='carousel-inner'></div>");

        var elmActionBar = $("<div class='row-fluid'></div>");
        this.$base.after(elmActionBar);

        this.$finish = $("<button type='button' class='btn btn-large pull-right' style='width:120px;margin: 5px'>" + this.options.finishText + "</button>");
        if(!this.options.finishAlwaysEnable){
            this.$finish.attr("disabled", true);
        }        
        this.$finish.on("click", this.options.finish);
        elmActionBar.append(this.$finish);

        this.$next = $("<button type='button' class='btn btn-large pull-right' data-index='next' style='width:120px;margin: 5px'><i class='icon-chevron-right icon-white'></i> " + this.options.nextText
                + "</button>");
        this.$next.attr("href", "#" + this.$base.attr("id"));
        this.$next.bind("click", function() {
            self.next()
        });
        elmActionBar.append(this.$next);

        this.$previous = $("<button type='button' class='btn btn-large pull-right' data-index='prev' style='width:120px;margin: 5px'><i class='icon-chevron-left icon-white'></i> " + this.options.previousText
                + "</button>");
        this.$previous.attr("href", "#" + this.$base.attr("id"));
        this.$previous.attr("disabled", true);
        this.$previous.bind("click", function() {
            self.prev()
        });
        elmActionBar.append(this.$previous);

        // Listeners
        this.$wizardHeader.find("> a").click(function(event) {
            event.preventDefault();
            if ($(this).hasClass("disabled")) {
                return;
            }
            var index = $(this).data("index");
            // self.$element.addClass("animated");
            self.$base.carousel(index);
            self.idx = index;
            self.update();
        });

        // this.$base.on("slid", function(event){
        // window.setTimeout(function(){ self.$element.removeClass("animated");
        // }, 50);
        // });

        this.$element.show();
    };

    Wizard.prototype.update = function() {
        if (this.idx == 0) {
            this.$previous.attr("disabled", true);
            this.$next.attr("disabled", false);
            this.$finish.attr("disabled", true);

        } else if (this.idx == this.maxIdx) {
            this.$previous.attr("disabled", false);
            this.$next.attr("disabled", true);
            this.$finish.attr("disabled", false);
        } else {
            this.$previous.attr("disabled", false);
            this.$next.attr("disabled", false);
            this.$finish.attr("disabled", true);
        }
        if(this.options.finishAlwaysEnable){
            this.$finish.attr("disabled", false);
        } 
        $(".active", this.$wizardHeader).removeClass("active");
        $("a:eq(" + this.idx + ")", this.$wizardHeader).removeClass("disabled");
        $("a:eq(" + (this.idx - 1) + ")", this.$wizardHeader).addClass("done");
        $("a:eq(" + this.idx + ")", this.$wizardHeader).addClass("active");
    };

    Wizard.prototype.prev = function() {
        if (this.idx > 0) {
            this.show(this.idx - 1);
            return false;
        }

        return true;
    };

    Wizard.prototype.next = function() {
        if (this.idx <= this.maxIdx && this.options.validNextStep(this.idx)) {
            var index = this.idx + 1;
            if (index != null && index >= 0 && index <= this.maxIdx) {
                if (!$.isFunction(this.options.changeStep) || this.options.validNextStep(index)) {
                    if ($.isFunction(this.options.changeStep)) {
                        var stepOperation = this.options.changeStep(this.idx);
                        if (stepOperation == undefined || stepOperation.beforeNextShow == undefined 
                                || ($.isFunction(stepOperation.beforeNextShow) && stepOperation.beforeNextShow(this.idx))) {
                            this.$base.carousel(index);
                            this.idx = index;
                            $("a:gt(" + this.idx + ")", this.$wizardHeader).removeClass("done");
                            $("a:gt(" + this.idx + ")", this.$wizardHeader).addClass("disabled");
                            this.update();
                            if (stepOperation && $.isFunction(stepOperation.afterNextShow)) {
                                stepOperation.afterNextShow(this.idx);
                            }
                        }
                    }
                }
            }
            return false;
        }

        return true;
    };

    Wizard.prototype.show = function(idx) {
        if (idx <= this.maxIdx && this.options.validNextStep(idx)) {
            // this.$element.addClass("animated");
            this.$base.carousel(idx);
            this.idx = idx;
            this.update();
            return false;
        }

        return true;
    };

    Wizard.prototype.option = function(options) {
        if (!options) {
            return;
        }

        for ( var i in options) {
            switch (i) {
            case "nextText":
                this.$next.text(options[i]);

            case "previousText":
                this.$previous.text(options[i]);

            case "finishText":
                this.$finish.text(options[i]);

            case "finish":
                this.$finish.off("click", this.options.finish);
                this.$finish.on("click", options[i]);

            default:
                this.options[i] = options[i];
                break;
            }
        }
    };

    /*
     * WIZARD PLUGIN DEFINITION ========================
     */

    $.fn.wizard = function(opts) {
        var args = arguments;
        return this.each(function() {
            var $this = $(this), data = $this.data("wizard"), options = typeof opts == "object" && opts;

            if (data) {
                if (opts === 'show') {
                    var ar = Array.prototype.slice.call(args, 1);
                    data.show(ar[0]);
                } else if (opts === 'loadContent') {
                    var ar = Array.prototype.slice.call(args, 1);
                    LoadContent(ar[0]);
                    return true;
                } else {
                    data.option(options)
                }

            } else {
                $this.data("wizard", new $.fn.wizard.Constructor(this, options));
            }

        });
    };

    $.fn.wizard.defaults = {
        nextText : "下一步",
        previousText : "上一步",
        finishText : "完成",
        finishAlwaysEnable : false,
        validNextStep : function(index) {
            return true;
        },
        changeStep : function(index) {
            return true;
        },
        finish : function(event) {
        }
    };

    $.fn.wizard.Constructor = Wizard;

    /*
     * WIZARD DATA-API ===============
     */

    $(function() {
        $("body").find("div[data-role=wizard], div.wizard").wizard();
    });

}(window.jQuery);