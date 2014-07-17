$(function() {

    $(".form-biz-purchase-purchase-order-bpmInput").data("formOptions", {
        updateTotalAmount : function() {
            var $form = $(this);
            var $grid = $form.find(".grid-biz-purchase-purchase-order-bpmInput");
            var userData = {
                "commodity.display" : "合计："
            };
            //更新表格汇总统计数据
            userData.quantity = $grid.jqGrid('sumColumn', 'quantity');
            userData.amount = $grid.jqGrid('sumColumn', 'amount');
            userData.deliveryAmount = $grid.jqGrid('sumColumn', 'deliveryAmount');
            userData.discountAmount = $grid.jqGrid('sumColumn', 'discountAmount');

            $grid.jqGrid("footerData", "set", userData, true);
            var totalAmount = userData.deliveryAmount + userData.amount;
            $form.find(".span-total-amount").html(totalAmount);
            $form.setFormDatas({
                amount : userData.amount,
                totalDiscountAmount : userData.discountAmount,
                totalDeliveryAmount : userData.deliveryAmount,
                totalAmount : totalAmount
            });

        },
        bindEvents : function() {
            var $form = $(this);
            var $grid = $form.find(".grid-biz-purchase-purchase-order-bpmInput");
            //按金额自动分摊折扣金额
            $form.find(".btn-discount-by-amount").click(function() {
                if ($grid.jqGrid("isEditingMode", true)) {
                    return false;
                }
                //按照“原价金额”的比率计算更新每个行项的折扣额，注意最后一条记录需要以减法计算以修正小数精度问题
                var totalDiscountAmount = $form.find("input[name='totalDiscountAmount']").val();
                var totalOriginalAmount = $grid.jqGrid('sumColumn', 'originalAmount');
                var perDiscountAmount = MathUtil.mul(MathUtil.div(totalDiscountAmount, totalOriginalAmount, 5), 100);
                var lastrowid = null;
                var tempDiscountAmount = 0;
                var ids = $grid.jqGrid("getDataIDs");
                $.each(ids, function(i, id) {
                    var rowdata = $grid.jqGrid("getRowData", id);
                    if (rowdata['gift'] != 'true' && rowdata['commodity.id'] != '') {
                        rowdata['discountRate'] = perDiscountAmount;
                        $grid.data("gridOptions").calcRowAmount.call($grid, rowdata, 'discountRate');
                        $grid.jqGrid('setRowData', id, rowdata);
                        tempDiscountAmount = MathUtil.add(tempDiscountAmount, rowdata['discountAmount']);
                        lastrowid = id;
                    }
                });
                //最后一条记录需要以减法计算以修正小数精度问题
                if (lastrowid) {
                    var rowdata = $grid.jqGrid("getRowData", lastrowid);
                    rowdata['discountAmount'] = MathUtil.sub(totalDiscountAmount, MathUtil.sub(tempDiscountAmount, rowdata['discountAmount']));
                    $grid.data("gridOptions").calcRowAmount.call($grid, rowdata, 'discountAmount');
                    $grid.jqGrid('setRowData', lastrowid, rowdata);
                }

                $form.data("formOptions").updateTotalAmount.call($form);

            });
            //按金额自动分摊运费
            $form.find(".btn-delivery-by-amount").click(function() {

                if ($grid.jqGrid("isEditingMode", true)) {
                    return false;
                }
                //按照“原价金额”的比率计算更新每个行项的折扣额，注意最后一条记录需要以减法计算以修正小数精度问题
                var totalDeliveryAmount = $form.find("input[name='totalDeliveryAmount']").val();
                var totalOriginalAmount = $grid.jqGrid('sumColumn', 'originalAmount');

                var perDeliveryAmount = MathUtil.div(totalDeliveryAmount, totalOriginalAmount, 5);
                var lastrowid = null;
                var tempDeliveryAmount = 0;
                var ids = $grid.jqGrid("getDataIDs");
                $.each(ids, function(i, id) {
                    var rowdata = $grid.jqGrid("getRowData", id);
                    if (rowdata['commodity.id'] != '') {
                        rowdata['deliveryAmount'] = MathUtil.mul(perDeliveryAmount, rowdata['originalAmount']);
                        $grid.data("gridOptions").calcRowAmount.call($grid, rowdata, 'deliveryAmount');
                        $grid.jqGrid('setRowData', id, rowdata);
                        tempDeliveryAmount = MathUtil.add(tempDeliveryAmount, rowdata['deliveryAmount']);
                        lastrowid = id;
                    }
                });

                //最后一条记录需要以减法计算以修正小数精度问题
                if (lastrowid) {
                    var rowdata = $grid.jqGrid("getRowData", lastrowid);
                    rowdata['deliveryAmount'] = MathUtil.sub(totalDeliveryAmount, MathUtil.sub(tempDeliveryAmount, rowdata['deliveryAmount']));

                    $grid.data("gridOptions").calcRowAmount.call($grid, rowdata, 'deliveryAmount');
                    $grid.jqGrid('setRowData', lastrowid, rowdata);
                }

                $form.data("formOptions").updateTotalAmount.call($form);

            });
            //扫描枪输入处理
            $form.find("input[name='barcode']").focus(function(event) {
                $(this).select();
            }).keydown(function(event) {
                //回车事件
                if (event.keyCode === 13) {
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
                                if (item['commodity.barcode'] == rowdata['barcode']) {
                                    targetRowid = id;
                                    targetRowdata = item;
                                    return false;
                                }
                            });

                            if (targetRowdata) {
                                targetRowdata.quantity = Number(targetRowdata.quantity) + 1;
                                $grid.data("gridOptions").calcRowAmount.call($grid, targetRowdata);
                                $grid.jqGrid('setRowData', targetRowid, targetRowdata);
                            } else {
                                var newdata = {
                                    'commodity.id' : rowdata.id,
                                    'commodity.barcode' : rowdata.barcode,
                                    'measureUnit' : rowdata.measureUnit,
                                    'storageLocation.id' : rowdata['defaultStorageLocation.id'],
                                    'commodity.display' : rowdata.display,
                                    'discountRate' : 0,
                                    'taxRate' : 0,
                                    'quantity' : 1
                                }
                                newdata['price'] = rowdata['lastPurchasePrice'];
                                $grid.data("gridOptions").calcRowAmount.call($grid, newdata);
                                $grid.jqGrid('insertNewRowdata', newdata);
                            }
                            $form.data("formOptions").updateTotalAmount.call($form);
                        }
                        $(this).focus();
                    }
                }
            });

        }
    });

    $(".grid-biz-purchase-purchase-order-bpmInput").data("gridOptions", {
        calcRowAmount : function(rowdata, src) {

            rowdata['originalAmount'] = MathUtil.mul(rowdata['price'], rowdata['quantity']);
            //src=='deliveryAmount'的时候不计算折扣，避免已计算好的折扣再次计算出现精度问题
            if (src != 'deliveryAmount') {
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
            }
            rowdata['costAmount'] = MathUtil.add(rowdata['amount'], rowdata['deliveryAmount']);
            rowdata['costPrice'] = MathUtil.div(rowdata['costAmount'], rowdata['quantity'], 2);

        },
        updateRowAmount : function(src) {

            var $grid = $(this);
            var rowdata = $grid.jqGrid("getEditingRowdata");
            $grid.data("gridOptions").calcRowAmount.call($grid, rowdata, src);
            $grid.jqGrid("setEditingRowdata", rowdata);

        },
        batchEntitiesPrefix : "purchaseOrderDetails",
        url : function() {
            var pk = $(this).attr("data-pk");
            if (pk) {
                return WEB_ROOT + "/biz/purchase/purchase-order!purchaseOrderDetails?id=" + pk + "&clone=" + $(this).attr("data-clone");
            }
        },
        colModel : [ {
            label : '所属订单主键',
            name : 'purchaseOrder.id',
            hidden : true,
            hidedlg : true,
            editable : true,
            formatter : function(cellValue, options, rowdata, action) {
                var pk = $(this).attr("data-pk");
                return pk ? pk : "";
            }
        }, {
            label : '商品主键',
            name : 'commodity.id',
            hidden : true,
            hidedlg : true,
            editable : true
        }, {
            label : '行项号',
            name : 'subVoucher',
            width : 50,
            editable : true,
            editrules : {
                required : true
            },
            editoptions : {
                dataInit : function(elem) {
                    var $el = $(elem);
                    if ($el.val() == '') {
                        var $jqgrow = $el.closest(".jqgrow");
                        var idx = $jqgrow.parent().find(".jqgrow:visible").index($jqgrow);
                        $el.val(100 + idx * 10);
                    }
                }
            },
            align : 'center'
        }, {
            label : '采购商品',
            name : 'commodity.display',
            editable : true,
            width : 200,
            editrules : {
                required : true
            },
            editoptions : Biz.getGridCommodityEditOptions(),
            align : 'left'
        }, {
            label : '收货仓库',
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
                    if ($elem.val() == "") {
                        $elem.val(1)
                    }
                    $elem.keyup(function() {
                        $grid.data("gridOptions").updateRowAmount.call($grid, $elem.closest("tr.jqgrow"));
                    });
                }
            },
            summaryType : 'sum',
            responsive : 'sm'
        }, {
            label : '单价',
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
                        $grid.data("gridOptions").updateRowAmount.call($grid, $elem.closest("tr.jqgrow"));
                    });
                }
            },
            responsive : 'sm'
        }, {
            label : '原价金额',
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
            label : '分摊运费',
            name : 'deliveryAmount',
            width : 80,
            formatter : 'currency',
            editable : true,
            editoptions : {
                dataInit : function(elem) {
                    var $grid = $(this);
                    var $elem = $(elem);
                    if ($elem.val() == "") {
                        $elem.val(0)
                    }
                    var $form = $elem.closest("form");
                    $elem.keyup(function() {
                        $grid.data("gridOptions").updateRowAmount.call($grid, $elem.closest("tr.jqgrow"));
                    });
                }
            },
            responsive : 'sm'
        }, {
            label : '成本单价',
            name : 'costPrice',
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
            label : '总成本',
            name : 'costAmount',
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