<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/page-header.jsp"%>
<%@ include file="/common/index-header.jsp"%>
</head>
<body>
	<div class="container-fluid">
		<s2:tabbedpanel id="xxJcxxIndexTabs">
			<ul>
				<li><a href="#xxJcxxIndexListTab"><span>列表查询</span></a></li>
			</ul>
			<div id="xxJcxxIndexListTab">
				<div class="row-fluid">
					<div class="toolbar">
						<div class="toolbar-inner">
							<button type="button" class="btn" id="xxJcxxAddBtn">
								<i class="icon-plus-sign"></i> 添加
							</button>
							<button type="button" class="btn" id="xxJcxxDeleteBtn">
								<i class="icon-trash"></i> 删除
							</button>
							<div class="btn-group pull-right">
								<button type="button" class="btn" title="高级查询"
									onclick="$('#xxJcxxListDiv').jqGrid('advSearch');">
									<i class="icon-search"></i>
								</button>
							</div>
						</div>
					</div>
				</div>
				<div class="row-fluid">
					<table id="xxJcxxListDiv"></table>
					<div id="xxJcxxListDivPager"></div>
				</div>
			</div>
		</s2:tabbedpanel>
	</div>
	<%@ include file="/common/index-footer.jsp"%>
	<script type="text/javascript">
        $(function() {
            $("#xxJcxxListDiv").grid(
                    {
                        url : "${base}/biz/xx/xx-jcxx!findByPage",
                        colNames : [ '操作', '学校识别码', '学校(机构)代码', '学校名称', '学校英文名称', '行政区划码', '学校邮政编码', '属地管理教育行政部门代码', '学校所属主管教育行政部门代码', '举办者代码', '办学类型代码', '城乡分类代码', '学校举办者类别码', '校长姓名', '校长手机号码',
                                '联系电话', '传真电话', '电子信箱', '主页地址', '学校办别码', '创建时间', '版本号' ],
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
                                    onclick : "$.popupViewDialog('${base}/biz/xx/xx-jcxx!viewTabs?id=" + options.rowId + "')"
                                } ]);
                            }
                        }, {
                            name : 'xxdm',
                            align : 'center',
                            width : 100
                        }, {
                            name : 'xxjgdm',
                            hidden : true,
                            align : 'left'
                        }, {
                            name : 'xxmc',
                            align : 'left'
                        }, {
                            name : 'xxywmc',
                            hidden : true,
                            align : 'left'
                        }, {
                            name : 'xzqhm',
                            align : 'center',
                            fixed : true,
                            width : 70
                        }, {
                            name : 'xxyzbm',
                            hidden : true,
                            align : 'left'
                        }, {
                            name : 'sdgljyxzbm',
                            hidden : true,
                            align : 'left'
                        }, {
                            name : 'sszgdwm',
                            align : 'center',
                            fixed : true,
                            width : 70
                        }, {
                            name : 'xxjbzm',
                            hidden : true,
                            align : 'left'
                        }, {
                            name : 'xxbxlxm',
                            hidden : true,
                            align : 'left'
                        }, {
                            name : 'szdcxlxm',
                            hidden : true,
                            align : 'left'
                        }, {
                            name : 'xxbbm2',
                            hidden : true,
                            align : 'left'
                        }, {
                            name : 'xzxm',
                            align : 'center',
                            fixed : true,
                            width : 70
                        }, {
                            name : 'xzsjhm',
                            align : 'left'
                        }, {
                            name : 'lxdh',
                            align : 'left'
                        }, {
                            name : 'czdh',
                            hidden : true,
                            align : 'left'
                        }, {
                            name : 'dzxx',
                            align : 'left'
                        }, {
                            name : 'zydz',
                            hidden : true,
                            align : 'left'
                        }, {
                            name : 'xxbbm',
                            hidden : true,
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
                            url : "${base}/biz/xx/xx-jcxx!doDelete"
                        },
                        addRow : {
                            url : "${base}/biz/xx/xx-jcxx!inputTabs"
                        },
                        editRow : {
                            url : "${base}/biz/xx/xx-jcxx!inputTabs",
                            labelCol : 'xxmc'
                        },
                        ondblClickEnabledRow : function(rowid, iRow, iCol, e, rowdata) {
                            $("#xxJcxxIndexTabs").tabs("add", '${base}/biz/xx/xx-jcxx!inputTabs?id=' + rowid, "编辑-" + eraseCellValueLink(rowdata.xxdm));
                        },
                        caption : "学校基本信息列表"
                    });

            $("#xxJcxxAddBtn").click(function() {
                $("#xxJcxxListDiv").jqGrid('addRow');
            });

            $("#xxJcxxDeleteBtn").click(function() {
                $("#xxJcxxListDiv").jqGrid('delRow');
            });
        });
    </script>
</body>
</html>