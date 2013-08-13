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
			<table id="taskListDiv"></table>
			<div id="taskListDivPager"></div>
		</div>
	</div>
	<%@ include file="/common/index-footer.jsp"%>
	<script type="text/javascript">
        $(function() {
            $("#taskListDiv").grid({
                url : "${base}/sys/task!list",
                colNames : [ '操作', 'Class', 'Method', '类型', '定时规则' ],
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
                            title : "立即执行",
                            icon : "icon-play",
                            onclick : "$('#" + $(this).attr("id") + "').jqGrid('editRow','" + rowdata.id + "')"
                        }, {
                            title : "查看",
                            icon : "icon-book",
                            onclick : "$.popupViewDialog('${base}/auth/privilege!viewTabs?id=" + options.rowId + "')"
                        } ]);
                    }
                }, {
                    name : 'clazz',
                    align : 'left'
                }, {
                    name : 'method',
                    align : 'left'
                }, {
                    name : 'type',
                    align : 'center',
                    width : '100px'
                }, {
                    name : 'value',
                    align : 'center',
                    width : '100px'
                } ],
                cmTemplate : {
                    sortable : false
                },
                editRow : {
                    url : "${base}/sys/data-dict!inputTabs",
                    labelCol : 'key1Value'
                },
                grouping : true,
                groupingView : {
                    groupField : [ 'type' ],
                    groupOrder : [ 'asc' ],
                    groupCollapse : false
                },
                caption : "计划任务列表"
            });
        });
    </script>
</body>
</html>