<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/index-header.jsp"%>
</head>
<body>
	<div class="container-fluid">
		<div class="row-fluid">
			<table id="userLogonLogListDiv"></table>
			<div id="userLogonLogListDivPager"></div>
		</div>
	</div>
	<%@ include file="/common/index-footer.jsp"%>
	<script type="text/javascript">
        $(function() {
            $("#userLogonLogListDiv").grid(
                    {
                        url : "${base}/auth/user-logon-log!findByPage",
                        colNames : [ '操作', '登录账号', '账户编号', '登录时间', '登出时间', '登录时长', '登录次数', 'userAgent', 'xForwardFor', 'localAddr', 'localName', 'localPort', 'remoteAddr', 'remoteHost', 'remotePort',
                                'serverIP', 'Session编号', '创建时间', '版本号' ],
                        colModel : [ {
                            name : 'operaton',
                            align : 'center',
                            fixed : true,
                            hidedlg : true,
                            search : false,
                            width : 30,
                            formatter : function(cellValue, options, rowdata, action) {
                                return $.jgrid.buildButtons([ {
                                    title : "查看",
                                    icon : "icon-book",
                                    onclick : "$.popupViewDialog('${base}/auth/user-logon-log!view?id=" + options.rowId + "')"
                                } ]);
                            }
                        }, {
                            name : 'username',
                            width : 100,
                            align : 'left'
                        }, {
                            name : 'userid',
                            hidden : true,
                            align : 'left'
                        }, {
                            name : 'logonTime',
                            sorttype : 'date'
                        }, {
                            name : 'logoutTime',
                            sorttype : 'date'
                        }, {
                            name : 'logonTimeLengthFriendly',
                            index : 'logonTimeLength',
                            width : 100,
                            fixed : true,
                            align : 'center'
                        }, {
                            name : 'logonTimes',
                            width : 60,
                            sorttype : 'number',
                            align : 'center'
                        }, {
                            name : 'userAgent',
                            hidden : true,
                            align : 'left'
                        }, {
                            name : 'xForwardFor',
                            align : 'left'
                        }, {
                            name : 'localAddr',
                            hidden : true,
                            align : 'left'
                        }, {
                            name : 'localName',
                            hidden : true,
                            align : 'left'
                        }, {
                            name : 'localPort',
                            width : 60,
                            fixed : true,
                            hidden : true,
                            align : 'right'
                        }, {
                            name : 'remoteAddr',
                            hidden : true,
                            align : 'left'
                        }, {
                            name : 'remoteHost',
                            hidden : true,
                            align : 'left'
                        }, {
                            name : 'remotePort',
                            width : 60,
                            fixed : true,
                            hidden : true,
                            align : 'right'
                        }, {
                            name : 'serverIP',
                            hidden : true,
                            align : 'left'
                        }, {
                            name : 'httpSessionId',
                            hidden : true,
                            align : 'left'
                        }, {
                            name : 'createdDate',
                            width : 120,
                            fixed : true,
                            hidden : true,
                            align : 'center'
                        }, {
                            name : 'version',
                            hidden : true,
                            hidedlg : true
                        } ],
                        multiselect : false,
                        sortorder : "desc",
                        sortname : "logonTime",
                        caption : "用户登录登出历史记录列表"
                    });
        });
    </script>
</body>
</html>