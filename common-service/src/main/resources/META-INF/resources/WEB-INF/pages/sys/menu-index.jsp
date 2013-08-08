<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/index-header.jsp"%>
</head>
<body>
	<div class="container-fluid">
		<div class="row-fluid">
			<s2:tabbedpanel id="menuIndexTabs">
				<ul>
					<li><a href="#menuIndexListTab"><span>列表查询</span></a></li>
				</ul>
				<div id="menuIndexListTab">
					<div class="row-fluid">
						<div class="toolbar">
							<div class="toolbar-inner">
								<button type="button" class="btn" id="menuAddBtn">
									<i class="icon-plus-sign"></i> 添加
								</button>
								<button type="button" class="btn" id="menuDeleteBtn">
									<i class="icon-trash"></i> 删除
								</button>
								<div class="btn-group pull-right">
									<button type="button" class="btn" title="展开全部"
										onclick="$('#menuListDiv').find('div.treeclick.tree-plus').each(function(){$(this).click();})">
										<i class="icon-plus"></i>
									</button>
									<button type="button" class="btn" title="收拢全部"
										onclick="$('#menuListDiv').find('div.treeclick.tree-minus').each(function(){$(this).click();})">
										<i class="icon-minus"></i>
									</button>
									<div class="divider-vertical"></div>
									<button type="button" class="btn" title="高级查询"
										onclick="$('#menuListDiv').jqGrid('advSearch');">
										<i class="icon-search"></i>
									</button>
								</div>
							</div>
						</div>
					</div>
					<div class="row-fluid">
						<table id="menuListDiv"></table>
						<div id="menuListDivPager"></div>
					</div>
				</div>
			</s2:tabbedpanel>
		</div>
	</div>
	<%@ include file="/common/index-footer.jsp"%>
	<script type="text/javascript">
        $(function() {
            $("#menuListDiv").grid({
                url : "${base}/sys/menu!treeGridData",
                colNames : [ '操作', '代码', '名称', '菜单URL', '类型', '展开标识', '禁用标识', '图标样式', '排序号' ],
                colModel : [ {
                    name : 'operation',
                    align : 'center',
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
                    },
                }, {
                    name : 'code',
                    align : 'center',
                    hidedlg : true,
                    width : 80,
                    formatter : function(cellValue, options, rowdata, action) {
                        return $.jgrid.buildLink({
                            text : cellValue,
                            onclick : "$.popupViewDialog('${base}/sys/menu!viewTabs?id=" + options.rowId + "')"
                        });
                    }
                }, {
                    name : 'title',
                    align : 'left'
                }, {
                    name : 'url',
                    align : 'left'
                }, {
                    name : 'type.title',
                    index : 'type',
                    width : 80,
                    align : 'center',
                    stype : 'select',
                    searchoptions : {
                        value : enumsContainer['menuTypeEnum']
                    }
                }, {
                    name : 'initOpen',
                    formatter : booleanFormatter
                }, {
                    name : 'disabled',
                    formatter : booleanFormatter
                }, {
                    name : 'style',
                    hidden : true,
                    align : 'left'
                }, {
                    name : 'orderRank',
                    width : 60,
                    sorttype : 'number'
                } ],
                cmTemplate : {
                    sortable : false
                },
                sortorder : "desc",
                sortname : "orderRank",
                delRow : {
                    url : "${base}/sys/menu!doDelete"
                },
                addRow : {
                    url : "${base}/sys/menu!inputTabs"
                },
                editRow : {
                    url : "${base}/sys/menu!inputTabs",
                    labelCol : 'title'
                },
                treeGrid : true,
                treeGridModel : 'adjacency',
                ExpandColumn : 'url',
                caption : "菜单列表"
            });

            $("#menuAddBtn").click(function() {
                $("#menuListDiv").jqGrid('addRow');
            });

            $("#menuDeleteBtn").click(function() {
                $("#menuListDiv").jqGrid('delRow');
            });
        });
    </script>
</body>
</html>