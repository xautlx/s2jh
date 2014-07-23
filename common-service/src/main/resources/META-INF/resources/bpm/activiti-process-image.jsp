<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<img class="bpm-process-instance-image" />
<script type="text/javascript">
    $(function() {

        $(".bpm-process-instance-image:not([src])").each(
                function() {
                    var imgId = "img-" + new Date().getTime();
                    $(this).attr("id", imgId);
                    App.blockUI($("#" + imgId).closest(".ajax-get-container"));
                    var src = "${base}/bpm/activiti!processInstanceImage?bizKey=<s:property value='#parameters.bizKey'/>&processInstanceId=<s:property value='#parameters.processInstanceId'/>&_"
                            + new Date().getTime();
                    $(this).attr("src", src);

                    document.getElementById(imgId).onload = function() {
                        App.unblockUI($("#" + imgId).closest(".ajax-get-container"));
                    }
                })

    });
</script>
<%@ include file="/common/ajax-footer.jsp"%>
