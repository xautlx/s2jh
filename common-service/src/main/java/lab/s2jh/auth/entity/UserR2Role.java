package lab.s2jh.auth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseEntity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "T_AUTH_USER_R2_ROLE", uniqueConstraints = @UniqueConstraint(columnNames = { "USER_ID", "ROLE_ID" }))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(title = "角色与权限关联")
public class UserR2Role extends BaseEntity<String> {

    /** 关联用户对象 */
    private User user;

    /** 关联角色对象 */
    private Role role;
    
    private String id;

    @Id
    @Column(length = 40)
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        if (id == null || StringUtils.isBlank(id)) {
            this.id = null;
        } else {
            this.id = id;
        }
    }

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "ROLE_ID", nullable = false)
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    
    @Override
    @Transient
    public String getDisplayLabel() {
        return null;
    }
}
