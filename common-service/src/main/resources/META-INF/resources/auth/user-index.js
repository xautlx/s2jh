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
            label : '移动电话',
            name : 'mobilePhone',
            editable : true,
            width : 100
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
            name : 'department.id',
            hidden : true,
            hidedlg : true,
            editable : true
        }, {
            label : '所属部门',
            name : 'department.pathDisplay',
            index : 'department.code_OR_department.title',
            width : 120,
            sortable : false,
            editable : true,
            align : 'left',
            editoptions : {
                dataInit : function(elem, opt) {
                    $(elem).treeselect({
                        url : WEB_ROOT + "/auth/department!treeData"
                    });
                }
            }
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