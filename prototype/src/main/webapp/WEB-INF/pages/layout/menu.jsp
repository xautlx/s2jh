<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script>
    $(function() {
        $("#menuAccordion").accordion({
            heightStyle : "fill"
        });
    });

    var zNodes = <s:property value='#request.menuJsonData' escape='false'/>;
    var menuNodes = {};
    function treeToSimpleAarry(node) {
        menuNodes[node.id] = node;
        var children = node.children;
        if (children) {
            $.each(children, function() {
                //alert(this.id);
                treeToSimpleAarry(this);
            })
        }
    }
    $.each(zNodes, function() {
        treeToSimpleAarry(this);
    });

    function menuTabChange(mid) {
        //alert("mid=" + mid);
        var treeNode = menuNodes[mid];
        if (treeNode) {
            var url = treeNode.url;
            if (url == undefined || url == '') {
                //alert("未定义菜单URL");
                return;
            }
            if (url.startWith("/")) {
                url = "${base}" + url;
            }
            url = AddOrReplaceUrlParameter(url, '_', new Date().getTime());
            //alert(url);
            tabpanel.addTab({
                id : 'tabpanel_' + mid,
                title : treeNode.name,
                closable : true,
                html : '<iframe src="' + url + '" width="100%" height="100%" frameborder="0" id="' + 'tabpanel_' + mid + 'Frame" name="' + 'tabpanel_' + mid + 'Frame"></iframe>'
            });
        }
    }

    var curUrl = location.href;
    var idx = curUrl.indexOf("#/");
    if (idx > -1) {
        var mid = curUrl.substring(idx + 2, curUrl.length);
        menuTabChange(mid);
    }

    $.address.internalChange(function(event) {
        menuTabChange(event.value.substring(1, event.value.length));
    }).externalChange(function(event) {
        menuTabChange(event.value.substring(1, event.value.length));
    });
</script>

<div id="menuAccordion">
	<s:iterator value="#request.rootMenus" status="status" var="item">
		<h3>
			<s:property value="#item.name" />
		</h3>
		<div>
			<ul id="navMenu<s:property value='#item.id' />" class="ztree">加载数据...
			</ul>
		</div>
		<script type="text/javascript">
            var itemData;
            $.each(zNodes, function(i, item) {
                if (item.id == "<s:property value='#item.id' />") {
                    itemData = item;
                }
            });
            $.fn.zTree.init($("#navMenu<s:property value='#item.id'/>"), {
                callback : {
                    onClick : function(event, treeId, treeNode) {
                        event.stopPropagation();
                        event.preventDefault();
                        $.address.value(treeNode.id);
                        return false;
                    }
                }
            }, itemData.children);
        </script>
	</s:iterator>
</div>