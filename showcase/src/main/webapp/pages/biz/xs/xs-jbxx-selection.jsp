<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="container-fluid">
	<form id="xsJbxxSelectionForm" action="${base}/biz/xs/xs-jbxx!findByPage" class="form-inline"
		method="get">
		<div class="row-fluid">
			<div class="toolbar">
				<div class="toolbar-inner">
					<div class="input-prepend">
						<s:textfield name="search['CN_xh_OR_xm_OR_sfzjh']" cssClass="input-xlarge" title="学号/姓名/身份证件号" />
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
		<table id="xsJbxxSelectionListDiv"></table>
		<div id="xsJbxxSelectionListDivPager"></div>
	</div>
</div>
<script type="text/javascript">
    $(function() {
        $("#xsJbxxSelectionForm").formvalidate({
            initSubmit : false,
            submitHandler : function(form) {
                $("#xsJbxxSelectionListDiv").grid({
                    queryForm : $(form),
                    colNames : [ '选取', '学号', '姓名', '性别', '身份证件类型', '身份证件号', '学校代码' ],
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
                                title : "选取",
                                icon : "icon-ok",
                                onclick : "$.triggerGridRowDblClick(this)"
                            }]);
                        }
                    }, {
                        name : 'xh',
                        align : 'center',
                        width : 150,
                        fixed : true,
                        formatter : function(cellValue, options, rowdata, action) {
                            return $.jgrid.buildLink({
                                text : cellValue,
                                onclick : "$.popupViewDialog('${base}/biz/xs/xs-jbxx!viewTabs?id=" + options.rowId + "')"
                            });
                        }
                    }, {
                        name : 'xm',
                        align : 'left'
                    }, {
                        name : 'xbm',
                        align : 'center',
                        width : 30,
                        fixed : true,
                        formatter : function(cellValue, options, rowdata, action) {
                            return enumValueFormatter(cellValue, 'ZD_GB_XBM');
                        }
                    }, {
                        name : 'sfzjlxm',
                        hidden : true,
                        align : 'left'
                    }, {
                        name : 'sfzjh',
                        hidden : false,
                        align : 'center',
                        width : 200,
                        fixed : true
                    }, {
                        name : 'xxdm',
                        hidden : true
                    } ],
                    ondblClickRow : function(rowid, iRow, iCol, e, rowdata) {
                        var rowdata = $(this).jqGrid("getRowData", rowid);
                        <s:property value='#parameters.callback'/>({
                            id : rowid,
                            xh : eraseCellValueLink(rowdata.xh),
                            xxdm : rowdata.xxdm,
                            xm : rowdata.xm
                        });
                        $(this).closest("div.ui-dialog-content").dialog("close");

                    },
                    multiselect : false,
                    rowNum : 10,
                    caption : "学生列表"
                });
            }
        });
    });
</script>