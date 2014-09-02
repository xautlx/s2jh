$(function() {
    $(".grid-biz-sale-sale-delivery").data("gridOptions", {
        url : WEB_ROOT + '/biz/sale/sale-delivery!findByPage',
        colModel : [ {
            label : '凭证号',
            name : 'voucher',
            width : 120,
            cellattr : function(rowId, cellValue, rawObject, cm, rowdata) {
                if (rowdata['voucherState'] == 'REDW') {
                    return "class='badge-warning'"
                }
            },
            align : 'center'
        }, {
            label : '记账日期',
            name : 'voucherDate',
            formatter : 'date'
        }, {
            label : '收取客户运费',
            name : 'chargeLogisticsAmount',
            editable : true,
            hidden : true,
            width : 100
        }, {
            label : '整单金额',
            name : 'totalAmount',
            formatter : 'currency',
            width : 100
        }, {
            label : '已付金额',
            name : 'payedAmount',
            formatter : 'currency',
            width : 100
        }, {
            label : '客户',
            name : 'customerProfile.display',
            index : 'customerProfile.code_OR_customerProfile.title',
            width : 140
        }, {
            label : '经办人',
            align : 'center',
            name : 'voucherUser.signinid',
            width : 80
        }, {
            label : '收货人',
            name : 'receivePerson',
            editable : true,
            width : 100
        }, {
            label : '联系电话',
            name : 'mobilePhone',
            editable : true,
            width : 100
        }, {
            label : '收货地址',
            name : 'deliveryAddr',
            editable : true,
            width : 250
        }, {
            label : '物流公司',
            name : 'logistics.display',
            width : 100
        }, {
            label : '快递单号',
            name : 'logisticsNo',
            width : 100
        }, {
            label : '实际运费',
            name : 'logisticsAmount',
            width : 100
        } ],
        editcol : 'voucher',
        editurl : WEB_ROOT + "/biz/sale/sale-delivery!doSave",
        fullediturl : WEB_ROOT + "/biz/sale/sale-delivery!inputTabs",
        addable : false,
        inlineNav : {
            add : false
        },
        operations : function(itemArray) {
            var $select = $('<li data-position="multi" data-toolbar="show"><a href="javascript:;"><i class="fa fa-print"></i> 发货清单</a></li>');
            $select.children("a").bind("click", function(e) {
                e.preventDefault();
                var $grid = $(this).closest(".ui-jqgrid").find(".ui-jqgrid-btable:first");
                var url = WEB_ROOT + "/rpt/jasper-report!preview?report=SALE_DELIVERY_COMMODITY_LIST";
                var rowDatas = $grid.jqGrid("getSelectedRowdatas");
                for (i = 0; i < rowDatas.length; i++) {
                    var rowData = rowDatas[i];
                    url += "&reportParameters['SALE_DELIVERY_IDS']=" + rowData['voucher'];
                }
                window.open(url, "_blank");
            });
            itemArray.push($select);
            var $select2 = $('<li data-position="multi" data-toolbar="show"><a href="javascript:;"><i class="fa fa-print"></i> 全峰</a></li>');
            $select2.children("a").bind("click", function(e) {
                e.preventDefault();
                var $grid = $(this).closest(".ui-jqgrid").find(".ui-jqgrid-btable:first");
                var url = WEB_ROOT + "/rpt/jasper-report!preview?report=EXPRESS_QUANFENG";
                var rowDatas = $grid.jqGrid("getSelectedRowdatas");
                for (i = 0; i < rowDatas.length; i++) {
                    var rowData = rowDatas[i];
                    url += "&reportParameters['SALE_DELIVERY_IDS']=" + rowData['voucher'];
                }
                window.open(url, "_blank");
            });
            itemArray.push($select2);
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
            Grid.initAjax();
        },
    });
});
