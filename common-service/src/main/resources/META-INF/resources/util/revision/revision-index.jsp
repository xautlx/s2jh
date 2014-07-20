<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="row search-form-default">
	<div class="col-md-12">
		<form method="get" class="form-inline form-validation form-search-init" data-grid-search=".grid-revision-index"
			action="#">
			<div class="form-group">
				<s:select name="property" list="revisionFields" listKey="key.name" listValue="value" cssClass="input-xlarge" />
				<s:select name="changed" list="#{'true':'有变更','false':'无变更'}" cssClass="input-small" />
			</div>
			<button class="btn default hidden-inline-xs" type="reset">
				<i class="fa fa-undo"></i>&nbsp; 重&nbsp;置
			</button>
			<button class="btn green" type="submmit">
				<i class="m-icon-swapright m-icon-white"></i>&nbsp; 查&nbsp;询
			</button>
		</form>
	</div>
</div>
<div class="row">
	<div class="col-md-12">
		<table class="grid-revision-index" data-grid="table"></table>
	</div>
</div>

<%
    // @see ExtRestActionMapper.getMapping
			String path = (String) request
					.getAttribute("s2jh.struts.action.url");
			request.setAttribute("revisionListPath",
					StringUtils.substringBefore(path, "!") + "!revisionList");
			request.setAttribute("revisionComparePath",
					StringUtils.substringBefore(path, "!") + "!revisionCompare");
%>

<script type="text/javascript">
    $(function() {
        $(".grid-revision-index").data("gridOptions", {
            url : WEB_ROOT + '<s:property value="#request.revisionListPath"/>?id=<s:property value="#parameters.id"/>',
            colNames : [ '版本号', '操作时间', '操作类型', '原状态', '新状态', '操作说明', '操作人员' ],
            colModel : [ {
                name : 'revisionEntity.rev',
                width : 50,
                align : 'center'
            }, {
                name : 'revisionEntity.revstmp',
                formatter : 'timestamp',
                align : 'center'
            }, {
                name : 'revisionEntity.operationEventDisplay',
                width : 150,
                align : 'left'
            }, {
                name : 'revisionEntity.oldStateDisplay',
                width : 100,
                hidden : true,
                align : 'center'
            }, {
                name : 'revisionEntity.newStateDisplay',
                width : 100,
                hidden : true,
                align : 'center'
            }, {
                name : 'revisionEntity.operationExplain'
            }, {
                width : 100,
                name : 'revisionEntity.username',
                align : 'center'
            } ],
            filterToolbar : false,
            cmTemplate : {
                sortable : false
            },
            operations : function(itemArray) {
                var $select = $('<li data-position="single" data-toolbar="show"><a href="javascript:;"><i class="fa fa-circle-o"></i> 单数据查看</a></li>');
                $select.children("a").bind("click", function(e) {
                    e.preventDefault();
                    var $grid = $(this).closest(".ui-jqgrid").find(".ui-jqgrid-btable:first");
                    var selectedRows = $grid.jqGrid('getGridParam', 'selarrrow');
                    if (selectedRows.length != 1) {
                        alert("请选取单个行项");
                        return false;
                    }

                    var rowdata = $grid.jqGrid("getRowData", selectedRows);
                    var url = WEB_ROOT + '<s:property value="#request.revisionComparePath"/>';
                    url += "?id=<s:property value='#parameters.id'/>";
                    url += "&revLeft=" + rowdata['revisionEntity.rev'];
                    url += "&revRight=" + rowdata['revisionEntity.rev'];

                    $select.popupDialog({
                        url : url,
                        title : "历史版本数据查看"
                    })
                });
                itemArray.push($select);

                var $revisionsComparet = $('<li data-position="multi" data-toolbar="show"><a href="javascript:;"><i class="fa fa-indent"></i> 双数据对比</a></li>');
                $revisionsComparet.children("a").bind("click", function(e) {
                    e.preventDefault();
                    var $grid = $(this).closest(".ui-jqgrid").find(".ui-jqgrid-btable:first");
                    var selectedRows = $grid.jqGrid('getGridParam', 'selarrrow');
                    if (selectedRows.length != 2) {
                        alert("请选取两个比较行项");
                        return false;
                    }
                    var row0 = $grid.getRowData(selectedRows[0])['revisionEntity.rev'];
                    var row1 = $grid.getRowData(selectedRows[1])['revisionEntity.rev'];
                    var revLeft, revRight;
                    if (row0 > row1) {
                        revLeft = row1;
                        revRight = row0;
                    } else {
                        revLeft = row0;
                        revRight = row1;
                    }
                    var url = WEB_ROOT + '<s:property value="#request.revisionComparePath"/>';
                    url += "?id=<s:property value='#parameters.id'/>";
                    url += "&revLeft=" + revLeft;
                    url += "&revRight=" + revRight;

                    $select.popupDialog({
                        url : url,
                        title : "历史版本数据对比"
                    })
                });
                itemArray.push($revisionsComparet);
            }
        });
    });
</script>
<%@ include file="/common/ajax-footer.jsp"%>