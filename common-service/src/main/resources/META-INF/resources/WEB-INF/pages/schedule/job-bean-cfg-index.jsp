<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/index-header.jsp"%>
</head>
<body>
    <div class="container-fluid">
        <s2:tabbedpanel id="jobBeanCfgIndexTabs">
            <ul>
                <li><a href="#jobBeanCfgIndexListTab"><span>可配置任务列表</span></a></li>
                <li><a href="${base}/schedule/job-bean-cfg!forward?_to_=triggers"><span>任务动态管理</span></a></li>
            </ul>
            <div id="jobBeanCfgIndexListTab">
                <div class="row-fluid">
                    <div class="toolbar">
                        <div class="toolbar-inner">
                            <button type="button" class="btn" id="jobBeanCfgAddBtn">
                                <i class="icon-plus-sign"></i> 添加
                            </button>
                            <button type="button" class="btn" id="jobBeanCfgDeleteBtn">
                                <i class="icon-trash"></i> 删除
                            </button>
                            <div class="btn-group pull-right">
                                <button type="button" class="btn" title="高级查询"
                                    onclick="$('#jobBeanCfgListDiv').jqGrid('advSearch');">
                                    <i class="icon-search"></i>
                                </button>
                            </div>                            
                        </div>
                    </div>
                </div>
                <div class="row-fluid">
                    <table id="jobBeanCfgListDiv"></table>
                    <div id="jobBeanCfgListDivPager"></div>
                </div>
            </div>
        </s2:tabbedpanel>
    </div>                    
	<%@ include file="/common/index-footer.jsp"%>
    <script type="text/javascript">
        $(function() {
            $("#jobBeanCfgListDiv").grid({
                url: '${base}/schedule/job-bean-cfg!findByPage',
                colNames : [ '操作','任务类全名','CRON表达式','自动初始运行','启用历史记录','集群运行模式','创建时间','版本号'],
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
                        }, {
                            title : "查看",
                            icon : "icon-book",
                            onclick : "$.popupViewDialog('${base}/schedule/job-bean-cfg!viewTabs?id=" + options.rowId + "')"
                        } ]);
                    }                 
                }, {
                    name : 'jobClass',
                    align : 'left'
                }, {
                    name : 'cronExpression',
                    width : 120,
                    align : 'right'  
                }, {
                    name : 'autoStartup',
                    fixed : true,
                    formatter : booleanFormatter,
                    align : 'center'
                }, {
                    name : 'logRunHist',
                    fixed : true,
                    formatter : booleanFormatter,
                    align : 'center'
                }, {
                    name : 'runWithinCluster',
                    fixed : true,
                    formatter : booleanFormatter,
                    align : 'center'                    
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
                    url : "${base}/schedule/job-bean-cfg!doDelete"
                },
                addRow : {
                    url : "${base}/schedule/job-bean-cfg!inputTabs"
                },
                editRow : {
                    url : "${base}/schedule/job-bean-cfg!inputTabs",
                    labelCol : 'jobClass'
                },                
                caption:"定时任务配置列表"
            }); 
            
            $("#jobBeanCfgAddBtn").click(function() {
                $("#jobBeanCfgListDiv").jqGrid('addRow');
            });
            
            $("#jobBeanCfgDeleteBtn").click(function() {
                $("#jobBeanCfgListDiv").jqGrid('delRow');
            });                         
         });
    </script>	
</body>
</html>
