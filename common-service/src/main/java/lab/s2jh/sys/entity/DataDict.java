package lab.s2jh.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;
import lab.s2jh.sys.service.DataDictService;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "T_SYS_DATA_DICT", uniqueConstraints = @UniqueConstraint(columnNames = { "category", "key1Value",
        "key2Value" }))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(title = "数据字典")
public class DataDict extends BaseEntity<String> {

    /** 类别定义。分类代码对应中文描述在dataDictCategory国际化资源文件中定义。具体使用说明请参考 
     * {@link DataDictService#findDataDictByCategory(String)} 
     */
    @MetaData(title = "分类代码", description = "以代码方式维护数据，不要用中文")
    @EntityAutoCode(order = 5, search = true)
    private String category;

    @MetaData(title = "代码", description = "自动生成，主要用在偶尔进行父子关系设定时需要")
    @EntityAutoCode(order = 6, search = false)
    private String code;

    @MetaData(title = "父代码", description = "主要用在偶尔进行父子关系设定时需要")
    @EntityAutoCode(order = 7, search = false)
    private String parentCode;

    /** 
     * 字典数据的Key1值，绝大部分情况对于单一key就能确定唯一性的字典数据只需维护此字段值即可
     * key值定义是基于同一category下面的唯一性定义，不同category下面的key可以相同
     * 注意：为了灵活性，数据库层面无唯一约束定义，请自行确保category+key1数据的唯一性
     */
    @MetaData(title = "Key1定义")
    @EntityAutoCode(order = 8, search = true)
    private String key1Value;

    /** 
     * 字典数据的Key2值，如果key1值不能单一确定唯一性则可以启用key2值进行组合唯一控制
     */
    @MetaData(title = "Key2定义")
    @EntityAutoCode(order = 10, search = true)
    private String key2Value;

    /**
     * 字典数据对应的数据Value值
     * 大部分情况一般都是key-value形式的数据，只需要维护key1Value和data1Value即可，
     * 然后通过{@link DataDictService#findDataDictByCategory(String)}即可快速返回key-value形式的Map数据
     */
    @MetaData(title = "数据1设定")
    @EntityAutoCode(order = 20, search = true)
    private String data1Value;

    /**
     * 字典数据对应的补充数据Value值，如果除了data1Value业务设计需要其他补充数据可启用扩展Value字段存取这些值
     * 对于扩展数据的获取一般通过{@link lab.s2jh.sys.service.DataDictService#findDataDictListByCategory(String)}
     * 对于返回的数据，根据实际业务定制化使用即可
     */
    @MetaData(title = "数据2设定")
    @EntityAutoCode(order = 30, search = true)
    private String data2Value;

    /**
     * 字典数据对应的补充数据Value值，如果除了data1Value业务设计需要其他补充数据可启用扩展Value字段存取这些值
     * 对于扩展数据的获取一般通过{@link lab.s2jh.sys.service.DataDictService#findDataDictListByCategory(String)}
     * 对于返回的数据，根据实际业务定制化使用即可
     */
    @MetaData(title = "数据3设定", description = "以CLOB大文本方式存储用于特定的大文本数据配置")
    @EntityAutoCode(order = 30, search = false)
    private String data3Value;

    @MetaData(title = "禁用标识", description = "禁用项目全局不显示")
    @EntityAutoCode(order = 40, search = true)
    private Boolean disabled = Boolean.FALSE;

    @MetaData(title = "排序号", description = "相对排序号，数字越大越靠上显示")
    @EntityAutoCode(order = 1000)
    private Integer orderRank = 10;

    private String id;

    @Id
    @Column(length = 40)
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @Column(length = 128, nullable = false)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(length = 64)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(length = 64)
    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    @Column(length = 128, nullable = false)
    public String getKey1Value() {
        return key1Value;
    }

    public void setKey1Value(String key1Value) {
        this.key1Value = key1Value;
    }

    @Column(length = 128)
    public String getKey2Value() {
        return key2Value;
    }

    public void setKey2Value(String key2Value) {
        this.key2Value = key2Value;
    }

    @Column(length = 256)
    public String getData1Value() {
        return data1Value;
    }

    public void setData1Value(String data1Value) {
        this.data1Value = data1Value;
    }

    @Column(length = 256)
    public String getData2Value() {
        return data2Value;
    }

    public void setData2Value(String data2Value) {
        this.data2Value = data2Value;
    }

    @Lob
    public String getData3Value() {
        return data3Value;
    }

    public void setData3Value(String data3Value) {
        this.data3Value = data3Value;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Integer getOrderRank() {
        return orderRank;
    }

    public void setOrderRank(Integer orderRank) {
        this.orderRank = orderRank;
    }

    @Override
    @Transient
    public String getDisplayLabel() {
        return category + "[" + key1Value + ":" + data1Value + "]";
    }
}
