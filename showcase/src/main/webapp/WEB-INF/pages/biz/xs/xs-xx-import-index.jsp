<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/page-header.jsp"%>
<%@ include file="/common/index-header.jsp"%>
<script type="text/javascript">
    $(function() {

        $("#wizard").wizard({
            changeStep : function(index) {
                switch (index) {
                case 0:
                    return {
                        beforeNextShow : function() {
                            $("#fileUploadForm").submit();
                            $.blockUI();
                            return false;
                        }
                    };
                    break;
                case 1:
                    return {
                        afterNextShow : function() {
                            $("#xsXxImportValidListDiv").grid({
                                url : "${base}/biz/xs/xs-xx-import!findByPage?search['EQ_validatePass']=true",
                                colNames : [ '文件名称', 'Excel Sheet名称', 'Excel行号', '学号', '姓名' ],
                                colModel : [ {
                                    name : 'fileName',
                                    hidden : true,
                                    align : 'left'
                                }, {
                                    name : 'sheetName',
                                    hidden : true,
                                    align : 'left'
                                }, {
                                    name : 'lineNum',
                                    width : 60,
                                    fixed : true,
                                    align : 'center'
                                }, {
                                    name : 'xh',
                                    width : 120,
                                    fixed : true,
                                    align : 'left'
                                }, {
                                    name : 'xm',
                                    width : 120,
                                    fixed : true,
                                    align : 'left'
                                } ],
                                rowNum : 10,
                                sortorder : "asc",
                                sortname : "lineNum",
                                caption : "有效数据列表显示"
                            });
                        }
                    };
                    break;
                case 2:
                    return {
                        beforeNextShow : function() {
                            $.blockUI();
                            $.ajaxPostURL({
                                url : '${base}/biz/xs/xs-xx-import!doImport',
                                confirm : '确认把当前所有有效数据导入到学籍系统数据吗？',
                                successCallback : function(response) {
                                    $("#wizard").wizard("show", 3);
                                    $("#xsXxImportResultForm").submit();
                                }
                            });
                            return false;
                        }
                    };
                    break;
                }

                return true;
            },
            finish : function(event) {
                event.preventDefault();
                alert("Finished !")
            }
        });

        $("#fileUploadForm").formvalidate({
            successCallback : function(response, submitButton) {
                $("#wizard").wizard("show", 1);
                $("#xsXxImportSearchForm").submit();
            }
        });

        $("#xsXxImportSearchForm").formvalidate({
            initSubmit : false,
            submitHandler : function(form) {
                $("#xsXxImportListDiv").grid({
                    queryForm : $(form),
                    colNames : [ '操作', '文件名称', 'Excel Sheet名称', 'Excel行号', '学号', '姓名', '校验通过', '校验未过说明', '创建时间', '版本号' ],
                    colModel : [ {
                        name : 'operation',
                        align : 'center',
                        fixed : true,
                        sortable : false,
                        hidedlg : true,
                        width : 25,
                        formatter : function(cellValue, options, rowdata, action) {
                            return $.jgrid.buildButtons([ {
                                title : "编辑",
                                icon : "icon-pencil",
                                onclick : "$('#" + $(this).attr("id") + "').jqGrid('editRow','" + rowdata.id + "')"
                            } ]);
                        }
                    }, {
                        name : 'fileName',
                        hidden : true,
                        align : 'left'
                    }, {
                        name : 'sheetName',
                        hidden : true,
                        align : 'left'
                    }, {
                        name : 'lineNum',
                        width : 60,
                        fixed : true,
                        align : 'center'
                    }, {
                        name : 'xh',
                        width : 120,
                        fixed : true,
                        align : 'center'
                    }, {
                        name : 'xm',
                        width : 120,
                        fixed : true,
                        align : 'center'
                    }, {
                        name : 'validatePass',
                        width : 60,
                        fixed : true,
                        formatter : booleanFormatter,
                        align : 'center',
                        stype : 'select',
                        searchoptions : {
                            dataUrl : '${base}/sys/util!dict.json?type=booleanLabelMap',
                            sopt : [ 'eq', 'ne' ]
                        }
                    }, {
                        name : 'validateMessage',
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
                    ondblClickEnabledRow : function(rowid, iRow, iCol, e, rowdata) {
                        $("#xsXxImportIndexTabs").tabs("add", '${base}/biz/xs/xs-xx-import!inputTabs?id=' + rowid, "编辑-" + eraseCellValueLink(rowdata.display));
                    },
                    rowNum : 10,
                    sortorder : "asc",
                    sortname : "lineNum",
                    caption : "Excel导入数据列表"
                });
            }
        });

        $("#xsXxImportResultForm").formvalidate({
            initSubmit : false,
            submitHandler : function(form) {
                $("#xsXxImportResultListDiv").grid({
                    queryForm : $(form),
                    colNames : [ '文件名称', 'Excel Sheet名称', 'Excel行号', '学校代码', '学号', '姓名', '导入成功', '导入失败说明' ],
                    colModel : [ {
                        name : 'fileName',
                        hidden : true,
                        align : 'left'
                    }, {
                        name : 'sheetName',
                        hidden : true,
                        align : 'left'
                    }, {
                        name : 'lineNum',
                        width : 60,
                        fixed : true,
                        align : 'center'
                    }, {
                        name : 'xxdm',
                        width : 120,
                        fixed : true,
                        align : 'center'
                    }, {
                        name : 'xh',
                        width : 120,
                        fixed : true,
                        align : 'center'
                    }, {
                        name : 'xm',
                        width : 120,
                        fixed : true,
                        align : 'center'
                    }, {
                        name : 'importPass',
                        width : 60,
                        fixed : true,
                        formatter : booleanFormatter,
                        align : 'center',
                        stype : 'select',
                        searchoptions : {
                            dataUrl : '${base}/sys/util!dict.json?type=booleanLabelMap',
                            sopt : [ 'eq', 'ne' ]
                        }
                    }, {
                        name : 'importMessage',
                        align : 'left'
                    } ],
                    rowNum : 10,
                    sortorder : "asc",
                    sortname : "lineNum",
                    caption : "导入结果数据列表显示"
                });
            }
        });

        $('#uploadFiles').MultiFile();

    });
</script>
</head>
<body>
	<div class="container-fluid">
		<div id="wizard" class="hide">
			<div class="active" data-title="上传数据文件">
				<h2 class="step-title">上传指定格式的Excel数据文件</h2>
				<div class="well-small">
					<form action="${base}/biz/xs/xs-xx-import!doUploadFile" method="post" id="fileUploadForm"
						enctype="multipart/form-data">
						<div class="row-fluid">
							<div class="span6">
								<s:file name="uploadFiles" cssClass="input-fluid" label="上传数据文件" />
							</div>
							<div class="span6">
								提示：请确认数据文件符合模板文件格式要求，点击下载查看<a href="${base}/template/XS_IMPORT_TEMPATE.xlsx" target="_blank">导入数据模板文件</a>
							</div>
						</div>
					</form>
				</div>
			</div>

			<div data-title="处理解析数据">
				<h2 class="step-title">检查并修正无效数据</h2>
				<form id="xsXxImportSearchForm" action="${base}/biz/xs/xs-xx-import!findByPage"
					class="form-inline" method="get">
					<div class="row-fluid">
						<div class="toolbar">
							<div class="toolbar-inner">
								<div class="input-prepend">
									<s:textfield name="search['CN_xh_OR_xm']" cssClass="input-large" title="学号/姓名" />
								</div>
								<div class="input-prepend">
									<span class="add-on">Excel行号</span>
									<s:textfield name="search['EQ_lineNum']" cssClass="input-mini" title="Excel行号" />
								</div>
								<div class="input-prepend">
									<span class="add-on">校验通过</span>
									<s:select name="search['EQ_validatePass']" list="#application.enums.booleanLabel"
										emptyOption="true" value="false" cssClass="input-mini" />
								</div>
								<button type="submit" class="btn">
									<i class="icon-search"></i> 查询
								</button>
								<button type="reset" class="btn">
									<i class="icon-repeat"></i> 重置
								</button>
							</div>
						</div>
					</div>
				</form>
				<div class="row-fluid">
					<table id="xsXxImportListDiv"></table>
					<div id="xsXxImportListDivPager"></div>
				</div>
			</div>

			<div data-title="导入有效数据">
				<h2 class="step-title">提交导入有效数据</h2>
				<div class="row-fluid">
					<table id="xsXxImportValidListDiv"></table>
					<div id="xsXxImportValidListDivPager"></div>
				</div>
			</div>
			<div data-title="导入结果">
				<h2 class="step-title">查看数据导入结果</h2>
				<form id="xsXxImportResultForm" action="${base}/biz/xs/xs-xx-import!findByPage"
					class="form-inline" method="get">
					<s:hidden name="search['EQ_validatePass']" value="true" />
					<div class="row-fluid">
						<div class="toolbar">
							<div class="toolbar-inner">
								<div class="input-prepend">
									<s:textfield name="search['CN_xh_OR_xm']" cssClass="input-large" title="学号/姓名" />
								</div>
								<div class="input-prepend">
									<span class="add-on">Excel行号</span>
									<s:textfield name="search['EQ_lineNum']" cssClass="input-mini" title="Excel行号" />
								</div>
								<div class="input-prepend">
									<span class="add-on">导入通过</span>
									<s:select name="search['EQ_importPass']" list="#application.enums.booleanLabel"
										emptyOption="true" cssClass="input-mini" />
								</div>
								<button type="submit" class="btn">
									<i class="icon-search"></i> 查询
								</button>
								<button type="reset" class="btn">
									<i class="icon-repeat"></i> 重置
								</button>
							</div>
						</div>
					</div>
				</form>
				<div class="row-fluid">
					<table id="xsXxImportResultListDiv"></table>
					<div id="xsXxImportResultListDivPager"></div>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/common/index-footer.jsp"%>
</body>
</html>