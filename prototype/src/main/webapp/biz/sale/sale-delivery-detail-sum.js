$(function() {
    $(".grid-biz-sale-sale-delivery-detail-sum").data("gridOptions", {
        url : WEB_ROOT + "/biz/sale/sale-delivery-detail!findByGroupCommodity",
        colModel : [ {
            name : 'commodity.id',
            hidden : true,
            hidedlg : true
        }, {
            label : '商品编码',
            name : 'commodity.sku',
            width : 50,
            align : 'center'
        }, {
            label : '商品名称',
            name : 'commodity.title',
            width : 200,
            align : 'left'
        }, {
            label : '最低毛利率',
            name : 'minProfitRate',
            width : 40,
            formatter : 'percentage'
        }, {
            label : '最高毛利率',
            name : 'maxProfitRate',
            width : 40,
            formatter : 'percentage'
        }, {
            label : '平均毛利率',
            name : 'avgProfitRate',
            width : 50,
            formatter : 'percentage'
        }, {
            label : '总计销售数量',
            name : 'sum(quantity)',
            width : 50,
            formatter : 'number'
        }, {
            label : '总计销售金额',
            name : 'sum(amount)',
            width : 50,
            formatter : 'currency'
        }, {
            label : '总计毛利金额',
            name : 'sumProfitAmount',
            width : 50,
            formatter : 'currency'
        } ],
        recordtext : '{0} - {1}',
        pgtext : " {0} ",
        sortname : "commodity.barcode",
        footerrow : true,
        footerLocalDataColumn : [ 'sum(profitAmount)', 'sum(quantity)', 'sum(amount)' ],
        postData : {
            "search['EQ_saleDelivery.voucherState']" : 'POST'
        },
        multiselect : false,
        subGrid : true,
        subGridRowExpanded : function(subgrid_id, row_id) {
            var $grid = $(this);
            var rowdata = $grid.getRowData(row_id);
            Grid.initSubGrid(subgrid_id, row_id, {
                url : WEB_ROOT + "/biz/sale/sale-delivery-detail!findByPage",
                colModel : [ {
                    label : '销售单号',
                    name : 'saleDelivery.voucher',
                    width : 128,
                    align : 'center'
                }, {
                    label : '客户',
                    name : 'saleDelivery.customerProfile.display',
                    index : 'saleDelivery.customerProfile.nickName_OR_saleDelivery.customerProfile.trueName',
                    width : 100
                }, {
                    label : '经办人',
                    name : 'saleDelivery.voucherUser.display',
                    index : 'saleDelivery.voucherUser.signinid',
                    width : 120
                }, {
                    label : '凭证日期',
                    name : 'saleDelivery.voucherDate',
                    stype : 'date',
                    width : 100
                }, {
                    label : '数量',
                    name : 'quantity',
                    width : 50,
                    formatter : 'number'
                }, {
                    label : '成本单价',
                    name : 'costPrice',
                    width : 60,
                    formatter : 'currency'
                }, {
                    label : '销售单价',
                    name : 'price',
                    width : 60,
                    formatter : 'currency'
                }, {
                    label : '是否赠品',
                    name : 'gift',
                    width : 50,
                    edittype : 'checkbox'
                }, {
                    label : '原价金额',
                    name : 'originalAmount',
                    width : 60,
                    formatter : 'currency'
                }, {
                    label : '折扣率(%)',
                    name : 'discountRate',
                    width : 50,
                    hidden : true,
                    formatter : 'number'
                }, {
                    label : '折扣额',
                    name : 'discountAmount',
                    width : 60,
                    formatter : 'currency'
                }, {
                    label : '折后金额',
                    name : 'amount',
                    width : 60,
                    formatter : 'currency'
                }, {
                    label : '税率(%)',
                    name : 'taxRate',
                    width : 50,
                    hidden : true,
                    formatter : 'number'
                }, {
                    label : '税额',
                    name : 'taxAmount',
                    width : 60,
                    hidden : true,
                    formatter : 'currency'
                }, {
                    label : '含税总金额',
                    name : 'commodityAndTaxAmount',
                    width : 80,
                    hidden : true,
                    formatter : 'currency'
                }, {
                    label : '毛利率',
                    name : 'profitRate',
                    width : 40,
                    formatter : 'percentage'
                }, {
                    label : '毛利额',
                    name : 'profitAmount',
                    width : 40,
                    formatter : 'number'
                }, {
                    label : '发货仓库',
                    name : 'storageLocation.id',
                    width : 80,
                    stype : 'select',
                    formatter : 'select',
                    searchoptions : {
                        value : Biz.getStockDatas()
                    }
                } ],
                postData : {
                    "search['EQ_saleDelivery.voucherState']" : 'POST',
                    "search['EQ_commodity.id']" : rowdata['commodity.id'],
                    "search['BT_saleDelivery.voucherDate']" : $grid.getDataFromBindSeachForm("search['BT_saleDelivery.voucherDate']"),
                    "search['FETCH_commodity']" : "INNER",
                    "search['FETCH_saleDelivery']" : "INNER"
                }
            });
        }
    });
});