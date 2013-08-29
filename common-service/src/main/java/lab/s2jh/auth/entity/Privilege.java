package lab.s2jh.auth.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

/**
 * 权限控制资源，指对菜单、按钮、URL等访问控制点
 * 
 */
@Entity
@Table(name = "T_AUTH_PRIVILEGE")
@MetaData(title = "权限")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Privilege extends BaseEntity<String> {

    public final static String DATA_DICT_PRIVILEGE_TYPE = "PRIVILEGE_TYPE";

    @MetaData(title = "分类")
    @EntityAutoCode(order = 5, search = true)
    private String category;

    @MetaData(title = "类型")
    @EntityAutoCode(order = 8, search = true)
    private String type;

    @MetaData(title = "代码")
    @EntityAutoCode(order = 10, search = true)
    private String code;

    @MetaData(title = "名称")
    @EntityAutoCode(order = 20, search = true)
    private String title;

    @MetaData(title = "URL")
    @EntityAutoCode(order = 30, search = true)
    private String url;

    @MetaData(title = "描述")
    @EntityAutoCode(listShow = false)
    private String description;

    @MetaData(title = "禁用标识", description = "禁用不参与权限控制逻辑")
    @EntityAutoCode(order = 40, search = true)
    private Boolean disabled = Boolean.FALSE;

    /** 
     * 权限控制的优先级，数字越大匹配优先级越高，大部分情况无需要考虑此属性，直接默认值即可；
     * 如果权限URL部分由包含关系需要用到此属性，具体请查看Spring Security对于URL匹配规则说明 
     */
    @MetaData(title = "排序号", description = "权限控制的优先级，数字越大匹配优先级越高")
    @EntityAutoCode(order = 50)
    private Integer orderRank = 100;

    @MetaData(title = "角色权限关联")
    private List<RoleR2Privilege> roleR2Privileges = Lists.newArrayList();

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

    @Column(nullable = true, length = 128)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(nullable = false, length = 256)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(nullable = true, length = 2000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(nullable = false, unique = true, length = 64)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    @Column(nullable = true, length = 512)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(nullable = false, length = 128)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    @Transient
    public String getDisplayLabel() {
        return title;
    }

    @OneToMany(mappedBy = "privilege", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    public List<RoleR2Privilege> getRoleR2Privileges() {
        return roleR2Privileges;
    }

    public void setRoleR2Privileges(List<RoleR2Privilege> roleR2Privileges) {
        this.roleR2Privileges = roleR2Privileges;
    }
}