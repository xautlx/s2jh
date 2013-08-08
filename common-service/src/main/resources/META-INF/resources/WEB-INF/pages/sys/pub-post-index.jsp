<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/index-header.jsp"%>
</head>
<body>
	<div class="container-fluid">
		<s2:tabbedpanel id="pubPostIndexTabs">
			<ul>
				<li><a href="#pubPostIndexListTab"><span>列表查询</span></a></li>
			</ul>
			<div id="pubPostIndexListTab">
				<div class="row-fluid">
					<div class="toolbar">
						<div class="toolbar-inner">
							<button type="button" class="btn" id="pubPostAddBtn">
								<i class="icon-plus-sign"></i> 添加
							</button>
							<button type="button" class="btn" id="pubPostDeleteBtn">
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
					<table id="pubPostListDiv"></table>
					<div id="pubPostListDivPager"></div>
				</div>
			</div>
		</s2:tabbedpanel>
	</div>
	<%@ include file="/common/index-footer.jsp"%>
	<script type="text/javascript">
        $(function() {
            $("#pubPostListDiv").grid({
                url : '${base}/sys/pub-post!findByPage',
                colNames : [ '操作', '阅读人数', '标题', '生效时间', '到期时间', '创建时间', '版本号' ],
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
                    name : 'readUserCount',
                    width : 60,
                    fixed : true,
                    align : 'center'                
                }, {
                    name : 'htmlTitle',
                    align : 'left'
                }, {
                    name : 'publishTime',
                    fixed : true,
                    sorttype : 'date',
                    align : 'center'
                }, {
                    name : 'expireTime',
                    fixed : true,
                    sorttype : 'date',
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
                    url : "${base}/sys/pub-post!doDelete"
                },
                addRow : {
                    url : "${base}/sys/pub-post!inputTabs"
                },
                editRow : {
                    url : "${base}/sys/pub-post!inputTabs",
                    labelCol : 'htmlTitle'
                },
                caption : "公告列表"
            });

            $("#pubPostAddBtn").click(function() {
                $("#pubPostListDiv").jqGrid('addRow');
            });

            $("#pubPostDeleteBtn").click(function() {
                $("#pubPostListDiv").jqGrid('delRow');
            });
        });
    </script>
</body>
</html>