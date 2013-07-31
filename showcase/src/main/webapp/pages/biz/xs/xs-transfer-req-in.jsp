<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/page-header.jsp"%>
<%@ include file="/common/index-header.jsp"%>
<script type="text/javascript">
    $(function() {

        $("#xsTransferReqIndexTabs").tabs();

        $("#xsTransferReqSearchForm").formvalidate({
            initSubmit : true,
            submitHandler : function(form) {
                $("#xsTransferReqListDiv").grid({
                    queryForm : $(form),
                    colNames : [ '操作', '学号', '姓名', '性别', '身份证件类型', '身份证件号', '转出学校识别码', '转出学校名称', '申请时间', '最近审批时间', '状态', '创建时间', '版本号' ],
                    colModel : [ {
                        name : 'operation',
                        align : 'center',
                        fixed : true,
                        sortable : false,
                        hidedlg : true,
                        search : false,
                        width : 25,
                        formatter : function(cellValue, options, rowdata, action) {
                            return $.jgrid.buildButtons([ {
                                title : "编辑",
                                icon : "icon-pencil",
                                onclick : "$('#" + $(this).attr("id") + "').jqGrid('editRow','" + rowdata.id + "')"
                            } ]);
                        }
                    }, {
                        name : 'xsJbxx.xh',
                        align : 'center',
                        width : 100,
                        fixed : true,
                        formatter : function(cellValue, options, rowdata, action) {
                            return $.jgrid.buildLink({
                                text : cellValue,
                                onclick : "$.popupViewDialog('${base}/biz/xs/xs-jbxx!viewTabs?id=" + rowdata.xsJbxx.id + "')"
                            });  
                        }
                    }, {
                        name : 'xsJbxx.xm',
                        width : 60,
                        fixed : true,
                        align : 'center'
                    }, {
                        name : 'xsJbxx.xbm',
                        align : 'center',
                        width : 30,
                        fixed : true,
                        formatter : function(cellValue, options, rowdata, action) {
                            return enumValueFormatter(cellValue, 'ZD_GB_XBM');
                        },
                        stype : 'select',
                        searchoptions : {
                            dataUrl : '${base}/biz/sys/enum-value!data.json?type=ZD_GB_XBM',
                            sopt : [ 'eq', 'ne' ]
                        }
                    }, {
                        name : 'xsJbxx.sfzjlxm',
                        hidden : true,
                        align : 'center',
                        width : 30,
                        fixed : true,
                        formatter : function(cellValue, options, rowdata, action) {
                            return enumValueFormatter(cellValue, 'ZD_BB_SFZJLXM');
                        },
                        stype : 'select',
                        searchoptions : {
                            dataUrl : '${base}/biz/sys/enum-value!data.json?type=ZD_BB_SFZJLXM',
                            sopt : [ 'eq', 'ne' ]
                        }
                    }, {
                        name : 'xsJbxx.sfzjh',
                        hidden : false,
                        align : 'center',
                        width : 160,
                        fixed : true
                    }, {
                        name : 'sourceXx.xxdm',
                        align : 'center',
                        fixed : true,
                        width : 100,
                        formatter : function(cellValue, options, rowdata, action) {
                            return $.jgrid.buildLink({
                                text : cellValue,
                                onclick : "$.popupViewDialog('${base}/biz/xx/xx-jcxx!viewTabs?id=" + rowdata.targetXx.id + "')"
                            });   
                        }
                    }, {
                        name : 'sourceXx.xxmc',
                        align : 'left'
                    }, {
                        name : 'reqTime',
                        width : 120,
                        fixed : true,
                        align : 'center'
                    }, {
                        name : 'lastAuditTime',
                        width : 120,
                        fixed : true,
                        align : 'center',
                        searchoptions : {
                            sopt : [ 'ed', 'ad', 'bd' ],
                            dataInit : function(elem) {
                                $(elem).dateselector();
                            }
                        }
                    }, {
                        name : 'state.title',
                        width : 120,
                        fixed : true,
                        align : 'center'
                    }, {
                        name : 'createdDate',
                        width : 120,
                        fixed : true,
                        hidden : true,
                        align : 'center',
                        searchoptions : {
                            sopt : [ 'ed', 'ad', 'bd' ],
                            dataInit : function(elem) {
                                $(elem).dateselector();
                            }
                        }
                    }, {
                        name : 'version',
                        hidden : true,
                        hidedlg : true
                    } ],
                    ondblClickEnabledRow : function(rowid, iRow, iCol, e, rowdata) {
                        $("#xsTransferReqIndexTabs").tabs("add", '${base}/biz/xs/xs-transfer-req!forward?_to_=inInputTabs&id=' + rowid, "编辑-" + eraseCellValueLink(rowdata['xsJbxx.xh']));
                    },
                    caption : "学生异动列表"
                });
            }
        });

    });
</script>
</head>
<body>
	<div class="container-fluid">
		<div id="xsTransferReqIndexTabs" class="hide">
			<ul>
				<li><a href="#xsTransferReqIndexListTab"><span>列表查询</span></a></li>
			</ul>
			<div id="xsTransferReqIndexListTab">
				<form id="xsTransferReqSearchForm" action="${base}/biz/xs/xs-transfer-req!findByPage"
					class="form-inline" method="get">
					<s:hidden name="type" value="in" />
                    <s:hidden name="search['FETCH_sourceXx']" value="INNER" />
                    <s:hidden name="search['FETCH_targetXx']" value="INNER" />					
					<div class="row-fluid">
						<div class="toolbar">
							<div class="toolbar-inner">
								<div class="input-prepend">
									<s:textfield name="search['CN_xsJbxx.xh_OR_xsJbxx.xm_OR_xsJbxx.sfzjh']"
										cssClass="input-medium" title="学生学号/姓名/身份证件号" />
								</div>
								<div class="input-prepend">
									<span class="add-on">转出学校</span>
									<s:textfield name="search['CN_sourceXx.xxdm_OR_sourceXx.xxmc']" cssClass="input-medium"
										title="学校代码/名称" />
								</div>
								<button type="submit" class="btn">
									<i class="icon-search"></i> 查询
								</button>
								<button type="reset" class="btn">
									<i class="icon-repeat"></i> 重置
								</button>
								<div class="btn-group pull-right">
									<button type="button" class="btn" title="高级查询" onclick="$.toggleAdvanceSearch(this)">
										<i class="icon-chevron-down"></i>
									</button>
								</div>
							</div>
						</div>
					</div>
					<div class="row-fluid advanceSearchDiv hide">
						<div class="toolbar">
							<div class="toolbar-inner">
								<div class="input-prepend">
									<span class="add-on">流水号</span>
									<s:textfield name="search['EQL_id']" cssClass="input-small" validator="{digits:true,min:1}" />
								</div>
								<div class="input-prepend">
									<span class="add-on">创建时间</span>
									<s2:datetextfield name="search['AD_createdDate']" format="date" cssClass="input-small"
										validator="{date:true,dateISO:true}" />
									<span class="add-on">至</span>
									<s2:datetextfield name="search['BD_createdDate']" format="date" cssClass="input-small"
										validator="{date:true,dateISO:true}" />
								</div>
							</div>
						</div>
					</div>
				</form>
				<div class="row-fluid">
					<table id="xsTransferReqListDiv"></table>
					<div id="xsTransferReqListDivPager"></div>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/common/index-footer.jsp"%>
</body>
</html>