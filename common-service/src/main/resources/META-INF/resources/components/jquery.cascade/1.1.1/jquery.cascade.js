/*jquery.cascade.js */
/*
 * jQuery UI cascade
 * version: 1.1.1 (6/16/2008)
 * @requires: jQuery v1.2 or later
 * adapted from Yehuda Katz, Rein Henrichs autocomplete plugin
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *
 * Copyright 2008 Mike Nichols
   	
 */

;(function($) {
  
  $.ui = $.ui || {}; $.ui.cascade = $.ui.cascade || {}; 
  
  $.fn.cascade = function(parent,url,opt) {    
	if( opt && opt.event ){
		//namespace our event 
		opt.event = opt.event.replace('.cascade','') + '.cascade';
	}
	
    opt = $.extend({
            ajax: { 
                url: url
            }
    }, {	  
		list: [], //static list to use as datasource 
		timeout: 10,//delay before firing getList operation
		getList: function(select) { $(this).trigger("updateList", [opt.list]); }, //function to fetch datasource
		template: function(item) { return "<option value='" + item.value + "'>" + item.label + "</option>" },//applied to each item in datasource      
		match: function(selectedValue) { return true;}, //'this' is the js object, or the current list item from 'getList',
		event: "change.cascade",	//event to listen on parent which fires the cascade
		getParentValue: function(parent) { return $(parent).val(); } //delegate for retrieving the parent element's value
    }, opt);

    if($.ui.cascade.ext) {
		for(var ext in $.ui.cascade.ext) {
	        if(opt[ext]) {
	          opt = $.extend(opt, $.ui.cascade.ext[ext](opt));
	          delete opt[ext];
	        }
		} 
	}
	
    return this.each(function() {  
		var source = $(parent);				
		var self = $(this);		  
		
		//bind any events in extensions to each instance
		if($.ui.cascade.event) {
			for(var e in $.ui.cascade.event) {					
				self.bind(e + ".cascade",[source],$.ui.cascade.event[e]);		        
			} 
		}
		
		$(source).bind(opt.event,function() {							
			self.trigger("loading.cascade",[source[0]]);
			
			var selectTimeout = $.data(self, "selectTimeout");			
			if(selectTimeout) { window.clearInterval(selectTimeout); }			
			$.data(self, "selectTimeout", window.setTimeout(function() { 				
					self.trigger("cascade"); 
			}, opt.timeout));					
			
		});
		
        self.bind("cascade", function() {	
          self.one("updateList", function(e, list) {	
            list = $(list)
              .filter(function() { return opt.match.call(this, opt.getParentValue(parent)); })
              .map(function() { 
                var node = $(opt.template(this))[0];				                
                return node; 
              });
			  
			self.empty();//clear the source/select
			
            if(list.length){ 
				self.html(list);				
			}
			
			self.trigger("loaded.cascade",[source[0]]);//be sure to fire even if there is no data
			
			if( self.is(":input") ) {
				self.trigger("change.cascade");
			}						
          });
		  
          opt.getList.call(self[0],source);	//call with child element as this
		  
        });
    });
  };
  
})(jQuery);