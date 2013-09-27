<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/index-header.jsp"%>
</head>
<body>
    <div class="container-fluid">
        <s2:tabbedpanel id="jobRunHistIndexTabs">
            <ul>
                <li><a href="#jobRunHistIndexListTab"><span>列表查询</span></a></li>
            </ul>
            <div id="jobRunHistIndexListTab">
                <div class="row-fluid">
                    <div class="toolbar">
                        <div class="toolbar-inner">
                            <button type="button" class="btn" id="jobRunHistDeleteBtn">
                                <i class="icon-trash"></i> 删除
                            </button>
                            <div class="btn-group pull-right">
                                <button type="button" class="btn" title="高级查询"
                                    onclick="$('#jobRunHistListDiv').jqGrid('advSearch');">
                                    <i class="icon-search"></i>
                                </button>
                            </div>                            
                        </div>
                    </div>
                </div>
                <div class="row-fluid">
                    <table id="jobRunHistListDiv"></table>
                    <div id="jobRunHistListDivPager"></div>
                </div>
            </div>
        </s2:tabbedpanel>
    </div>                    
	<%@ include file="/common/index-footer.jsp"%>
    <script type="text/javascript">
        $(function() {
            $("#jobRunHistListDiv").grid({
                url: '${base}/schedule/job-run-hist!findByPage',
                colNames : [ '操作','主机节点','任务名称','触发时间', '下次触发时间','触发次数','异常标识','执行结果','创建时间','版本号'],
                colModel : [ {
                    name : 'operation',
                    align : 'center',
                    fixed : true,
                    sortable : false,
                    hidedlg : true,
                    search : false,
                    width : 30,
                    formatter : function(cellValue, options, rowdata, action) {
                        return $.jgrid.buildButtons([ {
                            title : "查看",
                            icon : "icon-book",
                            onclick : "$.popupViewDialog('${base}/schedule/job-run-hist!viewTabs?id=" + options.rowId + "')"
                        } ]);
                    }  
                }, {
                    name : 'nodeId',
                    width : 150,
                    align : 'center'                
                }, {
                    name : 'jobName',
                    align : 'left'
                }, {
                    name : 'previousFireTime',
                    fixed : true,
                    sorttype: 'date',
                    align : 'center'
                }, {
                    name : 'nextFireTime',
                    fixed : true,
                    sorttype: 'date',
                    align : 'center'
                }, {
                    name : 'refireCount',
                    width : 60,
                    fixed : true,
                    hidden : true,
                    align : 'right'
                }, {
                    name : 'exceptionFlag',
                    fixed : true,
                    formatter : booleanFormatter,
                    align : 'center'
                }, {
                    name : 'result',
                    width : 100,
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
                    url : "${base}/schedule/job-run-hist!doDelete"
                } ,             
                caption:"定时任务运行记录列表"
            }); 

            $("#jobRunHistDeleteBtn").click(function() {
                $("#jobRunHistListDiv").jqGrid('delRow');
            });                         
         });
    </script>	
</body>
</html>
