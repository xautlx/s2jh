<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/index-header.jsp"%>
</head>
<body>
	<div class="container-fluid">
		<s2:tabbedpanel id="privilegeIndexTabs">
			<ul>
				<li><a href="#privilegeIndexListTab"><span>配置权限列表</span></a></li>
				<li><a href="${base}/auth/privilege!forward?_to_=calcUrls"><span>系统URL权限汇总</span></a></li>
			</ul>
			<div id="privilegeIndexListTab">
				<div class="row-fluid">
					<div class="toolbar">
						<div class="toolbar-inner">
							<button type="button" class="btn" id="privilegeAddBtn">
								<i class="icon-plus-sign"></i> 添加
							</button>
							<button type="button" class="btn" id="privilegeDeleteBtn">
								<i class="icon-trash"></i> 删除
							</button>
							<div class="divider-vertical"></div>
							<button type="button" class="btn" id="privilegeEnableBtn">
								<i class="icon-play"></i> 启用
							</button>
							<button type="button" class="btn" id="privilegeDisableBtn">
								<i class="icon-stop"></i> 停用
							</button>
						</div>
					</div>
				</div>
				<div class="row-fluid">
					<table id="privilegeListDiv"></table>
					<div id="privilegeListDivPager"></div>
				</div>
			</div>
		</s2:tabbedpanel>
	</div>
	<%@ include file="/common/index-footer.jsp"%>
	<script type="text/javascript">
        $(function() {
            $("#privilegeListDiv").grid({
                url : '${base}/auth/privilege!findByPage',
                colNames : [ '操作','类型', '代码', '名称', '分类', 'URL', '禁用标识', '排序号' ],
                colModel : [ {
                    name : 'operation',
                    align : 'center',
                    fixed : true,
                    sortable : false,
                    search : false,
                    hidedlg : true,
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
                    name : 'type',
                    align : 'center',
                    width : 80,
                    stype : 'select',
                    searchoptions : {
                        dataUrl : '${base}/auth/privilege!types.json',
                    }              
                }, {
                    name : 'code',
                    align : 'left',
                    width : 200
                }, {
                    name : 'title',
                    align : 'left'
                }, {
                    name : 'category',
                    align : 'left',
                    width : 80,
                    stype : 'select',
                    searchoptions : {
                        dataUrl : '${base}/auth/privilege!distinctCategories.json',
                    }
                }, {
                    name : 'url',
                    align : 'left'
                }, {
                    name : 'disabled',
                    width : 60,
                    formatter : booleanFormatter
                }, {
                    name : 'orderRank',
                    width : 60,
                    sorttype : 'number'
                } ],
                delRow : {
                    url : "${base}/auth/privilege!doDelete"
                },
                addRow : {
                    url : "${base}/auth/privilege!inputTabs"
                },
                editRow : {
                    url : "${base}/auth/privilege!inputTabs",
                    labelCol : 'title'
                },
                grouping : true,
                groupingView : {
                    groupField : [ 'category' ],
                    groupOrder : [ 'asc' ],
                    groupCollapse : false
                },
                caption : "权限列表"
            });

            $("#privilegeAddBtn").click(function() {
                $("#privilegeListDiv").jqGrid('addRow');
            });

            $("#privilegeDeleteBtn").click(function() {
                $("#privilegeListDiv").jqGrid('delRow');
            });

            $("#privilegeEnableBtn").click(function() {
                if (rowids = $("#privilegeListDiv").jqGrid("getAtLeastOneSelectedItem")) {
                    $.ajaxPostURL({
                        url : '${base}/auth/privilege!doState',
                        data : {
                            ids : rowids,
                            disabled : false
                        },
                        successCallback : function(response) {
                            $("#privilegeListDiv").jqGrid("refresh");
                        }
                    });
                }
            });

            $("#privilegeDisableBtn").click(function() {
                if (rowids = $("#privilegeListDiv").jqGrid("getAtLeastOneSelectedItem")) {
                    $.ajaxPostURL({
                        url : '${base}/auth/privilege!doState',
                        data : {
                            ids : rowids,
                            disabled : true
                        },
                        successCallback : function(response) {
                            $("#privilegeListDiv").jqGrid("refresh");
                        }
                    });
                }
            });
        });
    </script>
</body>
</html>
