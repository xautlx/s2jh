<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/page-header.jsp"%>
<%@ include file="/common/index-header.jsp"%>
<title><s:property value="%{systemTitle}" /></title>
<script src="${base}/components/jquery-xtabpanel/2.0/xTabPanel.js?_=${buildVersion}"></script>
<link rel="stylesheet" type="text/css"
	href="${base}/components/jquery-xtabpanel/2.0/xTabPanel.css?_=${buildVersion}">

<script src="${base}/components/jquery-address/1.5/jquery.address-1.5.js?_=${pageScope.buildVersion}"></script>

<script src="${base}/components/jquery.marquee/1.0.01/lib/jquery.marquee.js?_=${buildVersion}"></script>
<style type="text/css">
ul.marquee {
	/* required styles */
	display: block;
	padding: 2px;
	margin-top: 2px;
	list-style: none;
	line-height: 1;
	position: relative;
	overflow: hidden;
	/* optional styles for appearance */
	width: 300px;
	height: 20px;
	/* height should be included to reserve visual space for the marquee */
	border: 0px;
}

ul.marquee i {
    margin-top: 5px;
}

ul.marquee li {
	/* required styles */
	position: absolute;
	top: -999em;
	left: 0;
	display: block;
	white-space: nowrap; /* keep all text on a single line */
	padding: 3px 5px;
}
</style>

</head>
<body>

	<DIV class="ui-layout-west" style="display: none">
		<DIV class="header">应用菜单列表</DIV>
		<DIV id="menuContentDiv" class="content" style="padding: 0px 0px 4px 0px;">loading menu...</DIV>
	</DIV>

	<DIV class=ui-layout-east style="display: none">
		<DIV class="header">Debug Panel</DIV>
		<DIV class=content1>
			<UL id="debugULDiv" style="overflow: auto">

			</UL>
		</DIV>
	</DIV>

	<DIV class="ui-layout-north" style="overflow: hidden; display: none">
		<div class="navbar">
			<div class="navbar-inner">
				<div class="container">
					<a class="brand" href='javascript:void(0)' onclick="window.location.reload()">&nbsp;<s:property
							value="%{systemTitle}" /></a>
					<div class="nav-collapse collapse navbar-responsive-collapse">
						<ul class="nav pull-right">
							<li class="divider-vertical"></li>
							<li><a href="javascript:void(0)"><s:property value="%{authUserDetails.username}" /></a></li>
							<li><a href="javascript:void(0)" id="switchLayout"><i class="icon-fullscreen"></i>
									切换显示</a></li>
							<li><a href="javascript:void(0)" id="changePasswd"><i class="icon-lock"></i> 修改密码</a></li>
							<li class="divider-vertical"></li>
							<li><a href="javascript:void(0)"
								onclick="if(confirm('确认注销登录吗？')){window.location.href='${base}/j_spring_security_logout';}">
									<i class="icon-off"></i> 注销登录
							</a></li>
						</ul>
						<ul class="marquee pull-right" id="marquee">
							<i class="icon-volume-down" style="display: none"></i>
							<li><a href="javascript:void(0)" class="hide">暂无最新公告</a></li>
						</ul>
					</div>
					<!-- /.nav-collapse -->
				</div>
			</div>
			<!-- /navbar-inner -->
		</div>
		<!-- /navbar -->
	</DIV>

	<DIV class="ui-layout-south" style="display: none; margin: 0; padding: 0;">
		<div class="row-fluid">
			<div class="span3">
				<div id="progressBar"></div>
			</div>
			<div class="span5">
				<span id="messageBar">&nbsp;</span>
			</div>
			<div class="span2">
				<div style="line-height: 15px"><%@ include file="/common/app-ver.jsp"%></div>
			</div>
			<div class="span2">
				<div id="timerDisplayBar" class="pull-right" style="line-height: 15px"></div>
			</div>
		</div>
	</DIV>

	<DIV id="mainContent" style="overflow: hidden; margin: 0; padding: 0; width: 100%;"></DIV>

	<%@ include file="/common/index-footer.jsp"%>

	<script type="text/javascript">
        var outerLayout;
        var tabpanel;

        /*
         *#######################
         * OUTER LAYOUT SETTINGS
         *#######################
         *
         * This configuration illustrates how extensively the layout can be customized
         * ALL SETTINGS ARE OPTIONAL - and there are more available than shown below
         *
         * These settings are set in 'json format' - ALL data must be in a nested data-structures
         * All default settings (applied to all panes) go inside the defaults:{} key
         * Pane-specific settings go inside their keys: north:{}, south:{}, center:{}, etc
         */
        var layoutSettings_Outer = {
            name : "outerLayout" // NO FUNCTIONAL USE, but could be used by custom code to 'identify' a layout
            // options.defaults apply to ALL PANES - but overridden by pane-specific settings
            ,
            defaults : {
                size : "auto",
                minSize : 50,
                paneClass : "pane" // default = 'ui-layout-pane'
                ,
                resizerClass : "resizer" // default = 'ui-layout-resizer'
                ,
                togglerClass : "toggler" // default = 'ui-layout-toggler'
                ,
                buttonClass : "button" // default = 'ui-layout-button'
                ,
                contentSelector : ".content" // inner div to auto-size so only it scrolls, not the entire pane!
                ,
                contentIgnoreSelector : "span" // 'paneSelector' for content to 'ignore' when measuring room for content
                ,
                togglerLength_open : 35 // WIDTH of toggler on north/south edges - HEIGHT on east/west edges
                ,
                togglerLength_closed : 35 // "100%" OR -1 = full height
                ,
                hideTogglerOnSlide : true // hide the toggler when pane is 'slid open'
                ,
                togglerTip_open : "Close This Pane",
                togglerTip_closed : "Open This Pane",
                resizerTip : "Resize This Pane",
                fxName : "slide" // none, slide, drop, scale
                ,
                fxSpeed : "slow" // slow, normal, fast, 1000, nnn
            },
            north : {
                spacing_open : 5 // cosmetic spacing
                ,
                size : 31,
                minSize : 25,
                maxSize : 120,
                togglerAlign_open : "right" // top/left, bottom/right, center, OR...
                ,
                togglerAlign_closed : "right" // 1 => nn = offset from top/left, -1 => -nn == offset from bottom/right
                ,
                resizable : false,
                slidable : false,
                fxName : "none"
            },
            south : {
                maxSize : 18,
                minSize : 18,
                spacing_closed : 0 // HIDE resizer & toggler when 'closed'
                ,
                spacing_open : 1 // cosmetic spacing
                ,
                togglerLength_closed : -1 // "100%" OR -1 = full width of pane
                ,
                slidable : false // REFERENCE - cannot slide if spacing_closed = 0
                ,
                initHidden : false,
                resizable : false,
                fxName : "none"
            },
            west : {
                size : 200,
                spacing_closed : 21 // wider space when closed
                ,
                togglerLength_closed : 21 // make toggler 'square' - 21x21
                ,
                togglerAlign_closed : "top" // align to top of resizer
                ,
                togglerLength_open : 0 // NONE - using custom togglers INSIDE west-pane
                ,
                togglerTip_open : "Close West Pane",
                togglerTip_closed : "Open West Pane",
                resizerTip_open : "Resize West Pane",
                slideTrigger_open : "mouseover" // default
                ,
                initClosed : false,
                onresize_end : function() {
                    $("#menuContentDiv").height($("#menuContentDiv").parent("div").height() - 28);
                    $("#menuAccordion").accordion("refresh");
                }
            },
            east : {
                size : 250,
                spacing_closed : 0 // wider space when closed
                ,
                togglerLength_closed : 0 // make toggler 'square' - 21x21
                ,
                togglerAlign_closed : "top" // align to top of resizer
                ,
                togglerLength_open : 0 // NONE - using custom togglers INSIDE east-pane
                ,
                togglerTip_open : "Close East Pane",
                togglerTip_closed : "Open East Pane",
                resizerTip_open : "Resize East Pane",
                fxName : "none",
                fxSpeed : "normal",
                initClosed : true
            },
            center : {
                paneSelector : "#mainContent" // sample: use an ID to select pane instead of a class
                ,
                onresize_end : function() {
                    $(window).trigger('resize.tabpanel');
                }
            }
        };

        //更新进度条
        function updatePercent() {
            var value = $('#progressBar').progressbar('option', 'value');
            if (value == 100) {
                value = 0;
            }
            $('#progressBar').progressbar('option', 'value', value + 1);
        }

        //启动进度条
        function startProgressBar() {
            $("#progressBar").progressbar({
                value : 0
            });
            window.clearInterval(window.thread);
            window.thread = window.setInterval("updatePercent()", 10);
        }
        //终止进度条
        function stopProgressBar() {
            window.clearInterval(window.thread);
            $('#progressBar').progressbar('option', 'value', 0);
        }
        
        //更新公告
        function updatePubPost() {
            var $marquee = $("#marquee");
            var $lis = $marquee.find("> li");

            $.ajax({
                type : "GET",
                dataType : "json",
                url : "${base}/profile/pub-post!messages",
                success : function(data) {
                    $marquee.find("> li").each(function() {
                        var id = $(this).attr("id");
                        if (id) {
                            var needRemove = true;
                            $.each(data, function(i, item) {
                                if (item.id == id) {
                                    needRemove = false;
                                }
                            });
                            if (needRemove) {
                                $(this).remove();
                            }
                        }
                    });

                    $.each(data, function(i, item) {
                        if ($marquee.find("> li[id='" + item.id + "']").length == 0) {
                            $marquee.append($('<li id="'+item.id+'"><a href="javascript:void(0)">' + item.publishTime +' ' + item.htmlTitle + '</a></li>'));
                        }
                    });
                    $marquee.marquee("update");
                    if ($marquee.find("> li[id]").length == 0) {
                        $marquee.find("> i").hide();
                    } else {
                        $marquee.find("> i").show();
                    }
                },
                error : function(XMLHttpRequest, textStatus, errorThrown) {
                    if(window.pubPostTimer){
                        clearInterval(window.pubPostTimer);
                    }                    
                    alert('系统检测到请求异常，请尝试刷新页面');
                }
            });
        };

        /*
         *#######################
         *     ON PAGE LOAD
         *#######################
         */
        $(document).ready(function() {

            // create the OUTER LAYOUT
            outerLayout = $("body").layout(layoutSettings_Outer);

            /*******************************
             ***  CUSTOM LAYOUT BUTTONS  ***
             *******************************
             *
             * Add SPANs to the east/west panes for customer "close" and "pin" buttons
             *
             * COULD have hard-coded span, div, button, image, or any element to use as a 'button'...
             * ... but instead am adding SPANs via script - THEN attaching the layout-events to them
             *
             * CSS will size and position the spans, as well as set the background-images
             */

            // save selector strings to vars so we don't have to repeat it
            // must prefix paneClass with "body > " to target ONLY the outerLayout panes
            var westSelector = "body > .ui-layout-west"; // outer-west pane
            var eastSelector = "body > .ui-layout-east"; // outer-east pane

            // CREATE SPANs for pin-buttons - using a generic class as identifiers
            $("<span></span>").addClass("pin-button").prependTo(westSelector);
            $("<span></span>").addClass("pin-button").prependTo(eastSelector);
            // BIND events to pin-buttons to make them functional
            outerLayout.addPinBtn(westSelector + " .pin-button", "west");
            outerLayout.addPinBtn(eastSelector + " .pin-button", "east");

            // CREATE SPANs for close-buttons - using unique IDs as identifiers
            $("<span></span>").attr("id", "west-closer").prependTo(westSelector);
            $("<span></span>").attr("id", "east-closer").prependTo(eastSelector);
            // BIND layout events to close-buttons to make them functional
            outerLayout.addCloseBtn("#west-closer", "west");
            outerLayout.addCloseBtn("#east-closer", "east");

            // DEMO HELPER: prevent hyperlinks from reloadin page when 'base.href' is set
            $("a").each(function() {
                var path = document.location.href;
                if (path.substr(path.length - 1) == "#")
                    path = path.substr(0, path.length - 1);
                if (this.href.substr(this.href.length - 1) == "#")
                    this.href = path + "#";
            });

            $("#menuContentDiv").height($("#menuContentDiv").parent("div").height() - 28);
            $("#menuContentDiv").ajaxGetUrl("${base}/layout!menu");

            tabpanel = new TabPanel({
                id : 'jTabPanel',
                renderTo : 'mainContent',
                border : false,
                autoResize : true,
                defaultTab : 0,
                items : [ {
                    id : 'tabpanel_workspace',
                    title : '工作空间',
                    closable : false,
                    html : '<iframe src="${base}/layout!welcome" width="100%" height="100%" frameborder="0" id="tabpanel_workspaceFrame" name="tabpanel_workspaceFrame"></iframe>'
                } ]
            });

            $("#switchLayout").click(function() {
                if (outerLayout.state.west.isClosed) {
                    outerLayout.open('north');
                    outerLayout.open('west');
                } else {
                    outerLayout.close('north');
                    outerLayout.close('west');
                }
            })

            $("#changePasswd").click(function() {
                $.popupDialog({
                    dialogId : 'changePasswdDialog',
                    url : "${base}/auth/profile!passwd",
                    title : "修改登录密码",
                    width : 600,
                    height : 350
                })
            })

            window.pubPostTimer = setInterval(updatePubPost, 5 * 60 * 1000);
            updatePubPost();

            $("#marquee").delegate("li", "click", function() {
                var id = $(this).attr("id");
                $.popupViewDialog('${base}/profile/pub-post!view?id=' + id);
            });

            $("#marquee").marquee();
        });

        //系统时间显示
        var curDateTime = new Date(
    <%=new java.util.Date().getTime()%>
        );
        setInterval(function() {
            curDateTime = new Date(curDateTime.getTime() + 1000);
            var week1 = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");
            var d1 = curDateTime;
            h1 = d1.getHours();
            if (h1 < 10) {
                h1 = "0" + h1
            }
            ;
            m1 = d1.getMinutes();
            if (m1 < 10) {
                m1 = "0" + m1
            }
            s1 = d1.getSeconds();
            if (s1 < 10) {
                s1 = "0" + s1
            }
            ;
            var showDate = d1.getFullYear() + "年" + (d1.getMonth() + 1) + "月" + d1.getDate() + "日   " + week1[d1.getDay()];
            var showTime = h1 + ":" + m1 + ":" + s1;
            $("#timerDisplayBar").html(showDate + "   " + showTime);
        }, 1000);
    </script>
</body>
</html>