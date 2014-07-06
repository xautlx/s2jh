$(function() {

    $(".form-biz-sale-sale-delivery-inputBasic").data("formOptions", {
        updateTotalAmount : function() {
            var $form = $(this);

            var $grid = $form.find(".grid-biz-sale-sale-delivery-inputBasic");
            var userData = {
                "commodity.display" : "合计："
            };
            // 更新表格汇总统计数据

            userData.quantity = $grid.jqGrid('sumColumn', 'quantity');
            userData.taxAmount = $grid.jqGrid('sumColumn', 'taxAmount');
            userData.amount = $grid.jqGrid('sumColumn', 'amount');
            userData.originalAmount = $grid.jqGrid('sumColumn', 'originalAmount');
            userData.discountAmount = $grid.jqGrid('sumColumn', 'discountAmount');

            $grid.jqGrid("footerData", "set", userData, true);

            var chargeLogisticsAmount = Util.parseFloatValDefaultZero($form.find("input[name='chargeLogisticsAmount']"));
            var discountAmount = Util.parseFloatValDefaultZero($form.find("input[name='discountAmount']"))
            var commodityAmount = userData.amount;
            var totalTaxAmount = userData.taxAmount
            var commodityAndTaxAmount = commodityAmount + totalTaxAmount;
            var totalAmount = commodityAndTaxAmount + chargeLogisticsAmount;
            $form.setFormDatas({
                commodityAmount : commodityAmount,
                totalTaxAmount : totalTaxAmount,
                commodityAndTaxAmount : commodityAndTaxAmount,
                discountAmount : userData.discountAmount,
                totalOriginalAmount : userData.originalAmount,
                totalAmount : totalAmount,
                payedAmount : totalAmount
            });
        },
        bindEvents : function() {
            var $form = $(this);
            var $grid = $form.find(".grid-biz-sale-sale-delivery-inputBasic");

            $form.find("input[name='chargeLogisticsAmount']").keyup(function() {
                $form.data("formOptions").updateTotalAmount.call($form);
            });

            //客户选取
            Biz.setupCustomerProfileSelect($form);

            //扫描枪输入处理
            $form.find("input[name='barcodeScan']").barcodeScanSupport({
                onEnter : function() {
                    var code = $(this).val();
                    if (code != '') {
                        var data = Biz.queryCacheCommodityDatas(code);
                        if (data && data.length > 0) {
                            var rowdata = data[0];
                            var ids = $grid.jqGrid('getDataIDs');
                            var targetRowdata = null;
                            var targetRowid = null;
                            $.each(ids, function(i, id) {
                                var item = $grid.jqGrid('getRowData', id);
                                if (item['commodity.sku'] == rowdata['sku']) {
                                    if (item['gift'] != 'true') {
                                        targetRowid = id;
                                        targetRowdata = item;
                                        return false;
                                    }
                                }
                            });

                            if (targetRowdata) {
                                targetRowdata.quantity = Number(targetRowdata.quantity) + 1;
                                $grid.data("gridOptions").calcRowAmount.call($grid, targetRowdata);
                                $grid.jqGrid('setRowData', targetRowid, targetRowdata);
                            } else {

                                var newdata = {
                                    'commodity.id' : rowdata.id,
                                    'commodity.sku' : rowdata.sku,
                                    'measureUnit' : rowdata.measureUnit,
                                    'storageLocation.id' : rowdata['defaultStorageLocation.id'],
                                    'commodity.display' : rowdata.display,
                                    'discountRate' : 0,
                                    'taxRate' : 0,
                                    'quantity' : 1
                                }
                                newdata['price'] = rowdata['lastSalePrice'];
                                $grid.data("gridOptions").calcRowAmount.call($grid, newdata);
                                $grid.jqGrid('insertNewRowdata', newdata);
                            }
                            $form.data("formOptions").updateTotalAmount.call($form);
                        }
                        $(this).focus();
                    }
                }
            });

            // 从销售订单选取
            $form.find(".btn-select-sale-order").click(function() {
                alert("TODO");
                return;
                $(this).popupDialog({
                    url : WEB_ROOT + '/biz/sale/sale-delivery!forward?_to_=selectSaleOrderDetails',
                    title : '选取销售订单',
                    callback : function(data) {
                        var master = data.master;
                        var rows = data.rows;

                        $form.setFormDatas({
                            'receivePerson' : master['saleOrder.receivePerson'],
                            'mobilePhone' : master['saleOrder.mobilePhone'],
                            'postCode' : master['saleOrder.postCode'],
                            'title' : "[" + master['reserveDeliveryTime'].substr(0, 7) + "]" + master['saleOrder.title'],
                            'deliveryAddr' : master['saleOrder.deliveryAddr'],
                            'customerProfile.id' : master['saleOrder.customerProfile.id'],
                            'customerProfile.display' : master['saleOrder.customerProfile.display'],
                            'referenceVoucher' : master['saleOrder.orderSeq'],
                            'referenceSource' : 'biz',
                            'memo' : master['saleOrder.customerMemo']
                        }, false);

                        $.each(rows, function(i, row) {

                            row['storageLocation.id'] = row['defaultStorageLocation.id'] ? row['defaultStorageLocation.id'] : row['commodity.defaultStorageLocation.id'];
                            row['measureUnit'] = row['commodity.measureUnit'];
                            row['saleOrderDetailCommodity.id'] = row['id'];
                            if (row['gift'] == 'true') {
                                row['discountRate'] = 100;
                                row['price'] = 0;
                            } else {
                                row['discountRate'] = 0;
                            }

                            row['taxRate'] = 0;
                            $grid.data("gridOptions").calcRowAmount.call($grid, row);
                            $grid.jqGrid('insertNewRowdata', row);
                            $form.data("formOptions").updateTotalAmount.call($form);
                        });
                    }
                })
            });

            //按金额自动分摊折扣金额
            $form.find(".btn-discount-by-amount").click(function() {
                if ($grid.jqGrid("isEditingMode", true)) {
                    return false;
                }
                //按照“原价金额”的比率计算更新每个行项的折扣额，注意最后一条记录需要以减法计算以修正小数精度问题
                var discountAmount = $form.find("input[name='discountAmount']").val();
                var totalOriginalAmount = $grid.jqGrid('sumColumn', 'originalAmount');
                var perDiscountAmount = MathUtil.mul(MathUtil.div(discountAmount, totalOriginalAmount, 5), 100);
                var lastrowid = null;
                var totalDiscountAmount = 0;
                var ids = $grid.jqGrid("getDataIDs");
                $.each(ids, function(i, id) {
                    var rowdata = $grid.jqGrid("getRowData", id);
                    if (rowdata['gift'] != 'true' && rowdata['commodity.id'] != '') {
                        rowdata['discountRate'] = perDiscountAmount;
                        $grid.data("gridOptions").calcRowAmount.call($grid, rowdata, 'discountRate');
                        $grid.jqGrid('setRowData', id, rowdata);
                        totalDiscountAmount = MathUtil.add(totalDiscountAmount, rowdata['discountAmount']);
                        lastrowid = id;
                    }
                });
                //最后一条记录需要以减法计算以修正小数精度问题
                if (lastrowid) {
                    var rowdata = $grid.jqGrid("getRowData", lastrowid);
                    rowdata['discountAmount'] = MathUtil.sub(discountAmount, MathUtil.sub(totalDiscountAmount, rowdata['discountAmount']));
                    $grid.data("gridOptions").calcRowAmount.call($grid, rowdata, 'discountAmount');
                    $grid.jqGrid('setRowData', lastrowid, rowdata);
                }

                $form.data("formOptions").updateTotalAmount.call($form);
            });
        }
    });

    $(".grid-biz-sale-sale-delivery-inputBasic").data("gridOptions", {
        calcRowAmount : function(rowdata, src) {
            rowdata['originalAmount'] = MathUtil.mul(rowdata['price'], rowdata['quantity']);
            if (src == 'discountAmount') {
                rowdata['discountRate'] = MathUtil.div(rowdata['discountAmount'], rowdata['originalAmount'], 5) * 100;
                rowdata['amount'] = MathUtil.sub(rowdata['originalAmount'], rowdata['discountAmount']);
            } else if (src == 'amount') {
                rowdata['discountAmount'] = MathUtil.sub(rowdata['originalAmount'], rowdata['amount']);
                rowdata['discountRate'] = MathUtil.div(rowdata['discountAmount'], rowdata['originalAmount'], 5) * 100;
            } else {
                rowdata['discountAmount'] = MathUtil.div(MathUtil.mul(rowdata['discountRate'], rowdata['originalAmount']), 100);
                rowdata['amount'] = MathUtil.sub(rowdata['originalAmount'], rowdata['discountAmount']);
            }

            rowdata['taxAmount'] = MathUtil.div(MathUtil.mul(rowdata['amount'], rowdata['taxRate']), 100);
            rowdata['commodityAndTaxAmount'] = MathUtil.add(rowdata['amount'], rowdata['taxAmount']);
        },
        updateRowAmount : function(src) {
            var $grid = $(this);
            var rowdata = $grid.jqGrid("getEditingRowdata");
            $grid.data("gridOptions").calcRowAmount.call($grid, rowdata, src);
            $grid.jqGrid("setEditingRowdata", rowdata);
        },
        batchEntitiesPrefix : "saleDeliveryDetails",
        url : function() {
            var pk = $(this).attr("data-pk");
            if (pk) {
                return WEB_ROOT + "/biz/sale/sale-delivery!saleDeliveryDetails?id=" + pk + "&clone=" + $(this).attr("data-clone");
            }
        },
        colModel : [ {
            label : '所属销售单主键',
            name : 'saleDelivery.id',
            hidden : true,
            hidedlg : true,
            editable : true,
            formatter : function(cellValue, options, rowdata, action) {
                var pk = $(this).attr("data-pk");
                return pk ? pk : "";
            }
        }, {
            name : 'commodity.id',
            hidden : true,
            hidedlg : true,
            editable : true
        }, {
            name : 'saleOrderDetailCommodity.id',
            hidden : true,
            hidedlg : true,
            editable : true
        }, {
            name : 'commodity.barcode',
            hidden : true,
            hidedlg : true,
            editable : true
        }, {
            label : '销售（发货）商品',
            name : 'commodity.display',
            editable : true,
            editrules : {
                required : true
            },
            editoptions : Biz.getGridCommodityEditOptions(),
            align : 'left'
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
            editable : true,
            editrules : {
                required : true,
                number : true
            },
            editoptions : {
                dataInit : function(elem) {
                    var $grid = $(this);
                    var $elem = $(elem);
                    $elem.keyup(function() {
                        $grid.data("gridOptions").updateRowAmount.call($grid);
                    });
                }
            },
            summaryType : 'sum'
        }, {
            label : '销售单价',
            name : 'price',
            width : 80,
            formatter : 'currency',
            editable : true,
            editrules : {
                required : true,
                number : true
            },
            editoptions : {
                dataInit : function(elem) {
                    var $grid = $(this);
                    var $elem = $(elem);
                    $elem.keyup(function() {
                        $grid.data("gridOptions").updateRowAmount.call($grid);
                    });
                }
            },
            summaryType : 'sum'
        }, {
            label : '是否赠品',
            name : 'gift',
            width : 80,
            edittype : 'checkbox',
            editable : true,
            align : 'center',
            responsive : 'sm',
            editoptions : {
                dataInit : function(elem) {
                    var $grid = $(this);
                    var $elem = $(elem);
                    var $form = $elem.closest("form");
                    $elem.change(function() {

                        if ($elem.is(":checked")) {
                            var rowdata = $grid.jqGrid("getEditingRowdata");
                            rowdata['price'] = 0;
                            rowdata['originalAmount'] = 0;
                            rowdata['discountRate'] = 0;
                            rowdata['discountAmount'] = 0;
                            rowdata['amount'] = 0;
                            rowdata['taxAmount'] = 0;
                            rowdata['commodityAndTaxAmount'] = 0;
                            $grid.jqGrid("setEditingRowdata", rowdata);
                        }
                    });
                }
            }
        }, {
            label : '原始金额',
            name : 'originalAmount',
            width : 80,
            formatter : 'currency',
            editable : true,
            editoptions : {
                dataInit : function(elem) {
                    var $elem = $(elem);
                    $elem.attr("readonly", true);
                }
            },
            responsive : 'sm'
        }, {
            label : '折扣率(%)',
            name : 'discountRate',
            width : 80,
            formatter : 'currency',
            editable : true,
            editrules : {
                number : true
            },
            editoptions : {
                dataInit : function(elem) {
                    var $grid = $(this);
                    var $elem = $(elem);
                    $elem.keyup(function() {
                        $grid.data("gridOptions").updateRowAmount.call($grid, $elem.attr("name"));
                    });
                }
            },
            responsive : 'sm'
        }, {
            label : '折扣额',
            name : 'discountAmount',
            width : 80,
            editable : true,
            editoptions : {
                dataInit : function(elem) {
                    var $grid = $(this);
                    var $elem = $(elem);
                    $elem.keyup(function() {
                        $grid.data("gridOptions").updateRowAmount.call($grid, $elem.attr("name"));
                    });
                }
            },
            align : 'right',
            responsive : 'sm'
        }, {
            label : '折后金额',
            name : 'amount',
            width : 80,
            formatter : 'currency',
            editable : true,
            editoptions : {
                dataInit : function(elem) {
                    var $grid = $(this);
                    var $elem = $(elem);
                    $elem.keyup(function() {
                        $grid.data("gridOptions").updateRowAmount.call($grid, $elem.attr("name"));
                    });
                }
            },
            responsive : 'sm'
        }, {
            label : '税率(%)',
            name : 'taxRate',
            width : 80,
            formatter : 'number',
            hidden : true,
            editable : true,
            editoptions : {
                dataInit : function(elem) {
                    var $grid = $(this);
                    var $elem = $(elem);
                    var $form = $elem.closest("form");
                    $elem.keyup(function() {
                        $grid.data("gridOptions").updateRowAmount.call($grid);
                    });
                }
            },
            responsive : 'sm'
        }, {
            label : '税额',
            name : 'taxAmount',
            width : 80,
            formatter : 'currency',
            hidden : true,
            editable : true,
            editoptions : {
                dataInit : function(elem) {
                    var $elem = $(elem);
                    $elem.attr("readonly", true);
                }
            },
            responsive : 'sm'
        }, {
            label : '含税总金额',
            name : 'commodityAndTaxAmount',
            width : 80,
            formatter : 'currency',
            hidden : true,
            editable : true,
            editoptions : {
                dataInit : function(elem) {
                    var $elem = $(elem);
                    $elem.attr("readonly", true);
                }
            },
            responsive : 'sm'
        }, {
            label : '发货仓库',
            name : 'storageLocation.id',
            width : 150,
            editable : true,
            editrules : {
                required : true
            },
            stype : 'select',
            searchoptions : {
                value : Biz.getStockDatas()
            }
        } ],
        footerrow : true,
        beforeInlineSaveRow : function(rowid) {
            var $grid = $(this);
            $grid.data("gridOptions").updateRowAmount.call($grid);
        },
        afterInlineSaveRow : function(rowid) {
            var $grid = $(this);
            // 整个Form相关金额字段更新
            var $form = $grid.closest("form");
            $form.data("formOptions").updateTotalAmount.call($form);
        },
        afterInlineDeleteRow : function(rowid) {
            var $grid = $(this);
            // 整个Form相关金额字段更新
            var $form = $grid.closest("form");
            $form.data("formOptions").updateTotalAmount.call($form);
        },
        userDataOnFooter : true
    });

});
