package lab.s2jh.biz.xs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.PersistableEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;
import lab.s2jh.core.entity.annotation.SkipParamBind;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "BIZ_XS_KZXX")
@MetaData(value = "学生个人扩展信息")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class XsKzxx extends PersistableEntity<String> {
    
    private XsJbxx xsJbxx;

    @MetaData(value = "学号", description = "学生在学校内编码")
    @EntityAutoCode(order = 10, search = true)
    private String xh;
    
    @MetaData(value = "是否寄宿生")
    @EntityAutoCode(order = 20, search = true)
    private Boolean sfjss;
    
    @MetaData(value = "是否独生子女")
    @EntityAutoCode(order = 20, search = true)
    private Boolean sfdszn;
    
    @MetaData(value = "是否流动人口")
    @EntityAutoCode(order = 20, search = true)
    private Boolean sfldrk;
    
    @MetaData(value = "是否受过学前教育 ")
    @EntityAutoCode(order = 20, search = true)
    private Boolean sfsgxqjy;
    
    @MetaData(value = "是否随迁子女 ")
    @EntityAutoCode(order = 20, search = true)
    private Boolean sfsqzn;
    
    @MetaData(value = "离家最近火车站")
    @EntityAutoCode(order = 20, search = true)
    private String ljzjhcz;

    private String id;

    @Id
    @Column(length = 40)
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
    
    /**
     * 基类采用通过ID判定是否新实例,由于此对象采用Assigned主键策略
     * 因此覆写采用关联对象来进行判定
     */
    @Override
    @Transient
    @JsonIgnore
    public boolean isNew() {
        return xsJbxx == null;
    }

    @Override
    @Transient
    public String getDisplay() {
        return xh;
    }

    @Column(nullable = false, length = 20)
    public String getXh() {
        return xh;
    }

    @SkipParamBind
    public void setXh(String xh) {
        this.xh = xh;
    }

    @Type(type = "lab.s2jh.biz.core.hib.BooleanStrUserType")
    @Column(nullable = true, length = 1)
    public Boolean getSfjss() {
        return sfjss;
    }

    public void setSfjss(Boolean sfjss) {
        this.sfjss = sfjss;
    }

    @Type(type = "lab.s2jh.biz.core.hib.BooleanStrUserType")
    @Column(nullable = true, length = 1 ,name="sfdszn")
    public Boolean getSfdszn() {
        return sfdszn;
    }

    public void setSfdszn(Boolean sfdszn) {
        this.sfdszn = sfdszn;
    }

    @Type(type = "lab.s2jh.biz.core.hib.BooleanStrUserType")
    @Column(nullable = false, length = 1)
    public Boolean getSfldrk() {
        return sfldrk;
    }

    public void setSfldrk(Boolean sfldrk) {
        this.sfldrk = sfldrk;
    }

    @Type(type = "lab.s2jh.biz.core.hib.BooleanStrUserType")
    @Column(nullable = true, length = 1)
    public Boolean getSfsgxqjy() {
        return sfsgxqjy;
    }

    public void setSfsgxqjy(Boolean sfsgxqjy) {
        this.sfsgxqjy = sfsgxqjy;
    }

    @Type(type = "lab.s2jh.biz.core.hib.BooleanStrUserType")
    @Column(nullable = true, length = 1)
    public Boolean getSfsqzn() {
        return sfsqzn;
    }

    public void setSfsqzn(Boolean sfsqzn) {
        this.sfsqzn = sfsqzn;
    }

    @Column(nullable = true, length = 200)
    public String getLjzjhcz() {
        return ljzjhcz;
    }

    public void setLjzjhcz(String ljzjhcz) {
        this.ljzjhcz = ljzjhcz;
    }


    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id", insertable = false, updatable = false, nullable = false)
    @JsonIgnore
    public XsJbxx getXsJbxx() {
        return xsJbxx;
    }

    public void setXsJbxx(XsJbxx xsJbxx) {
        this.xsJbxx = xsJbxx;
    }
}
