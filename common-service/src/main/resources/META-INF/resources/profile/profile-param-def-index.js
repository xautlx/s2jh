$(function() {
    $(".grid-profile-profile-param-def-index").data("gridOptions", {
        url : WEB_ROOT + '/profile/profile-param-def!findByPage',
        colModel : [ {
            label : '代码',
            name : 'code',
            width : 200,
            editable : true,
            align : 'left'
        }, {
            label : '名称',
            name : 'title',
            width : 200,
            editable : true,
            align : 'left'
        }, {
            label : '禁用标识',
            name : 'disabled',
            formatter : 'checkbox',
            editable : true
        }, {
            label : '参数类型',
            name : 'type',
            formatter : 'select',
            searchoptions : {
                value : Util.getCacheEnumsByType('dynamicParameterTypeEnum')
            },
            width : 120,
            editable : true,
            align : 'center'
        }, {
            label : '缺省参数值',
            name : 'defaultValue',
            width : 150,
            editable : true,
            align : 'left'
        }, {
            label : '排序号',
            name : 'orderRank',
            width : 60,
            editable : true,
            align : 'right'
        }, {
            label : '前端UI校验规则',
            name : 'validateRules',
            width : 200,
            editable : true,
            align : 'left'
        }, {
            label : '集合数据源 ',
            name : 'listDataSource',
            width : 200,
            editable : true,
            align : 'left'
        }, {
            label : '是否允许多选 ',
            name : 'multiSelectFlag',
            formatter : 'checkbox',
            editable : true
        } ],
        editurl : WEB_ROOT + '/profile/profile-param-def!doSave',
        delurl : WEB_ROOT + '/profile/profile-param-def!doDelete'
    });
});
