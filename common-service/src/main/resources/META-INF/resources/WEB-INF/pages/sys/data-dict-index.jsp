<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/index-header.jsp"%>
</head>
<body>
	<div class="container-fluid">
		<s2:tabbedpanel id="dataDictIndexTabs">
			<ul>
				<li><a href="#dataDictIndexListTab"><span>列表查询</span></a></li>
			</ul>
			<div id="dataDictIndexListTab">
				<div class="row-fluid">
					<div class="toolbar">
						<div class="toolbar-inner">
							<button type="button" class="btn" id="dataDictAddBtn">
								<i class="icon-plus-sign"></i> 单个添加
							</button>
							<button type="button" class="btn" id="dataDictAddBatchBtn">
								<i class="icon-th-list"></i> 批量添加
							</button>
							<button type="button" class="btn" id="dataDictDeleteBtn">
								<i class="icon-trash"></i> 删除
							</button>
							<div class="btn-group pull-right">
								<button type="button" class="btn" title="高级查询"
									onclick="$('#dataDictListDiv').jqGrid('advSearch');">
									<i class="icon-search"></i>
								</button>
							</div>
						</div>
					</div>
				</div>
				<div class="row-fluid">
					<table id="dataDictListDiv"></table>
					<div id="dataDictListDivPager"></div>
				</div>
			</div>
		</s2:tabbedpanel>
	</div>
	<%@ include file="/common/index-footer.jsp"%>
	<script type="text/javascript">
        $(function() {
            $("#dataDictListDiv").grid({
                url : "${base}/sys/data-dict!findByPage",
                colNames : [ '操作', '分类', 'Key1定义', '数据1设定', '禁用标识', '排序号', '创建时间', '版本号' ],
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
                            onclick : "$.popupViewDialog('${base}/auth/privilege!viewTabs?id=" + options.rowId + "')"
                        } ]);
                    }
                }, {
                    name : 'extraAttributes.categoryLabel',
                    index : 'category',
                    sortable : false,
                    align : 'left',
                    width : 200,
                    stype : 'select',
                    searchoptions : {
                        dataUrl : '${base}/sys/data-dict!distinctCategoriesData.json',
                    }
                }, {
                    name : 'key1Value',
                    align : 'left'
                }, {
                    name : 'data1Value',
                    align : 'left'
                }, {
                    name : 'disabled',
                    formatter : booleanFormatter
                }, {
                    name : 'orderRank',
                    sorttype : 'number'
                }, {
                    name : 'createdDate',
                    sorttype : 'date',
                    hidden : true
                }, {
                    name : 'version',
                    hidden : true,
                    hidedlg : true
                } ],
                delRow : {
                    url : "${base}/sys/data-dict!doDelete"
                },
                addRow : {
                    url : "${base}/sys/data-dict!inputTabs"
                },
                editRow : {
                    url : "${base}/sys/data-dict!inputTabs",
                    labelCol : 'key1Value'
                },
                grouping : true,
                groupingView : {
                    groupField : [ 'extraAttributes.categoryLabel' ],
                    groupOrder : [ 'asc' ],
                    groupCollapse : false
                },
                sortorder : "desc",
                sortname : "orderRank",
                caption : "数据字典列表"
            });

            $("#dataDictAddBtn").click(function() {
                $("#dataDictListDiv").jqGrid('addRow');
            });

            $("#dataDictDeleteBtn").click(function() {
                $("#dataDictListDiv").jqGrid('delRow');
            });

            $("#dataDictAddBatchBtn").click(function() {
                $("#dataDictIndexTabs").tabs("add", "${base}/sys/data-dict!forward?_to_=inputBatch", "批量添加");
            });
        });
    </script>
</body>
</html>
