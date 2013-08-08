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
                    colNames : [ '操作', '学号', '姓名', '性别', '身份证件类型', '身份证件号', '学校', '上级主管', '学校', '上级主管', '申请时间', '审批时间', '状态', '创建时间', '版本号' ],
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
                        name : 'sourceXx.xxmc',
                        index : 'sourceXx.xxdm',
                        align : 'left',
                        formatter : function(cellValue, options, rowdata, action) {
                            return $.jgrid.buildLink({
                                text : cellValue,
                                onclick : "$.popupViewDialog('${base}/biz/xx/xx-jcxx!viewTabs?id=" + rowdata.sourceXx.id + "')"
                            });  
                        }
                    }, {
                        name : 'sourceXx.sszgdwm',
                        align : 'center',
                        width : 60,
                        fixed : true,
                        formatter : function(cellValue, options, rowdata, action) {
                            return regionCodeFormatter(cellValue);
                        }
                    }, {
                        name : 'targetXx.xxmc',
                        index : 'targetXx.xxdm',
                        align : 'left',
                        formatter : function(cellValue, options, rowdata, action) {
                            return $.jgrid.buildLink({
                                text : cellValue,
                                onclick : "$.popupViewDialog('${base}/biz/xx/xx-jcxx!viewTabs?id=" + rowdata.targetXx.id + "')"
                            });                             
                        }
                    }, {
                        name : 'targetXx.sszgdwm',
                        align : 'center',
                        width : 60,
                        fixed : true,
                        formatter : function(cellValue, options, rowdata, action) {
                            return regionCodeFormatter(cellValue);
                        }
                    }, {
                        name : 'reqTime',
                        width : 80,
                        fixed : true,
                        align : 'center'
                    }, {
                        name : 'auditTime',
                        width : 80,
                        fixed : true,
                        align : 'center'
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
                        align : 'center'
                    }, {
                        name : 'version',
                        hidden : true,
                        hidedlg : true
                    } ],
                    ondblClickEnabledRow : function(rowid, iRow, iCol, e, rowdata) {
                        $("#xsTransferReqIndexTabs").tabs("add", '${base}/biz/xs/xs-transfer-audit!inputTabs?id=' + rowid, "审批-" + eraseCellValueLink(rowdata['xsJbxx.xh']));
                    },
                    groupHeaders : [ {
                        startColumnName : 'sourceXx.xxmc',
                        numberOfColumns : 2,
                        titleText : '转出学校'
                    }, {
                        startColumnName : 'targetXx.xxmc',
                        numberOfColumns : 2,
                        titleText : '转入学校'
                    } ],
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
				<form id="xsTransferReqSearchForm" action="${base}/biz/xs/xs-transfer-audit!findByPage"
					class="form-inline" method="get">
					<div class="row-fluid">
						<div class="toolbar">
							<div class="toolbar-inner">
								<div class="input-prepend">
									<span class="add-on">异动学籍</span>
									<s:textfield name="search['CN_xsJbxx.xh_OR_xsJbxx.xm_OR_xsJbxx.sfzjh']"
										cssClass="input-medium" title="学生学号/姓名/身份证件号" />
								</div>
								<div class="input-prepend">
									<span class="add-on">转出学校</span>
									<s:textfield name="search['CN_sourceXx.xxdm_OR_sourceXx.xxmc']" cssClass="input-medium"
										title="学校代码/名称" />
								</div>
								<div class="input-prepend">
									<span class="add-on">转入学校</span>
									<s:textfield name="search['CN_targetXx.xxdm_OR_targetXx.xxmc']" cssClass="input-medium"
										title="学校代码/名称" />
								</div>
								<div class="input-prepend">
									<span class="add-on">状态</span>
									<s2:multiselect name="search['IN_state']" id="search_state"
										list="xsTransferReqAuditStateMap" cssClass="input-medium" />
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
									<s:textfield name="search['EQ_id']" cssClass="input-small" validator="{digits:true,min:1}" />
								</div>
								<div class="input-prepend">
									<span class="add-on">创建时间</span>
									<s2:datetextfield name="search['AD_createdDate']" id="search_ADD_createdDate" format="date"
										cssClass="input-small" validator="{date:true,dateISO:true}" />
									<span class="add-on">至</span>
									<s2:datetextfield name="search['BD_createdDate']" id="search_BDD_createdDate" format="date"
										cssClass="input-small" validator="{date:true,dateISO:true}" />
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