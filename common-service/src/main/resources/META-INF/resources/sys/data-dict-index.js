$(function() {
    $(".grid-sys-data-dict-index").data("gridOptions", {
        url : WEB_ROOT + "/sys/data-dict!findByPage",
        colModel : [ {
            label : '主标识',
            name : 'primaryKey',
            align : 'center',
            editable : true,
            width : 100
        }, {
            label : '次标识',
            name : 'secondaryKey',
            align : 'center',
            hidden : true,
            editable : true,
            width : 50
        }, {
            label : '主要数据',
            name : 'primaryValue',
            align : 'center',
            editable : true,
            width : 150
        }, {
            label : '次要数据',
            name : 'secondaryValue',
            align : 'center',
            hidden : true,
            editable : true,
            width : 50
        }, {
            label : '禁用',
            name : 'disabled',
            editable : true,
            edittype : "checkbox"
        }, {
            label : '排序号',
            name : 'orderRank',
            width : 60,
            editable : true,
            sorttype : 'number'
        }, {
            label : '大文本数据',
            name : 'richTextValue',
            width : 200,
            hidden : true,
            editable : true,
            edittype : 'textarea'
        } ],
        sortorder : "desc",
        sortname : 'orderRank',
        multiselect : false,
        subGrid : true,
        gridDnD : true,
        subGridRowExpanded : function(subgrid_id, row_id) {
            Grid.initRecursiveSubGrid(subgrid_id, row_id, "parent.id");
        },
        editurl : WEB_ROOT + "/sys/data-dict!doSave",
        delurl : WEB_ROOT + "/sys/data-dict!doDelete",
        editcol : 'primaryKey',
        inlineNav : {
            add : true
        }
    });
});