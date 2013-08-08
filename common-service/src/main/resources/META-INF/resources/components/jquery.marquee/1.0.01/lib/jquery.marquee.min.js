/*
 * Marquee jQuery Plug-in
 *
 * Copyright 2009 Giva, Inc. (http://www.givainc.com/labs/) 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Date: 2009-05-20
 * Rev:  1.0.01
 */
(function(A){A.marquee={version:"1.0.01"};A.fn.marquee=function(E){var F=typeof arguments[0]=="string"&&arguments[0];var D=F&&Array.prototype.slice.call(arguments,1)||arguments;var C=(this.length==0)?null:A.data(this[0],"marquee");if(C&&F&&this.length){if(F.toLowerCase()=="object"){return C}else{if(C[F]){var B;this.each(function(G){var H=A.data(this,"marquee")[F].apply(C,D);if(G==0&&H){if(!!H.jquery){B=A([]).add(H)}else{B=H;return false}}else{if(!!H&&!!H.jquery){B=B.add(H)}}});return B||this}else{return this}}}else{return this.each(function(){new A.Marquee(this,E)})}};A.Marquee=function(E,Q){Q=A.extend({},A.Marquee.defaults,Q);var O=this,M=A(E),F=M.find("> li"),H=-1,G=false,L=false,N=0;A.data(M[0],"marquee",O);this.pause=function(){G=true;P()};this.resume=function(){G=false;D()};this.update=function(){var R=F.length;F=M.find("> li");if(R<=1){D()}};function K(R){if(F.filter("."+Q.cssShowing).length>0){return false}var T=F.eq(R);if(A.isFunction(Q.beforeshow)){Q.beforeshow.apply(O,[M,T])}var S={top:(Q.yScroll=="top"?"-":"+")+T.outerHeight()+"px",left:0};M.data("marquee.showing",true);T.addClass(Q.cssShowing);T.css(S).animate({top:"0px"},Q.showSpeed,Q.fxEasingShow,function(){if(A.isFunction(Q.show)){Q.show.apply(O,[M,T])}M.data("marquee.showing",false);J(T)})}function J(S,R){if(L==true){return false}R=R||Q.pauseSpeed;if(C(S)){setTimeout(function(){if(L==true){return false}var V=S.outerWidth(),T=V*-1,U=parseInt(S.css("left"),10);S.animate({left:T+"px"},((V+U)*Q.scrollSpeed),Q.fxEasingScroll,function(){I(S)})},R)}else{if(F.length>1){setTimeout(function(){if(L==true){return false}S.animate({top:(Q.yScroll=="top"?"+":"-")+M.innerHeight()+"px"},Q.showSpeed,Q.fxEasingScroll);I(S)},R)}}}function I(R){if(A.isFunction(Q.aftershow)){Q.aftershow.apply(O,[M,R])}R.removeClass(Q.cssShowing);B()}function P(){L=true;if(M.data("marquee.showing")!=true){F.filter("."+Q.cssShowing).dequeue().stop()}}function D(){L=false;if(M.data("marquee.showing")!=true){J(F.filter("."+Q.cssShowing),1)}}if(Q.pauseOnHover){M.hover(function(){if(G){return false}P()},function(){if(G){return false}D()})}function C(R){return(R.outerWidth()>M.innerWidth())}function B(){H++;if(H>=F.length){if(!isNaN(Q.loop)&&Q.loop>0&&(++N>=Q.loop)){return false}H=0}K(H)}if(A.isFunction(Q.init)){Q.init.apply(O,[M,Q])}B()};A.Marquee.defaults={yScroll:"top",showSpeed:850,scrollSpeed:12,pauseSpeed:5000,pauseOnHover:true,loop:-1,fxEasingShow:"swing",fxEasingScroll:"linear",cssShowing:"marquee-showing",init:null,beforeshow:null,show:null,aftershow:null}})(jQuery);