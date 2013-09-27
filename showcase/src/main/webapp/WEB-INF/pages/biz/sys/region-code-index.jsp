<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/index-header.jsp"%>
<style type="text/css">
.form-horizontal .control-label {
	width: 100px;
}
</style>
</head>
<body>
	<div class="container-fluid">
		<s2:tabbedpanel id="regionCodeIndexTabs">
			<ul>
				<li><a href="#regionCodeIndexListTab"><span>列表查询</span></a></li>
			</ul>
			<div id="regionCodeIndexListTab">
				<div class="row-fluid">
					<div class="toolbar">
						<div class="toolbar-inner">
							<button type="button" class="btn" id="regionCodeAddBtn">
								<i class="icon-plus-sign"></i> 添加
							</button>
							<button type="button" class="btn" id="regionCodeDeleteBtn">
								<i class="icon-trash"></i> 删除
							</button>
							<div class="btn-group pull-right">
								<button type="button" class="btn" title="高级查询"
									onclick="$('#regionCodeListDiv').jqGrid('advSearch');">
									<i class="icon-search"></i>
								</button>
							</div>
						</div>
					</div>
				</div>
				<div class="row-fluid">
					<table id="regionCodeListDiv"></table>
					<div id="regionCodeListDivPager"></div>
				</div>
                <div class="row-fluid">
                    <s2:tabbedpanel id="regionCodeListTabs">
                        <ul>
                        </ul>
                    </s2:tabbedpanel>
                </div>				
			</div>
		</s2:tabbedpanel>
	</div>
	<%@ include file="/common/index-footer.jsp"%>
	<script type="text/javascript">
        $(function() {
            $("#regionCodeListDiv").grid({
                url : "${base}/biz/sys/region-code!findByPage",
                colNames : [ '操作', '行政区划代码', '行政区划名称', '行政区划简称', '教育行政部门名称', '父行政区划代码', '地区类型', '可用状态', '创建时间', '版本号' ],
                colModel : [ {
                    name : 'operation',
                    align : 'center',
                    fixed : true,
                    sortable : false,
                    hidedlg : true,
                    search : false,
                    width : 40,
                    formatter : function(cellValue, options, rowdata, action) {
                        return $.jgrid.buildButtons([ {
                            title : "编辑",
                            icon : "icon-pencil",
                            onclick : "$('#" + $(this).attr("id") + "').jqGrid('editRow','" + rowdata.id + "')"
                        }, {
                            title : "查看",
                            icon : "icon-book",
                            onclick : "$.popupViewDialog('${base}/biz/sys/region-code!viewTabs?id=" + options.rowId + "')"
                        } ]);
                    }
                }, {
                    name : 'regionCode',
                    align : 'center',
                    hidedlg : true,
                    width : 100
                }, {
                    name : 'regionDesc',
                    align : 'left'
                }, {
                    name : 'regionShort',
                    align : 'left'
                }, {
                    name : 'regionEdu',
                    align : 'left'
                }, {
                    name : 'parentcode',
                    align : 'center'
                }, {
                    name : 'dqlx',
                    align : 'left'
                }, {
                    name : 'enabled',
                    formatter : booleanFormatter
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
                    $("#regionCodeIndexTabs").tabs("add", '${base}/biz/sys/region-code!inputTabs?id=' + rowid, "编辑-" + eraseCellValueLink(rowdata.regionCode));
                },
                delRow : {
                    url : "${base}/biz/sys/region-code!doDelete"
                },
                addRow : {
                    url : "${base}/biz/sys/region-code!inputTabs",
                    toTab : "#regionCodeListTabs"
                },
                editRow : {
                    url : "${base}/biz/sys/region-code!inputTabs",
                    toTab : "#regionCodeListTabs",
                    labelCol : 'regionCode'
                },
                sortorder : "asc",
                sortname : "regionCode",
                caption : "行政区划 列表"
            });

            $("#regionCodeAddBtn").click(function() {
                $("#regionCodeListDiv").jqGrid('addRow');
            });

            $("#regionCodeDeleteBtn").click(function() {
                $("#regionCodeListDiv").jqGrid('delRow');
            });
        });
    </script>
</body>
</html>
