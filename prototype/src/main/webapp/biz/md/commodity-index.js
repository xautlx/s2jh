$(function() {
    $(".grid-biz-md-commodity-index").data("gridOptions", {
        url : WEB_ROOT + '/biz/md/commodity!findByPage',
        colModel : [ {
            label : 'SKU编码',
            name : 'sku',
            width : 100,
            editable : true,
            align : 'center'
        }, {
            label : '商品名称',
            name : 'title',
            width : 200,
            editable : true,
            align : 'left'
        }, {
            label : '成本价',
            name : 'costPrice',
            width : 60,
            formatter : 'number',
            editable : true,
            align : 'right'
        }, {
            label : '销售价',
            name : 'salePrice',
            width : 60,
            formatter : 'number',
            editable : true,
            align : 'right'
        }, {
            label : '实物条码',
            name : 'barcode',
            width : 100,
            editable : true,
            align : 'center'
        }, {
            label : '不可买',
            name : 'soldOut',
            formatter : 'checkbox',
            editable : true,
            align : 'center'
        }, {
            label : '已下架',
            name : 'removed',
            formatter : 'checkbox',
            editable : true,
            align : 'center'
        }, {
            label : '默认库存地',
            name : 'defaultStorageLocation.id',
            width : 100,
            editable : true,
            align : 'left',
            stype : 'select',
            searchoptions : {
                optionsurl : WEB_ROOT + "/biz/stock/storage-location!selectOptions"
            }
        } ],
        postData : {
            "search['FETCH_defaultStorageLocation']" : "LEFT"
        },
        editcol : 'sku',
        editurl : WEB_ROOT + '/biz/md/commodity!doSave',
        editrulesurl : WEB_ROOT + '/biz/md/commodity!buildValidateRules',
        delurl : WEB_ROOT + '/biz/md/commodity!doDelete',
        fullediturl : WEB_ROOT + '/biz/md/commodity!inputTabs',
        operations : function(itemArray) {
            var $select = $('<li data-position="multi"><a href="javascript:;"><i class="fa fa-print"></i> 条码清单</a></li>');
            $select.children("a").bind("click", function(e) {
                e.preventDefault();
                var $grid = $(this).closest(".ui-jqgrid").find(".ui-jqgrid-btable:first");
                var url = WEB_ROOT + "/rpt/jasper-report!preview?report=COMMODITY_BARCODE_LIST";
                var rowDatas = $grid.jqGrid("getSelectedRowdatas");
                for (i = 0; i < rowDatas.length; i++) {
                    var rowData = rowDatas[i];
                    url += "&reportParameters['SKU_LIST']=" + rowData['sku'];
                }
                window.open(url, "_blank");
            });
            itemArray.push($select);
        }
    });
});