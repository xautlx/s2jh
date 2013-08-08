<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/page-header.jsp"%>
<%@ include file="/common/index-header.jsp"%>
<script type="text/javascript">
    $(function() {

        $("body").splitor({
            west : {
                size : "<s:property value='%{showRegionNavForAuthUserVar?300:200}'/>"
            }
        });

        $("#xsJbxxSearchForm").formvalidate({
            submitHandler : function(form) {
                $("#xsJbxxListDiv").grid({
                    queryForm : $(form),
                    colNames : [ '操作', '学号', '姓名', '年级', '班级', '性别', '出生日期', '出生地', '籍贯', '民族', '国籍/地区', '身份证件类型', '身份证件号', '港澳台侨外码', '政治面貌', '健康状况', '个人标识码' ],
                    colModel : [ {
                        name : 'operation',
                        align : 'center',
                        fixed : true,
                        sortable : false,
                        hidedlg : true,
                        search : false,
                        frozen : true,
                        width : 40,
                        formatter : function(cellValue, options, rowdata, action) {
                            return $.jgrid.buildButtons([ {
                                title : "编辑",
                                icon : "icon-pencil",
                                onclick : "$('#" + $(this).attr("id") + "').jqGrid('editRow','" + rowdata.id + "')"
                            }, {
                                title : "查看",
                                icon : "icon-book",
                                onclick : "$.popupViewDialog('${base}/biz/xs/xs-jbxx!viewTabs?id=" + options.rowId + "')"
                            } ]);
                        }
                    }, {
                        name : 'xh',
                        align : 'center',
                        width : 100,
                        fixed : true,
                        frozen : true
                    }, {
                        name : 'xm',
                        width : 50,
                        frozen : true,
                        align : 'center'
                    }, {
                        name : 'xxBj.nj',
                        align : 'center',
                        width : 50,
                        fixed : true
                    }, {
                        name : 'xxBj.bjmc',
                        align : 'left'
                    }, {
                        name : 'xbm',
                        align : 'center',
                        stype : 'select',
                        width : 40,
                        fixed : true,
                        formatter : function(cellValue, options, rowdata, action) {
                            return enumValueFormatter(cellValue, 'ZD_GB_XBM');
                        },
                        searchoptions : {
                            dataUrl : '${base}/biz/sys/enum-value!data.json?type=ZD_GB_XBM',
                            sopt : [ 'eq', 'le' ]
                        }
                    }, {
                        name : 'csrq',
                        align : 'center',
                        width : 100,
                        fixed : true,
                        searchoptions : {
                            sopt : [ 'eq', 'ge', 'le' ],
                            dataInit : function(elem) {
                                $(elem).dateselector({
                                    dateFormat : "yymmdd"
                                });
                            }
                        }
                    }, {
                        name : 'csdm',
                        hidden : true,
                        align : 'center',
                        width : 60,
                        fixed : true,
                        formatter : function(cellValue, options, rowdata, action) {
                            return regionCodeFormatter(cellValue);
                        }
                    }, {
                        name : 'jg',
                        hidden : true,
                        align : 'center',
                        width : 60,
                        fixed : true,
                        formatter : function(cellValue, options, rowdata, action) {
                            return regionCodeFormatter(cellValue);
                        }
                    }, {
                        name : 'mzm',
                        hidden : true,
                        align : 'left',
                        width : 60,
                        fixed : true,
                        formatter : function(cellValue, options, rowdata, action) {
                            return enumValueFormatter(cellValue, 'ZD_GB_MZM');
                        },
                        stype : 'select',
                        searchoptions : {
                            dataUrl : '${base}/biz/sys/enum-value!data.json?type=ZD_GB_MZM'
                        }
                    }, {
                        name : 'gjdqm',
                        hidden : true,
                        align : 'left',
                        width : 40,
                        fixed : true,
                        formatter : function(cellValue, options, rowdata, action) {
                            return enumValueFormatter(cellValue, 'ZD_GB_GJDQM');
                        },
                        stype : 'select',
                        searchoptions : {
                            dataUrl : '${base}/biz/sys/enum-value!data.json?type=ZD_GB_GJDQM'
                        }
                    }, {
                        name : 'sfzjlxm',
                        hidden : true,
                        align : 'center',
                        width : 60,
                        fixed : true,
                        formatter : function(cellValue, options, rowdata, action) {
                            return enumValueFormatter(cellValue, 'ZD_BB_SFZJLXM');
                        },
                        stype : 'select',
                        searchoptions : {
                            dataUrl : '${base}/biz/sys/enum-value!data.json?type=ZD_BB_SFZJLXM'
                        }
                    }, {
                        name : 'sfzjh',
                        hidden : false,
                        align : 'center',
                        width : 160,
                        fixed : true
                    }, {
                        name : 'gatqwm',
                        hidden : true,
                        align : 'center',
                        width : 50,
                        fixed : true,
                        formatter : function(cellValue, options, rowdata, action) {
                            return enumValueFormatter(cellValue, 'ZD_BB_GATQWM');
                        },
                        stype : 'select',
                        searchoptions : {
                            dataUrl : '${base}/biz/sys/enum-value!data.json?type=ZD_BB_GATQWM'
                        }
                    }, {
                        name : 'zzmmm',
                        hidden : true,
                        align : 'left',
                        width : 80,
                        fixed : true,
                        formatter : function(cellValue, options, rowdata, action) {
                            return enumValueFormatter(cellValue, 'ZD_BB_ZZMMM');
                        },
                        stype : 'select',
                        searchoptions : {
                            dataUrl : '${base}/biz/sys/enum-value!data.json?type=ZD_BB_ZZMMM'
                        }
                    }, {
                        name : 'jkzkm',
                        hidden : true,
                        align : 'left',
                        width : 80,
                        fixed : true,
                        formatter : function(cellValue, options, rowdata, action) {
                            return enumValueFormatter(cellValue, 'ZD_GB_JKZKM');
                        },
                        stype : 'select',
                        searchoptions : {
                            dataUrl : '${base}/biz/sys/enum-value!data.json?type=ZD_GB_JKZKM'
                        }
                    }, {
                        name : 'grbsm',
                        hidden : true,
                        width : 80,
                        fixed : true,
                        align : 'left'
                    } ],
                    rowNum : 15,
                    delRow : {
                        url : "${base}/biz/xs/xs-xx-mgt!doDelete"
                    },
                    addRow : {
                        url : "${base}/biz/xs/xs-xx-mgt!inputTabs"
                    },
                    editRow : {
                        url : "${base}/biz/xs/xs-xx-mgt!inputTabs",
                        labelCol : 'xm'
                    },
                    caption : "学生列表"
                });
            }
        });

        $("#xsJbxxAddBtn").click(function() {
            $("#xsJbxxListDiv").jqGrid('addRow');
        });

        $("#xsJbxxDeleteBtn").click(function() {
            $("#xsJbxxListDiv").jqGrid('addRow');
        });
    });

    function xxNjBjItemClick(event, treeId, treeNode) {
        var xxdmElement = $("#xsJbxxSearchForm").find("#search_EQ_xxdm");
        if (xxdmElement.length == 0) {
            xxdmElement = $("<input type='hidden'/>");
            xxdmElement.attr("id", "search_EQ_xxdm");
            xxdmElement.attr("name", "search['EQ_xxdm']");
            $("#xsJbxxSearchForm").append(xxdmElement);
        }

        xxdmElement.val("");
        $("#search_xxBjNj").val("");
        $("#search_xxBjMc").val("");
        if (treeNode.type == 'xx') {
            xxdmElement.val(treeNode.xxdm);
            $("#search_xxBjNj").val(treeNode.nj);
        } else if (treeNode.type == 'nj') {
            xxdmElement.val(treeNode.xxdm);
            $("#search_xxBjNj").val(treeNode.nj);
        } else if (treeNode.type == 'bj') {
            xxdmElement.val(treeNode.xxdm);
            $("#search_xxBjNj").val(treeNode.nj);
            $("#search_xxBjMc").val(treeNode.name);
        }
        $("#xsJbxxSearchForm").submit();
        return false;
    }
</script>
</head>
<body>
	<DIV class="ui-layout-west hide">
		<DIV class="header">分类导航</DIV>
		<DIV id="menuContentDiv" class="content" style="padding: 0px 0px 4px 0px;">
			<div class="container-fluid">
				<s:set var="showRegionNavForAuthUserVar" value="%{showRegionNavForAuthUser}" />
				<div class="row-fluid">
					<s:if test="showRegionNavForAuthUserVar">
						<div class="span6">
							<ul id="xsXxSearchRegionNavTree" class="ztree">数据加载...
							</ul>
						</div>
						<script type="text/javascript">
                            $.getJSON("${base}/biz/sys/region-code!regions?id=<s:property value='authUserDetails.aclCode'/>", function(data) {
                                $.fn.zTree.init($("#xsXxSearchRegionNavTree"), {
                                    callback : {
                                        onClick : function(event, treeId, treeNode) {
                                            if (treeNode.id.indexOf("0000") > -1) {
                                                return false;
                                            }
                                            $.getJSON("${base}/biz/xs/xs-xx-mgt!xxNjBjNav?regionCode=" + treeNode.id, function(data) {
                                                $.fn.zTree.init($("#xsXxSearchNjBjNavTree"), {
                                                    callback : {
                                                        onClick : xxNjBjItemClick
                                                    },
                                                    async : {
                                                        enable : false,
                                                        dataType : 'json',
                                                        type : 'get',
                                                        url : "${base}/biz/xs/xs-xx-mgt!xxNjBjNav",
                                                        autoParam : [ "type", "xxdm", "nj" ]
                                                    }
                                                }, data);
                                                var zTree = $.fn.zTree.getZTreeObj("xsXxSearchNjBjNavTree");
                                                zTree.setting.async.enable = true;
                                            });

                                            event.stopPropagation();
                                            event.preventDefault();
                                            return false;
                                        }
                                    },
                                    async : {
                                        enable : true,
                                        dataType : 'json',
                                        type : 'get',
                                        url : "${base}/biz/sys/region-code!regions",
                                        autoParam : [ "id" ]
                                    }
                                }, data);
                            });
                        </script>
						<div class="span6">
							<ul id="xsXxSearchNjBjNavTree" class="ztree">
							</ul>
						</div>
					</s:if>
					<s:else>
						<div class="span12">
							<ul id="xsXxSearchNjBjNavTree" class="ztree">
							</ul>
						</div>
						<script type="text/javascript">
                            $.getJSON("${base}/biz/xs/xs-xx-mgt!xxNjBjNav", function(data) {
                                $.fn.zTree.init($("#xsXxSearchNjBjNavTree"), {
                                    callback : {
                                        onClick : xxNjBjItemClick
                                    },
                                    async : {
                                        enable : true,
                                        dataType : 'json',
                                        type : 'get',
                                        url : "${base}/biz/xs/xs-xx-mgt!xxNjBjNav",
                                        autoParam : [ "type", "xxdm", "nj" ]
                                    }
                                }, data);
                            });
                        </script>
					</s:else>
				</div>
			</div>
		</DIV>
	</DIV>
	<DIV class="ui-layout-center hide" style="margin: 0; padding: 0;">
		<div class="container-fluid">
			<s2:tabbedpanel id="xsJbxxIndexTabs">
				<ul>
					<li><a href="#xsJbxxIndexListTab"><span>列表查询</span></a></li>
				</ul>
				<div id="xsJbxxIndexListTab">
					<form id="xsJbxxSearchForm" action="${base}/biz/xs/xs-jbxx!findByPage" class="form-inline"
						method="get">
						<s:hidden name="search['FETCH_xxBj']" value="LEFT" />
						<div class="row-fluid">
							<div class="toolbar">
								<div class="toolbar-inner">
									<button type="button" class="btn" id="xsJbxxAddBtn">
										<i class="icon-plus-sign"></i> 添加
									</button>
									<button type="button" class="btn" id="xsJbxxDeleteBtn">
										<i class="icon-trash"></i> 删除
									</button>
									<div class="divider-vertical"></div>
									<div class="input-prepend">
										<s:textfield name="search['CN_xh_OR_xm_OR_sfzjh']" cssClass="input-large"
											title="学号/姓名/身份证件号" />
									</div>
									<div class="input-prepend">
										<span class="add-on">年级</span>
										<s:textfield name="search['EQ_xxBj.nj']" id="search_xxBjNj" cssClass="input-mini" />
									</div>
									<div class="input-prepend">
										<span class="add-on">班级</span>
										<s:textfield name="search['CN_xxBj.bjmc']" id="search_xxBjMc" cssClass="input-medium" />
									</div>
									<button type="submit" class="btn">
										<i class="icon-search"></i> 查询
									</button>
									<button type="reset" class="btn">
										<i class="icon-repeat"></i> 重置
									</button>
									<div class="btn-group pull-right">
										<button type="button" class="btn" title="高级查询"
											onclick="$('#xsJbxxListDiv').jqGrid('advSearch');">
											<i class="icon-search"></i>
										</button>
									</div>
								</div>
							</div>
						</div>
					</form>
					<div class="row-fluid">
						<table id="xsJbxxListDiv"></table>
						<div id="xsJbxxListDivPager"></div>
					</div>
				</div>
			</s2:tabbedpanel>
		</div>
	</DIV>
	<%@ include file="/common/index-footer.jsp"%>
</body>
</html>