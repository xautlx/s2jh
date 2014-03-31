$(function() {
    $(".grid-auth-department-index").data("gridOptions", {
        url : WEB_ROOT + '/auth/department!findByPage',
        colModel : [ {
            label : '代码',
            name : 'code',
            width : 100,
            editable : true,
            align : 'left'
        }, {
            label : '名称',
            name : 'title',
            width : 100,
            editable : true,
            align : 'left'
        }, {
            label : '联系电话',
            name : 'contactTel',
            width : 100,
            editable : true,
            align : 'left'
        } ],
        postData : {
            "search['FETCH_manager']" : "LEFT"
        },
        editcol : 'code',
        editurl : WEB_ROOT + "/auth/department!doSave",
        delurl : WEB_ROOT + "/auth/department!doDelete",
        fullediturl : WEB_ROOT + "/auth/department!inputTabs"
    });
});