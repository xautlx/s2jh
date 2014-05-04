$(function() {
    $(".grid-auth-privilege-index").data("gridOptions", {
        url : WEB_ROOT + '/auth/privilege!findByPage',
        colModel : [ {
            label : '类型',
            name : 'type',
            align : 'center',
            width : 80,
            editable : true,
            stype : 'select',
            searchoptions : {
                value : Util.getCacheEnumsByType('privilegeTypeEnum')
            }
        }, {
            label : '代码',
            name : 'code',
            align : 'left',
            editable : true,
            width : 80
        }, {
            label : '名称',
            name : 'title',
            editable : true,
            align : 'left',
            width : 150
        }, {
            label : '分类',
            name : 'category',
            align : 'left',
            width : 80,
            editable : true,
            editoptions : {
                dataInit : function(elem) {
                    var $el = $(elem);
                    $el.autocomplete({
                        autoFocus : true,
                        source : Util.getCacheDatas(WEB_ROOT + "/auth/privilege!distinctCategories", $(this)),
                        minLength : 0,
                        select : function(event, ui) {
                            event.stopPropagation();
                            event.preventDefault();
                            return false;
                        }
                    }).focus(function() {
                        if ($(this).val() == "") {
                            $(this).autocomplete("search", "");
                        }
                    });
                }
            }
        }, {
            label : 'URL',
            name : 'url',
            sortable : false,
            editable : true,
            edittype : 'textarea',
            width : 300,
            align : 'left'
        }, {
            label : '排序号',
            name : 'orderRank',
            editable : true,
            width : 60,
            sorttype : 'number'
        } ],
        editcol : 'code',
        editurl : WEB_ROOT + "/auth/privilege!doSave",
        delurl : WEB_ROOT + "/auth/privilege!doDelete",
        fullediturl : WEB_ROOT + "/auth/privilege!inputTabs"
    });
});