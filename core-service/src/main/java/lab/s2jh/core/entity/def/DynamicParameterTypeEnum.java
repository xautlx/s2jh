package lab.s2jh.core.entity.def;

import lab.s2jh.core.annotation.MetaData;

public enum DynamicParameterTypeEnum {

    @MetaData(title = "日期", description = "不带时分的年月日日期,格式yyyy-MM-dd")
    DATE,

    @MetaData(title = "日期时间", description = "带时分的日期,格式：yyyy-MM-dd HH:mm:ss")
    TIMESTAMP,

    @MetaData(title = "浮点数")
    FLOAT,

    @MetaData(title = "整数")
    INTEGER,

    @MetaData(title = "是否布尔型")
    BOOLEAN,

    @MetaData(title = "枚举数据定义", description = "根据枚举对象的name()和getLabel()返回对应的key1-value1结构数据,对应的listDataSource写法示例：lab.s2jh.demo.po.entity.PurchaseOrder$PurchaseOrderTypEnum")
    ENUM,

    @MetaData(title = "数据字典下拉列表", description = "提供数据字典表中CATEGORY对应的key1-value1结构数据,对应的listDataSource写法示例：PRIVILEGE_CATEGORY")
    DATA_DICT_LIST,

    @MetaData(title = "SQL查询下拉列表", description = "直接以SQL语句形式返回key-value结构数据,对应的listDataSource写法示例：select role_code,role_name from t_sys_role")
    SQL_LIST,

    @MetaData(title = "OGNL语法键值对", description = "以OGNL语法构造key-value结构数据,对应的listDataSource写法示例：#{'A':'ClassA','B':'ClassB'}")
    OGNL_LIST,

    @MetaData(title = "多行文本")
    MULTI_TEXT,

    @MetaData(title = "HTML文本")
    HTML_TEXT,

    @MetaData(title = "单行文本")
    STRING;

}
