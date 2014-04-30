$(function() {
    $(".grid-auth-signup-user-index").data("gridOptions", {
        url : WEB_ROOT + "/auth/signup-user!findByPage",
        colModel : [ {
            label : '登录账号',
            name : 'signinid',
            width : 120
        }, {
            label : '机构代码',
            name : 'aclCode',
            width : 120
        }, {
            label : '昵称',
            name : 'nick',
            width : 120
        }, {
            label : '电子邮件',
            name : 'email',
            width : 200
        }, {
            label : '注册时间',
            name : 'signupTime',
            sorttype : 'date'
        }, {
            label : '审核时间',
            name : 'auditTime',
            sorttype : 'date'
        } ],
        editcol : 'signinid',
        delurl : WEB_ROOT + "/auth/signup-user!doDelete",
        fullediturl : WEB_ROOT + "/auth/signup-user!edit"
    });
});