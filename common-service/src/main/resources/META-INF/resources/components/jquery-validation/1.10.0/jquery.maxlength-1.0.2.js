/**
  * jQuery Maxlength plugin 1.0.2
  *
  * http://www.anon-design.se
  *
  * Copyright (c) 2008 Emil Stjerneman <emil@anon-design.se>
  * 
  * Dual licensed under the MIT and GPL licenses:
  * http://www.opensource.org/licenses/mit-license.php
  * http://www.gnu.org/licenses/gpl.html
  */

(function($) 
{

	$.fn.maxlength = function(options)
	{
		var settings = jQuery.extend(
		{
			events:				['maxlengthCheck'], // Array of events to be triggerd
			maxCharacters:		10, // characters limit
			status:				true, // true to show status indicator bewlow the element
			statusClass:		"status", // the class on the status div
			statusPreText:		"剩余可输入字符数", // the status text
			statusText:			"", // the status text
			notificationClass:	"notification",	// Will be added to the emement when maxlength is reached
			showAlert: 			true, // true to show a regular alert message
			alertText:			"文本框内容超过长度限制！" // Text in the alert message
		}, options );
		
		// Add the default event
		$.merge(settings.events, ['keyup']);

		return this.each(function() 
		{
			var item = $(this);
			
			var charactersLength = $(this).val().length;
			
			//Validate
			if(!validateElement()) 
			{
				return false;
			}
			
			// Loop through the events and bind them to the element
			$.each(settings.events, function (i, n) {
				item.bind(n, function(e) {
					charactersLength = item.val().length;					
					checkChars();					
				});
			})

			// Insert the status div
			if(settings.status) 
			{
				item.before($("<div/>").addClass(settings.statusClass).html('-'));
				updateStatus();
			}

			// remove the status div
			if(!settings.status) 
			{
				var removeThisDiv = item.prev("div");
				
				if(removeThisDiv) {
					removeThisDiv.remove();
				}

			}

			function checkChars() 
			{
				var valid = true;
				
				// Too many chars?
				if(charactersLength > settings.maxCharacters) 
				{
					// Too may chars, set the valid boolean to false
					valid = false;
					// Add the notifycation class when we have too many chars
					item.addClass(settings.notificationClass);
					// Cut down the string
					item.val(item.val().substr(0,settings.maxCharacters));
					// Show the alert dialog box, if its set to true
					showAlert();
				} 
				else 
				{
					// Remove the notification class
					if(item.hasClass(settings.notificationClass)) 
					{
						item.removeClass(settings.notificationClass);
					}
				}

				if(settings.status)
				{
					
					updateStatus();
				}
			};
			
			// Update the status text
			function updateStatus()
			{
				var charactersLeft = settings.maxCharacters - charactersLength;
				
				if(charactersLeft < 0) 
				{
					charactersLeft = 0;
				}
				item.prev("div").html(settings.statusPreText+ " " +charactersLeft + " " + settings.statusText);
			};
			
			// Shows an alert msg
			function showAlert() 
			{
				if(settings.showAlert)
				{
					alert(settings.alertText);
				}
			};

			// Check if the element is valid.
			function validateElement() 
			{
				var ret = false;
				
				if(item.is('textarea')) {
					ret = true;
				} else if(item.filter("input[type=text]")) {
					ret = true;
				} else if(item.filter("input[type=password]")) {
					ret = true;
				}

				return ret;
			};

		});
	};
})(jQuery);
