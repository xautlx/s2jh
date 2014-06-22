$(function() {
    $(".grid-biz-md-commodity-selection").data("gridOptions", {
        url : WEB_ROOT + '/biz/md/commodity!findByPage',
        colModel : [ {
            label : 'SKU编码',
            name : 'sku',
            width : 100,
            align : 'center'
        }, {
            label : '商品名称',
            name : 'title',
            width : 200,
            align : 'left'
        }, {
            label : '成本价',
            name : 'costPrice',
            width : 60,
            formatter : 'number',
            align : 'right'
        }, {
            label : '销售价',
            name : 'salePrice',
            width : 60,
            formatter : 'number',
            align : 'right'
        }, {
            label : '实物条码',
            name : 'barcode',
            width : 100,
            align : 'center'
        }, {
            label : '不可买',
            name : 'soldOut',
            formatter : 'checkbox',
            align : 'center'
        }, {
            label : '已下架',
            name : 'removed',
            formatter : 'checkbox',
            align : 'center'
        }, {
            name : 'display',
            hidden : true,
            hidedlg : true
        }, {
            name : 'defaultStorageLocation.id',
            hidden : true,
            hidedlg : true
        }, {
            name : 'barcode',
            hidden : true,
            hidedlg : true
        } ],
        postData : {
            "search['FETCH_defaultStorageLocation']" : "LEFT"
        },
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