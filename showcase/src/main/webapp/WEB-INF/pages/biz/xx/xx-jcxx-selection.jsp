<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
    $(function() {

        $("#xxJcxxSelectionForm").formvalidate({
            initSubmit : false,
            submitHandler : function(form) {
                $("#xxJcxxSelectionListDiv").grid({
                    queryForm : $(form),
                    colNames : [ '选取', '学校识别码', '学校(机构)代码', '学校名称' ],
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
                        name : 'xxdm',
                        align : 'center',
                        width : 100,
                        formatter : function(cellValue, options, rowdata, action) {
                            return $.jgrid.buildLink({
                                text : cellValue,
                                onclick : "$.popupViewDialog('${base}/biz/xx/xx-jcxx!viewTabs?id=" + options.rowId + "')"
                            });
                        }
                    }, {
                        name : 'xxjgdm',
                        hidden : true,
                        align : 'left'
                    }, {
                        name : 'xxmc',
                        align : 'left'
                    } ],
                    ondblClickRow : function(rowid, iRow, iCol, e) {
                        var rowdata = $(this).jqGrid("getRowData", rowid);
                        <s:property value='#parameters.callback'/>({
                            id : rowid,
                            xxdm : eraseCellValueLink(rowdata.xxdm),
                            xxmc : rowdata.xxmc
                        });
                        $(this).closest("div.ui-dialog-content").dialog("close");
                    },
                    multiselect : false,
                    rowNum : 10,
                    caption : "学校列表"
                });
            }
        });
    });
</script>
</head>
<body>
	<div class="container-fluid">
		<form id="xxJcxxSelectionForm" action="${base}/biz/xx/xx-jcxx!findByPage" class="form-inline"
			method="get">
			<div class="row-fluid">
				<div class="toolbar">
					<div class="toolbar-inner">
						<div class="input-prepend">
							<s:textfield name="search['CN_xxdm_OR_xxmc']" cssClass="input-xlarge" title="代码/名称" />
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
			<table id="xxJcxxSelectionListDiv"></table>
			<div id="xxJcxxSelectionListDivPager"></div>
		</div>
	</div>
</body>
</html>