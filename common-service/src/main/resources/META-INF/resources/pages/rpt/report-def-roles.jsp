<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/page-header.jsp"%>
<script type="text/javascript">
    $().ready(function() {
        $("#reportDefRelatedRoleListDiv<s:property value='#parameters.id'/>").grid({
            url : "${base}/rpt/report-def!findRelatedRoles?id=<s:property value='#parameters.id'/>",
            colNames : [ '关联状态', '代码', '名称', '禁用标识', '锁定标识', '关联时间', '创建时间', '版本号' ],
            colModel : [ {
                name : 'extraAttributes.related',
                align : 'center',
                fixed : true,
                width : 60,
                search : false,
                formatter : function(cellValue, options, rowdata, action) {
                    return cellValue ? '<i class="icon-magnet"></i>' : '';
                }
            }, {
                name : 'code',
                align : 'left',
                fixed : true,
                width : 120,
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
                fixed : true,
                formatter : booleanFormatter,
                align : 'center'
            }, {
                name : 'locked',
                width : 60,
                fixed : true,
                formatter : booleanFormatter,
                align : 'center'
            }, {
                name : 'extraAttributes.r2CreatedDate',
                width : 140,
                fixed : true,
                align : 'center'
            }, {
                name : 'createdDate',
                width : 140,
                fixed : true,
                hidden : true,
                align : 'center'
            }, {
                name : 'version',
                hidden : true,
                hidedlg : true
            } ],
            cmTemplate : {
                sortable : false
            },
            caption : "角色列表"
        });

        $("#reportDefRelatedRoleToolbar<s:property value='#parameters.id'/> .r2-operation").click(function() {
            if (rowids = $("#reportDefRelatedRoleListDiv<s:property value='#parameters.id'/>").jqGrid("getAtLeastOneSelectedItem")) {
                $.ajaxPostURL({
                    url : '${base}/rpt/report-def!doUpdateRelatedRoleR2s?op=' + $(this).val(),
                    data : {
                        id : "<s:property value='%{#parameters.id}'/>",
                        r2ids : rowids
                    },
                    successCallback : function(response) {
                        $("#reportDefRelatedRoleListDiv<s:property value='#parameters.id'/>").jqGrid("refresh");
                    }
                });
            }
        });

    });
</script>
<div class="container-fluid">
	<div class="toolbar">
		<div class="toolbar-inner" id="reportDefRelatedRoleToolbar<s:property value='#parameters.id'/>">
			<button type="button" class="btn r2-operation" value="add">
				<i class="icon-resize-small"></i> 添加关联
			</button>
			<button type="button" class="btn r2-operation" value="delete">
				<i class="icon-resize-full"></i> 删除关联
			</button>
		</div>
	</div>
	<table id="reportDefRelatedRoleListDiv<s:property value='#parameters.id'/>"></table>
	<div id="reportDefRelatedRoleListDiv<s:property value='#parameters.id'/>Pager"></div>
</div>