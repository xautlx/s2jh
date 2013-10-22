package lab.s2jh.biz.xx.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.PersistableEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "BIZ_XX_BJ", uniqueConstraints = { @UniqueConstraint(columnNames = { "ssxx", "bh" }) })
@MetaData(value = "学校班级")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class XxBj extends PersistableEntity<String> {

    @MetaData(value = "班号")
    @EntityAutoCode(order = 10, search = true)
    private String bh;

    @MetaData(value = "班级名称", description = "在学校中该班级的名称")
    @EntityAutoCode(order = 20, search = true)
    private String bjmc;

    @MetaData(value = "年级")
    @EntityAutoCode(order = 30, search = true)
    private String nj;

    @MetaData(value = "所属学校")
    @EntityAutoCode(order = 40, search = true)
    private String xxdm;

    @MetaData(value = "专业码")
    @EntityAutoCode(order = 50, searchAdvance = true)
    private String zym;

    @MetaData(value = "所属年级")
    @EntityAutoCode(order = 60, searchAdvance = true)
    private String ssnj;

    @MetaData(value = "学制", description = "以年为单位")
    @EntityAutoCode(order = 70, searchAdvance = true)
    private BigDecimal xz;

    @MetaData(value = "是否少数民族双语教学班", description = "0-否 1-是")
    @EntityAutoCode(order = 80, searchAdvance = true)
    private Boolean sfssmzsyjxb;

    @MetaData(value = "双语教学模式", description = "少数民族双语教学模式：1-一类模式 2-二类模式 3-三类模式")
    @EntityAutoCode(order = 90, searchAdvance = true)
    private String syjxmsm;

    @MetaData(value = "双语教学的少数民族语言")
    @EntityAutoCode(order = 100, searchAdvance = true)
    private String syjxssmzyym;

    @MetaData(value = "班主任工号式")
    @EntityAutoCode(order = 110, searchAdvance = true)
    private String bzrgh;

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

    @Override
    @Transient
    public String getDisplayLabel() {
        return bjmc;
    }

    @Column(nullable = false, length = 10)
    public String getBh() {
        return bh;
    }

    public void setBh(String bh) {
        this.bh = bh;
    }

    @Column(nullable = false, length = 20)
    public String getBjmc() {
        return bjmc;
    }

    public void setBjmc(String bjmc) {
        this.bjmc = bjmc;
    }

    @Column(nullable = false, length = 10, name = "nj")
    public String getNj() {
        return nj;
    }

    public void setNj(String nj) {
        this.nj = nj;
    }

    @Column(nullable = false, length = 10, name = "ssxx")
    public String getXxdm() {
        return xxdm;
    }

    public void setXxdm(String xxdm) {
        this.xxdm = xxdm;
    }

    @Column(nullable = true, length = 6)
    public String getZym() {
        return zym;
    }

    public void setZym(String zym) {
        this.zym = zym;
    }

    @Column(nullable = true, length = 10)
    public String getSsnj() {
        return ssnj;
    }

    public void setSsnj(String ssnj) {
        this.ssnj = ssnj;
    }

    @Column(nullable = true, precision = 3, scale = 1)
    public BigDecimal getXz() {
        return xz;
    }

    public void setXz(BigDecimal xz) {
        this.xz = xz;
    }

    @Type(type = "lab.s2jh.biz.core.hib.BooleanStrUserType")
    @Column(nullable = false, length = 1)
    public Boolean getSfssmzsyjxb() {
        return sfssmzsyjxb;
    }

    public void setSfssmzsyjxb(Boolean sfssmzsyjxb) {
        this.sfssmzsyjxb = sfssmzsyjxb;
    }

    @Column(nullable = true, length = 1)
    public String getSyjxmsm() {
        return syjxmsm;
    }

    public void setSyjxmsm(String syjxmsm) {
        this.syjxmsm = syjxmsm;
    }

    @Column(nullable = true, length = 3)
    public String getSyjxssmzyym() {
        return syjxssmzyym;
    }

    public void setSyjxssmzyym(String syjxssmzyym) {
        this.syjxssmzyym = syjxssmzyym;
    }

    @Column(nullable = true, length = 20)
    public String getBzrgh() {
        return bzrgh;
    }

    public void setBzrgh(String bzrgh) {
        this.bzrgh = bzrgh;
    }
}
