package lab.s2jh.auth.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseUuidEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;
import lab.s2jh.sys.entity.Menu;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "TBL_AUTH_DEPARTMENT")
@MetaData(value = "部门")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Department extends BaseUuidEntity {

    private static final long serialVersionUID = -7634994834209530394L;

    @MetaData(value = "代码")
    private String code;

    @MetaData(value = "名称")
    private String title;

    @MetaData(value = "描述")
    @EntityAutoCode
    private String description;

    @MetaData(value = "联系电话")
    private String contactTel;

    @MetaData(value = "父节点")
    private Department parent;

    @MetaData(value = "部门主管")
    private User manager;

    @Column(nullable = false, length = 64)
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

    @Size(min = 3)
    @Column(nullable = false, length = 64, unique = true)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    @Transient
    public String getDisplay() {
        return title;
    }

    @Column(nullable = true, length = 64)
    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    @OneToOne
    @JoinColumn(name = "USER_ID")
    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "PARENT_ID")
    @JsonIgnore
    public Department getParent() {
        return parent;
    }

    public void setParent(Department parent) {
        this.parent = parent;
    }
}
