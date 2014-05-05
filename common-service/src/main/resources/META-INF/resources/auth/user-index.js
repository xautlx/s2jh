$(function() {
    $(".grid-auth-user-index").data("gridOptions", {
        url : WEB_ROOT + "/auth/user!findByPage",
        colModel : [ {
            label : '登录账号',
            name : 'signinid',
            editable : true,
            editoptions : {
                title : '创建之后不可修改，请仔细填写',
                dataInit : function(elem) {
                    var $grid = $(this);
                    var $elem = $(elem);
                    var rowdata = $grid.jqGrid('getSelectedRowdata');
                    if (rowdata && rowdata.id) {
                        $elem.attr("disabled", true);
                    }
                }
            },
            width : 120
        }, {
            label : '机构代码',
            name : 'aclCode',
            editable : true,
            editoptions : {
                title : '用于分机构的数据访问控制代码'
            },
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
            editoptions : {
                title : '可用于用户自助找回密码等'
            },
            width : 200
        }, {
            label : '启用',
            name : 'enabled',
            editable : true,
            edittype : "checkbox",
            editoptions : {
                title : '未启用账号无法登录系统'
            }
        }, {
            label : '账号失效日期',
            name : 'accountExpireTime',
            editable : true,
            editoptions : {
                title : '设定账号访问系统的失效日期，为空表示永不失效'
            },
            sorttype : 'date'
        }, {
            label : '所属部门',
            name : 'department.id',
            editable : true,
            stype : 'select',
            editoptions : {
                value : Util.getCacheSelectOptionDatas(WEB_ROOT + "/auth/department!findByPage?rows=-1"),
                title : '设定账号访问系统的失效日期，为空表示永不失效'
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