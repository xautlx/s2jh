$(function() {
    $(".grid-biz-stock-commodity-stock").data("gridOptions", {
        url : WEB_ROOT + '/biz/stock/commodity-stock!findByPage',
        colModel : [ {
            label : '商品主键',
            name : 'commodity.id',
            hidden : true,
            hidedlg : true,
            editable : true
        }, {
            label : '实物条码',
            name : 'commodity.barcode',
            hidden : true,
            width : 100,
            align : 'center',
            responsive : 'sm'
        }, {
            label : '商品编码',
            name : 'commodity.sku',
            width : 80,
            editable : true,
            editrules : {
                required : true
            },
            editoptions : {
                updatable : false,
                dataInit : function(elem) {
                    var $grid = $(this);
                    var $elem = $(elem);
                    $elem.dblclick(function() {
                        $(this).popupDialog({
                            url : WEB_ROOT + '/biz/md/commodity!forward?_to_=selection',
                            title : '选取商品',
                            callback : function(item) {
                                var $curRow = $elem.closest("tr.jqgrow");
                                $grid.jqGrid("setEditingRowdata", {
                                    'commodity.id' : item.id,
                                    'commodity.commodityBarcode' : item.commodityBarcode,
                                    'commodity.barcode' : item.barcode,
                                    'commodity.title' : item.title
                                }, true);
                            }
                        })
                    }).keypress(function(event) {
                        return false;
                    });
                }
            },
            align : 'center'
        }, {
            label : '商品名称',
            name : 'commodity.title',
            editable : true,
            editoptions : {
                updatable : false,
                dataInit : function(elem) {
                    var $grid = $(this);
                    var $elem = $(elem);
                    $elem.dblclick(function() {
                        $elem.closest(".jqgrow").find("input[name='commodity.barcode']").dblclick();
                    }).keypress(function(event) {
                        return false;
                    });
                }
            },
            width : 150,
            editrules : {
                required : true
            },
            align : 'left'
        }, {
            label : '库存地',
            name : 'storageLocation.id',
            editable : true,
            width : 150,
            editrules : {
                required : true
            },
            stype : 'select',
            editoptions : {
                updatable : false,
                value : Biz.getStockDatas()
            },
            align : 'center'
        }, {
            label : '批次号',
            name : 'batchNo',
            width : 100,
            editable : true,
            align : 'center'
        }, {
            label : '生产日期',
            name : 'productDate',
            width : 80,
            hidden : true,
            editable : true,
            formatter : 'date'
        }, {
            label : '到期日期',
            name : 'expireDate',
            width : 80,
            editable : true,
            formatter : 'date'
        }, {
            label : '当前成本价',
            name : 'costPrice',
            editable : true,
            width : 60,
            formatter : 'currency',
            editoptions : {
                dataInit : function(elem) {
                    var $grid = $(this);
                    var $elem = $(elem);
                    var rowid = $elem.closest("tr.jqgrow").attr("id");
                    var rowdata = $grid.jqGrid("getRowData", rowid);
                    if ($elem.val() == '') {
                        $elem.val(0);
                    }
                }
            }
        }, {
            label : '当前库存量',
            name : 'curStockQuantity',
            width : 60,
            editable : true,
            formatter : 'number',
            editoptions : {
                dataInit : function(elem) {
                    var $grid = $(this);
                    var $elem = $(elem);
                    var rowid = $elem.closest("tr.jqgrow").attr("id");
                    var rowdata = $grid.jqGrid("getRowData", rowid);
                    if ($elem.val() == '') {
                        $elem.val(0);
                    }
                }
            }
        }, {
            label : '销售锁定库存',
            name : 'salingTotalQuantity',
            width : 60,
            editable : true,
            formatter : 'number',
            editoptions : {
                dataInit : function(elem) {
                    var $grid = $(this);
                    var $elem = $(elem);
                    var rowid = $elem.closest("tr.jqgrow").attr("id");
                    var rowdata = $grid.jqGrid("getRowData", rowid);
                    if ($elem.val() == '') {
                        $elem.val(0);
                    }
                }
            }
        }, {
            label : '采购在途库存',
            name : 'purchasingTotalQuantity',
            width : 60,
            editable : true,
            formatter : 'number',
            editoptions : {
                dataInit : function(elem) {
                    var $grid = $(this);
                    var $elem = $(elem);
                    var rowid = $elem.closest("tr.jqgrow").attr("id");
                    var rowdata = $grid.jqGrid("getRowData", rowid);
                    if ($elem.val() == '') {
                        $elem.val(0);
                    }
                }
            }
        }, {
            label : '库存报警阀值',
            name : 'stockThresholdQuantity',
            editable : true,
            width : 60,
            formatter : 'number',
            editoptions : {
                dataInit : function(elem) {
                    var $grid = $(this);
                    var $elem = $(elem);
                    var rowid = $elem.closest("tr.jqgrow").attr("id");
                    var rowdata = $grid.jqGrid("getRowData", rowid);
                    if ($elem.val() == '') {
                        $elem.val(0);
                    }
                }
            }
        }, {
            label : '计算可用库存',
            name : 'availableQuantity',
            width : 60,
            formatter : 'number'
        } ],
        postData : {
            "search['FETCH_storageLocation']" : "INNER"
        },
        editurl : WEB_ROOT + '/biz/stock/commodity-stock!doSave',
        delurl : WEB_ROOT + '/biz/stock/commodity-stock!doDelete',

        subGrid : true,
        subGridRowExpanded : function(subgrid_id, row_id) {
            Grid.initSubGrid(subgrid_id, row_id, {
                url : WEB_ROOT + '/biz/stock/stock-in-out!findByPage',
                postData : {
                    "search['EQ_commodityStock.id']" : row_id,
                    "search['FETCH_commodityStock.storageLocation']" : "INNER.INNER"
                },
                colModel : [ {
                    label : '时间',
                    name : 'createdDate',
                    stype : 'date',
                    align : 'center'
                }, {
                    label : '实物量',
                    name : 'quantity',
                    width : 60,
                    formatter : 'number',
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
                    formatter : 'number',
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
                    formatter : 'number',
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
                    width : 100,
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
                multiselect : false
            })
        }
    });
});