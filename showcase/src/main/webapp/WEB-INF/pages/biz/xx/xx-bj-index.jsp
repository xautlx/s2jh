<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/index-header.jsp"%>
<script type="text/javascript">
    $(function() {
        $("#xxBjListDiv").grid({
            url : "${base}/biz/xx/xx-bj!findByPage",
            colNames : [ '操作', '班号', '班级名称', '年级', '所属学校', '专业码', '所属年级', '学制', '是否少数民族双语教学班', '双语教学模式', '双语教学的少数民族语言', '班主任工号式', '创建时间', '版本号' ],
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
                        onclick : "$.popupViewDialog('${base}/biz/xx/xx-bj!viewTabs?id=" + options.rowId + "')"
                    } ]);
                }
            }, {
                name : 'bh',
                align : 'center',
                width : 100
            }, {
                name : 'bjmc',
                align : 'left'
            }, {
                name : 'nj',
                align : 'center',
                width : 60
            }, {
                name : 'xxdm',
                align : 'left'
            }, {
                name : 'zym',
                align : 'center',
                width : 60
            }, {
                name : 'ssnj',
                align : 'left'
            }, {
                name : 'xz',
                width : 30,
                align : 'center'
            }, {
                name : 'sfssmzsyjxb',
                formatter : booleanFormatter
            }, {
                name : 'syjxmsm',
                align : 'left'
            }, {
                name : 'syjxssmzyym',
                align : 'left'
            }, {
                name : 'bzrgh',
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
                url : "${base}/biz/xx/xx-bj!doDelete"
            },
            addRow : {
                url : "${base}/biz/xx/xx-bj!inputTabs"
            },
            editRow : {
                url : "${base}/biz/xx/xx-bj!inputTabs",
                labelCol : 'bjmc'
            },
            caption : "学校班级列表"
        });

        $("#xxBjAddBtn").click(function() {
            $("#xxBjListDiv").jqGrid('addRow');
        });

        $("#xxBjDeleteBtn").click(function() {
            $("#xxBjListDiv").jqGrid('delRow');
        });
    });
</script>
</head>
<body>
	<div class="container-fluid">
		<s2:tabbedpanel id="xxBjIndexTabs">
			<ul>
				<li><a href="#xxBjIndexListTab"><span>列表查询</span></a></li>
			</ul>
			<div id="xxBjIndexListTab">
				<div class="row-fluid">
					<div class="toolbar">
						<div class="toolbar-inner">
							<button type="button" class="btn" id="xxBjAddBtn">
								<i class="icon-plus-sign"></i> 添加
							</button>
							<button type="button" class="btn" id="xxBjDeleteBtn">
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
					<table id="xxBjListDiv"></table>
					<div id="xxBjListDivPager"></div>
				</div>
			</div>
		</s2:tabbedpanel>
	</div>
	<%@ include file="/common/index-footer.jsp"%>
</body>
</html>