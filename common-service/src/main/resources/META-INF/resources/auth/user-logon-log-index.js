$(function() {
    $(".grid-auth-user-logon-log-index").data("gridOptions", {
        url : WEB_ROOT + "/auth/user-logon-log!findByPage",
        colModel : [ {
            label : '失败类型',
            name : 'authenticationFailure',
            width : 50,
            edittype : "checkbox"
        }, {
            label : '登录账号',
            name : 'username',
            width : 100,
            align : 'center'
        }, {
            label : '账户编号',
            name : 'userid',
            width : 100,
            hidden : true,
            align : 'left'
        }, {
            label : '登录时间',
            name : 'logonTime',
            sorttype : 'date'
        }, {
            label : '登出时间',
            name : 'logoutTime',
            sorttype : 'date'
        }, {
            label : '登录时长',
            name : 'logonTimeLengthFriendly',
            index : 'logonTimeLength',
            width : 100,
            fixed : true,
            align : 'center'
        }, {
            label : '登录次数',
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
            width : 100,
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
        } ],
        multiselect : false,
        sortorder : "desc",
        sortname : "logonTime"
    });
});