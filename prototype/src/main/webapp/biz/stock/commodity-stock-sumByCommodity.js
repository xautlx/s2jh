$(function() {
    $(".grid-biz-stock-commodity-stock-sumByCommodity").data("gridOptions", {
        url : WEB_ROOT + '/biz/stock/commodity-stock!findByGroupCommodity',
        colModel : [ {
            label : '商品主键',
            name : 'commodity.id',
            hidden : true,
            hidedlg : true,
            editable : true
        }, {
            label : '商品编码',
            name : 'commodity.sku',
            width : 80,
            align : 'center'
        }, {
            label : '商品名称',
            name : 'commodity.title',
            width : 200,
            align : 'left'
        }, {
            label : '当前库存量',
            name : 'sum(curStockQuantity)',
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
            name : 'sum(salingTotalQuantity)',
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
            name : 'sum(purchasingTotalQuantity)',
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
            name : 'sum(stockThresholdQuantity)',
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
            name : 'sum(availableQuantity)',
            width : 60,
            formatter : 'number'
        } ],
        subGrid : true,
        subGridRowExpanded : function(subgrid_id, row_id) {
            var $grid = $(this);
            var rowdata = $grid.getRowData(row_id);
            Grid.initSubGrid(subgrid_id, row_id, {
                url : WEB_ROOT + '/biz/stock/commodity-stock!findByGroupStorageLocation',
                postData : {
                    "search['EQ_commodity.id']" : rowdata["commodity.id"]
                },
                colModel : [ {
                    label : '商品主键',
                    name : 'commodity.id',
                    hidden : true,
                    hidedlg : true,
                    editable : true
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
                    label : '当前库存量',
                    name : 'sum(curStockQuantity)',
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
                    name : 'sum(salingTotalQuantity)',
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
                    name : 'sum(purchasingTotalQuantity)',
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
                    name : 'sum(stockThresholdQuantity)',
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
                    name : 'sumAvailableQuantity',
                    width : 60,
                    formatter : 'number'
                } ],
                subGrid : true,
                subGridRowExpanded : function(subgrid_id, row_id) {
                    var $grid = $(this);
                    var rowdata = $grid.getRowData(row_id);
                    Grid.initSubGrid(subgrid_id, row_id, {
                        url : WEB_ROOT + '/biz/stock/commodity-stock!findByPage',
                        postData : {
                            "search['EQ_commodity.id']" : rowdata["commodity.id"],
                            "search['EQ_storageLocation.id']" : rowdata["storageLocation.id"]
                        },
                        colModel : [ {
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
                        sortorder : "desc",
                        sortname : "createdDate",
                        multiselect : false
                    })
                }
            })
        }
    });
});