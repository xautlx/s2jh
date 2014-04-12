package lab.s2jh.auth.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseUuidEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

@Entity
@Table(name = "tbl_AUTH_ROLE")
@MetaData(value = "角色")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role extends BaseUuidEntity {

    public static final String ROLE_ADMIN_CODE = "ROLE_ADMIN";
    public static final String ROLE_ANONYMOUSLY_CODE = "ROLE_ANONYMOUSLY";
    public static final String ROLE_PROTECTED_CODE = "ROLE_PROTECTED";
    /** 通过第三方认证过来的用户，默认赋予TBD角色，以便进行后续用户绑定等操作 */
    public static final String ROLE_TBD_USER_CODE = "ROLE_TBD_USER";

    @MetaData(value = "代码", description = "必须以ROLE_打头")
    @EntityAutoCode(order = 10, search = true)
    private String code = "ROLE_";

    @MetaData(value = "名称")
    @EntityAutoCode(order = 20, search = true)
    private String title;

    @MetaData(value = "描述")
    @EntityAutoCode(listShow = false)
    private String description;

    @MetaData(value = "禁用标识", description = "禁用角色不参与权限控制逻辑")
    @EntityAutoCode(order = 40, search = true)
    private Boolean disabled = Boolean.FALSE;

    @MetaData(value = "角色权限关联")
    private List<RoleR2Privilege> roleR2Privileges = Lists.newArrayList();

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

    @Size(min = 6)
    @Pattern(regexp = "^ROLE_.*", message = "必须以[ROLE_]开头")
    @Column(nullable = false, length = 64, unique = true)
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

    @Override
    @Transient
    public String getDisplay() {
        return code + " " + title;
    }

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotAudited
    @JsonIgnore
    public List<RoleR2Privilege> getRoleR2Privileges() {
        return roleR2Privileges;
    }

    public void setRoleR2Privileges(List<RoleR2Privilege> roleR2Privileges) {
        this.roleR2Privileges = roleR2Privileges;
    }
}
