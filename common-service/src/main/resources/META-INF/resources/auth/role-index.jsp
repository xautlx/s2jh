<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="tabbable tabbable-primary">
	<ul class="nav nav-pills">
		<li class="active"><a class="tab-default" data-toggle="tab" href="#tab-role">角色列表</a></li>
		<li class="tools pull-right"><a class="btn default reload" href="javascript:;"><i class="fa fa-refresh"></i></a></li>
	</ul>
	<div class="tab-content">
		<div id="tab-role" class="tab-pane fade active in">
			<div class="row search-form-default">
				<div class="col-md-12">
					<form action="#" method="get" class="form-inline form-validation form-search" data-grid-search=".grid-role-list">
						<div class="input-group">
							<div class="input-cont">
								<input type="text" name="search['CN_code_OR_title']" class="form-control" placeholder="代码、名称...">
							</div>
							<span class="input-group-btn">
								<button class="btn green" type="submmit">
									<i class="m-icon-swapright m-icon-white"></i>&nbsp; 查&nbsp;询
								</button>
								<button class="btn default" type="reset">
									<i class="fa fa-undo"></i>&nbsp; 重&nbsp;置
								</button>
							</span>
						</div>
					</form>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<table class="grid-role-list" data-grid="table"></table>
				</div>
			</div>

		</div>
	</div>
</div>
<script type="text/javascript">
    $(function() {
        $(".grid-role-list").data("gridOptions", {
            url : '${base}/auth/role!findByPage',
            rowattr : function(ts, rowdata) {
                if (rowdata.code == 'ROLE_ADMIN' || rowdata.code == 'ROLE_ANONYMOUSLY') {
                    return {
                        'class' : 'not-editable-row'
                    };
                }
            },
            colModel : [ {
                label : '代码',
                name : 'code',
                width : 100,
                editoptions : {
                    defaultValue : 'ROLE_'
                },
                editable : true
            }, {
                label : '名称',
                name : 'title',
                width : 100,
                editable : true
            }, {
                label : '禁用',
                name : 'disabled',
                width : 60,
                editable : true,
                edittype : 'checkbox'
            }, {
                label : '描述',
                name : 'description',
                sortable : false,
                editable : true,
                width : 200,
                edittype : 'textarea',
                align : 'left'
            } ],
            editcol : 'code',
            addable : false,
            editurl : "${base}/auth/role!doSave",
            delurl : "${base}/auth/role!doDelete",
            fullediturl : "${base}/auth/role!inputTabs"
        });
    });
</script>
<%@ include file="/common/ajax-footer.jsp"%>