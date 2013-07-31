<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
    $().ready(function() {

        $("#xsTransferReqDeleteBtn<s:property value='#parameters.id'/>").click(function() {
            $.ajaxPostURL({
                url : "${base}/biz/xs/xs-transfer-req!doDelete",
                data : {
                    ids : "<s:property value='#parameters.id'/>"
                },
                confirm : '确认删除？',
                successCallback : function(response) {
                    $("#xsTransferReqIndexTabs").tabs("remove");
                    $("#xsTransferReqListDiv").jqGrid("refresh");
                }
            });
        });

        $("#btnSubmitToAudit<s:property value='#parameters.id'/>").click(function() {
            $.ajaxPostURL({
                url : "${base}/biz/xs/xs-transfer-req!doSubmit",
                data : {
                    id : "<s:property value='#parameters.id'/>"
                },
                confirm : '提交后将不可修改数据，确认继续？',
                successCallback : function(response) {
                    $("#xsTransferReqIndexTabs").tabs("remove");
                    $("#xsTransferReqListDiv").jqGrid("refresh");
                }
            });
        });

    });
</script>
<div class="container-fluid data-edit">
	<s2:form cssClass="form-horizontal" method="post"
		action="%{persistentedModel?'/biz/xs/xs-transfer-req!doUpdate':'/biz/xs/xs-transfer-req!doCreate'}">
		<s:hidden name="id" />
		<s:hidden name="version" />
		<s:hidden name="prepare" value="true" />
		<s:token />
		<div class="row-fluid">
			<div class="toolbar">
				<div class="toolbar-inner">
					<s2:button cssClass="btn btn-submit" disabled="disallowUpdate"
						callback-tab="xsTransferReqIndexTabs" callback-grid="xsTransferReqListDiv">
						<i class="icon-ok"></i> 保存
					</s2:button>
					<s2:button cssClass="btn btn-submit submit-post-close" disabled="disallowUpdate"
						callback-tab="xsTransferReqIndexTabs" callback-grid="xsTransferReqListDiv">
						<i class="icon-check"></i> 保存并关闭
					</s2:button>
					<s2:button type="reset" cssClass="btn" disabled="disallowUpdate">
						<i class="icon-repeat"></i> 重置
					</s2:button>
					<div class="divider-vertical"></div>
					<s2:button cssClass="btn" id="%{'xsTransferReqDeleteBtn'+#parameters.id[0]}"
						disabled="disallowDelete">
						<i class="icon-trash"></i> 删除
					</s2:button>
					<s2:button cssClass="btn" id="%{'btnSubmitToAudit'+#parameters.id[0]}"
						disabled="disallowSubmit">
						<i class="icon-share-alt"></i> 提交审批
					</s2:button>
				</div>
			</div>
		</div>
		<div class="well">
			<div class="row-fluid">
				<div class="span6">
					<biz:xxinput name="targetXx.xxdm" label="转入学校 " validator="{}" requiredLabel="true" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<biz:xsinput name="xsJbxx.xh" label="异动学生 " />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span6">
					<s2:textarea name="reqExplain" rows="5" label="异动说明" />
				</div>
			</div>
		</div>
	</s2:form>
</div>