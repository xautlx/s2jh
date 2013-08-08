<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/page-header.jsp"%>
<script type="text/javascript">
    $().ready(function() {

        $("#roleRelatedPrivilegeListDiv<s:property value='#parameters.id'/>").grid({
            url : "${base}/auth/role!findRelatedRoleR2Privileges?id=<s:property value='#parameters.id'/>",
            colNames : [ '代码', '名称', '类别', 'URL' ],
            colModel : [ {
                name : 'privilege.code',
                align : 'center',
                width : 80,
                formatter : function(cellValue, options, rowdata, action) {
                    return $.jgrid.buildLink({
                        text : cellValue,
                        onclick : "$.popupViewDialog('${base}/auth/privilege!viewTabs?id=" + rowdata.privilege.id + "')"
                    });
                }
            }, {
                name : 'privilege.title',
                align : 'left'
            }, {
                name : 'privilege.category',
                align : 'left'
            }, {
                name : 'privilege.url',
                align : 'left',
                hidden : true
            } ],
            grouping : true,
            groupingView : {
                groupField : [ 'privilege.category' ],
                groupOrder : [ 'asc' ],
                groupCollapse : false
            },
            filterToolbar : {
                searchOperators : false
            },
            caption : "已经关联权限列表"
        });

        $("#roleUnRelatedPrivilegeListDiv<s:property value='#parameters.id'/>").grid({
            url : "${base}/auth/role!findUnRelatedPrivileges?id=<s:property value='#parameters.id'/>",
            colNames : [ '代码', '名称', '类别', 'URL' ],
            colModel : [ {
                name : 'code',
                align : 'center',
                fixed : true,
                width : 80,
                formatter : function(cellValue, options, rowdata, action) {
                    return $.jgrid.buildLink({
                        text : cellValue,
                        onclick : "$.popupViewDialog('${base}/auth/privilege!viewTabs?id=" + options.rowId + "')"
                    });
                }
            }, {
                name : 'title',
                align : 'left'
            }, {
                name : 'category',
                align : 'left'
            }, {
                name : 'url',
                align : 'left',
                hidden : true
            } ],
            grouping : true,
            groupingView : {
                groupField : [ 'category' ],
                groupOrder : [ 'asc' ],
                groupCollapse : false
            },
            filterToolbar : {
                searchOperators : false
            },
            caption : "可选关联权限列表"
        });

        $("#roleUnRelatedPrivilegeAddBtn<s:property value='#parameters.id'/>").click(function() {
            if (rowids = $("#roleUnRelatedPrivilegeListDiv<s:property value='#parameters.id'/>").jqGrid("getAtLeastOneSelectedItem")) {
                $.ajaxPostURL({
                    url : '${base}/auth/role!doAddUnRelatedPrivilegeR2s',
                    data : {
                        id : "<s:property value='%{#parameters.id}'/>",
                        r2ids : rowids
                    },
                    successCallback : function(response) {
                        $("#roleUnRelatedPrivilegeListDiv<s:property value='#parameters.id'/>").jqGrid("refresh");
                        $("#roleRelatedPrivilegeListDiv<s:property value='#parameters.id'/>").jqGrid("refresh");
                    }
                });
            }
        });

        $("#roleRelatedPrivilegeDeleteBtn<s:property value='#parameters.id'/>").click(function() {
            if (rowids = $("#roleRelatedPrivilegeListDiv<s:property value='#parameters.id'/>").jqGrid("getAtLeastOneSelectedItem")) {
                $.ajaxPostURL({
                    url : '${base}/auth/role!doDeleteRelatedPrivilegeR2s',
                    data : {
                        id : "<s:property value='%{#parameters.id}'/>",
                        r2ids : rowids
                    },
                    successCallback : function(response) {
                        $("#roleUnRelatedPrivilegeListDiv<s:property value='#parameters.id'/>").jqGrid("refresh");
                        $("#roleRelatedPrivilegeListDiv<s:property value='#parameters.id'/>").jqGrid("refresh");
                    }
                });
            }
        });
    });
</script>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span11">
			<table id="roleRelatedPrivilegeListDiv<s:property value='#parameters.id'/>"></table>
			<div id="roleRelatedPrivilegeListDiv<s:property value='#parameters.id'/>Pager"></div>
		</div>
	</div>
	<div class="row-fluid">
		<div class="toolbar">
			<div class="toolbar-inner">
				<button type="button" class="btn btn-primary"
					id="roleUnRelatedPrivilegeAddBtn<s:property value='#parameters.id'/>">
					<i class="icon-arrow-up"></i> 添加
				</button>
				<button type="button" class="btn btn-primary"
					id="roleRelatedPrivilegeDeleteBtn<s:property value='#parameters.id'/>">
					<i class="icon-arrow-down"></i> 移除
				</button>
			</div>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span11">
			<table id="roleUnRelatedPrivilegeListDiv<s:property value='#parameters.id'/>"></table>
			<div id="roleUnRelatedPrivilegeListDiv<s:property value='#parameters.id'/>Pager"></div>
		</div>
	</div>
</div>