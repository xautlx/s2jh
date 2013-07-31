/*jquery.cascade.ui.ext.js */
/*
 * jQuery UI cascade
 * version: 1.1 (5/20/2008)
 * @requires: jQuery v1.2 or later
 * adapted from Yehuda Katz, Rein Henrichs autocomplete plugin
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *  depends on templating  plugin if using with templateText
 * Copyright 2008 Mike Nichols
 */

;(function($) {
	$.ui = $.ui || {};
	$.ui.cascade = $.ui.cascade || {};
	$.ui.cascade.ext = $.ui.cascade.ext || {};
	$.ui.cascade.event = $.ui.cascade.event || {};

	$.ui.cascade.ext.ajax = function(opt) {				
		var ajax = opt.ajax;//ajax options hash...not just the url
		return { getList: function(parent) { 					
			var _ajax = {};
			var $this = $(this);//child element
			var data;
			if(jQuery.isFunction(ajax.data)){
				data=ajax.data();
			}else{
				data=ajax.data;
			}
			var defaultAjaxOptions = {
				type: "GET",
				dataType: "json",
				success: function(json) { $this.trigger("updateList", [json]); },
				data: $.extend(_ajax.data,data,{ val: opt.getParentValue(parent) })				
			};						
			//overwrite opt.ajax with required props (json,successcallback,data)		
			//this lets us still pass in handling the other ajax callbacks and options
			$.extend(_ajax,ajax,defaultAjaxOptions);	
			
			$.ajax(_ajax);		 		  
		} };
	};

	$.ui.cascade.ext.templateText = function(opt) {
	var template = $.makeTemplate(opt.templateText, "<%", "%>");
	return { template: function(obj) { return template(obj); } };
	};	
	
	/*these events are bound on every instance...so the indicator appears  on each target */
	/* 
	*	CSS: .cascade-loading: { background: transparent url("${staticDir}/Content/images/indicator.gif") no-repeat center; }
	*/
	$.ui.cascade.event.loading = function(e,source) { 		
		$(this).empty();				
		var position = {
			'z-index':'6000',
			'position':'absolute',
			'width':'16px'
		};				
		$.extend(position,$(this).offset());						
		position.top = position.top + 3;
		position.left = position.left + 3;				
		$("<div class='cascade-loading'>&nbsp;</div>").appendTo("body").css(position);
		$(this)[0].disabled = true;				
	};
	$.ui.cascade.event.loaded = function(e,source) { 		
		$(this)[0].disabled = false;
		$(".cascade-loading").remove();		
	};

})(jQuery);