$(function() {
    $(".grid-biz-finance-biz-trade-unit-selection").data("gridOptions", {
        url : WEB_ROOT + '/biz/finance/biz-trade-unit!findByPage',
        colModel : [ {
            label : '名称',
            name : 'name',
            width : 120,
            align : 'left'
        }, {
            label : '代码',
            name : 'code',
            width : 60,
            align : 'left'
        }, {
            label : '单位类型',
            name : 'type',
            formatter : 'select',
            searchoptions : {
                value : Util.getCacheEnumsByType('bizTradeUnitTypeEnum')
            },
            width : 80,
            align : 'center'
        }, {
            label : '办公电话',
            name : 'officePhone',
            width : 128,
            editable : true,
            align : 'left'
        }, {
            label : '移动电话',
            name : 'mobilePhone',
            width : 128,
            editable : true,
            align : 'left'
        }, {
            name : 'display',
            hidden : true,
            width : 150
        } ],
        rowNum : 10,
        multiselect : false,
        toppager : false,
        onSelectRow : function(id) {
            var $grid = $(this);
            var $dialog = $grid.closest(".modal");
            $dialog.modal("hide");
            var callback = $dialog.data("callback");
            if (callback) {
                var rowdata = $grid.jqGrid("getRowData", id);
                rowdata.id = id;
                callback.call($grid, rowdata);
            }
        }
    });
});
