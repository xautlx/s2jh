$(function() {
    $(".grid-biz-finance-account-subject-index").data("gridOptions", {
        url : WEB_ROOT + '/biz/finance/account-subject!findByPage',
        colModel : [ {
            label : '代码',
            name : 'code',
            width : 60,
            align : 'left',
            editable : true
        }, {
            label : '名称',
            name : 'name',
            width : 80,
            editable : true,
            align : 'left'
        }, {
            label : '描述',
            name : 'memo',
            editable : true
        } ],
        cmTemplate : {
            sortable : false
        },
        sortorder : "asc",
        sortname : 'code',
        multiselect : false,
        subGrid : true,
        subGridRowExpanded : function(subgrid_id, row_id) {
            Grid.initRecursiveSubGrid(subgrid_id, row_id, "parent.id");
        },
        editurl : WEB_ROOT + "/biz/finance/account-subject!doSave",
        delurl : WEB_ROOT + "/biz/finance/account-subject!doDelete",
        inlineNav : {
            add : true
        }
    });
});
