<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/index-header.jsp"%>
</head>
<body>
	<div class="container-fluid">
		<s2:tabbedpanel id="configPropertyIndexTabs">
			<ul>
				<li><a href="#configPropertyIndexListTab"><span>列表查询</span></a></li>
			</ul>
			<div id="configPropertyIndexListTab">
				<div class="row-fluid">
					<div class="toolbar">
						<div class="toolbar-inner">
							<button type="button" class="btn" id="configPropertyAddBtn">
								<i class="icon-plus-sign"></i> 添加
							</button>
							<button type="button" class="btn" id="configPropertyDeleteBtn">
								<i class="icon-trash"></i> 删除
							</button>
							<div class="btn-group pull-right">
								<button type="button" class="btn" title="高级查询"
									onclick="$('#userListDiv').jqGrid('advSearch');">
									<i class="icon-search"></i>
								</button>
							</div>
						</div>
					</div>
				</div>
				<div class="row-fluid">
					<table id="configPropertyListDiv"></table>
					<div id="configPropertyListDivPager"></div>
				</div>
			</div>
		</s2:tabbedpanel>
	</div>
	<%@ include file="/common/index-footer.jsp"%>
	<script type="text/javascript">
        $(function() {
            $("#configPropertyListDiv").grid({
                url : '${base}/sys/config-property!findByPage',
                colNames : [ '操作', '代码', '名称', '简单属性值', '创建时间', '版本号' ],
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
                        } ]);
                    }
                }, {
                    name : 'propKey',
                    align : 'left'
                }, {
                    name : 'propName',
                    align : 'left'
                }, {
                    name : 'simpleValue',
                    align : 'left'
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
                delRow : {
                    url : "${base}/sys/config-property!doDelete"
                },
                addRow : {
                    url : "${base}/sys/config-property!inputTabs"
                },
                editRow : {
                    url : "${base}/sys/config-property!inputTabs",
                    labelCol : 'propName'
                },
                caption : "配置属性列表"
            });

            $("#configPropertyAddBtn").click(function() {
                $("#configPropertyListDiv").jqGrid('addRow');
            });

            $("#configPropertyDeleteBtn").click(function() {
                $("#configPropertyListDiv").jqGrid('delRow');
            });
        });
    </script>
</body>
</html>