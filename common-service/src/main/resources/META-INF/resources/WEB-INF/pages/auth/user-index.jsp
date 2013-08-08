<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/index-header.jsp"%>
</head>
<body>
	<div class="container-fluid">
		<s2:tabbedpanel id="userIndexTabs">
			<ul>
				<li><a href="#userIndexListTab"><span>列表查询</span> </a></li>
			</ul>
			<div id="userIndexListTab">
				<div class="row-fluid">
					<div class="toolbar">
						<div class="toolbar-inner">
							<button type="button" class="btn" id="userAddBtn">
								<i class="icon-plus-sign"></i> 添加
							</button>
							<button type="button" class="btn" id="userDeleteBtn">
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
					<table id="userListDiv"></table>
					<div id="userListDivPager"></div>
				</div>
			</div>
		</s2:tabbedpanel>
	</div>
	<%@ include file="/common/index-footer.jsp"%>
	<script type="text/javascript">
        $(function() {
            $("#userListDiv").grid({
                url : '${base}/auth/user!findByPage',
                colNames : [ '操作', '机构代码', '登录账号', '昵称', '电子邮件', '禁用标识', '注册时间', '失效日期' ],
                colModel : [ {
                    name : 'operation',
                    align : 'center',
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
                            onclick : "$.popupViewDialog('${base}/auth/user!viewTabs?id=" + options.rowId + "')"
                        } ]);
                    }
                }, {
                    name : 'aclCode',
                    align : 'left',
                    width : 120
                }, {
                    name : 'signinid',
                    align : 'left'
                }, {
                    name : 'nick',
                    align : 'left'
                }, {
                    name : 'email',
                    align : 'left'
                }, {
                    name : 'disabled',
                    formatter : disabledFormatter
                }, {
                    name : 'signupTime',
                    sorttype: 'date'
                }, {
                    name : 'expireDate',
                    width : 80,
                    sorttype: 'date',
                    hidden : true
                } ],
                delRow : {
                    url : "${base}/auth/user!doDelete"
                },
                addRow : {
                    url : "${base}/auth/user!inputTabs"
                },
                editRow : {
                    url : "${base}/auth/user!inputTabs",
                    labelCol : 'signinid'
                },
                caption : "用户列表"
            });

            $("#userAddBtn").click(function() {
                $("#userListDiv").jqGrid('addRow');
            });

            $("#userDeleteBtn").click(function() {
                $("#userListDiv").jqGrid('delRow');
            });
        });
    </script>
</body>
</html>