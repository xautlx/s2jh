function aclTypeFormatter(cellValue, options, rowdata) {
    if (cellValue == '99') {
        return '超级';
    } else if (cellValue == '80') {
        return '中央';
    } else if (cellValue == '70') {
        return '省级';
    } else if (cellValue == '60') {
        return '地市';
    } else if (cellValue == '50') {
        return '区县';
    } else if (cellValue == '40') {
        return '学校';
    } else if (cellValue == '10') {
        return '普通';
    } else {
        return '未知';
    }
}

if (top.enumTypeValueCache==undefined ||  $.isEmptyObject(top.enumTypeValueCache)) {
    top.enumTypeValueCache={};
    $.ajax(WEB_ROOT+"/biz/sys/enum-value!data.json", {
        cache : true,
        dataType : 'json',
        success : function(data, status, xhr) {
            for ( var x in data) {
                var enumTypeValue = {};
                for ( var key in data[x]) {
                    key = key+ '';
                    enumTypeValue[key] = data[x][key];
                }
                top.enumTypeValueCache[x] = enumTypeValue;
            }
        }
    });
}

function enumValueFormatter(enumValue, enumType) {
    if (enumType in top.enumTypeValueCache) {
        var value=top.enumTypeValueCache[enumType][enumValue];
        return value==undefined?'':value;
    } else {
        alert("It's better to pre init enum data for type: "+enumType);
        $.ajax(WEB_ROOT+"/biz/sys/enum-value!data.json?type=" + enumType, {
            cache : true,
            dataType : 'json',
            success : function(data, status, xhr) {
                var enumTypeValue = {};
                for ( var key in data[x]) {
                    key = key+ '';
                    enumTypeValue[key] = data[x][key];
                }
                top.enumTypeValueCache[enumType] = enumTypeValue;
                return enumTypeValue[enumValue];
            }
        });
    }
}


function regionCodeFormatter(cellValue) {
    link = '<a href="javascript:void(0)" title="查看行政区划信息" onclick="$.popupViewDialog(WEB_ROOT+\'/biz/sys/region-code!view?regionCode=' + cellValue + '\')">' + cellValue + '</a>';
    return link;
}

