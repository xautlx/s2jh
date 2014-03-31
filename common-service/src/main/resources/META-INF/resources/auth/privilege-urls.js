$(function() {
    $(".grid-auth-privilege-urls").data("gridOptions", {
        url : WEB_ROOT + '/auth/privilege!urls',
        colModel : [ {
            label : '模块代码',
            name : 'namespace',
            align : 'left',
            width : 80,
            hidden : true,
            fixed : true
        }, {
            label : '模块',
            name : 'namespaceLabel',
            align : 'left',
            width : 80,
            fixed : true
        }, {
            label : '业务代码',
            name : 'actionName',
            align : 'left',
            width : 80,
            hidden : true,
            fixed : true
        }, {
            label : '业务',
            name : 'actionNameLabel',
            align : 'left',
            width : 100
        }, {
            label : '方法代码',
            name : 'methodName',
            align : 'left',
            hidden : true,
            width : 80
        }, {
            label : '方法',
            name : 'methodNameLabel',
            align : 'left',
            width : 150
        }, {
            label : 'URL',
            name : 'url',
            width : 250,
            align : 'left'
        }, {
            label : '受控',
            name : 'controlled',
            formatter : 'checkbox'
        }, {
            label : '关联权限',
            name : 'controllPrivilegesJoin',
            width : 250,
            align : 'left'
        } ],
        editcol : 'code',
        fullediturl : WEB_ROOT + "/auth/privilege!inputTabs",
        loadonce : true
    });
});