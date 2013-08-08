<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/index-header.jsp"%>
</head>
<body>
	<div class="container-fluid">
		<s2:tabbedpanel id="roleIndexTabs">
			<ul>
				<li><a href="#roleIndexListTab"><span>列表查询</span></a></li>
			</ul>
			<div id="roleIndexListTab">
				<div class="row-fluid">
					<div class="toolbar">
						<div class="toolbar-inner">
							<button type="button" class="btn" id="roleAddBtn">
								<i class="icon-plus-sign"></i> 添加
							</button>
							<button type="button" class="btn" id="roleDeleteBtn">
								<i class="icon-trash"></i> 删除
							</button>
							<button type="button" class="btn" id="roleEnableBtn">
								<i class="icon-play"></i> 启用
							</button>
							<button type="button" class="btn" id="roleDisableBtn">
								<i class="icon-stop"></i> 停用
							</button>
						</div>
					</div>
				</div>
				<div class="row-fluid">
					<table id="roleListDiv"></table>
					<div id="roleListDivPager"></div>
				</div>
			</div>
		</s2:tabbedpanel>
	</div>
	<%@ include file="/common/index-footer.jsp"%>
	<script type="text/javascript">
        $(function() {
            $("#roleListDiv").grid({
                url : '${base}/auth/role!findByPage',
                colNames : [ '操作', '代码', '名称', '禁用标识', '锁定标识', '创建时间', '版本号' ],
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
                    name : 'code',
                    align : 'left',
                    formatter : function(cellValue, options, rowdata, action) {
                        return $.jgrid.buildLink({
                            text : cellValue,
                            onclick : "$.popupViewDialog('${base}/auth/role!viewTabs?id=" + options.rowId + "')"
                        });
                    }
                }, {
                    name : 'title',
                    align : 'left'
                }, {
                    name : 'disabled',
                    width : 60,
                    formatter : booleanFormatter
                }, {
                    name : 'locked',
                    width : 60,
                    formatter : booleanFormatter
                }, {
                    name : 'createdDate',
                    width : 140,
                    hidden : true,
                    sorttype: 'date'
                }, {
                    name : 'version',
                    hidden : true,
                    hidedlg : true,
                    search : false
                } ],
                delRow : {
                    url : "${base}/auth/role!doDelete"
                },
                addRow : {
                    url : "${base}/auth/role!inputTabs"
                },
                editRow : {
                    url : "${base}/auth/role!inputTabs",
                    labelCol : 'code'
                },
                caption : "角色列表"
            });

            $("#roleAddBtn").click(function() {
                $("#roleListDiv").jqGrid('addRow');
            });

            $("#roleDeleteBtn").click(function() {
                $("#roleListDiv").jqGrid('delRow');
            });

            $("#roleEnableBtn").click(function() {
                if (rowids = $("#roleListDiv").jqGrid("getAtLeastOneSelectedItem")) {
                    $.ajaxPostURL({
                        url : '${base}/auth/role!doState',
                        data : {
                            ids : rowids,
                            disabled : false
                        },
                        successCallback : function(response) {
                            $("#roleListDiv").jqGrid("refresh");
                        }
                    });
                }
            });

            $("#roleDisableBtn").click(function() {
                if (rowids = $("#roleListDiv").jqGrid("getAtLeastOneSelectedItem")) {
                    $.ajaxPostURL({
                        url : '${base}/auth/role!doState',
                        data : {
                            ids : rowids,
                            disabled : true
                        },
                        successCallback : function(response) {
                            $("#roleListDiv").jqGrid("refresh");
                        }
                    });
                }
            });
        });
    </script>
</body>
</html>