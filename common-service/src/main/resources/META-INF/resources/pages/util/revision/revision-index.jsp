<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">

	function revisionsCompare(rowid){
	    var rowdata = $("#revsListDiv").jqGrid("getRowData", rowid);
        var url="<s:property value="%{getActionName()+'!revisionCompare'}"/>";
        url+="?id=<s:property value='#parameters.id'/>";
        url+="&revLeft="+rowdata['revisionEntity.rev'];
        url+="&revRight="+rowdata['revisionEntity.rev'];
        $.popupDialog({
            dialogId : 'revisionsCompareDialog',
            url : url,
            title : "数据对比"
        })
	}

    $().ready(function() {
        $("#revisionsSearchForm").formvalidate({
            initSubmit : true,
            submitHandler : function(form) {
                $("#revsListDiv").grid({
                    queryForm : $(form),
                    colNames : [ '操作', '版本号', '操作时间', '操作类型','原状态','新状态', '操作说明','操作人员' ],
                    colModel : [ {
                        name : 'operation',
                        align : 'center',
                        sortable : false,
                        hidedlg : true,
                        search : false,
                        width : 25,
                        formatter : function(cellValue, options, rowdata, action) {
                            return $.jgrid.buildButtons([{
                                title : "查看",
                                icon : "icon-book",
                                onclick : "revisionsCompare('"+options.rowId+"')"
                            } ]);
                        }
                    }, {
                        name : 'revisionEntity.rev',
                        width : 70,
                        align : 'center'
                    }, {
                        name : 'revisionEntity.revstmp',
                        width : 140,
                        fixed : true,
                        align : 'center'
                    }, {
                        name : 'revisionEntity.operationEventDisplay',
                        width : 100,
                        fixed : true,
                        align : 'center'
                    }, {
                        name : 'revisionEntity.oldStateDisplay',
                        width : 100,
                        fixed : true,
                        align : 'center'
                    }, {
                        name : 'revisionEntity.newStateDisplay',
                        width : 100,
                        fixed : true,
                        align : 'center'
                    }, {
                        name : 'revisionEntity.operationExplain'
                    }, {
                        fixed : true,
                        name : 'revisionEntity.username'
                    } ],
                    ondblClickRow: function(rowid, iRow, iCol, e) {
                        revisionsCompare(rowid);
                    },
                    pager: false,
                    loadonce: true,
                    caption : "数据修改记录"
                });
            }
        });
        
        $("#revisionsCompareBtn").click(function() {
            var selectedRows = $("#revsListDiv").jqGrid('getGridParam', 'selarrrow');
            if(selectedRows.length!=2){
                alert("请选取两个比较行项");
                return false;
            }
            var row0 = eraseCellValueLink($("#revsListDiv").getRowData(selectedRows[0])['revisionEntity.rev']);
            var row1 = eraseCellValueLink($("#revsListDiv").getRowData(selectedRows[1])['revisionEntity.rev']);
            var revLeft,revRight;
            if(row0>row1){
                revLeft=row1;
                revRight=row0;
            }else{
                revLeft=row0;
                revRight=row1;
            }
            
            var url="<s:property value="%{getActionName()+'!revisionCompare'}"/>";
            url+="?id=<s:property value='#parameters.id'/>";
            url+="&revLeft="+revLeft;
            url+="&revRight="+revRight;
            $.popupDialog({
                dialogId : 'revisionsCompareDialog',
                url : url,
                title : "数据对比"
            })
        });
    });
</script>
<div class="container-fluid">
	<s2:form id="revisionsSearchForm" action="%{getActionName()+'!revisionList'}" method="get"
		cssClass="form-inline">
		<s:hidden name="id" value="%{#parameters.id}" />
		<div class="row-fluid">
			<div class="toolbar">
				<div class="toolbar-inner">
					<button type="button" class="btn" id="revisionsCompareBtn">
						<i class="icon-plus-sign"></i> 对比所选二者
					</button>
					<div class="divider-vertical"></div>
					<div class="input-prepend">
						<span class="add-on">属性变更</span>
						<s:select name="property" list="revisionFields" listKey="key.name" listValue="value"
							cssClass="input-large" />
						<s:select name="changed" list="#{'true':'有变更','false':'无变更'}" cssClass="input-small" />
					</div>
					<button type="submit" class="btn btn_submit">
						<i class="icon-search"></i> 查询
					</button>
					<button type="reset" class="btn">
						<i class="icon-repeat"></i> 重置
					</button>
				</div>
			</div>
	</s2:form>
	<table id="revsListDiv"></table>
	<div id="revsListDivPager"></div>
</div>