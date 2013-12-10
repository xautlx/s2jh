<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<table class="table" style="width: 100%; margin-bottom: 0px">
	<tbody>
		<s:iterator value="%{model}" status="s" var="item">
			<tr>
				<td style="width: 20px; text-align: center"><s:if test="#item.extraAttributes['readed']">
						<i class="icon-folder-open"></i>
					</s:if> <s:else>
						<i class="icon-folder-close"></i>
					</s:else></td>
				<td><a href="javascript:void(0)"
					onclick="$.popupViewDialog('${base}/profile/pub-post!view?id=<s:property value="#item.id" />')"> <s:property
							value="#item.htmlTitle" escapeHtml="false" />
				</a></td>
			</tr>
		</s:iterator>
	</tbody>
</table>