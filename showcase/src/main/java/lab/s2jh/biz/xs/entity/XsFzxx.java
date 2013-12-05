package lab.s2jh.biz.xs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "BIZ_XS_FZXX")
@MetaData(value = "学生个人辅助信息")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class XsFzxx extends PersistableEntity<String> {

    private XsJbxx xsJbxx;

    @MetaData(value = "学号", description = "学生在学校内编码")
    @EntityAutoCode(order = 10, search = true)
    private String xh;

    @MetaData(value = "英文姓名")
    @EntityAutoCode(order = 20, searchAdvance = true, listHidden = true)
    private String ywxm;

    @MetaData(value = "姓名拼音", description = "姓名全称的汉语拼音")
    @EntityAutoCode(order = 30, searchAdvance = true, listHidden = true)
    private String xmpy;

    @MetaData(value = "曾用名", description = "指曾经正式使用过的姓名")
    @EntityAutoCode(order = 40, listHidden = true)
    private String cym;

    @MetaData(value = "身份证件有效期")
    @EntityAutoCode(order = 50, searchAdvance = true, listHidden = true)
    private String sfzjyxq;

    @MetaData(value = "信仰宗教", description = "GA 214.12。引用信仰宗教代码表（ZD_GB_XYZJM）")
    @EntityAutoCode(order = 60, searchAdvance = true, listHidden = true)
    private String xyzjm;

    @MetaData(value = "血型", description = "CELTS-29 XX 血型代码。引用血型代码表（ZD_GB_XXM）")
    @EntityAutoCode(order = 70, searchAdvance = true, listHidden = true)
    private String xxm;

    @MetaData(value = "户口所在地", description = "指户口所在地址，包括省（自治区、直辖市）/地（市、州）/县（区、旗）/乡（镇）/街（村）详细地址")
    @EntityAutoCode(order = 80, listHidden = true)
    private String hkszd;

    @MetaData(value = "户口性质", description = "GA 324.1。指公安户籍部门确认的农业户口或非农业户口")
    @EntityAutoCode(order = 90, searchAdvance = true, listHidden = true)
    private String hkxzm;

    @MetaData(value = "特长", description = "指某一方面特殊的能力或技能")
    @EntityAutoCode(order = 100, listShow = false)
    private String tc;

    private String id;
    
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

    @Id
    @Column(length = 40)
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
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

    @Column(nullable = true, length = 60)
    public String getYwxm() {
        return ywxm;
    }

    public void setYwxm(String ywxm) {
        this.ywxm = ywxm;
    }

    @Column(nullable = false, length = 20)
    public String getXmpy() {
        return xmpy;
    }

    public void setXmpy(String xmpy) {
        this.xmpy = xmpy;
    }

    @Column(nullable = true, length = 36)
    public String getCym() {
        return cym;
    }

    public void setCym(String cym) {
        this.cym = cym;
    }

    @Column(nullable = true, length = 36)
    public String getSfzjyxq() {
        return sfzjyxq;
    }

    public void setSfzjyxq(String sfzjyxq) {
        this.sfzjyxq = sfzjyxq;
    }

    @Column(nullable = true, length = 2)
    public String getXyzjm() {
        return xyzjm;
    }

    public void setXyzjm(String xyzjm) {
        this.xyzjm = xyzjm;
    }

    @Column(nullable = true, length = 1)
    public String getXxm() {
        return xxm;
    }

    public void setXxm(String xxm) {
        this.xxm = xxm;
    }

    @Column(nullable = true, length = 180)
    public String getHkszd() {
        return hkszd;
    }

    public void setHkszd(String hkszd) {
        this.hkszd = hkszd;
    }

    @Column(nullable = true, length = 1)
    public String getHkxzm() {
        return hkxzm;
    }

    public void setHkxzm(String hkxzm) {
        this.hkxzm = hkxzm;
    }

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
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
