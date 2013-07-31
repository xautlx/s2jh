<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/page-header.jsp"%>
<script type="text/javascript">
    $().ready(function() {
        $("#revCompareTable tbody tr").each(function() {
            var revLeftPropertyValue = $.trim($(this).find("td.left-property-value").html());
            var revRightPropertyValue = $.trim($(this).find("td.right-property-value").html());
            if (revLeftPropertyValue != revRightPropertyValue) {
                $(this).addClass("warning");
            }
        });

        $("#showJustDiffChk").click(function() {
            var tbl = $("#revCompareTable");
            var trs = tbl.find("tbody tr");
            if (this.checked) {
                trs.each(function() {
                    if (!$(this).hasClass("warning")) {
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
<div class="container-fluid">
	<div class="alert">
		<label class="checkbox inline"> <input type="checkbox" id="showJustDiffChk">只显示有差异数据
		</label>
	</div>
	<table class="table" id="revCompareTable">
		<thead>
			<tr>
				<th class="span2">属性</th>
				<th class="span5"><s:property value="#request.revLeftEntity.revisionEntity.rev" />版本数据 <small><s:date
							format="yyyy-MM-dd HH:mm:ss" name="#request.revLeftEntity.revisionEntity.revstmp" /></small></th>
				<th class="span5"><s:if test="#request.revRightEntity==null">无比较数据</s:if> <s:else>
						<s:property value="#request.revRightEntity.revisionEntity.rev" />版本数据
						<small><s:date format="yyyy-MM-dd HH:mm:ss"
								name="#request.revRightEntity.revisionEntity.revstmp" /></small>
					</s:else></th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>操作人员</td>
				<td class="left-property-value"><s:property
						value="#request.revLeftEntity.revisionEntity.username" /></td>
				<td class="right-property-value"><s:if test="#request.revRightEntity==null">N/A</s:if> <s:else>
						<s:property value="#request.revRightEntity.revisionEntity.username" />
					</s:else></td>
			</tr>
			<s:iterator var="property" value="#request.revEntityProperties" status="s">
				<tr>
					<td><s:property value="#property['name']" /></td>
					<td class="left-property-value"><s:property value="#property['revLeftPropertyValue']" /></td>
					<td class="right-property-value"><s:if test="#request.revRightEntity==null">N/A</s:if> <s:else>
							<s:property value="#property['revRightPropertyValue']" />
						</s:else></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</div>