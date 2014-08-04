$(function() {
    $(".grid-biz-sale-sale-delivery-saler-sum").data("gridOptions", {
        url : WEB_ROOT + '/biz/sale/sale-delivery!findByGroupSaler',
        colModel : [ {
            name : 'voucherUser.id',
            hidden : true,
            hidedlg : true
        }, {
            label : '用户账号',
            name : 'voucherUser.signinid',
            align : 'center',
            width : 60
        }, {
            label : '机构代码',
            name : 'aclCode',
            hidden : true,
            width : 120
        }, {
            label : '昵称',
            name : 'voucherUser.nick',
            hidden : true,
            width : 120
        }, {
            label : '最低毛利率',
            name : 'min(profitRate)',
            width : 40,
            formatter : 'percentage'
        }, {
            label : '最高毛利率',
            name : 'max(profitRate)',
            width : 40,
            formatter : 'percentage'
        }, {
            label : '平均毛利率',
            name : 'case(equal(sum(totalAmount),0),-1,quot(sum(profitAmount),sum(totalAmount)))',
            width : 50,
            formatter : 'percentage'
        }, {
            label : '总计成本金额',
            name : 'sum(commodityCostAmount)',
            width : 50,
            formatter : 'currency'
        }, {
            label : '总计销售金额',
            name : 'sum(totalAmount)',
            width : 50,
            formatter : 'currency'
        }, {
            label : '总计利润金额',
            name : 'sum(profitAmount)',
            width : 50,
            formatter : 'currency'
        } ],
        postData : {
            "search['EQ_voucherState']" : 'POST'
        },
        recordtext : '{0} - {1}',
        pgtext : " {0} ",
        subGrid : true,
        subGridRowExpanded : function(subgrid_id, row_id) {
            var $grid = $(this);
            var rowdata = $grid.getRowData(row_id);
            Grid.initSubGrid(subgrid_id, row_id, {
                url : WEB_ROOT + '/biz/sale/sale-delivery!findByPage',
                colModel : [ {
                    label : '凭证号',
                    name : 'voucher',
                    width : 80
                },{
                    label : '凭证日期',
                    name : 'voucherDate',
                    stype : 'date',
                    width : 80
                }, {
                    label : '整单成本金额',
                    name : 'totalCostAmount',
                    formatter : 'currency',
                    width : 100
                }, {
                    label : '整单销售金额',
                    name : 'totalAmount',
                    formatter : 'currency',
                    width : 100
                }, {
                    label : '整单利润金额',
                    name : 'profitAmount',
                    formatter : 'currency',
                    width : 100
                }, {
                    label : '整单利润率',
                    name : 'profitRate',
                    formatter : 'percentage',
                    width : 100
                }, {
                    label : '客户',
                    name : 'customerProfile.display',
                    index : 'customerProfile.nickName_OR_customerProfile.trueName',
                    width : 100
                }, {
                    label : '经办人',
                    name : 'voucherUser.display',
                    index : 'voucherUser.signinid',
                    width : 80
                }, {
                    label : '经办部门',
                    name : 'voucherDepartment.display',
                    index : 'voucherDepartment.code_OR_voucherDepartment.title',
                    hidden : true,
                    width : 100
                }, {
                    label : '收取运费',
                    name : 'chargeLogisticsAmount',
                    width : 80
                }, {
                    label : '支出运费',
                    name : 'logisticsAmount',
                    width : 80
                } ],
                postData : {
                    "search['EQ_voucherState']" : 'POST',
                    "search['EQ_voucherUser.id']" : rowdata['voucherUser.id'],
                    "search['BT_voucherDate']" : $grid.getDataFromBindSeachForm("search['BT_voucherDate']")
                },
                subGrid : true,
                subGridRowExpanded : function(subgrid_id, row_id) {
                    Grid.initSubGrid(subgrid_id, row_id, {
                        url : WEB_ROOT + "/biz/sale/sale-delivery!saleDeliveryDetails?id=" + row_id,
                        colModel : [ {
                            label : '销售（发货）商品',
                            name : 'commodity.display',
                            align : 'left'
                        }, {
                            label : '单位',
                            name : 'measureUnit',
                            editable : true,
                            width : 60
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
                            formatter : 'currency'
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
                        loadonce : true,
                        multiselect : false
                    });
                }
            });
        }
    });
});