$(function() {

    $(".grid-biz-purchase-purchase-order-inputBasic").data("gridOptions", {
        url : function() {
            var pk = $(this).attr("data-pk");
            if (pk) {
                return WEB_ROOT + "/biz/purchase/purchase-order!purchaseOrderDetails?id=" + pk;
            }
        },
        colModel : [ {
            label : '行项号',
            name : 'subVoucher',
            width : 50,
            align : 'center'
        }, {
            label : '采购商品',
            name : 'commodity.display',
            editable : true,
            width : 200,
            align : 'left'
        }, {
            label : '收货仓库',
            name : 'storageLocation.id',
            width : 150,
            stype : 'select',
            searchoptions : {
                value : Biz.getStockDatas()
            }
        }, {
            label : '单位',
            name : 'measureUnit',
            editable : true,
            width : 80
        }, {
            label : '数量',
            name : 'quantity',
            width : 80,
            formatter : 'number',
            summaryType : 'sum',
            responsive : 'sm'
        }, {
            label : '单价',
            name : 'price',
            width : 80,
            formatter : 'currency',
            responsive : 'sm'
        }, {
            label : '原价金额',
            name : 'originalAmount',
            width : 80,
            formatter : 'currency',
            responsive : 'sm'
        }, {
            label : '折扣率(%)',
            name : 'discountRate',
            width : 80,
            formatter : 'currency',
            responsive : 'sm'
        }, {
            label : '折扣额',
            name : 'discountAmount',
            width : 80,
            align : 'right',
            responsive : 'sm'
        }, {
            label : '折后金额',
            name : 'amount',
            width : 80,
            formatter : 'currency',
            responsive : 'sm'
        }, {
            label : '分摊运费',
            name : 'deliveryAmount',
            width : 80,
            formatter : 'currency',
            responsive : 'sm'
        }, {
            label : '成本单价',
            name : 'costPrice',
            width : 80,
            formatter : 'currency',
            responsive : 'sm'
        }, {
            label : '总成本',
            name : 'costAmount',
            width : 80,
            formatter : 'currency',
            responsive : 'sm'
        } ],
        footerrow : true,
        userDataOnFooter : true
    });
});