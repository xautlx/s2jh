<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">

    function addSingle(rowid) {
        var rowdata = $("#privilegeCalcUrlListDiv").getRowData(rowid);
        var url = '${base}/auth/privilege!create?';
        url += ("&category=" + rowdata.namespaceLabel);
        url += ("&title=" + rowdata.actionNameLabel + "-" + rowdata.methodNameLabel);
        url += ("&url=" + rowdata.url);
        $("#privilegeIndexTabs").tabs("add", url, "创建-权限");
    }

    $(function() {
        $("#privilegeCalcUrlListDiv").grid({
            url : '${base}/auth/privilege!calcUrls',
            colNames : [ '操作', '代码', '名称', '代码', '名称', '代码', '名称', 'URL', '受控', '关联权限' ],
            colModel : [ {
                name : 'operation',
                align : 'center',
                fixed : true,
                sortable : false,
                search : false,
                hidedlg : true,
                width : 25,
                formatter : function(cellValue, options, rowdata, action) {
                    return $.jgrid.buildButtons([ {
                        title : "编辑",
                        icon : "icon-pencil",
                        onclick : "$('#" + $(this).attr("id") + "').jqGrid('editRow','" + rowdata.id + "')"
                    } ]);
                }
            }, {
                name : 'namespace',
                align : 'left',
                width : 80,
                fixed : true
            }, {
                name : 'namespaceLabel',
                align : 'left',
                width : 80,
                fixed : true
            }, {
                name : 'actionName',
                align : 'left',
                width : 80,
                fixed : true
            }, {
                name : 'actionNameLabel',
                align : 'left',
                width : 100
            }, {
                name : 'methodName',
                align : 'left',
                width : 80
            }, {
                name : 'methodNameLabel',
                align : 'left',
                width : 150
            }, {
                name : 'url',
                align : 'left'
            }, {
                name : 'controlled',
                formatter : booleanFormatter
            }, {
                name : 'controllPrivileges',
                align : 'left'
            } ],
            ondblClickRow : function(rowid, iRow, iCol, e) {
                addSingle(rowid);
            },
            groupHeaders : [ {
                startColumnName : 'namespace',
                numberOfColumns : 2,
                titleText : '模块'
            }, {
                startColumnName : 'actionName',
                numberOfColumns : 2,
                titleText : '对象'
            }, {
                startColumnName : 'methodName',
                numberOfColumns : 2,
                titleText : '方法'
            } ],
            loadonce : true,
            caption : "URL列表"
        });

        $("#privilegeAddBatchBtn").click(function() {
            if (rowids = $("#privilegeCalcUrlListDiv").jqGrid("getAtLeastOneSelectedItem")) {
                $.ajaxPostURL({
                    url : '${base}/auth/privilege!doAddBatch',
                    data : {
                        ids : rowids
                    },
                    confirm : '确认批量将所选URL添加到权限列表中？',
                    successCallback : function(response) {
                        $("#privilegeCalcUrlListDiv").jqGrid("refresh");
                        $("#privilegeListDiv").jqGrid("refresh");
                    }
                });
            }
        });
        
        $("#privilegeAddSingleBtn").click(function() {
            if (rowid = $("#privilegeCalcUrlListDiv").jqGrid("getOnlyOneSelectedItem")) {
                addSingle(rowid);
            }
        });
    });
</script>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="toolbar">
			<div class="toolbar-inner">
				<button type="button" class="btn" id="privilegeAddBatchBtn">
					<i class="icon-list"></i> 批量创建权限
				</button>
				<button type="button" class="btn" id="privilegeAddSingleBtn">
					<i class="icon-share"></i> 参考创建权限
				</button>
			</div>
		</div>
	</div>
	<div class="row-fluid">
		<table id="privilegeCalcUrlListDiv"></table>
		<div id="privilegeCalcUrlListDivPager"></div>
	</div>
</div>