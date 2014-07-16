<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="row">
	<div class="col-md-12">
		<div class="note note-info">
			<label> <s:checkbox name="showJustDiffChk" value="true" />只显示有差异数据
			</label>
		</div>
	</div>
</div>
<div class="row">
	<div class="col-md-12">
		<table class="table table-condensed  table-advance table-hover" id="revCompareTable">
			<thead>
				<tr>
					<th>属性</th>
					<th><s:property value="#request.revLeftEntity.revisionEntity.rev" />版本数据 <small><s:date
								format="yyyy-MM-dd HH:mm:ss" name="#request.revLeftEntity.revisionEntity.revstmp" /></small></th>
					<th><s:if test="#request.revRightEntity==null">无比较数据</s:if> <s:else>
							<s:property value="#request.revRightEntity.revisionEntity.rev" />版本数据
<small> <s:date format="yyyy-MM-dd HH:mm:ss" name="#request.revRightEntity.revisionEntity.revstmp" /></small>
						</s:else></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td class="success">操作人员</td>
					<td class="left-property-value"><s:property value="#request.revLeftEntity.revisionEntity.username" /></td>
					<td class="right-property-value"><s:if test="#request.revRightEntity==null">N/A</s:if> <s:else>
							<s:property value="#request.revRightEntity.revisionEntity.username" />
						</s:else></td>
				</tr>
				<s:iterator var="property" value="#request.revEntityProperties" status="s">
					<tr>
						<td class="success"><s:property value="#property['name']" /></td>
						<td class="left-property-value"><s:property value="#property['revLeftPropertyValue']" /></td>
						<td class="right-property-value"><s:if test="#request.revRightEntity==null">N/A</s:if> <s:else>
								<s:property value="#property['revRightPropertyValue']" />
							</s:else></td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</div>
</div>
<script type="text/javascript">
    $(function() {
        $("#revCompareTable tbody tr").each(function() {
            var revLeftPropertyValue = $.trim($(this).find("td.left-property-value").html());
            var revRightPropertyValue = $.trim($(this).find("td.right-property-value").html());
            if (revLeftPropertyValue != revRightPropertyValue) {
                $(this).attr("diff", true);
                $(this).find("> .left-property-value, > .right-property-value").addClass("warning");
            } else {
                $(this).hide();
            }
        });

        $("#showJustDiffChk").click(function() {
            var tbl = $("#revCompareTable");
            var trs = tbl.find("tbody tr");
            if (this.checked) {
                trs.each(function() {
                    if ($(this).attr("diff")) {
                        $(this).show();
                    } else {
                        $(this).hide();
                    }
                });
            } else {
                trs.each(function() {
                    $(this).show();
                });
            }
        });
    });
</script>
<%@ include file="/common/ajax-footer.jsp"%>
