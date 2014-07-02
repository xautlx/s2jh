$(function() {
    $(".grid-auth-user-index").data("gridOptions", {
        url : WEB_ROOT + "/auth/user!findByPage",
        colModel : [ {
            label : '登录账号',
            name : 'signinid',
            editable : true,
            editoptions : {
                updatable : false
            },
            width : 120
        }, {
            label : '机构代码',
            name : 'aclCode',
            editable : true,
            width : 120
        }, {
            label : '昵称',
            name : 'nick',
            editable : true,
            width : 120
        }, {
            label : '电子邮件',
            name : 'email',
            editable : true,
            width : 200
        }, {
            label : '启用',
            name : 'enabled',
            editable : true,
            edittype : "checkbox"
        }, {
            label : '账号失效日期',
            name : 'accountExpireTime',
            editable : true,
            sorttype : 'date'
        }, {
            label : '所属部门',
            name : 'department.id',
            editable : true,
            stype : 'select',
            editoptions : {
                value : Util.getCacheSelectOptionDatas(WEB_ROOT + "/auth/department!findByPage?rows=-1")
            },
            width : 150
        }, {
            label : '注册时间',
            name : 'signupTime',
            sorttype : 'date'
        } ],
        editcol : 'signinid',
        inlineNav : {
            add : false
        },
        editurl : WEB_ROOT + "/auth/user!doSave",
        delurl : WEB_ROOT + "/auth/user!doDelete",
        fullediturl : WEB_ROOT + "/auth/user!inputTabs"
    });
});