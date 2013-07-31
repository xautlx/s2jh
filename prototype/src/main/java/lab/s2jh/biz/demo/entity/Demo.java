package lab.s2jh.biz.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * 此处给出一个基本的以UUID方式的实体对象定义参考
 * 具体可根据项目考虑选用其他主键如自增、序列等方式，只需修改相关泛型参数类型和主键定义注解即可
 * 各属性定义可先简单定义MetaData和EntityAutoCode注解即可，具体细节的控制属性含义可查看具体代码注释说明
 */
@Entity
@Table(name = "BIZ_DEMO")
@MetaData(title = "演示实体")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Demo extends BaseEntity<String> {

    @MetaData(title = "代码")
    @EntityAutoCode
    private String code;

    @MetaData(title = "标题")
    @EntityAutoCode
    private String name;

    private String id;

    @Id
    @Column(length = 40)
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        //容错处理id以空字符提交参数时修改为null避免hibernate主键无效修改
        if (id == null || StringUtils.isBlank(id)) {
            this.id = null;
        } else {
            this.id = id;
        }
    }

    /**
     * 用于某些需要显示对象时的友好字符串
     */
    @Override
    @Transient
    public String getDisplayLabel() {
        return code + "/" + name;
    }

    @Column(length = 128, unique = true, nullable = false)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(length = 256, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
