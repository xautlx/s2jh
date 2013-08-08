<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
    $(function() {

        $("#xxBjSelectionForm").formvalidate({
            initSubmit : false,
            submitHandler : function(form) {
                $("#xxBjSelectionListDiv").grid({
                    queryForm : $(form),
                    colNames : [ '选取', '班号', '班级名称', '年级' ],
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
                        name : 'bh',
                        align : 'center',
                        width : 100                
                    }, {
                        name : 'bjmc',
                        align : 'left'
                    }, {
                        name : 'nj',
                        align : 'left'
                    } ],
                    ondblClickRow : function(rowid, iRow, iCol, e) {
                        var rowdata = $(this).jqGrid("getRowData", rowid);
                        <s:property value='#parameters.callback'/>({
                            id : rowid,
                            bh : eraseCellValueLink(rowdata.bh),
                            bjmc : rowdata.bjmc
                        });
                        $(this).closest("div.ui-dialog-content").dialog("close");
                    },
                    multiselect : false,
                    rowNum : 10,
                    caption : "学校班级列表"
                });
            }
        });
    });
</script>
</head>
<body>
	<div class="container-fluid">
		<form id="xxBjSelectionForm" action="${base}/biz/xx/xx-bj!findByPage" class="form-inline"
			method="get">
			<div class="row-fluid">
				<div class="toolbar">
					<div class="toolbar-inner">
						<div class="input-prepend">
							<s:textfield name="search['CN_bh_OR_bjmc']" cssClass="input-medium" title="班号/名称" />
						</div>
						<div class="input-prepend">
							<span class="add-on">年级</span>
							<s:textfield name="search['EQ_nj']" cssClass="input-medium" title="年级" />
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
			<table id="xxBjSelectionListDiv"></table>
			<div id="xxBjSelectionListDivPager"></div>
		</div>
	</div>
</body>
</html>