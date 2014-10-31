<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="row">
	<div class="col-md-12">
		<div data-toggle="buttons" class="btn-group btn-group-department-users-type">
			<label class="btn btn-default btn-tab active btn-current"> <input type="radio" class="toggle">
				部门直属用户列表
			</label> <label class="btn btn-default btn-tab btn-cascade"> <input type="radio" class="toggle"> 部门所有下属用户列表
			</label>
		</div>
	</div>
</div>
<div class="row">
	<div class="col-md-12">
		<table class="grid-auth-department-users" data-grid="table"></table>
	</div>
</div>
<script type="text/javascript">
    $(function() {
        $(".grid-auth-department-users").data("gridOptions", {
            url : WEB_ROOT + "/auth/user!findByPage",
            postData : {
                "departmentId" : "<s:property value='#parameters.id'/>"
            },
            colModel : [ {
                label : '登录账号',
                name : 'signinid',
                editable : true,
                editoptions : {
                    updatable : false
                },
                width : 120
            }, {
                label : '机构代码',
                name : 'aclCode',
                editable : true,
                width : 120
            }, {
                label : '昵称',
                name : 'nick',
                editable : true,
                width : 120
            }, {
                label : '电子邮件',
                name : 'email',
                editable : true,
                width : 200
            }, {
                label : '移动电话',
                name : 'mobilePhone',
                editable : true,
                width : 100
            }, {
                label : '所属部门',
                name : 'department.pathDisplay',
                index : 'department.code_OR_department.title',
                width : 120,
                align : 'left'
            } ]
        });

        $(".btn-group-department-users-type .btn-current").on("click", function() {
            var $grid = $(this).closest(".row").parent().find(".ui-jqgrid-btable");
            $grid.jqGrid('search', {
                searchType : "current"
            });
        });

        $(".btn-group-department-users-type .btn-cascade").on("click", function() {
            var $grid = $(this).closest(".row").parent().find(".ui-jqgrid-btable");
            $grid.jqGrid('search', {
                searchType : "cascade"
            });
        });
    });
</script>
<%@ include file="/common/ajax-footer.jsp"%>