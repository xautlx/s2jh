$(function() {
    $(".grid-biz-stock-stock-in-out").data("gridOptions", {
        url : WEB_ROOT + '/biz/stock/stock-in-out!findByPage',
        colModel : [ {
            label : '主键',
            name : 'id',
            width : 50
        }, {
            label : '时间',
            name : 'createdDate',
            width : 120,
            align : 'center'
        }, {
            label : '商品',
            name : 'commodityStock.commodity.display',
            index : 'commodity.barcode_OR_commodity.title',
            width : 250
        }, {
            label : '库存地',
            name : 'commodityStock.storageLocation.id',
            width : 120,
            editrules : {
                required : true
            },
            stype : 'select',
            searchoptions : {
                value : Biz.getStockDatas()
            },
            align : 'left'
        }, {
            label : '实物量',
            name : 'quantity',
            width : 60,
            tooltips : '本次结余量(之前结余量-本次变更量)',
            formatter : function(cellValue, options, rowdata, action) {
                if (rowdata.diffQuantity < 0) {
                    return rowdata.quantity + "(" + rowdata.originalQuantity + "-" + (-rowdata.diffQuantity) + ")";
                } else {
                    return rowdata.quantity + "(" + rowdata.originalQuantity + "+" + (rowdata.diffQuantity) + ")";
                }
            },
            align : 'right'
        }, {
            label : '锁定量',
            name : 'originalSalingQuantity',
            width : 60,
            tooltips : '本次结余量(之前结余量-本次变更量)',
            formatter : function(cellValue, options, rowdata, action) {
                if (rowdata.diffSalingQuantity < 0) {
                    return rowdata.salingQuantity + "(" + rowdata.originalSalingQuantity + "-" + (-rowdata.diffSalingQuantity) + ")";
                } else {
                    return rowdata.salingQuantity + "(" + rowdata.originalSalingQuantity + "+" + (rowdata.diffSalingQuantity) + ")";
                }
            },
            align : 'right'
        }, {
            label : '在途量',
            name : 'originalPurchasingQuantity',
            width : 60,
            tooltips : '本次结余量(之前结余量-本次变更量)',
            formatter : function(cellValue, options, rowdata, action) {
                if (rowdata.diffPurchasingQuantity < 0) {
                    return rowdata.purchasingQuantity + "(" + rowdata.originalPurchasingQuantity + "-" + (-rowdata.diffPurchasingQuantity) + ")";
                } else {
                    return rowdata.purchasingQuantity + "(" + rowdata.originalPurchasingQuantity + "+" + (rowdata.diffPurchasingQuantity) + ")";
                }
            },
            align : 'right'
        }, {
            label : '凭证号',
            name : 'voucher',
            width : 140,
            align : 'center'
        }, {
            label : '类型',
            name : 'voucherType',
            width : 80,
            align : 'center',
            stype : 'select',
            searchoptions : {
                value : Util.getCacheEnumsByType('voucherTypeEnum')
            }
        }, {
            label : '操作人',
            name : 'createdBy',
            align : 'center',
            width : 50
        }, {
            label : '操作摘要',
            name : 'operationSummary',
            width : 150
        } ],
        sortorder : "desc",
        sortname : "createdDate",
        multiselect : false,
        postData : {
            "search['FETCH_commodityStock.commodity']" : "INNER.INNER",
            "search['FETCH_commodityStock.storageLocation']" : "INNER.INNER"
        }
    });
});