<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="tabbable tabbable-primary">
	<ul class="nav nav-pills">
		<li class="active"><a class="tab-default" data-toggle="tab" href="#tab-auto">采购订单列表</a></li>
		<li class="tools pull-right"><a class="btn default reload" href="javascript:;"><i class="fa fa-refresh"></i></a></li>
	</ul>
	<div class="tab-content">
		<div class="tab-pane fade active in">
			<div class="row search-form-default">
				<div class="col-md-12">
					<form method="get" class="form-inline form-validation form-search-init"
						data-grid-search=".grid-purchase-order-list" action="#">
						<div class="form-group">
							<input type="text" name="search['CN_voucher_OR_activeTaskName']" class="form-control input-large"
								placeholder="采购单号、活动任务">
						</div>
						<div class="form-group">
							<div class="btn-group">
								<button type="button" class="btn default dropdown-toggle" data-toggle="dropdown">
									过滤条件 <i class="fa fa-angle-down"></i>
								</button>
								<div class="dropdown-menu hold-on-click dropdown-checkboxes">
									<s:checkbox name="search['NE_activeTaskName']" value="true" fieldValue="END" label="隐藏已完结项目" />
								</div>
							</div>
						</div>
						<button class="btn default hidden-inline-xs" type="reset">
							<i class="fa fa-undo"></i>&nbsp; 重&nbsp;置
						</button>
						<button class="btn green" type="submmit">
							<i class="m-icon-swapright m-icon-white"></i>&nbsp; 查&nbsp;询
						</button>
					</form>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<table class="grid-purchase-order-list" data-grid="table"></table>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
    $(function() {
        $(".grid-purchase-order-list").data("gridOptions", {
            url : "${base}/biz/purchase/purchase-order!findByPage",
            colModel : [ {
                label : '采购订单号',
                name : 'voucher',
                align : 'center',
                width : 80
            }, {
                label : '经办人',
                name : 'voucherUser.display',
                index : 'voucherUser.signinid',
                align : 'center',
                width : 40
            }, {
                label : '提交时间',
                name : 'submitDate',
                stype : 'date',
                width : 100
            }, {
                label : '最近操作',
                name : 'lastOperationSummary',
                width : 120
            }, {
                label : '当前任务',
                name : 'activeTaskName',
                align : 'center',
                width : 60,
                formatter : function(cellValue, options, rowdata, action) {
                    if (cellValue) {
                        var url = '${base}/bpm/activiti!showProcessImage?bizKey=' + rowdata.voucher;
                        return '<a data-toggle="modal-ajaxify" href="'+url+'" title="运行流程图">' + cellValue + '</a>';
                    } else {
                        return '';
                    }
                }
            }, {
                label : '标题摘要',
                name : 'title',
                width : 120
            }, {
                label : '供货商',
                name : 'supplier.display',
                index : 'supplier.code_OR_supplier.abbr',
                hidden : true,
                width : 200
            }, {
                label : '采购金额',
                name : 'totalAmount',
                width : 50,
                formatter : 'currency'
            }, {
                label : '预付金额',
                name : 'actualPayedAmount',
                width : 50,
                formatter : 'currency'
            } ],
            editcol : 'voucher',
            addable : false,
            cloneurl : "${base}/biz/purchase/purchase-order!bpmNew",
            fullediturl : "${base}/biz/purchase/purchase-order!inputTabs",
            subGrid : true,
            subGridRowExpanded : function(subgrid_id, row_id) {
                $("<table data-grid='table'/>").appendTo($("#" + subgrid_id)).data("gridOptions", {
                    url : WEB_ROOT + "/biz/purchase/purchase-order!purchaseOrderDetails?id=" + row_id,
                    colModel : [ {
                        label : '销售（发货）商品',
                        name : 'commodity.display',
                        align : 'left'
                    }, {
                        label : '发货仓库',
                        name : 'storageLocation.id',
                        width : 80,
                        stype : 'select',
                        formatter : 'select',
                        searchoptions : {
                            value : Biz.getStockDatas()
                        }
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
                        label : '已收货数量',
                        name : 'recvQuantity',
                        width : 50,
                        align : 'center'
                    }, {
                        label : '采购价格',
                        name : 'price',
                        width : 60,
                        formatter : 'currency'

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
                        label : '分摊运费',
                        name : 'deliveryAmount',
                        width : 60,
                        hidden : true,
                        formatter : 'currency'
                    }, {
                        label : '入库成本价',
                        name : 'costPrice',
                        width : 80,
                        formatter : 'currency'
                    }, {
                        label : '采购商品成本金额',
                        name : 'costAmount',
                        width : 80,
                        formatter : 'currency'
                    } ],
                    loadonce : true,
                    multiselect : false
                });
                Grid.initAjax();
            },
        });
    });
</script>
<%@ include file="/common/ajax-footer.jsp"%>