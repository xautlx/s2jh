$(function() {
    $(".grid-auth-department-index").data("gridOptions", {
        url : WEB_ROOT + '/auth/department!findByPage',
        colModel : [ {
            label : '名称',
            name : 'title',
            width : 100,
            editable : true,
            editoptions : {
                spellto : 'code'
            },
            align : 'left'
        }, {
            label : '代码',
            name : 'code',
            width : 100,
            editable : true,
            align : 'left'
        }, {
            label : '主管人',
            name : 'manager.id',
            editable : true,
            stype : 'select',
            editoptions : {
                value : Util.getCacheSelectOptionDatas(WEB_ROOT + "/auth/user!findByPage?rows=-1")
            },
            width : 100
        }, {
            label : '联系电话',
            name : 'contactTel',
            width : 150,
            editable : true,
            align : 'left'
        } ],
        postData : {
            "search['FETCH_manager']" : "LEFT"
        },
        multiselect : false,
        subGrid : true,
        gridDnD : true,
        subGridRowExpanded : function(subgrid_id, row_id) {
            Grid.initRecursiveSubGrid(subgrid_id, row_id, "parent.id");
        },
        editcol : 'code',
        editurl : WEB_ROOT + "/auth/department!doSave",
        delurl : WEB_ROOT + "/auth/department!doDelete",
        fullediturl : WEB_ROOT + "/auth/department!inputTabs"
    });
});